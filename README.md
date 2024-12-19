
# Car Sharing Service - Spring

## Description

This project is an implementation of a Car Sharing Service REST API. The service enables managing cars, users, payments, and rentals in a car-sharing system.

## Features

- Manage cars (add, view, edit, delete).
- Manage users (register, view profile, update, delete).
- Manage rentals (create rentals, view and return them).
- Search for available cars based on filters.

## Technologies Used

Here is a list of all the technologies and libraries used in the project along with their respective versions:

#### **Spring Boot Framework**
- **spring-boot-starter-parent**: 3.3.4
- **spring-boot-starter-data-jpa**: Included in Spring Boot
- **spring-boot-starter-web**: Included in Spring Boot
- **spring-boot-starter-security**: Included in Spring Boot
- **spring-boot-starter-test**: Included in Spring Boot

#### **Database**
- **H2 Database**: 2.x (for testing)
- **MySQL Connector**: 8.0.28
- **Hibernate Core**: 6.5.3.Final
- **Liquibase Core**: 4.27.0

#### **Validation**
- **Jakarta Validation API**: 3.0.2

#### **Security and JWT**
- **Spring Security**: Included in Spring Boot
- **JSON Web Token (JWT)**:
  - **jjwt-api**: 0.11.5
  - **jjwt-impl**: 0.11.5
  - **jjwt-jackson**: 0.11.5

#### **Dependency Injection**
- **Lombok**: Included (Optional)
- **Lombok-Mapstruct-Binding**: 0.2.0
- **Mapstruct**:
  - **mapstruct**: 1.5.5.Final
  - **mapstruct-processor**: 1.5.5.Final

#### **Payment Processing**
- **Stripe Java SDK**: 28.0.0

#### **Messaging**
- **Telegram Bots API**: 5.3.0

#### **Testing**
- **Spring Security Test**: Included in Spring Boot
- **Spring Boot Test**: Included in Spring Boot

#### **Code Quality**
- **Checkstyle Plugin**: 3.3.0

#### **Build and Plugins**
- **Maven**:
  - **Spring Boot Maven Plugin**: Included in Spring Boot
  - **Liquibase Maven Plugin**: 4.27.0
  - **Maven Checkstyle Plugin**: 3.3.0

## Installation

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
`

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
