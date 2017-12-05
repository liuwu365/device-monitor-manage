package com.device.manage.web.service.permission;

import com.device.api.entity.Page;
import com.device.api.entity.Result;
import com.device.api.entity.backrole.BackResRoleKey;
import com.device.api.entity.backrole.BackRole;
import com.device.api.uitls.CheckUtil;
import com.device.api.uitls.ErrorWriterUtil;
import com.device.dao.BackResRoleMapper;
import com.device.dao.BackRoleMapper;
import com.device.manage.core.constant.BasicConstant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class BackRoleServiceImpl implements BackRoleService, BeanFactoryAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackRoleServiceImpl.class);

    private static final Gson GSON = new Gson();

    @Resource
    private BackRoleMapper backRoleMapper;

    @Resource
    private BackResRoleMapper backResRoleMapper;


    @Override
    @Transactional
    public Result<Long> addOrUpdateItem(BackRole role) {

        boolean res = false;
        String sucMsg = BasicConstant.ADD_SUCCESS;
        String faiMsg = BasicConstant.ADD_FAILED;
        if (CheckUtil.isEmpty(role.getId())) {
            //新增
            try {
                res = this.backRoleMapper.insertSelective(role) == 1;
            } catch (Exception e) {
                LOGGER.error("add BackRole error|role={}|ex={}", GSON.toJson(role), ErrorWriterUtil.writeError(e).toString());
                throw new RuntimeException();
            }
        } else {
            //修改
            try {
                res = this.backRoleMapper.updateByPrimaryKeySelective(role) == 1;
                sucMsg = BasicConstant.UPDATE_SUCCESS;
                faiMsg = BasicConstant.UPDATE_FAILED;
            } catch (Exception e) {
                LOGGER.error("update BackRole error|role={}|ex={}", GSON.toJson(role), ErrorWriterUtil.writeError(e).toString());
                throw new RuntimeException();
            }
        }
        return res ? Result.success(sucMsg, role.getId()) : Result.failure(faiMsg);
    }

    @Override
    public BackRole getItem(Long id) {

        return this.backRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<BackRole> getPage(Page page) {

        PageHelper.startPage((int) page.getPage(), page.getLimit());
        List<BackRole> itemList = this.backRoleMapper.selectPage(page);
        PageInfo pageInfo = new PageInfo(itemList);
        page.setResult(pageInfo.getList());
        page.setTotal(pageInfo.getTotal());

        return page;
    }

    @Override
    public List<BackRole> getList() {

        List<BackRole> backRoles = this.backRoleMapper.selectList();
        return backRoles;
    }

    @Override
    public Result delItem(Long id) {

        try {
            boolean res = this.backRoleMapper.deleteByPrimaryKey(id) == 1;
            return res ? Result.success(BasicConstant.DELETE_SUCCESS) : Result.failure(BasicConstant.DELETE_FAILED);
        } catch (Exception e) {
            LOGGER.error("delItem error|id={}|ex={}", id, ErrorWriterUtil.writeError(e).toString());
            throw new RuntimeException();
        }
    }

    @Override
    @Transactional
    public Result saveRoleResRelation(String res, Long roleId, Long userId) {

        int i1 = this.backResRoleMapper.delByRoleId(roleId, userId);
        if (CheckUtil.isEmpty(res))
            return Result.success(BasicConstant.SAVE_SUCCESS);
        List<BackResRoleKey> list = new ArrayList<>();
        String[] arr = res.split(",");
        for (int i = 0; i < arr.length; i++) {
            Long resId = Long.valueOf(arr[i]);
            BackResRoleKey backResRoleKey = new BackResRoleKey();
            backResRoleKey.setRoleId(roleId);
            backResRoleKey.setResId(resId);
            backResRoleKey.setUserId(userId);
            list.add(backResRoleKey);
        }
        int i = this.backResRoleMapper.insertBatch(list);
        if (i == -1 || i1 == -1)
            throw new RuntimeException();
        return Result.success(BasicConstant.SAVE_SUCCESS);
    }

    @Override
    public Result getRoleResList(Long roleId, Long userId) {

        List<BackResRoleKey> backResRoleKeys = this.backResRoleMapper.selectByRoleId(roleId, userId);
        return Result.success(backResRoleKeys);
    }

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
