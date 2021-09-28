package cz.zcu.students.cacha.cms_server.services;

import cz.zcu.students.cacha.cms_server.domain.Article;
import cz.zcu.students.cacha.cms_server.domain.Review;
import cz.zcu.students.cacha.cms_server.domain.User;
import cz.zcu.students.cacha.cms_server.exceptions.*;
import cz.zcu.students.cacha.cms_server.repositories.ArticleRepository;
import cz.zcu.students.cacha.cms_server.repositories.ReviewRepository;
import cz.zcu.students.cacha.cms_server.repositories.UserRepository;
import cz.zcu.students.cacha.cms_server.shared.ArticleState;
import cz.zcu.students.cacha.cms_server.view_models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    public Set<ArticleVM> getPublished() {
        Set<Article> articles =  articleRepository.findByPublishedAtIsNotNullOrderByPublishedAtDesc();
        Set<ArticleVM> articleVMS = articles.stream().map(ArticleVM::new).collect(Collectors.toSet());
        return articleVMS;
    }

    public void save(Article article, User user) {
        article.setUser(user);
        String documentName;
        try {
            documentName = fileService.save(article.getDocumentName(), article.getEncodedDocument());
        } catch (IOException exception) {
            throw new CannotSaveDocumentException("Přiložený dokument se nepodařilo uložit");
        }
        article.setDocumentName(documentName);
        articleRepository.save(article);
    }

    public Set<ArticleVM> getMyArticles(User user) {
        Set<Article> articles =  articleRepository.findPersonalArticles(user.getId());
        Set<ArticleVM> articleVMS = articles.stream().map(ArticleVM::new).collect(Collectors.toSet());
        return articleVMS;
    }

    public void update(UpdateArticleVM updateArticleVM, Long id, User user) {
        Optional<Article> articleOptional = articleRepository.findById(id);
        if(articleOptional.isEmpty()) {
            throw new NotFoundException("Nebyl nalezen článek s daným id");
        }

        Article article = articleOptional.get();

        if(article.getState() != ArticleState.REVIEWED) {
            throw new CannotUpdateArticleException("Lze aktualizvat pouze aktuálně hodnocené články");
        }

        if(article.getUser().getId() != user.getId()) {
            throw new UnauthorizedException("Článek nepatří přihlášenému uživateli");
        }

        Article articleWithSendName = articleRepository.findByName(updateArticleVM.getName());
        if(articleWithSendName != null && !articleWithSendName.getId().equals(id)) {
            throw new ValidationErrorException(new HashMap<>(){{
                put("name", "Název je již obsazený");
            }});
        }

        article.setName(updateArticleVM.getName());
        article.setText(updateArticleVM.getText());

        if(updateArticleVM.getEncodedDocument() != null) {
            fileService.delete(article.getName());
            String documentName;
            try {
                documentName = fileService.save(updateArticleVM.getDocumentName(), updateArticleVM.getEncodedDocument());
            } catch (IOException exception) {
                throw new CannotSaveDocumentException("Přiložený dokument se nepodařilo uložit");
            }
            article.setDocumentName(documentName);
        }

        articleRepository.save(article);
    }

    public ArticleVM getArticle(Long articleId, User user) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);

        if(articleOptional.isEmpty()) {
            throw new NotFoundException("Článek nebyl nalezen");
        }

        Article article = articleOptional.get();

        if(article.getUser().getId() != user.getId() && !user.isAdmin()) {
            throw new UnauthorizedException("Článek nepatří přihlášenému uživateli");
        }

        return new ArticleVM(article);
    }

    public Set<ArticleVM> getReviewedArticles() {
        Set<Article> reviewed = articleRepository.findByEvaluationIsNull();
        return reviewed.stream().map(ArticleVM::new).collect(Collectors.toSet());
    }

    public Set<ArticleToReviewVM> attachReviewers(Set<ArticleVM> reviewed) {
        Set<ArticleToReviewVM> toReview = new HashSet<>();

        for(ArticleVM article : reviewed) {
            Set<User> notReviewers = userRepository.selectNotReviewers(article.getId(), article.getAuthorId());
            ArticleToReviewVM articleToReviewVM = new ArticleToReviewVM(notReviewers.stream().map(ReviewerVM::new).collect(Collectors.toList()), article);
            toReview.add(articleToReviewVM);
        }

        return toReview;
    }

    public void addReviewer(Long articleId, Long userId) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        if(articleOptional.isEmpty()) {
            throw new NotFoundException("Článek nebyl nalezen");
        }
        Article article = articleOptional.get();


        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new NotFoundException("Uživatel nebyl nalezen");
        }
        User user = userOptional.get();

        Review review = new Review();
        review.setUser(user);
        review.setArticle(article);
        reviewRepository.save(review);
    }

    public Set<ReviewerVM> getReviewers(Long articleId) {
        return userRepository.selectReviewers(articleId).stream().map(ReviewerVM::new).collect(Collectors.toSet());
    }

    public void addDecide(DecideVM decideVM) {
        Optional<Article> articleOptional = articleRepository.findById(decideVM.getArticleId());
        if(articleOptional.isEmpty()) {
            throw new NotFoundException("Článek nebyl nalezen");
        }
        Article article = articleOptional.get();

        if(article.getEvaluation() != null) {
            throw new CannotPerformActionException("Článek je již ohodnocen");
        }

        article.setEvaluation(decideVM.getEvaluation());

        if(decideVM.getRecommend()) {
            article.setState(ArticleState.ACCEPTED);
        } else {
            article.setState(ArticleState.REJECTED);
        }

        article.setPublishedAt(new Date());

        articleRepository.save(article);
    }

    public Set<ReviewVM> getArticleReviews(Long articleId) {
        Set<Review> reviews = reviewRepository.findByArticleId(articleId);
        return reviews.stream().map(ReviewVM::new).collect(Collectors.toSet());
    }
}
