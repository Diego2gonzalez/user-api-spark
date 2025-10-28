# 🛍️ Collectibles Store API – Sprint 1

This project is the first stage of the **Java Web Application Development Challenge**.  
It implements an **API service** for an online collectibles store using **Java** and the **Spark framework**.

---

## 🚀 Project Overview

The goal of this Sprint is to create a functional **REST API** that manages users of the online store.  
It includes all CRUD operations (Create, Read, Update, Delete) and follows clean coding and modular configuration using Maven.

---

## 🧩 Technologies Used

- **Java 17**
- **Spark Framework** (for API routes)
- **Gson** (for JSON handling)
- **Logback** (for logging)
- **Maven** (for dependency management)

---

## 📦 Maven Configuration

Main dependencies used in the `pom.xml` file:

```xml
<dependencies>
    <!-- Spark Framework -->
    <dependency>
        <groupId>com.sparkjava</groupId>
        <artifactId>spark-core</artifactId>
        <version>2.9.4</version>
    </dependency>

    <!-- Gson for JSON serialization -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>

    <!-- Logback for logging -->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.2.11</version>
    </dependency>
</dependencies>
```

---

## 🧠 API Endpoints

| Method | Endpoint          | Description                        |
|:-------|:------------------|:-----------------------------------|
| GET    | `/users`          | Get all users                      |
| GET    | `/users/:id`      | Get a user by ID                   |
| POST   | `/users/:id`      | Add a new user                     |
| PUT    | `/users/:id`      | Edit an existing user              |
| OPTIONS| `/users/:id`      | Check if a user exists             |
| DELETE | `/users/:id`      | Delete a specific user             |

---

## 🧪 How to Run the Project

### 1️⃣ Clone the repository
```bash
git clone https://github.com/your-username/collectibles-store-api.git
```

### 2️⃣ Open in IntelliJ IDEA
- Open the project folder
- Wait for Maven to load dependencies

### 3️⃣ Run the application
- Open `Main.java`
- Click ▶️ **Run**

### 4️⃣ Access the API
- Base URL: `http://localhost:4567`
- Test endpoints using **Thunder Client** or **Postman**

Example:
```bash
GET http://localhost:4567/users
```

---

## ⚡ Example Requests (Thunder Client)

**POST /users/1**
```
Body: Diego
```
Response:
```
User added: Diego
```

**GET /users**
```
{"1": "Diego"}
```

**PUT /users/1**
```
Body: Diego G.
```
Response:
```
User updated: Diego G.
```

---

## 📚 Project Structure

```
📁 src
 ┣ 📂 main
 ┃ ┣ 📂 java
 ┃ ┃ ┗ 📜 Main.java
 ┃ ┗ 📂 resources
 ┃    ┗ 📜 logback.xml
 ┣ 📜 pom.xml
 ┣ 📜 README.md
```

---

## 🗒️ Key Decisions and Notes

- Spark was chosen for its simplicity and lightweight structure for small web apps.
- Gson was added to serialize and deserialize JSON data efficiently.
- Logback provides clean console logs for each request, improving debugging.

---

## 👥 Author

**Diego González Miranda** – Java Web Developer  
Project developed as part of the **Digital NAO Challenge – Sprint 1**

---

## 🏁 Next Steps (Sprint 2 Preview)

- Add Mustache templates for views  
- Implement forms for item offers  
- Add custom exception handling
