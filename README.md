# Spring Boot 4 Features — Demo Project

Short: a small Spring Boot app demonstrating programmatic bean registration (modern BeanRegistrar) and a simple REST controller that returns messages from a selected MessageService implementation (EMAIL or SMS).

## Table of contents
- What this project demonstrates
- Prerequisites
- Build & run (Windows)
- Configuration
- Architecture and main classes
- Bean registration (how it works)
- REST endpoints
- Tests
- Troubleshooting & notes

## What this project demonstrates
- Programmatic bean registration using the (Spring 6 / Boot 4) BeanRegistrar API via `MessageServiceRegistrar`.
- A single `MessageService` interface with multiple implementations:
  - `EmailMessageService`
  - `SmsMessageService`
- How to switch implementations via configuration (`application.yaml` or environment variable).
- A simple `@RestController` (`MessageController`) exposing `/api/messages` returning a small record response.

## Prerequisites
- Java 21 (project `pom.xml` sets `java.version` to 21)
- Maven (or use the bundled wrapper `mvnw.cmd` on Windows)
- Windows PowerShell (examples below use PowerShell / curl)

## Build & run (Windows)
From project root (Windows PowerShell):

Build with the wrapper:

```powershell
.\mvnw.cmd clean package
```

Run from the build artifact:

```powershell
java -jar target\spring-boot-4-features-0.0.1-SNAPSHOT.jar
```

Or run directly using the wrapper (dev):

```powershell
.\mvnw.cmd spring-boot:run
```

## Configuration
Default config is in `src/main/resources/application.yaml`:

```yaml
spring:
  application:
    name: spring-boot-4-features

app:
  message-type: sms
```

- `app.message-type` controls which `MessageService` implementation is registered at startup. Supported values: `email`, `sms`.
- You can override this via environment variable or JVM property, for example:

PowerShell (env var for a single process):

```powershell
$env:APP_MESSAGE_TYPE = 'email'; .\mvnw.cmd spring-boot:run
```

Or pass as JVM property:

```powershell
java -Dapp.message-type=email -jar target\spring-boot-4-features-0.0.1-SNAPSHOT.jar
```

Note: Spring property name `app.message-type` maps to environment variable `APP_MESSAGE_TYPE`.

## Architecture & main classes
- `com.aamir.SpringBoot4FeaturesApplication` — main Spring Boot application class.
- `com.aamir.prgramaticBeanRegistration.config.ModernConfig` — configuration class that imports the registrar.
- `com.aamir.prgramaticBeanRegistration.registrar.MessageServiceRegistrar` — programmatic registrar using `BeanRegistrar` to register `messageService` bean based on `app.message-type`.
- `com.aamir.prgramaticBeanRegistration.service.MessageService` — interface with two methods: `getMessage()` and `getServiceType()`.
- Implementations:
  - `com.aamir.prgramaticBeanRegistration.email.EmailMessageService`
  - `com.aamir.prgramaticBeanRegistration.sms.SmsMessageService`
- `com.aamir.prgramaticBeanRegistration.controller.MessageController` — REST controller exposing `/api/messages`.

There is also a commented-out `TraditionalConfig` showing an alternate approach using a `BeanDefinitionRegistryPostProcessor` to register beans (kept as a reference in `src/main/java/.../TraditionalConfig.java`).

## Bean registration (how it works)
- At application startup Spring processes `@Configuration` classes. `ModernConfig` uses `@Import(MessageServiceRegistrar.class)`.
- `MessageServiceRegistrar` implements the `BeanRegistrar` API and implements `register(BeanRegistry registry, Environment env)`.
- The registrar reads `app.message-type` (default `email`) and registers a bean named `messageService` bound to either `EmailMessageService` or `SmsMessageService`.
- The controller receives a `MessageService` via constructor injection; the registered implementation will be injected.

## REST endpoints
- GET /api/messages
  - Description: returns a JSON object with `message` and `serviceType` fields.
  - Example response when `app.message-type: sms` (default in repo):

```json
{
  "message": " 📱 This is an SMS Message Service",
  "serviceType": "SMS"
}
```

- Example response when `app.message-type: email`:

```json
{
  "message": " 📧 This is an Email Message Service",
  "serviceType": "EMAIL"
}
```

Quick test using PowerShell (after app is running on default port 8080):

```powershell
Invoke-RestMethod -Uri http://localhost:8080/api/messages
```

Or using curl (Git Bash / WSL / curl for Windows):

```powershell
curl http://localhost:8080/api/messages
```

## Tests
- There is a simple context-load test in `src/test/java/com/aamir/SpringBoot4FeaturesApplicationTests.java`.

Run tests:

```powershell
.\mvnw.cmd test
```

## Troubleshooting & notes
- Java version mismatch: the `pom.xml` specifies Java 21. If you see compilation errors, confirm `java -version` and your IDE JDK settings.
- If the application registers the wrong `MessageService`, check `src/main/resources/application.yaml` and any environment variables or command-line properties you passed (`-Dapp.message-type=...`).
- The code includes two registration approaches:
  - Modern: `MessageServiceRegistrar` (active)
  - Traditional: `TraditionalConfig` (commented-out example using `BeanDefinitionRegistryPostProcessor`)
- If you add more implementations, update `MessageServiceRegistrar` switch logic accordingly.

## Where to look next (source layout)
- Application entry: `src/main/java/com/aamir/SpringBoot4FeaturesApplication.java`
- Configs: `src/main/java/com/aamir/prgramaticBeanRegistration/config/ModernConfig.java` and `TraditionalConfig.java`
- Registrar: `src/main/java/com/aamir/prgramaticBeanRegistration/registrar/MessageServiceRegistrar.java`
- Controller: `src/main/java/com/aamir/prgramaticBeanRegistration/controller/MessageController.java`
- Services & implementations: `src/main/java/com/aamir/prgramaticBeanRegistration/service/MessageService.java`, `email/EmailMessageService.java`, `sms/SmsMessageService.java`
- Tests: `src/test/java/com/aamir/SpringBoot4FeaturesApplicationTests.java`

---

If you want, I can also:
- Add a sample Postman collection or a tiny script to call the endpoint and assert the response.
- Change the default property to `email` in `application.yaml` and update README examples.
- Add an integration test that starts the app and asserts the `/api/messages` response for both `sms` and `email` modes.

Tell me which of those you'd like next.
