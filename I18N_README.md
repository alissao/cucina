# Internationalization (i18n) Implementation

This project implements internationalization using YAML message files with SnakeYAML for parsing and Quarkus CDI for dependency injection.

## Features

- **Type-safe message access** using Kotlin data classes
- **YAML-based message files** for easy translation management
- **Automatic locale detection** from HTTP headers
- **Parameter substitution** in messages
- **CDI integration** with Quarkus

## Supported Languages

- English (en) - Default
- Spanish (es)
- French (fr)

## File Structure

```
src/main/resources/
├── data/
│   ├── messages_en.yaml
│   ├── messages_es.yaml
│   └── messages_fr.yaml
└── application.yml

src/main/kotlin/org/it/business/i18n/
├── UserMessages.kt
├── MessageService.kt
└── I18nResource.kt
```

## Usage Examples

### 1. Basic Message Retrieval

```kotlin
@Inject
private lateinit var messageService: MessageService

// Get welcome message in default locale
val welcome = messageService.getWelcomeMessage()

// Get welcome message in specific locale
val welcomeEs = messageService.getWelcomeMessage(Locale("es"))
```

### 2. Parameterized Messages

```kotlin
// Get greeting with name parameter
val greeting = messageService.getGreeting("John", Locale("es"))
// Returns: "¡Hola, John!"
```

### 3. REST API Endpoints

The implementation provides several REST endpoints for testing:

#### Get Welcome Message
```bash
GET /api/i18n/welcome?lang=es
```

#### Get Greeting
```bash
GET /api/i18n/greeting?name=John&lang=fr
```

#### Get User Messages
```bash
GET /api/i18n/user/created?lang=es
```

#### Get Error Messages
```bash
GET /api/i18n/error/validation/required?lang=fr
```

#### Get Success Messages
```bash
GET /api/i18n/success/dataSaved?lang=es
```

#### Get All Messages
```bash
GET /api/i18n/messages?lang=fr
```

### 4. Locale Detection

The service automatically detects locale from:
1. Query parameter `lang` (highest priority)
2. `Accept-Language` HTTP header
3. Default locale (English)

## Adding New Languages

1. Create a new YAML file in `src/main/resources/data/`:
   ```yaml
   # messages_de.yaml (German example)
   welcome: "Willkommen in unserer Anwendung!"
   greeting: "Hallo, {name}!"
   # ... other messages
   ```

2. Update the `MessageService` to include the new locale:
   ```kotlin
   @Named("messages_de") private val germanMessages: UserMessages
   ```

3. Add the locale to the `getMessagesForLocale` method:
   ```kotlin
   "de" -> germanMessages
   ```

4. Update `application.yml` to include the new locale:
   ```yaml
   quarkus:
     locale:
       supported:
         - en
         - es
         - fr
         - de
   ```

## Adding New Messages

1. Add the message to all YAML files:
   ```yaml
   # In messages_en.yaml
   new_message: "New message in English"
   
   # In messages_es.yaml
   new_message: "Nuevo mensaje en español"
   ```

2. Update the `UserMessages` data class if needed
3. Add a getter method in `MessageService` if desired

## Configuration

The i18n configuration is in `application.yml`:

```yaml
quarkus:
  locale:
    default: en
    supported:
      - en
      - es
      - fr

i18n:
  messages:
    base-name: messages
    encoding: UTF-8
    cache-duration: 3600
    fallback-locale: en
```

## Dependencies

The implementation uses:
- `org.yaml:snakeyaml:2.2` - For YAML parsing
- `io.quarkus:quarkus-config-yaml` - For YAML configuration support

## Testing

You can test the implementation by:

1. Starting the application
2. Making requests to the `/api/i18n/*` endpoints
3. Using different `lang` query parameters or `Accept-Language` headers

Example with curl:
```bash
# English (default)
curl http://localhost:8080/api/i18n/welcome

# Spanish
curl http://localhost:8080/api/i18n/welcome?lang=es

# French with Accept-Language header
curl -H "Accept-Language: fr" http://localhost:8080/api/i18n/welcome
```
