package com.example.registerandlogindemo;

import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.Header;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.registerandlogindemo.util.InterfaceBodyJson;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

//import org.apache.http.Header;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
//implements HttpResponeCallBack
public class RegisterActivity extends Activity  {

    private EditText loginNick;//用户昵称
    private EditText email;//注册邮箱
    private EditText password;//注册密码
    private Button registBtn;//注册

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        loginNick = (EditText) findViewById(R.id.regist_nick);
        email = (EditText) findViewById(R.id.regist_account);
        password = (EditText) findViewById(R.id.regist_password);
        registBtn = (Button) findViewById(R.id.regist_btn);

        registBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //获得用户输入的信息
                String nick = loginNick.getText().toString();
                String emailStr = email.getText().toString();
                String passwordStr = password.getText().toString();
                if (!TextUtils.isEmpty(nick) &&
                        !TextUtils.isEmpty(emailStr)
                        && !TextUtils.isEmpty(passwordStr)) {
                    if (Utils.isMobileNO(emailStr)) {//验证邮箱格式是否符合
                    	//RequestParams params = new RequestParams();
                    	HttpUtil.post(InterfaceBodyJson.useraddurl, getParames(nick,emailStr,passwordStr), responseHandler);
//                        RequestApiData.getInstance().getRegistData(nick, emailStr, passwordStr,
//                                AnalyticalRegistInfo.class, RegisterActivity.this);
                    } else {
                        Toast.makeText(RegisterActivity.this, "输入邮箱有误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "输入信息未完全", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
     
    private RequestParams getParames(String nickname,String username,String password){
        RequestParams params = new RequestParams();
        params.put(InterfaceBodyJson.nickname, nickname);
        params.put(InterfaceBodyJson.username,username);
        params.put(InterfaceBodyJson.password,password);
        return params;
    }
    private TextHttpResponseHandler  responseHandler =  new TextHttpResponseHandler()
    {
        @Override
        public void onStart() {
            System.out.println("onStart====");
        }
        
        @Override
		public void onSuccess(int i, Header aheader[], String s)
        {
        	JSONObject so;
			try {
				so = new JSONObject(s);
				//if(so.getInt("success")==1)
				if(so.getBoolean("success"))
				{
	                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
	                RegisterActivity.this.startActivity(intent);
	                Toast.makeText(RegisterActivity.this, "注册成功...", Toast.LENGTH_SHORT).show();
	                RegisterActivity.this.finish();
				}
				else
				{
					Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        }
//        @Override
//        public void onSuccess(int statusCode, Header[] headers, String responseBody) {
////            Log.e(tag, "onSuccess====");
//            StringBuilder builder = new StringBuilder();
//            for (Header h : headers) {
//                String _h = String.format(Locale.US, "%s : %s", h.getName(), h.getValue());
//                builder.append(_h);
//                builder.append("\n");
//            }
//           // Log.e(tag, "statusCode:" + statusCode + " headers:" + builder.toString() + " response:" + response);
//        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) 
        {
        	Toast.makeText(RegisterActivity.this, "注册失败...", Toast.LENGTH_SHORT).show();
        	Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            RegisterActivity.this.startActivity(intent);
            //Toast.makeText(RegisterActivity.this, "注册成功...", Toast.LENGTH_SHORT).show();
            RegisterActivity.this.finish();
            //Log.e(tag, "onFailure====");
//            StringBuilder builder = new StringBuilder();
//            for (Header h : headers) {
//                String _h = String.format(Locale.US, "%s : %s", h.getName(), h.getValue());
//                builder.append(_h);
//                builder.append("\n");
//            }
            //Log.e(tag, "statusCode:" + statusCode + " headers:" + builder.toString(), e);
        }

        @Override
        public void onRetry(int retryNo) {
            // called when request is retried
        }

//		@Override
//		public void onFailure(int arg0, cz.msebera.android.httpclient.Header[] arg1, String arg2, Throwable arg3) {
//			// TODO Auto-generated method stub
//			
//		}

//		@Override
//		public void onSuccess(int arg0, cz.msebera.android.httpclient.Header[] arg1, String arg2) {
//			// TODO Auto-generated method stub
//			
//		}
    };

//    @Override
//    public void onResponeStart(String apiName) {
//        // TODO Auto-generated method stub
//        Toast.makeText(RegisterActivity.this, "正在请求数据...", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onLoading(String apiName, long count, long current) {
//        Toast.makeText(RegisterActivity.this, "Loading...", Toast.LENGTH_SHORT).show(); 
//    }
//
//    @Override
//    public void onSuccess(String apiName, Object object) {
//        // TODO Auto-generated method stub
//        //注册接口
//    	System.out.println("onSuccess");
//        if (UrlConstance.KEY_REGIST_INFO.equals(apiName)) {
//        	 if (object != null) {
//        		 JSONObject so;
//				try {
//					so = new JSONObject(object.toString());
//					if(so.getInt("retcode")==0)
//					{
//	                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//	                    RegisterActivity.this.startActivity(intent);
//	
//	                    Toast.makeText(RegisterActivity.this, "注册成功...", Toast.LENGTH_SHORT).show();
//	
//	                    RegisterActivity.this.finish();
//					}
//					else
//					{
//						Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//					
// //           if (object != null && object instanceof AnalyticalRegistInfo) {
////                AnalyticalRegistInfo info = (AnalyticalRegistInfo) object;
////                String successCode = info.getRet();
////                //请求成功
////                if (successCode.equals(Constant.KEY_SUCCESS)) {
////                    UserBaseInfo baseUser = new UserBaseInfo();
////                    baseUser.setEmail(info.getEmail());
////                    baseUser.setNickname(info.getNickname());
////                    baseUser.setUserhead(info.getUserhead());
////                    baseUser.setUserid(String.valueOf(info.getUserid()));
////                    ItLanBaoApplication.getInstance().setBaseUser(baseUser);
////                    UserPreference.save(KeyConstance.IS_USER_ID, String.valueOf(info.getUserid()));
////                    UserPreference.save(KeyConstance.IS_USER_ACCOUNT, info.getEmail());
////                    UserPreference.save(KeyConstance.IS_USER_PASSWORD, password.getText().toString());
////
////
////                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
////                    RegisterActivity.this.startActivity(intent);
////
////                    Toast.makeText(RegisterActivity.this, "注册成功...", Toast.LENGTH_SHORT).show();
////
////                    RegisterActivity.this.finish();
////
////                } else {
////                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
////                }
//            }
//        }
//
//    }
//
//    @Override
//    public void onFailure(String apiName, Throwable t, int errorNo, String strMsg) {
//    	System.out.println(apiName+"onFailure");
//        Toast.makeText(RegisterActivity.this, "Failure", Toast.LENGTH_SHORT).show();
//    }
}
