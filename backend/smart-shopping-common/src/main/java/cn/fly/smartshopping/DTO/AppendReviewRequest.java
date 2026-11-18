package cn.fly.smartshopping.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AppendReviewRequest {
    @NotBlank(message = "追加内容不能为空")
    @Size(max = 500, message = "追加内容不能超过500字")
    private String content;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
