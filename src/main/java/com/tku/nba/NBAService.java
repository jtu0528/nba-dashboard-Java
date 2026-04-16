package com.tku.nba;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NBAService {

    /**
     * 此為手動錄入之真實歷史數據庫，涵蓋球員活躍週期與生涯末期表現。
     * 用於模擬真實數據庫在處理「球員退役狀態」時的邏輯判斷。
     */
    public PlayerDTO getFullAnalytics(String name, String season) {
        PlayerDTO dto = new PlayerDTO();
        dto.setFullName(name);
        dto.setSeason(season);
        
        int selectedYear = Integer.parseInt(season);
        String statusPrefix = "";

        // --- 球員活躍週期與歷史數據邏輯控制 ---

        if (name.contains("Jordan")) {
            // Michael Jordan: 2001-2003 復出於巫師，2003正式退休
            int lastActive = 2003;
            if (selectedYear > lastActive) {
                statusPrefix = "【已退役】";
                setStats(dto, "Wizards (Retired)", 20.0, 6.1, 3.8, Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
            } else if (selectedYear < 2001) {
                statusPrefix = "【未復出/退休中】";
                setStats(dto, "Retired", 0.0, 0.0, 0.0, Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
            } else {
                setStats(dto, "Wizards", 20.0, 6.1, 3.8, Arrays.asList(18.5, 22.0, 25.4, 19.0, 20.2, 20.0));
            }

        } else if (name.contains("Kobe")) {
            // Kobe Bryant: 1996-2016 效力於湖人，2016正式退休
            int lastActive = 2016;
            if (selectedYear > lastActive) {
                statusPrefix = "【已退役】";
                setStats(dto, "Lakers (Retired)", 17.6, 3.7, 2.8, Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
            } else {
                // 模擬 2006 巔峰賽季與其餘賽季差異
                if (selectedYear == 2006) setStats(dto, "Lakers", 35.4, 5.3, 4.5, Arrays.asList(40.2, 35.0, 45.0, 28.0, 31.0, 35.4));
                else setStats(dto, "Lakers", 25.3, 5.2, 4.7, Arrays.asList(22.0, 28.0, 24.0, 26.0, 30.0, 25.3));
            }

        } else if (name.contains("James")) {
            // LeBron James: 2003-Present (活躍中)
            if (selectedYear < 2003) {
                statusPrefix = "【尚未入盟】";
                setStats(dto, "N/A", 0.0, 0.0, 0.0, Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
            } else {
                String team = (selectedYear <= 2010) ? "Cavaliers" : (selectedYear <= 2014 ? "Heat" : "Lakers");
                setStats(dto, team, 27.1, 7.5, 7.4, Arrays.asList(25.0, 30.0, 22.0, 28.0, 32.0, 27.1));
            }

        } else if (name.contains("O'Neal")) {
            // Shaquille O'Neal: 1992-2011 正式退休
            int lastActive = 2011;
            if (selectedYear > lastActive) {
                statusPrefix = "【已退役】";
                setStats(dto, "Celtics (Retired)", 9.2, 4.8, 0.7, Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
            } else {
                setStats(dto, "Lakers/Heat", 23.7, 10.9, 2.5, Arrays.asList(20.0, 25.0, 30.0, 22.0, 18.0, 23.7));
            }

        } else if (name.contains("Curry")) {
            // Stephen Curry: 2009-Present (活躍中)
            if (selectedYear < 2009) {
                statusPrefix = "【尚未入盟】";
                setStats(dto, "N/A", 0.0, 0.0, 0.0, Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
            } else {
                setStats(dto, "Warriors", 27.3, 6.5, 4.8, Arrays.asList(22.0, 31.0, 28.0, 35.0, 20.0, 27.3));
            }

        } else {
            // 基礎通用模擬數據
            setStats(dto, "NBA League", 15.0, 4.0, 4.0, Arrays.asList(10.0, 15.0, 12.0, 18.0, 14.0, 15.0));
        }

        // 根據判定狀態生成分析標籤
        if (statusPrefix.contains("退役")) {
            dto.setCoreStyle("🐍 傳奇存檔 (呈現生涯最後數據)");
        } else if (statusPrefix.contains("尚未") || dto.getPts() == 0) {
            dto.setCoreStyle("⏳ 非活躍賽季 (無數據)");
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

    public List<String> getTeams() {
        return Arrays.asList("Lakers", "Warriors", "Bulls", "Mavericks", "Spurs");
    }

    public List<String> getPlayersByTeam(String team) {
        switch (team) {
            case "Lakers": return Arrays.asList("Kobe Bryant", "LeBron James", "Shaquille O'Neal");
            case "Warriors": return Arrays.asList("Stephen Curry", "Kevin Durant");
            case "Bulls": return Arrays.asList("Michael Jordan");
            default: return Arrays.asList("Michael Jordan", "Kobe Bryant", "Stephen Curry", "LeBron James");
        }
    }
}
