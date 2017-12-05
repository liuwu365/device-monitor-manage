package com.device.api.entity.device;

import java.io.Serializable;
import java.util.Date;

public class DeviceWarnRecord implements Serializable {
    private Long id;

    private Long deviceId;

    private String item;

    private Double value;

    private Integer level;

    private Byte warnType;

    private Byte status;

    private Date handleStartTime;   //handle_start_time

    private Date handleEndTime;     //handle_end_time

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item == null ? null : item.trim();
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Byte getWarnType() {
        return warnType;
    }

    public void setWarnType(Byte warnType) {
        this.warnType = warnType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getHandleStartTime() {
        return handleStartTime;
    }

    public void setHandleStartTime(Date handleStartTime) {
        this.handleStartTime = handleStartTime;
    }

    public Date getHandleEndTime() {
        return handleEndTime;
    }

    public void setHandleEndTime(Date handleEndTime) {
        this.handleEndTime = handleEndTime;
    }
}