import edu.virginia.sde.model.Course;
import edu.virginia.sde.service.CourseService;
import edu.virginia.sde.service.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseServiceTest {

    private CourseService courseService;

    @BeforeEach
    public void setUp() {
        // Initialize the CourseService instance before each test
        courseService = new CourseServiceImpl();
    }

    @Test
    public void testAddCourse() {
        Course course = new Course("CS", 3140, "Software Development Essentials", 0.0);
        assertTrue(courseService.addCourse(course));
        List<Course> courses = courseService.getAllCourses();
        assertEquals(1, courses.size());
        assertEquals("CS", courses.get(0).subjectProperty().get());
    }

    @Test
    public void testDuplicateCourseAddition() {
        Course course = new Course("CS", 3140, "Software Development Essentials", 0.0);
        assertTrue(courseService.addCourse(course));
        assertFalse(courseService.addCourse(course));
        List<Course> courses = courseService.getAllCourses();
        assertEquals(1, courses.size());
    }

    @Test
    public void testSearchCoursesBySubject() {
        Course course1 = new Course("CS", 3140, "Software Development Essentials", 0.0);
        Course course2 = new Course("MATH", 1010, "Calculus I", 0.0);
        courseService.addCourse(course1);
        courseService.addCourse(course2);
        List<Course> csCourses = courseService.searchCourses("CS", "", "");
        assertEquals(1, csCourses.size());
        assertEquals("CS", csCourses.get(0).subjectProperty().get());
    }

    @Test
    public void testSearchCoursesByNumber() {
        Course course1 = new Course("CS", 3140, "Software Development Essentials", 0.0);
        Course course2 = new Course("CS", 2110, "Data Structures and Algorithms", 0.0);
        courseService.addCourse(course1);
        courseService.addCourse(course2);
        List<Course> result = courseService.searchCourses("", "3140", "");
        assertEquals(1, result.size());
        assertEquals(3140, result.get(0).numberProperty().get());
    }

    @Test
    public void testSearchCoursesByTitle() {
        Course course1 = new Course("CS", 3140, "Software Development Essentials", 0.0);
        Course course2 = new Course("CS", 2110, "Data Structures and Algorithms", 0.0);
        courseService.addCourse(course1);
        courseService.addCourse(course2);
        List<Course> result = courseService.searchCourses("", "", "Software");
        assertEquals(1, result.size());
        assertTrue(result.get(0).titleProperty().get().contains("Software"));
    }

    @Test
    public void testSearchCoursesCombinedCriteria() {
        Course course1 = new Course("CS", 3140, "Software Development Essentials", 0.0);
        Course course2 = new Course("CS", 2110, "Data Structures and Algorithms", 0.0);
        Course course3 = new Course("MATH", 1010, "Calculus I", 0.0);
        courseService.addCourse(course1);
        courseService.addCourse(course2);
        courseService.addCourse(course3);
        List<Course> result = courseService.searchCourses("CS", "", "Development");
        assertEquals(1, result.size());
        assertEquals("Software Development Essentials", result.get(0).titleProperty().get());
    }

    @Test
    public void testSearchCoursesCaseInsensitive() {
        Course course = new Course("CS", 3140, "Software Development Essentials", 0.0);
        courseService.addCourse(course);
        List<Course> result = courseService.searchCourses("cs", "", "");
        assertEquals(1, result.size());
        assertEquals("CS", result.get(0).subjectProperty().get());
    }

    @Test
    public void testSearchWithEmptyParametersReturnsAllCourses() {
        Course course1 = new Course("CS", 3140, "Software Development Essentials", 0.0);
        Course course2 = new Course("MATH", 1010, "Calculus I", 0.0);
        courseService.addCourse(course1);
        courseService.addCourse(course2);
        List<Course> allCourses = courseService.searchCourses("", "", "");
        assertEquals(2, allCourses.size());
    }

    @Test
    public void testWhitespaceHandlingInCourseAddition() {
        Course course = new Course("  CS  ", 3140, "  Software Development Essentials  ", 0.0);
        assertTrue(courseService.addCourse(course));
        List<Course> courses = courseService.getAllCourses();
        assertEquals(1, courses.size());
        assertEquals("CS", courses.get(0).subjectProperty().get().trim());
        assertEquals("Software Development Essentials", courses.get(0).titleProperty().get().trim());
    }
}
