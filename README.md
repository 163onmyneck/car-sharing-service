
# Car Sharing Service - Spring

## Description

This project is an implementation of a Car Sharing Service REST API. The service enables managing cars, users, payments, and rentals in a car-sharing system.

## Features

- Manage cars (add, view, edit, delete).
- Manage users (register, view profile, update, delete).
- Manage rentals (create rentals, view and return them).
- Search for available cars based on filters.

## Technologies Used

- Java: Core language for backend development.
- Spring Boot: Framework for building the REST API.
- Hibernate: ORM for database interaction.
- Database: MySQL for production; H2 for testing
- JUnit 5: Testing framework.
- Mockito: Mocking framework for unit tests.
- Build Tool: Maven
- Containerization: Docker
- Security: Spring Security with JWT for authentication
- Payment Integration: Stripe API for handling payments
- Notifications: Telegram Bot API for administrative alerts

## Installation

### Prerequisites

- Java 11 or higher
- Maven
- MySQL

### Steps

1. Clone the repository:
   `bash git clone https://github.com/163onmyneck/car-sharing-service.git`

2. Navigate to the project directory:`bash cd car-sharing-service`

3. Create a MySQL database:`sql
   CREATE DATABASE name`, (instead of `name` you can put your database name);

4. Update the `env.template` file with your MySQL database credentials, also
you can use `resources/application.properties` for your MySQL database credentials;

5. Build the project using Maven:
`bash
   mvn clean package
`

6. Run the application:
 `bash
   mvn spring-boot:run

### Database Schema

#### Users
| Column Name   | Type      | Constraints                 | Description                                |
|---------------|-----------|-----------------------------|--------------------------------------------|
| `id`          | BIGINT    | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for each user.          |
| `email`       | VARCHAR   | NOT NULL, UNIQUE            | User's email address.                     |
| `first_name`  | VARCHAR   | NOT NULL                    | User's first name.                        |
| `last_name`   | VARCHAR   | NOT NULL                    | User's last name.                         |
| `password`    | VARCHAR   | NOT NULL                    | Hashed user password.                     |
| `is_deleted`  | BOOLEAN   | DEFAULT FALSE               | Soft delete indicator.                    |
| `tg_chat_id`  | BIGINT    | NULLABLE                    | Telegram chat ID for notifications.       |

---

#### Roles
| Column Name   | Type      | Constraints                 | Description                                |
|---------------|-----------|-----------------------------|--------------------------------------------|
| `id`          | BIGINT    | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for each role.          |
| `role_name`   | ENUM      | NOT NULL                    | Role name: `MANAGER`, `CUSTOMER`.         |

**Relation**: Many-to-Many with `Users` through `users_roles`.

---

#### Cars
| Column Name   | Type      | Constraints                 | Description                                |
|---------------|-----------|-----------------------------|--------------------------------------------|
| `id`          | BIGINT    | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for each car.           |
| `model`       | VARCHAR   | NOT NULL                    | Model of the car.                         |
| `brand`       | VARCHAR   | NOT NULL                    | Brand of the car.                         |
| `car_type`    | ENUM      | NOT NULL                    | Car type: `SEDAN`, `SUV`, `UNIVERSAL`, etc. |
| `inventory`   | INT       | NOT NULL                    | Number of cars available.                 |
| `fee_usd`     | DECIMAL   | NOT NULL                    | Rental fee per day in USD.                |
| `is_deleted`  | BOOLEAN   | DEFAULT FALSE               | Soft delete indicator.                    |

---

#### Rentals
| Column Name       | Type      | Constraints                 | Description                                |
|-------------------|-----------|-----------------------------|--------------------------------------------|
| `id`              | BIGINT    | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for each rental.        |
| `rental_date`     | DATE      | NOT NULL                    | Start date of the rental.                 |
| `return_date`     | DATE      | NOT NULL                    | Expected return date.                     |
| `actual_return_date` | DATE   | NULLABLE                    | Actual date when the car was returned.    |
| `car_id`          | BIGINT    | FOREIGN KEY                 | Associated car (from `Cars`).             |
| `user_id`         | BIGINT    | FOREIGN KEY                 | Associated user (from `Users`).           |
| `is_deleted`      | BOOLEAN   | DEFAULT FALSE               | Soft delete indicator.                    |
| `is_active`       | BOOLEAN   | DEFAULT TRUE                | Indicates whether the rental is ongoing.  |

---

#### Payments
| Column Name       | Type      | Constraints                 | Description                                |
|-------------------|-----------|-----------------------------|--------------------------------------------|
| `id`              | BIGINT    | PRIMARY KEY                 | Unique identifier for each payment.       |
| `rental_id`       | BIGINT    | FOREIGN KEY                 | Associated rental (from `Rentals`).       |
| `status`          | ENUM      | NOT NULL                    | Payment status: `PENDING`, `PAID`, etc.   |
| `type`            | ENUM      | NOT NULL                    | Payment type: `PAYMENT`, `FINE`.          |
| `session_url`     | VARCHAR   | NOT NULL                    | URL for payment session.                  |
| `session_id`      | VARCHAR   | NOT NULL                    | Identifier for payment session.           |
| `amount_to_pay`   | DECIMAL   | NOT NULL                    | Total amount to be paid in USD.           |
| `is_deleted`      | BOOLEAN   | DEFAULT FALSE               | Soft delete indicator.                    |

---

### Relationships
1. **Users and Roles**:
   - Many-to-Many relationship through the `users_roles` join table.
2. **Users and Rentals**:
   - One-to-Many relationship (a user can have multiple rentals).
3. **Cars and Rentals**:
   - One-to-Many relationship (a car can be rented multiple times).
4. **Rentals and Payments**:
   - One-to-One relationship (payment can not exist without rental).


## Usage

User Operations:
- Register and log in to access car rental services.
- Browse available cars and filter based on criteria.
- Initiate rentals and process payments through the integrated payment gateway.

## Administrator Operations:

- Manage car inventory by adding, updating, or removing vehicles.
- Monitor active rentals and track overdue returns.
- Manage user roles and permissions.

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch `git checkout -b feature-branch\`.
3. Make your changes.
4. Commit your changes `git commit -m 'Add some feature'`.
5. Push to the branch `git push origin feature-branch`.
6. Open a pull request.

## Contact

For any questions or suggestions, feel free to reach out:

- GitHub: [163onmyneck](https://github.com/163onmyneck)
- email: olegtrusskiykcm01@gmail.com
