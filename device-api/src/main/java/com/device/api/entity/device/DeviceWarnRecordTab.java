package com.device.api.entity.device;

import com.device.api.entity.Page;

import java.io.Serializable;

/**
 * @Description:
 * @Date: 2017-12-04 14:43
 */
public class DeviceWarnRecordTab implements Serializable {

    Page<DeviceWarnRecord> pageWait;
    Page<DeviceWarnRecord> pageProcess;
    Page<DeviceWarnRecord> pageFinish;

    public Page<DeviceWarnRecord> getPageWait() {
        return pageWait;
    }

    public void setPageWait(Page<DeviceWarnRecord> pageWait) {
        this.pageWait = pageWait;
    }

    public Page<DeviceWarnRecord> getPageProcess() {
        return pageProcess;
    }

    public void setPageProcess(Page<DeviceWarnRecord> pageProcess) {
        this.pageProcess = pageProcess;
    }

    public Page<DeviceWarnRecord> getPageFinish() {
        return pageFinish;
    }

    public void setPageFinish(Page<DeviceWarnRecord> pageFinish) {
        this.pageFinish = pageFinish;
    }
}
