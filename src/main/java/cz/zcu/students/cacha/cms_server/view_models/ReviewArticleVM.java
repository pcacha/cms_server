package cz.zcu.students.cacha.cms_server.view_models;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.zcu.students.cacha.cms_server.domain.Article;
import cz.zcu.students.cacha.cms_server.domain.Review;
import cz.zcu.students.cacha.cms_server.shared.ArticleState;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ReviewArticleVM {

    private Long reviewId;
    private String name;
    private String text;
    private String documentName;
    private String username;
    @JsonFormat(pattern = "dd.MM. yyyy")
    private Date publishedAt;
    @JsonFormat(pattern = "dd.MM. yyyy")
    private Date createdAt;

    public ReviewArticleVM(Article article, Review review) {
        this.reviewId = review.getId();
        this.name = article.getName();
        this.text = article.getText();
        this.documentName = article.getDocumentName();
        this.username = article.getUser().getUsername();
        this.publishedAt = article.getPublishedAt();
        this.createdAt = article.getCreatedAt();
    }
}
