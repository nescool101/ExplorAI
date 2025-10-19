import React from 'react';
import type { ItineraryResponse, DayItinerary, Activity, Restaurant, Accommodation } from '../services/api';

interface ItineraryDisplayProps {
  itinerary: ItineraryResponse;
}

export const ItineraryDisplay: React.FC<ItineraryDisplayProps> = ({ itinerary }) => {
  const formatCurrency = (amount: number, currency: string) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: currency,
    }).format(amount);
  };

  const ActivityCard: React.FC<{ activity: Activity }> = ({ activity }) => (
    <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200 mb-3">
      <div className="flex items-start space-x-3">
        <div className="text-2xl">{activity.icon}</div>
        <div className="flex-1">
          <div className="flex items-center justify-between">
            <h4 className="font-semibold text-gray-900">{activity.name}</h4>
            <span className="text-sm font-medium text-green-600">
              {formatCurrency(activity.cost, activity.currency)}
            </span>
          </div>
          <p className="text-sm text-gray-600 mt-1">{activity.description}</p>
          <div className="flex items-center space-x-4 mt-2 text-xs text-gray-500">
            <span>🕐 {activity.time}</span>
            <span>⏱️ {activity.duration}</span>
            <span>📍 {activity.location}</span>
          </div>
          {activity.notes && (
            <p className="text-xs text-blue-600 mt-2 italic">{activity.notes}</p>
          )}
        </div>
      </div>
    </div>
  );

  const RestaurantCard: React.FC<{ restaurant: Restaurant }> = ({ restaurant }) => (
    <div className="bg-orange-50 rounded-lg p-4 border border-orange-200">
      <div className="flex items-start justify-between">
        <div className="flex-1">
          <h4 className="font-semibold text-gray-900">{restaurant.name}</h4>
          <p className="text-sm text-orange-700 font-medium">{restaurant.cuisine}</p>
          <p className="text-sm text-gray-600 mt-1">{restaurant.description}</p>
          <div className="flex items-center space-x-4 mt-2 text-xs text-gray-500">
            <span>⭐ {restaurant.rating}/5</span>
            <span>💰 {restaurant.priceRange}</span>
            <span>🕐 {restaurant.timeSlot}</span>
          </div>
          <p className="text-xs text-gray-500 mt-1">📍 {restaurant.address}</p>
          {restaurant.notes && (
            <p className="text-xs text-orange-600 mt-2 italic">{restaurant.notes}</p>
          )}
        </div>
      </div>
    </div>
  );

  const AccommodationCard: React.FC<{ accommodation: Accommodation }> = ({ accommodation }) => (
    <div className="bg-blue-50 rounded-lg p-6 border border-blue-200">
      <div className="flex items-start justify-between mb-4">
        <div className="flex-1">
          <h3 className="text-xl font-bold text-gray-900">{accommodation.name}</h3>
          <p className="text-sm text-blue-700 font-medium">{accommodation.type}</p>
          <p className="text-gray-600 mt-2">{accommodation.description}</p>
        </div>
        <div className="text-right">
          <div className="text-2xl font-bold text-green-600">
            {formatCurrency(accommodation.nightlyRate, accommodation.currency)}
          </div>
          <div className="text-sm text-gray-500">per night</div>
        </div>
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
        <div>
          <p className="font-medium text-gray-900">📍 Address</p>
          <p className="text-gray-600">{accommodation.address}</p>
        </div>
        <div>
          <p className="font-medium text-gray-900">⭐ Rating</p>
          <p className="text-gray-600">{accommodation.rating}/5</p>
        </div>
        <div>
          <p className="font-medium text-gray-900">🏨 Amenities</p>
          <p className="text-gray-600">{accommodation.amenities}</p>
        </div>
        <div>
          <p className="font-medium text-gray-900">🕐 Check-in/out</p>
          <p className="text-gray-600">{accommodation.checkIn} / {accommodation.checkOut}</p>
        </div>
      </div>
    </div>
  );

  const DayCard: React.FC<{ day: DayItinerary }> = ({ day }) => (
    <div className="bg-white rounded-xl shadow-lg border border-gray-200 overflow-hidden">
      <div className="bg-gradient-to-r from-blue-500 to-purple-600 text-white p-6">
        <div className="flex items-center justify-between">
          <div>
            <h2 className="text-2xl font-bold">Day {day.dayNumber}</h2>
            <p className="text-blue-100">{day.dayTitle}</p>
            <p className="text-sm text-blue-200">{day.date}</p>
          </div>
          <div className="text-right">
            <div className="text-xl font-bold">{formatCurrency(day.estimatedCost, 'USD')}</div>
            <div className="text-sm text-blue-200">estimated cost</div>
          </div>
        </div>
      </div>
      
      <div className="p-6">
        <p className="text-gray-600 mb-6 italic">{day.daySummary}</p>
        
        {day.morningActivities.length > 0 && (
          <div className="mb-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-3 flex items-center">
              🌅 Morning
            </h3>
            {day.morningActivities.map((activity, index) => (
              <ActivityCard key={index} activity={activity} />
            ))}
          </div>
        )}

        {day.afternoonActivities.length > 0 && (
          <div className="mb-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-3 flex items-center">
              ☀️ Afternoon
            </h3>
            {day.afternoonActivities.map((activity, index) => (
              <ActivityCard key={index} activity={activity} />
            ))}
          </div>
        )}

        {day.eveningActivities.length > 0 && (
          <div className="mb-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-3 flex items-center">
              🌙 Evening
            </h3>
            {day.eveningActivities.map((activity, index) => (
              <ActivityCard key={index} activity={activity} />
            ))}
          </div>
        )}

        {day.restaurants.length > 0 && (
          <div className="mb-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-3 flex items-center">
              🍽️ Dining
            </h3>
            {day.restaurants.map((restaurant, index) => (
              <RestaurantCard key={index} restaurant={restaurant} />
            ))}
          </div>
        )}
      </div>
    </div>
  );

  return (
    <div className="space-y-8">
      {/* Trip Overview */}
      <div className="bg-gradient-to-r from-green-500 to-blue-600 text-white rounded-xl p-6">
        <h1 className="text-3xl font-bold mb-2">{itinerary.destination}</h1>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
          <div>
            <p className="text-green-100">📅 Duration</p>
            <p className="font-semibold">{itinerary.days.length} days</p>
          </div>
          <div>
            <p className="text-green-100">👥 Travelers</p>
            <p className="font-semibold">{itinerary.travelers} people</p>
          </div>
          <div>
            <p className="text-green-100">💰 Budget</p>
            <p className="font-semibold capitalize">{itinerary.budget}</p>
          </div>
        </div>
        <div className="mt-4 pt-4 border-t border-green-400">
          <div className="flex items-center justify-between">
            <span className="text-green-100">Total Estimated Cost:</span>
            <span className="text-2xl font-bold">
              {formatCurrency(itinerary.totalEstimatedCost, itinerary.currency)}
            </span>
          </div>
        </div>
      </div>

      {/* Accommodation */}
      <div>
        <h2 className="text-2xl font-bold text-gray-900 mb-4">🏨 Accommodation</h2>
        <AccommodationCard accommodation={itinerary.accommodation} />
      </div>

      {/* Daily Itinerary */}
      <div>
        <h2 className="text-2xl font-bold text-gray-900 mb-6">📅 Daily Itinerary</h2>
        <div className="space-y-8">
          {itinerary.days.map((day) => (
            <DayCard key={day.dayNumber} day={day} />
          ))}
        </div>
      </div>

      {/* Travel Tips */}
      {itinerary.travelTips.length > 0 && (
        <div className="bg-yellow-50 rounded-xl p-6 border border-yellow-200">
          <h2 className="text-xl font-bold text-gray-900 mb-4 flex items-center">
            💡 Travel Tips
          </h2>
          <ul className="space-y-2">
            {itinerary.travelTips.map((tip, index) => (
              <li key={index} className="flex items-start space-x-2">
                <span className="text-yellow-600 mt-1">•</span>
                <span className="text-gray-700">{tip}</span>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};
