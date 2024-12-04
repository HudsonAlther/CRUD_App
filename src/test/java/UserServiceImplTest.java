import edu.virginia.sde.model.User;
import edu.virginia.sde.service.UserServiceImpl;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTest {

    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl();
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:test_course_reviews.db")) {
            Statement stmt = connection.createStatement();
            stmt.execute("DROP TABLE IF EXISTS Users");
            stmt.execute("CREATE TABLE Users (" +
                    "id INTEGER PRIMARY KEY," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL" +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to set up database: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:test_course_reviews.db")) {
            Statement stmt = connection.createStatement();
            stmt.execute("DROP TABLE IF EXISTS Users");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to clean up database: " + e.getMessage());
        }
    }



    @Test
    public void testGetUser() {
        userService.createUser("testUser", "password123");
        User fetchedUser = userService.getUser("testUser");

        assertNotNull(fetchedUser, "Fetched user should not be null");
        assertEquals("testUser", fetchedUser.getUsername(), "Username should match");
        assertEquals("password123", fetchedUser.getPassword(), "Password should match");
    }

    @Test
    public void testValidateUser() {
        userService.createUser("testUser", "password123");

        assertTrue(userService.validateUser("testUser", "password123"), "Validation should succeed for correct credentials");
        assertFalse(userService.validateUser("testUser", "wrongPassword"), "Validation should fail for incorrect password");
        assertFalse(userService.validateUser("nonExistentUser", "password123"), "Validation should fail for non-existent user");
    }
}
