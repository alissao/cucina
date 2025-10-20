# Hexagonal Architecture Implementation

This project has been refactored to follow the **Hexagonal Architecture** (also known as Ports and Adapters) pattern. This architecture promotes separation of concerns, testability, and maintainability by isolating the core business logic from external dependencies.

## Architecture Overview

The hexagonal architecture consists of three main layers:

### 1. Domain Layer (Core Business Logic)
- **Location**: `src/main/kotlin/org/it/business/domain/`
- **Purpose**: Contains the core business logic, entities, value objects, and domain services
- **Dependencies**: No external dependencies (pure Kotlin)

#### Components:
- **Entities**: `MessageBundle` - Core business entities
- **Value Objects**: `Locale`, `MessageKey`, `Message` - Immutable objects representing concepts
- **Domain Services**: `MessageResolutionService` - Business logic that doesn't belong to entities
- **Repository Interfaces**: `MessageBundleRepository` - Ports defining persistence contracts

### 2. Application Layer (Use Cases & Application Services)
- **Location**: `src/main/kotlin/org/it/business/application/`
- **Purpose**: Orchestrates use cases and coordinates between domain and infrastructure
- **Dependencies**: Domain layer only

#### Components:
- **Ports**: `MessageServicePort` - Interfaces defining application contracts
- **Use Cases**: `GetMessageUseCase`, `GetAllMessagesUseCase`, `DetectLocaleUseCase` - Specific business operations
- **Application Services**: `MessageService` - Implementation of application ports

### 3. Infrastructure Layer (External Concerns)
- **Location**: `src/main/kotlin/org/it/business/infrastructure/`
- **Purpose**: Implements adapters for external systems (REST, persistence, etc.)
- **Dependencies**: Application and Domain layers

#### Components:
- **REST Adapters**: `I18nResource`, `GreetingResource` - HTTP endpoints
- **Persistence Adapters**: `YamlMessageBundleRepository` - YAML file storage implementation
- **Configuration**: `HexagonalConfiguration` - Dependency injection setup

## Key Benefits

### 1. **Separation of Concerns**
- Business logic is isolated from technical concerns
- Each layer has a single responsibility
- Changes in one layer don't affect others

### 2. **Testability**
- Domain layer can be tested without external dependencies
- Use cases can be tested with mock implementations
- Infrastructure adapters can be tested independently

### 3. **Flexibility**
- Easy to swap implementations (e.g., YAML to database storage)
- New adapters can be added without changing core logic
- Multiple interfaces for the same functionality

### 4. **Maintainability**
- Clear boundaries between layers
- Business logic is centralized and reusable
- Technical decisions are isolated to infrastructure

## Directory Structure

```
src/main/kotlin/org/it/business/
├── domain/
│   ├── entity/
│   │   └── MessageBundle.kt
│   ├── valueobject/
│   │   ├── Locale.kt
│   │   ├── MessageKey.kt
│   │   └── Message.kt
│   ├── service/
│   │   └── MessageResolutionService.kt
│   └── repository/
│       └── MessageBundleRepository.kt
├── application/
│   ├── port/
│   │   └── MessageServicePort.kt
│   ├── service/
│   │   └── MessageService.kt
│   └── usecase/
│       ├── GetMessageUseCase.kt
│       ├── GetAllMessagesUseCase.kt
│       └── DetectLocaleUseCase.kt
└── infrastructure/
    ├── adapter/
    ├── persistence/
    │   └── YamlMessageBundleRepository.kt
    ├── rest/
    │   ├── I18nResource.kt
    │   └── GreetingResource.kt
    └── config/
        └── HexagonalConfiguration.kt
```

## Dependency Flow

```
Infrastructure Layer → Application Layer → Domain Layer
```

- **Domain Layer**: No dependencies on other layers
- **Application Layer**: Depends only on Domain Layer
- **Infrastructure Layer**: Depends on both Application and Domain Layers

## Usage Examples

### 1. Getting a Message
```kotlin
// In a REST controller (Infrastructure)
val response = getMessageUseCase.execute(
    GetMessageRequest.Welcome(locale)
)
```

### 2. Adding a New Use Case
```kotlin
// 1. Define the use case in application layer
class UpdateMessageUseCase(
    private val messageService: MessageServicePort
) { ... }

// 2. Implement in infrastructure if needed
// 3. Wire up in HexagonalConfiguration
```

### 3. Changing Storage Implementation
```kotlin
// Create new implementation of MessageBundleRepository
class DatabaseMessageBundleRepository : MessageBundleRepository { ... }

// Update HexagonalConfiguration to use new implementation
@Produces
fun messageBundleRepository(): MessageBundleRepository {
    return DatabaseMessageBundleRepository()
}
```

## Migration from Original Architecture

The original monolithic structure has been transformed:

### Before (Traditional Layered):
- `MessageService` - Mixed business logic with infrastructure concerns
- `I18nResource` - Direct dependency on service implementation
- `UserMessages` - Data classes mixed with business logic

### After (Hexagonal):
- **Domain**: Pure business logic with `MessageBundle` entity and value objects
- **Application**: Orchestrated use cases with clear contracts
- **Infrastructure**: Adapters implementing the contracts

## Testing Strategy

### Unit Tests
- **Domain**: Test entities and domain services with pure Kotlin
- **Application**: Test use cases with mock ports
- **Infrastructure**: Test adapters with real or test implementations

### Integration Tests
- Test complete flows through REST endpoints
- Verify dependency injection works correctly
- Test persistence adapters with real data

## Future Enhancements

1. **Database Persistence**: Replace YAML files with database storage
2. **Caching**: Add caching adapter for improved performance
3. **Validation**: Add validation use cases
4. **Event Sourcing**: Implement domain events for audit trails
5. **API Versioning**: Add versioning to REST adapters

## Configuration

Dependency injection is handled by `HexagonalConfiguration` class, which:
- Wires up all dependencies following the hexagonal pattern
- Uses CDI annotations for Quarkus integration
- Provides clear separation between different implementations

This architecture ensures that your application remains maintainable, testable, and flexible as it grows.
