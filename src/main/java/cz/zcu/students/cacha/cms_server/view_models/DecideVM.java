package cz.zcu.students.cacha.cms_server.view_models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class DecideVM {

    @NotNull(message = "Id článku nesmí být prázdné")
    private Long articleId;

    @NotNull(message = "Hodnocení článku nesmí býr prázdné")
    private String evaluation;

    @NotNull(message = "Doporučení článku nesmí býr prázdné")
    private Boolean recommend;
}
