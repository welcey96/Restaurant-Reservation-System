package nbcc.config;

import nbcc.interceptors.LoginInterceptor;
import nbcc.interceptors.UserActivityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginInterceptor loginInterceptor;
    private final UserActivityInterceptor userActivityInterceptor;

    public WebConfig(LoginInterceptor loginInterceptor, UserActivityInterceptor userActivityInterceptor) {
        this.loginInterceptor = loginInterceptor;
        this.userActivityInterceptor = userActivityInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor).addPathPatterns(
                "/event/create/**",
                "/event/edit/**",
                "/event/delete/**"
        );

        registry.addInterceptor(loginInterceptor).addPathPatterns(
                "/seatingtime/event/**"
        );

        registry.addInterceptor(loginInterceptor).addPathPatterns(
                "/layout",
                "/layout/create/**",
                "/layout/edit/**",
                "/layout/detail/**",
                "/layout/delete/**",
                "/layout/{layoutId}/diningtable/**"
        );

        registry.addInterceptor(loginInterceptor).addPathPatterns(
                "/menu/create/**",
                "/menu/edit/**",
                "/menu/delete/**",
                "/menu/{menuId}/item/**"
        );

        registry.addInterceptor(loginInterceptor).addPathPatterns(
                "/reservation/event/**"
        );

        registry.addInterceptor(userActivityInterceptor);
    }
}
