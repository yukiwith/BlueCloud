package com.example.bolgwithcontents.repository;

import com.example.bolgwithcontents.model.StuffPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StuffPictureRepository extends JpaRepository<StuffPicture, Long> {

    List<StuffPicture> findByStuffIdIn(List<Long> stuffIds);

}
