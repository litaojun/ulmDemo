package com.example.registerandlogindemo.qiniu;

import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.Zone;
import com.qiniu.util.Auth;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class QiniuTool 
{
	private UploadManager uploadManager = new UploadManager();
	Auth auth=null;
	String bucketname = "ltjimg";
    public QiniuTool()
    {
    	 String ACCESS_KEY = "_Gcw7EfKEUGc-R93LyW2gVPBcVBkR-ebE3q0YWY9";
    	  String SECRET_KEY = "x8RU3iSL20o5yH_1LuEBhJ3nX71scHFHYgYA5T4S";
    	  
    	   auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    	Configuration config = new Configuration.Builder()
                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
                .connectTimeout(10) // 链接超时。默认10秒
                .responseTimeout(60) // 服务器响应超时。默认60秒
//                .recorder(recorder)  // recorder分片上传时，已上传片记录器。默认null
//                .recorder(recorder, keyGen)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认 Zone.zone0
                .build();
               //重用uploadManager。一般地，只需要创建一个uploadManager对象
              uploadManager = new UploadManager(config);


    }
    public String getUpToken(String bucketname){
        return auth.uploadToken(bucketname);
    }
    public void upload(String data,String key,final Handler handle)
    {
    	// 重用uploadManager。一般地，只需要创建一个uploadManager对象
    	//UploadManager uploadManager = new UploadManager();
    	//String data = "";//<File对象、或 文件路径、或 字节数组>
    	//String key = "test";//<指定七牛服务上的文件名，或 null>;
    	String token = this.getUpToken(bucketname);//<从服务端SDK获取>;
    	final String filename = key;
    	uploadManager.put(data, key, token,
    	new UpCompletionHandler() {
//    	    @Override
//    	    public void complete(String key, ResponseInfo info, JsonObject res) {
//    	        //res包含hash、key等信息，具体字段取决于上传策略的设置。 
//    	        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
//    	    }

			@Override
			public void complete(String arg0, ResponseInfo arg1, JSONObject arg2) {
				// TODO Auto-generated method stub
				 Log.i("qiniu", arg0 + ",\r\n " + arg1 + ",\r\n " + arg2);
				// arg1.error
				 Message msg = Message.obtain();
					msg.what = 2;
				 if(arg1.isOK())
				 {

					//msg.arg1 = arg1;
					msg.obj = filename;
				 }
				 else
					 msg.obj = null;
				 handle.sendMessage(msg);
			}
    	}, null);
    }
}
