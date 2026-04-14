package com.tku.nba.service;

import com.tku.nba.model.PlayerDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NBAService {

    // 模擬調用 NBA 資料 API (例如 RapidAPI)
    public PlayerDTO getPlayerAnalysis(String name) {
        // 在專業後端，這裡會用 RestTemplate 呼叫真正的 API
        // RestTemplate restTemplate = new RestTemplate();
        // String json = restTemplate.getForObject("https://api.nba.com/v1/player/" + name, String.class);
        
        // --- 移植你的 Python 分析邏輯 ---
        PlayerDTO player = new PlayerDTO();
        player.setFullName(name);
        player.setPts(28.5); // 假設 API 抓到的數據
        player.setAst(7.2);
        player.setReb(6.8);

        // analyze_style 邏輯
        if (player.getPts() >= 25 && player.getAst() >= 6) {
            player.setStyle("🌟 頂級全能巨星");
            player.setRating("具備統治力的核心球員。");
        } else {
            player.setStyle("角色球員");
            player.setRating("穩定的輪換戰力。");
        }
        
        return player;
    }
}
