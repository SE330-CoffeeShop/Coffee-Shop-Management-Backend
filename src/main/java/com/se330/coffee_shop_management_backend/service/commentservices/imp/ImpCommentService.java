package com.se330.coffee_shop_management_backend.service.commentservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.comment.CommentCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.comment.CommentUpdateDTO;
import com.se330.coffee_shop_management_backend.entity.Comment;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.repository.CommentRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import com.se330.coffee_shop_management_backend.service.commentservices.ICommentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ImpCommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;

    public ImpCommentService(CommentRepository commentRepository, ProductRepository productRepository) {
        this.commentRepository = commentRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Comment findByIdComment(int id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Comment> findAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Override
    public Comment createComment(CommentCreateRequestDTO commentCreateRequestDTO) {
        Product product = productRepository.findById(UUID.fromString(commentCreateRequestDTO.getProductId()))
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + commentCreateRequestDTO.getProductId()));

        return commentRepository.save(
                Comment.builder()
                        .product(product)
                        .commentContent(commentCreateRequestDTO.getCommentContent())
                        .commentLeft(commentCreateRequestDTO.getCommentLeft())
                        .commentRight(commentCreateRequestDTO.getCommentRight())
                        .commentIsDeleted(false)
                        .commentRating(BigDecimal.valueOf(0))
                        .build()
        );
    }

    @Override
    public Comment updateComment(CommentUpdateDTO commentUpdateDTO) {
        int commentId = commentUpdateDTO.getCommentId();

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with ID: " + commentId));

        existingComment.setCommentContent(commentUpdateDTO.getCommentContent());
        existingComment.setCommentRating(commentUpdateDTO.getCommentRating());
        existingComment.setCommentIsDeleted(commentUpdateDTO.isCommentIsDeleted());

        return commentRepository.save(existingComment);
    }

    @Override
    public void deleteComment(int id) {
        commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with ID: " + id));

        commentRepository.deleteById(id);
    }
}