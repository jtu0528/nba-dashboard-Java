package com.tku.nba;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NBAService {

    /**
     * 此為自建數據庫，包含球員活躍狀態判定、全中文球隊支援。
     */
    public PlayerDTO getFullAnalytics(String name, String season) {
        PlayerDTO dto = new PlayerDTO();
        dto.setFullName(name);
        dto.setSeason(season);
        int year = Integer.parseInt(season);
        String status = "";

        // --- 核心模擬數據引擎：精確球員歷史邏輯 ---
        if (name.contains("Michael Jordan")) {
            if (year > 2003) {
                status = "【已退役】";
                setStats(dto, "華盛頓巫師 (傳奇存檔)", 20.0, 6.1, 3.8, 75, 78, Arrays.asList(18.5, 21.0, 19.0, 22.0, 20.0));
            } else if (year < 2001) {
                status = "【退休/未復出】";
                setStats(dto, "芝加哥公牛 (歷史數據)", 28.7, 5.8, 3.5, 96, 92, Arrays.asList(30.0, 35.0, 25.0, 31.0, 28.7));
            } else {
                setStats(dto, "華盛頓巫師", 21.2, 5.9, 4.4, 78, 80, Arrays.asList(19.0, 22.0, 25.0, 20.0, 21.2));
            }
        } else if (name.contains("Kobe Bryant")) {
            if (year > 2016) {
                status = "【已退役】";
                setStats(dto, "洛杉磯湖人 (傳奇存檔)", 17.6, 3.7, 2.8, 65, 68, Arrays.asList(15.0, 12.0, 18.0, 25.0, 17.6));
            } else {
                if (year == 2006) setStats(dto, "洛杉磯湖人", 35.4, 5.3, 4.5, 92, 98, Arrays.asList(40.2, 35.0, 45.0, 31.0, 35.4));
                else setStats(dto, "洛杉磯湖人", 25.3, 5.2, 4.7, 88, 90, Arrays.asList(22.0, 28.0, 24.0, 26.0, 25.3));
            }
        } else if (name.contains("LeBron James")) {
            String team = (year <= 2010) ? "克里夫蘭騎士" : (year <= 2014 ? "邁阿密熱火" : (year <= 2018 ? "克里夫蘭騎士" : "洛杉磯湖人"));
            int defScore = (year <= 2014) ? 94 : 82;
            setStats(dto, team, 27.2, 7.5, 7.3, defScore, 98, Arrays.asList(25.0, 30.0, 22.0, 28.0, 27.2));
        } else if (name.contains("Kyrie Irving")) {
            String team = (year <= 2017) ? "克里夫蘭騎士" : (year <= 2019 ? "波士頓塞爾提克" : (year <= 2023 ? "布魯克林籃網" : "達拉斯獨行俠"));
            setStats(dto, team, 23.5, 3.9, 5.7, 60, 92, Arrays.asList(20.0, 25.0, 22.0, 23.6));
        } else if (name.contains("Devin Booker")) {
            if (year < 2015) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0, 0,0, Arrays.asList(0.0)); }
            else setStats(dto, "鳳凰城太陽", 27.1, 4.5, 6.9, 70, 89, Arrays.asList(25.0, 32.0, 28.0, 27.1));
        } else if (name.contains("Dillon Brooks")) {
            String team = (year <= 2023) ? "曼菲斯灰熊" : "休士頓火箭";
            setStats(dto, team, 12.7, 3.4, 1.7, 95, 62, Arrays.asList(10.0, 14.0, 12.7));
        } else if (name.contains("Giannis Antetokounmpo")) {
            if (year < 2013) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0, 0,0, Arrays.asList(0.0)); }
            else setStats(dto, "密爾瓦基公鹿", 30.4, 11.5, 6.5, 98, 96, Arrays.asList(28.0, 32.0, 35.0, 30.4));
        } else if (name.contains("Kyle Kuzma")) {
            String team = (year <= 2021) ? "洛杉磯湖人" : "華盛頓巫師";
            setStats(dto, team, 22.2, 6.6, 4.2, 72, 78, Arrays.asList(18.0, 26.0, 22.2));
        } else if (name.contains("Dwight Howard")) {
            String team = (year <= 2012) ? "奧蘭多魔術" : (year == 2013 ? "洛杉磯湖人" : (year <= 2016 ? "休士頓火箭" : "洛杉磯湖人"));
            setStats(dto, team, 16.0, 11.7, 1.4, 99, 85, Arrays.asList(16.0, 22.0, 16.0));
        } else if (name.contains("Stephen Curry")) {
            if (year < 2009) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0, 0,0, Arrays.asList(0.0)); }
            else setStats(dto, "金州勇士", 27.3, 4.8, 6.5, 70, 99, Arrays.asList(22.0, 35.0, 28.0, 31.0, 27.3));
        } else if (name.contains("Luka Doncic")) {
            if (year < 2018) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0, 0,0, Arrays.asList(0.0)); }
            else setStats(dto, "達拉斯獨行俠", 33.9, 9.2, 9.8, 75, 97, Arrays.asList(30.0, 40.0, 33.9));
        } else {
            setStats(dto, "NBA 聯盟", 15.0, 4.0, 4.0, 70, 70, Arrays.asList(15.0));
        }

        // 統計標註
        if (status.contains("退役")) dto.setCoreStyle("🐍 歷史傳奇存檔");
        else if (status.contains("尚未") || dto.getPts() == 0) dto.setCoreStyle("⏳ 非活躍賽季");
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
        return Arrays.asList("洛杉磯湖人", "金州勇士", "芝加哥公牛", "達拉斯獨行俠", "鳳凰城太陽", "密爾瓦基公鹿", "奧蘭多魔術", "休士頓火箭", "華盛頓巫師", "克里夫蘭騎士", "邁阿密熱火", "波士頓塞爾提克", "布魯克林籃網", "曼菲斯灰熊");
    }


    public List<String> getPlayersByTeamAndSeason(String team, String season) {
        int year = Integer.parseInt(season);
        List<String> players = new ArrayList<>();

        if ("洛杉磯湖人".equals(team)) {
            if (year <= 2016) players.add("Kobe Bryant");
            if (year >= 2018) players.add("LeBron James");
            if (year >= 2017 && year <= 2021) players.add("Kyle Kuzma");
            if (year == 2013 || year == 2020 || year == 2022) players.add("Dwight Howard");
        } else if ("克里夫蘭騎士".equals(team)) {
            if (year <= 2010 || (year >= 2014 && year <= 2018)) players.add("LeBron James");
            if (year >= 2011 && year <= 2017) players.add("Kyrie Irving");
        } else if ("邁阿密熱火".equals(team)) {
            if (year >= 2011 && year <= 2014) players.add("LeBron James");
        } else if ("芝加哥公牛".equals(team)) {
            if (year <= 1998) players.add("Michael Jordan");
        } else if ("華盛頓巫師".equals(team)) {
            if (year >= 2001 && year <= 2003) players.add("Michael Jordan");
            if (year >= 2021) players.add("Kyle Kuzma");
        } else if ("達拉斯獨行俠".equals(team)) {
            if (year >= 2018) players.add("Luka Doncic");
            if (year >= 2023) players.add("Kyrie Irving");
        } else if ("金州勇士".equals(team)) {
            if (year >= 2009) players.add("Stephen Curry");
        } else if ("鳳凰城太陽".equals(team)) {
            if (year >= 2015) players.add("Devin Booker");
        } else if ("密爾瓦基公鹿".equals(team)) {
            if (year >= 2013) players.add("Giannis Antetokounmpo");
        } else if ("奧蘭多魔術".equals(team)) {
            if (year >= 2004 && year <= 2012) players.add("Dwight Howard");
        } else if ("休士頓火箭".equals(team)) {
            if (year >= 2013 && year <= 2016) players.add("Dwight Howard");
            if (year >= 2023) players.add("Dillon Brooks");
        } else if ("曼菲斯灰熊".equals(team)) {
            if (year >= 2017 && year <= 2023) players.add("Dillon Brooks");
        }

        return players.isEmpty() ? Arrays.asList("目前該賽季無資料") : players;
    }
}
