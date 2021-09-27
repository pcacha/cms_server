package cz.zcu.students.cacha.cms_server.view_models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class AddReviewerVM {

    @NotNull(message = "Id článků nesmí být prázdné")
    private Long articleId;

    @NotNull(message = "Id uživatele nesmí být prázdné")
    private Long userId;
}
