# VGI Bus Booking Backend

A comprehensive backend service for managing group bus bookings for VGI (Verkehrsgesellschaft Ingolstadt). This Quarkus-based application provides REST APIs for finding public transport connections, managing bookings, and handling administrative functions.

## Overview

The VGI Bus Booking Backend is a Java application built with Quarkus that serves as the core API for a group bus booking system. It integrates with Google Maps APIs for route planning and geocoding, manages bookings in a PostgreSQL database, and provides email notifications for booking confirmations and cancellations.

## Key Features

### 🚌 **Public Transport Route Planning**
- **Connection Search**: Find optimal bus routes between two locations using Google Routes API
- **Real-time Data**: Integrates with Google Maps for accurate transit information
- **Multi-segment Journeys**: Supports complex routes with multiple bus connections
- **Stop Management**: Provides access to all available bus stops with coordinates

### 📅 **Group Booking Management**
- **Booking Creation**: Create group bookings with passenger count and contact information
- **Booking Retrieval**: Query bookings by ID, hash, or timestamp
- **Booking Cancellation**: Cancel bookings with email verification
- **Unique Hash System**: Each booking gets a unique hash for secure access

### 🚫 **Route Blocking System**
- **Capacity Management**: Block routes when passenger threshold is exceeded
- **Administrative Control**: Allow administrators to block specific routes
- **Time-based Blocking**: Block routes for specific time periods
- **Conflict Prevention**: Prevent overbooking on popular routes

### 📧 **Email Notification System**
- **Multi-recipient Notifications**: Send emails to users, admins, and control center
- **HTML Templates**: Professional email templates for confirmations and cancellations
- **Booking Details**: Include complete journey information in emails
- **Cancellation Links**: Provide secure links for booking cancellations

### 🛠️ **Administrative Functions**
- **Data Management**: Upload and manage GTFS data (stops, routes, trips)
- **Route Information**: Access route short names and trip headsigns
- **System Monitoring**: Track bookings and route blocks over time

## Architecture

### **Technology Stack**
- **Framework**: Quarkus (Supersonic Subatomic Java Framework)
- **Database**: PostgreSQL with Hibernate ORM
- **Build Tool**: Gradle
- **Email**: Quarkus Mailer with SMTP
- **External APIs**: Google Maps (Routes, Geocoding)
- **Validation**: Jakarta Validation

### **Core Components**

#### **REST Resources**
- `ConnectionResource` - Route planning and stop information
- `BookingResource` - Booking management (create, read, delete)
- `AdminResource` - Administrative functions and data upload
- `RouteBlockResource` - Route blocking management

#### **Services**
- `ConnectionService` - Route planning logic and Google Maps integration
- `BookingService` - Booking business logic and validation
- `EmailService` - Email template management and sending
- `RouteBlockService` - Route blocking logic
- `AdminService` - Administrative data management

#### **External Clients**
- `GoogleRoutesClient` - Google Routes API integration
- `GoogleGeocodingClient` - Google Geocoding API integration

#### **Data Layer**
- **Entities**: Booking, Contact, Route, Stop, Trip, RouteBlock
- **Repositories**: Data access layer with Panache
- **Database**: PostgreSQL with GTFS data support

## API Endpoints

### **Connections API** (`/v1/connections`)
- `GET /stops` - Get all available bus stops with coordinates
- `POST /` - Find connections between two locations

### **Bookings API** (`/v1/bookings`)
- `POST /` - Create a new group booking
- `GET /` - Get bookings after a specific timestamp
- `GET /by-booking-id` - Get booking by ID
- `GET /by-booking-hash` - Get booking by unique hash
- `DELETE /` - Cancel a booking

### **Route Management API** (`/v1/routes`)
- `GET /` - Get route blocks after a specific timestamp
- `POST /` - Block a specific route

### **Admin API** (`/v1/admin`)
- `GET /route-short-names` - Get all available route short names
- `GET /trip-headsigns` - Get all available trip headsigns
- `POST /upload` - Upload GTFS data files

## Environment Setup

### **Required Environment Variables**

Copy `.env.example` to `.env` and configure:

```bash
# Database Configuration
DB_USERNAME=myuser
DB_PASSWORD=mypassword
JDBC_URL=jdbc:postgresql://localhost:5432/transport_data

# Application Configuration
FRONTEND_URL=http://localhost:3000
ADMIN_EMAIL=admin@example.com
CONTROL_CENTER_EMAIL=control@example.com

# Google APIs
GOOGLE_API_KEY=your_google_maps_api_key_here

# Email Configuration
MAIL_USERNAME=your_smtp_username
MAIL_PASSWORD=your_smtp_password
```

### **Google Maps API Requirements**
- Maps JavaScript API
- Geocoding API
- Routes API (for Directions API)

## Running the Application

### **Development Mode**

```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus Dev UI is available at <http://localhost:8080/q/dev/>.

### **Database Setup**

Start PostgreSQL with Docker Compose:

```shell script
cd database
docker-compose up -d
```

### **Production Build**

```shell script
./gradlew build
```

Run the application:

```shell script
java -jar build/quarkus-app/quarkus-run.jar
```

### **Native Executable**

```shell script
./gradlew build -Dquarkus.native.enabled=true
```

## Data Models

### **Connection Request**
```json
{
  "fromStopName": "Christoph-Scheiner-Gymnasium",
  "fromCoordinates": {
    "latitude": 48.7602430,
    "longitude": 11.4224340
  },
  "toStopName": "Humboldtstraße",
  "toCoordinates": {
    "latitude": 48.7571210,
    "longitude": 11.4004590
  },
  "departure": "2024-11-15T11:00:00",
  "pax": 5
}
```

### **Connection Response**
```json
{
  "duration": "00:13:00",
  "isBlocked": false,
  "from": {
    "stopName": "Christoph-Scheiner-Gymnasium",
    "departure": "2024-11-15T11:00:00",
    "coordinates": {
      "latitude": 48.7602430,
      "longitude": 11.4224340
    }
  },
  "to": {
    "stopName": "Humboldtstraße",
    "arrival": "2024-11-15T11:13:00",
    "coordinates": {
      "latitude": 48.7571210,
      "longitude": 11.4004590
    }
  },
  "connectionSegments": [
    {
      "routeShortName": "30",
      "tripHeadsign": "Lenting",
      "expectedOccupation": 70,
      "isBlocked": false,
      "fromStop": { /* stop details */ },
      "toStop": { /* stop details */ }
    }
  ]
}
```

## Business Logic

### **Booking Process**
1. User searches for connections between two locations
2. System queries Google Routes API for transit options
3. System checks for existing route blocks
4. User creates booking with contact information
5. System generates unique booking hash
6. Email notifications sent to user, admin, and control center
7. Route blocks created if passenger count exceeds threshold

### **Route Blocking**
- Routes are automatically blocked when passenger count ≥ threshold (default: 5)
- Administrators can manually block routes
- Blocked routes prevent new bookings for that specific route/time
- System checks for conflicts before creating bookings

### **Email System**
- Three types of recipients: User, Admin, Control Center
- HTML email templates with booking details
- Confirmation emails include cancellation links
- Cancellation emails notify all parties

## Development

### **Project Structure**
```
src/main/java/bITe/
├── client/          # External API clients
├── common/          # DTOs and request/response models
├── entity/          # JPA entities
├── repository/      # Data access layer
├── resource/        # REST endpoints
└── service/         # Business logic
```

### **Key Dependencies**
- Quarkus REST
- Hibernate ORM with Panache
- PostgreSQL JDBC driver
- Google Maps services
- Quarkus Mailer
- Jakarta Validation
- Commons Codec

## Learn More

- [Quarkus Documentation](https://quarkus.io/)
- [Google Maps Routes API](https://developers.google.com/maps/documentation/routes)
- [GTFS Reference](https://gtfs.org/)

## JSONs:

### ConnectionRequest Example
```json
{
  "fromStopName": "Christoph-Scheiner-Gymnasium",
  "fromCoordinates": {
    "latitude": 48.7602430,
    "longitude": 11.4224340
  },
  "toStopName": "Humboldtstraße",
  "toCoordinates": {
    "latitude": 48.7571210,
    "longitude": 11.4004590
  }
}
```

### Connection Example
```json
{
    "duration": "00:13:00",
    "isBlocked": false,
    "from": {
        "stopName": "Christoph-Scheiner-Gymnasium",
        "departure": "2024-11-15T11:00:00",
        "arrival": null,
        "coordinates": {
            "latitude": 48.7602430,
            "longitude": 11.4224340
        }
    },
    "to": {
        "stopName": "Humboldtstraße",
        "departure": null,
        "arrival": "2024-11-15T11:13:00",
        "coordinates": {
            "latitude": 48.7571210,
            "longitude": 11.4004590
        }
    },
    "connectionSegments": [
        {
            "routeShortName": "30",
            "tripHeadsign": "Lenting",
            "expectedOccupation": 70,
            "isBlocked": false,
            "fromStop": {
                "stopName": "Christoph-Scheiner-Gymnasium",
                "departure": "2024-11-15T11:00:00",
                "arrival": null,
                "coordinates": {
                    "latitude": 48.7602430,
                    "longitude": 11.4224340
                }
            },
            "toStop": {
                "stopName": "Universität (Kreuztor)",
                "departure": null,
                "arrival": "2024-11-15T11:02:00",
                "coordinates": {
                    "latitude": 48.7653800,
                    "longitude": 11.4165920
                }
            }
        },
        {
            "routeShortName": "53",
            "tripHeadsign": "Humboldtstraße",
            "expectedOccupation": 50,
            "isBlocked": false,
            "fromStop": {
                "stopName": "Universität (Kreuztor)",
                "departure": "2024-11-15T11:09:00",
                "arrival": null,
                "coordinates": {
                    "latitude": 48.7653800,
                    "longitude": 11.4165920
                }
            },
            "toStop": {
                "stopName": "Humboldtstraße",
                "departure": null,
                "arrival": "2024-11-15T11:13:00",
                "coordinates": {
                    "latitude": 48.7571210,
                    "longitude": 11.4004590
                }
            }
        }
    ]
}
```

