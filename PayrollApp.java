// This code implements a simple in-memory payroll system with basic CRUD operations for different types of employees.
// It allows adding, removing, updating, searching, and generating payslips for employees.
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

// ---------- Model classes ----------
abstract class Employee {
    private static int counter = 0;
    private final int id;
    private String name;
    private String email;

    public Employee(String name, String email) {
        this.id = ++counter;
        this.name = name;
        this.email = email;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public abstract double calculateSalary();

    // Used in payslip: can be overridden to show breakdown
    public String salaryBreakdown() {
        return String.format("Base salary: %.2f", calculateSalary());
    }

    @Override
    public String toString() {
        return String.format("ID:%d | Name:%s | Type:%s | Salary:%.2f", id, name, getClass().getSimpleName(), calculateSalary());
    }
}

class FullTimeEmployee extends Employee {
    private double monthlySalary;
    private double bonus;      // optional
    private double deduction;  // optional

    public FullTimeEmployee(String name, String email, double monthlySalary) {
        super(name, email);
        this.monthlySalary = monthlySalary;
    }

    public double getMonthlySalary() { return monthlySalary; }
    public void setMonthlySalary(double monthlySalary) { this.monthlySalary = monthlySalary; }
    public double getBonus() { return bonus; }
    public void setBonus(double bonus) { this.bonus = bonus; }
    public double getDeduction() { return deduction; }
    public void setDeduction(double deduction) { this.deduction = deduction; }

    @Override
    public double calculateSalary() {
        return monthlySalary + bonus - deduction;
    }

    @Override
    public String salaryBreakdown() {
        return String.format("Monthly: %.2f | Bonus: %.2f | Deduction: %.2f | Net: %.2f",
                monthlySalary, bonus, deduction, calculateSalary());
    }
}

class PartTimeEmployee extends Employee {
    private int hoursWorked;
    private double hourlyRate;

    public PartTimeEmployee(String name, String email, int hoursWorked, double hourlyRate) {
        super(name, email);
        this.hoursWorked = hoursWorked;
        this.hourlyRate = hourlyRate;
    }

    public int getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(int hoursWorked) { this.hoursWorked = hoursWorked; }
    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    @Override
    public double calculateSalary() {
        return hoursWorked * hourlyRate;
    }

    @Override
    public String salaryBreakdown() {
        return String.format("Hours: %d | Rate: %.2f | Net: %.2f", hoursWorked, hourlyRate, calculateSalary());
    }
}

class ContractEmployee extends Employee {
    private double contractAmount;

    public ContractEmployee(String name, String email, double contractAmount) {
        super(name, email);
        this.contractAmount = contractAmount;
    }

    public double getContractAmount() { return contractAmount; }
    public void setContractAmount(double contractAmount) { this.contractAmount = contractAmount; }

    @Override
    public double calculateSalary() {
        return contractAmount;
    }
}

class Intern extends Employee {
    private double stipend;

    public Intern(String name, String email, double stipend) {
        super(name, email);
        this.stipend = stipend;
    }

    public double getStipend() { return stipend; }
    public void setStipend(double stipend) { this.stipend = stipend; }

    @Override
    public double calculateSalary() {
        return stipend;
    }
}

// ---------- Payroll system ----------
class PayrollSystem {
    private final List<Employee> employees = new ArrayList<>();

    // Add
    public void addEmployee(Employee e) {
        employees.add(e);
        System.out.println("Added: " + e);
    }

    // Remove by id
    public boolean removeEmployee(int id) {
        Optional<Employee> opt = employees.stream().filter(x -> x.getId() == id).findFirst();
        if (opt.isPresent()) {
            employees.remove(opt.get());
            System.out.println("Removed employee with ID " + id);
            return true;
        }
        System.out.println("No employee found with ID " + id);
        return false;
    }

    // Update fields depends on type; simple approach: replace or mutate
    public Optional<Employee> findById(int id) {
        return employees.stream().filter(x -> x.getId() == id).findFirst();
    }

    public List<Employee> searchByName(String namePart) {
        String lowered = namePart.toLowerCase();
        List<Employee> result = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getName().toLowerCase().contains(lowered)) result.add(e);
        }
        return result;
    }

    public void listAll() {
        if (employees.isEmpty()) {
            System.out.println("No employees registered.");
            return;
        }
        System.out.println("---- Employee List ----");
        employees.forEach(System.out::println);
    }

    public void generatePayslip(int id) {
        Optional<Employee> opt = findById(id);
        if (opt.isEmpty()) {
            System.out.println("Employee not found.");
            return;
        }
        Employee e = opt.get();
        System.out.println("----- PAYSLIP -----");
        System.out.println("ID: " + e.getId());
        System.out.println("Name: " + e.getName());
        System.out.println("Type: " + e.getClass().getSimpleName());
        System.out.println(e.salaryBreakdown());
        System.out.println("-------------------");
    }
}

// ---------- Console UI ----------
public class PayrollApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final PayrollSystem payroll = new PayrollSystem();

    public static void main(String[] args) {
        seedDemoData(); // remove or keep for demo
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1 -> addEmployeeMenu();
                case 2 -> removeEmployeeMenu();
                case 3 -> updateEmployeeMenu();
                case 4 -> searchMenu();
                case 5 -> payroll.listAll();
                case 6 -> payslipMenu();
                case 0 -> {
                    System.out.println("Exiting... Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n=== Employee Payroll System (In-Memory) ===");
        System.out.println("1. Add Employee");
        System.out.println("2. Remove Employee");
        System.out.println("3. Update Employee");
        System.out.println("4. Search Employee");
        System.out.println("5. List All Employees");
        System.out.println("6. Generate Payslip");
        System.out.println("0. Exit");
    }

    // ---------- Menus ----------
    private static void addEmployeeMenu() {
        System.out.println("Select type: 1.Full-time 2.Part-time 3.Contract 4.Intern");
        int t = readInt("Type: ");
        String name = readString("Name: ");
        String email = readString("Email: ");

        switch (t) {
            case 1 -> {
                double monthly = readDouble("Monthly salary: ");
                FullTimeEmployee f = new FullTimeEmployee(name, email, monthly);
                if (yesNo("Add bonus? (y/n)")) f.setBonus(readDouble("Bonus: "));
                if (yesNo("Add deduction? (y/n)")) f.setDeduction(readDouble("Deduction: "));
                payroll.addEmployee(f);
            }
            case 2 -> {
                int hours = readInt("Hours worked: ");
                double rate = readDouble("Hourly rate: ");
                payroll.addEmployee(new PartTimeEmployee(name, email, hours, rate));
            }
            case 3 -> {
                double amount = readDouble("Contract amount: ");
                payroll.addEmployee(new ContractEmployee(name, email, amount));
            }
            case 4 -> {
                double stipend = readDouble("Stipend: ");
                payroll.addEmployee(new Intern(name, email, stipend));
            }
            default -> System.out.println("Invalid type.");
        }
    }

    private static void removeEmployeeMenu() {
        int id = readInt("Enter ID to remove: ");
        payroll.removeEmployee(id);
    }

    private static void updateEmployeeMenu() {
        int id = readInt("Enter employee ID to update: ");
        Optional<Employee> opt = payroll.findById(id);
        if (opt.isEmpty()) {
            System.out.println("No employee with ID " + id);
            return;
        }
        Employee e = opt.get();
        System.out.println("Found: " + e);
        if (yesNo("Change name? (y/n)")) e.setName(readString("New name: "));
        if (yesNo("Change email? (y/n)")) e.setEmail(readString("New email: "));

        if (e instanceof FullTimeEmployee fte) {
            if (yesNo("Change monthly salary? (y/n)")) fte.setMonthlySalary(readDouble("Monthly salary: "));
            if (yesNo("Change bonus? (y/n)")) fte.setBonus(readDouble("Bonus: "));
            if (yesNo("Change deduction? (y/n)")) fte.setDeduction(readDouble("Deduction: "));
        } else if (e instanceof PartTimeEmployee pte) {
            if (yesNo("Change hours worked? (y/n)")) pte.setHoursWorked(readInt("Hours worked: "));
            if (yesNo("Change hourly rate? (y/n)")) pte.setHourlyRate(readDouble("Hourly rate: "));
        } else if (e instanceof ContractEmployee ce) {
            if (yesNo("Change contract amount? (y/n)")) ce.setContractAmount(readDouble("Contract amount: "));
        } else if (e instanceof Intern in) {
            if (yesNo("Change stipend? (y/n)")) in.setStipend(readDouble("Stipend: "));
        }

        System.out.println("Updated: " + e);
    }

    private static void searchMenu() {
        System.out.println("Search by: 1.ID  2.Name");
        int s = readInt("Choice: ");
        if (s == 1) {
            int id = readInt("ID: ");
            Optional<Employee> e = payroll.findById(id);
            e.ifPresentOrElse(emp -> System.out.println(emp), () -> System.out.println("Not found"));
        } else if (s == 2) {
            String name = readString("Name part: ");
            List<Employee> results = payroll.searchByName(name);
            if (results.isEmpty()) System.out.println("No matches.");
            else results.forEach(System.out::println);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void payslipMenu() {
        int id = readInt("Enter employee ID for payslip: ");
        payroll.generatePayslip(id);
    }

    // ---------- Utilities ----------
    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (Exception ex) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = scanner.nextLine().trim();
                return Double.parseDouble(line);
            } catch (Exception ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static boolean yesNo(String prompt) {
        System.out.print(prompt + " ");
        String r = scanner.nextLine().trim().toLowerCase();
        return r.startsWith("y");
    }

    // Seed some demo data (optional)
    private static void seedDemoData() {
        FullTimeEmployee f1 = new FullTimeEmployee("Vikas", "vikas@example.com", 70000);
        f1.setBonus(2000);
        PartTimeEmployee p1 = new PartTimeEmployee("Manish", "manish@example.com", 40, 100);
        ContractEmployee c1 = new ContractEmployee("Raj", "raj@example.com", 50000);
        Intern i1 = new Intern("Ria", "ria@example.com", 5000);
        payroll.addEmployee(f1);
        payroll.addEmployee(p1);
        payroll.addEmployee(c1);
        payroll.addEmployee(i1);
    }
}
