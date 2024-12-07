
import edu.virginia.sde.service.UserService;
import edu.virginia.sde.service.UserServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    private final UserService userService = new UserServiceImpl();

    @Test
    void testValidPassword() {
        assertTrue(userService.isValidPassword("password123")); // Valid password
    }

    @Test
    void testShortPassword() {
        assertFalse(userService.isValidPassword("short")); // Too short
    }

    @Test
    void testNullPassword() {
        assertFalse(userService.isValidPassword(null)); // Null
    }

    @Test
    void testEmptyPassword() {
        assertFalse(userService.isValidPassword("")); // Empty
    }
}
