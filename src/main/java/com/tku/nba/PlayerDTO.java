package com.tku.nba;

import java.util.List;

public class PlayerDTO {
    private String fullName;
    private String team;
    private String season;
    private double pts;
    private double reb;
    private double ast;
    private double def;      
    private double eff;      
    
    private String coreStyle;
    private String simpleRating; 
    
    private List<Double> ptsHistory;
    private List<Integer> radarData; 
    
    // 多隊切換按鈕
    private List<String> availableTeams; 

    // 動態比較圖表
    private String compareCategory; 
    private double compareValue;    
    private double averageValue;    

    public PlayerDTO() {}
    
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getFullName() { return this.fullName; }

    public void setTeam(String team) { this.team = team; }
    public String getTeam() { return this.team; }

    public void setSeason(String season) { this.season = season; }
    public String getSeason() { return this.season; }

    public void setPts(double pts) { this.pts = pts; }
    public double getPts() { return this.pts; }

    public void setReb(double reb) { this.reb = reb; }
    public double getReb() { return this.reb; }

    public void setAst(double ast) { this.ast = ast; }
    public double getAst() { return this.ast; }

    public void setDef(double def) { this.def = def; }
    public double getDef() { return this.def; }

    public void setEff(double eff) { this.eff = eff; }
    public double getEff() { return this.eff; }

    public void setCoreStyle(String coreStyle) { this.coreStyle = coreStyle; }
    public String getCoreStyle() { return this.coreStyle; }

    public void setSimpleRating(String simpleRating) { this.simpleRating = simpleRating; }
    public String getSimpleRating() { return this.simpleRating; }

    public void setPtsHistory(List<Double> ptsHistory) { this.ptsHistory = ptsHistory; }
    public List<Double> getPtsHistory() { return this.ptsHistory; }

    public void setRadarData(List<Integer> radarData) { this.radarData = radarData; }
    public List<Integer> getRadarData() { return this.radarData; }

    public void setAvailableTeams(List<String> availableTeams) { this.availableTeams = availableTeams; }
    public List<String> getAvailableTeams() { return this.availableTeams; }

    public void setCompareCategory(String compareCategory) { this.compareCategory = compareCategory; }
    public String getCompareCategory() { return this.compareCategory; }

    public void setCompareValue(double compareValue) { this.compareValue = compareValue; }
    public double getCompareValue() { return this.compareValue; }

    public void setAverageValue(double averageValue) { this.averageValue = averageValue; }
    public double getAverageValue() { return this.averageValue; }
}
