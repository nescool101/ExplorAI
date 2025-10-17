package com.tripmind.ai.dto;

import java.util.List;

/**
 * Itinerario de un día específico
 */
public class DayItinerary {
    private int dayNumber;
    private String date;
    private String dayTitle;
    private List<Activity> morningActivities;
    private List<Activity> afternoonActivities;
    private List<Activity> eveningActivities;
    private List<Restaurant> restaurants;
    private String daySummary;
    private double estimatedCost;

    // Constructors
    public DayItinerary() {}

    public DayItinerary(int dayNumber, String date, String dayTitle,
                       List<Activity> morningActivities, List<Activity> afternoonActivities,
                       List<Activity> eveningActivities, List<Restaurant> restaurants,
                       String daySummary, double estimatedCost) {
        this.dayNumber = dayNumber;
        this.date = date;
        this.dayTitle = dayTitle;
        this.morningActivities = morningActivities;
        this.afternoonActivities = afternoonActivities;
        this.eveningActivities = eveningActivities;
        this.restaurants = restaurants;
        this.daySummary = daySummary;
        this.estimatedCost = estimatedCost;
    }

    // Getters and Setters
    public int getDayNumber() { return dayNumber; }
    public void setDayNumber(int dayNumber) { this.dayNumber = dayNumber; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDayTitle() { return dayTitle; }
    public void setDayTitle(String dayTitle) { this.dayTitle = dayTitle; }

    public List<Activity> getMorningActivities() { return morningActivities; }
    public void setMorningActivities(List<Activity> morningActivities) { this.morningActivities = morningActivities; }

    public List<Activity> getAfternoonActivities() { return afternoonActivities; }
    public void setAfternoonActivities(List<Activity> afternoonActivities) { this.afternoonActivities = afternoonActivities; }

    public List<Activity> getEveningActivities() { return eveningActivities; }
    public void setEveningActivities(List<Activity> eveningActivities) { this.eveningActivities = eveningActivities; }

    public List<Restaurant> getRestaurants() { return restaurants; }
    public void setRestaurants(List<Restaurant> restaurants) { this.restaurants = restaurants; }

    public String getDaySummary() { return daySummary; }
    public void setDaySummary(String daySummary) { this.daySummary = daySummary; }

    public double getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(double estimatedCost) { this.estimatedCost = estimatedCost; }
}
