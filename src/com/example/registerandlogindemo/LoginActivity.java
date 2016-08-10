package com.example.registerandlogindemo;

import java.util.Locale;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.registerandlogindemo.pic.WeixinMainActivity;
import com.example.registerandlogindemo.util.InterfaceBodyJson;
import com.example.registerandlogindemo.util.RSAUtils;
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
                	UrlConstance.password = password;
                	RequestParams params = new RequestParams();
                    params.put(InterfaceBodyJson.keyAlias,"test");
                	HttpUtil.post(InterfaceBodyJson.getkeyurl, params, responseHandlerPubkey);
                	//HttpUtil.post(InterfaceBodyJson.userloginurl, getParames(account,password), responseHandler);
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
    };
    
    private TextHttpResponseHandler  responseHandlerPubkey =  new TextHttpResponseHandler()
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
			    	   // UrlConstance.loginsign = true;
			    	    String key = "";
			    	    key = so.getString("publicKey");
			    	    String password = RSAUtils.encryptBase64(UrlConstance.password, key);
			    	    HttpUtil.post(InterfaceBodyJson.userloginurl, getParames(UrlConstance.username,password), responseHandler);
			     }
			     else
					{
	                        Toast.makeText(LoginActivity.this, "获取密钥失败--2", Toast.LENGTH_SHORT).show();
					}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
            //Log.e(tag, "onFailure====");
        	Toast.makeText(LoginActivity.this, "获取密钥失败", Toast.LENGTH_SHORT).show();
            //Log.e(tag, "statusCode:" + statusCode + " headers:" + builder.toString(), e);
        }
        @Override
        public void onRetry(int retryNo) {
            // called when request is retried
        }
    };

}
