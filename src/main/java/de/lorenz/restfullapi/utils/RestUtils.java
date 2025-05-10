package de.lorenz.restfullapi.utils;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RestUtils {


    public Long generateUserId() {
        String digits = "1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 9; i++) {
            int index = random.nextInt(digits.length());
            sb.append(digits.charAt(index));
        }
        return Long.parseLong(sb.toString());
    }
}
