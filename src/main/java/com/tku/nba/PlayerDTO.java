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

    public PlayerDTO() {}

    // --- Getter & Setter (絕對不可缺少) ---

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }

    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }

    public double getPts() { return pts; }
    public void setPts(double pts) { this.pts = pts; }

    public double getReb() { return reb; }
    public void setReb(double reb) { this.reb = reb; }

    public double getAst() { return ast; }
    public void setAst(double ast) { this.ast = ast; }

    public double getDef() { return def; }
    public void setDef(double def) { this.def = def; }

    public double getEff() { return eff; }
    public void setEff(double eff) { this.eff = eff; }

    public String getCoreStyle() { return coreStyle; }
    public void setCoreStyle(String coreStyle) { this.coreStyle = coreStyle; }

    public String getSimpleRating() { return simpleRating; }
    public void setSimpleRating(String simpleRating) { this.simpleRating = simpleRating; }

    public List<Double> getPtsHistory() { return ptsHistory; }
    public void setPtsHistory(List<Double> ptsHistory) { this.ptsHistory = ptsHistory; }

    public List<Integer> getRadarData() { return radarData; }
    public void setRadarData(List<Integer> radarData) { this.radarData = radarData; }
}
