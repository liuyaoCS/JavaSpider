package com.ly.spider.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.plaf.TextUI;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ly.spider.bean.HouseInfoData;
import com.ly.spider.bean.LinkTypeData;
import com.ly.spider.rule.Rule;
import com.ly.spider.rule.RuleException;
import com.ly.spider.util.TextUtil;

/**
 * 
 * @author zhy
 * 
 */
public class LJHouseExtractService
{
	/**
	 * @param rule
	 * @return
	 */
	public static Set<HouseInfoData> extract(Rule rule)
	{

		// 进行对rule的必要校验
		validateRule(rule);

		//List<HouseInfoData> datas = new ArrayList<HouseInfoData>();
		Set<HouseInfoData> datas=new TreeSet<HouseInfoData>();
		HouseInfoData data = null;
		try
		{
			/**
			 * 解析rule
			 */
			String url = rule.getUrl();
			String[] params = rule.getParams();
			String[] values = rule.getValues();
			String resultTagName = rule.getResultTagName();
			int type = rule.getType();
			int requestType = rule.getRequestMoethod();

			Connection conn = Jsoup.connect(url);
			// 设置查询参数

			if (params != null)
			{
				for (int i = 0; i < params.length; i++)
				{
					conn.data(params[i], values[i]);
				}
			}

			// 设置请求类型
			Document doc = null;
			switch (requestType)
			{
			case Rule.GET:
				doc = conn.timeout(100000).get();
				break;
			case Rule.POST:
				doc = conn.timeout(100000).post();
				break;
			}

			//处理返回数据
			Elements results = new Elements();
			switch (type)
			{
			case Rule.CLASS:
				results = doc.getElementsByClass(resultTagName);
				break;
			case Rule.ID:
				Element result = doc.getElementById(resultTagName);
				results.add(result);
				break;
			case Rule.SELECTION:
				results = doc.select(resultTagName);
				break;
			default:
				//当resultTagName为空时默认去body标签
				if (TextUtil.isEmpty(resultTagName))
				{
					results = doc.getElementsByTag("body");
				}
			}

			for (Element result : results)
			{
				Element linkUrlEle = result.select("div.title a").get(0);
				String linkUrl=linkUrlEle.attr("href");
				Element picUrlEle = result.select("a.img img").get(0);
				String picUrl=picUrlEle.attr("data-original");
				Element priceEle=result.select("div.totalPrice span").get(0);
				String price=priceEle.text();
				Element unitPriceEle=result.select("div.unitPrice span").get(0);
				String unitPrice=unitPriceEle.text();
				Element titleEle=result.select("div.title").get(0);
				String title=titleEle.text();			
				Element areaEle=result.select("div.positionInfo a").get(0);
				String area=areaEle.text();				
				Element addressEle=result.select("div.houseInfo a").get(0);
				String address=addressEle.text();
				
				data=new HouseInfoData();
				data.setLinkUrl(linkUrl);
				data.setPicUrl(picUrl);
				data.setPrice(Integer.valueOf(price));
				data.setUnitPrice(atoi(unitPrice));
				data.setTitle(title);
				data.setArea(area);
				data.setAddress(address);
				
				datas.add(data);
//				for (Element link : links)
//				{
//					//必要的筛选
//					String linkHref = link.attr("href");
//					String linkText = link.text();
//
//					data = new LinkTypeData();
//					data.setLinkHref(linkHref);
//					data.setLinkText(linkText);
//
//					datas.add(data);
//				}
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return datas;
	}
	private static int atoi(String input){
		int beginIndex=input.indexOf("单价");
		int endIndex=input.indexOf("元/平米");
		String price=input.substring(beginIndex+2, endIndex);
		return Integer.parseInt(price);
	}
	/**
	 * 对传入的参数进行必要的校验
	 */
	private static void validateRule(Rule rule)
	{
		String url = rule.getUrl();
		if (TextUtil.isEmpty(url))
		{
			throw new RuleException("url不能为空！");
		}
		if (!url.startsWith("http://"))
		{
			throw new RuleException("url的格式不正确！");
		}

		if (rule.getParams() != null && rule.getValues() != null)
		{
			if (rule.getParams().length != rule.getValues().length)
			{
				throw new RuleException("参数的键值对个数不匹配！");
			}
		}

	}


}
