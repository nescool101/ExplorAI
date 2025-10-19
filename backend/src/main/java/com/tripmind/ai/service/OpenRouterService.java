package com.tripmind.ai.service;

import com.tripmind.ai.controller.ItineraryController.ItineraryRequest;
import com.tripmind.ai.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Servicio para integración directa con OpenRouter API usando DeepSeek-R1
 */
@Service
public class OpenRouterService {

    @Value("${OPENROUTER_API_KEY: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}")
    private String apiKey;

    @Value("${OPENROUTER_MODEL:deepseek/deepseek-r1}")
    private String model;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public OpenRouterService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder
            .baseUrl("https://openrouter.ai/api/v1")
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .defaultHeader("Content-Type", "application/json")
            .build();
        this.objectMapper = objectMapper;
    }

    /**
     * Genera un itinerario usando OpenRouter API
     */
    public ItineraryResponse generateItinerary(ItineraryRequest request) {
        try {
            String prompt = buildPrompt(request);
            String response = callOpenRouter(prompt);
            return parseResponse(response, request);
        } catch (Exception e) {
            throw new RuntimeException("Error calling OpenRouter API: " + e.getMessage(), e);
        }
    }

    private String buildPrompt(ItineraryRequest request) {
        int duration = calculateDuration(request.getStartDate(), request.getEndDate());
        String interests = String.join(", ", request.getInterests());
        
        return String.format("""
            Generate a detailed travel itinerary for %s from %s to %s (%d days) for %d travelers with %s budget.
            
            Interests: %s
            
            Please provide a JSON response with this exact structure:
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
                  "title": "Day 1: Arrival and First Impressions",
                  "morningActivities": [
                    {
                      "name": "Airport Transfer",
                      "description": "Transfer from airport to hotel",
                      "time": "09:00",
                      "duration": "2 hours",
                      "location": "Airport to Hotel",
                      "category": "Transportation",
                      "cost": 50.0,
                      "currency": "USD",
                      "emoji": "🚗",
                      "bookingUrl": "",
                      "tips": "Allow extra time for customs"
                    }
                  ],
                  "afternoonActivities": [],
                  "eveningActivities": [],
                  "restaurants": [
                    {
                      "name": "Local Restaurant",
                      "cuisine": "Local Cuisine",
                      "description": "Authentic local dishes",
                      "address": "123 Main Street",
                      "phone": "+1-234-567-8900",
                      "priceRange": "$$",
                      "rating": 4.5,
                      "mealType": "Lunch",
                      "bookingUrl": "",
                      "tips": "Reservations recommended"
                    }
                  ],
                  "summary": "First day summary",
                  "estimatedCost": 100.0
                }
              ],
              "accommodation": {
                "name": "Hotel Name",
                "type": "Hotel",
                "description": "Comfortable hotel",
                "address": "456 Hotel Street",
                "phone": "+1-234-567-8901",
                "rating": 4.5,
                "priceRange": "$$",
                "nightlyRate": 150.0,
                "currency": "USD",
                "amenities": "WiFi, Pool",
                "bookingUrl": "",
                "checkIn": "15:00",
                "checkOut": "11:00"
              },
              "totalCost": 800.0,
              "currency": "USD",
              "travelTips": [
                "Book accommodations in advance",
                "Download offline maps",
                "Carry local currency"
              ]
            }
            
            Make it realistic and detailed for %s.
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
            request.getInterests().toString(),
            request.getStartDate(),
            request.getDestination()
        );
    }

    private String callOpenRouter(String prompt) {
        Map<String, Object> requestBody = Map.of(
            "model", model,
            "messages", List.of(
                Map.of("role", "user", "content", prompt)
            ),
            "max_tokens", 4000,
            "temperature", 0.7
        );

        return webClient.post()
            .uri("/chat/completions")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    private ItineraryResponse parseResponse(String response, ItineraryRequest request) {
        try {
            JsonNode root = objectMapper.readTree(response);
            String content = root.get("choices").get(0).get("message").get("content").asText();
            
            // Extract JSON from the response
            String jsonContent = extractJsonFromContent(content);
            JsonNode itineraryData = objectMapper.readTree(jsonContent);

            // Parse the itinerary
            List<DayItinerary> days = parseDays(itineraryData.get("days"));
            Accommodation accommodation = parseAccommodation(itineraryData.get("accommodation"));
            double totalCost = itineraryData.get("totalCost").asDouble();
            String currency = itineraryData.get("currency").asText("USD");
            List<String> travelTips = parseTravelTips(itineraryData.get("travelTips"));

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
            throw new RuntimeException("Error parsing AI response: " + e.getMessage(), e);
        }
    }

    private String extractJsonFromContent(String content) {
        int start = content.indexOf("{");
        int end = content.lastIndexOf("}") + 1;
        
        if (start != -1 && end > start) {
            return content.substring(start, end);
        }
        
        return content;
    }

    private List<DayItinerary> parseDays(JsonNode daysNode) {
        List<DayItinerary> days = new ArrayList<>();
        if (daysNode != null && daysNode.isArray()) {
            for (JsonNode dayNode : daysNode) {
                days.add(parseDay(dayNode));
            }
        }
        return days;
    }

    private DayItinerary parseDay(JsonNode dayNode) {
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
            return new Accommodation(
                "Default Hotel", "Hotel", "Comfortable accommodation",
                "123 Main Street", "+1-234-567-8900", 4.0, "$$",
                100.0, "USD", "WiFi, Pool", "", "15:00", "11:00"
            );
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

    private int calculateDuration(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return (int) ChronoUnit.DAYS.between(start, end) + 1;
    }
}
