package com.example.bolgwithcontents.repository;

import com.example.bolgwithcontents.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog,String> {
    Optional<Blog> findBlogByBlogId(String id);

    List<Blog> findBlogByTitleContaining(String title);


    @Query("SELECT b.blogId FROM Blog b")
    List<String> findAllIds();


}
