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
            @RequestParam(required = false) String actionTrigger, 
            Model model) {

        // 生成賽季清單
        List<String> seasons = IntStream.rangeClosed(2000, 2025)
                .mapToObj(String::valueOf).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        model.addAttribute("teamOptions", nbaService.getTeams());
        model.addAttribute("seasonOptions", seasons);

        // 獲取分析報告
        PlayerDTO report = null;
        if (name != null && !name.isEmpty() && !name.equals("none")) {
            report = nbaService.getFullAnalytics(name, season, selectedTeam);
        }

        // 強制覆蓋球隊選單？
        if (report != null) {
            boolean shouldFollowPlayer = (team == null || team.isEmpty() || "season".equals(actionTrigger) || "name".equals(actionTrigger));
            if (shouldFollowPlayer) {
                String rawTeam = report.getTeam();
                if (rawTeam != null && !rawTeam.isEmpty()) {
                    String cleanTeam = rawTeam.split(" ")[0];
                    if (nbaService.getTeams().contains(cleanTeam)) {
                        team = cleanTeam; 
                    }
                }
            }
        }

        // 4. 動態生成該球隊的球員清單
        List<String> playerOptions = new ArrayList<>();
        if (team != null && !team.isEmpty()) {
            playerOptions.addAll(nbaService.getPlayersByTeamAndSeason(team, season));
        } else {
            playerOptions.addAll(Arrays.asList("LeBron James", "Kobe Bryant", "Stephen Curry", "Luka Doncic", "James Harden"));
        }

        // 5. 判斷選單狀態
        String displaySelectedName = name;
        if (name != null && !name.equals("none") && !playerOptions.contains(name)) {
            displaySelectedName = "none";
        }

        // 6. 返回模型數據 
        model.addAttribute("report", report); 
        model.addAttribute("filterTeam", team);         
        model.addAttribute("selectedTeam", selectedTeam); 
        model.addAttribute("selectedName", displaySelectedName); 
        model.addAttribute("selectedSeason", season);
        model.addAttribute("playerOptions", playerOptions); 

        return "report";
    }
}
