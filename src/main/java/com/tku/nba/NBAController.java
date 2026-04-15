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
    public String dashboard(
            @RequestParam(required = false) String team,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "2023") String season, 
            Model model) {

        // 1. 真實球隊名單
        Map<Integer, String> teamMap = nbaService.getTeamMap();
        model.addAttribute("teamOptions", teamMap.values());

        // 2. 2000-2026 賽季
        List<String> seasons = IntStream.rangeClosed(2000, 2026)
                .mapToObj(String::valueOf)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        model.addAttribute("seasonOptions", seasons);

        // 3. 連動邏輯：選了球隊才出球員清單，沒選球隊就顯示幾個熱門球員作為啟動
        List<String> playerOptions;
        if (team != null && !team.isEmpty()) {
            playerOptions = nbaService.getPlayersByTeam(team);
        } else {
            playerOptions = Arrays.asList("Stephen Curry", "LeBron James", "Luka Doncic", "Kevin Durant", "Nikola Jokic");
        }
        model.addAttribute("playerOptions", playerOptions);

        // 4. 只有當點選了具體球員名，且不是提示文字時，才去 API 抓資料
        PlayerDTO report = null;
        if (name != null && !name.isEmpty() && !name.contains("--")) {
            report = nbaService.getFullAnalytics(name, season);
            if (report != null) team = report.getTeam(); // 反向連動球隊
        }

        model.addAttribute("report", report);
        model.addAttribute("selectedTeam", team);
        model.addAttribute("selectedName", name);
        model.addAttribute("selectedSeason", season);

        return "report";
    }
}
