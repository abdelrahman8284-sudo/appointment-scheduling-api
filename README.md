# 🏥 Appointment Scheduling API

A robust RESTful Backend service built with **Spring Boot 3.4** to manage medical clinics' appointments, doctors' schedules, and patient bookings.

## 🚀 Overview
This project provides a complete system for managing the lifecycle of a medical appointment. It allows administrators to manage doctor profiles, schedule available time slots, and enables patients to search and book these slots efficiently.

## 🛠️ Tech Stack
* **Java 21** (LTS)
* **Spring Boot 3.4.1**
* **Spring Data JPA** (Hibernate)
* **MySQL** (Database)
* **MapStruct** (For high-performance DTO mapping)
* **Lombok** (To reduce boilerplate code)
* **Swagger/OpenAPI 3** (For API Documentation)

## ✨ Key Features
* **Doctor Management:** CRUD operations for doctors and their specializations.
* * **Slot Scheduling:**
  * Create and update time slots for doctors.
  * Prevent overlapping slots for the same doctor.
  * Track slot status (`Available`, `Booked`, `Cancelled`).

* **Advanced Search:** Search for available slots by **Doctor ID** or **Specialization**.
* **Booking System:**
    * Book an available slot.
    * Automatic status updates for slots.
    * Cancel bookings.
* **Data Validation:** Strict validation using Jakarta Bean Validation with global exception handling.


## 📖 API Documentation (Swagger)
Once the application is running, you can access the interactive API documentation at:
`http://localhost:8080/swagger-ui/index.html`

## 🏗️ Database Schema
The system consists of three main entities:
1.  **Doctor:** Stores doctor info and their specializations.
2.  **AppointmentSlot:** Represents a specific time window (`startTime` to `endTime`) for a doctor.
3.  **Booking:** Connects a patient to a specific slot.

## 🚦 Getting Started

### Prerequisites
* JDK 21
* Maven 3.x
* MySQL Server

### Installation & Run
1. Clone the repository:
   ```bash
  git clone https://github.com/abdelrahman8284-sudo/appointment-scheduling-api
2.Configure your database in src/main/resources/application.properties.
3.Build the project: mvn clean install
4.Run the application:mvn spring-boot:run
   
