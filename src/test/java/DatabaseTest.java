import edu.virginia.sde.model.User;
import edu.virginia.sde.service.UserService;
import edu.virginia.sde.service.UserServiceImpl;

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
}
