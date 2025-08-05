# 📊 Smart Parking Lot System - Class Diagrams

## Table of Contents
1. [Overview](#overview)
2. [Core Entity Classes](#core-entity-classes)
3. [Service Layer Classes](#service-layer-classes)
4. [Repository Layer Classes](#repository-layer-classes)
5. [Controller Layer Classes](#controller-layer-classes)
6. [DTO Classes](#dto-classes)
7. [Exception Classes](#exception-classes)
8. [Complete Class Relationship Diagram](#complete-class-relationship-diagram)

---

## Overview

This document provides comprehensive class diagrams for the Smart Parking Lot Management System, illustrating the relationships, dependencies, and structure of all major components.

### Class Diagram Notation
- **→** : Association/Dependency
- **◇** : Aggregation
- **◆** : Composition
- **▷** : Inheritance/Implementation
- **+** : Public
- **-** : Private
- **#** : Protected

---

## Core Entity Classes

### Vehicle Entity
```mermaid
classDiagram
    class Vehicle {
        -String licensePlate
        -VehicleType vehicleType
        -String ownerName
        -String phoneNumber
        -LocalDateTime createdAt
        +Vehicle()
        +Vehicle(licensePlate, vehicleType, ownerName)
        +String getLicensePlate()
        +VehicleType getVehicleType()
        +String getOwnerName()
        +String getPhoneNumber()
        +LocalDateTime getCreatedAt()
        +void setLicensePlate(String)
        +void setVehicleType(VehicleType)
        +void setOwnerName(String)
        +void setPhoneNumber(String)
        +void setCreatedAt(LocalDateTime)
    }
    
    class VehicleType {
        <<enumeration>>
        MOTORCYCLE
        CAR
        BUS
    }
    
    Vehicle --> VehicleType
```

### ParkingSpot Entity
```mermaid
classDiagram
    class ParkingSpot {
        -Long spotId
        -String spotNumber
        -Integer floor
        -ParkingSpotType spotType
        -Boolean isAvailable
        -ParkingTicket currentTicket
        -LocalDateTime createdAt
        +ParkingSpot()
        +ParkingSpot(spotNumber, floor, spotType)
        +void occupy()
        +void makeAvailable()
        +Boolean getIsAvailable()
        +String getSpotNumber()
        +Integer getFloor()
        +ParkingSpotType getSpotType()
        +ParkingTicket getCurrentTicket()
        +void setCurrentTicket(ParkingTicket)
    }
    
    class ParkingSpotType {
        <<enumeration>>
        MOTORCYCLE
        COMPACT
        LARGE
    }
    
    ParkingSpot --> ParkingSpotType
    ParkingSpot --> ParkingTicket
```

### ParkingTicket Entity
```mermaid
classDiagram
    class ParkingTicket {
        -Long ticketId
        -String ticketNumber
        -Vehicle vehicle
        -ParkingSpot parkingSpot
        -LocalDateTime entryTime
        -LocalDateTime exitTime
        -BigDecimal fee
        -TicketStatus status
        -LocalDateTime createdAt
        +ParkingTicket()
        +ParkingTicket(ticketNumber, vehicle, spot, entryTime)
        +void markAsExited(LocalDateTime)
        +void markAsPaid(BigDecimal)
        +String getTicketNumber()
        +Vehicle getVehicle()
        +ParkingSpot getParkingSpot()
        +LocalDateTime getEntryTime()
        +LocalDateTime getExitTime()
        +BigDecimal getFee()
        +TicketStatus getStatus()
    }
    
    class TicketStatus {
        <<enumeration>>
        ACTIVE
        PAID
        LOST
    }
    
    ParkingTicket --> Vehicle
    ParkingTicket --> ParkingSpot
    ParkingTicket --> TicketStatus
```

### Floor Entity
```mermaid
classDiagram
    class Floor {
        -Long id
        -Integer floorNumber
        -String floorName
        -Boolean underMaintenance
        -String maintenanceReason
        -LocalDateTime maintenanceSince
        -LocalDateTime createdAt
        +Floor()
        +Floor(floorNumber)
        +Floor(floorNumber, floorName)
        +void setMaintenanceMode(Boolean, String)
        +Integer getFloorNumber()
        +String getFloorName()
        +Boolean getUnderMaintenance()
        +String getMaintenanceReason()
        +LocalDateTime getMaintenanceSince()
    }
```

---

## Service Layer Classes

### ParkingService
```mermaid
classDiagram
    class ParkingService {
        -VehicleRepository vehicleRepository
        -ParkingTicketRepository ticketRepository
        -ParkingSpotAllocationService spotAllocationService
        -FeeCalculationService feeCalculationService
        +ParkingResponse parkVehicle(VehicleEntryRequest)
        +ParkingResponse exitVehicle(VehicleExitRequest)
        +ParkingLotStatus getParkingLotStatus()
        -Vehicle getOrCreateVehicle(VehicleEntryRequest)
        -boolean isVehicleCurrentlyParked(String)
        -String generateTicketNumber()
    }
    
    ParkingService --> VehicleRepository
    ParkingService --> ParkingTicketRepository
    ParkingService --> ParkingSpotAllocationService
    ParkingService --> FeeCalculationService
```

### ParkingSpotAllocationService
```mermaid
classDiagram
    class ParkingSpotAllocationService {
        -ParkingSpotRepository parkingSpotRepository
        -FloorManagementService floorManagementService
        +ParkingSpot allocateSpot(VehicleType)
        +ParkingSpot allocateSpotWithFloorPreference(VehicleType, Integer)
        +void freeSpot(ParkingSpot)
        +long getAvailableSpotCount()
        +long getAvailableSpotCountByType(ParkingSpotType)
        -Optional~ParkingSpot~ findBestAvailableSpot(VehicleType)
        -Optional~ParkingSpot~ findAvailableSpotByType(ParkingSpotType)
        -Optional~ParkingSpot~ findBestAvailableSpotOnFloor(VehicleType, Integer)
        -Optional~ParkingSpot~ findAvailableSpotByTypeAndFloor(ParkingSpotType, Integer)
    }
    
    ParkingSpotAllocationService --> ParkingSpotRepository
    ParkingSpotAllocationService --> FloorManagementService
```

### FeeCalculationService
```mermaid
classDiagram
    class FeeCalculationService {
        -BigDecimal MOTORCYCLE_RATE
        -BigDecimal CAR_RATE
        -BigDecimal BUS_RATE
        -BigDecimal MINIMUM_FEE
        +BigDecimal calculateFee(VehicleType, LocalDateTime, LocalDateTime)
        +long calculateDurationInMinutes(LocalDateTime, LocalDateTime)
        -BigDecimal getHourlyRate(VehicleType)
    }
```

### FloorManagementService
```mermaid
classDiagram
    class FloorManagementService {
        -ParkingSpotRepository parkingSpotRepository
        -FloorRepository floorRepository
        +void addFloor(int, int, int, int)
        +ParkingSpot addParkingSpot(int, ParkingSpotType)
        +List~FloorStatus~ getAllFloorsStatus()
        +FloorStatus getFloorStatus(int)
        +void removeParkingSpot(String)
        +void removeFloor(int)
        +void setFloorMaintenanceMode(int, boolean, String)
        +List~Integer~ getFloorsUnderMaintenance()
        +List~Integer~ getAvailableFloors()
        -void createFloorSpots(int, int, int, int)
        -FloorStatus createFloorStatus(int, List~ParkingSpot~)
        -String getTypePrefix(ParkingSpotType)
    }
    
    FloorManagementService --> ParkingSpotRepository
    FloorManagementService --> FloorRepository
```

---

## Repository Layer Classes

### Repository Interfaces
```mermaid
classDiagram
    class JpaRepository~T,ID~ {
        <<interface>>
        +List~T~ findAll()
        +Optional~T~ findById(ID)
        +T save(T)
        +void delete(T)
        +void deleteById(ID)
        +boolean existsById(ID)
        +long count()
    }
    
    class VehicleRepository {
        <<interface>>
        +Optional~Vehicle~ findByLicensePlate(String)
        +List~Vehicle~ findByVehicleType(VehicleType)
        +List~Vehicle~ findByOwnerNameContaining(String)
    }
    
    class ParkingSpotRepository {
        <<interface>>
        +List~ParkingSpot~ findByFloor(Integer)
        +Optional~ParkingSpot~ findBySpotNumber(String)
        +List~ParkingSpot~ findByIsAvailableTrue()
        +List~ParkingSpot~ findBySpotTypeAndIsAvailableTrue(ParkingSpotType)
        +List~ParkingSpot~ findAvailableSpotsByTypeOrderByFloorAndSpotNumber(ParkingSpotType)
        +List~ParkingSpot~ findAvailableSpotsByTypeExcludingFloors(ParkingSpotType, List~Integer~)
        +long countByIsAvailableTrue()
        +long countBySpotTypeAndIsAvailableTrue(ParkingSpotType)
    }
    
    class ParkingTicketRepository {
        <<interface>>
        +Optional~ParkingTicket~ findByTicketNumber(String)
        +List~ParkingTicket~ findByStatus(TicketStatus)
        +List~ParkingTicket~ findByEntryTimeBetween(LocalDateTime, LocalDateTime)
        +Optional~ParkingTicket~ findByLicensePlateAndStatus(String, TicketStatus)
        +long countActiveTickets()
    }
    
    class FloorRepository {
        <<interface>>
        +Optional~Floor~ findByFloorNumber(Integer)
        +List~Floor~ findByUnderMaintenanceFalse()
        +List~Floor~ findByUnderMaintenanceTrue()
        +List~Integer~ findAvailableFloorNumbers()
        +boolean existsByFloorNumber(Integer)
    }
    
    VehicleRepository --|> JpaRepository
    ParkingSpotRepository --|> JpaRepository
    ParkingTicketRepository --|> JpaRepository
    FloorRepository --|> JpaRepository
```

---

## Controller Layer Classes

### REST Controllers
```mermaid
classDiagram
    class ParkingController {
        -ParkingService parkingService
        +ResponseEntity~ParkingResponse~ parkVehicle(VehicleEntryRequest)
        +ResponseEntity~ParkingResponse~ exitVehicle(VehicleExitRequest)
        +ResponseEntity~ParkingLotStatus~ getParkingLotStatus()
        +ResponseEntity~String~ healthCheck()
    }
    
    class FloorManagementController {
        -FloorManagementService floorManagementService
        +ResponseEntity~List~FloorStatus~~ getAllFloors()
        +ResponseEntity~FloorStatus~ getFloorStatus(Integer)
        +ResponseEntity~List~Floor~~ getFloorsUnderMaintenance()
        +ResponseEntity~String~ setFloorMaintenanceMode(Integer, MaintenanceModeDto)
        +ResponseEntity~String~ addFloor(Integer, FloorConfigDto)
        +ResponseEntity~String~ addParkingSpot(Integer, ParkingSpotType)
    }
    
    ParkingController --> ParkingService
    FloorManagementController --> FloorManagementService
```

---

## DTO Classes

### Request DTOs
```mermaid
classDiagram
    class VehicleEntryRequest {
        -String licensePlate
        -VehicleType vehicleType
        -String ownerName
        +VehicleEntryRequest()
        +VehicleEntryRequest(licensePlate, vehicleType, ownerName)
        +String getLicensePlate()
        +VehicleType getVehicleType()
        +String getOwnerName()
        +void setLicensePlate(String)
        +void setVehicleType(VehicleType)
        +void setOwnerName(String)
    }
    
    class VehicleExitRequest {
        -String licensePlate
        +VehicleExitRequest()
        +VehicleExitRequest(licensePlate)
        +String getLicensePlate()
        +void setLicensePlate(String)
    }
    
    class MaintenanceModeDto {
        -Boolean underMaintenance
        -String maintenanceReason
        +Boolean getUnderMaintenance()
        +String getMaintenanceReason()
        +void setUnderMaintenance(Boolean)
        +void setMaintenanceReason(String)
    }
```

### Response DTOs
```mermaid
classDiagram
    class ParkingResponse {
        -boolean success
        -String message
        -String ticketNumber
        -String spotNumber
        -LocalDateTime entryTime
        -LocalDateTime exitTime
        -Long durationMinutes
        -BigDecimal fee
        -LocalDateTime timestamp
        +ParkingResponse success(String)
        +ParkingResponse failure(String)
        +boolean isSuccess()
        +String getMessage()
        +String getTicketNumber()
        +String getSpotNumber()
        +LocalDateTime getEntryTime()
        +LocalDateTime getExitTime()
        +Long getDurationMinutes()
        +BigDecimal getFee()
        +LocalDateTime getTimestamp()
    }
    
    class ParkingLotStatus {
        -long totalSpots
        -long availableSpots
        -long occupiedSpots
        -long activeTickets
        -long motorcycleSpots
        -long compactSpots
        -long largeSpots
        -LocalDateTime timestamp
        +long getTotalSpots()
        +long getAvailableSpots()
        +long getOccupiedSpots()
        +long getActiveTickets()
        +long getMotorcycleSpots()
        +long getCompactSpots()
        +long getLargeSpots()
        +LocalDateTime getTimestamp()
    }
    
    class FloorStatus {
        -int floorNumber
        -int totalSpots
        -int availableSpots
        -int occupiedSpots
        -int motorcycleSpots
        -int compactSpots
        -int largeSpots
        -int availableMotorcycleSpots
        -int availableCompactSpots
        -int availableLargeSpots
        -boolean underMaintenance
        -String maintenanceReason
        +int getFloorNumber()
        +int getTotalSpots()
        +int getAvailableSpots()
        +int getOccupiedSpots()
        +boolean isUnderMaintenance()
        +String getMaintenanceReason()
    }
```

---

## Exception Classes

### Custom Exceptions
```mermaid
classDiagram
    class RuntimeException {
        <<Java Standard>>
    }
    
    class NoAvailableSpotException {
        +NoAvailableSpotException(String)
    }
    
    class VehicleNotFoundException {
        +VehicleNotFoundException(String)
    }
    
    class VehicleAlreadyParkedException {
        +VehicleAlreadyParkedException(String)
    }
    
    class FloorMaintenanceException {
        +FloorMaintenanceException(String)
    }
    
    class InvalidParkingOperationException {
        +InvalidParkingOperationException(String)
    }
    
    NoAvailableSpotException --|> RuntimeException
    VehicleNotFoundException --|> RuntimeException
    VehicleAlreadyParkedException --|> RuntimeException
    FloorMaintenanceException --|> RuntimeException
    InvalidParkingOperationException --|> RuntimeException
```

---

## Complete Class Relationship Diagram

### System Overview
```mermaid
classDiagram
    %% Controllers
    class ParkingController {
        +parkVehicle()
        +exitVehicle()
        +getParkingLotStatus()
    }
    
    class FloorManagementController {
        +getAllFloors()
        +setFloorMaintenanceMode()
    }
    
    %% Services
    class ParkingService {
        +parkVehicle()
        +exitVehicle()
        +getParkingLotStatus()
    }
    
    class ParkingSpotAllocationService {
        +allocateSpot()
        +freeSpot()
    }
    
    class FeeCalculationService {
        +calculateFee()
    }
    
    class FloorManagementService {
        +setFloorMaintenanceMode()
        +getAllFloorsStatus()
    }
    
    %% Repositories
    class VehicleRepository {
        +findByLicensePlate()
    }
    
    class ParkingSpotRepository {
        +findAvailableSpots()
    }
    
    class ParkingTicketRepository {
        +findByLicensePlateAndStatus()
    }
    
    class FloorRepository {
        +findByUnderMaintenanceTrue()
    }
    
    %% Entities
    class Vehicle {
        -licensePlate
        -vehicleType
        -ownerName
    }
    
    class ParkingSpot {
        -spotNumber
        -floor
        -spotType
        -isAvailable
    }
    
    class ParkingTicket {
        -ticketNumber
        -entryTime
        -exitTime
        -fee
        -status
    }
    
    class Floor {
        -floorNumber
        -underMaintenance
        -maintenanceReason
    }
    
    %% DTOs
    class VehicleEntryRequest
    class VehicleExitRequest
    class ParkingResponse
    class FloorStatus
    
    %% Relationships - Controllers to Services
    ParkingController --> ParkingService
    FloorManagementController --> FloorManagementService
    
    %% Relationships - Services to Repositories
    ParkingService --> VehicleRepository
    ParkingService --> ParkingTicketRepository
    ParkingService --> ParkingSpotAllocationService
    ParkingService --> FeeCalculationService
    
    ParkingSpotAllocationService --> ParkingSpotRepository
    ParkingSpotAllocationService --> FloorManagementService
    
    FloorManagementService --> ParkingSpotRepository
    FloorManagementService --> FloorRepository
    
    %% Relationships - Repositories to Entities
    VehicleRepository --> Vehicle
    ParkingSpotRepository --> ParkingSpot
    ParkingTicketRepository --> ParkingTicket
    FloorRepository --> Floor
    
    %% Entity Relationships
    ParkingTicket --> Vehicle
    ParkingTicket --> ParkingSpot
    
    %% DTO Relationships
    ParkingController --> VehicleEntryRequest
    ParkingController --> VehicleExitRequest
    ParkingController --> ParkingResponse
    FloorManagementController --> FloorStatus
```

### Data Flow Diagram
```mermaid
flowchart TD
    A[Client Request] --> B[ParkingController]
    B --> C[ParkingService]
    C --> D[ParkingSpotAllocationService]
    C --> E[FeeCalculationService]
    C --> F[VehicleRepository]
    C --> G[ParkingTicketRepository]
    D --> H[ParkingSpotRepository]
    D --> I[FloorManagementService]
    I --> J[FloorRepository]
    
    F --> K[(Vehicle Table)]
    G --> L[(ParkingTicket Table)]
    H --> M[(ParkingSpot Table)]
    J --> N[(Floor Table)]
    
    C --> O[ParkingResponse]
    O --> B
    B --> P[Client Response]
```

### Package Structure
```mermaid
classDiagram
    namespace learn.spring.smart_parking_lot {
        class SmartParkingLotApplication
    }
    
    namespace learn.spring.smart_parking_lot.controller {
        class ParkingController
        class FloorManagementController
    }
    
    namespace learn.spring.smart_parking_lot.service {
        class ParkingService
        class ParkingSpotAllocationService
        class FeeCalculationService
        class FloorManagementService
    }
    
    namespace learn.spring.smart_parking_lot.repository {
        class VehicleRepository
        class ParkingSpotRepository
        class ParkingTicketRepository
        class FloorRepository
    }
    
    namespace learn.spring.smart_parking_lot.model {
        class Vehicle
        class ParkingSpot
        class ParkingTicket
        class Floor
        class VehicleType
        class ParkingSpotType
        class TicketStatus
    }
    
    namespace learn.spring.smart_parking_lot.dto {
        class VehicleEntryRequest
        class VehicleExitRequest
        class ParkingResponse
        class ParkingLotStatus
        class FloorStatus
        class MaintenanceModeDto
    }
    
    namespace learn.spring.smart_parking_lot.exception {
        class NoAvailableSpotException
        class VehicleNotFoundException
        class VehicleAlreadyParkedException
        class FloorMaintenanceException
    }
    
    namespace learn.spring.smart_parking_lot.config {
        class DataInitializer
    }
```

---

## Design Patterns in Class Structure

### 1. Repository Pattern
```mermaid
classDiagram
    class Repository~T~ {
        <<interface>>
        +save(T entity)
        +findById(ID id)
        +findAll()
        +delete(T entity)
    }
    
    class JpaRepository~T,ID~ {
        <<interface>>
    }
    
    class VehicleRepository {
        <<interface>>
    }
    
    class VehicleRepositoryImpl {
        +findByLicensePlate()
    }
    
    Repository --|> JpaRepository
    VehicleRepository --|> JpaRepository
    VehicleRepositoryImpl ..|> VehicleRepository
```

### 2. Service Layer Pattern
```mermaid
classDiagram
    class BusinessService {
        <<interface>>
    }
    
    class ParkingService {
        -repositories
        +businessMethods()
    }
    
    class TransactionManager {
        +beginTransaction()
        +commit()
        +rollback()
    }
    
    ParkingService ..|> BusinessService
    ParkingService --> TransactionManager
```

### 3. DTO Pattern
```mermaid
classDiagram
    class RequestDto {
        <<abstract>>
        +validate()
    }
    
    class ResponseDto {
        <<abstract>>
        +success()
        +failure()
    }
    
    class VehicleEntryRequest {
        +validate()
    }
    
    class ParkingResponse {
        +success()
        +failure()
    }
    
    VehicleEntryRequest --|> RequestDto
    ParkingResponse --|> ResponseDto
```

---

## Class Dependencies and Injection

### Dependency Injection Diagram
```mermaid
flowchart TD
    A[Spring Application Context] --> B[ParkingController]
    A --> C[FloorManagementController]
    A --> D[ParkingService]
    A --> E[ParkingSpotAllocationService]
    A --> F[FeeCalculationService]
    A --> G[FloorManagementService]
    A --> H[VehicleRepository]
    A --> I[ParkingSpotRepository]
    A --> J[ParkingTicketRepository]
    A --> K[FloorRepository]
    
    B --> D
    C --> G
    D --> E
    D --> F
    D --> H
    D --> J
    E --> I
    E --> G
    G --> I
    G --> K
```

### Annotation-based Configuration
```mermaid
classDiagram
    class Component {
        <<annotation>>
    }
    
    class Service {
        <<annotation>>
    }
    
    class Repository {
        <<annotation>>
    }
    
    class Controller {
        <<annotation>>
    }
    
    class Autowired {
        <<annotation>>
    }
    
    class ParkingService {
        <<@Service>>
        <<@Transactional>>
        -VehicleRepository vehicleRepository <<@Autowired>>
    }
    
    Service --|> Component
    Repository --|> Component
    Controller --|> Component
    ParkingService --> Service
    ParkingService --> Autowired
```

---

This comprehensive class diagram documentation provides a complete view of the Smart Parking Lot system's structure, showing all classes, their relationships, dependencies, and how they work together to implement the business requirements. The diagrams use standard UML notation and are organized by architectural layers for easy understanding and maintenance.
