package cz.zcu.students.cacha.cms_server.controllers;

import cz.zcu.students.cacha.cms_server.domain.Article;
import cz.zcu.students.cacha.cms_server.domain.User;
import cz.zcu.students.cacha.cms_server.responses.GenericResponse;
import cz.zcu.students.cacha.cms_server.services.ArticleService;
import cz.zcu.students.cacha.cms_server.shared.CurrentUser;
import cz.zcu.students.cacha.cms_server.view_models.ArticleVM;
import cz.zcu.students.cacha.cms_server.view_models.UpdateArticleVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public Set<ArticleVM> getPublished() {
        Set<ArticleVM> articles = articleService.getPublished();
        return articles;
    }

    @PostMapping
    public GenericResponse createArticle(@Valid @RequestBody Article article, @CurrentUser User user) {
        articleService.save(article, user);
        return new GenericResponse("Článek byl vytvořen");
    }

    @PutMapping("/update")
    public GenericResponse updateArticle(@Valid @RequestBody UpdateArticleVM updateArticleVM, @CurrentUser User user) {
        articleService.update(updateArticleVM, updateArticleVM.getId(), user);
        return new GenericResponse("Článek byl aktualizován");
    }

    @GetMapping("/personal")
    public Set<ArticleVM> getMyArticles(@CurrentUser User user) {
        Set<ArticleVM> articles = articleService.getMyArticles(user);
        return articles;
    }

    @GetMapping("/{articleId}")
    public ArticleVM getArticle(@PathVariable Long articleId, @CurrentUser User user) {
        ArticleVM article = articleService.getArticle(articleId, user);
        return article;
    }
}
