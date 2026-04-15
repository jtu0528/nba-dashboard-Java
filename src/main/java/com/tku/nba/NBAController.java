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
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String team,
            @RequestParam(required = false, defaultValue = "2025") String season, 
            Model model) {

        // 1. 生成 2000-2026 賽季選單
        List<String> seasons = IntStream.rangeClosed(2000, 2026)
                .mapToObj(String::valueOf)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        // 2. 獲取所有球隊
        List<String> teams = nbaService.getAllTeams();

        // 3. 處理連動邏輯
        List<String> playerOptions = new ArrayList<>(Arrays.asList("Stephen Curry", "LeBron James", "Luka Doncic", "Kevin Durant", "Nikola Jokic", "Joel Embiid", "Kyrie Irving"));
        
        // 如果選了球隊，可以在這裡加入過濾球員的邏輯 (這部分在真實 API 需額外呼叫，此處先以連動展示為主)
        if (team != null && !team.isEmpty() && (name == null || name.isEmpty())) {
            // 範例：選了 Lakers 就只留 LeBron
            if (team.equals("Lakers")) playerOptions = Arrays.asList("LeBron James", "Anthony Davis");
            if (team.equals("Warriors")) playerOptions = Arrays.asList("Stephen Curry", "Klay Thompson");
        }

        // 4. 抓取數據 (只有當選了球員名字時才抓)
        PlayerDTO report = null;
        if (name != null && !name.isEmpty()) {
            report = nbaService.getRealPlayerData(name, season);
            // 選了球員後，強迫球隊選單同步
            if (report != null) team = report.getTeam().split(" ")[report.getTeam().split(" ").length - 1];
        }

        model.addAttribute("seasons", seasons);
        model.addAttribute("teams", teams);
        model.addAttribute("playerOptions", playerOptions);
        model.addAttribute("report", report);
        model.addAttribute("selectedName", name);
        model.addAttribute("selectedTeam", team);
        model.addAttribute("selectedSeason", season);

        return "report";
    }
}
