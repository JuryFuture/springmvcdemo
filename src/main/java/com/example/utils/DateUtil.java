package com.example.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * Created by tengguodong on 2017/3/28.
 */
public class DateUtil {
	public static Date addMonths(Date startDate, int months) {
		// 如果startDate是当月最后一天，那就先加月，把加完之后的天数调整为该月最后一天
		// 如果startDate不是当月最后一天，先加月，如果结果是该月最后一天，则计算startdate离当月最后一天距离几天，则计算结果-距离天数
		return new Date();
	}
	public static int dateRange(Date startDate, Date endDate) {
		// 如果两天都是月底，计算月的差额
		return 0;
	}
	public static boolean isLastDayOfMonth(Date date) {
		return true;
	}
}
