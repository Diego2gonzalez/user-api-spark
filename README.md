# 🧩 Java Spark Web App

This project is a simple web application made with **Java Spark Framework**.  
It allows users to view, add, edit, and delete products.  
It also includes a small admin panel and database connection.

---

## 📘 Table of Contents
1. [📝 Description](#-description)  
2. [🧰 Technologies Used](#-technologies-used)  
3. [⚙️ Features](#️-features)  
4. [📦 Maven Configuration](#-maven-configuration)  
5. [🗂️ Project Structure](#️-project-structure)  
6. [🚀 How to Run the Project](#-how-to-run-the-project)  
7. [🖼️ Screenshots](#️-screenshots)  
8. [⚖️ License](#️-license)

---

## 📝 Description
**Java_Spark_for_web_apps** is a simple CRUD (Create, Read, Update, Delete) project using **Spark Java**, **Gson**, and **Mustache** templates.  
It connects to a database and allows basic product management.

---

## 🧰 Technologies Used
- **Java 17+**
- **Spark Framework 2.9.4**
- **Gson 2.10.1**
- **Logback 1.2.11**
- **Mustache Templates**
- **HTML, CSS, JavaScript**
- **MySQL**

---

## ⚙️ Features
✅ Product list in table view  
✅ Add new product  
✅ Edit product  
✅ Delete product  
✅ Admin panel for easy control  
✅ Modular code structure  

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

## 🗂️ Project Structure
```
Java_Spark_for_web_apps/
├── DB/                     # SQL scripts
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/
│   │   │       ├── productapi/
│   │   │       │   ├── DBConnection.java
│   │   │       │   ├── ExceptionHandlerModule.java
│   │   │       │   ├── InvalidInputException.java
│   │   │       │   ├── Main.java
│   │   │       │   ├── Product.java
│   │   │       │   ├── ProductNotFoundException.java
│   │   │       │   └── ProductService.java
│   │   │       └── userapi/
│   │   └── resources/
│   │       ├── public/
│   │       │   ├── script.js
│   │       │   └── styles.css
│   │       └── templates/
│   │           ├── form.mustache
│   │           ├── index.mustache
│   │           ├── modals.mustache
│   │           ├── products.mustache
│   │           └── users.mustache
│   └── logback.xml
└── pom.xml
```

---

## 🚀 How to Run the Project
1. Install **Java 17+** and **Maven**.  
2. Clone the repository:  
   ```bash
   git clone https://github.com/yourusername/Java_Spark_for_web_apps.git
   ```
3. Go to the project folder:  
   ```bash
   cd Java_Spark_for_web_apps
   ```
4. Build and run the project:  
   ```bash
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.example.productapi.Main"
   ```
5. Open your browser and go to:  
   **http://localhost:4567**

---

## 🖼️ Screenshots

🏠 **Home Page**  
![Home Page](https://i.imgur.com/9P8Qv2N.png)

⚙️ **Admin Panel**  
![Admin Panel](https://i.imgur.com/vBgYsQq.png)

📋 **Product Table View (CRUD)**  
![Product Table](https://i.imgur.com/hyKf7Tx.png)

➕ **Add Product Modal**  
![Add Product](https://i.imgur.com/mMFiVKR.png)

✏️ **Edit Product Modal**  
![Edit Product](https://i.imgur.com/83vTXon.png)

❌ **Delete Product Modal**  
![Delete Product](https://i.imgur.com/n2B5vXp.png)

---

## ⚖️ License
This project is **free software**.  
You can use, modify, and share it for learning and development.

---

📚 *Made with ❤️ using Java Spark Framework*
