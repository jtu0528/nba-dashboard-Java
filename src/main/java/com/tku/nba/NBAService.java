package com.tku.nba;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

@Service
public class NBAService {
    private final ObjectMapper mapper = new ObjectMapper();

    private final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final String API_BASE = "https://www.balldontlie.io/api/v1";

    public Map<Integer, String> getTeamMap() {
        Map<Integer, String> teams = new TreeMap<>();
        try {
            String res = fetchData(API_BASE + "/teams");
            JsonNode data = mapper.readTree(res).get("data");
            for (JsonNode team : data) {
                teams.put(team.get("id").asInt(), team.get("full_name").asText());
            }
        } catch (Exception e) {
            System.err.println("無法取得球隊清單: " + e.getMessage());
        }
        return teams;
    }

    public List<String> getPlayersByTeam(String teamName) {
        try {

            String res = fetchData(API_BASE + "/players?per_page=100");
            JsonNode data = mapper.readTree(res).get("data");
            List<String> players = new ArrayList<>();
            for (JsonNode p : data) {
                if (p.get("team").get("full_name").asText().equalsIgnoreCase(teamName)) {
                    players.add(p.get("first_name").asText() + " " + p.get("last_name").asText());
                }
            }
            return players;
        } catch (Exception e) { return Collections.emptyList(); }
    }

    public PlayerDTO getFullAnalytics(String playerName, String season) {
        PlayerDTO dto = new PlayerDTO();
        try {
            // 1. 查球員 ID
            String pRes = fetchData(API_BASE + "/players?search=" + playerName.replace(" ", "_"));
            JsonNode pNode = mapper.readTree(pRes).get("data");
            if (pNode == null || pNode.size() == 0) return null;
            
            int id = pNode.get(0).get("id").asInt();
            dto.setFullName(pNode.get(0).get("first_name").asText() + " " + pNode.get(0).get("last_name").asText());
            dto.setTeam(pNode.get(0).get("team").get("full_name").asText());
            dto.setSeason(season);

            // 2. 查場均數據 
            String sRes = fetchData(API_BASE + "/season_averages?season=" + season + "&player_ids[]=" + id);
            JsonNode sData = mapper.readTree(sRes).get("data");
            
            if (sData.isArray() && sData.size() > 0) {
                JsonNode stats = sData.get(0);
                dto.setPts(stats.get("pts").asDouble());
                dto.setReb(stats.get("reb").asDouble());
                dto.setAst(stats.get("ast").asDouble());
            }

            // 3. 查最近比賽數據 (趨勢圖)
            String gRes = fetchData(API_BASE + "/stats?player_ids[]=" + id + "&seasons[]=" + season + "&per_page=10");
            JsonNode gData = mapper.readTree(gRes).get("data");
            List<Double> history = new ArrayList<>();
            if (gData.isArray()) {
                for (JsonNode game : gData) {
                    history.add(game.get("pts").asDouble());
                }
            }
            dto.setPtsHistory(history);
            dto.setCoreStyle(analyze(dto.getPts(), dto.getAst(), dto.getReb()));

        } catch (Exception e) {
            System.err.println("數據抓取核心錯誤: " + e.getMessage());
        }
        return dto;
    }

    private String fetchData(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Mozilla/5.0") 
                .header("Accept", "application/json")
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private String analyze(double pts, double ast, double reb) {
        if (pts >= 25 && ast >= 7) return "🌟 頂級全能超級巨星";
        if (pts >= 25) return "🔥 傳奇得分王";
        if (reb >= 10) return "🛡️ 禁區守護者";
        if (ast >= 8) return "🪄 核心組織者";
        return "🛠️ 角色球員";
    }
}
