package nbcc.initialize;

import nbcc.config.environment.DefaultAdminConfig;
import nbcc.entities.UserDetail;
import nbcc.services.UserService;
import org.springframework.stereotype.Component;

@Component
public class AuthInitializer {

    private final UserService userService;
    private final DefaultAdminConfig defaultAdminConfig;

    public AuthInitializer(UserService userService, DefaultAdminConfig defaultAdminConfig) {
        this.userService = userService;
        this.defaultAdminConfig = defaultAdminConfig;
    }

    public void initializeAdminUser() {
        var uName = defaultAdminConfig.getDefaultAdminUsername();
        if (uName == null || uName.isBlank())
            return;

        if (userService.usernameExists(uName))
            return;

        var pass = defaultAdminConfig.getDefaultAdminPassword();
        var email = defaultAdminConfig.getDefaultAdminEmail();
        var fName = defaultAdminConfig.getDefaultAdminFirstName();
        var lName = defaultAdminConfig.getDefaultAdminLastName();

        if (pass == null || pass.isBlank() || email == null || email.isBlank() ||
                fName == null || fName.isBlank() || lName == null || lName.isBlank()
        ) {
            return;
        }

        var userDetail = new UserDetail(uName, pass, fName, lName, email);

        if (userService.register(userDetail) != null)
            System.out.println("Admin user registered: " + uName);
        else
            System.out.println("Admin user not registered: " + uName);
    }
}
