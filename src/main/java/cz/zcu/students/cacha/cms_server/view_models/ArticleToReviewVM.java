package cz.zcu.students.cacha.cms_server.view_models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ArticleToReviewVM {
    private List<ReviewerVM> notReviewers;
    private ArticleVM article;

    public ArticleToReviewVM(List<ReviewerVM> notReviewers, ArticleVM article) {
        this.notReviewers = notReviewers;
        this.article = article;
    }
}
