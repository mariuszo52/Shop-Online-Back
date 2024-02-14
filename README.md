# Online Game Key Store

## Overview

This web application serves as an online game key store, facilitating the purchase and distribution of CD keys for various games. It employs modern technologies to ensure secure transactions, user management, and efficient administration.

## Features

### Admin Panel
- **User Management:** Admins can create, modify, and delete user accounts.
- **Product Management:** Easily add, update, or remove game CD keys from the store.
- **Order Tracking:** Monitor and manage customer orders for efficient processing.
- **Analytics Dashboard:** Access insightful data on sales, popular products, and user activity.

### User Panel
- **Account Registration/Login:** Users can create accounts, log in securely, and manage their profiles.
- **Browse and Purchase:** Browse available game CD keys, add them to the cart, and complete transactions securely.
- **Order History:** View past orders and their status.
- **Profile Customization:** Users can update their personal information and preferences.

### Additional Features
- **Email Notifications:** Utilizes Jakarta Mail for sending order confirmations, updates, and promotional emails.
- **OAuth2 Integration:** Ensures secure user authentication using OAuth2 for a seamless login experience.
- **JWT Token:** Implements JWT tokens for secure and efficient user authorization.
- **Database Management:** Utilizes Liquibase for database schema versioning and MySQL as the backend database.
- **Containerization:** Docker is used for containerizing the application, facilitating easy deployment.

## Technologies Used

- **Backend Framework:** Spring Boot
- **Frontend Framework:** React
- **Database:** MySQL
- **Containerization:** Docker
- **Authentication:** OAuth2, JWT Token
- **Database Versioning:** Liquibase

## Getting Started

1. Clone the repository.
2. Set up the MySQL database and update application properties.
3. Build and run the Docker container.
4. Access the application at `http://localhost:8080`.

Feel free to explore and enhance the functionality as needed. For any issues or improvements, please submit a pull request or contact the administrator.


