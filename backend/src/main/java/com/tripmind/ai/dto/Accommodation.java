package com.tripmind.ai.dto;

/**
 * Alojamiento recomendado
 */
public class Accommodation {
    private String name;
    private String type;
    private String description;
    private String address;
    private String phone;
    private double rating;
    private String priceRange;
    private double nightlyRate;
    private String currency;
    private String amenities;
    private String bookingUrl;
    private String checkIn;
    private String checkOut;

    // Constructors
    public Accommodation() {}

    public Accommodation(String name, String type, String description, String address,
                        String phone, double rating, String priceRange, double nightlyRate,
                        String currency, String amenities, String bookingUrl,
                        String checkIn, String checkOut) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.rating = rating;
        this.priceRange = priceRange;
        this.nightlyRate = nightlyRate;
        this.currency = currency;
        this.amenities = amenities;
        this.bookingUrl = bookingUrl;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getPriceRange() { return priceRange; }
    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }

    public double getNightlyRate() { return nightlyRate; }
    public void setNightlyRate(double nightlyRate) { this.nightlyRate = nightlyRate; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }

    public String getBookingUrl() { return bookingUrl; }
    public void setBookingUrl(String bookingUrl) { this.bookingUrl = bookingUrl; }

    public String getCheckIn() { return checkIn; }
    public void setCheckIn(String checkIn) { this.checkIn = checkIn; }

    public String getCheckOut() { return checkOut; }
    public void setCheckOut(String checkOut) { this.checkOut = checkOut; }
}
