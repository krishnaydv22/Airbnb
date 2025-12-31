## 🔁 Saga Pattern Implementation

This project implements the **Saga Pattern** to handle distributed transactions involved in the booking confirmation workflow.  
Instead of relying on a single long-running database transaction, the system breaks the process into **multiple smaller steps**, each coordinated through events.

This approach improves **scalability, fault tolerance, and system resilience**, making it suitable for real-world backend systems.

### 📌 Why Saga Pattern?

- Prevents long-running and blocking database transactions
- Enables asynchronous and non-blocking workflows
- Maintains data consistency across multiple steps
- Allows failure handling using compensating actions
- Well-suited for microservices and distributed systems

### 🧩 Booking Saga Flow

1. A user initiates a booking request
2. A Saga event is created with status `PENDING`
3. The event is published to a Redis-based Saga queue
4. The Saga consumer asynchronously picks the event
5. Availability for the Airbnb listing is validated
6. If availability is confirmed:
   - Booking status is updated to `CONFIRMED`
7. If any step fails:
   - A compensating action is triggered
   - Booking is either rolled back or marked as `FAILED`

This ensures **eventual consistency** without tightly coupling services.

---

## 🧵 Redis Usage

Redis is used as an **event-driven messaging mechanism** to coordinate Saga steps asynchronously.

Instead of direct synchronous calls between components, events are exchanged through Redis, allowing independent processing and better fault isolation.

### 🔹 Redis Queue Details

- Redis **List** is used as the Saga event queue
- Events are serialized as JSON before publishing
- Producer pushes events to the queue
- Consumer continuously polls and processes events

- ## 🧩 CQRS (Command Query Responsibility Segregation)

This project follows the **CQRS pattern** by clearly separating **write (command)** operations from **read (query)** operations.

The goal of CQRS in this system is to:
- Simplify complex business logic
- Improve scalability and performance
- Avoid mixing read and write concerns
- Work seamlessly with Saga-based workflows

---

### 📌 CQRS in Booking Workflow

- **Commands** handle state-changing operations
- **Queries** handle data retrieval operations
- Read and write models are separated at the repository and service level

This design aligns well with **event-driven architectures** and distributed systems.

---

### ✍️ Command Side (Write Model)

The **command side** is responsible for:
- Creating bookings
- Initiating Saga events
- Updating booking and availability state
- Executing compensating actions

Key characteristics:
- Contains business rules and valida



Queue Name: saga:events

## 🔄 Saga Booking Sequence Diagram (Text-Based)

Below is a simplified sequence diagram representing the booking confirmation Saga flow:

```text
User
 │
 │ 1. Create Booking Request
 ▼
Booking Service
 │
 │ 2. Publish Saga Event (PENDING)
 ▼
Redis Queue (saga:events)
 │
 │ 3. Consume Event
 ▼
Saga Event Consumer
 │
 │ 4. Check Availability
 ▼
Availability Service
 │
 ├── Available ──► Confirm Booking
 │
 └── Not Available ──► Trigger Compensation

