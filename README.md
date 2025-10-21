# VGI Bus Booking System

A comprehensive group bus booking system for VGI (Verkehrsgesellschaft Ingolstadt) that allows users to find public transport connections and make group bookings with automatic capacity management.

## 🚌 Overview

The VGI Bus Booking System is a full-stack application consisting of a React frontend and a Quarkus backend. It integrates with Google Maps APIs to provide real-time public transport route planning and includes a sophisticated booking system with email notifications and capacity management.

### Key Features

- **🔍 Smart Route Planning**: Find optimal bus connections using Google Maps Routes API
- **👥 Group Booking Management**: Book for multiple passengers with contact management
- **📧 Email Notifications**: Automated confirmations and cancellations for users, admins, and control center
- **🚫 Capacity Management**: Automatic route blocking when passenger threshold is exceeded
- **📱 Responsive Design**: Modern React frontend with mobile-friendly interface
- **🛠️ Admin Panel**: Administrative functions for route and booking management

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   React Frontend │    │  Quarkus Backend │    │   PostgreSQL    │
│   (Port 3000)   │◄──►│   (Port 8080)   │◄──►│   (Port 5432)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ▼
                       ┌─────────────────┐
                       │  Google Maps    │
                       │  APIs (Routes,  │
                       │  Geocoding)     │
                       └─────────────────┘
```

## 🚀 Quick Start

### Prerequisites

- **Node.js** (v16 or higher)
- **Java** (v17 or higher)
- **Docker** and **Docker Compose**
- **Google Maps API Key** with Routes, Geocoding, and Maps JavaScript APIs enabled

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/VGIChallenge.git
cd VGIChallenge
```

### 2. Environment Setup

#### Backend Configuration

```bash
cd Backend
cp .env.example .env
```

Edit `.env` with your configuration:

```bash
# Database Configuration
DB_USERNAME=myuser
DB_PASSWORD=mypassword
JDBC_URL=jdbc:postgresql://localhost:5432/transport_data

# Application Configuration
FRONTEND_URL=http://localhost:3000
ADMIN_EMAIL=admin@example.com
CONTROL_CENTER_EMAIL=control@example.com

# Google APIs (Required)
GOOGLE_API_KEY=your_google_maps_api_key_here

# Email Configuration
MAIL_USERNAME=your_smtp_username
MAIL_PASSWORD=your_smtp_password
```

#### Frontend Configuration

```bash
cd Frontend
cp .env.example .env.local
```

Edit `.env.local`:

```bash
REACT_APP_API_URL=http://localhost:8080
```

### 3. Start the Database

```bash
cd Backend/database
docker-compose up -d
```

### 4. Start the Backend

```bash
cd Backend
./gradlew quarkusDev
```

### 5. Start the Frontend

```bash
cd Frontend
npm install
npm start
```

### 6. Access the Application

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **Backend Dev UI**: http://localhost:8080/q/dev/
- **API Documentation**: http://localhost:8080/q/swagger-ui/

## 📁 Project Structure

```
VGIChallenge/
├── Backend/                 # Quarkus Java Backend
│   ├── src/main/java/      # Java source code
│   ├── src/main/resources/ # Configuration files
│   ├── database/           # PostgreSQL setup
│   └── .env.example       # Environment variables template
├── Frontend/               # React Frontend
│   ├── src/               # React source code
│   ├── public/            # Static assets
│   └── .env.example       # Environment variables template
└── README.md              # This file
```

## 🛠️ Development

### Backend Development

The backend is built with Quarkus and provides REST APIs for:

- **Connection Management**: Route planning and stop information
- **Booking Management**: Create, read, and cancel group bookings
- **Route Blocking**: Capacity management and conflict prevention
- **Administrative Functions**: Data management and monitoring

See [Backend README](Backend/README.md) for detailed information.

### Frontend Development

The frontend is a React application with:

- **Connection Search**: Interactive route planning interface
- **Booking Management**: User-friendly booking creation and management
- **Admin Panel**: Administrative functions for system management
- **Responsive Design**: Mobile-friendly interface

See [Frontend README](Frontend/README.md) for detailed information.

## 🐳 Docker Deployment

### Full Stack with Docker Compose

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Individual Services

```bash
# Database only
cd Backend/database
docker-compose up -d

# Backend only
cd Backend
docker build -t vgi-backend .
docker run -p 8080:8080 --env-file .env vgi-backend

# Frontend only
cd Frontend
docker build -t vgi-frontend .
docker run -p 3000:3000 vgi-frontend
```

## 🔧 Configuration

### Required Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `GOOGLE_API_KEY` | Google Maps API key | `AIza...` |
| `DB_USERNAME` | PostgreSQL username | `myuser` |
| `DB_PASSWORD` | PostgreSQL password | `mypassword` |
| `JDBC_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/transport_data` |
| `FRONTEND_URL` | Frontend URL for CORS | `http://localhost:3000` |
| `ADMIN_EMAIL` | Admin notification email | `admin@example.com` |
| `CONTROL_CENTER_EMAIL` | Control center email | `control@example.com` |
| `MAIL_USERNAME` | SMTP username | `your_smtp_username` |
| `MAIL_PASSWORD` | SMTP password | `your_smtp_password` |

### Google Maps API Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable the following APIs:
   - Maps JavaScript API
   - Geocoding API
   - Routes API
4. Create credentials (API Key)
5. Restrict the API key to your domains
6. Add the API key to your environment variables

## 📊 API Documentation

### Core Endpoints

- **Connections**: `/v1/connections` - Route planning and stop information
- **Bookings**: `/v1/bookings` - Booking management
- **Routes**: `/v1/routes` - Route blocking management
- **Admin**: `/v1/admin` - Administrative functions

### Interactive API Documentation

When running in development mode, visit:
- **Swagger UI**: http://localhost:8080/q/swagger-ui/
- **OpenAPI Spec**: http://localhost:8080/q/openapi

## 🧪 Testing

### Backend Tests

```bash
cd Backend
./gradlew test
```

### Frontend Tests

```bash
cd Frontend
npm test
```

### Integration Tests

```bash
# Start test database
cd Backend/database
docker-compose -f docker-compose.test.yml up -d

# Run integration tests
cd Backend
./gradlew integrationTest
```

## 🚀 Production Deployment

### Backend Deployment

1. **Build the application**:
   ```bash
   cd Backend
   ./gradlew build -Dquarkus.package.type=uber-jar
   ```

2. **Run the application**:
   ```bash
   java -jar build/*-runner.jar
   ```

3. **Docker deployment**:
   ```bash
   docker build -t vgi-backend .
   docker run -p 8080:8080 --env-file .env vgi-backend
   ```

### Frontend Deployment

1. **Build the application**:
   ```bash
   cd Frontend
   npm run build
   ```

2. **Serve the build**:
   ```bash
   npx serve -s build -l 3000
   ```

3. **Docker deployment**:
   ```bash
   docker build -t vgi-frontend .
   docker run -p 3000:3000 vgi-frontend
   ```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add some amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

### Development Guidelines

- Follow Java coding standards for backend code
- Use ESLint and Prettier for frontend code
- Write tests for new features
- Update documentation as needed
- Follow conventional commit messages

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/yourusername/VGIChallenge/issues) page
2. Create a new issue with detailed information
3. Contact the development team

## 🙏 Acknowledgments

- [Quarkus](https://quarkus.io/) - Supersonic Subatomic Java Framework
- [React](https://reactjs.org/) - JavaScript library for building user interfaces
- [Google Maps Platform](https://developers.google.com/maps) - Maps and location services
- [PostgreSQL](https://www.postgresql.org/) - Open source relational database

---

**Made with ❤️ for VGI (Verkehrsgesellschaft Ingolstadt)**
