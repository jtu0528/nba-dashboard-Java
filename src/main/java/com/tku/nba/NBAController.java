package com.tku.nba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Arrays;
import java.util.List;

@Controller
public class NBAController {

    @Autowired
    private NBAService nbaService;

    @GetMapping("/")
    public String showReport(
            @RequestParam(name = "name", required = false, defaultValue = "Stephen Curry") String name,
            @RequestParam(name = "season", required = false, defaultValue = "2023") String season,
            @RequestParam(name = "team", required = false, defaultValue = "Warriors") String team,
            Model model) {

        // 1. 準備選單選項
        model.addAttribute("playerOptions", Arrays.asList("Stephen Curry", "LeBron James", "Luka Doncic", "Kevin Durant", "Rudy Gobert", "Chris Paul"));
        model.addAttribute("seasonOptions", Arrays.asList("2023", "2022", "2021", "2020"));
        model.addAttribute("teamOptions", Arrays.asList("Warriors", "Lakers", "Mavericks", "Suns", "Bucks", "Timberwolves"));

        // 2. 抓取數據與趨勢
        PlayerDTO report = nbaService.getPlayerData(name, season);
        
        // 3. 傳遞數據與狀態
        model.addAttribute("report", report);
        model.addAttribute("selectedName", name);
        model.addAttribute("selectedSeason", season);
        model.addAttribute("selectedTeam", team);

        return "report";
    }
}
