package com.device.api.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个是列表树形式显示的实体,
 * 这里的字段是在前台显示所有的,可修改
 * @author lanyuan
 * Email：mmm333zzz520@163.com
 * date：2014-11-20
 */
@Data
public class TreeObject {
	private Integer id;
	private Integer parentId;
	private String name;
	private String parentName;
	private String resKey;
	private String resUrl;
	private Integer level;
	private String type;
	private String description;
	private String icon;
	private Integer ishide;
	private boolean isChoice;
	private List<TreeObject> children = new ArrayList<>();
}
