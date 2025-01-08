# To-Do List GUI Application

A simple yet powerful **To-Do List Application** built using **JavaFX**. This project allows users to manage tasks in an intuitive graphical user interface (GUI), providing features like adding, removing, reordering tasks, and persistent saving.

## **Overview**
This application enables users to:
- Add new tasks to the list.
- Remove existing tasks.
- Reorder tasks (move to top, bottom, raise, or lower position).
- Save the list for future sessions (persistence via serialization).
- Start with a previously saved list or a fresh, empty list.

The project demonstrates object-oriented programming principles and GUI development using JavaFX.

---

## **Features**
1. **Task Management:**
   - Add tasks using a text field or by pressing Enter.
   - Remove selected tasks from the list.
   - Reorder tasks within the list:
     - Move a task to the top or bottom.
     - Raise or lower a task by one position.

2. **Persistent Storage:**
   - Save the current list to a file (`objects.ser`).
   - Automatically load a previously saved list when the application starts (optional).

3. **Interactive Alerts:**
   - Prompt users to start with an empty or saved list.
   - Confirm saving changes when exiting the application.

4. **User-Friendly GUI:**
   - Built with JavaFX, featuring a clean layout with intuitive controls.

---

## **Technologies Used**
- **Programming Language**: Java
- **GUI Framework**: JavaFX
- **Persistence**: Serialization

---

## **Getting Started**
### **Prerequisites**
- **Java Development Kit (JDK)** 11 or later.
- **JavaFX SDK** (not bundled with the JDK since JDK 11).

### **Running the Application**
1. Clone the repository or download the project files.
2. Set up JavaFX in your IDE (refer to [JavaFX Setup Guide](https://openjfx.io/openjfx-docs/)).
3. Run the `ToDoListGUI.java` file in your IDE.

### **VM Options for JavaFX**
Add the following VM options in your run configuration:
```plaintext
--module-path "<path-to-javafx-sdk>/lib" --add-modules javafx.controls,javafx.fxml
