package Rocxoiu_Mario;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        StudentRepository repo = new StudentRepository();

        try {
            repo.addStudent("Test", "Student", LocalDate.of(2001, 1, 1), "M", "001");
            System.out.println("Student added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding student: " + e.getMessage());
        }

        try {
            repo.deleteStudent("001");
            System.out.println("Student deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }
}
