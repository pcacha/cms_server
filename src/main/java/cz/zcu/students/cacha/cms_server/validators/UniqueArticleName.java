package cz.zcu.students.cacha.cms_server.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueArticleNameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueArticleName {
    String message() default "Název je již obsazený";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
