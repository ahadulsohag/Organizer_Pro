# Organizer_Pro
 the ultimate solution for everyday task management
## 📋 Table of Contents
- [Features](#features)
- [Screenshots](#screenshots)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Database Setup](#database-setup)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

### User Authentication
- Secure user registration and login system
- Password visibility toggle
- Session management
- User-specific data isolation

### Task Management
- **Create Tasks**: Add tasks with title, description, priority, category, and due dates
- **Edit Tasks**: Modify existing tasks with real-time updates
- **Delete Tasks**: Remove tasks with confirmation dialogs
- **Duplicate Tasks**: Quick task duplication for recurring activities
- **Task Status**: Mark tasks as complete or pending

### Organization & Filtering
- **Filter by Status**: All Tasks, Pending, Completed, Overdue
- **Filter by Priority**: High, Medium, Low priority tasks
- **Search Functionality**: Search tasks by title or description
- **Sorting Options**: Sort by title, priority, due date, or creation date

### User Interface
- **Modern Design**: Clean and intuitive user interface
- **Responsive Layout**: Optimized for different screen sizes
- **Visual Indicators**: Color-coded priority badges and status indicators
- **Interactive Elements**: Checkboxes for quick status updates

### Data Persistence
- **MySQL Integration**: All data stored in MySQL database
- **Real-time Updates**: Changes reflected immediately across the application
- **Data Integrity**: Foreign key relationships and data validation

## 📸 Screenshots

### Login Page

<img width="487" height="799" alt="Screenshot (36)" src="https://github.com/user-attachments/assets/1f3d7922-d216-4577-98ec-389c597c08ce" />

<img width="488" height="789" alt="Screenshot (37)" src="https://github.com/user-attachments/assets/06791a71-e7ec-42fb-8a3f-c7b7931d3923" />

<img width="483" height="787" alt="Screenshot (38)" src="https://github.com/user-attachments/assets/b52e214c-d32c-4e1e-9ce3-9216992dd921" />

<img width="642" height="789" alt="Screenshot (39)" src="https://github.com/user-attachments/assets/14e519f7-9f0a-4e54-9fc8-67e2f257fc25" />


### Registration Page

<img width="492" height="790" alt="Screenshot (48)" src="https://github.com/user-attachments/assets/aa5b7a00-0041-4ab1-ab12-513d0ece343d" />


### Main Dashboard

<img width="1356" height="1080" alt="Screenshot (40)" src="https://github.com/user-attachments/assets/bd55f6b4-1740-45cd-9fa4-1c1531c6c001" />

<img width="1357" height="1080" alt="Screenshot (42)" src="https://github.com/user-attachments/assets/8262ed4a-de8b-4fa4-8cc3-7f5bb8982860" />

<img width="1344" height="1080" alt="Screenshot (41)" src="https://github.com/user-attachments/assets/4c8938e6-0865-451d-9638-0d04a2d16e3a" />
<img width="1350" height="1080" alt="Screenshot (43)" src="https://github.com/user-attachments/assets/43a7d33b-1688-4ed3-b512-0c5969e9ab0c" />

<img width="1351" height="1080" alt="Screenshot (44)" src="https://github.com/user-attachments/assets/068be630-283d-4a0e-87f6-d8eff36961a3" />
<img width="1351" height="1080" alt="Screenshot (46)" src="https://github.com/user-attachments/assets/99fb18c1-d971-41b9-b636-4fbff45876fe" />

<img width="1357" height="1080" alt="Screenshot (45)" src="https://github.com/user-attachments/assets/738e904e-3b9d-4e25-90ba-fd468e62c0e3" />

<img width="1350" height="1080" alt="Screenshot (47)" src="https://github.com/user-attachments/assets/ecc63658-44d9-4420-9257-4aac2e5b8ba2" />

## 🔧 Prerequisites

Before running this application, make sure you have the following installed:

- **Java Development Kit (JDK) 8 or higher**
- **JavaFX SDK** (if not included with your JDK)
- **XAMPP** (for Apache and MySQL)
- **MySQL Connector/J** (JDBC driver)
- **IDE** (NetBeans, IntelliJ IDEA, or Eclipse)

## 🚀 Installation

### 1. Clone the Repository
```bash
git clone https://github.com/ahadulsohag/organizer-pro.git
cd organizer-pro
```

### 2. Set up XAMPP
1. Download and install [XAMPP](https://www.apachefriends.org/)
2. Start Apache and MySQL services from XAMPP Control Panel

### 3. Database Configuration
1. Open phpMyAdmin (http://localhost/phpmyadmin)
2. Create a new database named `organizerpro`
3. Import the database schema (see Database Setup section)

### 4. Add MySQL Connector
1. Download [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
2. Add the JAR file to your project's classpath
3. In NetBeans: Right-click project → Properties → Libraries → Add JAR/Folder

### 5. Configure IDE
1. Open the project in your preferred IDE
2. Set the main class to `organizer.pro.OrganizerPro`
3. Ensure JavaFX is properly configured

## 🗄️ Database Setup

### Automatic Setup
Run the provided SQL script to create the database structure:

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS organizerpro;
USE organizerpro;

-- Create admin table
CREATE TABLE IF NOT EXISTS admin (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    date DATE NOT NULL
);

-- Create tasks table
CREATE TABLE IF NOT EXISTS tasks (
    task_id INT PRIMARY KEY AUTO_INCREMENT,
    admin_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    priority ENUM('High', 'Medium', 'Low') DEFAULT 'Medium',
    category VARCHAR(50) DEFAULT 'Personal',
    due_date DATE,
    completed BOOLEAN DEFAULT FALSE,
    created_date DATE NOT NULL,
    FOREIGN KEY (admin_id) REFERENCES admin(admin_id) ON DELETE CASCADE
);

-- Insert sample admin for testing
INSERT INTO admin (email, username, password, date) VALUES 
('admin@test.com', 'admin', 'admin123', CURDATE());
```

### Test Database Connection
Use the provided `DatabaseTest.java` utility to verify your database connection:

```bash
javac DatabaseTest.java
java DatabaseTest
```

## 💻 Usage

### Running the Application
1. Ensure XAMPP MySQL is running
2. Compile and run the main class: `organizer.pro.OrganizerPro`
3. The login window will appear

### Default Login Credentials
- **Username**: `admin`
- **Password**: `admin123`

### Creating Your Account
1. Click "click to register" on the login page
2. Fill in your email, username, and password (minimum 8 characters)
3. Click "Sign Up"
4. Return to login page and use your credentials

### Managing Tasks
1. **Add Task**: Fill in the task form and click "Add Task"
2. **Edit Task**: Select a task from the list and click "Edit Task"
3. **Complete Task**: Check the checkbox next to the task or use "Mark Complete"
4. **Delete Task**: Select a task and click "Delete Task"
5. **Filter Tasks**: Use the sidebar buttons or dropdown filters
6. **Search**: Type in the search box to find specific tasks

## 📁 Project Structure

```
organizer-pro/
├── src/
│   └── organizer/pro/
│       ├── AlertMessage.java          # Alert dialog utility
│       ├── Database.java              # Database connection and operations
│       ├── FXMLDocumentController.java # Login/Registration controller
│       ├── MainDashboardController.java # Main application controller
│       ├── OrganizerPro.java          # Main application class
│       ├── Task.java                  # Task model class
│       ├── FXMLDocument.fxml          # Login/Registration UI
│       └── MainPage.fxml              # Main dashboard UI
├── Design/
│   └── PageDesign.css                 # Application styles
├── screenshots/                       # Application screenshots
├── database/
│   └── organizerpro.sql              # Database schema
├── libs/
│   └── mysql-connector-j-x.x.x.jar  # MySQL JDBC driver
└── README.md                         # This file
```

## 🛠️ Technologies Used

- **Java** - Core programming language
- **JavaFX** - User interface framework
- **MySQL** - Database management system
- **JDBC** - Database connectivity
- **FXML** - UI markup language
- **CSS** - Styling and design
- **XAMPP** - Development server environment

## 📱 Key Components

### Models
- **Task.java** - Task entity with properties and business logic
- **Data.java** - Static data holder for user session

### Controllers
- **FXMLDocumentController.java** - Handles login/registration logic
- **MainDashboardController.java** - Manages main application functionality

### Utilities
- **Database.java** - Database operations and connection management
- **AlertMessage.java** - Standardized alert dialogs

### Views
- **FXMLDocument.fxml** - Login and registration interface
- **MainPage.fxml** - Main dashboard layout
- **PageDesign.css** - Application styling

## 🔧 Configuration

### Database Configuration
The application connects to MySQL using these default settings:
- **Host**: localhost
- **Port**: 3306
- **Database**: organizerpro
- **Username**: root
- **Password**: (empty)

To modify these settings, edit the `Database.java` file:

```java
Connection connect = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/organizerpro?useSSL=false&serverTimezone=UTC", 
    "your_username", 
    "your_password"
);
```

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Ensure XAMPP MySQL is running
   - Check if the database `organizerpro` exists
   - Verify MySQL is running on port 3306

2. **Login Not Working**
   - Run `DatabaseTest.java` to check database connectivity
   - Verify the admin table has user data
   - Check console output for error messages

3. **Tasks Not Displaying**
   - Confirm tasks table exists and has correct structure
   - Check foreign key relationships
   - Verify user session data

4. **JavaFX Runtime Issues**
   - Ensure JavaFX is properly configured in your IDE
   - Add JavaFX modules to VM options if needed

### Getting Help
If you encounter issues:
1. Check the console output for error messages
2. Run the database connection test
3. Verify all prerequisites are installed
4. Check the Issues section of this repository

## 🚀 Future Enhancements

- [ ] Task categories customization
- [ ] Task reminders and notifications
- [ ] Task sharing and collaboration
- [ ] Export tasks to various formats
- [ ] Dark theme support
- [ ] Mobile responsive design
- [ ] Task statistics and analytics
- [ ] File attachments to tasks
- [ ] Task templates
- [ ] Calendar integration

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines
- Follow Java naming conventions
- Add comments for complex logic
- Test database operations thoroughly
- Update documentation for new features

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

- GitHub: [@ahadulsohag](https://github.com/ahadulsohag)
- Email: ahadulsohag@gmail.com

## 🙏 Acknowledgments

- JavaFX community for excellent documentation
- MySQL team for robust database system
- Apache Friends for XAMPP development environment
- Contributors and testers who helped improve this application

---

**⭐ If you found this project helpful, please give it a star!**
