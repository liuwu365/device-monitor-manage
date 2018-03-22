package com.device.manage.web.controller;

import com.device.api.entity.HouseTrend;
import com.device.api.entity.Page;
import com.device.api.entity.Result;
import com.device.api.uitls.ErrorWriterUtil;
import com.device.manage.core.handler.HouseInfoCrawlerHandler;
import com.device.manage.web.controller.base.BaseController;
import com.device.manage.web.service.HouseTrendService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 房价趋势控制器
 * @Date: 2018-02-01 14:40
 */
@Controller
@RequestMapping("/houseTrend")
public class HouseTrendController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(HouseTrendController.class);
    private static final Gson gson = new Gson();

    @Resource
    private HouseTrendService houseTrendService;

    private final String listPage = "model/house/house_trend_list";

    @RequestMapping(value = "/houseTrend_list")
    public String houseTrendList(Model model, Page<HouseTrend> page) {
        try {
            page = houseTrendService.findPage(page);
            model.addAttribute("page", page);
            logger.info("select houseTrendList={}", gson.toJson(page));
        } catch (Exception e) {
            logger.error("houseTrendList error|page={}|ex={}", gson.toJson(page), ErrorWriterUtil.writeError(e));
        }
        return listPage;
    }

    @RequestMapping(value = "/getNewData")
    @ResponseBody
    public String getNewData() {
        Result result = null;
        try {
            List<HouseTrend> list = HouseInfoCrawlerHandler.getHouseInfoByCrawler();
            result = houseTrendService.insertBatch(list);
        } catch (Exception e) {
            logger.error("getNewData error|ex={}", ErrorWriterUtil.writeError(e));
        }
        return gson.toJson(result);
    }

}
