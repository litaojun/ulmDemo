package com.example.registerandlogindemo;

/**
 * @author itlanbao
 * 处理网络的参数常量类
 */
public class UrlConstance {
	 
	 public static final String ACCESSTOKEN_KEY ="accesstoken";
	 
	   
   public static String sessionkey="session";
   public static String username="";
   public static String password="";
   public static String nickname="";
   public static String imgurl="";
   public static boolean loginsign=false;
   //签约公钥，即客户端与服务器协商订的一个公钥
   public static final String PUBLIC_KEY ="*.itlanbao.com";
   //public static final String APP_URL = "http://192.168.43.120:8080/";
   public static final String APP_URL = "http://www.itlanbao.com/api/app/";
   
   //4.6注册用户接口
   //public static final String KEY_REGIST_INFO ="useradd";
   public static final String KEY_REGIST_INFO ="users/user_register_Handler.ashx";
   
   //4.8登录用户接口
   public static final String KEY_LOGIN_INFO ="userlogin";

   //4.9获取用户基本信息
   public static final String KEY_USER_BASE_INFO ="userget";
   
}
