package com.example.registerandlogindemo.util;

import java.io.IOException;

import com.example.registerandlogindemo.qiniu.UploadDemo;

public class InterfaceBodyJson 
{
	public static String useraddurl = "b001.001.001";
	public static String username= "phoneNo";
	public static String password= "password";
	public static String nickname= "nickname";
	
	public static String userloginurl = "b001.001.002";
	public static String sessionId= "sessionId";
	public static String imgurl= "avatar";
	
	public static String userupdateurl = "b001.001.003";
	
	
	public static String userqueryurl = "b001.001.004"; //用户信息异步查询
	public static String viewCode = "viewCode";
	public static String userId = "userId";
	
	public static String userscanloginurl = "b001.001.006"; //扫码登录
	
	public static String getkeyurl = "b000.001.001";   //获取公钥
	public static String keyAlias = "keyAlias";
	public static String publicKey = "publicKey";
	  public static void main(String args[]) throws IOException{  
		    new UploadDemo().upload();
		  }

}
