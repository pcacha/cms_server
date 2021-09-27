package cz.zcu.students.cacha.cms_server.validators;

import cz.zcu.students.cacha.cms_server.domain.User;
import cz.zcu.students.cacha.cms_server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameExclPrincipalValidator implements ConstraintValidator<UniqueUsernameExclPrincipal, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueUsernameExclPrincipal constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        User inDB = userRepository.findByUsername(value);

        if(inDB == null) {
            return true;
        }

        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(inDB.getId().equals(auth.getId())) {
            return true;
        }
        return false;
    }
}
