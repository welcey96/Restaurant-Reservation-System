# Events Reservation System

A Spring Boot–based web application for managing event reservations with seating, menus, and scheduling features.

## Tech Stack

[![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/17/)   [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)   [![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-3.x-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-data-jpa)   [![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-005F0F?logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)  
[![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1?logo=mysql&logoColor=white)](https://dev.mysql.com/downloads/)  [![Spring Validation](https://img.shields.io/badge/Spring%20Validation-3.x-6DB33F?logo=spring&logoColor=white)](https://spring.io/guides/gs/validating-form-input/) [![Spring Security Crypto](https://img.shields.io/badge/Spring%20Security%20Crypto-3.x-6DB33F?logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)  
[![Spring Mail](https://img.shields.io/badge/Spring%20Mail-3.x-6DB33F?logo=spring&logoColor=white)](https://spring.io/guides/gs/handling-form-submission/)  [![Tomcat](https://img.shields.io/badge/Apache%20Tomcat-10.x-F8DC75?logo=apachetomcat&logoColor=black)](http://tomcat.apache.org/)

---

✅ **Core Features**
- Event creation & management
- Seating times & reservation scheduling
- Menu creation and customization
- Email notifications for reservations
- Secure data handling with Spring Security Crypto
- Server-side rendering with Thymeleaf

## Installation
✨ Make sure you have a running MySQL instance✨

## Configuration

All environment-specific settings (database connection, email, default login, etc.) are located in the **`application.properties`** file.

- Update the **database URL, username, and password** to match your local setup.
- Default login credentials are also defined in this file.

Example snippet from `src/main/resources/application.properties`:
