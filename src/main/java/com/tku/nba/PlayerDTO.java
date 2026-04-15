package com.tku.nba;

public class PlayerDTO {
    private String fullName;
    private double pts;
    private double reb;
    private double ast;
    private String coreStyle;

    // Getter & Setter (一定要有，Spring 才能讀取數據)
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public double getPts() { return pts; }
    public void setPts(double pts) { this.pts = pts; }
    public double getReb() { return reb; }
    public void setReb(double reb) { this.reb = reb; }
    public double getAst() { return ast; }
    public void setAst(double ast) { this.ast = ast; }
    public String getCoreStyle() { return coreStyle; }
    public void setCoreStyle(String coreStyle) { this.coreStyle = coreStyle; }
}
