package com.device.api.entity.backrole;

import java.util.Date;

public class BackIpLimit {
    private Long id;

    private String ipAddress;

    private String addPeople;

    private Date createTime;

    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }

    public String getAddPeople() {
        return addPeople;
    }

    public void setAddPeople(String addPeople) {
        this.addPeople = addPeople == null ? null : addPeople.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }
}