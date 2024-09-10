# **Food Delivery Management System**

## **Overview**
This project is a **Food Delivery Management System** that simulates the workflow of a restaurant. The system handles key operations such as managing **chefs**, **orders**, and **delivery drivers**. It provides asynchronous handling of tasks like order preparation and delivery, allowing the system to scale and handle multiple requests efficiently.

The project is designed to provide a real-world example of building a **concurrent application**, showing how tasks like preparing food and delivering orders can be processed simultaneously by different workers, each working independently but within a structured and well-managed system.

## **Application Features**

### **1. Chef Management**
- **Create Chefs**: Add chefs to the system and mark them as available for handling orders.
- **Assign Chefs to Orders**: Automatically assign available chefs to customer orders and update their status to "busy" while they prepare the food.
- **Order Completion**: Mark an order as complete once the chef finishes preparing it, making the chef available for new orders.

### **2. Order Management**
- **Place Orders**: Customers can place new food orders. Each order will have an assigned chef who is responsible for preparing the meal.
- **Track Order Status**: Orders are tracked as they move through different stages, from "preparing" to "out for delivery" to "delivered."
- **Update Order Status**: The system updates the status of the order as it progresses through preparation and delivery stages.

### **3. Delivery Driver Management**
- **Create Delivery Drivers**: Add delivery drivers to the system and manage their availability.
- **Assign Delivery Drivers**: Once an order is prepared, the system assigns an available delivery driver to deliver the order to the customer.
- **Complete Delivery**: The driver marks the delivery as complete, and they become available for the next delivery task.

## **Technology Stack**
- **Java 17**
- **Spring Boot**: Backend framework to manage REST APIs and service orchestration.
- **MySQL**: Relational database to store chef, order, and driver information.
- **Maven**: Build and dependency management tool.

## **Multi-Threading Features**
While this application is focused on managing food delivery workflows, it also showcases how to handle real-world concurrency challenges:
- **Handling Multiple Chefs and Drivers**: Using concurrency tools to manage multiple chefs preparing orders and drivers delivering them, without blocking the system.
- **Efficient Task Management**: Tasks such as assigning orders and completing deliveries are handled asynchronously, ensuring that the system remains responsive even under heavy loads.

## **How to Run the Application**
1. Clone this repository:
    ```bash
    git clone https://github.com/chandrakanthrck/Online-Food-Delivery-System.git
    ```
2. Navigate to the project directory:
    ```bash
    cd fooddelivery
    ```
3. Run the application:
    ```bash
    mvn spring-boot:run
    ```
4. The application will be available at `http://localhost:8080`.

## **API Endpoints**

### **Chef Management**
- **Create a Chef**: 
    ```bash
    POST /chefs/create?name={chefName}
    ```
    Add a new chef to the system.

- **Assign Chef to an Order**: 
    ```bash
    PUT /chefs/assign/{chefId}/toOrder/{orderId}
    ```
    Assign an available chef to an order.

- **Complete an Order**: 
    ```bash
    PUT /chefs/completeOrder/{orderId}
    ```
    Mark the order as completed once the chef finishes preparing the food.

### **Order Management**
- **Place an Order**:
    ```bash
    POST /orders/place?customerName={customerName}&items={items}
    ```
    Place a new order.

- **View All Orders**:
    ```bash
    GET /orders/allorder
    ```
    Retrieve a list of all orders.

- **Update Order Status**:
    ```bash
    PUT /orders/updateorder/{orderId}?status={status}
    ```
    Update the status of an existing order.

### **Delivery Driver Management**
- **Create a Delivery Driver**:
    ```bash
    POST /drivers/create?name={driverName}
    ```
    Add a new delivery driver to the system.

- **Assign Driver to an Order**:
    ```bash
    PUT /drivers/assign/{driverId}/toOrder/{orderId}
    ```
    Assign a delivery driver to deliver an order.

- **Complete a Delivery**:
    ```bash
    PUT /drivers/completeDelivery/{orderId}
    ```
    Mark an order as delivered by the driver.

## **Project Structure**
```plaintext
src/
│
├── main/
│   ├── java/
│   │   └── com/fooddelivery/
│   │       ├── controller/   # REST API controllers
│   │       ├── entity/       # Data entities (Chef, Order, DeliveryDriver)
│   │       ├── repository/   # Repository interfaces for database interactions
│   │       └── service/      # Business logic (ChefService, OrderService, DeliveryDriverService)
│   └── resources/
│       ├── application.properties  # Configuration files
│       └── data.sql               # Initial data to seed the database
```

## **Learning Objectives**
- **Understand Task Assignment**: See how tasks (like assigning chefs or drivers) can be handled asynchronously, improving system responsiveness.
- **Handle Multiple Workers**: Learn how to manage multiple chefs and drivers working in parallel without conflicting assignments or blocking tasks.
- **Use Real-World Analogies**: By modeling chefs and drivers, you get a more intuitive grasp of how concurrency and task distribution work in real systems.

## **Contributing**
If you're interested in contributing to this project or have suggestions for new features, feel free to submit a pull request or open an issue.

## **License**
This project is licensed under the MIT License.
