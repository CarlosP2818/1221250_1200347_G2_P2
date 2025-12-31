package pt.psoft.g1.psoftg1.shared.services.IdGenerators;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@Profile("base65")
public class Base65IdGenerator implements IdGenerator {
    private static final String BASE65_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_+";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int DEFAULT_LENGTH = 5;

    public String generateId() {
        StringBuilder sb = new StringBuilder(DEFAULT_LENGTH);
        for (int i = 0; i < DEFAULT_LENGTH; i++) {
            int index = RANDOM.nextInt(BASE65_CHARS.length());
            sb.append(BASE65_CHARS.charAt(index));
        }
        return sb.toString();
    }
}
