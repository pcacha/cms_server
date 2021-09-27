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
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

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

    public List<ArticleVM> getPublished() {
        List<Article> articles =  articleRepository.findByPublishedAtIsNotNullOrderByPublishedAtDesc();
        List<ArticleVM> articleVMS = articles.stream().map(ArticleVM::new).collect(Collectors.toList());
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

    public List<ArticleVM> getMyArticles(User user) {
        List<Article> articles =  articleRepository.findPersonalArticles(user.getId());
        List<ArticleVM> articleVMS = articles.stream().map(ArticleVM::new).collect(Collectors.toList());
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

    public List<ArticleVM> getReviewedArticles() {
        List<Article> reviewed = articleRepository.findByEvaluationIsNull();
        return reviewed.stream().map(ArticleVM::new).collect(Collectors.toList());
    }

    public List<ArticleToReviewVM> attachReviewers(List<ArticleVM> reviewed) {
        List<ArticleToReviewVM> toReview = new ArrayList();

        for(ArticleVM article : reviewed) {
            List<User> notReviewers = userRepository.selectNotReviewers(article.getId(), article.getAuthorId());
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

    public List<ReviewerVM> getReviewers(Long articleId) {
        return userRepository.selectReviewers(articleId).stream().map(ReviewerVM::new).collect(Collectors.toList());
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

    public List<ReviewVM> getArticleReviews(Long articleId) {
        List<Review> reviews = reviewRepository.findByArticleId(articleId);
        return reviews.stream().map(ReviewVM::new).collect(Collectors.toList());
    }
}
