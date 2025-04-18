package Rocxoiu_Mario;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StudentRepository {
    private Map<String, Student> students = new HashMap<>();
    private static final Logger logger = Logger.getLogger(StudentRepository.class.getName());

    public void addStudent(String firstName, String lastName, LocalDate dob, String gender, String id) {
        validateName(firstName, "First name");
        validateName(lastName, "Last name");
        validateDateOfBirth(dob);
        validateGender(gender);
        validateId(id);

        if (students.containsKey(id)) {
            throw new IllegalArgumentException("Student with ID already exists");
        }

        Student student = new Student(firstName, lastName, dob, gender, id);
        students.put(id, student);
        logger.info("Student added: " + student);
    }

    public void deleteStudent(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID must not be empty");
        }
        if (!students.containsKey(id)) {
            throw new NoSuchElementException("Student with ID " + id + " does not exist");
        }
        students.remove(id);
        logger.info("Student with ID " + id + " deleted");
    }

    public List<Student> getStudentsByAge(String ageStr) {
        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Age must be a number");
        }
        if (age < 0) throw new IllegalArgumentException("Age must be non-negative");

        return students.values().stream()
                .filter(s -> s.getAge() == age)

                .collect(Collectors.toList());
    }

    public List<Student> listStudentsOrderedBy(String criteria) {
        if (criteria == null || criteria.trim().isEmpty()) {
            throw new IllegalArgumentException("Ordering criteria must not be empty");
        }

        return switch (criteria.toLowerCase()) {
            case "lastname" -> students.values().stream()
                    .sorted(Comparator.comparing(Student::getLastName))
                    .collect(Collectors.toList());
            case "birthdate" -> students.values().stream()
                    .sorted(Comparator.comparing(Student::getDateOfBirth))
                    .collect(Collectors.toList());
            default -> throw new IllegalArgumentException("Invalid ordering criteria: " + criteria);
        };
    }

    private void validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be empty");
        }
    }

    private void validateDateOfBirth(LocalDate dob) {
        int currentYear = LocalDate.now().getYear();
        if (dob == null || dob.getYear() < 1900 || dob.getYear() > currentYear - 18) {
            throw new IllegalArgumentException("Date of birth must be between 1900 and " + (currentYear - 18));
        }
    }

    private void validateGender(String gender) {
        if (gender == null || !(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female")
                || gender.equalsIgnoreCase("m") || gender.equalsIgnoreCase("f"))) {
            throw new IllegalArgumentException("Gender must be Male/Female (M/F)");
        }
    }

    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID must not be empty");
        }
    }
}
