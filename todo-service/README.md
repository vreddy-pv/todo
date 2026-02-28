# Todo Service (Backend)

This repository contains the Spring Boot backend for the Todo application. It exposes a REST API for managing accounts, todos, and financial transactions. Originally the frontend and backend were part of a single repository; the Angular UI has since been split into its own project (`https://github.com/vreddy-pv/todo-list.git`).

## Key features

- Account management with balance tracking
- Transaction creation (credit/debit) with automatic balance updates
- Todo entity CRUD operations
- All data persisted using Spring Data JPA
- Service layer encapsulating business logic

## Getting started

```bash
# build the project
dotnet mvn clean package

# run the application
mvn spring-boot:run
```

The backend listens on port 8080 by default. Example endpoints:

```
GET /accounts
POST /transactions
GET /todos
```

## Development notes

- The package base was changed from `com.example` to `com.vrgt` on 2026-02-28.
- TransactionService handles balance updates and is used by `TransactionController`.
- The repository now contains only backend code; frontend is a separate repository.

## Repository history

Their independent histories are maintained in separate repositories:

- Backend: `c:\Veera\AI\claude-code\todo-service` (current project)
- Frontend: `https://github.com/vreddy-pv/todo-list.git`

The backend branch `appmod/java-upgrade-20260228044119` contains recent refactoring and package changes.

## Building & running

Use Maven to build and run. Java 21 is required for compilation.


