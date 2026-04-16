package com.tku.nba;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NBAService {

    /**
     * 此為自建真實歷史模擬數據庫 (2000-2026)。
     * 包含各球隊歷史各階段至少兩名核心球員，並實作退役回溯與入盟判定邏輯。
     */
    public PlayerDTO getFullAnalytics(String name, String season) {
        PlayerDTO dto = new PlayerDTO();
        dto.setFullName(name);
        dto.setSeason(season);
        int selectedYear = Integer.parseInt(season);
        String status = "";

        // --- 核心數據邏輯鏈 ---
        if (name.contains("Michael Jordan")) {
            if (selectedYear > 2003) { status = "【已退役】"; dto.setSeason("2003"); setStats(dto, "華盛頓巫師 (傳奇存檔)", 20.0, 6.1, 3.8, 75, 78, Arrays.asList(18.0, 22.0, 20.0, 20.0)); }
            else if (selectedYear < 2001) { status = "【退休/未復出】"; dto.setSeason("1998"); setStats(dto, "芝加哥公牛 (歷史數據)", 28.7, 5.8, 3.5, 96, 95, Arrays.asList(30.0, 32.0, 28.7)); }
            else setStats(dto, "華盛頓巫師", 21.2, 5.9, 4.4, 78, 80, Arrays.asList(19.0, 21.2));

        } else if (name.contains("Kobe Bryant")) {
            if (selectedYear > 2016) { status = "【已退役】"; dto.setSeason("2016"); setStats(dto, "洛杉磯湖人 (傳奇存檔)", 17.6, 3.7, 2.8, 65, 68, Arrays.asList(15.0, 17.6)); }
            else setStats(dto, "洛杉磯湖人", (selectedYear == 2006 ? 35.4 : 25.3), 5.2, 4.7, 88, 92, Arrays.asList(22.0, 30.0, 25.3));

        } else if (name.contains("LeBron James")) {
            if (selectedYear < 2003) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0,0,0, Arrays.asList(0.0)); }
            else {
                String team = (selectedYear <= 2010) ? "克里夫蘭騎士" : (selectedYear <= 2014 ? "邁阿密熱火" : (selectedYear <= 2018 ? "克里夫蘭騎士" : "洛杉磯湖人"));
                setStats(dto, team, 27.2, 7.5, 7.3, 85, 98, Arrays.asList(25.0, 30.0, 27.2));
            }

        } else if (name.contains("Stephen Curry")) {
            if (selectedYear < 2009) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0,0,0, Arrays.asList(0.0)); }
            else setStats(dto, "金州勇士", 27.3, 4.8, 6.5, 72, 99, Arrays.asList(22.0, 35.0, 27.3));

        } else if (name.contains("Shaquille O'Neal")) {
            if (selectedYear > 2011) { status = "【已退役】"; dto.setSeason("2011"); setStats(dto, "波士頓塞爾提克", 9.2, 4.8, 0.7, 70, 75, Arrays.asList(9.2)); }
            else {
                String team = (selectedYear <= 2004) ? "洛杉磯湖人" : (selectedYear <= 2008 ? "邁阿密熱火" : "鳳凰城太陽");
                setStats(dto, team, 24.0, 11.0, 2.5, 95, 92, Arrays.asList(22.0, 26.0, 24.0));
            }

        } else if (name.contains("Dwyane Wade")) {
            if (selectedYear > 2019) { status = "【已退役】"; dto.setSeason("2019"); setStats(dto, "邁阿密熱火", 15.0, 4.0, 4.2, 80, 85, Arrays.asList(15.0)); }
            else setStats(dto, "邁阿密熱火", 22.0, 4.7, 5.4, 88, 90, Arrays.asList(20.0, 22.0));

        } else if (name.contains("Dirk Nowitzki")) {
            if (selectedYear > 2019) { status = "【已退役】"; dto.setSeason("2019"); setStats(dto, "達拉斯獨行俠", 7.3, 3.1, 0.7, 60, 70, Arrays.asList(7.3)); }
            else setStats(dto, "達拉斯獨行俠", 20.7, 7.5, 2.4, 75, 94, Arrays.asList(18.0, 20.7));

        } else if (name.contains("Kevin Durant")) {
            if (selectedYear < 2007) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0,0,0, Arrays.asList(0.0)); }
            else {
                String team = (selectedYear <= 2016) ? "奧克拉荷馬雷霆" : (selectedYear <= 2019 ? "金州勇士" : (selectedYear <= 2023 ? "布魯克林籃網" : "鳳凰城太陽"));
                setStats(dto, team, 27.3, 7.1, 4.4, 82, 97, Arrays.asList(25.0, 27.3));
            }

        } else if (name.contains("Giannis")) {
            if (selectedYear < 2013) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0,0,0, Arrays.asList(0.0)); }
            else setStats(dto, "密爾瓦基公鹿", 30.4, 11.5, 6.5, 96, 98, Arrays.asList(28.0, 30.4));

        } else if (name.contains("Jayson Tatum")) {
            if (selectedYear < 2017) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0,0,0, Arrays.asList(0.0)); }
            else setStats(dto, "波士頓塞爾提克", 26.9, 8.1, 4.9, 85, 90, Arrays.asList(22.0, 26.9));

        } else if (name.contains("James Harden")) {
            if (selectedYear < 2009) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0,0,0, Arrays.asList(0.0)); }
            else {
                String team = (selectedYear <= 2012) ? "雷霆" : (selectedYear <= 2021 ? "休士頓火箭" : "快艇");
                setStats(dto, team, 24.5, 5.6, 7.0, 70, 95, Arrays.asList(20.0, 24.5));
            }
        } else if (name.contains("Anthony Davis")) {
            if (selectedYear < 2012) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0,0,0, Arrays.asList(0.0)); }
            else {
                String team = (selectedYear <= 2019) ? "紐奧良鵜鶘" : "洛杉磯湖人";
                setStats(dto, team, 24.1, 10.6, 2.5, 96, 94, Arrays.asList(20.0, 24.1));
            }
        } else if (name.contains("Chris Paul")) {
            if (selectedYear < 2005) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0,0,0, Arrays.asList(0.0)); }
            else {
                String team = (selectedYear <= 2011) ? "黃蜂" : (selectedYear <= 2017 ? "快艇" : (selectedYear <= 2023 ? "太陽" : "馬刺"));
                setStats(dto, team, 17.5, 4.5, 9.4, 92, 95, Arrays.asList(15.0, 17.5));
            }
        } else {
            // 通用保底數據
            setStats(dto, "NBA 聯盟", 15.0, 4.0, 4.0, 70, 70, Arrays.asList(15.0));
        }

        // 分析與標記
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
        if (pts >= 28) return "👑 核心得分機器";
        if (pts >= 22 && ast >= 7) return "🌟 全能組織核心";
        return "🛠️ 優質核心球員";
    }

    public List<String> getTeams() {
        return Arrays.asList("洛杉磯湖人", "金州勇士", "芝加哥公牛", "達拉斯獨行俠", "鳳凰城太陽", "密爾瓦基公鹿", "奧蘭多魔術", "休士頓火箭", "波士頓塞爾提克", "邁阿密熱火", "華盛頓巫師", "布魯克林籃網");
    }


    public List<String> getPlayersByTeamAndSeason(String team, String season) {
        int year = Integer.parseInt(season);
        List<String> players = new ArrayList<>();

        switch (team) {
            case "洛杉磯湖人":
                if (year <= 2004) { players.add("Shaquille O'Neal"); players.add("Kobe Bryant"); }
                else if (year <= 2016) { players.add("Kobe Bryant"); players.add("Pau Gasol"); }
                else { players.add("LeBron James"); players.add("Anthony Davis"); }
                break;
            case "金州勇士":
                players.add("Stephen Curry");
                if (year >= 2016 && year <= 2019) players.add("Kevin Durant");
                else players.add("Klay Thompson");
                break;
            case "邁阿密熱火":
                if (year >= 2010 && year <= 2014) { players.add("LeBron James"); players.add("Dwyane Wade"); }
                else { players.add("Dwyane Wade"); players.add("Jimmy Butler"); }
                break;
            case "波士頓塞爾提克":
                if (year <= 2013) { players.add("Paul Pierce"); players.add("Kevin Garnett"); }
                else { players.add("Jayson Tatum"); players.add("Jaylen Brown"); }
                break;
            case "芝加哥公牛":
                if (year <= 1998) { players.add("Michael Jordan"); players.add("Scottie Pippen"); }
                else { players.add("Derrick Rose"); players.add("Jimmy Butler"); }
                break;
            case "達拉斯獨行俠":
                if (year <= 2019) { players.add("Dirk Nowitzki"); players.add("Jason Kidd"); }
                else { players.add("Luka Doncic"); players.add("Kyrie Irving"); }
                break;
            case "鳳凰城太陽":
                if (year <= 2012) { players.add("Steve Nash"); players.add("Shaquille O'Neal"); }
                else { players.add("Devin Booker"); players.add("Kevin Durant"); }
                break;
            case "休士頓火箭":
                if (year <= 2011) { players.add("Yao Ming"); players.add("Tracy McGrady"); }
                else { players.add("James Harden"); players.add("Dillon Brooks"); }
                break;
            case "密爾瓦基公鹿":
                players.add("Giannis Antetokounmpo");
                if (year >= 2023) players.add("Damian Lillard");
                else players.add("Khris Middleton");
                break;
            case "奧蘭多魔術":
                if (year <= 2012) { players.add("Dwight Howard"); players.add("Tracy McGrady"); }
                else { players.add("Paolo Banchero"); players.add("Franz Wagner"); }
                break;
            case "華盛頓巫師":
                if (year >= 2001 && year <= 2003) { players.add("Michael Jordan"); players.add("Jerry Stackhouse"); }
                else { players.add("Kyle Kuzma"); players.add("Bradley Beal"); }
                break;
            case "布魯克林籃網":
                if (year >= 2019 && year <= 2023) { players.add("Kevin Durant"); players.add("Kyrie Irving"); }
                else { players.add("Vince Carter"); players.add("Jason Kidd"); }
                break;
        }
        return players.isEmpty() ? Arrays.asList("目前該賽季無資料") : players;
    }
}
