package com.tku.nba;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NBAService {

    /**
     * 此為自建數據庫，包含球員活躍狀態判定。
     */
    public PlayerDTO getFullAnalytics(String name, String season) {
        PlayerDTO dto = new PlayerDTO();
        dto.setFullName(name);
        // 預設為選取賽季，若退休則會在下方邏輯覆蓋
        dto.setSeason(season);
        
        int selectedYear = Integer.parseInt(season);
        String status = "";

        // --- 核心模擬數據引擎：精確球員歷史邏輯與賽季回溯 ---

        if (name.contains("Michael Jordan")) {
            int lastActive = 2003;
            if (selectedYear > lastActive) {
                status = "【已退役】";
                dto.setSeason("2003"); // 強制顯示最後賽季
                setStats(dto, "華盛頓巫師 (傳奇存檔)", 20.0, 6.1, 3.8, 75, 78, Arrays.asList(18.5, 21.0, 19.0, 22.0, 20.0));
            } else if (selectedYear < 2001) {
                status = "【退休/未復出】";
                dto.setSeason("1998"); // 強制顯示公牛最後賽季
                setStats(dto, "芝加哥公牛 (歷史數據)", 28.7, 5.8, 3.5, 96, 92, Arrays.asList(32.0, 28.0, 45.0, 38.0, 30.1));
            } else {
                setStats(dto, "華盛頓巫師", 21.2, 5.9, 4.4, 78, 80, Arrays.asList(19.0, 22.0, 25.0, 20.0, 21.2));
            }

        } else if (name.contains("Kobe Bryant")) {
            int lastActive = 2016;
            if (selectedYear > lastActive) {
                status = "【已退役】";
                dto.setSeason("2016"); // 強制顯示最後賽季
                setStats(dto, "洛杉磯湖人 (傳奇存檔)", 17.6, 3.7, 2.8, 65, 68, Arrays.asList(15.0, 12.0, 18.0, 25.0, 17.6));
            } else if (selectedYear == 2006) {
                setStats(dto, "洛杉磯湖人", 35.4, 5.3, 4.5, 92, 98, Arrays.asList(40.2, 45.0, 50.0, 30.0, 35.4));
            } else {
                setStats(dto, "洛杉磯湖人", 25.3, 5.2, 4.7, 85, 88, Arrays.asList(22.0, 28.0, 24.0, 26.0, 25.3));
            }

        } else if (name.contains("Dwight Howard")) {
            int lastActive = 2022;
            if (selectedYear > lastActive) {
                status = "【已退役】";
                dto.setSeason("2022"); // 強制顯示最後賽季
                setStats(dto, "洛杉磯湖人 (傳奇存檔)", 6.2, 5.9, 0.6, 82, 60, Arrays.asList(6.2, 5.0, 8.0, 6.2));
            } else if (selectedYear <= 2012) {
                setStats(dto, "奧蘭多魔術", 18.4, 13.0, 1.5, 99, 85, Arrays.asList(18.0, 22.0, 18.4));
            } else {
                setStats(dto, "NBA 團隊", 13.0, 10.0, 1.2, 88, 75, Arrays.asList(13.0));
            }

        } else if (name.contains("LeBron James")) {
            // LeBron 尚在活躍，直接使用選取年份
            String team = (selectedYear <= 2010) ? "克里夫蘭騎士" : (selectedYear <= 2014 ? "邁阿密熱火" : (selectedYear <= 2018 ? "克里夫蘭騎士" : "洛杉磯湖人"));
            setStats(dto, team, 27.2, 7.5, 7.3, 85, 98, Arrays.asList(25.0, 30.0, 22.0, 28.0, 27.2));

        } else if (name.contains("Stephen Curry")) {
            if (selectedYear < 2009) {
                status = "【尚未入盟】";
                setStats(dto, "N/A", 0,0,0,0,0, Arrays.asList(0.0));
            } else {
                setStats(dto, "金州勇士", 27.3, 4.8, 6.5, 72, 99, Arrays.asList(22.0, 35.0, 28.0, 31.0, 27.3));
            }
        } else if (name.contains("Luka Doncic")) {
            if (selectedYear < 2018) {
                status = "【尚未入盟】";
                setStats(dto, "N/A", 0,0,0,0,0, Arrays.asList(0.0));
            } else {
                setStats(dto, "達拉斯獨行俠", 33.9, 9.2, 9.8, 75, 97, Arrays.asList(30.0, 40.0, 33.9));
            }
        } else {
            setStats(dto, "NBA 聯盟", 15.0, 4.0, 4.0, 70, 70, Arrays.asList(15.0));
        }

        // 統計標註
        if (status.contains("退役")) dto.setCoreStyle("🐍 歷史傳奇存檔");
        else if (dto.getPts() == 0) dto.setCoreStyle("⏳ 非活躍賽季");
        else dto.setCoreStyle(analyzeStyle(dto.getPts(), dto.getAst(), dto.getReb(), dto.getDef(), dto.getEff()));

        dto.setFullName(status + dto.getFullName());
        return dto;
    }

    private void setStats(PlayerDTO d, String team, double p, double r, double a, int def, int eff, List<Double> h) {
        d.setTeam(team); d.setPts(p); d.setReb(r); d.setAst(a); d.setDef(def); d.setEff(eff); d.setPtsHistory(h);
        int sScore = (int) Math.min(100, p * 3.0);
        int rScore = (int) Math.min(100, r * 8.0);
        int aScore = (int) Math.min(100, a * 10.0);
        d.setRadarData(Arrays.asList(sScore, rScore, aScore, def, eff));
    }

    private String analyzeStyle(double pts, double ast, double reb, double def, double eff) {
        if (def >= 95) return "🛡️ 頂級防守大閘";
        if (eff >= 98) return "⚡ 效率之神";
        if (pts >= 30) return "👑 歷史級得分機器";
        if (pts >= 25 && ast >= 7) return "🌟 全能超級巨星";
        return "🛠️ 優質核心球員";
    }

    public List<String> getTeams() {
        return Arrays.asList("洛杉磯湖人", "金州勇士", "芝加哥公牛", "達拉斯獨行俠", "鳳凰城太陽", "密爾瓦基公鹿", "奧蘭多魔術", "休士頓火箭", "華盛頓巫師", "克里夫蘭騎士", "邁阿密熱火");
    }

    public List<String> getPlayersByTeamAndSeason(String team, String season) {
        int year = Integer.parseInt(season);
        List<String> players = new ArrayList<>();
        if ("洛杉磯湖人".equals(team)) {
            if (year <= 2016) players.add("Kobe Bryant");
            if (year >= 2018) players.add("LeBron James");
            if (year >= 2017 && year <= 2021) players.add("Kyle Kuzma");
            if (year == 2013 || year == 2020 || year == 2022) players.add("Dwight Howard");
        } else if ("芝加哥公牛".equals(team)) {
            if (year <= 1998) players.add("Michael Jordan");
        } else if ("達拉斯獨行俠".equals(team)) {
            if (year >= 2018) players.add("Luka Doncic");
            if (year >= 2023) players.add("Kyrie Irving");
        } else if ("金州勇士".equals(team)) {
            if (year >= 2009) players.add("Stephen Curry");
        } else if ("奧蘭多魔術".equals(team)) {
            if (year >= 2004 && year <= 2012) players.add("Dwight Howard");
        } else if ("華盛頓巫師".equals(team)) {
            if (year >= 2001 && year <= 2003) players.add("Michael Jordan");
        }
        return players.isEmpty() ? Arrays.asList("目前該賽季無資料") : players;
    }
}
