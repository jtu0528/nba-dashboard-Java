package com.tku.nba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // 注意：這裡必須用 @Controller，才能回傳 Thymeleaf 網頁
public class NBAController {

    @Autowired
    private NBAService nbaService;

    /**
     * 當使用者訪問首頁，或在搜尋框輸入名字後按下提交時，會觸發此方法。
     * @param name 使用者輸入的球員姓名，預設值為 Stephen Curry
     */
    @GetMapping("/")
    public String showReport(
            @RequestParam(name = "name", required = false, defaultValue = "Stephen Curry") String name, 
            Model model) {
        
        // 1. 呼叫 Service 去 API 抓資料並執行分類邏輯
        PlayerDTO report = nbaService.getPlayerData(name);
        
        // 2. 將分析結果打包進 model，讓 HTML 端的 ${report.xxx} 可以讀到
        model.addAttribute("report", report);
        
        // 3. 告訴 Spring Boot 去找 templates/report.html
        return "report";
    }
}
