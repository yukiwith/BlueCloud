package com.example.bolgwithcontents.repository;

import com.example.bolgwithcontents.model.Stuff;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StuffRepository extends JpaRepository<Stuff,Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Stuff s SET s.stock = s.stock - 1 WHERE s.stuffId = :stuffId")
    void sell(@Param("stuffId") Long stuffId);

    List<Stuff> findByStuffIdIn(List<Long> stuffIds);

    @Modifying
    @Transactional
    @Query("UPDATE Stuff s SET s.stock = s.stock + :amount WHERE s.stuffId = :stuffId")
    void updateStockByStuffId(@Param("stuffId") Long stuffId, @Param("amount") int amount);

}
