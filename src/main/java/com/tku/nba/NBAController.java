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
            @RequestParam(required = false) String pinnedName, 
            Model model) {

        // 生成賽季清單
        List<String> seasons = IntStream.rangeClosed(2000, 2025)
                .mapToObj(String::valueOf).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        model.addAttribute("teamOptions", nbaService.getTeams());
        model.addAttribute("seasonOptions", seasons);

        String reportTarget = (name != null && !name.equals("none")) ? name : pinnedName;
        PlayerDTO report = null;
        if (reportTarget != null && !reportTarget.isEmpty()) {
            report = nbaService.getFullAnalytics(reportTarget, season, selectedTeam);
        }

        boolean hasActivePlayerSelected = (name != null && !name.equals("none"));

        //強制覆蓋球隊選單？
        if (report != null) {
            // 只有在以下情況，球隊選單才會跟著球員跳動：
            // a) 剛進網頁都沒選 (team == null)
            // b) 使用者明確點了球員名字 (actionTrigger == 'name')
            // c) 選單有選人，且使用者換了賽季 (這樣查舊球員歷史時，球隊才會跟著歷史跑)
            boolean shouldFollowPlayer = (team == null || team.isEmpty() || "name".equals(actionTrigger) || (hasActivePlayerSelected && "season".equals(actionTrigger)));
            
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

        // 動態生成該球隊的球員清單
        List<String> playerOptions = new ArrayList<>();
        if (team != null && !team.isEmpty()) {
            playerOptions.addAll(nbaService.getPlayersByTeamAndSeason(team, season));
        } else {
            playerOptions.addAll(Arrays.asList("LeBron James", "Kobe Bryant", "Stephen Curry", "Luka Doncic", "James Harden"));
        }

        // 如果使用者的動作是「換賽季」或「換球員」，代表他正在看這位球員，絕對不能讓他從選單消失！
        if (hasActivePlayerSelected && ("season".equals(actionTrigger) || "name".equals(actionTrigger))) {
            if (!playerOptions.contains(name)) {
                playerOptions.add(0, name); 
            }
        }

        // 判斷選單狀態
        String displaySelectedName = name;
        if (hasActivePlayerSelected && !playerOptions.contains(name)) {
            displaySelectedName = "none";
        }

        // 返回模型數據
        model.addAttribute("report", report); 
        model.addAttribute("filterTeam", team);         
        model.addAttribute("selectedTeam", selectedTeam); 
        model.addAttribute("selectedName", displaySelectedName); 
        model.addAttribute("selectedSeason", season);
        model.addAttribute("playerOptions", playerOptions);
        model.addAttribute("pinnedName", reportTarget); 

        return "report";
    }
}
