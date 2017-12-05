package com.device.api.entity;

/**
 * Created by  on 4/19/16.
 */
public class Message<T> {
    private int msgType;

    private int from;

    private int to;

    private long ts;

    private String sessionId;

    private int count;

    private T msgContent;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public T getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(T msgContent) {
        this.msgContent = msgContent;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
