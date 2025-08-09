# 🧾 Employee Payroll System

The **Employee Payroll System** is a desktop-based application designed to simplify the process of managing employee records, calculating salaries, and generating payslips.  
This project was developed in **Java** using **Swing** for the GUI and **MySQL** as the database, following an **Object-Oriented Programming (OOP)** approach.

---

## 📌 Features

### 🔹 Employee Management
- Add new employees with details like name, position, department, and joining date.
- Update employee details in case of promotions, transfers, or corrections.
- Delete employees who have left the organization.

### 🔹 Payroll Calculation
- Automatically calculate salaries based on basic pay, allowances, deductions, and overtime.
- Supports **monthly and hourly wage** employees.
- Tax and deduction calculations integrated.

### 🔹 Payslip Generation
- Generate a payslip for any employee with full breakdown.
- Export payslip as **PDF** or print directly.

### 🔹 User Authentication
- Secure login for admin and HR staff.
- Different access levels (Admin, HR Staff).

---

## 🛠️ Tech Stack

| Component       | Technology Used        |
|-----------------|------------------------|
| **Programming Language** | Java (JDK 8+) |
| **GUI**         | Java Swing |
| **Database**    | MySQL |
| **Database Connector** | JDBC |
| **IDE**         | NetBeans / Eclipse |

---

## 🗂️ Database Structure

### Table: `employees`
| Column Name | Type        | Description |
|-------------|------------|-------------|
| emp_id      | INT (PK)   | Employee ID |
| name        | VARCHAR    | Employee Name |
| position    | VARCHAR    | Job Position |
| department  | VARCHAR    | Department Name |
| joining_date| DATE       | Date of Joining |

### Table: `payroll`
| Column Name | Type        | Description |
|-------------|------------|-------------|
| payroll_id  | INT (PK)   | Payroll Record ID |
| emp_id      | INT (FK)   | Employee ID |
| basic_pay   | DECIMAL    | Basic Salary |
| allowances  | DECIMAL    | Extra Allowances |
| deductions  | DECIMAL    | Tax, Provident Fund, etc. |
| net_salary  | DECIMAL    | Final Salary |

---

## ⚙️ Installation (Windows)

### 1️⃣ Install Required Software
- **Java JDK 8 or higher** → [Download JDK](https://www.oracle.com/java/technologies/javase-downloads.html)
- **MySQL Server** → [Download MySQL](https://dev.mysql.com/downloads/mysql/)
- **NetBeans IDE** or **Eclipse** (Recommended for Java GUI)
- **MySQL Connector/J** → [Download Connector](https://dev.mysql.com/downloads/connector/j/)

### 2️⃣ Database Setup
1. Open **MySQL Command Line** or **phpMyAdmin**.
2. Create a new database:
   ```sql
   CREATE DATABASE payroll_system;
