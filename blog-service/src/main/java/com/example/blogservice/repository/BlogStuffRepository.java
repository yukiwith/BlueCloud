package com.example.bolgwithcontents.repository;

import com.example.bolgwithcontents.model.BlogStuffConnected;
import com.example.bolgwithcontents.model.BlogStuffId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogStuffConnectedRepository extends JpaRepository<BlogStuffConnected, BlogStuffId> {

    @Query(value = "SELECT bs.stuff_id FROM blog_stuff AS bs WHERE bs.blog_id = :blogId", nativeQuery = true)
    List<Long> findStuffIdsByBlogId(@Param("blogId") String blogId);
}
