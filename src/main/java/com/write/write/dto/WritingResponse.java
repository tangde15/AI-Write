package com.write.write.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 自动生成 getter、setter、toString、equals、hashCode
@NoArgsConstructor // 自动生成无参构造方法
@AllArgsConstructor // 自动生成全参构造方法（替代手动写的有参构造）
public class WritingResponse {
    private String ai_response;
}
