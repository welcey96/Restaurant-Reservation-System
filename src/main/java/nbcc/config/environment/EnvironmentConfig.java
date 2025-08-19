package nbcc.config.environment;

import nbcc.config.EmailSenderConfig;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentConfig implements DefaultAdminConfig, EmailSenderConfig {


    private final Environment environment;

    public EnvironmentConfig(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getDefaultAdminUsername() {
        return environment.getProperty("Default.Admin.Username");
    }

    @Override
    public String getDefaultAdminPassword() {
        return environment.getProperty("Default.Admin.Password");
    }

    @Override
    public String getDefaultAdminEmail() {
        return environment.getProperty("Default.Admin.Email");
    }

    @Override
    public String getDefaultAdminFirstName() {
        return environment.getProperty("Default.Admin.FirstName");
    }

    @Override
    public String getDefaultAdminLastName() {
        return environment.getProperty("Default.Admin.LastName");
    }

    @Override
    public String getDefaultFrom() {
        return environment.getProperty("Default.From.Email");
    }
}
