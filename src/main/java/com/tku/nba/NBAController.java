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
            @RequestParam(required = false) String actionTrigger, // 接收前端傳來的動作觸發器
            Model model) {

        // 1. 生成賽季清單
        List<String> seasons = IntStream.rangeClosed(2000, 2025)
                .mapToObj(String::valueOf).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        model.addAttribute("teamOptions", nbaService.getTeams());
        model.addAttribute("seasonOptions", seasons);

        // 2. 獲取分析報告 (只要有 name 就先去查，確保舊圖表能釘在畫面上)
        PlayerDTO report = null;
        if (name != null && !name.isEmpty() && !name.equals("none")) {
            report = nbaService.getFullAnalytics(name, season, selectedTeam);
        }

        // 3. --- 核心邏輯：要不要強制覆蓋球隊選單？ ---
        // 只有在「沒選球隊」、「改了賽季」或「改了球員」時，球隊選單才強制跟著球員跑。
        // 如果使用者剛剛是「手動改了球隊 (actionTrigger == 'team')」，絕對尊重他的選擇，不覆蓋！
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
            // 預設球員名單
            playerOptions.addAll(Arrays.asList("LeBron James", "Kobe Bryant", "Stephen Curry", "Luka Doncic", "James Harden"));
        }

        // 如果目前選定的舊球員不在新名單內，就把選單退回 "-- 選擇球員 --"
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

        return "report";
    }
}
