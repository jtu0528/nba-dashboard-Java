package com.tku.nba;
import java.util.List;

public class PlayerDTO {
    private String fullName;
    private String team;
    private String season;
    private double pts;
    private double reb;
    private double ast;
    private String coreStyle;
    private List<Double> ptsHistory;

    public PlayerDTO() {}
    // Getter & Setter 
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
    public String getCoreStyle() { return coreStyle; }
    public void setCoreStyle(String coreStyle) { this.coreStyle = coreStyle; }
    public List<Double> getPtsHistory() { return ptsHistory; }
    public void setPtsHistory(List<Double> ptsHistory) { this.ptsHistory = ptsHistory; }
}
