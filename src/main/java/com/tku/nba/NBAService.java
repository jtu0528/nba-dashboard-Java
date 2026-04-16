package com.tku.nba;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NBAService {

    // 定義判定閾值 (與 Python 的設定保持一致)
    private final double HIGH_PTS = 25.0;
    private final double HIGH_AST = 8.0;
    private final double HIGH_REB = 10.0;

    public PlayerDTO getFullAnalytics(String name, String season) {
        PlayerDTO dto = new PlayerDTO();
        dto.setFullName(name);
        dto.setSeason(season);
        int selectedYear = Integer.parseInt(season);
        String status = "";

        // --- 數據庫與回溯邏輯 ---
        if (name.contains("Michael Jordan")) {
            if (selectedYear > 2003) { status = "【已退役】"; dto.setSeason("2003"); setStats(dto, "華盛頓巫師 (傳奇存檔)", 20.0, 6.1, 3.8, 75, 78, Arrays.asList(18.0, 22.0, 20.0)); }
            else if (selectedYear < 2001) { status = "【退休/未復出】"; dto.setSeason("1998"); setStats(dto, "芝加哥公牛 (歷史數據)", 28.7, 5.8, 3.5, 96, 95, Arrays.asList(30.0, 28.7)); }
            else setStats(dto, "華盛頓巫師", 21.2, 5.9, 4.4, 78, 80, Arrays.asList(19.0, 21.2));
        } else if (name.contains("Kobe Bryant")) {
            if (selectedYear > 2016) { status = "【已退役】"; dto.setSeason("2016"); setStats(dto, "洛杉磯湖人 (傳奇存檔)", 17.6, 3.7, 2.8, 65, 68, Arrays.asList(17.6)); }
            else setStats(dto, "洛杉磯湖人", (selectedYear == 2006 ? 35.4 : 25.3), 5.2, 4.7, 88, 92, Arrays.asList(35.4));
        } else if (name.contains("LeBron James")) {
            String team = (selectedYear <= 2010) ? "克里夫蘭騎士" : (selectedYear <= 2014 ? "邁阿密熱火" : "洛杉磯湖人");
            setStats(dto, team, 27.2, 7.5, 7.3, 85, 98, Arrays.asList(27.2));
        } else if (name.contains("Stephen Curry")) {
            setStats(dto, "金州勇士", 27.3, 4.8, 6.5, 70, 99, Arrays.asList(27.3));
        } else if (name.contains("Luka Doncic")) {
            setStats(dto, "達拉斯獨行俠", 33.9, 9.2, 9.8, 75, 97, Arrays.asList(33.9));
        } else if (name.contains("Giannis")) {
            setStats(dto, "密爾瓦基公鹿", 30.4, 11.5, 6.5, 96, 98, Arrays.asList(30.4));
        } else if (name.contains("Dillon Brooks")) {
            setStats(dto, "休士頓火箭", 12.7, 3.4, 1.7, 95, 62, Arrays.asList(12.7));
        } else if (name.contains("Dwight Howard")) {
            if (selectedYear <= 2012) setStats(dto, "奧蘭多魔術", 18.4, 13.0, 1.5, 99, 85, Arrays.asList(18.4));
            else { status = "【已退役】"; dto.setSeason("2022"); setStats(dto, "海外/退休", 6.2, 5.9, 0.6, 80, 60, Arrays.asList(6.2)); }
        } else {
            setStats(dto, "NBA 聯盟", 15.0, 4.0, 4.0, 70, 70, Arrays.asList(15.0));
        }

        if (status.contains("退役")) {
            dto.setCoreStyle("🐍 歷史傳奇存檔 (Legacy)");
            dto.setSimpleRating("傳奇球員之歷史表現數據回溯。");
        } else {
            analyzeStylePythonic(dto);
        }

        dto.setFullName(status + dto.getFullName());
        return dto;
    }

    /**
     * 完全複製 Python 版本邏輯的判定器
     */
    private void analyzeStylePythonic(PlayerDTO d) {
        double pts = d.getPts();
        double ast = d.getAst();
        double reb = d.getReb();

        if (pts >= HIGH_PTS && ast >= 6 && reb >= 6) {
            d.setCoreStyle("🌟 頂級全能巨星 (Elite All-Around Star)");
            d.setSimpleRating("集得分、組織和籃板於一身的劃時代球員。");
        } else if (pts >= HIGH_PTS) {
            d.setCoreStyle("得分機器 (Volume Scorer)");
            d.setSimpleRating("聯盟頂級的得分手，能夠在任何位置取分。");
        } else if (ast >= HIGH_AST && pts >= 15) {
            d.setCoreStyle("🎯 組織大師 (Playmaking Maestro)");
            d.setSimpleRating("以傳球優先的組織核心，同時具備可靠的得分能力。");
        } else if (reb >= HIGH_REB && pts < 15) {
            d.setCoreStyle("🧱 籃板/防守支柱 (Rebounding/Defense Anchor)");
            d.setSimpleRating("內線防守和籃板的專家，隊伍的堅實後盾。");
        } else {
            d.setCoreStyle("角色球員 (Role Player)");
            d.setSimpleRating("一名可靠的輪換球員。");
        }
    }

    private void setStats(PlayerDTO d, String team, double p, double r, double a, int def, int eff, List<Double> h) {
        d.setTeam(team); d.setPts(p); d.setReb(r); d.setAst(a); d.setDef(def); d.setEff(eff); d.setPtsHistory(h);
        d.setRadarData(Arrays.asList((int)(p*3), (int)(r*8), (int)(a*10), def, eff));
    }

    public List<String> getTeams() { return Arrays.asList("洛杉磯湖人", "金州勇士", "芝加哥公牛", "達拉斯獨行俠", "密爾瓦基公鹿", "奧蘭多魔術", "休士頓火箭"); }
    public List<String> getPlayersByTeamAndSeason(String t, String s) {
        int y = Integer.parseInt(s);
        List<String> p = new ArrayList<>();
        if ("洛杉磯湖人".equals(t)) { if (y <= 2016) p.add("Kobe Bryant"); if (y >= 2018) p.add("LeBron James"); }
        else if ("芝加哥公牛".equals(t)) { if (y <= 1998) p.add("Michael Jordan"); }
        else if ("達拉斯獨行俠".equals(t)) { p.add("Luka Doncic"); p.add("Kyrie Irving"); }
        else if ("休士頓火箭".equals(t)) { p.add("Dillon Brooks"); }
        return p;
    }
}
