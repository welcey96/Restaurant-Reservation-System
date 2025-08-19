package nbcc.services;

import nbcc.dtos.LoginDto;
import nbcc.entities.UserDetail;

public interface LoginService {
    boolean login(LoginDto loginDto);

    void logout();

    void updateLastUsedAt();

    boolean isCurrentLoginValid();

    boolean usernameExists(String username);

    boolean register(UserDetail user);

    void clearLoginSession();

}
