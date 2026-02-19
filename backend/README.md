# MedTwin Backend - Medical Device Digital Twin Engine

## 🎯 Overview
Spring Boot backend implementing 5 core capabilities for medical device digital twin simulation and analysis.

## 🏗️ Architecture

### Core Capabilities

1. **Requirement Processing** (`RequirementProcessingService`)
   - Validates and processes medical device requirements
   - Applies defaults and compliance checks
   - Manages device specifications

2. **Architecture Generation** (`ArchitectureGenerationService`)
   - AI-driven system architecture generation
   - Component selection and specification
   - Cost and complexity analysis

3. **Digital Twin State Management** (`DigitalTwinStateService`)
   - Real-time state tracking
   - Health monitoring
   - Component status management
   - Scheduled state updates (every 5 seconds)

4. **Simulation & What-If Engine** (`SimulationEngineService`)
   - Physics-based simulation
   - Multi-parameter scenario analysis
   - Time-series data generation
   - AI-optimized parameter generation

5. **AI Insight Layer** (`AIInsightService`)
   - Intelligent recommendations
   - Risk analysis
   - Compliance checking
   - Cost optimization suggestions

## 📦 Technology Stack

- **Framework**: Spring Boot 3.2.2
- **Java**: 17
- **Database**: H2 (in-memory for prototype)
- **ORM**: Spring Data JPA
- **Build Tool**: Maven
- **Real-time**: WebSocket support
- **Validation**: Jakarta Validation

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build & Run

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### Access Points
- **API**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:medtwindb`
  - Username: `sa`
  - Password: (empty)

## 📡 API Endpoints

### 1. Requirements API
```
POST   /api/requirements              - Create requirement
GET    /api/requirements/{id}         - Get requirement
GET    /api/requirements              - List all requirements
PUT    /api/requirements/{id}         - Update requirement
GET    /api/requirements/type/{type}  - Get by device type
```

### 2. Architecture API
```
POST   /api/architecture/generate/{requirementId}  - Generate architecture
GET    /api/architecture/{id}                      - Get architecture
GET    /api/architecture/requirement/{id}          - Get by requirement
```

### 3. Digital Twin API
```
POST   /api/twin/initialize/{architectureId}  - Initialize twin
GET    /api/twin/state/{architectureId}       - Get current state
PUT    /api/twin/state/{architectureId}       - Update state
GET    /api/twin/history/{architectureId}     - Get state history
POST   /api/twin/deactivate/{architectureId}  - Deactivate twin
```

### 4. Simulation API
```
POST   /api/simulation/run/{architectureId}        - Run simulation
GET    /api/simulation/{id}                        - Get simulation
GET    /api/simulation/architecture/{id}           - List by architecture
GET    /api/simulation/optimize/{architectureId}   - Get optimized params
```

### 5. AI Insights API
```
POST   /api/insights/simulation/{id}      - Generate for simulation
POST   /api/insights/architecture/{id}    - Generate for architecture
GET    /api/insights/simulation/{id}      - Get simulation insights
GET    /api/insights/architecture/{id}    - Get architecture insights
GET    /api/insights/critical             - Get critical insights
```

## 🗄️ Data Models

### DeviceRequirement
- Device specifications
- Compliance standards
- Technical parameters
- Power mode settings

### SystemArchitecture
- Generated architecture
- Component list
- Cost and complexity metrics
- Confidence scores

### SystemComponent
- Component specifications
- Power consumption
- Thermal output
- Suggested parts

### DigitalTwinState
- Real-time metrics
- Component states
- Health scores
- Operational status

### SimulationRun
- Input parameters
- Simulation results
- Time-series data
- Risk and compliance scores

### AIInsight
- Insight type and severity
- Recommendations
- Confidence scores
- Reasoning explanation

## 🔬 Physics Simulation Model

### Power Consumption
```
Total Power = (Base + Sampling + Processing + Airflow) × Mode Multiplier
Battery Life = (Capacity × Voltage) / Total Power
```

### Thermal Model
```
Heat Generation = Power × 0.3
Heat Dissipation = min(Generation × 0.8, 2.0)
Thermal Load = 25°C + (Generation - Dissipation) × 15
```

### Efficiency Model
```
Efficiency = 98% - Thermal Loss - Power Loss
Thermal Loss = max(0, (Temp - 40) × 0.5)
Power Loss = max(0, (Power - 80) × 0.2)
```

### Risk Assessment
```
Risk = Thermal Risk + Power Risk + Battery Risk
Thermal Risk = (Temp > Threshold) ? (Temp - Threshold) × 2 : 0
Power Risk = (Power > 90) ? (Power - 90) × 1.5 : 0
Battery Risk = (Life < 8) ? (8 - Life) × 3 : 0
```

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Test Coverage
- Unit tests for services
- Integration tests for repositories
- API endpoint tests

## 📊 Monitoring

### Real-time Updates
- Digital twin states update every 5 seconds
- Automatic health score calculation
- Status monitoring (NORMAL/WARNING/CRITICAL)

### Logging
- DEBUG level for com.medtwin package
- INFO level for Spring framework
- All operations logged with context

## 🔒 Security Considerations

For production deployment:
- Add Spring Security
- Implement JWT authentication
- Enable HTTPS
- Add rate limiting
- Implement audit logging

## 🚢 Deployment

### Docker (Future)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Environment Variables
```properties
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://...
DATABASE_USERNAME=...
DATABASE_PASSWORD=...
```

## 📈 Performance

- In-memory H2 database for fast prototyping
- JPA query optimization
- Scheduled tasks for background processing
- Async support for long-running operations

## 🔄 Future Enhancements

1. **Database**: PostgreSQL for production
2. **Caching**: Redis for state management
3. **Message Queue**: RabbitMQ for async processing
4. **API Gateway**: Spring Cloud Gateway
5. **Service Discovery**: Eureka
6. **Monitoring**: Prometheus + Grafana
7. **Documentation**: Swagger/OpenAPI
8. **Testing**: Increased coverage to 80%+

## 📝 License

Proprietary - MedTwin Project

## 👥 Team

Backend developed for MedTwin Digital Twin Engine prototype submission.

---

**Status**: ✅ Prototype Ready
**Version**: 1.0.0
**Last Updated**: February 2026
