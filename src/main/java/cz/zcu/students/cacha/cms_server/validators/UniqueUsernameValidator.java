package cz.zcu.students.cacha.cms_server.validators;

import cz.zcu.students.cacha.cms_server.domain.User;
import cz.zcu.students.cacha.cms_server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        User inDB = userRepository.findByUsername(value);
        if(inDB == null) {
            return true;
        }
        return false;
    }
}
