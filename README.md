# Energy Management System - Assignment 3
## WebSockets and Real-Time Communication

**Student:** Rafa Ioana  
**Group:** 30243  
**Course:** Distributed Systems 2025

---

## 📋 Overview

This project represents **Assignment 3** - the final phase of a comprehensive Energy Management System built across three progressive assignments. It introduces **real-time communication**, **WebSocket-based notifications**, and **intelligent customer support** to create a fully functional distributed system.

### Evolution Across Assignments

#### Assignment 1: Foundation (Request-Reply Communication)
- ✅ RESTful microservices architecture
- ✅ User Management Microservice
- ✅ Device Management Microservice  
- ✅ Authentication Microservice (JWT-based)
- ✅ Frontend with Admin and Client roles
- ✅ Docker deployment with Traefik reverse proxy

#### Assignment 2: Asynchronous Communication
- ✅ Monitoring Microservice for energy consumption tracking
- ✅ RabbitMQ message broker for asynchronous communication
- ✅ Device Data Simulator (generates measurements every 10 minutes)
- ✅ Hourly energy aggregation and storage
- ✅ User and Device synchronization across microservices

#### Assignment 3: Real-Time Features (Current)
- ✨ **WebSocket Microservice** for real-time notifications
- ✨ **Customer Support Microservice** with rule-based chatbot (12 rules)
- ✨ **Overconsumption Alerts** sent instantly via WebSocket
- ✨ **Client-to-Admin Chat** for real-time support
- ✨ **Enhanced Frontend** with live alerts and chat interface

---

## 🆕 What's New in Assignment 3

### 1. WebSocket Microservice
**Purpose:** Enables real-time bidirectional communication between backend and frontend.

**Features:**
- Real-time delivery of overconsumption alerts to connected users
- Instant message routing for chat functionality
- Uses STOMP protocol over SockJS for cross-browser compatibility
- Subscribes to RabbitMQ queues and broadcasts via WebSocket topics

**Topics:**
- `/topic/overconsumption` - Alerts for all users
- `/topic/admin-messages` - Messages to admin dashboard
- `/topic/client-messages-{userId}` - Messages to specific client

### 2. Customer Support Microservice
**Purpose:** Provides automated and human-assisted customer support.

**Features:**
- **Rule-Based Chatbot:** 12 intelligent rules covering:
  - Greetings and general help
  - Device management queries
  - Energy consumption questions
  - Alert explanations
  - Account settings
  - System usage guidance
- **Hybrid Support:** Bot responds instantly; admin can intervene for complex queries
- **Message Routing:** All messages flow through RabbitMQ for reliability

**Chatbot Rules Include:**
- Device information and management
- Energy consumption monitoring
- Alert interpretation
- Maximum consumption configuration
- Historical data access
- Account settings
- Logout procedures
- System usage tips

### 3. Overconsumption Alert System
**Flow:**
1. Device Simulator sends measurements → RabbitMQ
2. Monitoring Service detects overconsumption (consumption > maxConsumption)
3. Alert published to `overconsumption.alerts` queue
4. WebSocket Service consumes alert and broadcasts to `/topic/overconsumption`
5. Frontend displays real-time notification

### 4. Client-to-Admin Chat
**Flow:**
1. Client sends message → Customer Support Service
2. Message sent to RabbitMQ → WebSocket Service
3. Admin sees message in real-time dashboard
4. Admin replies → flows back through same pipeline
5. Client receives response instantly

### 5. Enhanced Frontend
**New UI Components:**
- 🔔 Real-time alert notifications (top-right badge)
- 💬 Chat interface (bottom-right floating widget)
- 📊 Live connection status indicator
- 🎨 Modern dark theme with glassmorphism effects

---

## 🏗️ Architecture

### System Components

```
┌─────────────────┐
│   Frontend      │ (React on localhost:3000)
│   (Port 3000)   │
└────────┬────────┘
         │ HTTP/WebSocket
         ▼
┌─────────────────┐
│    Traefik      │ (Reverse Proxy on Port 80)
│   (Port 80)     │
└────────┬────────┘
         │
    ┌────┴────┬──────────┬──────────┬──────────┬──────────┐
    ▼         ▼          ▼          ▼          ▼          ▼
┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐
│  Auth  │ │  User  │ │ Device │ │Monitor │ │WebSocket│ │Support │
│Service │ │Service │ │Service │ │Service │ │Service  │ │Service │
└───┬────┘ └───┬────┘ └───┬────┘ └───┬────┘ └───┬─────┘ └───┬────┘
    │          │          │          │          │          │
    ▼          ▼          ▼          ▼          ▼          ▼
┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐              
│Auth DB │ │User DB │ │Device  │ │Monitor │   ┌──────────────┐
│        │ │        │ │  DB    │ │  DB    │   │   RabbitMQ   │
└────────┘ └────────┘ └────────┘ └────────┘   │ (Port 5672)  │
                                               └──────┬───────┘
                                                      ▲
                                                      │
                                              ┌───────┴────────┐
                                              │ Device         │
                                              │ Simulator      │
                                              └────────────────┘
```

### Technology Stack

| Component | Technology |
|-----------|-----------|
| **Backend** | Java 17, Spring Boot 3.2.0 |
| **Frontend** | React 18, TailwindCSS |
| **Message Broker** | RabbitMQ 3.12 |
| **Database** | PostgreSQL 16 |
| **WebSocket** | STOMP over SockJS |
| **Reverse Proxy** | Traefik v3.1 |
| **Containerization** | Docker, Docker Compose |
| **Authentication** | JWT (JSON Web Tokens) |

---

## 🚀 Getting Started

### Prerequisites
- Docker Desktop installed and running
- Ports available: 80, 3000, 5672, 15672

### Build and Run

1. **Clone the repository:**
```bash
git clone git@gitlab.com:ioanarafa/ds2025_30243_rafa_ioana_assignment_3.git
cd ds2025_30243_rafa_ioana_assignment_3
```

2. **Start all services:**
```bash
docker-compose up -d --build
```

3. **Wait 30-40 seconds** for all services to initialize

4. **Start the frontend:**
```bash
cd frontend
npm install
npm start
```

5. **Access the application:**
   - Frontend: http://localhost:3000
   - RabbitMQ Management: http://localhost:15672 (guest/guest)
   - Traefik Dashboard: http://localhost:8080

### Create Admin User

Run the PowerShell script:
```powershell
.\create-admin.ps1
```

**Credentials:**
- Username: `admin`
- Password: `admin`

### Create Client User

Run the PowerShell script:
```powershell
.\create-client.ps1
```

**Credentials:**
- Username: `client1`
- Password: `client123`

---

## 🧪 Testing Assignment 3 Features

### Test 1: Overconsumption Alerts (Real-Time Notifications)

**Objective:** Verify that alerts appear instantly when device consumption exceeds limit.

**Steps:**
1. Login as **admin**
2. Go to **Devices** tab
3. Create a device with **low maxConsumption** (e.g., 0.5 kWh):
   - Name: `Test Device`
   - Description: `For testing alerts`
   - Address: `Test Address`
   - Max Consumption: `0.5`

4. Assign the device to a user

5. **Check device simulator logs:**
```bash
docker-compose logs -f device-simulator
```

6. **Expected result:** Within 10-30 minutes, you'll see:
   - "Overconsumption alert sent" in monitoring-service logs
   - Real-time notification appears in frontend (red badge on 🔔 icon)
   - Alert details show: device name, current vs max consumption

**Troubleshooting:**
- Check WebSocket connection status in browser console (F12)
- Verify device simulator is sending measurements
- Ensure maxConsumption is realistically low for quick testing

### Test 2: Rule-Based Chatbot

**Objective:** Test automated customer support responses.

**Steps:**
1. Login as **client1**
2. Click the **💬 Chat** button (bottom-right)
3. Try these test messages:

| Message | Expected Response |
|---------|-------------------|
| `hello` | Greeting with username |
| `what devices do I have?` | Explanation of device management |
| `how to check consumption?` | Instructions for viewing consumption |
| `why am I getting alerts?` | Explanation of overconsumption system |
| `how to change password?` | Account settings guidance |
| `random question xyz` | Fallback response with topic suggestions |

**Expected:** Instant responses (< 1 second) from "System Bot"

### Test 3: Client-to-Admin Chat

**Objective:** Test real-time bidirectional chat between client and admin.

**Steps:**
1. Open two browser windows:
   - Window 1: Login as **client1**
   - Window 2: Login as **admin**

2. In **client1** window:
   - Click 💬 Chat button
   - Type: `I need help with my device`
   - Send message

3. In **admin** window:
   - Open **Chat** tab (admin dashboard)
   - You should see client's message appear instantly
   - Type reply: `How can I assist you?`
   - Send

4. In **client1** window:
   - Reply should appear instantly (no page refresh needed)

**Expected:**
- Messages appear within 1-2 seconds
- Chat history preserved during session
- Proper sender identification (Client/Admin/Bot)

---

## 📊 Data Flow Examples

### Overconsumption Alert Flow
```
Device Simulator (every 10 min)
    → measurement.value (e.g., 1.2 kWh)
    → RabbitMQ: device.measurements queue
    
Monitoring Service
    → Reads measurement
    → Aggregates hourly consumption
    → Checks: totalConsumption > maxConsumption?
    → YES → Publishes to RabbitMQ: overconsumption.alerts queue
    
WebSocket Service
    → Consumes from overconsumption.alerts
    → Broadcasts to /topic/overconsumption
    
Frontend (React)
    → WebSocket client receives message
    → Updates alerts state
    → Shows notification badge + alert list
```

### Chat Message Flow
```
Frontend (Client)
    → User types message
    → POST /chat/send
    
Customer Support Service
    → Receives message
    → Checks rule-based system (12 rules)
    → MATCH? → Generates bot response
    → NO MATCH? → Forwards to admin
    → Publishes message to RabbitMQ: chat.messages queue
    
WebSocket Service
    → Consumes from chat.messages
    → Routes to appropriate topic:
        - USER message → /topic/admin-messages
        - ADMIN message → /topic/client-messages-{userId}
        - BOT message → /topic/client-messages-{userId}
    
Frontend
    → WebSocket client receives message
    → Updates chat messages state
    → Displays in chat UI
```

---

## 🛠️ Configuration

### Device Simulator Configuration

To run multiple simulators or change measurement frequency:

**Edit `docker-compose.yml`:**
```yaml
device-simulator:
  environment:
    - DEVICE_ID=1                    # Device to simulate
    - INTERVAL_MINUTES=10            # Measurement interval
```

**Add another simulator:**
```yaml
device-simulator-2:
  build: ./device-simulator
  container_name: device-simulator-2
  environment:
    - DEVICE_ID=2
    - INTERVAL_MINUTES=5             # Faster measurements
  depends_on:
    - rabbitmq
  networks:
    - mynet
```

### RabbitMQ Queues

| Queue Name | Purpose |
|------------|---------|
| `device.measurements` | Device sensor data |
| `user.sync` | User creation events |
| `device.sync` | Device creation events |
| `overconsumption.alerts` | Alert notifications |
| `chat.messages` | Chat messages |

---

## 📁 Project Structure

```
.
├── auth-service/               # JWT authentication
├── user-service/               # User CRUD operations
├── device-service/             # Device CRUD operations
├── monitoring-service/         # Energy aggregation + alerts
├── websocket-service/          # NEW: Real-time communication
├── customer-support-service/   # NEW: Chatbot + chat routing
├── device-simulator/           # Measurement generation
├── frontend/                   # React UI with WebSocket
├── reverse-proxy/              # Traefik configuration
├── docker-compose.yml          # Orchestration
├── create-admin.ps1            # Admin creation script
├── create-client.ps1           # Client creation script
├── DiagramAss3.png             # UML Deployment Diagram
└── README.md                   # This file
```

---

## 🔍 Monitoring and Debugging

### Check Service Status
```bash
docker-compose ps
```

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f websocket-service
docker-compose logs -f customer-support-service
docker-compose logs -f monitoring-service
```

### Restart a Service
```bash
docker-compose restart websocket-service
```

### RabbitMQ Management
- URL: http://localhost:15672
- Username: `guest`
- Password: `guest`
- Check queues, message rates, and consumers

### Frontend Console (F12)
Look for:
```
WebSocket connected!
Subscribed to overconsumption alerts
Subscribed to admin messages
```

---

## 🎯 Assignment 3 Requirements Checklist

### Minimum Requirements (5 points)
- ✅ WebSocket Microservice for overconsumption notifications
- ✅ Integration with Assignment 2 monitoring system
- ✅ Rule-based chatbot with 12 rules (> minimum 10)
- ✅ README file (this document)
- ✅ Docker deployment

### Additional Features (7 points)
- ✅ **+2p** Client-to-Admin chat integration
- ❌ **+1p** AI-driven customer support (not implemented)
- ❌ **+2p** Load balancing service (not implemented)

### Project Requirements (10 points)
- ✅ Reverse Proxy (Traefik)
- ✅ Docker deployment
- ✅ UML Deployment Diagram (`DiagramAss3.png`)

**Total Implemented: 14/17 points (Minimum + Chat Integration + Project)**

---

## 🐛 Common Issues and Solutions

### Issue: Alerts not appearing
**Solution:**
1. Check device simulator is running: `docker-compose ps`
2. Verify measurements are sent: `docker-compose logs -f device-simulator`
3. Ensure maxConsumption is low (0.5 kWh) for quick testing
4. Check WebSocket connection in browser console

### Issue: Chat not working
**Solution:**
1. Verify WebSocket service is running
2. Check RabbitMQ connection: http://localhost:15672
3. Look for errors in customer-support-service logs
4. Refresh browser and check console for WebSocket errors

### Issue: "Failed to fetch" on login
**Solution:**
1. Ensure all services are running: `docker-compose ps`
2. Wait 30-40 seconds after `docker-compose up`
3. Check auth-service logs for CORS errors
4. Restart services: `docker-compose restart`

### Issue: Device simulator not sending data
**Solution:**
1. Check if device exists in database
2. Verify `DEVICE_ID` environment variable in `docker-compose.yml`
3. Restart simulator: `docker-compose restart device-simulator`
4. Check logs: `docker-compose logs -f device-simulator`

---

## 👨‍💻 Development Notes

### Key Technical Decisions

1. **STOMP over SockJS**: Chosen for cross-browser WebSocket compatibility and fallback support
2. **RabbitMQ as Message Bus**: Decouples services and ensures reliable message delivery
3. **Topic-based Broadcasting**: Allows targeted message delivery (admin vs specific client)
4. **Hybrid Chat System**: Rule-based bot for common queries, human escalation for complex issues
5. **Real-time Aggregation**: Monitoring service processes measurements on-the-fly

### Future Improvements

- Implement AI-driven customer support (Gemini/OpenAI integration)
- Add load balancing service with Docker Swarm
- Implement persistent chat history in database
- Add typing indicators and read receipts
- Support file attachments in chat
- Add push notifications for mobile devices

---

## 📚 References

- [Assignment 3 Requirements](a3.pdf)
- [Spring WebSocket Guide](https://spring.io/guides/gs/messaging-stomp-websocket)
- [RabbitMQ Documentation](https://www.rabbitmq.com/documentation.html)
- [STOMP Protocol](https://stomp.github.io/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)

---

## 📝 License

This project is submitted as coursework for **Distributed Systems** course at **Technical University of Cluj-Napoca**.

**Submitted by:** Rafa Ioana  
**Date:** February 2026  
**Course:** Distributed Systems 2025-2026
