package cz.zcu.students.cacha.cms_server.controllers;

import cz.zcu.students.cacha.cms_server.responses.GenericResponse;
import cz.zcu.students.cacha.cms_server.services.ArticleService;
import cz.zcu.students.cacha.cms_server.view_models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/api/articlemanager")
public class AdminArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public Set<ArticleToReviewVM> getArticlesToReview() {
        Set<ArticleVM> reviewed = articleService.getReviewedArticles();
        Set<ArticleToReviewVM> articlesToReview = articleService.attachReviewers(reviewed);
        return articlesToReview;
    }

    @PostMapping("/reviewer")
    public GenericResponse addReviewer(@Valid @RequestBody AddReviewerVM addReviewerVM) {
        articleService.addReviewer(addReviewerVM.getArticleId(), addReviewerVM.getUserId());
        return new GenericResponse("Uživatel byl přidán jako rezenzent článku");
    }

    @GetMapping("/reviewer/{articleId}")
    public Set<ReviewerVM> getReviewers(@PathVariable Long articleId) {
        Set<ReviewerVM> reviewers = articleService.getReviewers(articleId);
        return reviewers;
    }

    @PostMapping("/decide")
    public GenericResponse decideArticle(@Valid @RequestBody DecideVM decideVM) {
        articleService.addDecide(decideVM);
        return new GenericResponse("Rozhodnutí o článku bylo přidáno");
    }

    @GetMapping("/reviews/{articleId}")
    public Set<ReviewVM> getArticleReviews(@PathVariable Long articleId) {
        Set<ReviewVM> reviews = articleService.getArticleReviews(articleId);
        return reviews;
    }
}
