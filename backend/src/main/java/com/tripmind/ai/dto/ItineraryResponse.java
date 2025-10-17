package com.tripmind.ai.dto;

import java.util.List;

/**
 * Respuesta principal del itinerario generado
 */
public class ItineraryResponse {
    private String destination;
    private String startDate;
    private String endDate;
    private int travelers;
    private String budget;
    private List<String> interests;
    private List<DayItinerary> days;
    private Accommodation accommodation;
    private double totalEstimatedCost;
    private String currency;
    private List<String> travelTips;

    // Constructors
    public ItineraryResponse() {}

    public ItineraryResponse(String destination, String startDate, String endDate, 
                           int travelers, String budget, List<String> interests,
                           List<DayItinerary> days, Accommodation accommodation,
                           double totalEstimatedCost, String currency, List<String> travelTips) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.travelers = travelers;
        this.budget = budget;
        this.interests = interests;
        this.days = days;
        this.accommodation = accommodation;
        this.totalEstimatedCost = totalEstimatedCost;
        this.currency = currency;
        this.travelTips = travelTips;
    }

    // Getters and Setters
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public int getTravelers() { return travelers; }
    public void setTravelers(int travelers) { this.travelers = travelers; }

    public String getBudget() { return budget; }
    public void setBudget(String budget) { this.budget = budget; }

    public List<String> getInterests() { return interests; }
    public void setInterests(List<String> interests) { this.interests = interests; }

    public List<DayItinerary> getDays() { return days; }
    public void setDays(List<DayItinerary> days) { this.days = days; }

    public Accommodation getAccommodation() { return accommodation; }
    public void setAccommodation(Accommodation accommodation) { this.accommodation = accommodation; }

    public double getTotalEstimatedCost() { return totalEstimatedCost; }
    public void setTotalEstimatedCost(double totalEstimatedCost) { this.totalEstimatedCost = totalEstimatedCost; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public List<String> getTravelTips() { return travelTips; }
    public void setTravelTips(List<String> travelTips) { this.travelTips = travelTips; }
}
