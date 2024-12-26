package com.example.bolgwithcontents.repository;

import com.example.bolgwithcontents.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment,String> {


    List<Comment> findAllByRootIdIsNullAndBlogId(String blogId);

    List<Comment> findAllByRootId(String commentId);

    long countByBlogId(String blogId);

    List<Comment> findByBlogIdNotIn(List<String> blogIds);

}
