package com.ly.spider.test;

import java.util.Set;

import com.ly.spider.bean.HouseInfoData;
import com.ly.spider.core.LJHouseExtractService;
import com.ly.spider.rule.Rule;

public class HouseTest
{
	private static String url="http://bj.lianjia.com/ershoufang/bp360ep1362rs望京/";
	
	public static void getLianJiaDatas()
	{
		Rule rule = new Rule(url,
				null, 
				null,
				"li.clear", //div.title a[data-el=ershoufang]
				Rule.SELECTION, 
				Rule.GET);
		Set<HouseInfoData> extracts = LJHouseExtractService.extract(rule);
		//System.out.println("共找到"+extracts.size()+"套");
		for (HouseInfoData data : extracts)System.out.println(data.toString());
	}
	

	public static void main(String[] args) {
		
		getLianJiaDatas();
	}
}
