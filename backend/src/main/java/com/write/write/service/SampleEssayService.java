package com.write.write.service;

import com.write.write.entity.SampleEssay;
import com.write.write.repository.SampleEssayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SampleEssayService {

    private final SampleEssayRepository sampleEssayRepository;

    /** 获取所有范文（按创建时间倒序） **/
    public List<SampleEssay> getAllEssays() {
        return sampleEssayRepository.findAllByOrderByCreatedAtDesc();
    }

    /** 根据ID获取范文详情 **/
    public Optional<SampleEssay> getEssayById(Long id) {
        return sampleEssayRepository.findById(id);
    }

    /** 根据标签获取范文 **/
    public List<SampleEssay> getEssaysByTag(String tag) {
        return sampleEssayRepository.findByTagOrderByCreatedAtDesc(tag);
    }

    /** 获取收藏数最高的范文（用于收藏榜单） **/
    public List<SampleEssay> getTopFavoriteEssays() {
        return sampleEssayRepository.findTopByOrderByFavoriteCountDesc();
    }

    /** 增加收藏数 **/
    public void incrementFavoriteCount(Long id) {
        Optional<SampleEssay> essayOpt = sampleEssayRepository.findById(id);
        if (essayOpt.isPresent()) {
            SampleEssay essay = essayOpt.get();
            essay.setFavoriteCount(essay.getFavoriteCount() + 1);
            sampleEssayRepository.save(essay);
        }
    }
}


