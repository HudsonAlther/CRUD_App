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
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:course_reviews.db")) {
            Statement stmt = connection.createStatement();
            stmt.execute("DROP TABLE IF EXISTS users");
            stmt.execute("CREATE TABLE users (username TEXT PRIMARY KEY, password TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to set up database");
        }
    }

    @Test
    public void testCreateUser() {
        User user = new User("testUser", "password123");
        boolean isCreated = userService.createUser(user);
        assertTrue(isCreated, "User should be created successfully");
    }

    @Test
    public void testGetUser() {
        User user = new User("testUser", "password123");
        userService.createUser(user);

        User fetchedUser = userService.getUser("testUser");
        assertNotNull(fetchedUser, "Fetched user should not be null");
        assertEquals("testUser", fetchedUser.getUsername(), "Username should match");
    }

    @Test
    public void testValidateUser() {
        User user = new User("testUser", "password123");
        userService.createUser(user);

        assertTrue(userService.validateUser("testUser", "password123"), "Validation should succeed for correct credentials");
        assertFalse(userService.validateUser("testUser", "wrongPassword"), "Validation should fail for incorrect password");
    }
}
