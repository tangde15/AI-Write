package com.write.write.repository;

import com.write.write.entity.SampleEssay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleEssayRepository extends JpaRepository<SampleEssay, Long> {
    
    /** 获取所有范文，按创建时间倒序 **/
    List<SampleEssay> findAllByOrderByCreatedAtDesc();
    
    /** 根据标签查询范文 **/
    List<SampleEssay> findByTagOrderByCreatedAtDesc(String tag);
    
    /** 获取收藏数最高的范文 **/
    @Query("SELECT s FROM SampleEssay s ORDER BY s.favoriteCount DESC, s.createdAt DESC")
    List<SampleEssay> findTopByOrderByFavoriteCountDesc();
}


