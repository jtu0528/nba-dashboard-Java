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

        List<String> seasons = IntStream.rangeClosed(2000, 2026)
                .mapToObj(String::valueOf).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        
        model.addAttribute("teamOptions", nbaService.getTeams());
        model.addAttribute("seasonOptions", seasons);

        // 根據賽季與球隊，動態決定球員選單內容
        List<String> playerOptions;
        if (team != null && !team.isEmpty()) {
            playerOptions = nbaService.getPlayersByTeamAndSeason(team, season);
        } else {
            playerOptions = Arrays.asList("Michael Jordan", "Kobe Bryant", "LeBron James", "Stephen Curry", "Luka Doncic", "Giannis Antetokounmpo");
        }
        model.addAttribute("playerOptions", playerOptions);

        PlayerDTO report = null;
        if (name != null && !name.isEmpty() && !name.equals("none") && !name.contains("無資料")) {
            report = nbaService.getFullAnalytics(name, season);
            // 如果切換年份導致球員換隊，自動同步球隊選單
            if (report != null) {
                team = report.getTeam().contains("(") ? report.getTeam().split(" ")[0] : report.getTeam();
            }
        }

        model.addAttribute("report", report);
        model.addAttribute("selectedTeam", team);
        model.addAttribute("selectedName", name);
        model.addAttribute("selectedSeason", season);

        return "report";
    }
}
