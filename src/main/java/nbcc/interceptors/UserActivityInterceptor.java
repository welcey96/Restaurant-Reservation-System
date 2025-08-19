package nbcc.interceptors;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nbcc.services.LoginService;
import nbcc.services.RouteService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserActivityInterceptor implements HandlerInterceptor {
    private final LoginService loginService;
    private final RouteService routeService;

    public UserActivityInterceptor(LoginService loginService, RouteService routeService) {
        this.loginService = loginService;
        this.routeService = routeService;
    }

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request,
                             @Nonnull HttpServletResponse response,
                             @Nonnull Object handler) throws Exception {

        if (handler instanceof HandlerMethod handlerMethod) {
            if (handlerMethod.getMethod().isAnnotationPresent(GetMapping.class))
                routeService.updateCurrentRoute(request.getRequestURI());
        }

        if (loginService.isCurrentLoginValid())
            loginService.updateLastUsedAt();
        else
            loginService.clearLoginSession();

        return true;
    }
}
