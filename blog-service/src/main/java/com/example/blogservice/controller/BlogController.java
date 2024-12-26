package com.example.bolgwithcontents.controller;


import com.example.bolgwithcontents.model.BlogUpload;
import com.example.bolgwithcontents.service.impl.BlogServiceImpl;
import com.example.bolgwithcontents.model.Result;
import com.example.bolgwithcontents.service.impl.LikeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/blog")

public class BlogController {

    @Autowired
    private BlogServiceImpl blogService;

    @Autowired
    private LikeServiceImpl likeService;

    @GetMapping
    public Result findAll(int page) {
        return new Result("200","",blogService.getBlogs(page,20));
    }

    /**
     *
     * 将图片存储到本地，路径和其他数据存入数据库
     *
     */
    @PostMapping("/addBlog")
    public Result addBlog(@RequestParam("title") String title,
                          @RequestParam("content") String content,
                          @RequestParam("userId") String userId,
                          @RequestParam("userName") String userName,
                          @RequestParam("picture") List<MultipartFile> picture) {
        BlogUpload blogUpload = new BlogUpload();
        blogUpload.setTitle(title);
        blogUpload.setContent(content);
        blogUpload.setUserId(userId);
        blogUpload.setUserName(userName);
        blogUpload.setPicture(picture);
        blogService.addBlog(blogUpload);
        return new Result("200","成功添加",null);
    }

    @DeleteMapping("/deleteBlog/{id}")
    public Result deleteBlog(@PathVariable String id) {
        blogService.deleteBlogById(id);
        return new Result("200","成功删除",null);
    }

    @GetMapping("/search")
    public Result searchBlog(String title) {
        return new Result("200","",blogService.findByTitle(title));
    }

    @GetMapping("/into")
    public Result getBlog(String blogId) {
        return new Result("200","",blogService.findById(blogId));
    }

    @PostMapping("/like")
    public Result like(@RequestParam("userId") String userId, @RequestParam("blogId") String blogId) {
        likeService.likePost(userId,blogId);
        return new Result("200","成功点赞",null);
    }

    @PostMapping("/cancelLike")
    public Result cancelLike(@RequestParam("userId") String userId, @RequestParam("blogId") String blogId) {
        likeService.cancelLikePost(userId,blogId);
        return new Result("200","取消点赞",null);
    }

    @GetMapping("/getLikes")
    public Result getLikes(String blogId) {
        return new Result("200", "", likeService.countLikes(blogId));
    }
}
