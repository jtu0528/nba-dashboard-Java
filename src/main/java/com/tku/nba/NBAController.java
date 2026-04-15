package com.tku.nba;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NBAController {

    @GetMapping("/")
    public String showReport(@RequestParam(defaultValue = "LeBron James") String name, Model model) {
        // 1. 模擬數據 
        PlayerDTO report = new PlayerDTO();
        report.setFullName(name);
        report.setPts(25.7);
        report.setReb(7.3);
        report.setAst(8.3);
        report.setCoreStyle("🌟 頂級全能巨星");

        // 2. 傳給 HTML，名稱叫 "report"
        model.addAttribute("report", report);

        // 3. 找 templates/report.html
        return "report";
    }
}
