package com.wyf.util;

/**
 * 输出日志
 *
 *@author: Weiyf
 *@Date: 2019-02-25 15:40
 */
public class LogUtil {

    public static void infoMsg(String msg,String... args){
        logger("info===>" + msg,args);
    }

    public static void errorMsg(String msg,String... args){
        logger("error===>" + msg,args);
    }

    private static void logger(String msg,String... args){
        System.out.println(String.format(msg,args));
    }

}
