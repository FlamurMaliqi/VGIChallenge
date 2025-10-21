# VGI Bus Booking System - Deployment Guide

This guide provides comprehensive instructions for deploying the VGI Bus Booking System in various environments.

## 📋 Table of Contents

- [Prerequisites](#prerequisites)
- [Local Development](#local-development)
- [Docker Deployment](#docker-deployment)
- [Production Deployment](#production-deployment)
- [Cloud Deployment](#cloud-deployment)
- [Environment Configuration](#environment-configuration)
- [Monitoring & Maintenance](#monitoring--maintenance)
- [Troubleshooting](#troubleshooting)

## 🔧 Prerequisites

### System Requirements

- **Operating System**: Linux, macOS, or Windows
- **Memory**: Minimum 4GB RAM (8GB recommended)
- **Storage**: Minimum 10GB free space
- **Network**: Internet connection for Google Maps APIs

### Software Requirements

- **Docker** (v20.10 or higher)
- **Docker Compose** (v2.0 or higher)
- **Node.js** (v16 or higher) - for local development
- **Java** (v17 or higher) - for local development
- **PostgreSQL** (v13 or higher) - if not using Docker

### External Services

- **Google Maps API Key** with the following APIs enabled:
  - Maps JavaScript API
  - Geocoding API
  - Routes API
- **SMTP Server** for email notifications
- **Domain name** (for production)

## 🚀 Local Development

### Option 1: Docker Compose (Recommended)

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/VGIChallenge.git
   cd VGIChallenge
   ```

2. **Create environment file**:
   ```bash
   cp .env.example .env
   ```

3. **Configure environment variables**:
   ```bash
   # Edit .env file with your values
   nano .env
   ```

4. **Start all services**:
   ```bash
   docker-compose up -d
   ```

5. **Access the application**:
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080
   - Backend Dev UI: http://localhost:8080/q/dev/

### Option 2: Manual Setup

#### Backend Setup

1. **Start PostgreSQL**:
   ```bash
   cd Backend/database
   docker-compose up -d
   ```

2. **Configure backend**:
   ```bash
   cd Backend
   cp .env.example .env
   # Edit .env with your configuration
   ```

3. **Start backend**:
   ```bash
   ./gradlew quarkusDev
   ```

#### Frontend Setup

1. **Install dependencies**:
   ```bash
   cd Frontend
   npm install
   ```

2. **Configure frontend**:
   ```bash
   cp .env.example .env.local
   # Edit .env.local with your configuration
   ```

3. **Start frontend**:
   ```bash
   npm start
   ```

## 🐳 Docker Deployment

### Single Container Deployment

#### Backend Container

```bash
# Build backend image
cd Backend
docker build -t vgi-backend .

# Run backend container
docker run -d \
  --name vgi-backend \
  -p 8080:8080 \
  --env-file .env \
  --link postgres:postgres \
  vgi-backend
```

#### Frontend Container

```bash
# Build frontend image
cd Frontend
docker build -t vgi-frontend .

# Run frontend container
docker run -d \
  --name vgi-frontend \
  -p 3000:3000 \
  -e REACT_APP_API_URL=http://localhost:8080 \
  vgi-frontend
```

### Multi-Container Deployment

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Scale services
docker-compose up -d --scale backend=2

# Stop services
docker-compose down
```

## 🏭 Production Deployment

### 1. Server Preparation

#### Ubuntu/Debian

```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

#### CentOS/RHEL

```bash
# Install Docker
sudo yum install -y yum-utils
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
sudo yum install -y docker-ce docker-ce-cli containerd.io
sudo systemctl start docker
sudo systemctl enable docker

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

### 2. Application Deployment

#### Using Docker Compose

1. **Create production environment file**:
   ```bash
   cp .env.example .env.production
   ```

2. **Configure production settings**:
   ```bash
   # Edit .env.production
   FRONTEND_URL=https://yourdomain.com
   JDBC_URL=jdbc:postgresql://postgres:5432/transport_data
   # ... other production settings
   ```

3. **Deploy with production compose file**:
   ```bash
   docker-compose -f docker-compose.prod.yml up -d
   ```

#### Using Docker Swarm

1. **Initialize swarm**:
   ```bash
   docker swarm init
   ```

2. **Deploy stack**:
   ```bash
   docker stack deploy -c docker-compose.swarm.yml vgi-stack
   ```

3. **Check services**:
   ```bash
   docker service ls
   ```

### 3. Reverse Proxy Setup

#### Nginx Configuration

```nginx
# /etc/nginx/sites-available/vgi-booking
server {
    listen 80;
    server_name yourdomain.com;

    # Frontend
    location / {
        proxy_pass http://localhost:3000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Backend API
    location /v1/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # WebSocket support
    location /ws/ {
        proxy_pass http://localhost:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
}
```

#### SSL Certificate Setup

```bash
# Install Certbot
sudo apt install certbot python3-certbot-nginx

# Obtain SSL certificate
sudo certbot --nginx -d yourdomain.com

# Auto-renewal
sudo crontab -e
# Add: 0 12 * * * /usr/bin/certbot renew --quiet
```

## ☁️ Cloud Deployment

### AWS Deployment

#### Using ECS

1. **Create ECS cluster**:
   ```bash
   aws ecs create-cluster --cluster-name vgi-booking
   ```

2. **Create task definition**:
   ```json
   {
     "family": "vgi-booking",
     "networkMode": "awsvpc",
     "requiresCompatibilities": ["FARGATE"],
     "cpu": "1024",
     "memory": "2048",
     "containerDefinitions": [
       {
         "name": "backend",
         "image": "your-account.dkr.ecr.region.amazonaws.com/vgi-backend:latest",
         "portMappings": [{"containerPort": 8080}],
         "environment": [
           {"name": "DB_USERNAME", "value": "myuser"},
           {"name": "DB_PASSWORD", "value": "mypassword"}
         ]
       }
     ]
   }
   ```

3. **Create service**:
   ```bash
   aws ecs create-service \
     --cluster vgi-booking \
     --service-name vgi-backend \
     --task-definition vgi-booking:1 \
     --desired-count 1
   ```

#### Using EKS

1. **Create EKS cluster**:
   ```bash
   eksctl create cluster --name vgi-booking --region us-west-2
   ```

2. **Deploy application**:
   ```bash
   kubectl apply -f k8s/
   ```

### Google Cloud Platform

#### Using Cloud Run

1. **Build and push images**:
   ```bash
   # Backend
   gcloud builds submit --tag gcr.io/PROJECT_ID/vgi-backend ./Backend

   # Frontend
   gcloud builds submit --tag gcr.io/PROJECT_ID/vgi-frontend ./Frontend
   ```

2. **Deploy services**:
   ```bash
   # Backend
   gcloud run deploy vgi-backend \
     --image gcr.io/PROJECT_ID/vgi-backend \
     --platform managed \
     --region us-central1

   # Frontend
   gcloud run deploy vgi-frontend \
     --image gcr.io/PROJECT_ID/vgi-frontend \
     --platform managed \
     --region us-central1
   ```

### Azure Deployment

#### Using Container Instances

1. **Create resource group**:
   ```bash
   az group create --name vgi-booking --location eastus
   ```

2. **Deploy containers**:
   ```bash
   # Backend
   az container create \
     --resource-group vgi-booking \
     --name vgi-backend \
     --image your-registry/vgi-backend:latest \
     --ports 8080

   # Frontend
   az container create \
     --resource-group vgi-booking \
     --name vgi-frontend \
     --image your-registry/vgi-frontend:latest \
     --ports 3000
   ```

## ⚙️ Environment Configuration

### Development Environment

```bash
# .env.development
NODE_ENV=development
REACT_APP_API_URL=http://localhost:8080
DB_USERNAME=myuser
DB_PASSWORD=mypassword
JDBC_URL=jdbc:postgresql://localhost:5432/transport_data
GOOGLE_API_KEY=your_dev_api_key
```

### Staging Environment

```bash
# .env.staging
NODE_ENV=production
REACT_APP_API_URL=https://staging-api.yourdomain.com
DB_USERNAME=staging_user
DB_PASSWORD=staging_password
JDBC_URL=jdbc:postgresql://staging-db:5432/transport_data
GOOGLE_API_KEY=your_staging_api_key
```

### Production Environment

```bash
# .env.production
NODE_ENV=production
REACT_APP_API_URL=https://api.yourdomain.com
DB_USERNAME=prod_user
DB_PASSWORD=secure_prod_password
JDBC_URL=jdbc:postgresql://prod-db:5432/transport_data
GOOGLE_API_KEY=your_prod_api_key
MAIL_USERNAME=your_smtp_username
MAIL_PASSWORD=your_smtp_password
```

## 📊 Monitoring & Maintenance

### Health Checks

#### Backend Health Check

```bash
# Check backend health
curl http://localhost:8080/q/health

# Check database connection
curl http://localhost:8080/q/health/ready
```

#### Frontend Health Check

```bash
# Check frontend
curl http://localhost:3000

# Check API connectivity
curl http://localhost:3000/api/health
```

### Logging

#### Docker Logs

```bash
# View all logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f postgres
```

#### Log Rotation

```bash
# Configure logrotate
sudo nano /etc/logrotate.d/docker-containers

# Add:
/var/lib/docker/containers/*/*.log {
    rotate 7
    daily
    compress
    size=1M
    missingok
    delaycompress
    copytruncate
}
```

### Backup Strategy

#### Database Backup

```bash
# Create backup script
cat > backup-db.sh << 'EOF'
#!/bin/bash
BACKUP_DIR="/backups"
DATE=$(date +%Y%m%d_%H%M%S)
docker exec postgres pg_dump -U myuser transport_data > $BACKUP_DIR/backup_$DATE.sql
gzip $BACKUP_DIR/backup_$DATE.sql
find $BACKUP_DIR -name "backup_*.sql.gz" -mtime +7 -delete
EOF

chmod +x backup-db.sh

# Schedule backup
crontab -e
# Add: 0 2 * * * /path/to/backup-db.sh
```

#### Application Backup

```bash
# Backup application data
tar -czf vgi-backup-$(date +%Y%m%d).tar.gz \
  --exclude=node_modules \
  --exclude=build \
  --exclude=.git \
  /path/to/VGIChallenge
```

### Performance Monitoring

#### Resource Monitoring

```bash
# Monitor container resources
docker stats

# Monitor specific container
docker stats vgi-backend vgi-frontend
```

#### Application Monitoring

```bash
# Check backend metrics
curl http://localhost:8080/q/metrics

# Check database performance
docker exec postgres psql -U myuser -d transport_data -c "SELECT * FROM pg_stat_activity;"
```

## 🔧 Troubleshooting

### Common Issues

#### Backend Won't Start

```bash
# Check logs
docker-compose logs backend

# Check environment variables
docker-compose config

# Restart service
docker-compose restart backend
```

#### Database Connection Issues

```bash
# Check database status
docker-compose ps postgres

# Check database logs
docker-compose logs postgres

# Test connection
docker exec postgres psql -U myuser -d transport_data -c "SELECT 1;"
```

#### Frontend Build Issues

```bash
# Clear npm cache
docker-compose exec frontend npm cache clean --force

# Rebuild frontend
docker-compose build --no-cache frontend
```

#### Memory Issues

```bash
# Check memory usage
docker stats

# Increase memory limits
# Edit docker-compose.yml
deploy:
  resources:
    limits:
      memory: 2G
```

### Performance Issues

#### Slow API Responses

```bash
# Check backend logs
docker-compose logs backend | grep -i error

# Check database performance
docker exec postgres psql -U myuser -d transport_data -c "EXPLAIN ANALYZE SELECT * FROM bookings;"
```

#### High Memory Usage

```bash
# Check memory usage
docker stats

# Optimize JVM settings
# Add to backend environment:
JAVA_OPTS=-Xmx1g -Xms512m
```

### Security Issues

#### SSL Certificate Problems

```bash
# Check certificate
openssl s_client -connect yourdomain.com:443

# Renew certificate
sudo certbot renew --dry-run
```

#### API Security

```bash
# Check API endpoints
curl -I http://localhost:8080/v1/connections/stops

# Test authentication (if implemented)
curl -H "Authorization: Bearer token" http://localhost:8080/v1/admin/route-short-names
```

## 📞 Support

### Getting Help

1. **Check the logs** first
2. **Review this documentation**
3. **Search existing issues** on GitHub
4. **Create a new issue** with detailed information

### Issue Template

When creating an issue, include:

- **Environment**: OS, Docker version, Node.js version
- **Steps to reproduce**: Detailed steps
- **Expected behavior**: What should happen
- **Actual behavior**: What actually happens
- **Logs**: Relevant log output
- **Screenshots**: If applicable

### Emergency Contacts

- **Development Team**: dev@yourdomain.com
- **System Administrator**: admin@yourdomain.com
- **Emergency Hotline**: +1-XXX-XXX-XXXX

---

**For more information, visit the [main README](README.md) or [Backend README](Backend/README.md).**
