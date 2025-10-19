import React, { useState } from 'react';
import { apiService } from '../services/api';
import type { ItineraryRequest, ItineraryResponse } from '../services/api';

interface HeroProps {
  title: string;
  subtitle: string;
  ctaText: string;
  onCtaClick: () => void;
  onPromptClick?: (prompt: string) => void;
}

const examplePrompts = [
  "1 week in Rome for food and history",
  "4 days in Tokyo for culture and sushi", 
  "5 days in Barcelona with art and beaches",
  "Weekend in Paris with museums and cafés"
];

export const Hero: React.FC<HeroProps> = ({ title, subtitle, ctaText, onCtaClick, onPromptClick }) => {
  const [activeTab, setActiveTab] = useState<'quick' | 'detailed'>('quick');
  const [prompt, setPrompt] = useState<string>("");
  const [startDate, setStartDate] = useState<string>("");
  const [endDate, setEndDate] = useState<string>("");
  const [travelers, setTravelers] = useState<number>(1);
  const [budget, setBudget] = useState<string>("mid-range");
  const [interests, setInterests] = useState<string[]>([]);
  const [transportFlights, setTransportFlights] = useState<boolean>(true);
  const [transportTrains, setTransportTrains] = useState<boolean>(true);
  const [transportBoat, setTransportBoat] = useState<boolean>(false);
  const [isPlanning, setIsPlanning] = useState<boolean>(false);

  const parseDestinationAndDuration = (text: string): { destination: string; days: number } => {
    const weekRegex = /(1|one)\s*week/i;
    const daysRegex = /(\d+)\s*day/i;
    const destRegex = /in\s+([A-Za-z\s]+?)(?:\s+for|$)/i;
    
    const weekMatch = weekRegex.exec(text);
    const daysMatch = daysRegex.exec(text);
    const destMatch = destRegex.exec(text);
    
    const days = weekMatch ? 7 : (daysMatch ? Number.parseInt(daysMatch[1], 10) : 4);
    const destination = destMatch ? destMatch[1].trim() : (text.split(' in ')[1]?.split(' ')[0] || 'Rome');
    
    return { destination, days: Math.max(2, Math.min(14, days)) };
  };

  const handleInterestToggle = (interest: string) => {
    setInterests(prev => 
      prev.includes(interest) 
        ? prev.filter(i => i !== interest)
        : [...prev, interest]
    );
  };

  const generateItinerary = async () => {
    try {
      setIsPlanning(true);
      
      let destination: string;
      let start: Date;
      let end: Date;
      let finalInterests: string[] = [];

      if (activeTab === 'quick') {
        const base = prompt || examplePrompts[0];
        const parsed = parseDestinationAndDuration(base);
        destination = parsed.destination;
        start = new Date();
        end = new Date(start);
        end.setDate(start.getDate() + parsed.days - 1);
        
        if (/food|café|cafe|sushi|beach|wine/i.test(base)) finalInterests.push('Food');
        if (/history|art|museum|culture/i.test(base)) finalInterests.push('Culture');
        if (finalInterests.length === 0) finalInterests.push('Culture');
      } else {
        if (!startDate || !endDate) {
          alert('Please select start and end dates for detailed planning');
          return;
        }
        destination = prompt || 'Rome';
        start = new Date(startDate);
        end = new Date(endDate);
        finalInterests = interests.length > 0 ? interests : ['Culture', 'Food'];
      }

      const format = (d: Date) => d.toISOString().slice(0, 10);

      const request: ItineraryRequest = {
        destination,
        startDate: format(start),
        endDate: format(end),
        travelers: activeTab === 'detailed' ? travelers : 2,
        budget: activeTab === 'detailed' ? budget : 'mid-range',
        interests: finalInterests
      };

      const response: ItineraryResponse = await apiService.generateItinerary(request);
      if (typeof window !== 'undefined') {
        try {
          window.dispatchEvent(new CustomEvent('show-itinerary', { detail: response }));
        } catch {}
      }
      if (typeof globalThis !== 'undefined' && (globalThis as any).showItineraryModal) {
        (globalThis as any).showItineraryModal(response);
      } else if (typeof onCtaClick === 'function') {
        onCtaClick();
      }
    } catch (e) {
      console.error('Planning failed', e);
      onCtaClick();
    } finally {
      setIsPlanning(false);
    }
  };

  return (
    <div className="bg-gradient-to-br from-blue-600 via-purple-600 to-pink-600 text-white relative overflow-hidden">
      {/* Background decoration */}
      <div className="absolute inset-0 bg-black opacity-10"></div>
      <div className="absolute top-0 left-0 w-full h-full">
        <div className="absolute top-20 left-10 w-20 h-20 bg-white opacity-5 rounded-full"></div>
        <div className="absolute top-40 right-20 w-32 h-32 bg-white opacity-5 rounded-full"></div>
        <div className="absolute bottom-20 left-1/4 w-16 h-16 bg-white opacity-5 rounded-full"></div>
      </div>
      
      <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24">
        <div className="text-center mb-8">
          <h1 className="text-3xl md:text-5xl font-bold mb-4 leading-tight">
            {title}
          </h1>
          <p className="text-base md:text-lg mb-8 leading-relaxed">
            {subtitle}
          </p>
        </div>

        {/* Stats Section - Moved Higher */}
        <div className="text-center mb-12">
          <div className="inline-flex items-center space-x-8 bg-white/15 rounded-2xl px-8 py-4 backdrop-blur-sm border border-white/30">
            <div className="text-center">
              <div className="text-2xl font-bold">25,000+</div>
              <div className="text-sm text-blue-50">travelers planned trips</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold">3,500+</div>
              <div className="text-sm text-blue-50">itineraries generated</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold">4.8★</div>
              <div className="text-sm text-blue-50">average rating</div>
            </div>
          </div>
        </div>

        {/* Prompt Suggestions - Improved Visibility */}
        <div className="mt-8">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-3 max-w-6xl mx-auto">
            {examplePrompts.map((examplePrompt) => (
              <button
                key={examplePrompt}
                onClick={() => setPrompt(examplePrompt)}
                className="text-left p-4 bg-white/20 hover:bg-white/30 rounded-xl border border-white/30 hover:border-white/40 transition-all duration-300 hover:scale-105 backdrop-blur-sm shadow-lg"
              >
                <p className="text-sm text-white leading-relaxed">
                  {examplePrompt}
                </p>
              </button>
            ))}
          </div>

          {/* Tabbed Planner - Improved Visibility */}
          <div className="mt-8 max-w-4xl mx-auto bg-white/15 backdrop-blur-sm border border-white/30 rounded-2xl p-6 shadow-lg">
            {/* Tabs */}
            <div className="flex mb-6">
              <button
                onClick={() => setActiveTab('quick')}
                className={`px-6 py-3 rounded-lg font-medium transition-all duration-200 ${
                  activeTab === 'quick'
                    ? 'bg-white text-blue-600'
                    : 'text-white hover:bg-white/10'
                }`}
              >
                Quick Planning
              </button>
              <button
                onClick={() => setActiveTab('detailed')}
                className={`px-6 py-3 rounded-lg font-medium transition-all duration-200 ${
                  activeTab === 'detailed'
                    ? 'bg-white text-blue-600'
                    : 'text-white hover:bg-white/10'
                }`}
              >
                Detailed Planning
              </button>
            </div>

            {/* Tab Content */}
            {activeTab === 'quick' ? (
              <div className="space-y-4">
                <input
                  type="text"
                  value={prompt}
                  onChange={(e) => setPrompt(e.target.value)}
                  placeholder="Describe your trip (e.g., 1 week in Rome for food and history)"
                  className="w-full px-4 py-3 rounded-xl text-gray-900 placeholder-gray-500 border-0 focus:ring-2 focus:ring-blue-500"
                />
                <div className="text-center">
                  <button
                    onClick={generateItinerary}
                    disabled={isPlanning}
                    className="bg-white text-blue-600 px-8 py-3 rounded-xl font-bold hover:bg-gray-100 transition-all duration-300 shadow-lg disabled:opacity-60"
                  >
                    {isPlanning ? 'Planning…' : 'Start Planning'}
                  </button>
                </div>
              </div>
            ) : (
              <div className="space-y-6">
                <input
                  type="text"
                  value={prompt}
                  onChange={(e) => setPrompt(e.target.value)}
                  placeholder="Where do you want to go?"
                  className="w-full px-4 py-3 rounded-xl text-gray-900 placeholder-gray-500 border-0 focus:ring-2 focus:ring-blue-500"
                />
                
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label htmlFor="start-date" className="block text-sm text-blue-50 mb-2">Start Date</label>
                    <input
                      id="start-date"
                      type="date"
                      value={startDate}
                      onChange={(e) => setStartDate(e.target.value)}
                      className="w-full px-4 py-3 rounded-xl text-gray-900 border-0 focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                  <div>
                    <label htmlFor="end-date" className="block text-sm text-blue-50 mb-2">End Date</label>
                    <input
                      id="end-date"
                      type="date"
                      value={endDate}
                      onChange={(e) => setEndDate(e.target.value)}
                      className="w-full px-4 py-3 rounded-xl text-gray-900 border-0 focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label htmlFor="travelers" className="block text-sm text-blue-50 mb-2">Number of Travelers</label>
                    <input
                      id="travelers"
                      type="number"
                      min="1"
                      max="20"
                      value={travelers}
                      onChange={(e) => setTravelers(Number.parseInt(e.target.value, 10) || 1)}
                      className="w-full px-4 py-3 rounded-xl text-gray-900 border-0 focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                  <div>
                    <label htmlFor="budget" className="block text-sm text-blue-50 mb-2">Budget Range</label>
                    <select
                      id="budget"
                      value={budget}
                      onChange={(e) => setBudget(e.target.value)}
                      className="w-full px-4 py-3 rounded-xl text-gray-900 border-0 focus:ring-2 focus:ring-blue-500"
                    >
                      <option value="budget">Budget</option>
                      <option value="mid-range">Mid-range</option>
                      <option value="luxury">Luxury</option>
                    </select>
                  </div>
                </div>

                <div>
                  <label className="block text-sm text-blue-50 mb-3">Interests (select all that apply)</label>
                  <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
                    {['Adventure', 'Culture', 'Food', 'Nature', 'History', 'Nightlife', 'Shopping', 'Beach'].map((interest) => (
                      <label key={interest} className="flex items-center space-x-2 text-sm text-blue-50">
                        <input
                          type="checkbox"
                          checked={interests.includes(interest)}
                          onChange={() => handleInterestToggle(interest)}
                          className="rounded"
                        />
                        <span>{interest}</span>
                      </label>
                    ))}
                  </div>
                </div>

                <div className="flex items-center justify-center space-x-6 text-sm text-blue-50">
                  <label className="inline-flex items-center space-x-2">
                    <input 
                      type="checkbox" 
                      checked={transportFlights} 
                      onChange={(e) => setTransportFlights(e.target.checked)}
                      className="rounded"
                    />
                    <span>Flights</span>
                  </label>
                  <label className="inline-flex items-center space-x-2">
                    <input 
                      type="checkbox" 
                      checked={transportTrains} 
                      onChange={(e) => setTransportTrains(e.target.checked)}
                      className="rounded"
                    />
                    <span>Train</span>
                  </label>
                  <label className="inline-flex items-center space-x-2">
                    <input 
                      type="checkbox" 
                      checked={transportBoat} 
                      onChange={(e) => setTransportBoat(e.target.checked)}
                      className="rounded"
                    />
                    <span>Boat</span>
                  </label>
                </div>

                <div className="text-center">
                  <button
                    onClick={generateItinerary}
                    disabled={isPlanning}
                    className="bg-white text-blue-600 px-8 py-3 rounded-xl font-bold hover:bg-gray-100 transition-all duration-300 shadow-lg disabled:opacity-60"
                  >
                    {isPlanning ? 'Planning…' : 'Create Detailed Plan'}
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};