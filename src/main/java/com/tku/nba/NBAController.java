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

        // 此為動態生成 2000-2026 賽季選單
        List<String> seasons = IntStream.rangeClosed(2000, 2026)
                .mapToObj(String::valueOf).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        
        model.addAttribute("teamOptions", nbaService.getTeams());
        model.addAttribute("seasonOptions", seasons);

        // 實作連動邏輯：優先過濾球員名單
        List<String> playerOptions = (team != null && !team.isEmpty()) ? 
                                     nbaService.getPlayersByTeam(team) : 
                                     nbaService.getPlayersByTeam("all");
        model.addAttribute("playerOptions", playerOptions);

        // 執行資料調閱邏輯
        PlayerDTO report = null;
        if (name != null && !name.isEmpty() && !name.equals("none")) {
            report = nbaService.getFullAnalytics(name, season);
            // 實作反向連動：確保球隊選單同步該球員所屬隊伍
            if (report != null && (team == null || team.isEmpty())) {
                team = report.getTeam();
            }
        }

        model.addAttribute("report", report);
        model.addAttribute("selectedTeam", team);
        model.addAttribute("selectedName", name);
        model.addAttribute("selectedSeason", season);

        return "report";
    }
}
