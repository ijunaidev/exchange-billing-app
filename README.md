# Exchange Billing Application

This Spring Boot project calculates the net payable amount on a bill based on user discounts and real-time currency exchange rates.

## 🛠 Features

- Real-time currency conversion using **Exchange Rates API (https://www.exchangerate-api.com/)**
- Discount rules based on user type & tenure
- Caching of exchange rates to reduce API calls
- JWT-based authentication
- REST endpoint: `/api/calculate`
- Unit tests with Mockito
- Static code analysis with Checkstyle
- Code coverage reports via JaCoCo
- Maven build and CLI support

---

## 🗄 Database Setup

This project uses **MySQL**. You can quickly set it up using the provided SQL dump.

### 🛠 Steps to Import the Database Schema

1. Open your terminal and run the following commands:

```bash
# Step 1: Create the database
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS exchange_billing;"
````
2: Import the schema
```bash
mysql -u root -p exchange_billing < schema/schema.sql
```

## 🚀 How to Run

### Build the project
```bash
mvn clean install
```

### Run application
```bash
mvn spring-boot:run
```

### Run Tests
```bash
mvn test
```

## ✅ Generate Reports
1. Checkstyle Report
```bash
mvn checkstyle:check
```

2. Coverage Report (JaCoCo)
```bash
mvn clean verify
```

## 👨‍💻 CURLs

### Generate Token
```bash
curl --location 'http://localhost:8080/api/v1/auth/login' \
--header 'Content-Type: application/json' \
--data '{
  "username": "admin",
  "password": "password"
}'
```

### Calculate endpoint
```bash
curl --location 'http://localhost:8080/api/calculate' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc0NTA0MjU3MiwiZXhwIjoxNzQ1MDQ2MTcyfQ.4s8qDP2GPkq-Wm9DWZo8FUXiHf_1P2pnIrUMfS2RKbs' \
--data '{
  "items": [
    {
      "name": "Headphones",
      "category": "NON_GROCERY",
      "price": 400
    },
    {
      "name": "Milk",
      "category": "GROCERY",
      "price": 100
    }
  ],
  "userType": "CUSTOMER",
  "tenureInYears": 3,
  "originalCurrency": "USD",
  "targetCurrency": "JPY"
}'
```