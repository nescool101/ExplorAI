package com.tripmind.ai.dto;

/**
 * Actividad individual en el itinerario
 */
public class Activity {
    private String name;
    private String description;
    private String time;
    private String duration;
    private String location;
    private String category;
    private double cost;
    private String currency;
    private String icon;
    private String bookingUrl;
    private String notes;

    // Constructors
    public Activity() {}

    public Activity(String name, String description, String time, String duration,
                   String location, String category, double cost, String currency,
                   String icon, String bookingUrl, String notes) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.duration = duration;
        this.location = location;
        this.category = category;
        this.cost = cost;
        this.currency = currency;
        this.icon = icon;
        this.bookingUrl = bookingUrl;
        this.notes = notes;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getBookingUrl() { return bookingUrl; }
    public void setBookingUrl(String bookingUrl) { this.bookingUrl = bookingUrl; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
