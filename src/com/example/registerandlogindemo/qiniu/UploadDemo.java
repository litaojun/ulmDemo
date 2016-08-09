package com.example.registerandlogindemo.qiniu;

import com.qiniu.util.Auth;

import android.util.Log;

import com.qiniu.android.storage.UploadOptions;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpToken;

import java.io.IOException;

import org.json.JSONObject;

//import com.qiniu.common.QiniuException;
//import com.qiniu.android.http.Response;
//import com.qiniu.storage.UploadManager;
import com.qiniu.android.storage.UploadManager;

public class UploadDemo {
  //设置好账号的ACCESS_KEY和SECRET_KEY
  String ACCESS_KEY = "_Gcw7EfKEUGc-R93LyW2gVPBcVBkR-ebE3q0YWY9";
  String SECRET_KEY = "x8RU3iSL20o5yH_1LuEBhJ3nX71scHFHYgYA5T4S";
  //要上传的空间
  String bucketname = "ltjimg";
  //上传到七牛后保存的文件名
  String key = "my-java.png";
  //上传文件的路径
  String FilePath = "C:\\Users\\litaojun\\Pictures\\用户相关场景\\Screenshot_2016-04-12-10-20-48.png";

  //密钥配置
  Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
  //创建上传对象
  UploadManager uploadManager = new UploadManager();

  //简单上传，使用默认策略，只需要设置上传的空间名就可以了
  public String getUpToken(){
      return auth.uploadToken(bucketname);
  }

  public void upload() throws IOException{
    //调用put方法上传
      uploadManager.put(FilePath, key, getUpToken(),new UpCompletionHandler() {
			@Override
			public void complete(String arg0, ResponseInfo arg1, JSONObject arg2) {
				// TODO Auto-generated method stub
				 //Log.i("qiniu", arg0 + ",\r\n " + arg1 + ",\r\n " + arg2);
				System.out.println("qiniu"+arg0 + ",\r\n " + arg1.host + ",\r\n " + arg2.toString());
			}
  	}, null);
      //打印返回的信息
      //System.out.println(res.bodyString());        
  }

  public static void main(String args[]) throws IOException{  
    new UploadDemo().upload();
  }

}


