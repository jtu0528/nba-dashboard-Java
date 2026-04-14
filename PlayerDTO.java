package com.tku.nba.model;

import lombok.Data;

@Data
public class PlayerDTO {
    private String fullName;
    private String team;
    private double pts; // 場均得分
    private double reb; // 場均籃板
    private double ast; // 場均助攻
    private String style; // 你的分析邏輯：得分機器/全能巨星
    private String rating; // 評價
}
