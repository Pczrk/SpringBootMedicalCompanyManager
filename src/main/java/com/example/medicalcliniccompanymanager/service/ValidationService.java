package com.example.medicalcliniccompanymanager.service;

import com.example.medicalcliniccompanymanager.exception.appexceptions.NullArgumentException;
import com.example.medicalcliniccompanymanager.exception.appexceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ValidationService {
    private final String mailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01" +
            "-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[" +
            "a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][" +
            "0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1" +
            "f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private final String passwordRegex = ""; //TODO

    boolean validate(String regex, String textToValidate){
        return Pattern.compile(regex).matcher(textToValidate).matches();
    }

    protected void validateMail(String mail){
        if (mail == null)
            throw new NullArgumentException();

        if (!validate(mailRegex,mail))
            throw new ValidationException("Mail address is invalid");
    }

    protected void validatePassword(String password){ //TODO
//        if (password == null)
//            throw new NullArgumentException();
//
//        if(!validate(passwordRegex,password))
//            throw new ValidationException("Password is invalid");
    }

    protected void validatePESEL(String pesel){
        if (pesel == null)
            throw new NullArgumentException();

        if (pesel.length() != 11 || !pesel.matches("\\d{11}"))
            throw new ValidationException("Pesel is invalid");

        int sum = 0;
        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        for (int i = 0; i < 10; i++)
            sum += Character.getNumericValue(pesel.charAt(i)) * weights[i];
        int checksum = 10 - (sum % 10);

        if (checksum != Character.getNumericValue(pesel.charAt(10)))
            throw new ValidationException("Pesel is invalid");
    }
}
