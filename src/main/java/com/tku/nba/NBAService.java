package com.tku.nba;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class NBAService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public PlayerDTO getPlayerData(String name) {
        PlayerDTO dto = new PlayerDTO();
        dto.setFullName(name);

        try {
            // 步驟 1: 搜尋球員獲取 ID
            String searchUrl = "https://www.balldontlie.io/api/v1/players?search=" + name.replace(" ", "_");
            String playerInfo = sendGetRequest(searchUrl);
            JsonNode playerNode = objectMapper.readTree(playerInfo).get("data");

            if (playerNode.isArray() && playerNode.size() > 0) {
                int playerId = playerNode.get(0).get("id").asInt();
                dto.setFullName(playerNode.get(0).get("first_name").asText() + " " + playerNode.get(0).get("last_name").asText());

                // 步驟 2: 根據 ID 獲取本賽季平均數據
                String statsUrl = "https://www.balldontlie.io/api/v1/season_averages?player_ids[]=" + playerId;
                String statsInfo = sendGetRequest(statsUrl);
                JsonNode statsNode = objectMapper.readTree(statsInfo).get("data");

                if (statsNode.isArray() && statsNode.size() > 0) {
                    JsonNode seasonData = statsNode.get(0);
                    double pts = seasonData.get("pts").asDouble();
                    double reb = seasonData.get("reb").asDouble();
                    double ast = seasonData.get("ast").asDouble();

                    dto.setPts(pts);
                    dto.setReb(reb);
                    dto.setAst(ast);
                    dto.setCoreStyle(analyzeStyle(pts, reb, ast));
                } else {
                    dto.setCoreStyle("⚠️ 該球員本賽季尚無出賽數據");
                }
            } else {
                dto.setCoreStyle("❌ 找不到該球員，請確認英文姓名");
            }
        } catch (Exception e) {
            dto.setCoreStyle("🛑 數據連接錯誤: " + e.getMessage());
        }
        return dto;
    }

    private String sendGetRequest(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private String analyzeStyle(double pts, double reb, double ast) {
        
        if (pts >= 25 && ast >= 7) {
            return "🌟 頂級全能巨星 (Elite All-Around)";
        } else if (pts >= 25 && reb >= 10) {
            return "🦾 統治級內線 (Dominant Big Man)";
        } else if (pts >= 20) {
            return "🔥 頂尖得分手 (Elite Scorer)";
        } else if (ast >= 8) {
            return "🪄 組織大腦 (Floor General)";
        } else if (reb >= 10) {
            return "🛡️ 禁區守護者 (Paint Protector)";
        } else if (pts >= 12 && ast >= 4) {
            return "🚄 優秀雙能衛 (Skilled Combo Guard)";
        } else if (pts >= 15) {
            return "🏹 核心得分箭頭 (Primary Scorer)";
        } else if (pts >= 8 && (reb >= 5 || ast >= 3)) {
            return "🛠️ 優質角色球員 (Solid Role Player)";
        } else {
            return "👟 板凳戰力 (Bench Contributor)";
        }
    }
}
