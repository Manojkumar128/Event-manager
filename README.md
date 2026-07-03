# Event Manager Backend

## Overview

This repository contains a Spring Boot backend for an event registration system. The application provides REST APIs to manage events and registrations, including optional admin operations.

## Features

- Event management with create and lookup operations
- User registration for events with capacity checks
- Registration listing and cancelation
- Admin endpoints for managing event records
- In-memory H2 database for development and testing
- Basic HTTP authentication for admin APIs

## Architecture

The backend is structured as a standard Spring Boot application with the following layers:

- `model` — JPA entity classes for `Event` and `Registration`
- `repository` — Spring Data JPA repositories
- `controller` — REST controllers for public and admin APIs
- `config` — security configuration and optional application settings

## Data Model

- `Event`
  - `id`: primary key
  - `title`
  - `description`
  - `location`
  - `startDateTime`
  - `endDateTime`
  - `capacity`

- `Registration`
  - `id`: primary key
  - `event`: reference to `Event`
  - `userName`
  - `userEmail`
  - `registeredAt`
  - `active`

## API Endpoints

### Public APIs

- `GET /api/events` — list all events
- `GET /api/events/{id}` — retrieve event details
- `POST /api/events` — create a new event
- `POST /api/registrations` — register a user for an event
- `GET /api/registrations` — list registrations, filter by `email` or `eventId`
- `DELETE /api/registrations/{id}` — cancel a registration

### Admin APIs

- `GET /api/admin/events` — list all events as an admin
- `DELETE /api/admin/events/{id}` — delete an event as an admin

## Admin Access

The application uses basic authentication for admin endpoints. The default admin credentials are:

- Username: `admin`
- Password: `Admin123!`

## Development Setup

### Prerequisites

- Java 17
- Maven

### Build and run

From the `backend` directory:

```bash
./mvnw test
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`.

## H2 Database Console

The H2 console is available at:

- `http://localhost:8080/h2-console`

JDBC URL: `jdbc:h2:mem:eventdb`

## Notes

- The application uses an in-memory H2 database, so data is reset on restart.
- Sample events are seeded on startup when the database is empty.
