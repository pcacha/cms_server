package cz.zcu.students.cacha.cms_server.view_models;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.zcu.students.cacha.cms_server.domain.Article;
import cz.zcu.students.cacha.cms_server.shared.ArticleState;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ArticleVM {

    private Long id;
    private String name;
    private String text;
    private String documentName;
    private String username;
    @JsonFormat(pattern = "dd.MM. yyyy")
    private Date publishedAt;
    @JsonFormat(pattern = "dd.MM. yyyy")
    private Date createdAt;
    private String evaluation;
    private ArticleState state;
    private boolean editable;
    private Long authorId;

    public ArticleVM(Article article) {
        this.id = article.getId();
        this.name = article.getName();
        this.text = article.getText();
        this.documentName = article.getDocumentName();
        this.publishedAt = article.getPublishedAt();
        this.createdAt = article.getCreatedAt();
        this.evaluation = article.getEvaluation();
        this.state = article.getState();
        this.username = article.getUser().getUsername();
        this.editable = article.getState() == ArticleState.REVIEWED;
        this.authorId = article.getUser().getId();
    }
}
