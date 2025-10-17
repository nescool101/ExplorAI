# ExplorAI - AI-Powered Travel Planning

ExplorAI is an intelligent travel planning application that uses artificial intelligence to create personalized, detailed itineraries for travelers. The project consists of a Spring Boot backend with AI integration and an Astro frontend with React components.

## 🏗️ Project Structure

```
ExplorAI/
├── backend/                 # Spring Boot API with Spring AI
│   ├── src/main/java/      # Java source code
│   ├── build.gradle.kts    # Gradle build configuration
│   └── Dockerfile          # Docker configuration
├── frontend/               # Astro + React + TypeScript frontend
│   ├── src/
│   │   ├── components/     # React components
│   │   ├── layouts/        # Astro layouts
│   │   └── pages/          # Astro pages
│   ├── astro.config.mjs    # Astro configuration
│   └── package.json        # Frontend dependencies
├── .gitignore             # Git ignore rules
└── README.md              # This file
```

## 🚀 Quick Start

### Prerequisites

- **Java 17+** (for backend)
- **Node.js 18+** (for frontend)
- **Docker** (optional, for containerized deployment)

### Backend Setup (Spring Boot)

1. **Navigate to backend directory:**
   ```bash
   cd backend
   ```

2. **Set up environment variables:**
   ```bash
   cp env.example .env
   # Edit .env with your API keys
   ```

3. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

   The API will be available at `http://localhost:8080`

### Frontend Setup (Astro + React)

1. **Navigate to frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start development server:**
   ```bash
   npm run dev
   ```

   The frontend will be available at `http://localhost:4321`

## 🛠️ Development

### Backend Development

The backend is built with:
- **Spring Boot 3.3+** - Main framework
- **Spring AI** - AI integration
- **Gradle** - Build tool
- **Java 17+** - Programming language

**Key endpoints:**
- `POST /api/generate-itinerary` - Generate AI-powered itineraries
- `GET /api/validate-flight` - Validate flight information
- `POST /api/save-trip` - Save user trips
- `GET /api/user/preferences` - Get user preferences

### Frontend Development

The frontend is built with:
- **Astro** - Static site generator
- **React** - UI components
- **TypeScript** - Type safety
- **Tailwind CSS** - Styling

**Key components:**
- `Hero` - Landing page hero section
- `FeatureCard` - Feature showcase cards
- `ItineraryForm` - Trip planning form
- `Layout` - Main page layout

## 🔧 Configuration

### Environment Variables

Create a `.env` file in the backend directory:

```env
# AI Configuration
OPENAI_API_KEY=your_openai_api_key
DEEPSEEK_API_KEY=your_deepseek_api_key

# Database (optional)
DATABASE_URL=your_database_url

# External APIs
SKYSCANNER_API_KEY=your_skyscanner_key
OPENWEATHER_API_KEY=your_openweather_key
```

### Frontend Configuration

The frontend uses Astro's built-in configuration. Key settings in `astro.config.mjs`:
- React integration enabled
- Tailwind CSS configured
- TypeScript support

## 🚀 Deployment

### Backend Deployment (Fly.io)

1. **Install Fly CLI:**
   ```bash
   curl -L https://fly.io/install.sh | sh
   ```

2. **Deploy:**
   ```bash
   cd backend
   fly launch
   fly secrets set OPENAI_API_KEY=your_key
   fly deploy
   ```

### Frontend Deployment (Static Hosting)

1. **Build the project:**
   ```bash
   cd frontend
   npm run build
   ```

2. **Deploy to your preferred static hosting:**
   - GitHub Pages
   - Netlify
   - Vercel
   - AWS S3 + CloudFront

## 🧠 AI Integration

ExplorAI uses multiple AI models and services:

- **OpenAI GPT** - Primary language model
- **DeepSeek** - Alternative model via OpenRouter
- **Spring AI** - Java AI integration framework

The AI generates personalized itineraries based on:
- Destination preferences
- Travel dates and duration
- Budget constraints
- Interest categories
- Traveler count

## 🌍 External Integrations

- **Skyscanner/Amadeus** - Flight validation and pricing
- **OpenWeatherMap** - Weather information
- **Google Maps/Mapbox** - Location and routing data
- **Wikipedia/Wikivoyage** - Local attraction information

## 📝 API Documentation

### Generate Itinerary

```http
POST /api/generate-itinerary
Content-Type: application/json

{
  "destination": "Tokyo, Japan",
  "startDate": "2024-06-01",
  "endDate": "2024-06-07",
  "travelers": 2,
  "budget": "mid-range",
  "interests": ["culture", "food", "history"]
}
```

### Validate Flight

```http
GET /api/validate-flight?origin=NYC&destination=LAX&date=2024-06-01
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/your-username/ExplorAI/issues) page
2. Create a new issue with detailed information
3. Contact the development team

## 🗺️ Roadmap

- [ ] User authentication and profiles
- [ ] Trip sharing and collaboration
- [ ] Mobile app (React Native)
- [ ] Offline itinerary downloads
- [ ] Voice interaction
- [ ] Real-time trip updates
- [ ] Community features
- [ ] Advanced AI personalization

---

**Built with ❤️ using Spring Boot, Astro, React, and AI**

