package nbcc.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HashingService {
    private final PasswordEncoder encoder;

    public HashingService(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public String hash(String data) {
        return encoder.encode(data);
    }

    public boolean valid(String plainText, String hashedValue) {
        return encoder.matches(plainText, hashedValue);
    }
}
