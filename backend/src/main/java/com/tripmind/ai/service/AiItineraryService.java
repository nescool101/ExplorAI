package com.tripmind.ai.service;

import com.tripmind.ai.controller.ItineraryController.ItineraryRequest;
import com.tripmind.ai.dto.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Servicio de IA para la generación de itinerarios usando OpenRouter con DeepSeek-R1
 */
@Service
public class AiItineraryService {

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private ObjectMapper objectMapper;

    private final ChatClient chatClient;

    public AiItineraryService(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    /**
     * Genera un itinerario personalizado usando IA
     * @param request Datos del viaje
     * @return Itinerario generado por IA
     */
    public ItineraryResponse generateAiItinerary(ItineraryRequest request) {
        try {
            String prompt = buildItineraryPrompt(request);
            String aiResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

            return parseAiResponse(aiResponse, request);
        } catch (Exception e) {
            // Fallback to mock data if AI fails
            return generateFallbackItinerary(request);
        }
    }

    private String buildItineraryPrompt(ItineraryRequest request) {
        int duration = calculateDuration(request.getStartDate(), request.getEndDate());
        String interests = String.join(", ", request.getInterests());
        
        return String.format("""
            Generate a detailed travel itinerary for %s from %s to %s (%d days) for %d travelers with %s budget.
            
            Interests: %s
            
            Please provide a JSON response with the following structure:
            {
              "destination": "%s",
              "startDate": "%s",
              "endDate": "%s",
              "travelers": %d,
              "budget": "%s",
              "interests": %s,
              "days": [
                {
                  "dayNumber": 1,
                  "date": "%s",
                  "title": "Day 1 Title",
                  "morningActivities": [
                    {
                      "name": "Activity Name",
                      "description": "Detailed description",
                      "time": "09:00",
                      "duration": "2 hours",
                      "location": "Location name",
                      "category": "Culture",
                      "cost": 25.0,
                      "currency": "USD",
                      "emoji": "🏛️",
                      "bookingUrl": "",
                      "tips": "Helpful tips"
                    }
                  ],
                  "afternoonActivities": [],
                  "eveningActivities": [],
                  "restaurants": [
                    {
                      "name": "Restaurant Name",
                      "cuisine": "Local Cuisine",
                      "description": "Restaurant description",
                      "address": "Full address",
                      "phone": "+1234567890",
                      "priceRange": "$$",
                      "rating": 4.5,
                      "mealType": "Lunch",
                      "bookingUrl": "https://example.com",
                      "tips": "Reservation tips"
                    }
                  ],
                  "summary": "Day summary",
                  "estimatedCost": 150.0
                }
              ],
              "accommodation": {
                "name": "Hotel Name",
                "type": "Hotel",
                "description": "Hotel description",
                "address": "Hotel address",
                "phone": "+1234567890",
                "rating": 4.8,
                "priceRange": "$$",
                "nightlyRate": 150.0,
                "currency": "USD",
                "amenities": "WiFi, Pool, Gym",
                "bookingUrl": "https://example.com",
                "checkIn": "15:00",
                "checkOut": "11:00"
              },
              "totalCost": 1200.0,
              "currency": "USD",
              "travelTips": [
                "Tip 1",
                "Tip 2",
                "Tip 3"
              ]
            }
            
            Make the itinerary realistic, detailed, and personalized based on the interests and budget.
            Include specific locations, times, costs, and practical tips.
            """, 
            request.getDestination(),
            request.getStartDate(),
            request.getEndDate(),
            duration,
            request.getTravelers(),
            request.getBudget(),
            interests,
            request.getDestination(),
            request.getStartDate(),
            request.getEndDate(),
            request.getTravelers(),
            request.getBudget(),
            objectMapper.writeValueAsString(request.getInterests()),
            request.getStartDate()
        );
    }

    private ItineraryResponse parseAiResponse(String aiResponse, ItineraryRequest request) {
        try {
            // Clean the response to extract JSON
            String jsonResponse = extractJsonFromResponse(aiResponse);
            JsonNode root = objectMapper.readTree(jsonResponse);

            // Parse days
            List<DayItinerary> days = new ArrayList<>();
            JsonNode daysNode = root.get("days");
            if (daysNode != null && daysNode.isArray()) {
                for (JsonNode dayNode : daysNode) {
                    days.add(parseDayItinerary(dayNode));
                }
            }

            // Parse accommodation
            Accommodation accommodation = parseAccommodation(root.get("accommodation"));

            // Parse other fields
            double totalCost = root.get("totalCost").asDouble();
            String currency = root.get("currency").asText("USD");
            List<String> travelTips = parseTravelTips(root.get("travelTips"));

            return new ItineraryResponse(
                request.getDestination(),
                request.getStartDate(),
                request.getEndDate(),
                request.getTravelers(),
                request.getBudget(),
                request.getInterests(),
                days,
                accommodation,
                totalCost,
                currency,
                travelTips
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }

    private String extractJsonFromResponse(String response) {
        // Find JSON content between ```json and ``` or just look for { ... }
        int start = response.indexOf("{");
        int end = response.lastIndexOf("}") + 1;
        
        if (start != -1 && end > start) {
            return response.substring(start, end);
        }
        
        return response;
    }

    private DayItinerary parseDayItinerary(JsonNode dayNode) {
        int dayNumber = dayNode.get("dayNumber").asInt();
        String date = dayNode.get("date").asText();
        String title = dayNode.get("title").asText();
        
        List<Activity> morningActivities = parseActivities(dayNode.get("morningActivities"));
        List<Activity> afternoonActivities = parseActivities(dayNode.get("afternoonActivities"));
        List<Activity> eveningActivities = parseActivities(dayNode.get("eveningActivities"));
        List<Restaurant> restaurants = parseRestaurants(dayNode.get("restaurants"));
        
        String summary = dayNode.get("summary").asText();
        double estimatedCost = dayNode.get("estimatedCost").asDouble();

        return new DayItinerary(
            dayNumber, date, title,
            morningActivities, afternoonActivities, eveningActivities,
            restaurants, summary, estimatedCost
        );
    }

    private List<Activity> parseActivities(JsonNode activitiesNode) {
        List<Activity> activities = new ArrayList<>();
        if (activitiesNode != null && activitiesNode.isArray()) {
            for (JsonNode activityNode : activitiesNode) {
                activities.add(new Activity(
                    activityNode.get("name").asText(),
                    activityNode.get("description").asText(),
                    activityNode.get("time").asText(),
                    activityNode.get("duration").asText(),
                    activityNode.get("location").asText(),
                    activityNode.get("category").asText(),
                    activityNode.get("cost").asDouble(),
                    activityNode.get("currency").asText("USD"),
                    activityNode.get("emoji").asText("📍"),
                    activityNode.get("bookingUrl").asText(""),
                    activityNode.get("tips").asText("")
                ));
            }
        }
        return activities;
    }

    private List<Restaurant> parseRestaurants(JsonNode restaurantsNode) {
        List<Restaurant> restaurants = new ArrayList<>();
        if (restaurantsNode != null && restaurantsNode.isArray()) {
            for (JsonNode restaurantNode : restaurantsNode) {
                restaurants.add(new Restaurant(
                    restaurantNode.get("name").asText(),
                    restaurantNode.get("cuisine").asText(),
                    restaurantNode.get("description").asText(),
                    restaurantNode.get("address").asText(),
                    restaurantNode.get("phone").asText(),
                    restaurantNode.get("priceRange").asText(),
                    restaurantNode.get("rating").asDouble(),
                    restaurantNode.get("mealType").asText(),
                    restaurantNode.get("bookingUrl").asText(""),
                    restaurantNode.get("tips").asText("")
                ));
            }
        }
        return restaurants;
    }

    private Accommodation parseAccommodation(JsonNode accommodationNode) {
        if (accommodationNode == null) {
            return generateDefaultAccommodation();
        }
        
        return new Accommodation(
            accommodationNode.get("name").asText(),
            accommodationNode.get("type").asText(),
            accommodationNode.get("description").asText(),
            accommodationNode.get("address").asText(),
            accommodationNode.get("phone").asText(),
            accommodationNode.get("rating").asDouble(),
            accommodationNode.get("priceRange").asText(),
            accommodationNode.get("nightlyRate").asDouble(),
            accommodationNode.get("currency").asText("USD"),
            accommodationNode.get("amenities").asText(),
            accommodationNode.get("bookingUrl").asText(""),
            accommodationNode.get("checkIn").asText("15:00"),
            accommodationNode.get("checkOut").asText("11:00")
        );
    }

    private List<String> parseTravelTips(JsonNode tipsNode) {
        List<String> tips = new ArrayList<>();
        if (tipsNode != null && tipsNode.isArray()) {
            for (JsonNode tipNode : tipsNode) {
                tips.add(tipNode.asText());
            }
        }
        return tips;
    }

    private ItineraryResponse generateFallbackItinerary(ItineraryRequest request) {
        // Fallback to mock data if AI fails
        ItineraryService mockService = new ItineraryService();
        return mockService.generateMockItinerary(request);
    }

    private Accommodation generateDefaultAccommodation() {
        return new Accommodation(
            "Default Hotel",
            "Hotel",
            "Comfortable accommodation",
            "123 Main Street",
            "+1-234-567-8900",
            4.0,
            "$$",
            100.0,
            "USD",
            "WiFi, Pool",
            "",
            "15:00",
            "11:00"
        );
    }

    private int calculateDuration(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return (int) ChronoUnit.DAYS.between(start, end) + 1;
    }
}
