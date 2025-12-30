package pt.psoft.g1.psoftg1.bookmanagement.services.IdGenerators;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@Profile("hex")
public class HexIdGenerator implements IdGenerator {

    private static final String HEX_CHARS = "0123456789abcdef";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int DEFAULT_LENGTH = 5;

    public String generateId() {
        StringBuilder sb = new StringBuilder(DEFAULT_LENGTH);
        for (int i = 0; i < DEFAULT_LENGTH; i++) {
            int index = RANDOM.nextInt(HEX_CHARS.length());
            sb.append(HEX_CHARS.charAt(index));
        }
        return sb.toString();
    }
}
