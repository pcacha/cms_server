package cz.zcu.students.cacha.cms_server.validators;

import cz.zcu.students.cacha.cms_server.domain.Article;
import cz.zcu.students.cacha.cms_server.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueArticleNameValidator implements ConstraintValidator<UniqueArticleName, String> {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public void initialize(UniqueArticleName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        Article inDB = articleRepository.findByName(value);
        if(inDB == null) {
            return true;
        }
        return false;
    }
}
