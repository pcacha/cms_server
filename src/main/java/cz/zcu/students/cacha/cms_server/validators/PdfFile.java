package cz.zcu.students.cacha.cms_server.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PdfFileValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PdfFile {
    String message() default "Soubor musí být typu PDF";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
