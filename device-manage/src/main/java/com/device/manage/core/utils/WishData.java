package com.device.manage.core.utils;

import com.device.api.uitls.CheckUtil;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author liuwu_eva@163.com
 */
public class WishData {
    public static void main(String[] args) {
        //getData();
//		getData2();
//		getData3();
        getData4();
    }

    private static void getData() {
        try {
            // 得到浏览器对象，直接New一个就能得到，现在就好比说你得到了一个浏览器了
            WebClient webclient = new WebClient();
            // 这里是配置一下不加载css和javaScript,配置起来很简单，是不是
            webclient.getOptions().setCssEnabled(false);
            webclient.getOptions().setJavaScriptEnabled(false);
            // 做的第一件事，去拿到这个网页，只需要调用getPage这个方法即可
            HtmlPage htmlpage = webclient.getPage("http://news.baidu.com/advanced_news.html");
            // 根据名字得到一个表单，查看上面这个网页的源代码可以发现表单的名字叫“f”
            final HtmlForm form = htmlpage.getFormByName("f");
            // 同样道理，获取”百度一下“这个按钮
            final HtmlSubmitInput button = form.getInputByValue("百度一下");
            // 得到搜索框
            final HtmlTextInput textField = form.getInputByName("q1");
            // 最近周星驰比较火呀，我这里设置一下在搜索框内填入”周星驰“
            textField.setValueAttribute("周星驰");
            // 输入好了，我们点一下这个按钮
            final HtmlPage nextPage = button.click();
            // 我把结果转成String
            String result = nextPage.asXml();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getData2() {
        try {
            String url = "https://www.wish.com/c/54b4dad61c3ab223806371f2";
            WebClient webclient = new WebClient();
            webclient.getOptions().setCssEnabled(false);
            webclient.getOptions().setJavaScriptEnabled(true);
            webclient.getOptions().setThrowExceptionOnScriptError(false);
            HtmlPage htmlpage = webclient.getPage(url);
            System.out.println(htmlpage.asXml());
//			String productName = htmlpage.getElementById("product-name").toString();
//			System.out.println(productName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getData3() {
        System.out.println("======================================>开始抓取数据");
        String url = "https://www.wish.com/c/583eb0ede173475fef337d4f";
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
//		    System.out.println("为了获取js执行的数据 线程开始沉睡等待");  
//		    Thread.sleep(10000);//主要是这个线程的等待 因为js加载也是需要时间的  
//		    System.out.println("线程结束沉睡");  
            String html = rootPage.asXml();
            System.out.println(html);
            System.out.println("======================================>抓取数据完成");

//	        System.out.println(getScriptData(html)); //script内容
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getData4() {
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

            /*
            Element areaA = fjTableWrap.get(0).getElementById("pricestr118380");
            Element newHousePrice = fjTableWrap.get(0).getElementById("area18380");
            Element oldHousePrice = fjTableWrap.get(0).getElementById("esfarea18380");
            Element tongbi = fjTableWrap.get(0).getElementById("tongbi18380");
            Element huanbi = fjTableWrap.get(0).getElementById("huanbi18380");
            Element tongbi2 = fjTableWrap.get(0).getElementById("esftongbi18380");
            Element huanbi2 = fjTableWrap.get(0).getElementById("esfhuanbi18380");
            System.out.println("区域:" + areaA.text() +
                    " 新房均价：" + newHousePrice.text() +
                    " 同比：" + CommonUtil.getNumbers2(tongbi.text()) +
                    " 同比趋势：" + trendDesc(tongbi.attr("class")) +
                    " 环比：" + huanbi.text() +
                    " 环比趋势：" + trendDesc(huanbi.attr("class")) +
                    " /n/r" +
                    " 二手房均价：" + oldHousePrice.text() +
                    " 同比：" + CommonUtil.getNumbers2(tongbi2.text()) +
                    " 同比趋势：" + trendDesc(tongbi2.attr("class")) +
                    " 环比：" + huanbi2.text() +
                    " 环比趋势：" + trendDesc(huanbi2.attr("class"))
            );
            */

            Elements tableTr1 = fjTableWrap.get(0).getElementsByClass("fj_table_tr1");
            for (int i = 0; i < tableTr1.size(); i++) {
                String area = tableTr1.get(i).getElementsByTag("a").text();
                if (!CheckUtil.isEmpty(area)) { //新房
                    System.out.println("区域:" + area);
                    Element newHousePrice = tableTr1.get(i).getElementsByTag("td").get(3);
                    Element tongbi = tableTr1.get(i).getElementsByTag("td").get(5);
                    Element huanbi = tableTr1.get(i).getElementsByTag("td").get(6);
                    System.out.println(
                            " 新房均价：" + newHousePrice.text() +
                            " 同比：" + CommonUtil.getNumbers2(tongbi.text()) +
                            " 同比趋势：" + trendDesc(tongbi.attr("class")) +
                            " 环比：" + huanbi.text() +
                            " 环比趋势：" + trendDesc(huanbi.attr("class"))
                    );
                } else {  //二手房
                    Element oldHousePrice = tableTr1.get(i).getElementsByTag("td").get(2);
                    Element tongbi = tableTr1.get(i).getElementsByTag("td").get(4);
                    Element huanbi = tableTr1.get(i).getElementsByTag("td").get(5);
                    System.out.println(
                            " 二手房均价：" + oldHousePrice.text() +
                            " 同比：" + CommonUtil.getNumbers2(tongbi.text()) +
                            " 同比趋势：" + trendDesc(tongbi.attr("class")) +
                            " 环比：" + huanbi.text() +
                            " 环比趋势：" + trendDesc(huanbi.attr("class"))
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /**
     * 用正则匹配所有script的内容
     *
     * @param html 网页内容
     */
    private static String getScriptData(String html) {
        StringBuffer sbf = new StringBuffer();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sbf.toString();
    }

}
