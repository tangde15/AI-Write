package com.write.write.controller;

import com.write.write.dto.WritingRequest;
import com.write.write.dto.WritingResponse;
import com.write.write.service.WritingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/writing")
public class WritingController {


    private final WritingService writingService;


    // 使用构造器注入，确保writingService被正确初始化
    @Autowired
    public WritingController(WritingService writingService) {
        this.writingService = writingService;
    }


    @PostMapping("/process")
    public ResponseEntity<WritingResponse> process(@RequestBody WritingRequest request) {
        // 调用服务层处理请求
        String aiResponse = writingService.handleRequest(request);

        // 封装响应并返回
        WritingResponse response = new WritingResponse(aiResponse);
        return ResponseEntity.ok(response);
    }
}
