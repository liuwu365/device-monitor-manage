package com.device.api.enums;

/**
 * @author Created by Administrator on 2017/6/22.
 */
public enum WorkProcessResult {

	COMPLETE(0, "处理完成,等待确认"),
	DESCRIPTION_ERROR(1, "客服描述错误"),
	OTHER(2, "其他"),
	RESOLVED(3, "已解决"),
	UPDATE_WORK_ORDER(4, "客服修改驳回的工单"),
	NOT_CONNECTED_CUSTOMER(5, "联系不上客户"),
	CLOSE_WORK_ORDER(6, "信息错误关闭工单"),
	CLOSE_WORK_ORDER_FOR_SUPER(7, "超管关闭工单"),
	REOPEN_WORK_ORDER_FOR_SUPER(8, "超管重开工单");

	private int code;

	private String desc;

	WorkProcessResult(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}


	/**
	 * 确认页面列表
	 *
	 * @return
	 */
	public static WorkProcessResult[] getCompleteView() {
		WorkProcessResult[] result = new WorkProcessResult[3];
		result[0] = COMPLETE;
		result[1] = DESCRIPTION_ERROR;
		result[2] = OTHER;
		return result;
	}

	/**
	 * 驳回页面
	 *
	 * @return
	 */
	public static WorkProcessResult[] getRejectView() {
		WorkProcessResult[] result = new WorkProcessResult[2];
		result[0] = DESCRIPTION_ERROR;
		result[1] = OTHER;
		return result;
	}

	/**
	 * 确认页面
	 *
	 * @return
	 */
	public static WorkProcessResult[] getConfirmView() {
		WorkProcessResult[] result = new WorkProcessResult[2];
		result[0] = RESOLVED;
		result[1] = NOT_CONNECTED_CUSTOMER;
		return result;
	}

	public static WorkProcessResult getResult(String desc) {
		for (WorkProcessResult workProcessResult : WorkProcessResult.values()) {
			if (workProcessResult.getDesc().equals(desc)) {
				return workProcessResult;
			}
		}
		return null;
	}


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
}
