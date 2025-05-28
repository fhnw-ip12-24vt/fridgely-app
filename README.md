# Project Overview

Fridgely is an interactive Java application for managing refrigerator inventory, designed to run on a Raspberry Pi. It tracks food items, suggests recipes, and includes gamified elements like a multiplayer mode. The application supports multiple displays and is localized for English, German, and French.

## Key Functionalities

*   **Inventory Management:** Track products and stock in a refrigerator.
*   **Recipe Management:** Store and utilize recipes based on available ingredients.
*   **Gamification:** Includes game modes (e.g., multiplayer) and tutorials to engage users.
*   **Multi-Display Support:** Manages separate displays for the main application and for showing scanned items.
*   **Localization:** Supports multiple languages (English, German, French).
*   **User Interface:** Built using Java Swing.
*   **Deployment:** Designed for deployment and execution on a Raspberry Pi.

## Technology Stack

*   **Primary Language:** Java (Version 21)
*   **Core Framework:** Spring Boot (Version 3.4.5)
*   **Data Persistence:** Spring Data JPA, Hibernate, QueryDSL, SQLite
*   **Build Tool:** Apache Maven
*   **GUI:** Java Swing
*   **Logging:** SLF4J
*   **Utility:** Lombok
*   **Testing:** JUnit, Mockito

---

Thanks to Klemen Zmahar for the original Fridgely concept and initial development.

---

### Special Thanks

Huge thanks to [**Klemen Zmahar**](https://gitlab.fhnw.ch/klemen.zmahar)  for generously spending over 3 hours helping me set up, debug, and fix my Continuous Deployment pipeline. Especially despite the fact that you are not in our IP12 Team. Your guidance was instrumental in getting it all working — couldn't have done it without you!