# MongoDB Migration Guide

## Changes Made

### 1. Dependencies (pom.xml)
- ✅ Removed: `spring-boot-starter-data-jpa`
- ✅ Removed: `h2` database
- ✅ Added: `spring-boot-starter-data-mongodb`

### 2. Configuration (application.yml)
- ✅ Removed: H2 datasource configuration
- ✅ Added: MongoDB connection string
```yaml
spring:
  data:
    mongodb:
      uri: mongodb+srv://medtwinUser:prasad777@medtwin-cluster.uryx4jo.mongodb.net/medtwin?retryWrites=true&w=majority&appName=medtwin-cluster
      database: medtwin
```

### 3. Entity Annotations to Convert

Replace JPA annotations with MongoDB annotations in all model files:

| JPA Annotation | MongoDB Annotation |
|----------------|-------------------|
| `@Entity` | `@Document(collection="name")` |
| `@Table(name="...")` | Remove (use @Document) |
| `@Id` + `@GeneratedValue` | `@Id` (MongoDB auto-generates) |
| `@Column` | `@Field` (optional) |
| `@ManyToOne` | `@DBRef` or embed document |
| `@OneToMany` | Embed list or `@DBRef` |
| `@ElementCollection` | Keep as-is (works in MongoDB) |
| `@Enumerated` | Remove (MongoDB handles enums) |
| `@PrePersist` | Keep (works in MongoDB) |
| `@Transactional` | Remove (MongoDB doesn't use JPA transactions) |

### 4. Repository Changes

Replace `JpaRepository` with `MongoRepository`:

```java
// Before (JPA)
public interface DeviceRequirementRepository extends JpaRepository<DeviceRequirement, Long> {
    List<DeviceRequirement> findByDeviceType(String deviceType);
}

// After (MongoDB)
public interface DeviceRequirementRepository extends MongoRepository<DeviceRequirement, String> {
    List<DeviceRequirement> findByDeviceType(String deviceType);
}
```

**Note**: ID type changes from `Long` to `String` (MongoDB uses String IDs)

### 5. Model Files to Update

1. ✅ `DeviceRequirement.java`
2. ✅ `SystemArchitecture.java`
3. ✅ `SystemComponent.java`
4. ✅ `DigitalTwinState.java`
5. ✅ `SimulationRun.java`
6. ✅ `SimulationDataPoint.java`
7. ✅ `AIInsight.java`

### 6. Service Changes

Update all services to handle String IDs instead of Long:

```java
// Before
public DeviceRequirement getRequirement(Long id) {
    return repository.findById(id).orElseThrow(...);
}

// After
public DeviceRequirement getRequirement(String id) {
    return repository.findById(id).orElseThrow(...);
}
```

### 7. Controller Changes

Update all controller path variables from `Long` to `String`:

```java
// Before
@GetMapping("/requirements/{id}")
public ResponseEntity<DeviceRequirement> getRequirement(@PathVariable Long id) {

// After
@GetMapping("/requirements/{id}")
public ResponseEntity<DeviceRequirement> getRequirement(@PathVariable String id) {
```

## Quick Conversion Steps

### Step 1: Update Model Classes

For each model file, make these changes:

```java
// Remove these imports
import jakarta.persistence.*;

// Add these imports
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

// Change annotations
@Document(collection = "device_requirements")  // Instead of @Entity @Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequirement {
    
    @Id  // MongoDB auto-generates String IDs
    private String id;  // Changed from Long to String
    
    // Remove @Column annotations (optional in MongoDB)
    private String deviceType;
    
    // For relationships, use @DBRef or embed
    @DBRef  // If you want to reference another document
    private SystemArchitecture architecture;
    
    // Or embed the document directly (no annotation needed)
    private List<SystemComponent> components;
}
```

### Step 2: Update Repositories

```java
// Change from JpaRepository to MongoRepository
public interface DeviceRequirementRepository extends MongoRepository<DeviceRequirement, String> {
    List<DeviceRequirement> findByDeviceType(String deviceType);
    // Query methods work the same way
}
```

### Step 3: Remove @Transactional

MongoDB doesn't use JPA transactions, so remove `@Transactional` annotations from services.

## Testing After Migration

1. Start MongoDB connection
2. Run the application
3. Test endpoints:
```bash
# Health check
curl http://localhost:8080/api/health

# Create requirement (will get String ID back)
curl -X POST http://localhost:8080/api/requirements \
  -H "Content-Type: application/json" \
  -d '{"deviceType":"Ventilator","targetMarket":"EU",...}'

# Response will have String ID like: "507f1f77bcf86cd799439011"
```

## Benefits of MongoDB

1. **Flexible Schema** - Easy to add new fields without migrations
2. **Embedded Documents** - Can embed components directly in architecture
3. **Better for JSON** - Native JSON storage
4. **Scalability** - Horizontal scaling with sharding
5. **Cloud-Ready** - MongoDB Atlas integration

## Potential Issues

1. **ID Type Change** - All IDs are now Strings (MongoDB ObjectId)
2. **No Transactions** - Remove @Transactional annotations
3. **Relationships** - Decide between @DBRef (reference) or embedding
4. **Queries** - Some complex JPA queries may need rewriting

## Recommendation

For this application, I recommend:
- **Embed** `SystemComponent` in `SystemArchitecture` (they're tightly coupled)
- **Embed** `SimulationDataPoint` in `SimulationRun` (time-series data)
- **Use @DBRef** for `DeviceRequirement` → `SystemArchitecture` (separate entities)
- **Use @DBRef** for `SystemArchitecture` → `SimulationRun` (separate entities)

This gives you the best of both worlds: embedded documents for performance, references for flexibility.
