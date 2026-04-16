package com.tku.nba;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NBAService {

    /**
     * 此為預設模擬資料庫，用於確保演示環境穩定。
     * 提供 2000-2026 年間標竿球員之場均數據。
     */
    public PlayerDTO getFullAnalytics(String name, String season) {
        PlayerDTO dto = new PlayerDTO();
        dto.setFullName(name);
        dto.setSeason(season);
        
        int year = Integer.parseInt(season);

        // 根據球員姓名進行數據匹配
        if (name.contains("Curry")) {
            dto.setTeam("Warriors");
            // 模擬賽季表現差異：2021年後數據權重增加
            if (year >= 2021) { 
                setStats(dto, 29.4, 6.1, 5.4, Arrays.asList(32.0, 18.0, 25.0, 40.0, 22.0, 29.4)); 
            } else { 
                setStats(dto, 23.5, 6.6, 4.5, Arrays.asList(20.0, 22.0, 25.0, 18.0, 30.0, 23.5)); 
            }
        } else if (name.contains("Jordan")) {
            dto.setTeam("Bulls");
            setStats(dto, 30.1, 5.3, 6.2, Arrays.asList(35.0, 40.0, 28.0, 32.0, 45.0, 30.1));
        } else if (name.contains("Kobe")) {
            dto.setTeam("Lakers");
            if (year <= 2008) { 
                setStats(dto, 35.4, 4.5, 5.3, Arrays.asList(40.0, 42.0, 50.0, 25.0, 31.0, 35.4)); 
            } else { 
                setStats(dto, 25.3, 4.8, 5.1, Arrays.asList(22.0, 28.0, 24.0, 26.0, 30.0, 25.3)); 
            }
        } else if (name.contains("LeBron")) {
            dto.setTeam(year <= 2014 ? "Heat" : "Lakers");
            setStats(dto, 27.2, 7.5, 7.3, Arrays.asList(25.0, 30.0, 22.0, 28.0, 32.0, 27.2));
        } else if (name.contains("Doncic")) {
            dto.setTeam("Mavericks");
            setStats(dto, 33.9, 9.8, 9.2, Arrays.asList(28.0, 35.0, 40.0, 33.0, 31.0, 33.9));
        } else {
            // 基礎通用數據
            dto.setTeam("NBA League");
            setStats(dto, 15.2, 4.2, 4.0, Arrays.asList(10.0, 15.0, 12.0, 18.0, 14.0, 15.2));
        }

        // 執行統計分析標註
        dto.setCoreStyle(analyzeStyle(dto.getPts(), dto.getAst(), dto.getReb()));
        return dto;
    }

    private void setStats(PlayerDTO d, double p, double a, double r, List<Double> h) {
        d.setPts(p); d.setAst(a); d.setReb(r); d.setPtsHistory(h);
    }

    private String analyzeStyle(double pts, double ast, double reb) {
        if (pts >= 30) return "👑 歷史級得分機器";
        if (pts >= 25 && ast >= 7) return "🌟 頂級全能超級巨星";
        if (reb >= 10) return "🛡️ 禁區守護者";
        if (ast >= 8) return "🪄 核心組織指揮官";
        return "🛠️ 優質核心角色球員";
    }

    // 此為預設球隊清單
    public List<String> getTeams() {
        return Arrays.asList("Lakers", "Warriors", "Bulls", "Mavericks", "Spurs", "Suns");
    }

    // 根據球隊連動對應球員名單
    public List<String> getPlayersByTeam(String team) {
        switch (team) {
            case "Lakers": return Arrays.asList("Kobe Bryant", "LeBron James", "Shaquille O'Neal");
            case "Warriors": return Arrays.asList("Stephen Curry", "Kevin Durant", "Klay Thompson");
            case "Bulls": return Arrays.asList("Michael Jordan", "Derrick Rose");
            case "Mavericks": return Arrays.asList("Luka Doncic", "Dirk Nowitzki");
            default: return Arrays.asList("Stephen Curry", "LeBron James", "Kobe Bryant", "Michael Jordan", "Luka Doncic");
        }
    }
}
