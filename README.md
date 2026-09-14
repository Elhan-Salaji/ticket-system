# Ticket System

Kleines Ticketsystem für Support-Anfragen, gebaut mit Java und Spring Boot.

## Tech Stack

- Java 21
- Spring Boot (Web, Data JPA, Validation, Actuator)
- PostgreSQL
- Maven
- Docker Compose (für die lokale Datenbank)
- JUnit 5, Mockito, Testcontainers

## Voraussetzungen

- JDK 21
- Docker Desktop (muss laufen, bevor die Anwendung startet)

## Projekt starten

1. Repository klonen:
   ```
   git clone https://github.com/Elhan-Salaji/ticket-system.git
   cd ticket-system
   ```
2. Docker Desktop starten und warten, bis es läuft.
3. Die Anwendung in IntelliJ starten (Run auf `TicketSystemApplication`) oder über das Terminal:
   ```
   mvn spring-boot:run
   ```
   Beim Start fährt Docker Compose automatisch eine PostgreSQL-Instanz mit hoch, siehe `compose.yaml`.
4. Die API ist danach unter `http://localhost:8080` erreichbar.

## API

| Methode | Endpunkt | Beschreibung |
|---|---|---|
| POST | /api/tickets | Neues Ticket anlegen |
| GET | /api/tickets/{id} | Ticket per ID abrufen |

Beispielanfragen liegen in `requests.http`.

## Tests ausführen

```
mvn test
```