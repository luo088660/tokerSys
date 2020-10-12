package com.toker.reslib.sm;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.toker.reslib.aesrsa.RSA;
import com.toker.reslib.sm.SM4Test;
import com.toker.reslib.sm4.Base64Utils;
import com.toker.reslib.sm4.RSAUtils;

import java.security.PrivateKey;
import java.text.ParseException;
import java.util.TreeMap;

public class Text {


    public static void main(String[] ager) {


        long daySub = getDaySub("1564725175000", "1564725175000");
        System.out.println(daySub);



    }
    /**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr,String endDateStr)
    {
        long day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate;
        java.util.Date endDate;
        try
        {
            beginDate = format.parse(beginDateStr);
            endDate= format.parse(endDateStr);
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
            //System.out.println("相隔的天数="+day);
        } catch (ParseException e)
        {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return day;
    }



}
