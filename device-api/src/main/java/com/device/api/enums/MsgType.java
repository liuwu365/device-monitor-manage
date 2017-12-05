package com.device.api.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by Administrator on 2017/7/10.
 * 通知类型前后端通用
 * 0:手动编辑的消息 前端
 * 1开头的消息是前端模板
 * 2开头的消息是后台模板
 */
public enum MsgType implements IntEnum<MsgType> {



	SYSTEM(0,"SYSTEM","系统群发[前端用户组]消息"),
//	RECHARGE_ACCOUNT(10,"RECHARGE_ACCOUNT","充值到账"),
	RECHARGE_FAILED(11,"RECHARGE_FAILED","充值失败"),
//	WITHDRAW_SUCCESSFUL(12,"WITHDRAW_SUCCESSFUL","提现成功"),
//	WITHDRAW_FAILED(13,"WITHDRAW_FAILED","提现失败"),
//	QUIZ_RESULT(14,"QUIZ_RESULT","你的竞猜已有结果"),
	BET_RESULT(14,"BET_RESULT","比赛结果"),
	PAYMENT_FAILED(15,"PAYMENT_FAILED","出款失败"),
	DRAWING_FAILED(16,"DRAWING_FAILED","提款失败"),
	WORK_ORDER_CREATE(20,"WORK_ORDER_CREATE","工单创建消息"),
	WORK_ORDER_TIMING(21,"WORK_ORDER_TIMING","工单定时消息"),
	PAY(22,"PAY","充值消息"),
	WITHDRAW(23,"WITHDRAW","提现消息"),
	PARAGRAPH(24,"PARAGRAPH","出款消息");


	private int code;
	private String key;
	private String desc;


	MsgType(int code, String key, String desc) {
		this.code = code;
		this.key = key;
		this.desc = desc;
	}

	public MsgType[] getMgtValue() {
		List<MsgType> mgtList = new ArrayList<MsgType>();
		for (MsgType msgType : MsgType.values()) {
			if (String.valueOf(msgType.getCode()).startsWith("2")) {
				mgtList.add(msgType);
			}
		}
		return mgtList.toArray(new MsgType[mgtList.size()]);
	}

	@Override
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getKey() {
		return key;
	}

	@Override
	public int getIntValue() {
		return this.code;
	}
}
