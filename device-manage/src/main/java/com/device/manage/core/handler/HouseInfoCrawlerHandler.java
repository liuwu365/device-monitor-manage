package com.device.manage.core.handler;

import com.device.api.entity.HouseTrend;
import com.device.api.uitls.CheckUtil;
import com.device.manage.core.utils.CommonUtil;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 房价趋势信息爬虫
 * @Date: 2018-02-01 17:48
 */
public class HouseInfoCrawlerHandler {
    private static final Logger logger = LoggerFactory.getLogger(HouseInfoCrawlerHandler.class);
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        getHouseInfoByCrawler();
    }

    private static List<HouseTrend> getHouseInfoByCrawler() {
        List<HouseTrend> list = new ArrayList<>();
        System.out.println("======================================>开始抓取数据");
        String url = "http://shiyan.jiwu.com/fangjia/";
        try {
            //模拟谷歌浏览器
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            //设置webClient的相关参数
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setCssEnabled(false);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            //webClient.getOptions().setTimeout(50000);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            //模拟浏览器打开一个目标网址
            HtmlPage rootPage = webClient.getPage(url);
            //System.out.println("为了获取js执行的数据 线程开始沉睡等待");
            //Thread.sleep(10000);//主要是这个线程的等待 因为js加载也是需要时间的
            //System.out.println("线程结束沉睡");
            String html = rootPage.asXml();
            //System.out.println(html);
            System.out.println("======================================>抓取数据完成");

            //开始解析数据
            Document doc = Jsoup.parse(html);
            Elements fjTableWrap = doc.getElementsByClass("fj_table_wrap");
            //System.out.println(fjTableWrap.html());

            Elements tableTr1 = fjTableWrap.get(0).getElementsByClass("fj_table_tr1");
            for (int i = 0; i < tableTr1.size(); i++) {
                HouseTrend houseInfo = new HouseTrend();
                String area = tableTr1.get(i).getElementsByTag("a").text();
                if (!CheckUtil.isEmpty(area)) { //新房
                    System.out.println("区域:" + area);
                    Element newHousePrice = tableTr1.get(i).getElementsByTag("td").get(3);
                    Element huanbi = tableTr1.get(i).getElementsByTag("td").get(5);
                    Element tongbi = tableTr1.get(i).getElementsByTag("td").get(6);
                    System.out.println(
                            " 新房均价：" + newHousePrice.text() +
                                    " 环比(比例)：" + huanbi.text() +
                                    " 环比趋势(升降)：" + trendDesc(huanbi.attr("class")) +
                                    " 同比(比例)：" + getPercentValue(tongbi.text()) +
                                    " 同比趋势(升降)：" + trendDesc(tongbi.attr("class"))
                    );

                    houseInfo.setArea(area);
                    houseInfo.setNewHousePrice(Integer.parseInt(newHousePrice.text()));
                    houseInfo.setNewHuanbiPercent(getPercentValue(huanbi.text()));
                    houseInfo.setNewHuanbi(trendType(huanbi.attr("class")).byteValue());
                    houseInfo.setNewTonbiPercent(getPercentValue(tongbi.text()));
                    houseInfo.setNewTongbi(trendType(tongbi.attr("class")).byteValue());

                } else {  //二手房
                    Element oldHousePrice = tableTr1.get(i).getElementsByTag("td").get(2);
                    Element huanbi = tableTr1.get(i).getElementsByTag("td").get(4);
                    Element tongbi = tableTr1.get(i).getElementsByTag("td").get(5);
                    System.out.println(
                            " 二手房均价：" + oldHousePrice.text() +
                                    " 环比(比例)：" + huanbi.text() +
                                    " 环比趋势(升降)：" + trendDesc(huanbi.attr("class")) +
                                    " 同比(比例)：" + getPercentValue(tongbi.text()) +
                                    " 同比趋势(升降)：" + trendDesc(tongbi.attr("class"))
                    );
                    houseInfo.setOldHousePrice(Integer.parseInt(oldHousePrice.text()));
                    houseInfo.setOldHuanbiPercent(getPercentValue(huanbi.text()));
                    houseInfo.setOldHuanbi(trendType(huanbi.attr("class")).byteValue());
                    houseInfo.setOldTongbiPercent(getPercentValue(tongbi.text()));
                    houseInfo.setOldTongbi(trendType(tongbi.attr("class")).byteValue());
                }
                houseInfo.setCreateTime(new Date());
                houseInfo.setUpdateTime(new Date());
                list.add(houseInfo);
            }
            logger.info("爬虫结果:{}", gson.toJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //获取比例的值,为空时给0
    private static double getPercentValue(String valueStr) {
        String value = CommonUtil.getNumbers2(valueStr);
        return CheckUtil.isEmpty(value) ? 0 : Double.parseDouble(value);
    }

    //返回趋势描述
    private static String trendDesc(String value) {
        String str = "";
        if (CheckUtil.isEmpty(value)) {
            return str;
        }
        switch (value) {
            case "fj_red":
                str = "上升";
                break;
            case "fj_green":
                str = "下降";
                break;
            default:
                break;
        }
        return str;
    }

    //返回趋势type(0:-- 1:上升2:下降)
    private static Integer trendType(String value) {
        Integer str = 0;
        if (CheckUtil.isEmpty(value)) {
            return str;
        }
        switch (value) {
            case "fj_red":
                str = 1;
                break;
            case "fj_green":
                str = 2;
                break;
            default:
                break;
        }
        return str;
    }

}
