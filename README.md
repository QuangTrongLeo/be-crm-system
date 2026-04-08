# CRM System Backend API

This project contains the backend REST APIs for the CRM System. This documentation summarizes the current available endpoints.

**Base URL Parameter:** `${api.crm-system-url}` (e.g., `/api/v1`)

---

## 1. Authentication Endpoints

### 1.1 Login User
Authenticate a user and return a JWT token.

*   **URL:** `POST /auth/login`
*   **Request Body:** `LoginRequest` (username, password)
*   **Response:** JWT token inside an `ApiResponse` container.

---

## 2. Customer Endpoints

*Note: The Customer controller root is mapped as `/customer/`.*

### 2.1 Create Customer
Creates a new customer.

*   **URL:** `POST /customer/`
*   **Request Body:** `CreateCustomerRequest`
*   **Response:** Returns `201 Created` with the newly created `Customer` entity.

### 2.2 Get All Customers
Fetches a list of all current customers.

*   **URL:** `GET /customer/`
*   **Response:** `200 OK` List of `Customer`.

### 2.3 Get Customer By ID
Fetches a specific customer by ID.

*   **URL:** `GET /customer/{id}`
*   **Path Variable:** `id` (String but parses to Long)
*   **Response:** `200 OK` and the `Customer`.

### 2.4 Update Customer
Updates details of an existing customer using partial updates.

*   **URL:** `PUT /customer/{id}`
*   **Path Variable:** `id`
*   **Request Body:** `UpdateCustomerRequest`
*   **Response:** `200 OK` and the updated `Customer`.

### 2.5 Search Customers
Filters or searches via customer text inputs (email, phone, name) and customer status.

*   **URL:** `GET /customer/search`
*   **Request Body (Note: Sending body in GET):** `SearchRequest` 
*   **Response:** `200 OK` List of filtered `Customer`s.

### 2.6 Delete Customer
Deletes a specific customer.
*Note: Requires `ADMIN` role (`@PreAuthorize("hasRole('ADMIN')")`).*

*   **URL:** `DELETE /customer/{id}`
*   **Path Variable:** `id`
*   **Response:** `200 OK` with an `ApiResponse<String>`.

---

## 3. Notes Endpoints

### 3.1 Fetch Customer Notes
Retrieves all note records attached to a single customer ID.

*   **URL:** `GET /notes/customer/{customerId}`
*   **Path Variable:** `customerId`
*   **Response:** `200 OK` List of `Note`s.

### 3.2 Add Note
Creates a new note associated with a Customer and a User.

*   **URL:** `POST /notes`
*   **Request Body:** `CreateNoteRequest` (customerId, userId, content, isImportant)
*   **Response:** `201 Created` with the created `Note`.

### 3.3 Mark Note As Important
Toggles or updates the boolean importance flag of a Note.

*   **URL:** `PATCH /notes/{noteId}/important`
*   **Query Parameter:** `isImportant` (boolean)
*   **Response:** `200 OK` with the updated `Note`.

---

## 4. Interactions Endpoints

### 4.1 Fetch Customer Interactions
Retrieves all logged interactions attached to a single customer ID.

*   **URL:** `GET /interactions/customer/{customerId}`
*   **Path Variable:** `customerId`
*   **Response:** `200 OK` List of `Interaction`s.

### 4.2 Log Interaction
Creates a new interaction log (e.g., CALL, EMAIL, MEETING, OTHER) for a Customer.

*   **URL:** `POST /interactions`
*   **Request Body:** `CreateInteractionRequest` (customerId, userId, interactionType, interactionDate, summary)
*   **Response:** `201 Created` with the created `Interaction`.

