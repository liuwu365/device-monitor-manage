package com.device.api.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HouseTrend implements Serializable {
    private Long id;

    private String area;

    private Integer newHousePrice;

    private Byte newHuanbi;

    private Double newHuanbiPercent;

    private Byte newTongbi;

    private Double newTonbiPercent;

    private Integer oldHousePrice;

    private Byte oldHuanbi;

    private Double oldHuanbiPercent;

    private Byte oldTongbi;

    private Double oldTongbiPercent;

    private Date createTime;

    private Date updateTime;


}