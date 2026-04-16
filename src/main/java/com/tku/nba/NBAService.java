package com.tku.nba;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NBAService {

    /**
     * 此為模擬資料庫，整合 2000-2026 年 NBA 標竿球員之真實歷史變遷。
     * 用於演示後端對於球員活躍狀態、退役回溯及跨賽季數據之邏輯處理。
     */
    public PlayerDTO getFullAnalytics(String name, String season) {
        PlayerDTO dto = new PlayerDTO();
        dto.setFullName(name);
        dto.setSeason(season);
        
        int year = Integer.parseInt(season);
        String status = ""; // 用於儲存活躍狀態標記

        // --- 核心模擬數據引擎：精確球員歷史邏輯 ---

        if (name.contains("Michael Jordan")) {
            // Michael Jordan: 2001-2003 巫師，2003後正式退役
            if (year > 2003) {
                status = "【已退役】";
                setStats(dto, "華盛頓巫師 (傳奇存檔)", 20.0, 3.8, 6.1, Arrays.asList(18.0, 22.0, 20.0, 25.0, 19.0, 20.0));
            } else if (year < 2001) {
                status = "【退休/未復出】";
                setStats(dto, "芝加哥公牛 (歷史數據)", 30.1, 5.3, 6.2, Arrays.asList(32.0, 28.0, 45.0, 38.0, 22.0, 30.1));
            } else {
                setStats(dto, "華盛頓巫師", 20.0, 3.8, 6.1, Arrays.asList(18.0, 22.0, 20.0, 25.0, 19.0, 20.0));
            }

        } else if (name.contains("Kobe Bryant")) {
            // Kobe Bryant: 2016 正式退役
            if (year > 2016) {
                status = "【已退役】";
                setStats(dto, "洛杉磯湖人 (傳奇存檔)", 17.6, 2.8, 3.7, Arrays.asList(15.0, 12.0, 20.0, 34.0, 25.0, 17.6));
            } else {
                if (year == 2006) setStats(dto, "洛杉磯湖人", 35.4, 4.5, 5.3, Arrays.asList(40.2, 35.0, 45.0, 28.0, 31.0, 35.4));
                else setStats(dto, "洛杉磯湖人", 25.3, 4.7, 5.2, Arrays.asList(22.0, 28.0, 24.0, 26.0, 30.0, 25.3));
            }

        } else if (name.contains("LeBron James")) {
            // LeBron James: 2003-至今
            if (year < 2003) { status = "【尚未入盟】"; setStats(dto, "N/A", 0, 0, 0, Arrays.asList(0.0)); }
            else {
                String team = (year <= 2010) ? "克里夫蘭騎士" : (year <= 2014 ? "邁阿密熱火" : (year <= 2018 ? "克里夫蘭騎士" : "洛杉磯湖人"));
                setStats(dto, team, 27.2, 7.4, 7.5, Arrays.asList(25.0, 30.0, 22.0, 32.0, 28.0, 27.2));
            }

        } else if (name.contains("Kyrie Irving")) {
            // Kyrie Irving: 2011-至今
            if (year < 2011) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0, Arrays.asList(0.0)); }
            else if (year <= 2017) setStats(dto, "克里夫蘭騎士", 21.6, 5.5, 3.4, Arrays.asList(18.0, 22.0, 25.0, 21.6));
            else if (year <= 2019) setStats(dto, "波士頓塞爾提克", 24.1, 6.9, 4.4, Arrays.asList(22.0, 24.1));
            else if (year <= 2023) setStats(dto, "布魯克林籃網", 27.1, 5.8, 4.8, Arrays.asList(25.0, 27.1));
            else setStats(dto, "達拉斯獨行俠", 25.6, 5.2, 5.0, Arrays.asList(24.0, 25.6));

        } else if (name.contains("Dwight Howard")) {
            // Dwight Howard: 2022後顯示退役
            if (year < 2004) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0, Arrays.asList(0.0)); }
            else if (year > 2022) { status = "【已退役】"; setStats(dto, "湖人/海外 (傳奇存檔)", 6.2, 0.6, 5.9, Arrays.asList(6.2)); }
            else if (year <= 2012) setStats(dto, "奧蘭多魔術", 18.4, 1.5, 13.0, Arrays.asList(18.4));
            else if (year <= 2016) setStats(dto, "休士頓火箭", 16.0, 1.4, 11.7, Arrays.asList(16.0));
            else setStats(dto, "洛杉磯湖人", 7.5, 0.7, 7.3, Arrays.asList(7.5));

        } else if (name.contains("Devin Booker")) {
            if (year < 2015) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0, Arrays.asList(0.0)); }
            else setStats(dto, "鳳凰城太陽", 27.1, 6.9, 4.5, Arrays.asList(22.0, 30.0, 27.1));

        } else if (name.contains("Giannis Antetokounmpo")) {
            if (year < 2013) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0, Arrays.asList(0.0)); }
            else setStats(dto, "密爾瓦基公鹿", 30.4, 6.5, 11.5, Arrays.asList(28.0, 32.0, 30.4));

        } else if (name.contains("Kyle Kuzma")) {
            if (year < 2017) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0, Arrays.asList(0.0)); }
            else if (year <= 2021) setStats(dto, "洛杉磯湖人", 15.2, 1.9, 5.6, Arrays.asList(15.2));
            else setStats(dto, "華盛頓巫師", 22.2, 4.2, 6.6, Arrays.asList(22.2));

        } else if (name.contains("Dillon Brooks")) {
            if (year < 2017) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0, Arrays.asList(0.0)); }
            else if (year <= 2023) setStats(dto, "曼菲斯灰熊", 14.5, 2.1, 3.1, Arrays.asList(14.5));
            else setStats(dto, "休士頓火箭", 12.7, 1.7, 3.4, Arrays.asList(12.7));

        } else if (name.contains("Stephen Curry")) {
            if (year < 2009) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0, Arrays.asList(0.0)); }
            else setStats(dto, "金州勇士", 27.3, 6.5, 4.8, Arrays.asList(22.0, 31.0, 27.3));

        } else if (name.contains("Luka Doncic")) {
            if (year < 2018) { status = "【尚未入盟】"; setStats(dto, "N/A", 0,0,0, Arrays.asList(0.0)); }
            else setStats(dto, "達拉斯獨行俠", 33.9, 9.8, 9.2, Arrays.asList(30.0, 35.0, 33.9));

        } else {
            // 通用保底數據
            setStats(dto, "NBA 聯盟", 15.0, 4.0, 4.0, Arrays.asList(12.0, 18.0, 15.0));
        }

        // --- 統計分析標註邏輯 ---
        if (status.contains("退役")) {
            dto.setCoreStyle("🐍 歷史傳奇存檔 (呈現生涯末期表現)");
        } else if (status.contains("尚未") || dto.getPts() == 0) {
            dto.setCoreStyle("⏳ 非活躍賽季 (查無紀錄)");
        } else {
            dto.setCoreStyle(analyzeStyle(dto.getPts(), dto.getAst(), dto.getReb()));
        }
        
        dto.setFullName(status + dto.getFullName());
        return dto;
    }

    private void setStats(PlayerDTO d, String team, double p, double a, double r, List<Double> h) {
        d.setTeam(team); d.setPts(p); d.setAst(a); d.setReb(r); d.setPtsHistory(h);
    }

    private String analyzeStyle(double pts, double ast, double reb) {
        if (pts >= 30) return "👑 歷史級得分機器";
        if (pts >= 25 && ast >= 7) return "🌟 頂級全能超級巨星";
        if (reb >= 10) return "🛡️ 禁區守護者";
        if (ast >= 8) return "🪄 核心組織指揮官";
        return "🛠️ 優質核心角色球員";
    }

    public List<String> getTeams() {
        return Arrays.asList("洛杉磯湖人", "金州勇士", "芝加哥公牛", "達拉斯獨行俠", "鳳凰城太陽", "密爾瓦基公鹿", "休士頓火箭", "波士頓塞爾提克", "布魯克林籃網", "華盛頓巫師", "曼菲斯灰熊", "奧蘭多魔術");
    }

    public List<String> getPlayersByTeam(String team) {
        switch (team) {
            case "洛杉磯湖人": return Arrays.asList("Kobe Bryant", "LeBron James", "Kyle Kuzma", "Dwight Howard");
            case "金州勇士": return Arrays.asList("Stephen Curry");
            case "芝加哥公牛": return Arrays.asList("Michael Jordan");
            case "達拉斯獨行俠": return Arrays.asList("Luka Doncic", "Kyrie Irving");
            case "鳳凰城太陽": return Arrays.asList("Devin Booker");
            case "密爾瓦基公鹿": return Arrays.asList("Giannis Antetokounmpo");
            case "休士頓火箭": return Arrays.asList("Dillon Brooks", "Dwight Howard");
            case "波士頓塞爾提克": return Arrays.asList("Kyrie Irving");
            case "布魯克林籃網": return Arrays.asList("Kyrie Irving");
            case "華盛頓巫師": return Arrays.asList("Kyle Kuzma", "Michael Jordan");
            case "曼菲斯灰熊": return Arrays.asList("Dillon Brooks");
            case "奧蘭多魔術": return Arrays.asList("Dwight Howard");
            default: return Arrays.asList("Michael Jordan", "Kobe Bryant", "Stephen Curry", "LeBron James", "Luka Doncic");
        }
    }
}
