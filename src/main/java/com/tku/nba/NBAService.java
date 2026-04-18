package com.tku.nba;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NBAService {

    private final double HIGH_PTS = 25.0;
    private final double HIGH_AST = 8.0;
    private final double HIGH_REB = 10.0;

    public PlayerDTO getFullAnalytics(String name, String season) {
        PlayerDTO dto = new PlayerDTO();
        dto.setFullName(name);
        dto.setSeason(season);
        int year = Integer.parseInt(season);
        String status = "";

        // --- 核心數據庫：真實賽季與球隊精確映射 ---
        if (name.contains("Michael Jordan")) {
            if (year > 2003) { status = "【已退役】"; dto.setSeason("2003"); setStats(dto, "華盛頓巫師", 20.0, 6.1, 3.8, 75, 78, Arrays.asList(18.0, 22.0, 19.0, 21.0, 20.0)); }
            else if (year < 2001) { status = "【退休】"; dto.setSeason("1998"); setStats(dto, "芝加哥公牛", 28.7, 5.8, 3.5, 96, 95, Arrays.asList(30.0, 32.0, 28.7, 31.0, 28.7)); }
            else setStats(dto, "華盛頓巫師", 21.2, 5.9, 4.4, 78, 80, Arrays.asList(19.0, 22.0, 25.0, 20.0, 21.2));

        } else if (name.contains("Kobe Bryant")) {
            if (year > 2016) { status = "【已退役】"; dto.setSeason("2016"); setStats(dto, "洛杉磯湖人", 17.6, 3.7, 2.8, 65, 68, Arrays.asList(15.0, 20.0, 12.0, 25.0, 17.6)); }
            else setStats(dto, "洛杉磯湖人", (year == 2006 ? 35.4 : 25.3), 5.2, 4.7, 85, 90, Arrays.asList(22.0, 28.0, 35.0, 24.0, 25.3));

        } else if (name.contains("LeBron James")) {
            if (year < 2003) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0,0,0, Arrays.asList(0.0,0.0,0.0,0.0,0.0)); }
            else {
                String team = (year <= 2010) ? "克里夫蘭騎士" : (year <= 2014 ? "邁阿密熱火" : (year <= 2018 ? "克里夫蘭騎士" : "洛杉磯湖人"));
                setStats(dto, team, 27.2, 7.5, 7.3, 85, 98, Arrays.asList(25.0, 30.0, 22.0, 32.0, 27.2));
            }

        } else if (name.contains("Stephen Curry")) {
            if (year < 2009) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0,0,0, Arrays.asList(0.0,0.0,0.0,0.0,0.0)); }
            else setStats(dto, "金州勇士", 27.3, 4.8, 6.5, 70, 99, Arrays.asList(22.0, 35.0, 28.0, 31.0, 27.3));

        } else if (name.contains("Shaquille O'Neal")) {
            if (year > 2011) { status = "【已退役】"; dto.setSeason("2011"); setStats(dto, "波士頓塞爾提克", 9.2, 4.8, 0.7, 70, 75, Arrays.asList(9.2, 9.2, 9.2, 9.2, 9.2)); }
            else {
                String team = (year <= 2004) ? "洛杉磯湖人" : (year <= 2008 ? "邁阿密熱火" : (year <= 2009 ? "鳳凰城太陽" : "克里夫蘭騎士"));
                setStats(dto, team, 24.1, 11.0, 2.5, 95, 92, Arrays.asList(20.0, 26.0, 24.0, 28.0, 24.1));
            }

        } else if (name.contains("Kyrie Irving")) {
            String team = (year <= 2017) ? "克里夫蘭騎士" : (year <= 2019 ? "波士頓塞爾提克" : (year <= 2023 ? "布魯克林籃網" : "達拉斯獨行俠"));
            setStats(dto, team, 23.6, 3.9, 5.7, 60, 92, Arrays.asList(20.0, 25.0, 30.0, 22.0, 23.6));

        } else if (name.contains("Kevin Durant")) {
            String team = (year <= 2016) ? "奧克拉荷馬雷霆" : (year <= 2019 ? "金州勇士" : (year <= 2023 ? "布魯克林籃網" : "鳳凰城太陽"));
            setStats(dto, team, 27.3, 7.1, 4.4, 82, 97, Arrays.asList(26.0, 28.0, 31.0, 25.0, 27.3));

        } else if (name.contains("Dwight Howard")) {
            if (year <= 2012) setStats(dto, "奧蘭多魔術", 18.4, 13.0, 1.5, 99, 85, Arrays.asList(18.4, 18.4, 18.4, 18.4, 18.4));
            else if (year == 2013) setStats(dto, "洛杉磯湖人", 17.1, 12.4, 1.4, 90, 82, Arrays.asList(17.1, 17.1, 17.1, 17.1, 17.1));
            else if (year <= 2016) setStats(dto, "休士頓火箭", 16.0, 11.7, 1.4, 92, 80, Arrays.asList(16.0, 16.0, 16.0, 16.0, 16.0));
            else { status = "【已退役】"; dto.setSeason("2022"); setStats(dto, "洛杉磯湖人", 6.2, 5.9, 0.6, 80, 60, Arrays.asList(6.2, 6.2, 6.2, 6.2, 6.2)); }

        } else if (name.contains("Derrick Rose")) {
            String team = (year <= 2016) ? "芝加哥公牛" : (year == 2017 ? "紐約尼克" : "克里夫蘭騎士");
            setStats(dto, team, 17.4, 3.3, 5.2, 65, 80, Arrays.asList(15.0, 20.0, 25.0, 10.0, 17.4));

        } else {
            // 如果真的選到沒定義的人，直接顯示「無數據」
            setStats(dto, "無數據球隊", 0,0,0,0,0, Arrays.asList(0.0,0.0,0.0,0.0,0.0));
        }

        // --- Python 判定邏輯同步 ---
        if (status.contains("退役")) {
            dto.setCoreStyle("🐍 歷史傳奇存檔");
            dto.setSimpleRating("傳奇球員之歷史表現數據回溯。");
        } else if (dto.getPts() > 0) {
            analyzePythonic(dto);
        } else {
            dto.setCoreStyle("⏳ 非活躍賽季");
            dto.setSimpleRating("該賽季查無有效出賽紀錄。");
        }
        dto.setFullName(status + dto.getFullName());
        return dto;
    }

    private void analyzePythonic(PlayerDTO d) {
        if (d.getPts() >= HIGH_PTS && d.getAst() >= 6 && d.getReb() >= 6) {
            d.setCoreStyle("🌟 頂級全能巨星 (Elite All-Around Star)"); d.setSimpleRating("集得分、組織和籃板於一身的劃時代球員。");
        } else if (d.getPts() >= HIGH_PTS) {
            d.setCoreStyle("得分機器 (Volume Scorer)"); d.setSimpleRating("聯盟頂級的得分手，能夠在任何位置取分。");
        } else if (d.getAst() >= HIGH_AST && d.getPts() >= 15) {
            d.setCoreStyle("🎯 組織大師 (Playmaking Maestro)"); d.setSimpleRating("以傳球優先的組織核心。");
        } else if (d.getReb() >= HIGH_REB && d.getPts() < 15) {
            d.setCoreStyle("🧱 籃板/防守支柱"); d.setSimpleRating("內線防守和籃板的專家。");
        } else {
            d.setCoreStyle("角色球員 (Role Player)"); d.setSimpleRating("一名可靠的輪換球員。");
        }
    }

    private void setStats(PlayerDTO d, String team, double p, double r, double a, int def, int eff, List<Double> h) {
        d.setTeam(team); d.setPts(p); d.setReb(r); d.setAst(a); d.setDef(def); d.setEff(eff); d.setPtsHistory(h);
        d.setRadarData(Arrays.asList((int)(p*3), (int)(r*8), (int)(a*10), def, eff));
    }

    public List<String> getTeams() {
        return Arrays.asList("洛杉磯湖人", "金州勇士", "芝加哥公牛", "達拉斯獨行俠", "鳳凰城太陽", "密爾瓦基公鹿", "波士頓塞爾提克", "休士頓火箭", "布魯克林籃網", "華盛頓巫師", "邁阿密熱火", "克里夫蘭騎士");
    }

    public List<String> getPlayersByTeamAndSeason(String team, String season) {
        int year = Integer.parseInt(season);
        List<String> p = new ArrayList<>();
        switch (team) {
            case "洛杉磯湖人":
                if (year <= 2004) { p.add("Shaquille O'Neal"); p.add("Kobe Bryant"); }
                else if (year <= 2016) { p.add("Kobe Bryant"); p.add("Pau Gasol"); }
                else { p.add("LeBron James"); p.add("Anthony Davis"); }
                break;
            case "金州勇士":
                p.add("Stephen Curry"); 
                if (year >= 2016 && year <= 2019) p.add("Kevin Durant"); else p.add("Klay Thompson");
                break;
            case "芝加哥公牛":
                if (year <= 1998) { p.add("Michael Jordan"); p.add("Scottie Pippen"); }
                else { p.add("Derrick Rose"); p.add("Joakim Noah"); }
                break;
            case "邁阿密熱火":
                if (year >= 2010 && year <= 2014) { p.add("LeBron James"); p.add("Dwyane Wade"); }
                else { p.add("Dwyane Wade"); p.add("Shaquille O'Neal"); }
                break;
            case "克里夫蘭騎士":
                if (year >= 2014 && year <= 2017) { p.add("LeBron James"); p.add("Kyrie Irving"); }
                else { p.add("LeBron James"); p.add("Shaquille O'Neal"); }
                break;
            case "鳳凰城太陽":
                if (year <= 2012) { p.add("Steve Nash"); p.add("Shaquille O'Neal"); }
                else { p.add("Devin Booker"); p.add("Kevin Durant"); }
                break;
            case "華盛頓巫師":
                if (year >= 2001 && year <= 2003) { p.add("Michael Jordan"); p.add("Jerry Stackhouse"); }
                else { p.add("Bradley Beal"); p.add("John Wall"); }
                break;
            case "休士頓火箭":
                if (year <= 2011) { p.add("Yao Ming"); p.add("Tracy McGrady"); }
                else { p.add("James Harden"); p.add("Dwight Howard"); }
                break;
            case "達拉斯獨行俠":
                if (year <= 2018) { p.add("Dirk Nowitzki"); p.add("Jason Kidd"); }
                else { p.add("Luka Doncic"); p.add("Kyrie Irving"); }
                break;
            case "波士頓塞爾提克":
                if (year <= 2013) { p.add("Paul Pierce"); p.add("Kevin Garnett"); }
                else { p.add("Jayson Tatum"); p.add("Jaylen Brown"); }
                break;
            case "布魯克林籃網":
                if (year >= 2019 && year <= 2023) { p.add("Kevin Durant"); p.add("Kyrie Irving"); }
                else { p.add("Jason Kidd"); p.add("Vince Carter"); }
                break;
            case "密爾瓦基公鹿":
                p.add("Giannis Antetokounmpo"); p.add("Khris Middleton");
                break;
        }
        return p;
    }
}
