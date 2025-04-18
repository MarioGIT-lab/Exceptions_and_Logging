package Rocxoiu_Mario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;


public class StudentRepositoryUnitTest {

    private StudentRepository repository;

    @BeforeEach
    public void setup() {
        repository = new StudentRepository();
    }

    @Test
    public void testAddValidStudent() {
        repository.addStudent("Vlad", "Andrei", LocalDate.of(2000, 1, 1), "Male", "123456");
        assertEquals(1, repository.listStudentsOrderedBy("lastname").size());
    }

    @Test
    public void testAddStudentWithEmptyFirstName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                repository.addStudent("", "Dumitru", LocalDate.of(2000, 1, 1), "Male", "123456")
        );
        assertTrue(exception.getMessage().contains("First name must not be empty"));
    }

    @Test
    public void testAddStudentWithInvalidDOB() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                repository.addStudent("Popescu", "Catalin", LocalDate.of(2010, 1, 1), "Female", "654321")
        );
        assertTrue(exception.getMessage().contains("Date of birth must be between"));
    }

    @Test
    public void testDeleteExistingStudent() {
        repository.addStudent("Pintiliu", "Marius", LocalDate.of(2000, 1, 1), "M", "789012");
        repository.deleteStudent("789012");
        assertTrue(repository.listStudentsOrderedBy("lastname").isEmpty());
    }

    @Test
    public void testDeleteNonexistentStudent() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                repository.deleteStudent("000000")
        );
        assertEquals("Student with ID 000000 does not exist", exception.getMessage());
    }

    @Test
    public void testGetStudentsByAge() {
        repository.addStudent("Andrei", "Florin", LocalDate.now().minusYears(22), "F", "111111");
        List<Student> result = repository.getStudentsByAge("22");
        assertEquals(1, result.size());
        assertEquals("Andrei", result.get(0).getFirstName());
    }

    @Test
    public void testGetStudentsByAgeInvalid() {
        assertThrows(IllegalArgumentException.class, () -> repository.getStudentsByAge("abc"));
        assertThrows(IllegalArgumentException.class, () -> repository.getStudentsByAge("-5"));
    }

    @Test
    public void testListStudentsOrderByLastName() {
        repository.addStudent("Zafiu", "Matei", LocalDate.of(1999, 5, 20), "F", "001");
        repository.addStudent("Blagoi", "Andrei", LocalDate.of(1998, 3, 10), "M", "002");

        List<Student> result = repository.listStudentsOrderedBy("lastname");
        assertEquals("Andrei", result.get(0).getLastName());
        assertEquals("Matei", result.get(1).getLastName());
    }

    @Test
    public void testListStudentsOrderByBirthDate() {
        repository.addStudent("Mogos", "Elena", LocalDate.of(1995, 2, 15), "M", "003");
        repository.addStudent("Postolache", "Andreea", LocalDate.of(1993, 7, 10), "F", "004");

        List<Student> result = repository.listStudentsOrderedBy("birthdate");
        assertEquals("Postolache", result.get(0).getFirstName()); // Older comes first
    }

    @Test
    public void testListStudentsWithEmptyCriteria() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                repository.listStudentsOrderedBy("")
        );
        assertEquals("Ordering criteria must not be empty", exception.getMessage());
    }

    @Test
    public void testListStudentsWithInvalidCriteria() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                repository.listStudentsOrderedBy("unknown")
        );
        assertEquals("Invalid ordering criteria: unknown", exception.getMessage());
    }
}
