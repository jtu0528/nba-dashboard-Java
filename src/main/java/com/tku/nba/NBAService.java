package com.tku.nba;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NBAService {

    private final double HIGH_PTS = 25.0;
    private final double HIGH_AST = 8.0;
    private final double HIGH_REB = 10.0;

    private void populatePlayerStats(PlayerDTO d, String name, int year, String selectedTeam) {
        if (name.contains("LeBron James")) handleLeBron(d, year, selectedTeam);
        else if (name.contains("Kobe Bryant")) handleKobe(d, year, selectedTeam);
        else if (name.contains("Stephen Curry")) handleCurry(d, year, selectedTeam);
        else if (name.contains("Kevin Durant")) handleDurant(d, year, selectedTeam);
        else if (name.contains("James Harden")) handleHarden(d, year, selectedTeam);
        else if (name.contains("Russell Westbrook")) handleWestbrook(d, year, selectedTeam);
        else if (name.contains("Chris Paul")) handlePaul(d, year, selectedTeam);
        else if (name.contains("Shaquille O'Neal")) handleShaq(d, year, selectedTeam);
        else if (name.contains("Dwyane Wade")) handleWade(d, year, selectedTeam);
        else if (name.contains("Klay Thompson")) handleKlay(d, year, selectedTeam);
        else if (name.contains("Kyrie Irving")) handleKyrie(d, year, selectedTeam);
        else if (name.contains("Tim Duncan")) handleDuncan(d, year, selectedTeam);
        else if (name.contains("Tony Parker")) handleParker(d, year, selectedTeam);
        else if (name.contains("Dirk Nowitzki")) handleNowitzki(d, year, selectedTeam);
        else if (name.contains("Luka Doncic")) handleDoncic(d, year, selectedTeam);
        else if (name.contains("Steve Nash")) handleNash(d, year, selectedTeam);
        else if (name.contains("Amar'e Stoudemire")) handleStoudemire(d, year, selectedTeam);
        else if (name.contains("Jason Terry")) handleTerry(d, year, selectedTeam);
        else if (name.contains("Giannis Antetokounmpo")) handleGiannis(d, year, selectedTeam);
        else if (name.contains("Nikola Jokic")) handleJokic(d, year, selectedTeam);
        else if (name.contains("Anthony Davis")) handleDavis(d, year, selectedTeam);
        else if (name.contains("Victor Wembanyama")) handleWembanyama(d, year, selectedTeam);
        else if (name.contains("Pau Gasol")) handleGasol(d, year, selectedTeam);
        else if (name.contains("Damian Lillard")) handleLillard(d, year, selectedTeam);
        else if (name.contains("Jamal Murray")) handleMurray(d, year, selectedTeam);
        else setStats(d, "NBA 明星", 15.0, 5.0, 5.0, 75, 80, Arrays.asList(15.0));
    }

    // 計算該賽季資料庫中所有活躍球員的平均數據
    private double getSeasonAverage(int year, String category) {
        List<String> allPlayers = new ArrayList<>();
        // 抓出該年度所有球隊的球員
        for (String t : getTeams()) {
            allPlayers.addAll(getPlayersByTeamAndSeason(t, String.valueOf(year)));
        }
        allPlayers = new ArrayList<>(new LinkedHashSet<>(allPlayers)); 

        double total = 0.0;
        int count = 0;

        for (String pName : allPlayers) {
            PlayerDTO temp = new PlayerDTO();
            // 抓取數據但不跑 analyzeStyle，避免遞迴
            populatePlayerStats(temp, pName, year, "TOT"); 
            
            // 排除尚未入盟、受傷報銷或已退役
            if (temp.getPts() > 0 && temp.getTeam() != null && !temp.getTeam().contains("已退役")) { 
                if ("PTS".equals(category)) total += temp.getPts();
                else if ("AST".equals(category)) total += temp.getAst();
                else if ("REB".equals(category)) total += temp.getReb();
                count++;
            }
        }
        return count > 0 ? (total / count) : 0.0;
    }

    public PlayerDTO getFullAnalytics(String name, String season, String selectedTeam) {
        PlayerDTO d = new PlayerDTO();
        d.setFullName(name);
        d.setSeason(season);
        int year = Integer.parseInt(season);

		populatePlayerStats(d, name, year, selectedTeam);

// 狀態分析 「正常出賽」、「尚未入盟」與「賽季報銷」
        if (d.getPts() > 0) {
            // 正常出賽
            analyzeStyle(d);
	    if(d.getTeam() != null && d.getTeam().contains("已退役")) {
                d.setCoreStyle("🏁 已退役");
                d.setSimpleRating("球員已退役。圖表顯示其生涯最終賽季 (" + d.getTeam().replace(" (已退役)", "") + ") 之數據。");
            }
        } 
        else if ("尚未入盟".equals(d.getTeam())) {
            d.setCoreStyle("⏳ 尚未進入聯盟");
            d.setSimpleRating("該球員此時尚未參加 NBA 選秀，數據庫中無相關紀錄。");
            d.setRadarData(Arrays.asList(0, 0, 0, 0, 0));
        } 
        else {
            // 賽季報銷
            d.setCoreStyle("🚨 賽季報銷 / DNP");
            d.setSimpleRating("該賽季球員因嚴重傷勢報銷或無出賽紀錄，各項數據統計為 0。");
            d.setRadarData(Arrays.asList(0, 0, 0, 0, 0));
        }

        return d;
    }

// --- Jamal Murray ---
    private void handleMurray(PlayerDTO d, int y, String t) {
        if (y < 2016) { setStats(d, "尚未入盟", 0.0, 0.0, 0.0, 0, 0, Arrays.asList(0.0)); return; }
        switch (y) {
            case 2016: setStats(d, "丹佛金塊 (DEN)", 9.9, 2.6, 2.1, 70, 75, Arrays.asList(9.9)); break;
            case 2017: setStats(d, "丹佛金塊 (DEN)", 16.7, 3.7, 3.4, 72, 82, Arrays.asList(16.7)); break;
            case 2018: setStats(d, "丹佛金塊 (DEN)", 18.2, 4.2, 4.8, 75, 85, Arrays.asList(18.2)); break;
            case 2019: setStats(d, "丹佛金塊 (DEN)", 18.5, 4.0, 4.8, 75, 88, Arrays.asList(18.5)); break;
            case 2020: setStats(d, "丹佛金塊 (DEN)", 21.2, 4.0, 4.8, 78, 92, Arrays.asList(21.2)); break;
            case 2021: // 傷停 (ACL)
                setStats(d, "丹佛金塊 (DEN)", 0.0, 0.0, 0.0, 0, 0, Arrays.asList(0.0)); break;
            case 2022: setStats(d, "丹佛金塊 (DEN)", 20.0, 4.0, 6.2, 78, 90, Arrays.asList(20.0)); break;
            case 2023: setStats(d, "丹佛金塊 (DEN)", 21.2, 4.1, 6.5, 80, 92, Arrays.asList(21.2)); break;
            case 2024: setStats(d, "丹佛金塊 (DEN)", 21.4, 3.9, 6.0, 80, 92, Arrays.asList(21.4)); break;
            case 2025: setStats(d, "丹佛金塊 (DEN)", 25.4, 4.4, 7.1, 82, 95, Arrays.asList(25.4)); break;
        }
    }

// --- Damian Lillard ---
    private void handleLillard(PlayerDTO d, int y, String t) {
        if (y < 2012) { setStats(d, "尚未入盟", 0.0, 0.0, 0.0, 0, 0, Arrays.asList(0.0)); return; }
        switch (y) {
            case 2013: setStats(d, "波特蘭開拓者 (POR)", 22.9, 5.1, 6.5, 70, 88, Arrays.asList(22.9)); break;
            case 2014: setStats(d, "波特蘭開拓者 (POR)", 21.6, 4.0, 4.6, 70, 85, Arrays.asList(21.6)); break;
            case 2015: setStats(d, "波特蘭開拓者 (POR)", 26.5, 4.3, 6.3, 72, 92, Arrays.asList(26.5)); break;
            case 2016: setStats(d, "波特蘭開拓者 (POR)", 27.8, 4.5, 3.3, 72, 94, Arrays.asList(27.8)); break;
            case 2017: setStats(d, "波特蘭開拓者 (POR)", 18.5, 4.5, 4.8, 70, 85, Arrays.asList(18.5)); break;
            case 2018: setStats(d, "波特蘭開拓者 (POR)", 26.9, 4.8, 6.6, 72, 92, Arrays.asList(26.9)); break;
            case 2019: setStats(d, "波特蘭開拓者 (POR)", 24.3, 3.5, 4.3, 70, 90, Arrays.asList(24.3)); break;
            case 2020: setStats(d, "波特蘭開拓者 (POR)", 34.3, 4.3, 10.2, 75, 98, Arrays.asList(34.3)); break;
            case 2023: setStats(d, "密爾瓦基公鹿 (MIL)", 31.3, 3.3, 5.0, 75, 95, Arrays.asList(31.3)); break;
            case 2024: setStats(d, "密爾瓦基公鹿 (MIL)", 7.0, 2.7, 4.7, 72, 80, Arrays.asList(7.0)); break;
        }
    }

// --- Pau Gasol ---
    private void handleGasol(PlayerDTO d, int y, String t) {
        if (y < 2001) { setStats(d, "尚未入盟", 0.0, 0.0, 0.0, 0, 0, Arrays.asList(0.0)); return; }
	if (y > 2018) { setStats(d, "TOT (已退役)", 3.9, 4.6, 1.7, 75, 65, Arrays.asList(3.9)); return; }
        switch (y) {
            case 2001: setStats(d, "孟菲斯灰熊 (MEM)", 17.6, 8.9, 2.7, 82, 88, Arrays.asList(17.6)); break;
            case 2002: setStats(d, "孟菲斯灰熊 (MEM)", 19.0, 8.8, 2.8, 82, 90, Arrays.asList(19.0)); break;
            case 2003: setStats(d, "孟菲斯灰熊 (MEM)", 17.7, 7.7, 2.5, 80, 88, Arrays.asList(17.7)); break;
            case 2004: setStats(d, "孟菲斯灰熊 (MEM)", 17.8, 7.3, 2.4, 80, 88, Arrays.asList(17.8)); break;
            case 2005: setStats(d, "孟菲斯灰熊 (MEM)", 20.4, 8.9, 4.6, 82, 92, Arrays.asList(20.4)); break;
            case 2006: setStats(d, "孟菲斯灰熊 (MEM)", 20.8, 9.8, 3.4, 82, 93, Arrays.asList(20.8)); break;
            case 2007: // 季中交易：TOT, MEM, LAL
		d.setAvailableTeams(Arrays.asList("TOT", "MEM", "LAL"));
                if ("MEM".equals(t)) setStats(d, "孟菲斯灰熊 (MEM)", 18.9, 8.8, 3.0, 80, 92, Arrays.asList(18.9));
                else if ("LAL".equals(t)) setStats(d, "洛杉磯湖人 (LAL)", 18.8, 7.8, 3.5, 82, 92, Arrays.asList(18.8));
                else setStats(d, "TOT (賽季總計)", 18.9, 8.4, 3.2, 82, 92, Arrays.asList(18.9));
                break;
            case 2008: setStats(d, "洛杉磯湖人 (LAL)", 18.9, 9.6, 3.5, 85, 95, Arrays.asList(18.9)); break;
            case 2009: setStats(d, "洛杉磯湖人 (LAL)", 18.3, 11.3, 3.4, 85, 96, Arrays.asList(18.3)); break;
            case 2010: setStats(d, "洛杉磯湖人 (LAL)", 18.8, 10.2, 3.3, 85, 94, Arrays.asList(18.8)); break;
            case 2011: setStats(d, "洛杉磯湖人 (LAL)", 17.4, 10.4, 3.6, 82, 92, Arrays.asList(17.4)); break;
            case 2012: setStats(d, "洛杉磯湖人 (LAL)", 13.7, 8.6, 4.1, 80, 85, Arrays.asList(13.7)); break;
            case 2013: setStats(d, "洛杉磯湖人 (LAL)", 17.4, 9.7, 3.4, 80, 90, Arrays.asList(17.4)); break;
            case 2014: setStats(d, "芝加哥公牛 (CHI)", 18.5, 11.8, 2.7, 82, 94, Arrays.asList(18.5)); break;
            case 2015: setStats(d, "芝加哥公牛 (CHI)", 16.5, 11.0, 4.1, 80, 90, Arrays.asList(16.5)); break;
            case 2016: setStats(d, "聖安東尼奧馬刺 (SAS)", 12.4, 7.8, 2.3, 85, 85, Arrays.asList(12.4)); break;
            case 2017: setStats(d, "聖安東尼奧馬刺 (SAS)", 10.1, 8.0, 3.1, 82, 82, Arrays.asList(10.1)); break;
            case 2018: // 季中交易：TOT, SAS, MIL
		d.setAvailableTeams(Arrays.asList("TOT", "SAS", "MIL"));
                if ("SAS".equals(t)) setStats(d, "聖安東尼奧馬刺 (SAS)", 4.2, 4.7, 1.9, 75, 70, Arrays.asList(4.2));
                else if ("MIL".equals(t)) setStats(d, "密爾瓦基公鹿 (MIL)", 1.3, 3.3, 0.7, 75, 60, Arrays.asList(1.3));
                else setStats(d, "TOT (賽季總計)", 3.9, 4.6, 1.7, 75, 65, Arrays.asList(3.9));
                break;
        }
    }

// --- Victor Wembanyama ---
    private void handleWembanyama(PlayerDTO d, int y, String t) {
        if (y < 2023) { setStats(d, "尚未入盟", 0.0, 0.0, 0.0, 0, 0, Arrays.asList(0.0)); return; }
        switch (y) {
            case 2023: setStats(d, "聖安東尼奧馬刺 (SAS)", 21.4, 10.6, 3.9, 92, 98, Arrays.asList(21.4)); break;
            case 2024: setStats(d, "聖安東尼奧馬刺 (SAS)", 24.3, 11.0, 3.7, 95, 100, Arrays.asList(24.3)); break;
            case 2025: setStats(d, "聖安東尼奧馬刺 (SAS)", 25.0, 11.5, 3.1, 95, 100, Arrays.asList(25.0)); break;
        }
    }

// --- Anthony Davis ---
    private void handleDavis(PlayerDTO d, int y, String t) {
        if (y < 2012) { setStats(d, "尚未入盟", 0.0, 0.0, 0.0, 0, 0, Arrays.asList(0.0)); return; }
        switch (y) {
            case 2012: setStats(d, "新奧爾良黃蜂 (NOH)", 13.5, 8.2, 1.0, 80, 85, Arrays.asList(13.5)); break;
            case 2013: setStats(d, "新奧爾良鵜鶘 (NOP)", 20.8, 10.0, 1.6, 85, 90, Arrays.asList(20.8)); break;
            case 2014: setStats(d, "新奧爾良鵜鶘 (NOP)", 24.4, 10.2, 2.2, 88, 95, Arrays.asList(24.4)); break;
            case 2015: setStats(d, "新奧爾良鵜鶘 (NOP)", 24.3, 10.3, 1.9, 88, 94, Arrays.asList(24.3)); break;
            case 2016: setStats(d, "新奧爾良鵜鶘 (NOP)", 28.0, 11.8, 2.1, 90, 98, Arrays.asList(28.0)); break;
            case 2017: setStats(d, "新奧爾良鵜鶘 (NOP)", 28.1, 11.1, 2.3, 90, 98, Arrays.asList(28.1)); break;
            case 2018: setStats(d, "新奧爾良鵜鶘 (NOP)", 25.9, 12.0, 3.9, 88, 96, Arrays.asList(25.9)); break;
            case 2019: setStats(d, "洛杉磯湖人 (LAL)", 26.1, 9.3, 3.2, 92, 99, Arrays.asList(26.1)); break;
            case 2020: setStats(d, "洛杉磯湖人 (LAL)", 21.8, 7.9, 3.1, 90, 92, Arrays.asList(21.8)); break;
            case 2021: setStats(d, "洛杉磯湖人 (LAL)", 23.2, 9.9, 3.1, 90, 94, Arrays.asList(23.2)); break;
            case 2022: setStats(d, "洛杉磯湖人 (LAL)", 25.9, 12.5, 2.6, 92, 97, Arrays.asList(25.9)); break;
            case 2023: setStats(d, "洛杉磯湖人 (LAL)", 24.7, 12.6, 3.5, 92, 98, Arrays.asList(24.7)); break;
            case 2024: // 季中交易：TOT, LAL, DAL
		d.setAvailableTeams(Arrays.asList("TOT", "LAL", "DAL"));
                if ("LAL".equals(t)) setStats(d, "洛杉磯湖人 (LAL)", 25.7, 11.9, 3.4, 90, 96, Arrays.asList(25.7));
                else if ("DAL".equals(t)) setStats(d, "達拉斯獨行俠 (DAL)", 20.0, 10.1, 4.4, 85, 90, Arrays.asList(20.0));
                else setStats(d, "TOT (賽季總計)", 24.7, 11.6, 3.5, 88, 94, Arrays.asList(24.7));
                break;
            case 2025: setStats(d, "達拉斯獨行俠 (DAL)", 20.4, 11.1, 2.8, 85, 88, Arrays.asList(20.4)); break;
        }
    }

// --- Nikola Jokic ---
private void handleJokic(PlayerDTO d, int y, String t) {
    if (y < 2015) { setStats(d, "尚未入盟", 0.0, 0.0, 0.0, 0, 0, Arrays.asList(0.0)); return; }
    switch (y) {
        case 2015: setStats(d, "丹佛金塊 (DEN)", 10.0, 7.0, 2.4, 75, 82, Arrays.asList(10.0)); break;
        case 2016: setStats(d, "丹佛金塊 (DEN)", 16.7, 9.8, 4.9, 78, 88, Arrays.asList(16.7)); break;
        case 2017: setStats(d, "丹佛金塊 (DEN)", 18.5, 10.7, 6.1, 80, 92, Arrays.asList(18.5)); break;
        case 2018: setStats(d, "丹佛金塊 (DEN)", 20.1, 10.8, 7.3, 82, 94, Arrays.asList(20.1)); break;
        case 2019: setStats(d, "丹佛金塊 (DEN)", 19.9, 9.7, 7.0, 82, 95, Arrays.asList(19.9)); break;
        case 2020: setStats(d, "丹佛金塊 (DEN)", 26.4, 10.8, 8.3, 85, 98, Arrays.asList(26.4)); break;
        case 2021: setStats(d, "丹佛金塊 (DEN)", 27.1, 13.8, 7.9, 88, 100, Arrays.asList(27.1)); break;
        case 2022: setStats(d, "丹佛金塊 (DEN)", 24.5, 11.8, 9.8, 88, 99, Arrays.asList(24.5)); break;
        case 2023: setStats(d, "丹佛金塊 (DEN)", 26.4, 12.4, 9.0, 90, 100, Arrays.asList(26.4)); break;
        case 2024: setStats(d, "丹佛金塊 (DEN)", 29.6, 12.7, 10.2, 92, 100, Arrays.asList(29.6)); break;
        case 2025: setStats(d, "丹佛金塊 (DEN)", 27.7, 12.9, 10.7, 92, 100, Arrays.asList(27.7)); break;
    }
}

// --- Giannis Antetokounmpo ---
private void handleGiannis(PlayerDTO d, int y, String t) {
    if (y < 2013) { setStats(d, "尚未入盟", 0.0, 0.0, 0.0, 0, 0, Arrays.asList(0.0)); return; }
    switch (y) {
        case 2013: setStats(d, "密爾瓦基公鹿 (MIL)", 6.8, 4.4, 1.9, 75, 75, Arrays.asList(6.8)); break;
        case 2014: setStats(d, "密爾瓦基公鹿 (MIL)", 12.7, 6.7, 2.6, 78, 80, Arrays.asList(12.7)); break;
        case 2015: setStats(d, "密爾瓦基公鹿 (MIL)", 16.9, 7.7, 4.3, 82, 85, Arrays.asList(16.9)); break;
        case 2016: setStats(d, "密爾瓦基公鹿 (MIL)", 22.9, 8.8, 5.4, 88, 92, Arrays.asList(22.9)); break;
        case 2017: setStats(d, "密爾瓦基公鹿 (MIL)", 26.9, 10.0, 4.8, 90, 95, Arrays.asList(26.9)); break;
        case 2018: setStats(d, "密爾瓦基公鹿 (MIL)", 27.7, 12.5, 5.9, 92, 98, Arrays.asList(27.7)); break;
        case 2019: setStats(d, "密爾瓦基公鹿 (MIL)", 29.5, 13.6, 5.6, 95, 100, Arrays.asList(29.5)); break;
        case 2020: setStats(d, "密爾瓦基公鹿 (MIL)", 28.1, 11.0, 5.9, 95, 100, Arrays.asList(28.1)); break;
        case 2021: setStats(d, "密爾瓦基公鹿 (MIL)", 29.9, 11.6, 5.8, 95, 98, Arrays.asList(29.9)); break;
        case 2022: setStats(d, "密爾瓦基公鹿 (MIL)", 31.1, 11.8, 5.7, 92, 99, Arrays.asList(31.1)); break;
        case 2023: setStats(d, "密爾瓦基公鹿 (MIL)", 30.4, 11.5, 6.5, 90, 100, Arrays.asList(30.4)); break;
        case 2024: setStats(d, "密爾瓦基公鹿 (MIL)", 30.4, 11.9, 6.5, 90, 100, Arrays.asList(30.4)); break;
        case 2025: setStats(d, "密爾瓦基公鹿 (MIL)", 27.6, 9.8, 5.4, 90, 95, Arrays.asList(27.6)); break;
    }
}

// --- Jason Terry ---
    private void handleTerry(PlayerDTO d, int y, String t) {
        if (y < 1999) { setStats(d, "尚未入盟", 0.0, 0.0, 0.0, 0, 0, Arrays.asList(0.0)); return; }
	if (y > 2017) { setStats(d, "密爾瓦基公鹿 (已退役)", 3.3, 0.9, 1.2, 60, 60, Arrays.asList(3.3)); return; }
        switch (y) {
            case 2000: setStats(d, "亞特蘭大老鷹 (ATL)", 19.7, 3.3, 4.9, 70, 85, Arrays.asList(19.7)); break;
            case 2001: setStats(d, "亞特蘭大老鷹 (ATL)", 19.3, 3.5, 5.7, 72, 85, Arrays.asList(19.3)); break;
            case 2002: setStats(d, "亞特蘭大老鷹 (ATL)", 17.2, 3.4, 7.4, 72, 88, Arrays.asList(17.2)); break;
            case 2003: setStats(d, "亞特蘭大老鷹 (ATL)", 16.8, 4.1, 5.4, 70, 84, Arrays.asList(16.8)); break;
            case 2004: setStats(d, "達拉斯獨行俠 (DAL)", 12.4, 2.4, 5.4, 75, 82, Arrays.asList(12.4)); break;
            case 2005: setStats(d, "達拉斯獨行俠 (DAL)", 17.1, 2.0, 3.8, 78, 88, Arrays.asList(17.1)); break;
            case 2006: setStats(d, "達拉斯獨行俠 (DAL)", 16.7, 2.9, 5.2, 78, 88, Arrays.asList(16.7)); break;
            case 2007: setStats(d, "達拉斯獨行俠 (DAL)", 15.5, 2.5, 3.2, 75, 85, Arrays.asList(15.5)); break;
            case 2008: setStats(d, "達拉斯獨行俠 (DAL)", 19.6, 2.4, 3.4, 75, 92, Arrays.asList(19.6)); break;
            case 2009: setStats(d, "達拉斯獨行俠 (DAL)", 16.6, 1.8, 3.8, 72, 85, Arrays.asList(16.6)); break;
            case 2010: setStats(d, "達拉斯獨行俠 (DAL)", 15.8, 1.9, 4.1, 72, 85, Arrays.asList(15.8)); break;
            case 2011: setStats(d, "達拉斯獨行俠 (DAL)", 15.1, 2.4, 3.6, 70, 84, Arrays.asList(15.1)); break;
            case 2012: setStats(d, "波士頓塞爾提克 (BOS)", 10.1, 2.0, 2.5, 68, 75, Arrays.asList(10.1)); break;
            case 2013: setStats(d, "布魯克林籃網 (BKN)", 4.5, 1.1, 1.6, 65, 65, Arrays.asList(4.5)); break;
            case 2014: setStats(d, "休士頓火箭 (HOU)", 7.0, 1.6, 1.9, 68, 70, Arrays.asList(7.0)); break;
            case 2015: setStats(d, "休士頓火箭 (HOU)", 5.9, 1.1, 1.4, 65, 68, Arrays.asList(5.9)); break;
            case 2016: setStats(d, "密爾瓦基公鹿 (MIL)", 4.1, 1.4, 1.3, 65, 65, Arrays.asList(4.1)); break;
            case 2017: setStats(d, "密爾瓦基公鹿 (MIL)", 3.3, 0.9, 1.2, 60, 60, Arrays.asList(3.3)); break;
        }
    }

// --- Amar'e Stoudemire ---
    private void handleStoudemire(PlayerDTO d, int y, String t) {
        if (y < 2002) { setStats(d, "尚未入盟", 0.0, 0.0, 0.0, 0, 0, Arrays.asList(0.0)); return; }
	if (y > 2015) { setStats(d, "邁阿密熱火 (已退役)", 5.8, 4.3, 0.5, 65, 68, Arrays.asList(5.8)); return; }
        switch (y) {
            case 2002: setStats(d, "鳳凰城太陽 (PHX)", 13.5, 8.8, 1.0, 75, 82, Arrays.asList(13.5)); break;
            case 2003: setStats(d, "鳳凰城太陽 (PHX)", 20.6, 9.0, 1.4, 78, 88, Arrays.asList(20.6)); break;
            case 2004: setStats(d, "鳳凰城太陽 (PHX)", 26.0, 8.9, 1.6, 78, 95, Arrays.asList(26.0)); break;
            case 2005: setStats(d, "鳳凰城太陽 (PHX)", 8.7, 5.3, 0.7, 70, 75, Arrays.asList(8.7)); break;
            case 2006: setStats(d, "鳳凰城太陽 (PHX)", 20.4, 9.6, 1.0, 78, 90, Arrays.asList(20.4)); break;
            case 2007: setStats(d, "鳳凰城太陽 (PHX)", 25.2, 9.1, 1.5, 80, 96, Arrays.asList(25.2)); break;
            case 2008: setStats(d, "鳳凰城太陽 (PHX)", 21.4, 8.1, 2.0, 78, 92, Arrays.asList(21.4)); break;
            case 2009: setStats(d, "鳳凰城太陽 (PHX)", 23.1, 8.9, 1.0, 78, 94, Arrays.asList(23.1)); break;
            case 2010: setStats(d, "紐約尼克 (NYK)", 25.3, 8.2, 2.6, 75, 96, Arrays.asList(25.3)); break;
            case 2011: setStats(d, "紐約尼克 (NYK)", 17.5, 7.8, 1.1, 72, 85, Arrays.asList(17.5)); break;
            case 2012: setStats(d, "紐約尼克 (NYK)", 14.2, 5.0, 0.4, 70, 78, Arrays.asList(14.2)); break;
            case 2013: setStats(d, "紐約尼克 (NYK)", 11.9, 4.9, 0.5, 68, 75, Arrays.asList(11.9)); break;
            case 2014: // 季中交易：TOT, DAL, NYK
		d.setAvailableTeams(Arrays.asList("TOT", "DAL", "NYK"));
                if ("DAL".equals(t)) setStats(d, "達拉斯獨行俠 (DAL)", 10.8, 3.7, 0.3, 65, 75, Arrays.asList(10.8));
                else if ("NYK".equals(t)) setStats(d, "紐約尼克 (NYK)", 12.0, 6.8, 1.0, 68, 78, Arrays.asList(12.0));
                else setStats(d, "TOT (賽季總計)", 11.5, 5.6, 0.8, 68, 77, Arrays.asList(11.5));
                break;
            case 2015: setStats(d, "邁阿密熱火 (MIA)", 5.8, 4.3, 0.5, 65, 68, Arrays.asList(5.8)); break;
        }
    }

// --- Steve Nash ---
    private void handleNash(PlayerDTO d, int y, String t) {
        if (y < 1996) { setStats(d, "尚未入盟", 0.0, 0.0, 0.0, 0, 0, Arrays.asList(0.0)); return; }
	if (y > 2013) { setStats(d, "洛杉磯湖人 (已退役)", 6.8, 1.9, 5.7, 55, 70, Arrays.asList(6.8)); return; }
        switch (y) {
            case 2000: setStats(d, "達拉斯獨行俠 (DAL)", 15.6, 3.2, 7.3, 75, 88, Arrays.asList(15.6)); break;
            case 2001: setStats(d, "達拉斯獨行俠 (DAL)", 17.9, 3.1, 7.7, 75, 90, Arrays.asList(17.9)); break;
            case 2002: setStats(d, "達拉斯獨行俠 (DAL)", 17.7, 2.9, 7.3, 75, 90, Arrays.asList(17.7)); break;
            case 2003: setStats(d, "達拉斯獨行俠 (DAL)", 14.5, 3.0, 8.8, 75, 92, Arrays.asList(14.5)); break;
            case 2004: setStats(d, "鳳凰城太陽 (PHX)", 15.5, 3.3, 11.5, 72, 100, Arrays.asList(15.5)); break;
            case 2005: setStats(d, "鳳凰城太陽 (PHX)", 18.8, 4.2, 10.5, 72, 100, Arrays.asList(18.8)); break;
            case 2006: setStats(d, "鳳凰城太陽 (PHX)", 18.6, 3.5, 11.6, 72, 98, Arrays.asList(18.6)); break;
            case 2007: setStats(d, "鳳凰城太陽 (PHX)", 16.9, 3.5, 11.1, 70, 96, Arrays.asList(16.9)); break;
            case 2008: setStats(d, "鳳凰城太陽 (PHX)", 15.7, 3.0, 9.7, 70, 94, Arrays.asList(15.7)); break;
            case 2009: setStats(d, "鳳凰城太陽 (PHX)", 16.5, 3.3, 11.0, 70, 95, Arrays.asList(16.5)); break;
            case 2010: setStats(d, "鳳凰城太陽 (PHX)", 14.7, 3.5, 11.4, 68, 92, Arrays.asList(14.7)); break;
            case 2011: setStats(d, "鳳凰城太陽 (PHX)", 12.5, 3.0, 10.7, 65, 88, Arrays.asList(12.5)); break;
            case 2012: setStats(d, "洛杉磯湖人 (LAL)", 12.7, 2.8, 6.7, 60, 82, Arrays.asList(12.7)); break;
            case 2013: setStats(d, "洛杉磯湖人 (LAL)", 6.8, 1.9, 5.7, 55, 70, Arrays.asList(6.8)); break;
        }
    }
    // --- Luka Doncic ---
    private void handleDoncic(PlayerDTO d, int y, String t) {
        if (y < 2018) { setStats(d, "尚未入盟", 0, 0, 0, 0, 0, Arrays.asList(0.0)); return; }
        switch (y) {
            case 2018: setStats(d, "達拉斯獨行俠 (DAL)", 21.2, 7.8, 6.0, 75, 88, Arrays.asList(21.2)); break;
            case 2019: setStats(d, "達拉斯獨行俠 (DAL)", 28.8, 9.4, 8.8, 78, 96, Arrays.asList(28.8)); break;
            case 2020: setStats(d, "達拉斯獨行俠 (DAL)", 27.7, 8.0, 8.6, 78, 95, Arrays.asList(27.7)); break;
            case 2021: setStats(d, "達拉斯獨行俠 (DAL)", 28.4, 9.1, 8.7, 80, 96, Arrays.asList(28.4)); break;
            case 2022: setStats(d, "達拉斯獨行俠 (DAL)", 32.4, 8.6, 8.0, 82, 98, Arrays.asList(32.4)); break;
            case 2023: setStats(d, "達拉斯獨行俠 (DAL)", 33.9, 9.2, 9.8, 85, 100, Arrays.asList(33.9)); break;
            case 2024: // 季中交易
		d.setAvailableTeams(Arrays.asList("TOT", "LAL", "DAL"));
                if ("LAL".equals(t)) setStats(d, "洛杉磯湖人 (LAL)", 28.2, 8.1, 7.5, 82, 95, Arrays.asList(28.2));
                else if ("DAL".equals(t)) setStats(d, "達拉斯獨行俠 (DAL)", 28.1, 8.3, 7.8, 82, 95, Arrays.asList(28.1));
                else setStats(d, "TOT (賽季總計)", 28.2, 8.2, 7.7, 82, 95, Arrays.asList(28.2));
                break;
            case 2025: setStats(d, "洛杉磯湖人 (LAL)", 33.5, 7.7, 8.3, 85, 99, Arrays.asList(33.5)); break;
        }
    }

    // --- Dirk Nowitzki ---
    private void handleNowitzki(PlayerDTO d, int y, String t) {
        if (y < 1998) { setStats(d, "尚未入盟", 0, 0, 0, 0, 0, Arrays.asList(0.0)); return; }
	if (y > 2018) { setStats(d, "達拉斯獨行俠 (已退役)", 7.3, 3.1, 0.7, 60, 68, Arrays.asList(7.3)); return; }
        switch (y) {
            case 2000: setStats(d, "達拉斯獨行俠 (DAL)", 21.8, 9.2, 2.1, 80, 88, Arrays.asList(21.8)); break;
            case 2001: setStats(d, "達拉斯獨行俠 (DAL)", 23.4, 9.9, 2.4, 82, 92, Arrays.asList(23.4)); break;
            case 2002: setStats(d, "達拉斯獨行俠 (DAL)", 25.1, 9.9, 3.0, 82, 95, Arrays.asList(25.1)); break;
            case 2003: setStats(d, "達拉斯獨行俠 (DAL)", 21.8, 8.7, 2.7, 80, 88, Arrays.asList(21.8)); break;
            case 2004: setStats(d, "達拉斯獨行俠 (DAL)", 26.1, 9.7, 3.1, 82, 96, Arrays.asList(26.1)); break;
            case 2005: setStats(d, "達拉斯獨行俠 (DAL)", 26.6, 9.0, 2.8, 82, 97, Arrays.asList(26.6)); break;
            case 2006: setStats(d, "達拉斯獨行俠 (DAL)", 24.6, 8.9, 3.4, 82, 98, Arrays.asList(24.6)); break;
            case 2007: setStats(d, "達拉斯獨行俠 (DAL)", 23.6, 8.6, 3.5, 80, 94, Arrays.asList(23.6)); break;
            case 2008: setStats(d, "達拉斯獨行俠 (DAL)", 25.9, 8.4, 2.4, 80, 95, Arrays.asList(25.9)); break;
            case 2009: setStats(d, "達拉斯獨行俠 (DAL)", 25.0, 7.7, 2.7, 78, 94, Arrays.asList(25.0)); break;
            case 2010: setStats(d, "達拉斯獨行俠 (DAL)", 23.0, 7.0, 2.6, 78, 98, Arrays.asList(23.0)); break;
            case 2011: setStats(d, "達拉斯獨行俠 (DAL)", 21.6, 6.7, 2.2, 75, 92, Arrays.asList(21.6)); break;
            case 2012: setStats(d, "達拉斯獨行俠 (DAL)", 17.3, 6.8, 2.5, 72, 85, Arrays.asList(17.3)); break;
            case 2013: setStats(d, "達拉斯獨行俠 (DAL)", 21.7, 6.2, 2.7, 72, 88, Arrays.asList(21.7)); break;
            case 2014: setStats(d, "達拉斯獨行俠 (DAL)", 17.3, 5.9, 1.9, 70, 82, Arrays.asList(17.3)); break;
            case 2015: setStats(d, "達拉斯獨行俠 (DAL)", 18.3, 6.5, 1.8, 70, 84, Arrays.asList(18.3)); break;
            case 2016: setStats(d, "達拉斯獨行俠 (DAL)", 14.2, 6.5, 1.5, 68, 78, Arrays.asList(14.2)); break;
            case 2017: setStats(d, "達拉斯獨行俠 (DAL)", 12.0, 5.7, 1.6, 65, 75, Arrays.asList(12.0)); break;
            case 2018: setStats(d, "達拉斯獨行俠 (DAL)", 7.3, 3.1, 0.7, 60, 68, Arrays.asList(7.3)); break;
        }
    }

    // --- Tony Parker ---
    private void handleParker(PlayerDTO d, int y, String t) {
        if (y < 2001) { setStats(d, "尚未入盟", 0, 0, 0, 0, 0, Arrays.asList(0.0)); return; }
	if (y > 2018) { setStats(d, "夏洛特黃蜂 (已退役)", 9.5, 1.5, 3.7, 68, 72, Arrays.asList(9.5)); return; }
        switch (y) {
            case 2001: setStats(d, "聖安東尼奧馬刺 (SAS)", 9.2, 2.6, 4.3, 75, 78, Arrays.asList(9.2)); break;
            case 2002: setStats(d, "聖安東尼奧馬刺 (SAS)", 15.5, 2.6, 5.3, 78, 85, Arrays.asList(15.5)); break;
            case 2003: setStats(d, "聖安東尼奧馬刺 (SAS)", 14.7, 3.2, 5.5, 78, 82, Arrays.asList(14.7)); break;
            case 2004: setStats(d, "聖安東尼奧馬刺 (SAS)", 16.6, 3.7, 6.1, 80, 88, Arrays.asList(16.6)); break;
            case 2005: setStats(d, "聖安東尼奧馬刺 (SAS)", 18.9, 3.3, 5.8, 82, 90, Arrays.asList(18.9)); break;
            case 2006: setStats(d, "聖安東尼奧馬刺 (SAS)", 18.6, 3.2, 5.5, 82, 88, Arrays.asList(18.6)); break;
            case 2007: setStats(d, "聖安東尼奧馬刺 (SAS)", 18.8, 3.2, 6.0, 82, 90, Arrays.asList(18.8)); break;
            case 2008: setStats(d, "聖安東尼奧馬刺 (SAS)", 22.0, 3.1, 6.9, 82, 94, Arrays.asList(22.0)); break;
            case 2009: setStats(d, "聖安東尼奧馬刺 (SAS)", 16.0, 2.4, 5.7, 80, 85, Arrays.asList(16.0)); break;
            case 2010: setStats(d, "聖安東尼奧馬刺 (SAS)", 17.5, 3.1, 6.6, 80, 88, Arrays.asList(17.5)); break;
            case 2011: setStats(d, "聖安東尼奧馬刺 (SAS)", 18.3, 2.9, 7.7, 80, 92, Arrays.asList(18.3)); break;
            case 2012: setStats(d, "聖安東尼奧馬刺 (SAS)", 20.3, 3.0, 7.6, 80, 95, Arrays.asList(20.3)); break;
            case 2013: setStats(d, "聖安東尼奧馬刺 (SAS)", 16.7, 2.3, 5.7, 78, 88, Arrays.asList(16.7)); break;
            case 2014: setStats(d, "聖安東尼奧馬刺 (SAS)", 14.4, 1.9, 4.9, 75, 82, Arrays.asList(14.4)); break;
            case 2015: setStats(d, "聖安東尼奧馬刺 (SAS)", 11.9, 2.4, 5.3, 75, 80, Arrays.asList(11.9)); break;
            case 2016: setStats(d, "聖安東尼奧馬刺 (SAS)", 10.1, 1.8, 4.5, 72, 75, Arrays.asList(10.1)); break;
            case 2017: setStats(d, "聖安東尼奧馬刺 (SAS)", 7.7, 1.7, 3.5, 70, 70, Arrays.asList(7.7)); break;
            case 2018: setStats(d, "夏洛特黃蜂 (CHA)", 9.5, 1.5, 3.7, 68, 72, Arrays.asList(9.5)); break;
        }
    }

    // --- Tim Duncan ---
    private void handleDuncan(PlayerDTO d, int y, String t) {
        if (y < 1997) { setStats(d, "尚未入盟", 0, 0, 0, 0, 0, Arrays.asList(0.0)); return; }
	if (y > 2015) { setStats(d, "聖安東尼奧馬刺 (已退役)", 8.6, 7.3, 2.7, 75, 75, Arrays.asList(8.6)); return; }
        switch (y) {
            case 2000: setStats(d, "聖安東尼奧馬刺 (SAS)", 22.2, 12.2, 3.0, 95, 98, Arrays.asList(22.2)); break;
            case 2001: setStats(d, "聖安東尼奧馬刺 (SAS)", 25.5, 12.7, 3.7, 96, 100, Arrays.asList(25.5)); break;
            case 2002: setStats(d, "聖安東尼奧馬刺 (SAS)", 23.3, 12.9, 3.9, 96, 100, Arrays.asList(23.3)); break;
            case 2003: setStats(d, "聖安東尼奧馬刺 (SAS)", 22.3, 12.4, 3.1, 94, 98, Arrays.asList(22.3)); break;
            case 2004: setStats(d, "聖安東尼奧馬刺 (SAS)", 20.3, 11.1, 2.7, 94, 95, Arrays.asList(20.3)); break;
            case 2005: setStats(d, "聖安東尼奧馬刺 (SAS)", 18.6, 11.0, 3.2, 92, 92, Arrays.asList(18.6)); break;
            case 2006: setStats(d, "聖安東尼奧馬刺 (SAS)", 20.0, 10.6, 3.4, 92, 95, Arrays.asList(20.0)); break;
            case 2007: setStats(d, "聖安東尼奧馬刺 (SAS)", 19.3, 11.3, 2.8, 92, 94, Arrays.asList(19.3)); break;
            case 2008: setStats(d, "聖安東尼奧馬刺 (SAS)", 19.3, 10.7, 3.5, 90, 94, Arrays.asList(19.3)); break;
            case 2009: setStats(d, "聖安東尼奧馬刺 (SAS)", 17.9, 10.1, 3.2, 88, 90, Arrays.asList(17.9)); break;
            case 2010: setStats(d, "聖安東尼奧馬刺 (SAS)", 13.4, 8.9, 2.7, 85, 85, Arrays.asList(13.4)); break;
            case 2011: setStats(d, "聖安東尼奧馬刺 (SAS)", 15.4, 9.0, 2.3, 85, 88, Arrays.asList(15.4)); break;
            case 2012: setStats(d, "聖安東尼奧馬刺 (SAS)", 17.8, 9.9, 2.7, 88, 92, Arrays.asList(17.8)); break;
            case 2013: setStats(d, "聖安東尼奧馬刺 (SAS)", 15.1, 9.7, 3.0, 85, 90, Arrays.asList(15.1)); break;
            case 2014: setStats(d, "聖安東尼奧馬刺 (SAS)", 13.9, 9.1, 3.0, 82, 88, Arrays.asList(13.9)); break;
            case 2015: setStats(d, "聖安東尼奧馬刺 (SAS)", 8.6, 7.3, 2.7, 75, 75, Arrays.asList(8.6)); break;
        }
    }

    // --- Kyrie Irving ---
    private void handleKyrie(PlayerDTO d, int y, String t) {
        if (y < 2011) { setStats(d, "尚未入盟", 0, 0, 0, 0, 0, Arrays.asList(0.0)); return; }
        switch (y) {
            case 2014: setStats(d, "克里夫蘭騎士 (CLE)", 19.0, 3.6, 3.8, 70, 85, Arrays.asList(19.0)); break;
            case 2015: setStats(d, "克里夫蘭騎士 (CLE)", 25.2, 3.0, 4.7, 75, 92, Arrays.asList(25.2)); break;
            case 2016: setStats(d, "克里夫蘭騎士 (CLE)", 25.9, 2.8, 5.3, 75, 94, Arrays.asList(25.9)); break;
            case 2018: setStats(d, "波士頓塞爾提克 (BOS)", 21.3, 4.3, 7.0, 72, 90, Arrays.asList(21.3)); break;
            case 2020: setStats(d, "布魯克林籃網 (BKN)", 22.7, 5.8, 3.4, 68, 88, Arrays.asList(22.7)); break;
            case 2021: setStats(d, "布魯克林籃網 (BKN)", 21.3, 5.3, 5.3, 68, 86, Arrays.asList(21.3)); break;
            case 2023: setStats(d, "達拉斯獨行俠 (DAL)", 22.1, 3.7, 5.1, 70, 90, Arrays.asList(22.1)); break;
        }
    }

    // --- Klay Thompson ---
    private void handleKlay(PlayerDTO d, int y, String t) {
        if (y < 2011) { setStats(d, "尚未入盟", 0, 0, 0, 0, 0, Arrays.asList(0.0)); return; }
        switch (y) {
            case 2011: setStats(d, "金州勇士 (GSW)", 12.5, 2.4, 2.0, 75, 80, Arrays.asList(12.5)); break;
            case 2012: setStats(d, "金州勇士 (GSW)", 16.6, 3.7, 2.2, 78, 85, Arrays.asList(16.6)); break;
            case 2013: setStats(d, "金州勇士 (GSW)", 18.4, 3.1, 2.2, 80, 88, Arrays.asList(18.4)); break;
            case 2014: setStats(d, "金州勇士 (GSW)", 21.7, 3.2, 2.9, 82, 92, Arrays.asList(21.7)); break;
            case 2015: setStats(d, "金州勇士 (GSW)", 22.1, 3.8, 2.1, 85, 94, Arrays.asList(22.1)); break;
            case 2016: setStats(d, "金州勇士 (GSW)", 22.3, 3.7, 2.1, 85, 95, Arrays.asList(22.3)); break;
            case 2017: setStats(d, "金州勇士 (GSW)", 20.0, 3.8, 2.5, 85, 92, Arrays.asList(20.0)); break;
            case 2018: setStats(d, "金州勇士 (GSW)", 21.5, 3.8, 2.4, 82, 93, Arrays.asList(21.5)); break;
            case 2019: // 傷停 (ACL)
            case 2020: // 傷停 (Achilles)
                setStats(d, "金州勇士 (GSW)", 0.0, 0.0, 0.0, 0, 0, Arrays.asList(0.0)); break;
            case 2021: setStats(d, "金州勇士 (GSW)", 20.4, 3.9, 2.8, 75, 88, Arrays.asList(20.4)); break;
            case 2022: setStats(d, "金州勇士 (GSW)", 21.9, 4.1, 2.4, 72, 90, Arrays.asList(21.9)); break;
            case 2023: setStats(d, "金州勇士 (GSW)", 17.9, 3.3, 2.3, 70, 82, Arrays.asList(17.9)); break;
            case 2024: setStats(d, "達拉斯獨行俠 (DAL)", 14.0, 3.4, 2.0, 70, 80, Arrays.asList(14.0)); break;
            case 2025: setStats(d, "達拉斯獨行俠 (DAL)", 11.7, 2.1, 1.4, 68, 75, Arrays.asList(11.7)); break;
        }
    }

    // --- Dwyane Wade ---
    private void handleWade(PlayerDTO d, int y, String t) {
        if (y < 2003) { setStats(d, "尚未入盟", 0, 0, 0, 0, 0, Arrays.asList(0.0)); return; }
	if (y > 2018) { setStats(d, "邁阿密熱火 (已退役)", 15.0, 4.0, 4.2, 70, 78, Arrays.asList(15.0)); return; }
        switch (y) {
            case 2003: setStats(d, "邁阿密熱火 (MIA)", 16.2, 4.0, 4.5, 80, 85, Arrays.asList(16.2)); break;
            case 2004: setStats(d, "邁阿密熱火 (MIA)", 24.1, 5.2, 6.8, 85, 92, Arrays.asList(24.1)); break;
            case 2005: setStats(d, "邁阿密熱火 (MIA)", 27.2, 5.7, 6.7, 88, 98, Arrays.asList(27.2)); break;
            case 2006: setStats(d, "邁阿密熱火 (MIA)", 27.4, 4.7, 7.5, 88, 96, Arrays.asList(27.4)); break;
            case 2007: setStats(d, "邁阿密熱火 (MIA)", 24.6, 4.2, 6.9, 85, 93, Arrays.asList(24.6)); break;
            case 2008: setStats(d, "邁阿密熱火 (MIA)", 30.2, 5.0, 7.5, 90, 100, Arrays.asList(30.2)); break;
            case 2009: setStats(d, "邁阿密熱火 (MIA)", 26.6, 4.8, 6.5, 88, 97, Arrays.asList(26.6)); break;
            case 2010: setStats(d, "邁阿密熱火 (MIA)", 25.5, 6.4, 4.6, 92, 98, Arrays.asList(25.5)); break;
            case 2011: setStats(d, "邁阿密熱火 (MIA)", 22.1, 4.8, 4.6, 90, 95, Arrays.asList(22.1)); break;
            case 2012: setStats(d, "邁阿密熱火 (MIA)", 21.2, 5.0, 5.1, 88, 94, Arrays.asList(21.2)); break;
            case 2013: setStats(d, "邁阿密熱火 (MIA)", 19.0, 4.5, 4.7, 85, 90, Arrays.asList(19.0)); break;
            case 2014: setStats(d, "邁阿密熱火 (MIA)", 21.5, 3.5, 4.8, 80, 88, Arrays.asList(21.5)); break;
            case 2015: setStats(d, "邁阿密熱火 (MIA)", 19.0, 4.1, 4.6, 78, 85, Arrays.asList(19.0)); break;
            case 2016: setStats(d, "芝加哥公牛 (CHI)", 18.3, 4.5, 3.8, 75, 82, Arrays.asList(18.3)); break;
            case 2017: // 季中交易
		d.setAvailableTeams(Arrays.asList("TOT", "MIA", "CLE"));
                if ("CLE".equals(t)) setStats(d, "克里夫蘭騎士 (CLE)", 11.2, 3.9, 3.5, 72, 80, Arrays.asList(11.2));
                else if ("MIA".equals(t)) setStats(d, "邁阿密熱火 (MIA)", 12.0, 3.4, 3.1, 75, 82, Arrays.asList(12.0));
                else setStats(d, "TOT (賽季總計)", 11.4, 3.8, 3.4, 73, 81, Arrays.asList(11.4));
                break;
            case 2018: setStats(d, "邁阿密熱火 (MIA)", 15.0, 4.0, 4.2, 70, 78, Arrays.asList(15.0)); break;
        }
    }

    // --- Shaquille O'Neal ---
    private void handleShaq(PlayerDTO d, int y, String t) {
        if (y < 1992) { setStats(d, "尚未入盟", 0, 0, 0, 0, 0, Arrays.asList(0.0)); return; }
	if (y > 2010) { setStats(d, "波士頓塞爾提克 (已退役)", 9.2, 4.8, 0.7, 70, 70, Arrays.asList(9.2)); return; }
        switch (y) {
            case 2000: setStats(d, "洛杉磯湖人 (LAL)", 28.7, 12.7, 3.7, 95, 95, Arrays.asList(28.7)); break;
            case 2001: setStats(d, "洛杉磯湖人 (LAL)", 27.2, 10.7, 3.0, 92, 92, Arrays.asList(27.2)); break;
            case 2002: setStats(d, "洛杉磯湖人 (LAL)", 27.5, 11.1, 3.1, 92, 94, Arrays.asList(27.5)); break;
            case 2003: setStats(d, "洛杉磯湖人 (LAL)", 21.5, 11.5, 2.9, 88, 88, Arrays.asList(21.5)); break;
            case 2004: setStats(d, "邁阿密熱火 (MIA)", 22.9, 10.4, 2.7, 85, 90, Arrays.asList(22.9)); break;
            case 2005: setStats(d, "邁阿密熱火 (MIA)", 20.0, 9.2, 1.9, 82, 85, Arrays.asList(20.0)); break;
            case 2006: setStats(d, "邁阿密熱火 (MIA)", 17.3, 7.4, 2.0, 80, 80, Arrays.asList(17.3)); break;
            case 2007: // 季中交易
		d.setAvailableTeams(Arrays.asList("TOT", "MIA", "PHX"));
                if ("MIA".equals(t)) setStats(d, "邁阿密熱火 (MIA)", 14.2, 7.8, 1.4, 75, 75, Arrays.asList(14.2));
                else if ("PHX".equals(t)) setStats(d, "鳳凰城太陽 (PHX)", 12.9, 10.6, 1.7, 78, 78, Arrays.asList(12.9));
                else setStats(d, "TOT (賽季總計)", 13.6, 9.1, 1.5, 76, 76, Arrays.asList(13.6));
                break;
            case 2008: setStats(d, "鳳凰城太陽 (PHX)", 17.8, 8.4, 1.7, 78, 85, Arrays.asList(17.8)); break;
            case 2009: setStats(d, "克里夫蘭騎士 (CLE)", 12.0, 6.7, 1.5, 75, 75, Arrays.asList(12.0)); break;
            case 2010: setStats(d, "波士頓塞爾提克 (BOS)", 9.2, 4.8, 0.7, 70, 70, Arrays.asList(9.2)); break;
        }
    }

    // --- Russell Westbrook ---
    private void handleWestbrook(PlayerDTO d, int y, String t) {
        if (y < 2008) { setStats(d, "尚未入盟", 0, 0, 0, 0, 0, Arrays.asList(0.0)); return; }
        switch (y) {
            case 2008: setStats(d, "俄克拉荷馬雷霆 (OKC)", 15.3, 4.9, 5.3, 75, 78, Arrays.asList(15.3)); break;
            case 2009: setStats(d, "俄克拉荷馬雷霆 (OKC)", 16.1, 4.9, 8.0, 75, 80, Arrays.asList(16.1)); break;
            case 2010: setStats(d, "俄克拉荷馬雷霆 (OKC)", 21.9, 4.6, 8.2, 78, 85, Arrays.asList(21.9)); break;
            case 2011: setStats(d, "俄克拉荷馬雷霆 (OKC)", 23.6, 4.6, 5.5, 80, 88, Arrays.asList(23.6)); break;
            case 2012: setStats(d, "俄克拉荷馬雷霆 (OKC)", 23.2, 5.2, 7.4, 82, 90, Arrays.asList(23.2)); break;
            case 2013: setStats(d, "俄克拉荷馬雷霆 (OKC)", 21.8, 5.7, 6.9, 82, 88, Arrays.asList(21.8)); break;
            case 2014: setStats(d, "俄克拉荷馬雷霆 (OKC)", 28.1, 7.3, 8.6, 85, 94, Arrays.asList(28.1)); break;
            case 2015: setStats(d, "俄克拉荷馬雷霆 (OKC)", 23.5, 7.8, 10.4, 88, 92, Arrays.asList(23.5)); break;
            case 2016: setStats(d, "俄克拉荷馬雷霆 (OKC)", 31.6, 10.7, 10.4, 85, 100, Arrays.asList(31.6)); break;
            case 2017: setStats(d, "俄克拉荷馬雷霆 (OKC)", 25.4, 10.1, 10.3, 85, 96, Arrays.asList(25.4)); break;
            case 2018: setStats(d, "俄克拉荷馬雷霆 (OKC)", 22.9, 11.1, 10.7, 85, 95, Arrays.asList(22.9)); break;
            case 2019: setStats(d, "休士頓火箭 (HOU)", 27.2, 7.9, 7.0, 75, 94, Arrays.asList(27.2)); break;
            case 2020: setStats(d, "華盛頓巫師 (WAS)", 22.2, 11.5, 11.7, 72, 96, Arrays.asList(22.2)); break;
            case 2021: setStats(d, "洛杉磯湖人 (LAL)", 18.5, 7.4, 7.1, 70, 85, Arrays.asList(18.5)); break;
            case 2022:
		d.setAvailableTeams(Arrays.asList("TOT", "LAL", "LAC"));
                if ("LAC".equals(t)) setStats(d, "洛杉磯快艇 (LAC)", 15.8, 4.9, 7.6, 68, 86, Arrays.asList(15.8));
                else if ("LAL".equals(t)) setStats(d, "洛杉磯湖人 (LAL)", 15.9, 6.2, 7.5, 65, 84, Arrays.asList(15.9));
                else setStats(d, "TOT (賽季總計)", 15.9, 5.8, 7.5, 68, 85, Arrays.asList(15.9));
                break;
            case 2023: setStats(d, "洛杉磯快艇 (LAC)", 11.1, 5.0, 4.5, 70, 80, Arrays.asList(11.1)); break;
            case 2024: setStats(d, "丹佛金塊 (DEN)", 13.3, 4.9, 6.1, 72, 82, Arrays.asList(13.3)); break;
            case 2025: setStats(d, "沙加緬度國王 (SAC)", 15.2, 5.4, 6.7, 75, 84, Arrays.asList(15.2)); break;
        }
    }

    // --- Chris Paul ---
    private void handlePaul(PlayerDTO d, int y, String t) {
        if (y < 2005) { setStats(d, "尚未入盟", 0, 0, 0, 0, 0, Arrays.asList(0.0)); return; }
        switch (y) {
            case 2005: setStats(d, "新奧爾良黃蜂 (NOK)", 16.1, 5.1, 7.8, 82, 85, Arrays.asList(16.1)); break;
            case 2006: setStats(d, "新奧爾良黃蜂 (NOK)", 17.3, 4.4, 8.9, 82, 88, Arrays.asList(17.3)); break;
            case 2007: setStats(d, "新奧爾良黃蜂 (NOH)", 21.1, 4.0, 11.6, 88, 98, Arrays.asList(21.1)); break;
            case 2008: setStats(d, "新奧爾良黃蜂 (NOH)", 22.8, 5.5, 11.0, 90, 100, Arrays.asList(22.8)); break;
            case 2009: setStats(d, "新奧爾良黃蜂 (NOH)", 18.7, 4.2, 10.7, 85, 95, Arrays.asList(18.7)); break;
            case 2010: setStats(d, "新奧爾良黃蜂 (NOH)", 15.9, 4.1, 9.8, 85, 92, Arrays.asList(15.9)); break;
            case 2011: setStats(d, "洛杉磯快艇 (LAC)", 19.8, 3.6, 9.1, 88, 96, Arrays.asList(19.8)); break;
            case 2012: setStats(d, "洛杉磯快艇 (LAC)", 16.9, 3.7, 9.7, 88, 95, Arrays.asList(16.9)); break;
            case 2013: setStats(d, "洛杉磯快艇 (LAC)", 19.1, 4.3, 10.7, 88, 97, Arrays.asList(19.1)); break;
            case 2014: setStats(d, "洛杉磯快艇 (LAC)", 19.1, 4.6, 10.2, 86, 96, Arrays.asList(19.1)); break;
            case 2015: setStats(d, "洛杉磯快艇 (LAC)", 19.5, 4.2, 10.0, 86, 98, Arrays.asList(19.5)); break;
            case 2016: setStats(d, "洛杉磯快艇 (LAC)", 18.1, 5.0, 9.2, 85, 95, Arrays.asList(18.1)); break;
            case 2017: setStats(d, "休士頓火箭 (HOU)", 18.6, 5.4, 7.9, 82, 94, Arrays.asList(18.6)); break;
            case 2018: setStats(d, "休士頓火箭 (HOU)", 15.6, 4.6, 8.2, 80, 92, Arrays.asList(15.6)); break;
            case 2019: setStats(d, "俄克拉荷馬雷霆 (OKC)", 17.6, 5.0, 6.7, 82, 93, Arrays.asList(17.6)); break;
            case 2020: setStats(d, "鳳凰城太陽 (PHX)", 16.4, 4.5, 8.9, 78, 94, Arrays.asList(16.4)); break;
            case 2021: setStats(d, "鳳凰城太陽 (PHX)", 14.7, 4.4, 10.8, 75, 95, Arrays.asList(14.7)); break;
            case 2022: setStats(d, "鳳凰城太陽 (PHX)", 13.9, 4.3, 8.9, 72, 90, Arrays.asList(13.9)); break;
            case 2023: setStats(d, "金州勇士 (GSW)", 9.2, 3.9, 6.8, 70, 85, Arrays.asList(9.2)); break;
            case 2024: setStats(d, "聖安東尼奧馬刺 (SAS)", 8.8, 3.6, 7.4, 72, 84, Arrays.asList(8.8)); break;
            case 2025: setStats(d, "洛杉磯快艇 (LAC)", 2.9, 1.8, 3.3, 65, 75, Arrays.asList(2.9)); break;
        }
    }

    // --- Kevin Durant ---
    private void handleDurant(PlayerDTO d, int y, String t) {
        if (y < 2007) { setStats(d, "尚未入盟", 0, 0, 0, 0, 0, Arrays.asList(0.0)); return; }
        switch (y) {
            case 2007: setStats(d, "西雅圖超音速", 20.3, 4.4, 2.4, 75, 82, Arrays.asList(20.3)); break;
            case 2008: setStats(d, "俄克拉荷馬雷霆", 25.3, 6.5, 2.8, 78, 88, Arrays.asList(25.3)); break;
            case 2009: setStats(d, "俄克拉荷馬雷霆", 30.1, 7.6, 2.8, 80, 95, Arrays.asList(30.1)); break;
            case 2010: setStats(d, "俄克拉荷馬雷霆", 27.7, 6.8, 2.7, 82, 92, Arrays.asList(27.7)); break;
            case 2011: setStats(d, "俄克拉荷馬雷霆", 28.0, 8.0, 3.5, 85, 94, Arrays.asList(28.0)); break;
            case 2012: setStats(d, "俄克拉荷馬雷霆", 28.1, 7.9, 4.6, 85, 96, Arrays.asList(28.1)); break;
            case 2013: setStats(d, "俄克拉荷馬雷霆", 32.0, 7.4, 5.5, 85, 99, Arrays.asList(32.0)); break;
            case 2014: setStats(d, "俄克拉荷馬雷霆", 25.4, 6.6, 4.1, 82, 90, Arrays.asList(25.4)); break;
            case 2015: setStats(d, "俄克拉荷馬雷霆", 28.2, 8.2, 5.0, 85, 96, Arrays.asList(28.2)); break;
            case 2016: setStats(d, "金州勇士", 25.1, 8.3, 4.8, 88, 97, Arrays.asList(25.1)); break;
            case 2017: setStats(d, "金州勇士", 26.4, 6.8, 5.4, 88, 98, Arrays.asList(26.4)); break;
            case 2018: setStats(d, "金州勇士", 26.0, 6.4, 5.9, 88, 97, Arrays.asList(26.0)); break;
            case 2019: setStats(d, "布魯克林籃網", 0.0, 0.0, 0.0, 0, 0, Arrays.asList(0.0)); break;
            case 2020: setStats(d, "布魯克林籃網", 26.9, 7.1, 5.6, 80, 94, Arrays.asList(26.9)); break;
            case 2021: setStats(d, "布魯克林籃網", 29.9, 7.4, 6.4, 82, 96, Arrays.asList(29.9)); break;
            case 2022: setStats(d, "鳳凰城太陽", 29.1, 6.7, 5.0, 82, 96, Arrays.asList(29.1)); break;
            case 2023: setStats(d, "鳳凰城太陽", 27.1, 6.6, 5.0, 82, 95, Arrays.asList(27.1)); break;
            case 2024: setStats(d, "鳳凰城太陽", 26.6, 6.0, 4.2, 80, 93, Arrays.asList(26.6)); break;
            case 2025: setStats(d, "休士頓火箭", 26.0, 5.5, 4.8, 80, 92, Arrays.asList(26.0)); break;
        }
    }

    // --- Stephen Curry ---
    private void handleCurry(PlayerDTO d, int y, String t) {
        if (y < 2009) { setStats(d, "尚未入盟", 0, 0, 0, 0, 0, Arrays.asList(0.0)); return; }
        switch (y) {
            case 2009: setStats(d, "金州勇士", 17.5, 4.5, 5.9, 70, 88, Arrays.asList(17.5)); break;
            case 2010: setStats(d, "金州勇士", 18.6, 3.9, 5.8, 70, 90, Arrays.asList(18.6)); break;
            case 2011: setStats(d, "金州勇士", 14.7, 3.4, 5.3, 65, 80, Arrays.asList(14.7)); break;
            case 2012: setStats(d, "金州勇士", 22.9, 4.0, 6.9, 72, 92, Arrays.asList(22.9)); break;
            case 2013: setStats(d, "金州勇士", 24.0, 4.3, 8.5, 72, 95, Arrays.asList(24.0)); break;
            case 2014: setStats(d, "金州勇士", 23.8, 4.3, 7.7, 72, 98, Arrays.asList(23.8)); break;
            case 2015: setStats(d, "金州勇士", 30.1, 5.4, 6.7, 75, 100, Arrays.asList(30.1)); break;
            case 2016: setStats(d, "金州勇士", 25.3, 4.5, 6.6, 75, 96, Arrays.asList(25.3)); break;
            case 2017: setStats(d, "金州勇士", 26.4, 5.1, 6.1, 75, 97, Arrays.asList(26.4)); break;
            case 2018: setStats(d, "金州勇士", 27.3, 5.3, 5.2, 75, 96, Arrays.asList(27.3)); break;
            case 2019: setStats(d, "金州勇士", 20.8, 5.2, 6.6, 70, 90, Arrays.asList(20.8)); break;
            case 2020: setStats(d, "金州勇士", 32.0, 5.5, 5.8, 72, 99, Arrays.asList(32.0)); break;
            case 2021: setStats(d, "金州勇士", 25.5, 5.2, 6.3, 72, 96, Arrays.asList(25.5)); break;
            case 2022: setStats(d, "金州勇士", 29.4, 6.1, 6.3, 72, 98, Arrays.asList(29.4)); break;
            case 2023: setStats(d, "金州勇士", 26.4, 4.5, 5.1, 70, 94, Arrays.asList(26.4)); break;
            case 2024: setStats(d, "金州勇士", 24.5, 4.4, 6.0, 70, 92, Arrays.asList(24.5)); break;
            case 2025: setStats(d, "金州勇士", 26.6, 3.6, 4.7, 70, 93, Arrays.asList(26.6)); break;
        }
    }

    // --- Kobe Bryant ---
    private void handleKobe(PlayerDTO d, int y, String t) {
	if (y < 1996) { setStats(d, "尚未入盟", 0,0,0,0,0, Arrays.asList(0.0)); return; }
        if (y > 2015) { setStats(d, "洛杉磯湖人 (已退役)", 17.6, 3.7, 2.8, 65, 70, Arrays.asList(17.6)); return; }
        switch (y) {
            case 2000: setStats(d, "洛杉磯湖人", 28.5, 5.9, 5.0, 92, 90, Arrays.asList(28.5)); break;
            case 2001: setStats(d, "洛杉磯湖人", 25.2, 5.5, 5.5, 90, 88, Arrays.asList(25.2)); break;
            case 2002: setStats(d, "洛杉磯湖人", 30.0, 6.9, 5.9, 88, 96, Arrays.asList(30.0)); break;
            case 2003: setStats(d, "洛杉磯湖人", 24.0, 5.5, 5.1, 85, 82, Arrays.asList(24.0)); break;
            case 2004: setStats(d, "洛杉磯湖人", 27.6, 5.9, 6.0, 85, 88, Arrays.asList(27.6)); break;
            case 2005: setStats(d, "洛杉磯湖人", 35.4, 5.3, 4.5, 88, 98, Arrays.asList(35.4)); break;
            case 2006: setStats(d, "洛杉磯湖人", 31.6, 5.7, 5.4, 85, 95, Arrays.asList(31.6)); break;
            case 2007: setStats(d, "洛杉磯湖人", 28.3, 6.3, 5.4, 88, 93, Arrays.asList(28.3)); break;
            case 2008: setStats(d, "洛杉磯湖人", 26.8, 5.2, 4.9, 85, 90, Arrays.asList(26.8)); break;
            case 2009: setStats(d, "洛杉磯湖人", 27.0, 5.4, 5.0, 88, 92, Arrays.asList(27.0)); break;
            case 2010: setStats(d, "洛杉磯湖人", 25.3, 5.1, 4.7, 85, 88, Arrays.asList(25.3)); break;
            case 2011: setStats(d, "洛杉磯湖人", 27.9, 5.4, 4.6, 80, 85, Arrays.asList(27.9)); break;
            case 2012: setStats(d, "洛杉磯湖人", 27.3, 5.6, 6.0, 82, 86, Arrays.asList(27.3)); break;
            case 2013: setStats(d, "洛杉磯湖人", 13.8, 4.3, 6.3, 60, 65, Arrays.asList(13.8)); break;
            case 2014: setStats(d, "洛杉磯湖人", 22.3, 5.7, 5.6, 70, 75, Arrays.asList(22.3)); break;
            case 2015: setStats(d, "洛杉磯湖人", 17.6, 3.7, 2.8, 65, 70, Arrays.asList(17.6)); break;
        }
    }

    // --- LeBron James ---
    private void handleLeBron(PlayerDTO d, int y, String t) {
        if (y < 2003) { setStats(d, "尚未入盟", 0, 0, 0, 0, 0, Arrays.asList(0.0)); return; }
        switch (y) {
            case 2003: setStats(d, "克里夫蘭騎士", 20.9, 5.5, 5.9, 75, 82, Arrays.asList(20.9)); break;
            case 2004: setStats(d, "克里夫蘭騎士", 27.2, 7.4, 7.2, 80, 88, Arrays.asList(27.2)); break;
            case 2005: setStats(d, "克里夫蘭騎士", 31.4, 7.0, 6.6, 85, 95, Arrays.asList(31.4)); break;
            case 2006: setStats(d, "克里夫蘭騎士", 27.3, 6.7, 6.0, 88, 92, Arrays.asList(27.3)); break;
            case 2007: setStats(d, "克里夫蘭騎士", 30.0, 7.9, 7.2, 88, 98, Arrays.asList(30.0)); break;
            case 2008: setStats(d, "克里夫蘭騎士", 28.4, 7.6, 7.2, 90, 99, Arrays.asList(28.4)); break;
            case 2009: setStats(d, "克里夫蘭騎士", 29.7, 7.3, 8.6, 92, 99, Arrays.asList(29.7)); break;
            case 2010: setStats(d, "邁阿密熱火", 26.7, 7.5, 7.0, 94, 96, Arrays.asList(26.7)); break;
            case 2011: setStats(d, "邁阿密熱火", 27.1, 7.9, 6.2, 95, 98, Arrays.asList(27.1)); break;
            case 2012: setStats(d, "邁阿密熱火", 26.8, 8.0, 7.3, 96, 100, Arrays.asList(26.8)); break;
            case 2013: setStats(d, "邁阿密熱火", 27.1, 6.9, 6.3, 94, 98, Arrays.asList(27.1)); break;
            case 2014: setStats(d, "克里夫蘭騎士", 25.3, 6.0, 7.4, 82, 92, Arrays.asList(25.3)); break;
            case 2015: setStats(d, "克里夫蘭騎士", 25.3, 7.4, 6.8, 80, 93, Arrays.asList(25.3)); break;
            case 2016: setStats(d, "克里夫蘭騎士", 26.4, 8.6, 8.7, 85, 96, Arrays.asList(26.4)); break;
            case 2017: setStats(d, "克里夫蘭騎士", 27.5, 8.6, 9.1, 80, 97, Arrays.asList(27.5)); break;
            case 2018: setStats(d, "洛杉磯湖人", 27.4, 8.5, 8.3, 75, 95, Arrays.asList(27.4)); break;
            case 2019: setStats(d, "洛杉磯湖人", 25.3, 7.8, 10.2, 75, 96, Arrays.asList(25.3)); break;
            case 2020: setStats(d, "洛杉磯湖人", 25.0, 7.7, 7.8, 78, 94, Arrays.asList(25.0)); break;
            case 2021: setStats(d, "洛杉磯湖人", 30.3, 8.2, 6.2, 72, 96, Arrays.asList(30.3)); break;
            case 2022: setStats(d, "洛杉磯湖人", 28.9, 8.3, 6.8, 70, 95, Arrays.asList(28.9)); break;
            case 2023: setStats(d, "洛杉磯湖人", 25.7, 7.3, 8.3, 70, 94, Arrays.asList(25.7)); break;
            case 2024: setStats(d, "洛杉磯湖人", 25.0, 8.1, 7.4, 70, 92, Arrays.asList(25.0)); break;
            case 2025: setStats(d, "洛杉磯湖人", 24.1, 7.1, 8.2, 68, 90, Arrays.asList(24.1)); break;
        }
    }

    // --- James Harden ---
    private void handleHarden(PlayerDTO d, int y, String t) {
        if (y < 2009) { setStats(d, "尚未入盟", 0, 0, 0, 0, 0, Arrays.asList(0.0)); return; }
        switch (y) {
            case 2009: setStats(d, "俄克拉荷馬雷霆 (OKC)", 9.9, 3.2, 1.8, 70, 75, Arrays.asList(9.9)); break;
            case 2010: setStats(d, "俄克拉荷馬雷霆 (OKC)", 12.2, 3.1, 2.1, 72, 78, Arrays.asList(12.2)); break;
            case 2011: setStats(d, "俄克拉荷馬雷霆 (OKC)", 16.8, 4.1, 3.7, 75, 85, Arrays.asList(16.8)); break;
            case 2012: setStats(d, "休士頓火箭 (HOU)", 25.9, 4.9, 5.8, 78, 92, Arrays.asList(25.9)); break;
            case 2013: setStats(d, "休士頓火箭 (HOU)", 25.4, 4.7, 6.1, 75, 90, Arrays.asList(25.4)); break;
            case 2014: setStats(d, "休士頓火箭 (HOU)", 27.4, 5.7, 7.0, 78, 94, Arrays.asList(27.4)); break;
            case 2015: setStats(d, "休士頓火箭 (HOU)", 29.0, 6.1, 7.5, 80, 96, Arrays.asList(29.0)); break;
            case 2016: setStats(d, "休士頓火箭 (HOU)", 29.1, 8.1, 11.2, 75, 98, Arrays.asList(29.1)); break;
            case 2017: setStats(d, "休士頓火箭 (HOU)", 30.4, 5.4, 8.8, 78, 99, Arrays.asList(30.4)); break;
            case 2018: setStats(d, "休士頓火箭 (HOU)", 36.1, 6.6, 7.5, 75, 100, Arrays.asList(36.1)); break;
            case 2019: setStats(d, "休士頓火箭 (HOU)", 34.3, 6.6, 7.5, 78, 99, Arrays.asList(34.3)); break;
            case 2020:
		d.setAvailableTeams(Arrays.asList("TOT", "HOU", "BKN"));
                if ("BKN".equals(t)) setStats(d, "布魯克林籃網 (BKN)", 24.6, 8.5, 10.9, 72, 96, Arrays.asList(24.6));
                else if ("HOU".equals(t)) setStats(d, "休士頓火箭 (HOU)", 24.8, 5.1, 10.4, 70, 90, Arrays.asList(24.8));
                else setStats(d, "TOT (賽季總計)", 24.6, 7.9, 10.8, 75, 95, Arrays.asList(24.6));
                break;
            case 2021:
		d.setAvailableTeams(Arrays.asList("TOT", "BKN", "PHI"));
                if ("PHI".equals(t)) setStats(d, "費城 76 人 (PHI)", 21.0, 7.1, 10.5, 75, 90, Arrays.asList(21.0));
                else if ("BKN".equals(t)) setStats(d, "布魯克林籃網 (BKN)", 22.5, 8.0, 10.2, 72, 93, Arrays.asList(22.5));
                else setStats(d, "TOT (賽季總計)", 22.0, 7.7, 10.3, 75, 92, Arrays.asList(22.0));
                break;
            case 2022: setStats(d, "費城 76 人 (PHI)", 21.0, 6.1, 10.7, 75, 92, Arrays.asList(21.0)); break;
            case 2023: setStats(d, "洛杉磯快艇 (LAC)", 16.6, 5.1, 8.5, 72, 88, Arrays.asList(16.6)); break;
            case 2024: setStats(d, "洛杉磯快艇 (LAC)", 22.8, 5.8, 8.7, 75, 91, Arrays.asList(22.8)); break;
            case 2025:
		d.setAvailableTeams(Arrays.asList("TOT", "HOU", "CLE", "LAC"));
                if ("HOU".equals(t)) setStats(d, "休士頓火箭 (HOU)", 26.0, 5.5, 4.8, 80, 92, Arrays.asList(26.0));
                else if ("CLE".equals(t)) setStats(d, "克里夫蘭騎士 (CLE)", 20.5, 4.8, 7.7, 78, 88, Arrays.asList(20.5));
                else if ("LAC".equals(t)) setStats(d, "洛杉磯快艇 (LAC)", 25.4, 4.8, 8.1, 75, 94, Arrays.asList(25.4));
                else setStats(d, "TOT (賽季總計)", 23.6, 4.8, 8.0, 75, 92, Arrays.asList(23.6));
                break;
        }
    }

    private void analyzeStyle(PlayerDTO d, int year) {
        if (d.getPts() >= HIGH_PTS && d.getAst() >= 6 && d.getReb() >= 6) {
            d.setCoreStyle("🌟 頂級全能巨星");
            d.setSimpleRating("集得分、組織和籃板於一身。");
	    d.setCompareCategory("得分 (PTS)");
            d.setCompareValue(d.getPts());
        } else if (d.getPts() >= HIGH_PTS) {
            d.setCoreStyle("得分機器");
            d.setSimpleRating("聯盟頂級得分手。");
	    d.setCompareCategory("得分 (PTS)");
            d.setCompareValue(d.getPts());
        } else if (d.getAst() >= HIGH_AST) {
            d.setCoreStyle("🎯 組織大師");
            d.setSimpleRating("以傳球優先的組織核心。");
	    d.setCompareCategory("助攻 (AST)");
            d.setCompareValue(d.getAst());
	}else if (d.getReb() >= HIGH_REB || d.getDef() >= 80) { 
            d.setCoreStyle("🧱 籃板/防守支柱");
            d.setSimpleRating("球隊禁區或防守端的絕對核心。");
            d.setCompareCategory("籃板 (REB)");
            d.setCompareValue(d.getReb());
        } else {
            d.setCoreStyle("角色球員");
            d.setSimpleRating("一名可靠的輪換球員。");
	    d.setSimpleRating("無特別優異的單項數據，屬穩定輪換陣容。");
            d.setCompareCategory("NONE");
        }
		// 動態計算該賽季各數據之平均
        if (!"NONE".equals(d.getCompareCategory())) {
            if (d.getCompareCategory().contains("PTS")) d.setAverageValue(getSeasonAverage(year, "PTS"));
            else if (d.getCompareCategory().contains("AST")) d.setAverageValue(getSeasonAverage(year, "AST"));
            else if (d.getCompareCategory().contains("REB")) d.setAverageValue(getSeasonAverage(year, "REB"));
        }
    }

    private void setStats(PlayerDTO d, String team, double p, double r, double a, double def, double eff, List<Double> h) {
        d.setTeam(team);
        d.setPts(p);
        d.setReb(r);
        d.setAst(a);
        d.setDef(def);
        d.setEff(eff);
        d.setPtsHistory(h);
        d.setRadarData(Arrays.asList((int) (p * 3), (int) (r * 8), (int) (a * 10), (int) def, (int) eff));
    }
// 獲取球隊清單 
    public List<String> getTeams() {
        return Arrays.asList(
            "洛杉磯湖人", "達拉斯獨行俠", "鳳凰城太陽", 
            "密爾瓦基公鹿", "丹佛金塊", "聖安東尼奧馬刺",
            "金州勇士", "邁阿密熱火", "克里夫蘭騎士", "休士頓火箭"
        );
    }


    public List<String> getPlayersByTeamAndSeason(String team, String season) {
        int year = Integer.parseInt(season);
        List<String> p = new ArrayList<>();
        
        switch (team) {
            case "洛杉磯湖人":
                if (year <= 2004) { p.add("Kobe Bryant"); p.add("Shaquille O'Neal"); }
                else if (year <= 2013) { p.add("Kobe Bryant"); p.add("Pau Gasol"); p.add("Steve Nash"); }
                else if (year <= 2017) { p.add("Kobe Bryant"); } 
                else { p.add("LeBron James"); p.add("Anthony Davis"); p.add("Russell Westbrook"); }
                break;
                
            case "達拉斯獨行俠":
                if (year <= 2004) { p.add("Dirk Nowitzki"); p.add("Steve Nash"); }
                else if (year <= 2012) { p.add("Dirk Nowitzki"); p.add("Jason Terry"); }
                else if (year <= 2018) { p.add("Dirk Nowitzki"); p.add("Amar'e Stoudemire"); }
                else { p.add("Luka Doncic"); p.add("Kyrie Irving"); p.add("Klay Thompson"); p.add("Anthony Davis"); }
                break;
                
            case "鳳凰城太陽":
                if (year <= 2011) { p.add("Steve Nash"); p.add("Amar'e Stoudemire"); p.add("Shaquille O'Neal"); }
                else { p.add("Chris Paul"); p.add("Kevin Durant"); }
                break;
                
            case "密爾瓦基公鹿":
                p.add("Giannis Antetokounmpo");
                if (year >= 2016 && year <= 2018) { p.add("Jason Terry"); p.add("Pau Gasol"); }
                if (year >= 2023) p.add("Damian Lillard");
                break;
                
            case "丹佛金塊":
                if (year >= 2015) p.add("Nikola Jokic");
                if (year >= 2016) p.add("Jamal Murray");
                if (year >= 2024) p.add("Russell Westbrook");
                break;
                
            case "聖安東尼奧馬刺":
                if (year <= 2018) { p.add("Tim Duncan"); p.add("Tony Parker"); p.add("Pau Gasol"); }
                else { p.add("Victor Wembanyama"); p.add("Chris Paul"); }
                break;
                
            case "金州勇士":
                p.add("Stephen Curry");
                p.add("Klay Thompson");
                if (year >= 2016 && year <= 2018) p.add("Kevin Durant");
                if (year == 2023) p.add("Chris Paul");
                break;

            case "邁阿密熱火":
                p.add("Dwyane Wade");
                if (year <= 2007) p.add("Shaquille O'Neal");
                if (year >= 2010 && year <= 2014) p.add("LeBron James");
                if (year == 2015) p.add("Amar'e Stoudemire");
                break;

            case "克里夫蘭騎士":
                p.add("LeBron James");
                if (year >= 2011 && year <= 2017) p.add("Kyrie Irving");
                if (year == 2009) p.add("Shaquille O'Neal");
                if (year == 2017) p.add("Dwyane Wade");
                break;

            case "休士頓火箭":
                if (year >= 2012 && year <= 2020) p.add("James Harden");
                if (year >= 2014 && year <= 2015) p.add("Jason Terry");
                if (year >= 2017 && year <= 2018) p.add("Chris Paul");
                if (year == 2019) p.add("Russell Westbrook");
                break;
        }
        
        // 去除重複的球員
        return new ArrayList<>(new LinkedHashSet<>(p));
    }
}
