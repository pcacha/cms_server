package cz.zcu.students.cacha.cms_server.validators;

import cz.zcu.students.cacha.cms_server.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Base64;

public class PdfFileValidator implements ConstraintValidator<PdfFile, String> {

    @Autowired
    private FileService fileService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }

        byte[] decodeBytes = Base64.getMimeDecoder().decode(value.replace("\n", "").trim());
        String fileType = fileService.detectType(decodeBytes);
        if(fileType.equalsIgnoreCase("application/pdf")) {
            return true;
        }

        return false;
    }
}
