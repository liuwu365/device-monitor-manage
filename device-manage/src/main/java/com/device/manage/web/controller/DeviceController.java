package com.device.manage.web.controller;

import com.device.api.entity.Page;
import com.device.api.entity.Result;
import com.device.api.entity.device.DeviceInfo;
import com.device.api.entity.device.DeviceWarnRecord;
import com.device.api.entity.device.DeviceWarnRecordTab;
import com.device.api.entity.device.DeviceWarnRule;
import com.device.api.enums.ResultCode;
import com.device.api.enums.WarnRecordStatus;
import com.device.api.uitls.CheckUtil;
import com.device.api.uitls.ErrorWriterUtil;
import com.device.manage.web.service.device.DeviceService;
import com.device.manage.web.service.device.WarnRecordService;
import com.device.manage.web.service.device.WarnRuleService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Description: 设备管理控制器
 * @Date: 2017-12-02 11:18
 */
@Controller
@RequestMapping("/device")
public class DeviceController {
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);
    private static final Gson gson = new Gson();

    @Resource
    private DeviceService deviceService;
    @Resource
    private WarnRuleService warnRuleService;
    @Resource
    private WarnRecordService warnRecordService;

    private final String listPage = "model/device/device_list";
    private final String addPage = "model/device/device_add";
    private final String editPage = "model/device/device_edit";
    private final String mapPage = "model/device/device_map";
    private final String warnRuleListPage = "model/warnRule/warn_rule_list";
    private final String warnRuleAddPage = "model/warnRule/warn_rule_add";
    private final String warnRecordListPage = "model/device/device_warn_list";
    private final String warnRecordListPage1 = "model/device/device_warn_list_1";

    //设备列表
    @RequestMapping(value = "/device_list")
    public String deviceList(Model model, Page<DeviceInfo> page) {
        try {
            page = deviceService.findPage(page);
            model.addAttribute("page", page);
            logger.info("select device_list={}", gson.toJson(page));
        } catch (Exception e) {
            logger.error("device_list error|page={}|ex={}", gson.toJson(page), ErrorWriterUtil.writeError(e));
        }
        return listPage;
    }

    //跳转到页面
    @RequestMapping(value = "/map", method = RequestMethod.GET)
    public String map() {
        return "model/device/map";
    }

    //跳转到地图页面
    @RequestMapping(value = "/device_map", method = RequestMethod.GET)
    public String deviceMap() {
        return mapPage;
    }

    //跳转到地图页面
    @RequestMapping(value = "/deviceMapData")
    @ResponseBody
    public Result deviceMapData() {
        Result meta = null;
        try {
            List<DeviceInfo> deviceInfoList = deviceService.selectAll();
            meta = Result.success(deviceInfoList);
        } catch (Exception e) {
            logger.error("deviceMapData error ex={}", ErrorWriterUtil.writeError(e));
            meta = Result.error();
        }
        return meta;
    }

    //跳转到新增页面
    @RequestMapping(value = "/device_add", method = RequestMethod.GET)
    public String deviceAdd() {
        return addPage;
    }

    //新增设备操作
    @RequestMapping(value = "/device_add", method = RequestMethod.POST)
    @ResponseBody
    public Result deviceAdd(HttpServletRequest request, DeviceInfo deviceInfo) {
        Result meta = null;
        try {
            DeviceInfo info = deviceService.findByDeviceUid(deviceInfo.getDeviceUid());
            if (!CheckUtil.isEmpty(info)) {
                return Result.failure("设备Uid已存在");
            }
            deviceInfo.setUpdateTime(new Date());
            int count = deviceService.insertSelective(deviceInfo);
            meta = count > 0 ? Result.success() : Result.failure();
        } catch (Exception e) {
            logger.error("add device error|ex={}", ErrorWriterUtil.writeError(e).toString());
            meta = Result.error();
        }
        return meta;
    }

    //删除设备操作
    @RequestMapping("/deleteDevice/{id}")
    @ResponseBody
    public Result deleteDevice(HttpServletRequest request, @PathVariable("id") Long id) {
        Result meta = null;
        try {
            int count = deviceService.deleteByPrimaryKey(id);
            meta = count > 0 ? Result.success() : Result.failure();
        } catch (Exception e) {
            logger.error("deleteDevice id={}|error={}", id, ErrorWriterUtil.writeError(e).toString());
            meta = Result.error();
        }
        return meta;
    }

    //加载要编辑的设备信息
    @RequestMapping(value = "/loadDevice", method = RequestMethod.GET)
    public String loadFolder(Model model, @RequestParam(value = "deviceId", required = false) Long deviceId) throws Exception {
        DeviceInfo deviceInfo = deviceService.selectByPrimaryKey(deviceId);
        model.addAttribute("device", deviceInfo);
        return editPage;
    }

    //编辑设备操作
    @RequestMapping(value = "/editDevice", method = RequestMethod.POST)
    @ResponseBody
    public Result editDevice(HttpServletRequest request, DeviceInfo deviceInfo) {
        Result result;
        try {
            deviceInfo.setUpdateTime(new Date());
            int count = deviceService.updateByPrimaryKeySelective(deviceInfo);
            result = count > 0 ? Result.success() : Result.failure();
        } catch (Exception e) {
            logger.error("edit device error|ex={}", ErrorWriterUtil.writeError(e).toString());
            result = Result.error();
        }
        return result;
    }

    //编辑设备操作(启用或禁用)
    @RequestMapping(value = "/editDeviceStatus", method = RequestMethod.POST)
    @ResponseBody
    public Result editDeviceStatus(HttpServletRequest request, long id, int status) {
        Result result;
        try {
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setId(id);
            deviceInfo.setStatus((short) status);
            deviceInfo.setUpdateTime(new Date());
            int count = deviceService.updateByPrimaryKeySelective(deviceInfo);
            result = count > 0 ? Result.success() : Result.failure();
        } catch (Exception e) {
            logger.error("edit device status error|ex={}", ErrorWriterUtil.writeError(e).toString());
            result = Result.error();
        }
        return result;
    }

    //-------------------------------------------------------- 设备报警规则 -------------------------------------------------------------
    //报警规则列表
    @RequestMapping(value = "/warn_rule_list")
    public String warnRuleList(Model model, Page<DeviceWarnRule> page) {
        try {
            page = warnRuleService.findPage(page);
            model.addAttribute("page", page);
        } catch (Exception e) {
            logger.error("warn_rule_list error|page={}|ex={}", gson.toJson(page), ErrorWriterUtil.writeError(e));
        }
        return warnRuleListPage;
    }

    //跳转到新增页面
    @RequestMapping(value = "/warn_rule_add", method = RequestMethod.GET)
    public String warnRuleAdd() {
        return warnRuleAddPage;
    }

    //新增操作
    @RequestMapping(value = "/warn_rule_add", method = RequestMethod.POST)
    @ResponseBody
    public Result warnRuleAdd(DeviceWarnRule warnRule) {
        Result result = new Result();
        try {
            if (warnRule.getMinValue() <= warnRule.getMaxValue()) {
                return Result.failure("阀值上限不能小于等于阀值下限");
            }
            DeviceWarnRule exist = warnRuleService.selectByItemAndLevel(warnRule.getItem(), warnRule.getLevel());
            if (!CheckUtil.isEmpty(exist)) {
                return Result.failure("已经存在一个该类型该级别的报警规则了");
            }
            //查询它的小一级的值范围
            DeviceWarnRule target = warnRuleService.selectByItemAndLevel(warnRule.getItem(), warnRule.getLevel().intValue() - 1);
            if (!CheckUtil.isEmpty(target)) {
                if (warnRule.getMinValue() <= target.getMaxValue()) {
                    return Result.failure("阀值设置错误,请查看当前小一级的阀值范围,设置合理的新级别新阀值范围");
                }
            }
            warnRule.setUpdateTime(new Date());
            int count = warnRuleService.insertSelective(warnRule);
            result = count > 0 ? Result.success() : Result.failure();
        } catch (Exception e) {
            logger.error("add warn rule error|ex={}", ErrorWriterUtil.writeError(e).toString());
            result = Result.failure(ResultCode.SERVER_INTERNAL_ERROR);
        }
        return result;
    }

    //编辑操作(启用或禁用)
    @RequestMapping(value = "/editWarnRuleStatus", method = RequestMethod.POST)
    @ResponseBody
    public Result editWarnRuleStatus(long id, int status) {
        Result result;
        try {
            DeviceWarnRule warnRule = new DeviceWarnRule();
            warnRule.setId(id);
            warnRule.setStatus((short) status);
            warnRule.setUpdateTime(new Date());
            int count = warnRuleService.updateByPrimaryKeySelective(warnRule);
            result = count > 0 ? Result.success() : Result.failure();
        } catch (Exception e) {
            logger.error("edit device status error|ex={}", ErrorWriterUtil.writeError(e).toString());
            result = Result.error();
        }
        return result;
    }

    //编辑报警规则
    @RequestMapping(value = "/editWarnRule")
    @ResponseBody
    public Result editWarnRule(@RequestParam(value = "id") long id,
                               @RequestParam(value = "item") String item,
                               @RequestParam(value = "minValue") double minValue,
                               @RequestParam(value = "maxValue") double maxValue,
                               @RequestParam(value = "level") int level) {
        Result result = new Result();
        try {
            if (CheckUtil.isEmpty(id)) {
                return Result.failure(ResultCode.BAD_REQUEST.getCode(), ResultCode.BAD_REQUEST.getDesc());
            }
            if (minValue <= maxValue) {
                return Result.failure("阀值上限不能小于等于阀值下限");
            }
            DeviceWarnRule exist = warnRuleService.selectByItemAndLevel(item, level);
            if (!CheckUtil.isEmpty(exist)) {
                return Result.failure("已经存在一个该类型该级别的报警规则了");
            }
            //查询它的小一级的值范围
            DeviceWarnRule target = warnRuleService.selectByItemAndLevel(item, level - 1);
            if (!CheckUtil.isEmpty(target)) {
                if (minValue <= target.getMaxValue()) {
                    return Result.failure("阀值设置错误,请查看当前小一级的阀值范围,设置合理的新级别新阀值范围");
                }
            }
            DeviceWarnRule warnRule = new DeviceWarnRule();
            warnRule.setId(id);
            warnRule.setMinValue(minValue);
            warnRule.setMaxValue(maxValue);
            warnRule.setLevel(level);
            warnRule.setUpdateTime(new Date());
            int count = warnRuleService.updateByPrimaryKeySelective(warnRule);
            result = count > 0 ? Result.success() : Result.failure();
        } catch (Exception e) {
            result = Result.failure(ResultCode.SERVER_INTERNAL_ERROR);
            logger.error("/device/editWarnRule error|ex={}", ErrorWriterUtil.writeError(e));
        }
        return result;
    }

    //删除操作
    @RequestMapping("/deleteWarnRule/{id}")
    @ResponseBody
    public Result deleteWarnRule(HttpServletRequest request, @PathVariable("id") Long id) {
        Result meta = null;
        try {
            int count = warnRuleService.deleteByPrimaryKey(id);
            meta = count > 0 ? Result.success() : Result.failure();
        } catch (Exception e) {
            logger.error("deleteWarnRule id={}|error={}", id, ErrorWriterUtil.writeError(e).toString());
            meta = Result.error();
        }
        return meta;
    }

    //-------------------------------------------------------- 设备报警列表 -------------------------------------------------------------
    @RequestMapping(value = "/device_warn_list_1")
    public String deviceWarnList1(Model model, DeviceWarnRecordTab tab) {
        try {
            Page<DeviceWarnRecord> pageWait = new Page<>();
            Page<DeviceWarnRecord> pageProcess = new Page<>();
            Page<DeviceWarnRecord> pageFinish = new Page<>();

            if (CheckUtil.isEmpty(tab.getPageWait())) {
                pageWait.getFilter().put("status", WarnRecordStatus.WAIT.getCode());
                tab.setPageWait(pageWait);
            } else {
                tab.getPageWait().getFilter().put("status", WarnRecordStatus.WAIT.getCode());
            }
            tab.setPageWait(warnRecordService.findPage(tab.getPageWait()));

            if (CheckUtil.isEmpty(tab.getPageProcess())) {
                pageProcess.getFilter().put("status", WarnRecordStatus.PROCESS.getCode());
                tab.setPageProcess(pageProcess);
            } else {
                tab.getPageProcess().getFilter().put("status", WarnRecordStatus.PROCESS.getCode());
            }
            tab.setPageProcess(warnRecordService.findPage(tab.getPageProcess()));

            if (CheckUtil.isEmpty(tab.getPageFinish())) {
                pageFinish.getFilter().put("status", WarnRecordStatus.FINISH.getCode());
                tab.setPageFinish(pageFinish);
            } else {
                tab.getPageFinish().getFilter().put("status", WarnRecordStatus.FINISH.getCode());
            }
            tab.setPageFinish(warnRecordService.findPage(tab.getPageFinish()));

            model.addAttribute("pageWait", tab.getPageWait());
            model.addAttribute("pageProcess", tab.getPageProcess());
            model.addAttribute("pageFinish", tab.getPageFinish());

            //logger.info("select warn device_list={}", gson.toJson(page));
        } catch (Exception e) {
            e.printStackTrace();
            //logger.error("device_warn_list error|page={}|ex={}", gson.toJson(page), ErrorWriterUtil.writeError(e));
        }
        return warnRecordListPage1;
    }

    @RequestMapping(value = "/device_warn_list")
    public String deviceWarnList(Model model, Page<DeviceWarnRecord> page) {
        try {
            //默认查询待处理的报警信息
            if (CheckUtil.isEmpty(page.getFilter().get("status"))) {
                page.getFilter().put("status", WarnRecordStatus.WAIT.getCode());
            }
            page = warnRecordService.findPage(page);
            model.addAttribute("page", page);
            logger.info("select warn device_list={}", gson.toJson(page));
        } catch (Exception e) {
            logger.error("device_warn_list error|page={}|ex={}", gson.toJson(page), ErrorWriterUtil.writeError(e));
        }
        return warnRecordListPage;
    }

    //编辑报警记录(处理报警)
    @RequestMapping(value = "/editWarnRecord")
    @ResponseBody
    public Result editWarnRecord(@RequestParam(value = "id") long id, @RequestParam(value = "status") int status) {
        Result result = new Result();
        try {
            if (CheckUtil.isEmpty(id)) {
                return Result.failure(ResultCode.BAD_REQUEST.getCode(), ResultCode.BAD_REQUEST.getDesc());
            }
            DeviceWarnRecord warnRecord = new DeviceWarnRecord();
            warnRecord.setId(id);
            warnRecord.setStatus((byte) status);
            if (status == WarnRecordStatus.PROCESS.getCode()) {
                warnRecord.setHandleStartTime(new Date());
            } else if (status == WarnRecordStatus.FINISH.getCode()) {
                warnRecord.setHandleEndTime(new Date());
            }
            warnRecord.setUpdateTime(new Date());
            int count = warnRecordService.updateByPrimaryKeySelective(warnRecord);
            result = count > 0 ? Result.success() : Result.failure();
        } catch (Exception e) {
            result = Result.failure(ResultCode.SERVER_INTERNAL_ERROR);
            logger.error("/device/editWarnRecord error|ex={}", ErrorWriterUtil.writeError(e));
        }
        return result;
    }


}
