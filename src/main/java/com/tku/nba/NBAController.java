package com.tku.nba.controller;

import com.tku.nba.service.NBAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NBAController {

    @Autowired
    private NBAService nbaService;

    @GetMapping("/")
    public String index() {
        return "index"; // 指向 index.html
    }

    @GetMapping("/analyze")
    public String analyze(@RequestParam String name, Model model) {
        model.addAttribute("report", nbaService.getPlayerAnalysis(name));
        return "report"; // 指向 report.html 展示結果
    }
}
