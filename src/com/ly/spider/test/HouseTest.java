package com.ly.spider.test;

import java.util.Set;

import com.ly.spider.bean.HouseInfoData;
import com.ly.spider.core.LJHouseExtractService;
import com.ly.spider.rule.Rule;

public class HouseTest
{
	//private static String url="http://bj.lianjia.com/ershoufang/bp360ep460rs望京/";
	private static String preUrl="http://bj.lianjia.com/ershoufang/";
	private static String conditionUrl="bp360ep462rs望京/";
	private static String tag="li.clear";

	
	public static void getLianJiaDatas()
	{
		Rule rule = new Rule(preUrl+conditionUrl,
				null, 
				null,
				tag, //div.title a[data-el=ershoufang]
				Rule.SELECTION, 
				Rule.GET);
		Set<HouseInfoData> extracts = LJHouseExtractService.extract(rule,preUrl,conditionUrl);
		System.out.println("共找到"+extracts.size()+"套\n");
		for (HouseInfoData data : extracts)System.out.println(data.toString());
	}
	

	public static void main(String[] args) {
		
		getLianJiaDatas();
	}
}
