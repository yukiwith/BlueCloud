package com.example.bolgwithcontents.repository;

import com.example.bolgwithcontents.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture,Long> {

    List<Picture> findByBlogIdIn(List<String> blogId);

}
