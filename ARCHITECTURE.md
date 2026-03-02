# Architecture and Design Decisions

## Project Overview

The Factory Production Management System is designed to optimize industrial production based on available raw materials to maximize profit. The system follows a modern microservices-ready architecture with clear separation of concerns.

## Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────┐
│          Vue.js Frontend (Port 3000)         │
│  - Responsive UI with Bootstrap 5            │
│  - Multi-language support (i18n)             │
│  - Component-based architecture              │
└──────────────────┬──────────────────────────┘
                   │ HTTP/REST API
                   ↓
┌──────────────────────────────────────────────┐
│   Spring Boot Backend (Port 8080)             │
│  - RESTful API Endpoints                      │
│  - Business Logic Layer                       │
│  - Data Access Layer (JPA/Hibernate)          │
└──────────────────┬──────────────────────────┘
                   │ JDBC
                   ↓
┌──────────────────────────────────────────────┐
│     PostgreSQL Database (Port 5432)           │
│  - Persistent data storage                    │
│  - Transaction management                     │
└──────────────────────────────────────────────┘
```

## Backend Architecture

### Layered Architecture

```
┌────────────────────────────────────────────┐
│         REST Controllers                    │
│  - RawMaterialController                   │
│  - ProductController                       │
│  - ProductionController                    │
└────────────────┬─────────────────────────┘
                 │
┌────────────────▼─────────────────────────┐
│         Services (Business Logic)          │
│  - RawMaterialService                     │
│  - ProductService                         │
│  - ProductionOptimizationService          │
└────────────────┬─────────────────────────┘
                 │
┌────────────────▼─────────────────────────┐
│         Repositories (Data Access)         │
│  - RawMaterialRepository                  │
│  - ProductRepository                      │
│  - ProductCompositionRepository           │
└────────────────┬─────────────────────────┘
                 │
┌────────────────▼─────────────────────────┐
│         JPA Entities (Domain Model)        │
│  - RawMaterial                            │
│  - Product                                │
│  - ProductComposition                     │
└────────────────────────────────────────────┘
```

### Class Diagram

```
┌──────────────────────┐
│    RawMaterial       │
├──────────────────────┤
│ - id: Long           │
│ - code: String       │
│ - name: String       │
│ - stockQuantity: Double│
│ - unit: String       │
│ - description: String│
└──────────────────────┘
         ▲
         │ 1..*
         │
         │ 0..*
         │
┌────────────────────────┐    1    ┌──────────────────────┐
│ ProductComposition     │◄────────┤      Product         │
├────────────────────────┤         ├──────────────────────┤
│ - id: Long             │         │ - id: Long           │
│ - quantityRequired: D  │         │ - code: String       │
│ - product: Product     │         │ - name: String       │
│ - rawMaterial: RM      │         │ - price: Double      │
│                        │         │ - description: String│
│                        │         │ - composition: Set   │
└────────────────────────┘         └──────────────────────┘
```

## Frontend Architecture

### Component Hierarchy

```
App.vue (Main Component)
├── Navigation Bar
│   └── Language Toggle
├── Tab Navigation
│   ├── RawMaterialsTab.vue
│   │   ├── RawMaterialForm
│   │   └── RawMaterialList
│   ├── ProductsTab.vue
│   │   ├── ProductForm
│   │   ├── CompositionManager
│   │   └── ProductList
│   └── OptimizationTab.vue
│       ├── OptimizeButton
│       ├── StockDisplay
│       └── SuggestionsTable
└── Services
    └── api.js (API Service Layer)
```

## Algorithm: Production Optimization

### Algorithm Description

The production optimization algorithm uses a **Greedy Approach** to maximize total production value:

#### Steps:

1. **Calculate Production Capacity**

   - For each product, calculate the maximum units that can be produced with current stock
   - Formula: `maxUnits = min(stockLeft / quantityRequired)` for all materials in composition

2. **Calculate Total Value**

   - Total production value = unit price × max units possible

3. **Sort by Value (Descending)**

   - Products are sorted by total potential value

4. **Greedy Selection**
   - Iterate through sorted products
   - For each product, try to produce as many units as possible
   - Consume raw materials from inventory
   - Move to next product

#### Time Complexity: O(n² × m)

- n = number of products
- m = average materials per product

#### Space Complexity: O(n + m)

### Example:

**Stock:**

- Flour: 1000g
- Sugar: 500g
- Eggs: 100 units

**Products:**

1. **Bread** (Price: $10) - Needs: 200g flour + 50g sugar

   - Max units: min(1000/200, 500/50) = min(5, 10) = 5
   - Total value: 5 × $10 = $50

2. **Cake** (Price: $25) - Needs: 300g flour + 200g sugar + 2 eggs
   - Max units: min(1000/300, 500/200, 100/2) = min(3, 2, 50) = 2
   - Total value: 2 × $25 = $50

**Algorithm Execution:**

1. Both have same value ($50), but cake comes first after sorting
2. Produce 1 Cake: Uses 300g flour, 200g sugar, 2 eggs
   - Remaining: Flour 700g, Sugar 300g, Eggs 98
   - Revenue: $25
3. Produce 3 Breads: Uses 600g flour, 150g sugar
   - Remaining: Flour 100g, Sugar 150g, Eggs 98
   - Revenue: $30

**Total Revenue: $55**

## Key Design Decisions

### 1. Database Schema

**Decision:** Normalized relational schema with JPA/Hibernate ORM

**Rationale:**

- Data consistency through foreign keys
- ACID compliance for reliable transactions
- Easy scaling and backup with PostgreSQL

**Alternative Considered:**

- NoSQL (MongoDB): Would sacrifice consistency for flexibility
- In-memory (Redis): Suitable only for caching, not primary storage

### 2. API Design

**Decision:** RESTful API with standard HTTP methods

**Rationale:**

- Industry standard
- Easy to test with curl/Postman
- Stateless, scalable
- Clear CSRF token support

**Design Patterns:**

- Repository pattern for data access
- Service layer for business logic
- DTO for API contracts
- Exception handler for consistent error responses

### 3. Frontend Framework

**Decision:** Vue.js 3 with Vite

**Rationale:**

- Lightweight and fast (better UX)
- Composition API for complex logic
- Excellent for small to medium apps
- Easy internationalization

**Alternative Considered:**

- React: Over-engineered for this use case
- Angular: Too heavy for productivity app

### 4. Internationalization

**Decision:** vue-i18n for frontend localization

**Rationale:**

- Simple JSON-based translations
- Easy to add new languages
- No backend changes needed for UI text

**Supported Languages:**

- English (en)
- Portuguese (pt-BR)

### 5. Testing Strategy

**Decision:** Unit tests with mocking, Component tests for frontend

**Rationale:**

- Fast execution
- Isolated testing
- No database dependency
- Easy to maintain

**Tools:**

- Backend: JUnit 5 + Mockito
- Frontend: Vitest + Vue Test Utils

### 6. Error Handling

**Decision:** Centralized exception handler with meaningful messages

**Rationale:**

- Consistent error responses
- Easy debugging
- Better user experience

### 7. CORS Policy

**Decision:** Allow all origins in development, restrict in production

**Rationale:**

- Facilitates development
- Production configuration in CorsConfiguration class
- Easy to modify for specific domains

## Technology Stack Rationale

| Component        | Technology        | Why                                      |
| ---------------- | ----------------- | ---------------------------------------- |
| Backend Language | Java 21           | Performance, ecosystem, scalability      |
| Framework        | Spring Boot 4     | Industry standard, excellent ORM support |
| Database         | PostgreSQL 15     | ACID compliance, reliability, free       |
| Frontend         | Vue.js 3          | Quick development, good performance      |
| Build Tool       | Vite              | Fast builds, modern architecture         |
| Styling          | Bootstrap 5       | Responsive, professional look            |
| i18n             | vue-i18n          | Simple, performant, well-maintained      |
| Testing Backend  | JUnit 5 + Mockito | Industry standard, good coverage         |
| Testing Frontend | Vitest            | Modern, fast, compatible with Vue        |
| Containerization | Docker            | Consistent environments, easy deployment |
| CI/CD            | GitHub Actions    | Free, integrated with GitHub             |

## Scalability Considerations

### Current Limitations

- Single backend instance
- Single database instance
- No caching layer

### Future Improvements

1. **Horizontal Scaling**

   - Load balancer (nginx)
   - Multiple backend instances
   - Redis cache layer

2. **Database Optimization**

   - Query optimization
   - Connection pooling
   - Replication and failover

3. **API Enhancement**

   - Pagination for large datasets
   - Advanced filtering
   - Batch operations

4. **Frontend Optimization**
   - Code splitting
   - Lazy loading
   - Service workers

## Security Considerations

### Implemented

- Input validation (Spring Validation)
- CORS protection
- RESTful API (no session hijacking)

### Recommendations for Production

- HTTPS/TLS encryption
- API key authentication
- Rate limiting
- SQL injection prevention (already via JPA)
- CSRF tokens
- Dependency scanning (OWASP)
- Regular security audits

## Deployment Considerations

### Development

- Docker Compose with all services
- Hot reload enabled
- Console logging
- H2 database option available

### Production

- Docker images optimized for size
- Environment-based configuration
- Kubernetes-ready
- Cloud-agnostic (AWS, GCP, Azure)
- Automated backups

## Performance Metrics

### Backend

- Response time: < 200ms (production target)
- Throughput: > 1000 req/sec
- Database queries: < 50ms each

### Frontend

- Page load: < 2s
- Interaction to response: < 100ms
- Bundle size: < 200KB (gzipped)

## Monitoring and Logging

### Backend

- Spring Boot Actuator endpoints
- SLF4J logging
- Error tracking ready for Sentry/NewRelic

### Frontend

- Browser DevTools
- Error boundary ready
- Performance monitoring ready

## Future Road Map

1. **Q1 2025**

   - API pagination
   - Advanced reporting
   - Audit logs

2. **Q2 2025**

   - Mobile app (React Native)
   - Real-time updates (WebSocket)
   - Advanced analytics

3. **Q3 2025**

   - Machine learning predictions
   - Demand forecasting
   - Inventory alerts

4. **Q4 2025**
   - Supplier integration
   - EDI support
   - Supply chain optimization
