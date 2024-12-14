
# Car Sharing Service - Spring

## Description

This project is an implementation of a Car Sharing Service REST API, based on the requirements provided in mate-academy/jv-car-sharing-service. The service enables managing cars, users, payments, and rentals in a car-sharing system.

## Features

- Manage cars (add, view, edit, delete).
- Manage users (register, view profile, update, delete).
- Manage rentals (create rentals, view and return them).
- Search for available cars based on filters.

## Technologies Used

- Java: Core language for backend development.
- Spring Boot: Framework for building the REST API.
- Hibernate: ORM for database interaction.
- MySQL: Relational database for storing data.
JUnit 5: Testing framework.
Mockito: Mocking framework for unit tests.

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
`

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
