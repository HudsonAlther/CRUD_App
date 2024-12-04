import edu.virginia.sde.model.User;
import edu.virginia.sde.service.UserService;
import edu.virginia.sde.service.UserServiceImpl;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class DatabaseTest {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        User user = userService.getUser("testuser");
        if (user != null) {
            System.out.println("User found: " + user.getUsername());
        } else {
            System.out.println("User not found.");
        }
        boolean isValidUser = userService.validateUser("testuser", "password123");
        System.out.println("Login valid: " + isValidUser);
    }

    @Test
    public void testPersistence() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:course_reviews.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Courses")) {

            assertTrue(rs.next(), "Courses should persist between application runs");
        } catch (SQLException e) {
            fail("Database persistence test failed: " + e.getMessage());
        }
    }

}
