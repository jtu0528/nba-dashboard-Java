package com.tku.nba;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NBAService {

    public List<String> getAllTeams() {
        return Arrays.asList("Lakers", "Warriors", "Celtics", "Nets", "Suns", "Bucks", "Mavericks", "Bulls");
    }

    public List<String> getAllPlayers() {
        return Arrays.asList("Kobe Bryant", "Stephen Curry", "LeBron James", "Michael Jordan", "Kevin Durant", "Luka Doncic", "Shaquille O'Neal", "Rudy Gobert");
    }

    public List<String> getPlayersByTeam(String team) {
        if ("Warriors".equals(team)) return Arrays.asList("Stephen Curry", "Klay Thompson");
        if ("Lakers".equals(team)) return Arrays.asList("LeBron James", "Kobe Bryant", "Anthony Davis");
        if ("Bulls".equals(team)) return Arrays.asList("Michael Jordan", "Scottie Pippen");
        return getAllPlayers();
    }

    public String getTeamByPlayer(String name, String season) {
        if (name.contains("Curry")) return "Warriors";
        if (name.contains("LeBron")) return Integer.parseInt(season) > 2018 ? "Lakers" : "Cavaliers";
        if (name.contains("Jordan")) return "Bulls";
        return "Free Agent";
    }

    public PlayerDTO getPlayerData(String name, String season, String team) {
        PlayerDTO dto = new PlayerDTO();
        dto.setFullName(name);
        dto.setSeason(season);
        dto.setTeam(team);

        // 這裡可以串接 API，但為了 Demo 穩定，根據名字給予 2000-2026 的特徵值
        if (name.contains("Curry")) {
            dto.setPts(26.4); dto.setReb(4.5); dto.setAst(6.1);
            dto.setPtsHistory(Arrays.asList(20.0, 25.0, 32.0, 28.0, 30.0, 26.4));
        } else if (name.contains("Jordan")) {
            dto.setPts(30.1); dto.setReb(6.2); dto.setAst(5.3);
            dto.setPtsHistory(Arrays.asList(35.0, 28.0, 40.0, 32.0, 25.0, 30.1));
        } else {
            dto.setPts(15.2); dto.setReb(5.0); dto.setAst(4.0);
            dto.setPtsHistory(Arrays.asList(10.0, 15.0, 12.0, 18.0, 14.0, 15.2));
        }

        dto.setCoreStyle(analyzeStyle(dto.getPts(), dto.getAst(), dto.getReb()));
        return dto;
    }

    private String analyzeStyle(double pts, double ast, double reb) {
        if (pts >= 25 && ast >= 6) return "🌟 全能統治級巨星";
        if (pts >= 25) return "🔥 傳奇得分王";
        if (reb >= 10) return "🛡️ 禁區守護者";
        return "🛠️ 角色球員";
    }
}
