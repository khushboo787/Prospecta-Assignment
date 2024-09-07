# Prospecta-Assignment

## Product Management API
Overview
### The Product Management API is a RESTful service built with Spring Boot that interacts with the FakeStoreAPI to manage products. This API allows users to fetch, add, and manage products, providing essential functionalities such as product listing by category, adding new products, and handling product data.


### Features
1. List Products by Category: Fetch product details based on the provided category.
2. Add New Product: Add a new product to the FakeStore database using POST requests.
3. CSV File Handling: Process CSV files with values and formulas, evaluate the formulas, and return the results.

   
### Technologies Used
- Java
- Spring Boot
RestTemplate for API calls
CSV Parsing Library for file handling
Setup and Installation
Clone the repository:

bash

`
git clone https://github.com/your-username/product-management-api.git
cd product-management-api

`
Prerequisites: Ensure that you have the following installed:

Java 17 or higher
Maven
Spring Boot
Run the application:

bash
Copy code
mvn spring-boot:run
Access the API:
The API will be running on http://localhost:8080.

API Endpoints
Product Management
List Products by Category

Endpoint: GET /products/category/{category}
Description: Retrieves a list of products for the specified category.
Example:
bash
Copy code
GET http://localhost:8080/products/category/electronics
Response:
json
Copy code
[
  {
    "id": 1,
    "title": "Wireless Headphones",
    "price": 59.99,
    "category": "electronics"
  },
  {
    "id": 2,
    "title": "Smartphone",
    "price": 399.99,
    "category": "electronics"
  }
]
Add New Product

Endpoint: POST /products
Description: Adds a new product to the FakeStore database.
Request Body:
json
Copy code
{
  "title": "Laptop",
  "price": 899.99,
  "category": "electronics"
}
Response:
json
Copy code
{
  "id": 101,
  "title": "Laptop",
  "price": 899.99,
  "category": "electronics"
}
