package com.example.bolgwithcontents.controller;

import com.example.bolgwithcontents.model.Comment;
import com.example.bolgwithcontents.service.impl.CommentServiceImpl;
import com.example.bolgwithcontents.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")

public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    @GetMapping
    public Result findAll() {
        return new Result("200","",commentService.findAll());
    }

    @GetMapping("/count")
    public Result count(String blogId) {
        return new Result("200","",commentService.getCommentNumber(blogId));
    }

    @GetMapping("/firstComment")
    public Result findAllFirstComment(String blogId) {
        return new Result("200","",commentService.findAllFirstComment(blogId));
    }

    @GetMapping("/reply")
    public Result findAllReply(String commentId) {
        return new Result("200","",commentService.findAllReplyByCommentId(commentId));
    }

    @PostMapping("/add")
    public Result add(@RequestBody Comment comment) {
        if(!commentService.add(comment)) return new Result("204","评论消失了(ÒωÓױ)",null);
        return new Result("200","",null);
    }

    @DeleteMapping("/delete/{commentId}")
    public Result delete(@PathVariable String commentId) {
        commentService.delete(commentId);
        return new Result("200","",null);
    }
}
