package nbcc.config;

import jakarta.servlet.ServletContext;
import nbcc.initialize.AuthInitializer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationInitialConfig implements ServletContextInitializer {
    private final AuthInitializer authInitializer;

    public ApplicationInitialConfig(AuthInitializer authInitializer) {
        this.authInitializer = authInitializer;
    }


    @Override
    public void onStartup(ServletContext servletContext) {
        try {
            authInitializer.initializeAdminUser();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
