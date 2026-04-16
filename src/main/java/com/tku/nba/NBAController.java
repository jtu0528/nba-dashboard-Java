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
            Model model) {

        // 1. 賽季選單
        List<String> seasons = IntStream.rangeClosed(2000, 2026)
                .mapToObj(String::valueOf).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        model.addAttribute("teamOptions", nbaService.getTeams());
        model.addAttribute("seasonOptions", seasons);

        // 2. 獲取球員選單 (根據球隊與賽季連動)
        List<String> playerOptions = new ArrayList<>();
        if (team != null && !team.isEmpty()) {
            playerOptions.addAll(nbaService.getPlayersByTeamAndSeason(team, season));
        } else {
            playerOptions.addAll(Arrays.asList("Michael Jordan", "Kobe Bryant", "LeBron James", "Stephen Curry", "Luka Doncic", "Giannis Antetokounmpo"));
        }

        // 若目前選中的球員不在該賽季名單內 (如退休)，強行加入選單以維持選取狀態
        if (name != null && !name.isEmpty() && !name.equals("none") && !playerOptions.contains(name)) {
            playerOptions.add(0, name);
        }
        model.addAttribute("playerOptions", playerOptions);

        // 3. 執行數據分析
        PlayerDTO report = null;
        if (name != null && !name.isEmpty() && !name.equals("none") && !name.contains("無資料")) {
            report = nbaService.getFullAnalytics(name, season);
            if (report != null) {
                // 自動同步球隊顯示
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
