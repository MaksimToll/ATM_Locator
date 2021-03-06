package com.ss.atmlocator.service;

import com.ss.atmlocator.utils.Constants;
import com.ss.atmlocator.utils.UserCredMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Locale;


/**
 *
 */

public class ValidateUserPasswordService implements Validator {

    @Autowired
    @Qualifier("usercredmatcher")
    private UserCredMatcher userCredMatcher;

    @Autowired
    private MessageSource messages;


    @Override
    public boolean supports(Class<?> Clazz) {
        return String.class.isAssignableFrom(Clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {
        final String password = (String) object;
        if (!validatePassword(password)) {
            errors.rejectValue(Constants.USER_PASSWORD,
                    messages.getMessage("invalid.password", null, Locale.ENGLISH));
        }
    }

    private boolean validatePassword(String password) {
        return userCredMatcher.validatePassword(password);
    }


}
