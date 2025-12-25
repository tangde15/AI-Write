package com.write.write.controller;

import com.write.write.dto.SampleEssayDTO;
import com.write.write.entity.SampleEssay;
import com.write.write.service.SampleEssayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sample-essays")
@RequiredArgsConstructor
public class SampleEssayController {

    private final SampleEssayService sampleEssayService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** 获取所有范文（每日推荐） **/
    @GetMapping
    public ResponseEntity<List<SampleEssayDTO>> getAllEssays() {
        List<SampleEssay> essays = sampleEssayService.getAllEssays();
        System.out.println("[SampleEssay] 查询到 " + essays.size() + " 篇范文");
        List<SampleEssayDTO> dtos = essays.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /** 根据ID获取范文详情 **/
    @GetMapping("/{id}")
    public ResponseEntity<SampleEssayDTO> getEssayById(@PathVariable Long id) {
        return sampleEssayService.getEssayById(id)
                .map(essay -> ResponseEntity.ok(convertToDTO(essay)))
                .orElse(ResponseEntity.notFound().build());
    }

    /** 根据标签获取范文 **/
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<SampleEssayDTO>> getEssaysByTag(@PathVariable String tag) {
        List<SampleEssay> essays = sampleEssayService.getEssaysByTag(tag);
        List<SampleEssayDTO> dtos = essays.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /** 获取收藏榜单（收藏数最高的范文） **/
    @GetMapping("/top-favorites")
    public ResponseEntity<List<SampleEssayDTO>> getTopFavoriteEssays() {
        List<SampleEssay> essays = sampleEssayService.getTopFavoriteEssays();
        System.out.println("[SampleEssay] 查询到 " + essays.size() + " 篇收藏榜单范文");
        List<SampleEssayDTO> dtos = essays.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /** 增加收藏数 **/
    @PostMapping("/{id}/favorite")
    public ResponseEntity<String> incrementFavorite(@PathVariable Long id) {
        sampleEssayService.incrementFavoriteCount(id);
        return ResponseEntity.ok("收藏成功");
    }

    /** 转换为DTO **/
    private SampleEssayDTO convertToDTO(SampleEssay essay) {
        SampleEssayDTO dto = new SampleEssayDTO();
        dto.setId(essay.getId());
        dto.setTitle(essay.getTitle());
        dto.setAuthorName(essay.getAuthorName());
        dto.setAuthor(essay.getAuthor());
        dto.setContent(essay.getContent());
        dto.setTag(essay.getTag());
        dto.setFavoriteCount(essay.getFavoriteCount());
        if (essay.getCreatedAt() != null) {
            dto.setCreatedAt(essay.getCreatedAt().format(DATE_FORMATTER));
        }
        return dto;
    }
}

