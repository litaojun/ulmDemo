package com.example.registerandlogindemo;

import java.util.Locale;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.registerandlogindemo.pic.WeixinMainActivity;
import com.example.registerandlogindemo.util.InterfaceBodyJson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

    private EditText loginAccount;//账号
    private EditText loginPassword;//密码
    private Button loginBtn;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_login);
        init();
    }

    /**
     * 初始化数据
     */
    private void init() {
        loginAccount = (EditText) findViewById(R.id.login_account);
        loginPassword = (EditText) findViewById(R.id.login_password);
        loginBtn = (Button) findViewById(R.id.login_btn);
        registerBtn = (Button) findViewById(R.id.register_btn);
        //点击登录按钮
        loginBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String account = loginAccount.getText().toString();//账号
                String password = loginPassword.getText().toString();//密码
                if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)
                        && Utils.isMobileNO(account))
                {
                	UrlConstance.username = account;
                	
                	HttpUtil.post(InterfaceBodyJson.userloginurl, getParames(account,password), responseHandler);
//                    RequestApiData.getInstance().getLoginData(account, password, UserBaseInfo.class, LoginActivity.this);
                } else {
                    Toast.makeText(LoginActivity.this, "账号或者密码有误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();
            }
        });
    }
    private RequestParams getParames(String username,String password){
        RequestParams params = new RequestParams();

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
        	try {
				JSONObject so=new JSONObject(s);
				boolean a= so.getBoolean("success");
			     if(a)
			     {
			    	    UrlConstance.loginsign = true;
			    	    String imgurl = "";
			    	    if(so.has("avatar"))
			    	        imgurl = so.getString("avatar");
			    	    String nickname = so.getString("nickname");
			    	    UrlConstance.sessionkey = so.getString("session");
			    	    UrlConstance.imgurl = imgurl;
			    	    UrlConstance.nickname = nickname;
			    	    Intent intent = new Intent();
				        intent.setClass(LoginActivity.this, WeixinMainActivity.class);
				        startActivity(intent);
				        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				        finish();
			     }
			     else
					{
	                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
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
        public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
            //Log.e(tag, "onFailure====");
        	Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
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
//
//        if (UrlConstance.KEY_LOGIN_INFO.equals(apiName)) {
//            Toast.makeText(LoginActivity.this, "正在加载数据中", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onLoading(String apiName, long count, long current) {
//        // TODO Auto-generated method stub
//        Toast.makeText(LoginActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onSuccess(String apiName, Object object) {
//        // TODO Auto-generated method stub
//        if (UrlConstance.KEY_LOGIN_INFO.equals(apiName)) {
//            //邮箱登录返回数据
//        	if (object != null ) {
//        		try {
//					JSONObject so=new JSONObject(object.toString());
//					if(so.getInt("retcode")==0)
//					{
//						UrlConstance.sessionkey = so.getJSONObject("user").getString("sessionkey");
//	                    Intent intent = new Intent();
//	                    intent.setClass(LoginActivity.this, MainActivity.class);
//	                    startActivity(intent);
//	                    overridePendingTransition(android.R.anim.slide_in_left,
//	                            android.R.anim.slide_out_right);
//	                    finish();
//					}
//					else
//					{
//
//	                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//        	}
////            if (object != null && object instanceof UserBaseInfo) {
////                UserBaseInfo info = (UserBaseInfo) object;
////                if (info.getRet().equals(Constant.KEY_SUCCESS)) {
////
////                    //登录成功，保存登录信息
////                    ItLanBaoApplication.getInstance().setBaseUser(info);//保存到Application中
////
////                    //保存到SP中
////                    UserPreference.save(KeyConstance.IS_USER_ID, String.valueOf(info.getUserid()));
////
////                    UserPreference.save(KeyConstance.IS_USER_ACCOUNT, info.getEmail());
////                    UserPreference.save(KeyConstance.IS_USER_PASSWORD, loginPassword.getText().toString());
////
////
////                    Intent intent = new Intent();
////                    intent.setClass(LoginActivity.this, MainActivity.class);
////                    startActivity(intent);
////                    overridePendingTransition(android.R.anim.slide_in_left,
////                            android.R.anim.slide_out_right);
////                    finish();
////
////                } else {
////                    Log.e("TAG", "info="+info.toString());
////                    if (info.getErrcode().equals(Constant.KEY_NO_REGIST)) {
////                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
////                    } else {
////                        Toast.makeText(LoginActivity.this, info.getMsg(), Toast.LENGTH_SHORT).show();
////                        Log.e("TAG", "info.getMsg()="+info.getMsg());
////                    }
////
////                }
////            }
//        }
//
//    }
//
//    @Override
//    public void onFailure(String apiName, Throwable t, int errorNo,
//                          String strMsg) {
//        // TODO Auto-generated method stub
//        Toast.makeText(LoginActivity.this, "Failure", Toast.LENGTH_SHORT).show();   
//    }
}
