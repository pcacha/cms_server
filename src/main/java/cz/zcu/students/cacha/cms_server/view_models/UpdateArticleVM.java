package cz.zcu.students.cacha.cms_server.view_models;

import cz.zcu.students.cacha.cms_server.validators.PdfFile;
import cz.zcu.students.cacha.cms_server.validators.UniqueArticleName;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UpdateArticleVM {

    @NotNull(message = "Id nesmí být prázdné")
    private Long id;

    @NotNull(message = "Název nesmí být prázdný")
    @Size(min = 3, max = 255, message = "Název musí mít minimálně 3 znaky a maximálně 255 znaků")
    private String name;

    @NotNull(message = "Text nesmí být prázdný")
    @Size(min = 10, max = 10000, message = "Abstrakt článku nemá správnou délku")
    private String text;

    @NotNull(message = "Název přílohy nesmí být prázdný")
    private String documentName;

    @PdfFile
    private String encodedDocument;
}
