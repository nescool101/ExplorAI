package com.tripmind.ai.service;

import com.tripmind.ai.controller.ItineraryController.ItineraryRequest;
import com.tripmind.ai.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Servicio para la generación y gestión de itinerarios de viaje
 */
@Service
public class ItineraryService {

    /**
     * Genera un itinerario mock personalizado
     * @param request Datos del viaje
     * @return Itinerario generado
     */
    public ItineraryResponse generateMockItinerary(ItineraryRequest request) {
        int duration = calculateDuration(request.getStartDate(), request.getEndDate());
        List<DayItinerary> days = generateDayItineraries(request, duration);
        Accommodation accommodation = generateAccommodation(request);
        double totalCost = calculateTotalCost(days, accommodation, request.getBudget());
        List<String> travelTips = generateTravelTips(request);

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
            "USD",
            travelTips
        );
    }

    /**
     * Genera un itinerario personalizado usando IA
     * @param destination Destino del viaje
     * @param duration Duración en días
     * @param preferences Preferencias del usuario
     * @return Itinerario generado
     */
    public String generateItinerary(String destination, int duration, String preferences) {
        // TODO: Implementar integración con Spring AI
        // TODO: Integrar con APIs de clima, mapas, y datos locales
        return "Itinerario para " + destination + " (" + duration + " días) - " + preferences;
    }

    /**
     * Valida la disponibilidad de un vuelo
     * @param flightNumber Número de vuelo
     * @param departure Fecha de salida
     * @param arrival Fecha de llegada
     * @return Estado de validación
     */
    public boolean validateFlight(String flightNumber, String departure, String arrival) {
        // TODO: Integrar con API de Skyscanner o Amadeus
        return true; // Placeholder
    }

    /**
     * Guarda un viaje en la base de datos
     * @param userId ID del usuario
     * @param tripData Datos del viaje
     * @return ID del viaje guardado
     */
    public String saveTrip(String userId, String tripData) {
        // TODO: Implementar persistencia con Supabase/PostgreSQL
        return "trip_" + System.currentTimeMillis();
    }

    /**
     * Obtiene las preferencias del usuario
     * @param userId ID del usuario
     * @return Preferencias del usuario
     */
    public String getUserPreferences(String userId) {
        // TODO: Implementar obtención de preferencias desde base de datos
        return "Preferencias por defecto para usuario: " + userId;
    }

    // Métodos auxiliares para generar datos mock
    private int calculateDuration(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return (int) ChronoUnit.DAYS.between(start, end) + 1;
    }

    private List<DayItinerary> generateDayItineraries(ItineraryRequest request, int duration) {
        List<DayItinerary> days = new ArrayList<>();
        LocalDate currentDate = LocalDate.parse(request.getStartDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 1; i <= duration; i++) {
            String dayTitle = generateDayTitle(request.getDestination(), i, request.getInterests());
            List<Activity> morningActivities = generateMorningActivities(request, i);
            List<Activity> afternoonActivities = generateAfternoonActivities(request, i);
            List<Activity> eveningActivities = generateEveningActivities(request, i);
            List<Restaurant> restaurants = generateRestaurants(request, i);
            String daySummary = generateDaySummary(request, i, morningActivities, afternoonActivities, eveningActivities);
            double estimatedCost = calculateDayCost(morningActivities, afternoonActivities, eveningActivities, restaurants);

            days.add(new DayItinerary(
                i,
                currentDate.format(formatter),
                dayTitle,
                morningActivities,
                afternoonActivities,
                eveningActivities,
                restaurants,
                daySummary,
                estimatedCost
            ));

            currentDate = currentDate.plusDays(1);
        }

        return days;
    }

    private String generateDayTitle(String destination, int dayNumber, List<String> interests) {
        String[] titles = {
            "Arrival & First Impressions",
            "Cultural Exploration",
            "Local Cuisine & Markets",
            "Historic Sites & Landmarks",
            "Nature & Outdoor Adventures",
            "Art & Museums",
            "Shopping & Local Life",
            "Hidden Gems & Local Secrets"
        };
        
        if (dayNumber <= titles.length) {
            return titles[dayNumber - 1];
        }
        return "Day " + dayNumber + " Adventures";
    }

    private List<Activity> generateMorningActivities(ItineraryRequest request, int dayNumber) {
        List<Activity> activities = new ArrayList<>();
        String destination = request.getDestination().toLowerCase();
        
        if (dayNumber == 1) {
            activities.add(new Activity(
                "Airport Transfer & Hotel Check-in",
                "Transfer from airport to accommodation and check-in process",
                "09:00",
                "2 hours",
                "Airport to Hotel",
                "Transportation",
                50.0,
                "USD",
                "🚗",
                "",
                "Allow extra time for customs and immigration"
            ));
        } else {
            activities.add(new Activity(
                "Morning Coffee & Local Breakfast",
                "Start your day with authentic local breakfast and coffee",
                "08:00",
                "1.5 hours",
                "Local Café",
                "Food & Drink",
                15.0,
                "USD",
                "☕",
                "",
                "Try local specialties"
            ));
        }

        if (request.getInterests().contains("Culture")) {
            activities.add(new Activity(
                "Museum Visit",
                "Explore local history and culture at a renowned museum",
                "10:00",
                "2 hours",
                "City Museum",
                "Culture",
                25.0,
                "USD",
                "🏛️",
                "",
                "Book tickets in advance for popular exhibitions"
            ));
        }

        return activities;
    }

    private List<Activity> generateAfternoonActivities(ItineraryRequest request, int dayNumber) {
        List<Activity> activities = new ArrayList<>();
        
        if (request.getInterests().contains("Food")) {
            activities.add(new Activity(
                "Food Tour",
                "Guided tour of local food markets and street food",
                "14:00",
                "3 hours",
                "Local Markets",
                "Food & Drink",
                45.0,
                "USD",
                "🍜",
                "",
                "Come hungry and try everything!"
            ));
        }

        if (request.getInterests().contains("Adventure")) {
            activities.add(new Activity(
                "Adventure Activity",
                "Exciting outdoor adventure based on local geography",
                "16:00",
                "2 hours",
                "Adventure Location",
                "Adventure",
                80.0,
                "USD",
                "🏔️",
                "",
                "Wear appropriate clothing and bring water"
            ));
        }

        return activities;
    }

    private List<Activity> generateEveningActivities(ItineraryRequest request, int dayNumber) {
        List<Activity> activities = new ArrayList<>();
        
        activities.add(new Activity(
            "Sunset Viewpoint",
            "Watch the sunset from a beautiful local viewpoint",
            "18:30",
            "1 hour",
            "Scenic Viewpoint",
            "Sightseeing",
            0.0,
            "USD",
            "🌅",
            "",
            "Bring a camera for amazing photos"
        ));

        if (request.getInterests().contains("Nightlife")) {
            activities.add(new Activity(
                "Local Bar & Nightlife",
                "Experience the local nightlife scene",
                "20:00",
                "3 hours",
                "Downtown Area",
                "Nightlife",
                35.0,
                "USD",
                "🍻",
                "",
                "Check dress codes and age requirements"
            ));
        }

        return activities;
    }

    private List<Restaurant> generateRestaurants(ItineraryRequest request, int dayNumber) {
        List<Restaurant> restaurants = new ArrayList<>();
        
        restaurants.add(new Restaurant(
            "Local Restaurant",
            "Traditional Local Cuisine",
            "Authentic local dishes with traditional preparation methods",
            "123 Main Street, " + request.getDestination(),
            "+1-234-567-8900",
            "$$",
            4.5,
            "Lunch",
            "https://example.com/booking",
            "Reservations recommended for dinner"
        ));

        return restaurants;
    }

    private String generateDaySummary(ItineraryRequest request, int dayNumber, 
                                    List<Activity> morning, List<Activity> afternoon, List<Activity> evening) {
        return String.format("Day %d in %s offers a perfect blend of activities. " +
                           "Start with %d morning activities, enjoy %d afternoon experiences, " +
                           "and end with %d evening activities for a memorable day.",
                           dayNumber, request.getDestination(), 
                           morning.size(), afternoon.size(), evening.size());
    }

    private double calculateDayCost(List<Activity> morning, List<Activity> afternoon, 
                                  List<Activity> evening, List<Restaurant> restaurants) {
        double cost = 0.0;
        for (Activity activity : morning) cost += activity.getCost();
        for (Activity activity : afternoon) cost += activity.getCost();
        for (Activity activity : evening) cost += activity.getCost();
        return cost;
    }

    private Accommodation generateAccommodation(ItineraryRequest request) {
        String budget = request.getBudget();
        double baseRate = 150.0; // Default mid-range
        
        if ("budget".equals(budget)) baseRate = 80.0;
        else if ("luxury".equals(budget)) baseRate = 300.0;

        return new Accommodation(
            "Boutique Hotel " + request.getDestination(),
            "Hotel",
            "Beautiful boutique hotel in the heart of " + request.getDestination(),
            "456 Hotel Street, " + request.getDestination(),
            "+1-234-567-8901",
            4.8,
            budget.equals("budget") ? "$" : budget.equals("luxury") ? "$$$" : "$$",
            baseRate,
            "USD",
            "WiFi, Pool, Gym, Restaurant, Concierge",
            "https://example.com/hotel-booking",
            "15:00",
            "11:00"
        );
    }

    private double calculateTotalCost(List<DayItinerary> days, Accommodation accommodation, String budget) {
        double totalCost = 0.0;
        for (DayItinerary day : days) {
            totalCost += day.getEstimatedCost();
        }
        totalCost += accommodation.getNightlyRate() * days.size();
        return totalCost;
    }

    private List<String> generateTravelTips(ItineraryRequest request) {
        List<String> tips = new ArrayList<>();
        tips.add("Book accommodations in advance, especially during peak season");
        tips.add("Download offline maps and translation apps");
        tips.add("Carry local currency and a backup payment method");
        tips.add("Check visa requirements and travel documents");
        tips.add("Pack according to the local climate and culture");
        tips.add("Keep emergency contacts and embassy information handy");
        return tips;
    }
}
