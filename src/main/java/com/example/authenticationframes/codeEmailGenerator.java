package com.example.authenticationframes;

import java.util.Random;

public class codeEmailGenerator {

    public static String generateRandomPassword(){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    public static int generateCode() {
        int code = 0;
        for (int i = 0; i < 3; i++) {
            int randomNumber = (int) (Math.random() * 10);
            code = code * 10 + randomNumber;
        }
        return code;
    }
}