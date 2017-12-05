package com.device.api.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OperationLog implements Serializable {
    private Long id;

    private Long operUserId;

    private String operUserName;

    private Long targetUid;

    private String ipAddress;

    private Integer operType;

    private Long chatId;

    private String content;

    private Date createTime;

    private Date beginTime;

    private Date endTime;

    private String userName;

    private int leavel;

    public OperationLog() {
    }

    public OperationLog(Long operUserId, String operUserName, String ipAddress, String content, Date createTime) {
        this.operUserId = operUserId;
        this.operUserName = operUserName;
        this.ipAddress = ipAddress;
        this.content = content;
        this.createTime = createTime;
    }

    public OperationLog(Long id, Long operUserId, String operUserName,  String ipAddress,
                        Integer operType, Long chatId, String content, Date createTime) {
        this(id, operUserId, operUserName, ipAddress, operType, chatId, content, createTime, null);
    }

    public OperationLog(Long id, Long operUserId, String operUserName, String ipAddress,
                        Integer operType, Long chatId, String content, Date createTime, Long targetUid) {
        this.id = id;
        this.operUserId = operUserId;
        this.operUserName = operUserName;
        this.targetUid = targetUid;
        this.ipAddress = ipAddress;
        this.operType = operType;
        this.chatId = chatId;
        this.content = content;
        this.createTime = createTime;
    }

}