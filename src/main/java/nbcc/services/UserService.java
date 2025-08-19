package nbcc.services;

import nbcc.config.EmailSenderConfig;
import nbcc.dtos.LoginDto;
import nbcc.entities.UserDetail;
import nbcc.repositories.UserDetailRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserDetailRepository userDetailRepository;
    private final HashingService hashingService;

    public UserService(UserDetailRepository userDetailRepository, HashingService hashingService) {
        this.userDetailRepository = userDetailRepository;
        this.hashingService = hashingService;
    }


    public UserDetail register(UserDetail register) {
        try {
            var hashedPassword = hashingService.hash(register.getPassword());
            var userDetail = new UserDetail(
                    register.getUsername(),
                    hashedPassword,
                    register.getFirstName(),
                    register.getLastName(),
                    register.getEmail()
            );

            return userDetailRepository.save(userDetail);

        } catch (Exception e) {
            return null;
        }
    }

    public boolean valid(LoginDto loginDto) {
        var user = userDetailRepository.getUserDetailByUsername(loginDto.getUsername());

        if (user == null)
            return false;

        return hashingService.valid(loginDto.getPassword(), user.getPassword());
    }

    public UserDetail getUserByUsername(String username) {
        return userDetailRepository.getUserDetailByUsername(username);
    }

    public Optional<UserDetail> findUser(Long id) {
        if (id == null)
            return Optional.empty();

        return userDetailRepository.findById(id);
    }

    public boolean usernameExists(String username) {
        return userDetailRepository.existsByUsername(username);
    }

}
