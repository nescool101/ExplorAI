package com.tripmind.ai.dto;

/**
 * Restaurante recomendado
 */
public class Restaurant {
    private String name;
    private String cuisine;
    private String description;
    private String address;
    private String phone;
    private String priceRange;
    private double rating;
    private String timeSlot;
    private String bookingUrl;
    private String notes;

    // Constructors
    public Restaurant() {}

    public Restaurant(String name, String cuisine, String description, String address,
                     String phone, String priceRange, double rating, String timeSlot,
                     String bookingUrl, String notes) {
        this.name = name;
        this.cuisine = cuisine;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.priceRange = priceRange;
        this.rating = rating;
        this.timeSlot = timeSlot;
        this.bookingUrl = bookingUrl;
        this.notes = notes;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPriceRange() { return priceRange; }
    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public String getBookingUrl() { return bookingUrl; }
    public void setBookingUrl(String bookingUrl) { this.bookingUrl = bookingUrl; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
