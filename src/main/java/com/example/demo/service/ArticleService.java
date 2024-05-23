package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.controller.dto.ArticleCreateRequest;
import com.example.demo.controller.dto.ArticleResponse;
import com.example.demo.controller.dto.ArticleUpdateRequest;
import com.example.demo.domain.Article;
import com.example.demo.domain.Board;
import com.example.demo.domain.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.MemberRepository;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public ArticleService(
        ArticleRepository articleRepository,
        MemberRepository memberRepository,
        BoardRepository boardRepository
    ) {
        this.articleRepository = articleRepository;
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
    }

    public List<ArticleResponse> getAll() {
        List<Article> articles = articleRepository.findAll();
        return articles.stream()
            .map(article -> {
                Member member = memberRepository.findById(article.getAuthorId());
                Board board = boardRepository.findById(article.getBoardId());
                return ArticleResponse.of(article, member, board);
            })
            .toList();
    }

    public ArticleResponse getById(Long id) {
        Article article = articleRepository.findById(id);
        Member member = memberRepository.findById(article.getAuthorId());
        Board board = boardRepository.findById(article.getBoardId());
        return ArticleResponse.of(article, member, board);
    }

    public ArticleResponse create(ArticleCreateRequest request) {
        Article article = new Article(
            request.authorId(),
            request.boardId(),
            request.title(),
            request.description(),
            LocalDateTime.now()
        );
        Article saved = articleRepository.insert(article);
        Member member = memberRepository.findById(saved.getAuthorId());
        Board board = boardRepository.findById(article.getBoardId());
        return ArticleResponse.of(article, member, board);
    }

    public ArticleResponse update(Long id, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(id);
        article.update(request.boardId(), request.title(), request.description());
        Article updated = articleRepository.update(id, article);
        Member member = memberRepository.findById(updated.getAuthorId());
        Board board = boardRepository.findById(article.getBoardId());
        return ArticleResponse.of(article, member, board);
    }

    public void delete(Long id) {
        articleRepository.deleteById(id);
    }
}