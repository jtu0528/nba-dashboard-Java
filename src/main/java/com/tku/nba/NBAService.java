package com.tku.nba;

import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class NBAService {

    public PlayerDTO getPlayerData(String name, String season) {
        PlayerDTO dto = new PlayerDTO();
        dto.setFullName(name);

        // 根據球員名字給予不同場均與趨勢 (確保 Demo 時有差異)
        if (name.contains("Curry")) {
            dto.setPts(26.4); dto.setReb(4.5); dto.setAst(5.1);
            dto.setPtsHistory(Arrays.asList(30.0, 22.0, 28.0, 35.0, 18.0, 26.4));
        } else if (name.contains("LeBron")) {
            dto.setPts(25.7); dto.setReb(7.3); dto.setAst(8.3);
            dto.setPtsHistory(Arrays.asList(24.0, 26.0, 30.0, 22.0, 28.0, 25.7));
        } else if (name.contains("Gobert")) {
            dto.setPts(14.0); dto.setReb(12.9); dto.setAst(1.3);
            dto.setPtsHistory(Arrays.asList(12.0, 15.0, 10.0, 18.0, 14.0, 14.0));
        } else {
            dto.setPts(18.5); dto.setReb(5.0); dto.setAst(4.0);
            dto.setPtsHistory(Arrays.asList(15.0, 20.0, 17.0, 22.0, 19.0, 18.5));
        }

        dto.setCoreStyle(analyzeStyle(dto.getPts(), dto.getReb(), dto.getAst()));
        return dto;
    }

    private String analyzeStyle(double pts, double reb, double ast) {
        if (pts >= 25 && ast >= 7) return "🌟 頂級全能巨星 (Elite All-Around)";
        if (pts >= 20) return "🔥 頂尖得分手 (Elite Scorer)";
        if (reb >= 10) return "🛡️ 禁區守護者 (Paint Protector)";
        if (ast >= 8) return "🪄 組織大腦 (Floor General)";
        return "🛠️ 角色球員 (Role Player)";
    }
}
