package nbcc.services;

import jakarta.servlet.http.HttpSession;
import nbcc.dtos.LoginDto;
import nbcc.entities.UserDetail;
import nbcc.entities.UserLogin;
import nbcc.repositories.UserLoginRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class LoginServiceImpl implements LoginService {

    private static final String LOGIN_USER_ID = "LoginUserId";
    private static final String IS_USER_LOGGED_IN = "loggedIn";
    private static final String LOGIN_ID = "loginId";

    private final UserLoginRepository userLoginRepository;
    private final UserService userService;
    private final HttpSession session;
    private final RouteService routeService;

    public LoginServiceImpl(UserLoginRepository userLoginRepository,
                            UserService userService, HttpSession session, RouteService routeService) {
        this.userService = userService;
        this.session = session;
        this.userLoginRepository = userLoginRepository;
        this.routeService = routeService;
    }

    @Override
    public boolean login(LoginDto loginDto) {
        if (userService.valid(loginDto)) {
            var user = userService.getUserByUsername(loginDto.getUsername());
            var userLogin = createNewUserLogin(user);

            session.setAttribute(LOGIN_USER_ID, user.getId());
            session.setAttribute(LOGIN_ID, userLogin.getId());
            session.setAttribute(IS_USER_LOGGED_IN, true);

            return true;
        }

        return false;
    }

    @Override
    public void logout() {
        var login = getCurrentLogin();
        if (login != null) {
            login.setLoggedOutAt(LocalDateTime.now());
            userLoginRepository.save(login);
        }

        clearLoginSession();
        routeService.clearRouteSession();
    }

    @Override
    public boolean isCurrentLoginValid() {
        var login = getCurrentLogin();
        return login != null &&
                Duration.between(
                        login.getLastUsedAt(), LocalDateTime.now()
                ).toMinutes() <= 30;
    }

    @Override
    public void updateLastUsedAt() {
        var login = getCurrentLogin();
        if (login != null) {
            login.setLastUsedAt(LocalDateTime.now());
            userLoginRepository.save(login);
        }
    }

    @Override
    public boolean register(UserDetail user) {
        return userService.register(user) != null;
    }

    @Override
    public boolean usernameExists(String username) {
        return userService.usernameExists(username);
    }

    @Override
    public void clearLoginSession() {
        session.removeAttribute(LOGIN_USER_ID);
        session.removeAttribute(LOGIN_ID);
        session.removeAttribute(IS_USER_LOGGED_IN);
    }

    private UserLogin createNewUserLogin(UserDetail userDetail) {
        var login = new UserLogin(userDetail);
        userLoginRepository.save(login);
        return login;
    }

    private UserLogin getCurrentLogin() {
        var loginId = session.getAttribute(LOGIN_ID);
        if (loginId == null)
            return null;

        return userLoginRepository.findById(loginId.toString())
                .orElse(null);
    }

    private UserDetail getLoggedInUser() {
        Long userId = getLoggedInUserId();
        if (userId == null)
            return null;

        return userService.findUser(userId).orElse(null);
    }

    private Long getLoggedInUserId() {
        var userIdAttribute = session.getAttribute(LOGIN_USER_ID);
        if (userIdAttribute == null)
            return null;

        var userIdString = userIdAttribute.toString();
        try {
            return Long.parseLong(userIdString);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
