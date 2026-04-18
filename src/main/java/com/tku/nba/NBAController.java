package com.tku.nba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class NBAController {

    @Autowired
    private NBAService nbaService;

    @GetMapping("/")
    public String index(
            @RequestParam(required = false) String team,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "2025") String season, 
            @RequestParam(required = false, defaultValue = "TOT") String selectedTeam, 
            Model model) {

        // 1. 生成賽季清單 
        List<String> seasons = IntStream.rangeClosed(2000, 2025)
                .mapToObj(String::valueOf)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        
        model.addAttribute("teamOptions", nbaService.getTeams());
        model.addAttribute("seasonOptions", seasons);

        // 2. 動態生成球員清單
        List<String> playerOptions = new ArrayList<>();
        if (team != null && !team.isEmpty()) {
            playerOptions.addAll(nbaService.getPlayersByTeamAndSeason(team, season));
        } else {
            // 預設推薦球員清單
            playerOptions.addAll(Arrays.asList("LeBron James", "Kobe Bryant", "Stephen Curry", "Luka Doncic", "Victor Wembanyama"));
        }

        if (name != null && !name.isEmpty() && !name.equals("none") && !playerOptions.contains(name)) {
            playerOptions.add(0, name); 
        }
        model.addAttribute("playerOptions", playerOptions);

        // 3. 獲取分析報告
        PlayerDTO report = null;
        if (name != null && !name.isEmpty() && !name.equals("none")) {
            report = nbaService.getFullAnalytics(name, season, selectedTeam);
            
            if (report != null) {
                // 球員換隊，自動更新球隊選單顯示
                String rawTeam = report.getTeam();
                if (rawTeam != null && !rawTeam.isEmpty()) {
                    String cleanTeam = rawTeam.split(" ")[0];
                    if (nbaService.getTeams().contains(cleanTeam)) {
                        team = cleanTeam;
                    }
                }
            }
        }

        // 4. 返回模型數據 
        model.addAttribute("report", report);
        model.addAttribute("filterTeam", team);         // 給頂部「1. 篩選球隊」下拉選單用的
        model.addAttribute("selectedTeam", selectedTeam); // 給球員「多隊切換按鈕」用的
        model.addAttribute("selectedName", name);
        model.addAttribute("selectedSeason", season);

        return "report";
    }
}
