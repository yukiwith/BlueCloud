package com.example.bolgwithcontents.service.impl;

import com.example.bolgwithcontents.model.Comment;
import com.example.bolgwithcontents.repository.CommentRepository;
import com.example.bolgwithcontents.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;



    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public long getCommentNumber(String blogId) {
        return commentRepository.count();
    }

    @Override
    public List<Comment> findAllFirstComment(String blogId) {
        return commentRepository.findAllByRootIdIsNullAndBlogId(blogId);
    }

    @Override
    public List<Comment> findAllReplyByCommentId(String commentId) {
        return commentRepository.findAllByRootId(commentId);
    }

    /**
     *
     * @param comment
     * @return
     *
     * 添加评论
     * 套娃递归
     */
    @Override
    public boolean add(Comment comment) {
        comment.setCommentId(UUID.randomUUID().toString());
        commentRepository.save(comment);

        while (comment.getNumber() > 0) {
            Optional<Comment> parentComment = commentRepository.findById(comment.getParentId());
            // 如果是多级评论需检测上级评论是否还存在
            if (parentComment.isEmpty() || commentRepository.findById(comment.getRootId()).isEmpty()) return false;
            parentComment.get().getReply().put(comment.getCommentId(),comment);
            commentRepository.save(parentComment.get());
            comment = parentComment.get();
        }
        return true;
    }

    /**
     *
     * @param commentId
     *
     * 如果删除的评论是一级评论，则只用删除该条评论
     * 如果删除的评论是多级评论，则还需删除上级评论的reply
     * 如果检测到要删除的评论不存在，则其本身也不会显示，所以不需要返回删除失败的提示
     */
    @Override
    public void delete(String commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        if (commentOptional.isEmpty()) return;
        Comment comment = commentOptional.get();

        commentRepository.deleteById(commentId);
        // 如果是多级评论删除父评论的记录
        if (comment.getNumber() > 0) {

            Optional<Comment> parentOptional = commentRepository.findById(comment.getParentId());
            if (parentOptional.isEmpty()) return;

            Comment parentComment = parentOptional.get();
            parentComment.getReply().remove(comment);
            commentRepository.save(parentComment);
        }
    }

}
