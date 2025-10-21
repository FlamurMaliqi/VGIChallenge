# VGI Bus Booking Frontend

A modern React frontend for the VGI Bus Booking System that provides an intuitive interface for finding public transport connections and managing group bookings.

## 🎯 Overview

The VGI Bus Booking Frontend is a responsive React application built with Create React App. It provides a user-friendly interface for:

- **Route Planning**: Interactive connection search with Google Maps integration
- **Group Booking Management**: Easy booking creation and management
- **Admin Panel**: Administrative functions for system management
- **Responsive Design**: Mobile-friendly interface that works on all devices

## 🚀 Quick Start

### Prerequisites

- **Node.js** (v16 or higher)
- **npm** (v7 or higher)
- **Backend API** running on http://localhost:8080

### 1. Installation

```bash
# Clone the repository
git clone https://github.com/yourusername/VGIChallenge.git
cd VGIChallenge/Frontend

# Install dependencies
npm install
```

### 2. Environment Configuration

Create a `.env.local` file in the Frontend directory:

```bash
# Backend API URL
REACT_APP_API_URL=http://localhost:8080

# Optional: Google Maps API Key (if using maps directly in frontend)
REACT_APP_GOOGLE_MAPS_API_KEY=your_google_maps_api_key_here
```

### 3. Start Development Server

```bash
npm start
```

The application will open at [http://localhost:3000](http://localhost:3000).

## 🏗️ Project Structure

```
Frontend/
├── public/                 # Static assets
│   ├── index.html         # Main HTML template
│   ├── manifest.json      # PWA manifest
│   ├── images/           # Image assets
│   └── animations/       # Lottie animations
├── src/                   # Source code
│   ├── components/        # React components
│   │   ├── Booking.jsx   # Booking management
│   │   ├── Connection.jsx # Connection display
│   │   ├── Header.jsx    # Navigation header
│   │   └── ...
│   ├── contexts/         # React contexts
│   │   ├── AdminContext.js
│   │   ├── BookingContext.js
│   │   └── ...
│   ├── pages/            # Page components
│   │   ├── Home.jsx      # Landing page
│   │   ├── Booking.jsx   # Booking page
│   │   └── Admin.jsx     # Admin panel
│   ├── css/              # Stylesheets
│   │   ├── main.css      # Main styles
│   │   ├── header.css    # Header styles
│   │   └── ...
│   ├── api.js            # API client
│   ├── App.js            # Main app component
│   └── index.js          # Entry point
├── package.json          # Dependencies and scripts
└── Dockerfile           # Docker configuration
```

## 🎨 Features

### **Connection Search**
- Interactive map interface for selecting start and end points
- Real-time route planning with Google Maps integration
- Multiple connection options with timing and occupancy information
- Visual representation of bus routes and stops

### **Booking Management**
- User-friendly booking form with contact information
- Passenger count selection
- Booking confirmation and cancellation
- Email notifications integration

### **Admin Panel**
- Administrative dashboard for system management
- Route and booking monitoring
- Data upload and management tools
- System statistics and analytics

### **Responsive Design**
- Mobile-first design approach
- Touch-friendly interface
- Adaptive layouts for all screen sizes
- Progressive Web App (PWA) capabilities

## 🛠️ Development

### Available Scripts

#### `npm start`
Runs the app in development mode at [http://localhost:3000](http://localhost:3000).
- Hot reloading enabled
- Development tools available
- Error overlay for debugging

#### `npm test`
Launches the test runner in interactive watch mode.
- Jest testing framework
- React Testing Library
- Snapshot testing support

#### `npm run build`
Builds the app for production to the `build` folder.
- Optimized bundle for best performance
- Minified files with hashed names
- Production-ready static files

#### `npm run eject`
**⚠️ One-way operation - cannot be undone!**

Ejects from Create React App to get full control over build tools and configuration.

### Code Organization

#### **Components**
- **Functional Components**: Modern React with hooks
- **Context API**: State management across components
- **Custom Hooks**: Reusable logic and state management
- **PropTypes**: Type checking for component props

#### **Styling**
- **CSS Modules**: Scoped styling approach
- **Responsive Design**: Mobile-first CSS
- **CSS Variables**: Consistent theming
- **Flexbox/Grid**: Modern layout techniques

#### **API Integration**
- **Axios**: HTTP client for API requests
- **Error Handling**: Comprehensive error management
- **Loading States**: User feedback during API calls
- **Caching**: Optimized data fetching

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `REACT_APP_API_URL` | Backend API URL | `http://localhost:8080` | Yes |
| `REACT_APP_GOOGLE_MAPS_API_KEY` | Google Maps API Key | - | Optional |

### API Integration

The frontend communicates with the backend through REST APIs:

```javascript
// Example API call
import { getStops, findConnections, createBooking } from './api';

// Get all bus stops
const stops = await getStops();

// Find connections
const connections = await findConnections({
  fromStopName: "Start Stop",
  fromCoordinates: { latitude: 48.7602430, longitude: 11.4224340 },
  toStopName: "End Stop", 
  toCoordinates: { latitude: 48.7571210, longitude: 11.4004590 },
  departure: "2024-11-15T11:00:00",
  pax: 5
});

// Create booking
const booking = await createBooking({
  contact: { /* contact info */ },
  connection: connections[0],
  pax: 5
});
```

## 🧪 Testing

### Running Tests

```bash
# Run all tests
npm test

# Run tests in watch mode
npm test -- --watch

# Run tests with coverage
npm test -- --coverage

# Run tests once
npm test -- --watchAll=false
```

### Test Structure

```
src/
├── components/
│   ├── Booking.test.jsx
│   ├── Connection.test.jsx
│   └── ...
├── pages/
│   ├── Home.test.jsx
│   └── ...
└── __mocks__/          # Mock files
```

### Testing Tools

- **Jest**: Testing framework
- **React Testing Library**: Component testing utilities
- **MSW**: API mocking
- **jsdom**: DOM environment for tests

## 🚀 Deployment

### Production Build

```bash
# Create production build
npm run build

# Serve the build locally
npx serve -s build -l 3000
```

### Docker Deployment

```bash
# Build Docker image
docker build -t vgi-frontend .

# Run container
docker run -p 3000:3000 vgi-frontend
```

### Static Hosting

The build folder contains static files that can be deployed to any static hosting service:

- **Netlify**: Drag and drop the build folder
- **Vercel**: Connect your GitHub repository
- **AWS S3**: Upload build files to S3 bucket
- **GitHub Pages**: Use GitHub Actions for deployment

### Environment-Specific Builds

```bash
# Development
npm start

# Production
npm run build

# Staging
REACT_APP_API_URL=https://staging-api.example.com npm run build
```

## 🎨 UI/UX Features

### **Design System**
- Consistent color palette
- Typography hierarchy
- Spacing system
- Component library

### **Accessibility**
- ARIA labels and roles
- Keyboard navigation
- Screen reader support
- Color contrast compliance

### **Performance**
- Code splitting
- Lazy loading
- Image optimization
- Bundle analysis

## 🔍 Troubleshooting

### Common Issues

#### **API Connection Issues**
```bash
# Check if backend is running
curl http://localhost:8080/v1/connections/stops

# Verify environment variables
echo $REACT_APP_API_URL
```

#### **Build Issues**
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install

# Check for dependency conflicts
npm ls
```

#### **Test Issues**
```bash
# Clear Jest cache
npm test -- --clearCache

# Run specific test
npm test -- --testNamePattern="Booking"
```

### Debug Mode

```bash
# Enable debug logging
REACT_APP_DEBUG=true npm start

# Enable verbose logging
DEBUG=* npm start
```

## 📱 Mobile Development

### Responsive Breakpoints

```css
/* Mobile First Approach */
@media (min-width: 768px) { /* Tablet */ }
@media (min-width: 1024px) { /* Desktop */ }
@media (min-width: 1440px) { /* Large Desktop */ }
```

### Touch Interactions

- Touch-friendly button sizes (44px minimum)
- Swipe gestures for navigation
- Pull-to-refresh functionality
- Mobile-optimized forms

## 🔐 Security

### Best Practices

- **Environment Variables**: Never commit sensitive data
- **API Security**: Validate all API responses
- **XSS Prevention**: Sanitize user inputs
- **HTTPS**: Use secure connections in production

### Content Security Policy

```html
<meta http-equiv="Content-Security-Policy" 
      content="default-src 'self'; 
               script-src 'self' 'unsafe-inline' https://maps.googleapis.com;
               style-src 'self' 'unsafe-inline';
               img-src 'self' data: https:;">
```

## 📊 Performance Monitoring

### Bundle Analysis

```bash
# Analyze bundle size
npm run build
npx webpack-bundle-analyzer build/static/js/*.js
```

### Performance Metrics

- **First Contentful Paint (FCP)**
- **Largest Contentful Paint (LCP)**
- **Cumulative Layout Shift (CLS)**
- **First Input Delay (FID)**

## 🤝 Contributing

### Development Workflow

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Make your changes**
4. **Write tests** for new functionality
5. **Run tests**: `npm test`
6. **Build the project**: `npm run build`
7. **Commit changes**: `git commit -m 'Add amazing feature'`
8. **Push to branch**: `git push origin feature/amazing-feature`
9. **Open a Pull Request**

### Code Style

- **ESLint**: JavaScript linting
- **Prettier**: Code formatting
- **Conventional Commits**: Commit message format
- **Component Documentation**: JSDoc comments

## 📚 Learn More

- [React Documentation](https://reactjs.org/docs/getting-started.html)
- [Create React App](https://create-react-app.dev/docs/getting-started/)
- [React Testing Library](https://testing-library.com/docs/react-testing-library/intro/)
- [Progressive Web Apps](https://web.dev/progressive-web-apps/)

---

**Built with ❤️ using React and Create React App**