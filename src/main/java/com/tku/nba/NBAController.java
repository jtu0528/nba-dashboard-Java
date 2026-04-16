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
            @RequestParam(required = false, defaultValue = "2026") String season, 
            Model model) {

        // 1. 生成賽季清單
        List<String> seasons = IntStream.rangeClosed(2000, 2026)
                .mapToObj(String::valueOf).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        model.addAttribute("teamOptions", nbaService.getTeams());
        model.addAttribute("seasonOptions", seasons);

        // 2. 動態生成球員清單
        List<String> playerOptions = new ArrayList<>();
        if (team != null && !team.isEmpty()) {
            playerOptions.addAll(nbaService.getPlayersByTeamAndSeason(team, season));
        } else {
            // 預設明星組合
            playerOptions.addAll(Arrays.asList("Michael Jordan", "Kobe Bryant", "LeBron James", "Stephen Curry", "Luka Doncic"));
        }

        // --- 防重置核心邏輯 ---
        if (name != null && !name.isEmpty() && !name.equals("none") && !playerOptions.contains(name)) {
            playerOptions.add(0, name); // 強制將當前選中球員塞入選單，防止 HTML 重置
        }
        model.addAttribute("playerOptions", playerOptions);

        // 3. 獲取分析報告
        PlayerDTO report = null;
        if (name != null && !name.isEmpty() && !name.equals("none")) {
            report = nbaService.getFullAnalytics(name, season);
            if (report != null) {
                // 如果球員換隊，自動更新球隊選單顯示
                String cleanTeam = report.getTeam().split(" ")[0];
                if (nbaService.getTeams().contains(cleanTeam)) team = cleanTeam;
            }
        }

        model.addAttribute("report", report);
        model.addAttribute("selectedTeam", team);
        model.addAttribute("selectedName", name);
        model.addAttribute("selectedSeason", season);

        return "report";
    }
}
