# shopNOW

A comprehensive e-commerce desktop application for managing products, customers, sales, and inventory with reporting capabilities.

## Overview

shopNOW is a JavaFX-based desktop application designed for retail businesses to manage their operations efficiently. It provides features for customer management, product inventory, sales processing, and comprehensive reporting functionality.

## Features

- **Customer Management**
  - Add and view customers
  - Customer search and filtering
  - Password recovery functionality

- **Product Management**
  - Add and view products
  - Stock management and tracking
  - Product inventory overview

- **Sales & Orders**
  - Create and process sales transactions
  - Shopping cart with item management
  - Order history tracking
  - Memento pattern for draft management

- **Reporting**
  - Daily sales reports
  - CSV and PDF export formats
  - Report sharing functionality
  - Multiple report formatting options

- **Security**
  - User authentication and login system
  - Password hashing with bcrypt
  - Secure credential management

## Tech Stack

- **Framework:** JavaFX 21 (GUI)
- **Database:** SQLite 3.49.1.0
- **Build Tool:** Maven
- **Java Version:** 11+
- **Security:** jbcrypt 0.4 (password hashing)

## Project Structure

```
shopNOW/
├── src/main/java/com/example/shopnow/
│   ├── controller/              # MVC controllers for views
│   ├── DPApply/                 # Design patterns implementation
│   │   ├── Command.java         # Command pattern for operations
│   │   ├── Singleton.java       # Singleton for shared resources
│   │   ├── Observer.java        # Observer pattern for reminders
│   │   ├── CartMemento.java     # Memento pattern for draft carts
│   │   └── Report/              # Report strategy pattern
│   ├── Utill/                   # Utility classes
│   │   ├── DatabaseConnection.java
│   │   └── DatabaseSetup.java
│   └── HelloApplication.java    # Application entry point
├── src/main/resources/          # FXML layouts and assets
├── pom.xml                      # Maven configuration
└── data.sql                     # Database initialization script
```

## Design Patterns Used

- **Command Pattern:** For executing operations like AddProduct, ConfirmSale, SearchCustomer
- **Singleton Pattern:** Ensures single instance of shared resources
- **Observer Pattern:** For reminder notifications system
- **Memento Pattern:** For cart draft management
- **Strategy Pattern:** For different report formatting (CSV, PDF)
- **MVC Architecture:** Clean separation of concerns

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- SQLite 3

### Installation & Setup

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd shopnow
   ```

2. Build the project:
   ```bash
   ./mvnw clean package
   ```

3. Initialize the database:
   ```bash
   sqlite3 data.db < data.sql
   ```

4. Run the application:
   ```bash
   ./mvnw javafx:run
   ```

Or run directly:
```bash
java -jar target/shopnow-1.0-SNAPSHOT.jar
```

## Usage

1. **Login:** Start with the entry/login page to authenticate
2. **Home:** Access main dashboard with navigation to different modules
3. **Manage Customers:** Add new customers and view existing records
4. **Manage Products:** Add products, manage stock levels
5. **Create Sales:** Process customer orders and manage shopping cart
6. **View Reports:** Generate and export sales reports in CSV or PDF

## Database

The application uses SQLite for data persistence. The database schema is initialized through `data.sql` which sets up all necessary tables for:
- Users/Customers
- Products
- Inventory/Stock
- Orders/Sales
- Reports

## Building & Running

### Clean and Build
```bash
./mvnw clean compile
```

### Run Tests (if applicable)
```bash
./mvnw test
```

### Package for Distribution
```bash
./mvnw clean package
```

## License

This project is provided as-is for educational and commercial purposes.

## Author

shopNOW Development Team
