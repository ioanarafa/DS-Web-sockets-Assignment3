# Energy Management System - Assignment 3

**Student:** Rafa Ioana  
**Group:** DS2025_30243  
**Assignment:** 2 & 3 - Distributed Systems

---

## 📋 Project Overview

Energy Management System (EMS) is a microservices-based application for monitoring device energy consumption with real-time notifications and customer support features.

### Assignment 3 Features ✅

1. **WebSocket Microservice** - Real-time overconsumption alerts
2. **Rule-Based Chatbot** - 12 automated response rules
3. **Client-Admin Chat** - Bidirectional real-time communication

---

## 🏗️ Architecture

### Microservices:
- **Auth Service** (Port 8081) - JWT authentication
- **User Service** (Port 8082) - User management
- **Device Service** (Port 8083) - Device CRUD operations
- **Monitoring Service** (Port 8080) - Consumption tracking & overconsumption detection
- **WebSocket Service** (Port 8084) - Real-time communication ⭐
- **Customer Support Service** (Port 8085) - Rule-based chatbot ⭐

### Infrastructure:
- **Traefik** (Port 80) - Reverse proxy & load balancer
- **RabbitMQ** (Port 5672) - Message broker
- **PostgreSQL** - 4 separate databases
- **React Frontend** (Port 3000) - User interface
- **Device Simulator** - IoT device simulation

### Deployment Diagram:
See `deployment-diagram.puml` (PlantUML) or `deployment-diagram-simple.mmd` (Mermaid)

---

## 🚀 Build & Execution

### Prerequisites:
- Docker & Docker Compose
- Node.js (for frontend development)

### 1. Start All Services

```bash
docker-compose up -d
```

This will start all microservices, databases, RabbitMQ, and Traefik.

### 2. Start Frontend (Development)

```bash
cd frontend
npm install
npm start
```

Frontend will be available at: http://localhost:3000

### 3. Verify Services

Check all services are running:
```bash
docker-compose ps
```

View logs:
```bash
docker-compose logs -f [service-name]
```

---

## 👥 User Management

### Create Admin User

```powershell
.\create-admin.ps1
```

**Credentials:**
- Username: `admin`
- Password: `admin`
- Role: `ADMIN`

### Create Client User

```powershell
.\create-client.ps1
```

**Credentials:**
- Username: `client`
- Password: `client`
- Role: `CLIENT`

---

## 🧪 Testing

### Test 1: Overconsumption Alerts (WebSocket)

1. **Start device simulator:**
   ```bash
   docker-compose up -d device-simulator
   ```

2. **Login** at http://localhost:3000
   - Use admin or client credentials

3. **Wait for alert** (1-2 minutes)
   - Simulator sends measurements every 1 minute
   - Alert appears when consumption > maxConsumption

4. **Expected result:**
   - Red alert banner at top of screen
   - Alert notification with device details
   - Real-time update via WebSocket

### Test 2: Rule-Based Chatbot

1. **Login as client** (`client` / `client`)

2. **Click "Support" button** (top-right corner)

3. **Test chatbot rules:**
   ```
   Type: "hello"
   → Bot: "Hello client! Welcome to Energy Management System..."

   Type: "help"
   → Bot: "I can help you with: ✓ Device management..."

   Type: "devices"
   → Bot: "You can view and manage your devices..."

   Type: "alert"
   → Bot: "Overconsumption alerts are triggered when..."

   Type: "consumption"
   → Bot: "You can monitor your energy consumption..."
   ```

4. **Expected result:**
   - Instant automatic responses (< 1 second)
   - 12 rules covering common questions

### Test 3: Client-Admin Chat

**Terminal 1 - Client:**
1. Login as client (`client` / `client`)
2. Click "Support"
3. Type: "I need help with device 1"
4. Receive bot response automatically
5. Wait for admin reply

**Terminal 2 - Admin (new browser/incognito):**
1. Login as admin (`admin` / `admin`)
2. Click "Support"
3. See client's message in dashboard
4. Type reply in response field
5. Click "Reply"

**Terminal 1 - Client:**
- Receive admin reply in real-time

---

## 📊 Rule-Based Chatbot - 12 Rules

1. **Greetings** - hello, hi, hey
2. **Device Information** - devices, list devices
3. **Energy Consumption** - consumption, energy usage
4. **Alerts** - alert, notification, overconsumption
5. **Account Settings** - account, profile, password
6. **General Help** - help, support, assist
7. **Login** - login, sign in, authentication
8. **Device Management** - add device, delete device
9. **System Status** - system status, is working
10. **Goodbye** - goodbye, bye, see you
11. **Error/Problem** - error, problem, not working
12. **Data/Reports** - data, report, history

---

## 🔄 Communication Flows

### Overconsumption Alert Flow:
```
Device Simulator → RabbitMQ (measurements) → Monitoring Service
→ Detects overconsumption → RabbitMQ (alerts) → WebSocket Service
→ Broadcasts to Client Browser (real-time)
```

### Chatbot Flow:
```
Client Browser → Customer Support Service → Rule-Based Chatbot
→ Generates response → RabbitMQ (chat) → WebSocket Service
→ Sends to Client Browser (real-time)
```

### Client-Admin Chat Flow:
```
Client → Support Service → RabbitMQ → WebSocket Service → Admin
Admin → Support Service → RabbitMQ → WebSocket Service → Client
```

---

## 🛠️ Technologies Used

- **Backend:** Spring Boot (Java 21)
- **Frontend:** React.js
- **Real-time:** WebSocket (STOMP), SockJS
- **Messaging:** RabbitMQ (AMQP)
- **Database:** PostgreSQL
- **API Gateway:** Traefik
- **Containerization:** Docker, Docker Compose
- **Authentication:** JWT

---

## 🌐 Endpoints

### Frontend:
- **Application:** http://localhost:3000

### Backend (via Traefik):
- **Auth:** http://localhost/auth/**
- **Users:** http://localhost/users/**
- **Devices:** http://localhost/devices/**
- **Monitoring:** http://localhost/monitoring/**
- **Chat:** http://localhost/chat/**
- **WebSocket:** ws://localhost/ws

### Management:
- **RabbitMQ:** http://localhost:15672 (admin/admin)

---

## 🐳 Docker Services

```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f [service-name]

# Rebuild specific service
docker-compose build [service-name]
docker-compose up -d [service-name]

# Service names:
# - auth-service
# - user-service
# - device-service
# - monitoring-service
# - websocket-service
# - customer-support-service
# - rabbitmq
# - postgres-auth, postgres-user, postgres-device, postgres-monitoring
# - traefik
# - device-simulator
```

---

## 📁 Project Structure

```
.
├── auth-service/              # JWT authentication
├── user-service/              # User management
├── device-service/            # Device CRUD
├── monitoring-service/        # Consumption tracking
├── websocket-service/         # Real-time communication ⭐
├── customer-support-service/  # Chatbot ⭐
├── device-simulator/          # IoT simulator
├── frontend/                  # React UI
├── reverse-proxy/             # Traefik configuration
├── docker-compose.yml         # Docker orchestration
├── deployment-diagram.puml    # UML diagram
└── README.md                  # This file
```

---

## 🔒 Security

- **Authentication:** JWT-based token authentication
- **Authorization:** Role-based (ADMIN/CLIENT)
- **CORS:** Configured for frontend origin
- **Database:** Isolated databases per service
- **Network:** Docker internal network isolation

---

## 🎓 Evaluation Topics

### 1. Load Balancing
- Traefik serves as reverse proxy
- Routes to multiple service instances
- Can scale horizontally

### 2. Docker Swarm (Optional)
- Current setup uses Docker Compose
- Services are Swarm-ready
- Can be deployed to Docker Swarm cluster

### 3. WebSockets
- STOMP protocol over WebSocket
- SockJS for browser compatibility
- Real-time bidirectional communication
- Topic-based routing

### 4. Security
- JWT authentication
- Role-based authorization
- CORS configuration
- Database isolation

---

## 📝 Assignment Checklist

### Minimum to Pass (5 points):
- ✅ WebSocket Microservice for overconsumption notifications
- ✅ Rule-based chatbot (minimum 10 rules - implemented 12)

### Bonus Features:
- ✅ Client-Admin bidirectional chat
- ✅ Real-time message routing
- ✅ Comprehensive testing

### Deliverables:
- ✅ Source code (all microservices)
- ✅ UML Deployment Diagram
- ✅ README with build/execution instructions
- ✅ Docker configuration

---

## 🐛 Troubleshooting

### Services won't start:
```bash
docker-compose down
docker-compose up -d
```

### Frontend CORS errors:
- Ensure all services are running
- Check Traefik routing: http://localhost:8080/dashboard/

### WebSocket not connecting:
- Check WebSocket service logs: `docker-compose logs websocket-service`
- Verify RabbitMQ is running: `docker-compose ps rabbitmq`

### Database connection errors:
- Wait 10-15 seconds after starting services
- Check database logs: `docker-compose logs postgres-[service]`

---

## 📞 Support

For issues or questions about the assignment, refer to:
- Deployment diagram: `deployment-diagram.puml`
- Docker logs: `docker-compose logs -f`
- RabbitMQ management: http://localhost:15672

---

## 🎯 Key Features Summary

| Feature | Status | Description |
|---------|--------|-------------|
| Authentication | ✅ | JWT-based auth with roles |
| Device Management | ✅ | CRUD operations for devices |
| Energy Monitoring | ✅ | Real-time consumption tracking |
| Overconsumption Alerts | ✅ | WebSocket notifications |
| Rule-Based Chatbot | ✅ | 12 automated response rules |
| Client-Admin Chat | ✅ | Bidirectional real-time chat |
| Message Broker | ✅ | RabbitMQ async messaging |
| Load Balancing | ✅ | Traefik reverse proxy |
| Containerization | ✅ | Docker deployment |

---

**© 2025 - Energy Management System - Assignment 3**
