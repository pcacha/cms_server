package cz.zcu.students.cacha.cms_server.view_models;

import cz.zcu.students.cacha.cms_server.domain.Review;
import cz.zcu.students.cacha.cms_server.shared.ArticleState;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewVM {

    private Long id;
    private String articleName;
    private String articleAuthor;
    private String evaluation;
    private Boolean recommended;
    private Integer starCount;
    private Boolean editable;
    private String reviewAuthor;


    public ReviewVM(Review review) {
        this.id = review.getId();
        this.articleName = review.getArticle().getName();
        this.articleAuthor = review.getArticle().getUser().getUsername();
        this.evaluation = review.getEvaluation();
        this.recommended = review.getRecommended();
        this.starCount = review.getStarCount();
        this.editable = review.getArticle().getState() == ArticleState.REVIEWED;
        this.reviewAuthor = review.getUser().getUsername();
    }
}
