# PERSONAL FINANCIAL SYSTEM - STAGE 2 (REST API)
REST API for tracking revenues and expenses, built with Soring Boot. This is Stage 2 of a larger learning project divided into 4 stages.
It evolves the terminal application for Stage 1 (plain Java + JDBC), keeping the same MySQL database and the same fetaures,
now exposed as HTTP endpoints.

The next stages are a web interface (HTML, CSS and JavaScript) and the deployment on AWS.

## INDEX
 - Technologies Used
 - Features
 - Project Structure
 - API Endpoints 
 - How to Configure and Run
 - Request Examples
 - Next Steps

## TECHNOLOGIES USED
 - **Java 25**
 - **Spring Boot 4.1.2**
 - **Spring Data JPA**
 - **Validations**
 - **MySQL**
 - **Maven**

## FEATURES
 - Record transactions by logging the transaction amount, description, date, and category (Ex: food, leisure, salary);
 - Edit a transaction, with the ability to change any of its fields;
 - List transactions, showing all transactions and their information;
 - Delete transaction, removing a transaction of your choice;
 - Filter transaction, with the option to list transactions filtered by type, category, or date;
 - Register a new category by entering its name and type (revenue and expense);
 - List categories, showing all categories and their information;
 - Delete a category, removing a category of your choice;
 - Report of inflows and outflows, showing the total value of inflows and outflows;
 - Input Validation, required fields, size limits and positive amounts are checked before saving. For example, an empty description or a negative amount is rejected, using Bean Validation (`@Valid`);
 - Clear error handling, errors return a JSON message with the right HTTP status: `400` for invalid data or malformed JSON, `404` when an id does not exist, and `409` when a record is in use (for example, deleting a category that has transactions).

## PROJECT STRUCTURE
 ```
src/main/java/com/otaviosenger/financial/
├── PersonalFinancialSystemSbApplication.java
├── controller/
│   ├── CategoryController.java
│   ├── TransactionController.java
│   └── ReportController.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── model/
│   ├── Category.java
│   ├── Transaction.java
│   └── Type.java
└── repository/
    ├── CategoryRepository.java
    └── TransactionRepository.java

src/main/resources/
├── application.properties          (not in the repository, it holds your database password)
└── application.properties.example  (template with fake values)
```
 - **PersonalFinancialSystemApplication:** entry point of the application. It starts Spring Boot and the embedded Tomcat server;
 - **controller:** receives the HTTP requests, calls the repositories and returns the terminal menu of Stage 1 (`Main`);
 - **exception:** `ResourceNotFoundException` and `GlobalExceptionHandler`, which turn error into clear JSON messages with the right HTTP status;
 - **model:** JPA entities (`Category`, `Transaction`) and the `Type` enum, mapped to the database tables;
 - **repository:** interfaces that access the database through Spring Data JPA. They replace the DAOs of Stage 1, with no SQL or JDBC written by hand.

## API ENDPOINTS
The API runs on `http://localhost:8080`. All requests and responses use JSON.

 - **Categories**

   | Method | URL | Description | Success |
   |---|---|---|---|
   | GET | `/categories` | List all categories | `200` |
   | POST | `/categories` | Create a category (`name`, `type`) | `201` |
   | PUT | `/categories/{id}` | Replace a category | `200` |
   | DELETE | `/categories/{id}` | Delete a category | `204` |
 - **Transactions**

   | Method | URL | Description | Success |
   |---|---|---|---|
   | GET | `/transactions` | List all transactions, with optional filters (see below) | `200` |
   | POST | `/transactions` | Create a transaction (`description`, `amount`, `date`, `category`) | `201` |
   | PUT | `/transactions/{id}` | Replace a transaction | `200` |
   | DELETE | `/transactions/{id}` | Delete a transaction | `204` |
 - **Filters for `GET /transactions`** (query parameters, all optional)

   | Parameter | Example | Description |
      |---|---|---|
   | `type` | `?type=EXPENSES` | Transactions whose category is `REVENUE` or `EXPENSES` |
   | `categoryId` | `?categoryId=1` | Transactions of one category |
   | `startDate` and `endDate` | `?startDate=2026-01-01&endDate=2026-12-31` | Transactions in a date range, in `yyyy-MM-dd` format |
Only one filter is applied per request. If you send more then one, the date range wins over `categoryId`, and `categoryId` wins over `type`.

 - **Report**

   | Method | URL | Description | Success |
   |---|---|---|---|
   | GET | `/reports/inflow-outflow` | Total revenue, total expenses and balance | `200` |
 - **Error response**

   | Status | When it happens |
   |---|---|
   | `400 Bad Request` | Invalid data (failed validation) or malformed JSON |
   | `404 Not Found` | The id (or the category sent in a transaction) does not exist |
   | `409 Conflict` | The record is in use, for example deleting a category that has transactions |
 - **Notes**
   - `PUT` replaces the whole resource, so and all the fields;
   - In a transaction, the category is sent as an object with only its id: `"category": {"id": 1"}`.

## HOW TO CONFIGURE AND RUN

 - Prerequisites
   - JDK 25
   - MySQL running locally
   - Maven (optional, the project includes the Maven Wrapper `mvnw`)
1. Create the database

The application does not create tables (`ddl-auto=none), so run this script in MySQL first:
```sql
CREATE DATABASE personal_financial_system;
USE personal_financial_system;

CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type ENUM('REVENUE', 'EXPENSES') NOT NULL
);

CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descriptions VARCHAR(100) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    dates DATE NOT NULL,
    category_id INT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

INSERT INTO categories (name, type) VALUES
('FOOD', 'EXPENSES'),
('FUEL', 'EXPENSES'),
('LEISURE', 'EXPENSES'),
('BILLS', 'EXPENSES'),
('WAGE', 'REVENUE'),
('FREELANCE', 'REVENUE');
```
2. Configure the connection
The file `application.properties` is not in the repository,  because it holds your database password. Create it from the template:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```
Then open `src/main/resources/applicatoin.properties` and replace `your_user` and `your_password` with you MySQL credentials.
3. Run the application

From IntelliJ, run the class `PersonalFinancialSystemApplication`. Or from the terminal, in the project folder:
```bash
./mvnw spring-boot:run
```
If you get a permission error, run `chmod +x mvnw` first.

The API starts on `http://localhost:8080`. To check that it works:
```bash
curl http://localhost:8080/categories
```
 - Common problems
   - **"Port 8080 already in use":** another instance of the application is still running. Stop it and run again.
   - **"Failed to configure a DataSource":** the `application.properties` file is missing or has no `spring.datasource.*` settings.

## REQUEST EXAMPLES

The examples use `curl`. The `-i` flag show the HTTP status, Your ids will be different from the ones shown here.

 - Create a transaction
```bash
curl -i -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"description": "Lunch", "amount": 25.50, "date": "2026-11-24", "category": {"id": 1}}'
```
Response `201 Created`:
```json
{
  "id": 10,
  "description": "Lunch",
  "amount": 25.50,
  "date": "2026-11-24",
  "category": {
    "id": 1,
    "name": "FOOD",
    "type": "EXPENSES"
  }
}
```
 - Filter transactions
```bash
# by type
curl "http://localhost:8080/transactions?type=EXPENSES"

# by category
curl "http://localhost:8080/transactions?categoryId=1"

# by date range
curl "http://localhost:8080/transactions?startDate=2026-11-01&endDate=2026-11-30"
```
Keep the quotes, because of the `&` in the las URL.
 - Update and delete
```bash
# update (send all the fields)
curl -i -X PUT http://localhost:8080/transactions/10 \
  -H "Content-Type: application/json" \
  -d '{"description": "Lunch edited", "amount": 30.00, "date": "2026-11-24", "category": {"id": 1}}'

# delete (returns 204 with no body)
curl -i -X DELETE http://localhost:8080/transactions/10
```
 - Inflow and outflow report
```bash
curl http://localhost:8080/reports/inflow-outflow
```

```json
{
  "totalRevenue": 250.99,
  "totalExpenses": 150.85,
  "balance": 100.14
}
```
 - Error examples

Validation error, `400 Bad Request`:
```bash
curl -i -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"description": "", "amount": -5, "date": "2026-11-24", "category": {"id": 1}}'
```

```json
{
  "description": "Description is required",
  "amount": "Amount must be greater than zero"
}
```
Malformed JSON or invalid value, `400 Bad Request` (for example `"type": "WRONG"`):
```json
{
  "error": "Invalid JSON. Check the syntax, that type is REVENUE or EXPENSES, and that date is yyyy-MM-dd"
}
```
ID not found, `404 Not Found`:
```bash
curl -i -X DELETE http://localhost:8080/categories/999
```

```json
{
  "error": "Category 999 not found"
}
```
Record in use, `409 Conflict` (for example, deleting a category that has transactions):
```json
{
  "error": "Operation not allowed: the data is in use by other records, or references something that does not exist."
}
```

## NEXT STEPS   

This project is being built in 4 stages. Stage 2 (this one) is complete, and these are the next steps:

 - **Authentication (login):** protect the APi with Spring Security. I postponed it on purpose, because it is easier to design together with the web interface than to add it to a base API.
 - **Stage 3 - Web interface:** a frontend in HTML, CSS and JavaScript that consumes this API. It will also need CORS configuration, so the browser can call API from another origin.
 - **Stage 4 - Cloud deployment:** run the Spring Boot API on AWS, with the database migrated to Amazon RDS (MySQL).

 ### Know limitations and possible improvements

 - Only one filter applied per request in `GET/transactions`. Combining filters (for example, type and date range) is not supported yet.
 - Sending a transaction with a category without `id` (`"category": {}`) still returns a `500` instead of a `400`.
 - The inflow and outflow report covers all transaction. A version filtered by period would be more useful.
 - There are no automated tests yet.
