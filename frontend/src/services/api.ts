import axios from 'axios';

// Base API configuration (use Vite dev proxy in development)
const API_BASE_URL = '/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// TypeScript interfaces matching backend DTOs
export interface ItineraryRequest {
  destination: string;
  startDate: string;
  endDate: string;
  travelers: number;
  budget: string;
  interests: string[];
}

export interface Activity {
  name: string;
  description: string;
  time: string;
  duration: string;
  location: string;
  category: string;
  cost: number;
  currency: string;
  icon: string;
  bookingUrl: string;
  notes: string;
}

export interface Restaurant {
  name: string;
  cuisine: string;
  description: string;
  address: string;
  phone: string;
  priceRange: string;
  rating: number;
  timeSlot: string;
  bookingUrl: string;
  notes: string;
}

export interface Accommodation {
  name: string;
  type: string;
  description: string;
  address: string;
  phone: string;
  rating: number;
  priceRange: string;
  nightlyRate: number;
  currency: string;
  amenities: string;
  bookingUrl: string;
  checkIn: string;
  checkOut: string;
}

export interface DayItinerary {
  dayNumber: number;
  date: string;
  dayTitle: string;
  morningActivities: Activity[];
  afternoonActivities: Activity[];
  eveningActivities: Activity[];
  restaurants: Restaurant[];
  daySummary: string;
  estimatedCost: number;
}

export interface ItineraryResponse {
  destination: string;
  startDate: string;
  endDate: string;
  travelers: number;
  budget: string;
  interests: string[];
  days: DayItinerary[];
  accommodation: Accommodation;
  totalEstimatedCost: number;
  currency: string;
  travelTips: string[];
}

// API functions
export const apiService = {
  /**
   * Generate itinerary based on user preferences
   */
  async generateItinerary(request: ItineraryRequest): Promise<ItineraryResponse> {
    try {
      const response = await apiClient.post<ItineraryResponse>('/generate-itinerary', request);
      return response.data;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(`Failed to generate itinerary: ${error.response?.data || error.message}`);
      }
      throw new Error('An unexpected error occurred while generating itinerary');
    }
  },

  /**
   * Validate flight information
   */
  async validateFlight(flightNumber: string, departure: string, arrival: string): Promise<boolean> {
    try {
      const response = await apiClient.post('/validate-flight', {
        flightNumber,
        departure,
        arrival,
      });
      return response.data;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(`Failed to validate flight: ${error.response?.data || error.message}`);
      }
      throw new Error('An unexpected error occurred while validating flight');
    }
  },

  /**
   * Save trip data
   */
  async saveTrip(userId: string, tripData: ItineraryResponse): Promise<string> {
    try {
      const response = await apiClient.post('/save-trip', {
        userId,
        tripData: JSON.stringify(tripData),
      });
      return response.data;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(`Failed to save trip: ${error.response?.data || error.message}`);
      }
      throw new Error('An unexpected error occurred while saving trip');
    }
  },

  /**
   * Get user preferences
   */
  async getUserPreferences(userId: string): Promise<string> {
    try {
      const response = await apiClient.get(`/user/preferences?userId=${userId}`);
      return response.data;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(`Failed to get preferences: ${error.response?.data || error.message}`);
      }
      throw new Error('An unexpected error occurred while getting preferences');
    }
  },
};

export default apiService;
