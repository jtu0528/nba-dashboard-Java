package com.tku.nba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class NBAController {

    @Autowired
    private NBAService nbaService;

    @GetMapping("/")
    public String showDashboard(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String team,
            @RequestParam(required = false, defaultValue = "2025") String season,
            Model model) {

        // 1. 生成 2000-2026 賽季清單
        List<String> seasonOptions = IntStream.rangeClosed(2000, 2026)
                .mapToObj(String::valueOf).sorted((a,b)->b.compareTo(a)).collect(Collectors.toList());

        // 2. 獲取所有球隊與球員
        List<String> teamOptions = nbaService.getAllTeams();
        List<String> playerOptions;

        // 核心邏輯 A：如果選了球隊，球員選單要過濾
        if (team != null && !team.isEmpty() && (name == null || name.isEmpty())) {
            playerOptions = nbaService.getPlayersByTeam(team);
        } else {
            playerOptions = nbaService.getAllPlayers();
        }

        // 核心邏輯 B：如果選了球員，自動匹配該球員最新的球隊
        if (name != null && !name.isEmpty()) {
            team = nbaService.getTeamByPlayer(name, season);
        }

        // 3. 獲取數據 (若沒選人則回傳空物件)
        PlayerDTO report = (name != null && !name.isEmpty()) ? 
                          nbaService.getPlayerData(name, season, team) : null;

        model.addAttribute("report", report);
        model.addAttribute("playerOptions", playerOptions);
        model.addAttribute("teamOptions", teamOptions);
        model.addAttribute("seasonOptions", seasonOptions);
        model.addAttribute("selectedName", name);
        model.addAttribute("selectedTeam", team);
        model.addAttribute("selectedSeason", season);

        return "report";
    }
}
