package com.write.write.controller;

import com.write.write.api.ApiResponse;
import com.write.write.service.DevService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dev")
public class DevController {
    private final DevService devService;

    public DevController(DevService devService) {
        this.devService = devService;
    }

    @GetMapping("/ping")
    public ApiResponse<String> ping() {
        return ApiResponse.ok("pong");
    }

    @GetMapping("/async-demo")
    public ApiResponse<String> asyncDemo(@RequestParam(name = "ms", defaultValue = "2000") long ms,
                                         @RequestParam(name = "note", defaultValue = "demo") String note) {
        devService.asyncWork(ms, note);
        return ApiResponse.ok("accepted: async started");
    }

    @GetMapping("/boom")
    public ApiResponse<String> boom() {
        throw new RuntimeException("demo boom: intentional error for screenshot");
    }
}
