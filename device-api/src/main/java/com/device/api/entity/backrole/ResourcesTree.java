package com.device.api.entity.backrole;

import com.device.api.entity.TreeObject;
import lombok.Data;

import java.util.List;

/**
 * User: zc
 * Date: 2017/11/27.
 */
@Data
public class ResourcesTree {
    private BackResources item;
    private List<TreeObject> ns;
}
