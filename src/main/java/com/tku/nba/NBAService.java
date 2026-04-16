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

        // --- 真實數據與歷史路徑模擬 ---
        if (name.contains("Michael Jordan")) {
            if (year > 2003) { status = "【已退役】"; dto.setSeason("2003"); setStats(dto, "華盛頓巫師", 20.0, 6.1, 3.8, 75, 78, Arrays.asList(18.0, 22.0, 19.0, 21.0, 20.0)); }
            else if (year < 2001) { status = "【退休】"; dto.setSeason("1998"); setStats(dto, "芝加哥公牛", 28.7, 5.8, 3.5, 96, 95, Arrays.asList(30.0, 32.0, 28.0, 31.0, 28.7)); }
            else setStats(dto, "華盛頓巫師", 21.2, 5.9, 4.4, 78, 80, Arrays.asList(19.0, 22.0, 25.0, 20.0, 21.2));
        } else if (name.contains("Kobe Bryant")) {
            if (year > 2016) { status = "【已退役】"; dto.setSeason("2016"); setStats(dto, "洛杉磯湖人", 17.6, 3.7, 2.8, 65, 68, Arrays.asList(15.0, 20.0, 12.0, 25.0, 17.6)); }
            else setStats(dto, "洛杉磯湖人", (year == 2006 ? 35.4 : 25.3), 5.2, 4.7, 85, 90, Arrays.asList(22.0, 28.0, 35.0, 24.0, 25.3));
        } else if (name.contains("LeBron James")) {
            String team = (year <= 2010) ? "克里夫蘭騎士" : (year <= 2014 ? "邁阿密熱火" : (year <= 2018 ? "克里夫蘭騎士" : "洛杉磯湖人"));
            setStats(dto, team, 27.2, 7.5, 7.3, 85, 98, Arrays.asList(25.0, 30.0, 22.0, 32.0, 27.2));
        } else if (name.contains("Stephen Curry")) {
            if (year < 2009) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0,0,0, Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0)); }
            else setStats(dto, "金州勇士", 27.3, 4.8, 6.5, 70, 99, Arrays.asList(22.0, 35.0, 28.0, 31.0, 27.3));
        } else if (name.contains("Luka Doncic")) {
            if (year < 2018) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0,0,0, Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0)); }
            else setStats(dto, "達拉斯獨行俠", 33.9, 9.2, 9.8, 75, 97, Arrays.asList(30.0, 40.0, 35.0, 33.0, 33.9));
        } else if (name.contains("Kyrie Irving")) {
            String team = (year <= 2017) ? "克里夫蘭騎士" : (year <= 2019 ? "波士頓塞爾提克" : (year <= 2023 ? "布魯克林籃網" : "達拉斯獨行俠"));
            setStats(dto, team, 23.6, 3.9, 5.7, 60, 88, Arrays.asList(20.0, 25.0, 30.0, 22.0, 23.6));
        } else if (name.contains("Shaquille O'Neal")) {
            String team = (year <= 2004) ? "洛杉磯湖人" : (year <= 2008 ? "邁阿密熱火" : "鳳凰城太陽");
            setStats(dto, team, 24.1, 11.0, 2.5, 95, 92, Arrays.asList(20.0, 25.0, 28.0, 22.0, 24.1));
        } else if (name.contains("Dwight Howard")) {
            if (year > 2022) { status = "【已退役】"; dto.setSeason("2022"); setStats(dto, "海外/退休", 6.2, 5.9, 0.6, 80, 60, Arrays.asList(6.0, 4.0, 8.0, 5.0, 6.2)); }
            else setStats(dto, "洛杉磯湖人", 13.0, 10.0, 1.2, 90, 80, Arrays.asList(10.0, 15.0, 12.0, 14.0, 13.0));
        } else {
            setStats(dto, "NBA 聯盟", 15.0, 4.0, 4.0, 70, 70, Arrays.asList(12.0, 18.0, 15.0, 14.0, 15.0));
        }

        // --- Python 邏輯同步 ---
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
            d.setCoreStyle("🎯 組織大師 (Playmaking Maestro)"); d.setSimpleRating("以傳球優先的組織核心，同時具備可靠的得分能力。");
        } else if (d.getReb() >= HIGH_REB && d.getPts() < 15) {
            d.setCoreStyle("🧱 籃板/防守支柱 (Rebounding/Defense Anchor)"); d.setSimpleRating("內線防守和籃板的專家，內線支柱。");
        } else {
            d.setCoreStyle("角色球員 (Role Player)"); d.setSimpleRating("一名可靠的輪換球員。");
        }
    }

    private void setStats(PlayerDTO d, String team, double p, double r, double a, int def, int eff, List<Double> h) {
        d.setTeam(team); d.setPts(p); d.setReb(r); d.setAst(a); d.setDef(def); d.setEff(eff); d.setPtsHistory(h);
        d.setRadarData(Arrays.asList((int)(p*3), (int)(r*8), (int)(a*10), def, eff));
    }

    public List<String> getTeams() {
        return Arrays.asList("洛杉磯湖人", "金州勇士", "芝加哥公牛", "達拉斯獨行俠", "鳳凰城太陽", "密爾瓦基公鹿", "波士頓塞爾提克", "休士頓火箭", "布魯克林籃網", "華盛頓巫師", "邁阿密熱火", "奧蘭多魔術");
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
            case "芝加哥公牛":
                if (year <= 1998) { players.add("Michael Jordan"); players.add("Scottie Pippen"); }
                else if (year <= 2012) { players.add("Derrick Rose"); players.add("Joakim Noah"); }
                else { players.add("Jimmy Butler"); players.add("Zach LaVine"); }
                break;
            case "達拉斯獨行俠":
                if (year <= 2018) { players.add("Dirk Nowitzki"); players.add("Jason Kidd"); }
                else { players.add("Luka Doncic"); players.add("Kyrie Irving"); }
                break;
            case "鳳凰城太陽":
                if (year <= 2012) { players.add("Steve Nash"); players.add("Shaquille O'Neal"); }
                else { players.add("Devin Booker"); players.add("Kevin Durant"); }
                break;
            case "波士頓塞爾提克":
                if (year <= 2013) { players.add("Paul Pierce"); players.add("Kevin Garnett"); }
                else { players.add("Jayson Tatum"); players.add("Jaylen Brown"); }
                break;
            case "休士頓火箭":
                if (year <= 2011) { players.add("Yao Ming"); players.add("Tracy McGrady"); }
                else { players.add("James Harden"); players.add("Dillon Brooks"); }
                break;
            case "邁阿密熱火":
                if (year <= 2014) { players.add("LeBron James"); players.add("Dwyane Wade"); }
                else { players.add("Jimmy Butler"); players.add("Bam Adebayo"); }
                break;
            case "華盛頓巫師":
                if (year >= 2001 && year <= 2003) { players.add("Michael Jordan"); players.add("Jerry Stackhouse"); }
                else { players.add("Bradley Beal"); players.add("Kyle Kuzma"); }
                break;
            case "奧蘭多魔術":
                if (year <= 2012) { players.add("Dwight Howard"); players.add("Tracy McGrady"); }
                else { players.add("Paolo Banchero"); players.add("Franz Wagner"); }
                break;
            case "密爾瓦基公鹿":
                players.add("Giannis Antetokounmpo"); players.add("Khris Middleton");
                break;
            case "布魯克林籃網":
                if (year >= 2019 && year <= 2023) { players.add("Kevin Durant"); players.add("Kyrie Irving"); }
                else { players.add("Vince Carter"); players.add("Jason Kidd"); }
                break;
        }
        return players;
    }
}
