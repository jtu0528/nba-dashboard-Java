package com.tku.nba;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NBAService {

    /**
     * 此為模擬資料庫，整合 2000-2026 年間 NBA 標竿球員真實歷史平均數據。
     * 用於模擬真實數據庫在處理「球員退役狀態」時的邏輯判斷，並提供全中文球隊名稱支援。
     */
    public PlayerDTO getFullAnalytics(String name, String season) {
        PlayerDTO dto = new PlayerDTO();
        dto.setFullName(name);
        dto.setSeason(season);
        
        int selectedYear = Integer.parseInt(season);
        String statusPrefix = "";

        // --- 核心邏輯：球員活躍週期判定與跨隊歷史模擬 ---

        if (name.contains("Jordan")) {
            // Michael Jordan: 2001-2003 效力於華盛頓巫師
            int lastActive = 2003;
            if (selectedYear > lastActive) {
                statusPrefix = "【已退役】";
                setStats(dto, "華盛頓巫師 (傳奇存檔)", 20.0, 6.1, 3.8, Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
            } else if (selectedYear < 2001) {
                statusPrefix = "【退休/未復出】";
                setStats(dto, "芝加哥公牛 (傳奇存檔)", 30.1, 6.2, 5.3, Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
            } else {
                setStats(dto, "華盛頓巫師", 20.0, 6.1, 3.8, Arrays.asList(18.5, 22.0, 25.4, 19.0, 20.2, 20.0));
            }

        } else if (name.contains("Kobe")) {
            // Kobe Bryant: 1996-2016 全效力於洛杉磯湖人
            int lastActive = 2016;
            if (selectedYear > lastActive) {
                statusPrefix = "【已退役】";
                setStats(dto, "洛杉磯湖人 (傳奇存檔)", 17.6, 3.7, 2.8, Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
            } else {
                if (selectedYear == 2006) setStats(dto, "洛杉磯湖人", 35.4, 5.3, 4.5, Arrays.asList(40.2, 35.0, 45.0, 28.0, 31.0, 35.4));
                else setStats(dto, "洛杉磯湖人", 25.3, 5.2, 4.7, Arrays.asList(22.0, 28.0, 24.0, 26.0, 30.0, 25.3));
            }

        } else if (name.contains("James")) {
            // LeBron James: 2003-至今 活躍中
            if (selectedYear < 2003) {
                statusPrefix = "【尚未入盟】";
                setStats(dto, "N/A", 0.0, 0.0, 0.0, Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
            } else {
                String team = (selectedYear <= 2010) ? "克里夫蘭騎士" : (selectedYear <= 2014 ? "邁阿密熱火" : "洛杉磯湖人");
                setStats(dto, team, 27.2, 7.5, 7.3, Arrays.asList(25.0, 30.0, 22.0, 28.0, 32.0, 27.2));
            }

        } else if (name.contains("Curry")) {
            // Stephen Curry: 2009-至今 活躍中
            if (selectedYear < 2009) {
                statusPrefix = "【尚未入盟】";
                setStats(dto, "N/A", 0.0, 0.0, 0.0, Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
            } else {
                setStats(dto, "金州勇士", 27.3, 6.5, 4.8, Arrays.asList(22.0, 31.0, 28.0, 35.0, 20.0, 27.3));
            }

        } else if (name.contains("Doncic")) {
            // Luka Doncic: 2018-至今 活躍中
            if (selectedYear < 2018) {
                statusPrefix = "【尚未入盟】";
                setStats(dto, "N/A", 0.0, 0.0, 0.0, Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
            } else {
                setStats(dto, "達拉斯獨行俠", 33.9, 9.8, 9.2, Arrays.asList(28.0, 35.0, 40.0, 33.0, 31.0, 33.9));
            }
        } else {
            // 一般模擬數據
            setStats(dto, "NBA 聯盟", 15.0, 4.0, 4.0, Arrays.asList(10.0, 15.0, 12.0, 18.0, 14.0, 15.0));
        }

        // 分析分類標籤
        if (statusPrefix.contains("退役")) {
            dto.setCoreStyle("🐍 歷史傳奇存檔 (呈現退役前數據)");
        } else if (statusPrefix.contains("尚未") || dto.getPts() == 0) {
            dto.setCoreStyle("⏳ 非活躍賽季 (尚無數據)");
        } else {
            dto.setCoreStyle(analyzeStyle(dto.getPts(), dto.getAst(), dto.getReb()));
        }
        
        dto.setFullName(statusPrefix + dto.getFullName());
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

    /**
     * 此為全中文球隊清單，供前端選單調用。
     */
    public List<String> getTeams() {
        return Arrays.asList("洛杉磯湖人", "金州勇士", "芝加哥公牛", "達拉斯獨行俠", "聖安東尼奧馬刺", "克里夫蘭騎士", "邁阿密熱火");
    }

    /**
     * 此為球隊與球員之連動邏輯實作。
     */
    public List<String> getPlayersByTeam(String team) {
        switch (team) {
            case "洛杉磯湖人": return Arrays.asList("Kobe Bryant", "LeBron James", "Shaquille O'Neal");
            case "金州勇士": return Arrays.asList("Stephen Curry", "Kevin Durant", "Klay Thompson");
            case "芝加哥公牛": return Arrays.asList("Michael Jordan", "Derrick Rose");
            case "達拉斯獨行俠": return Arrays.asList("Luka Doncic", "Dirk Nowitzki");
            case "克里夫蘭騎士": return Arrays.asList("LeBron James", "Kyrie Irving");
            case "邁阿密熱火": return Arrays.asList("LeBron James", "Dwyane Wade");
            default: return Arrays.asList("Michael Jordan", "Kobe Bryant", "Stephen Curry", "LeBron James", "Luka Doncic");
        }
    }
}
