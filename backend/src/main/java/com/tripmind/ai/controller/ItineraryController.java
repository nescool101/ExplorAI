package com.tripmind.ai.controller;

import com.tripmind.ai.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la generación de itinerarios de viaje
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4321")
public class ItineraryController {

    @Autowired
    private ItineraryService itineraryService;

    /**
     * Genera un itinerario de viaje personalizado
     * @param request Datos del viaje (destino, fechas, preferencias)
     * @return Itinerario generado por IA
     */
    @PostMapping("/generate-itinerary")
    public ResponseEntity<?> generateItinerary(@RequestBody ItineraryRequest request) {
        try {
            // TODO: Implementar lógica de generación de itinerario con Spring AI
            return ResponseEntity.ok("Itinerario generado para: " + request.getDestination());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al generar itinerario: " + e.getMessage());
        }
    }

    /**
     * Valida la disponibilidad de vuelos
     * @param request Datos del vuelo a validar
     * @return Estado de validación del vuelo
     */
    @PostMapping("/validate-flight")
    public ResponseEntity<?> validateFlight(@RequestBody FlightRequest request) {
        try {
            // TODO: Implementar validación de vuelos con APIs externas
            return ResponseEntity.ok("Vuelo validado: " + request.getFlightNumber());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al validar vuelo: " + e.getMessage());
        }
    }

    /**
     * Guarda un viaje del usuario
     * @param request Datos del viaje a guardar
     * @return Confirmación de guardado
     */
    @PostMapping("/save-trip")
    public ResponseEntity<?> saveTrip(@RequestBody TripRequest request) {
        try {
            // TODO: Implementar persistencia de viajes
            return ResponseEntity.ok("Viaje guardado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al guardar viaje: " + e.getMessage());
        }
    }

    /**
     * Obtiene las preferencias del usuario
     * @param userId ID del usuario
     * @return Preferencias del usuario
     */
    @GetMapping("/user/preferences")
    public ResponseEntity<?> getUserPreferences(@RequestParam String userId) {
        try {
            // TODO: Implementar obtención de preferencias del usuario
            return ResponseEntity.ok("Preferencias del usuario: " + userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener preferencias: " + e.getMessage());
        }
    }

    // Clases internas para requests
    public static class ItineraryRequest {
        private String destination;
        private String startDate;
        private String endDate;
        private String preferences;

        // Getters y setters
        public String getDestination() { return destination; }
        public void setDestination(String destination) { this.destination = destination; }
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public String getPreferences() { return preferences; }
        public void setPreferences(String preferences) { this.preferences = preferences; }
    }

    public static class FlightRequest {
        private String flightNumber;
        private String departure;
        private String arrival;

        // Getters y setters
        public String getFlightNumber() { return flightNumber; }
        public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
        public String getDeparture() { return departure; }
        public void setDeparture(String departure) { this.departure = departure; }
        public String getArrival() { return arrival; }
        public void setArrival(String arrival) { this.arrival = arrival; }
    }

    public static class TripRequest {
        private String userId;
        private String tripData;

        // Getters y setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getTripData() { return tripData; }
        public void setTripData(String tripData) { this.tripData = tripData; }
    }
}
