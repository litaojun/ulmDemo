package com.example.registerandlogindemo.pic;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.registerandlogindemo.HttpUtil;
import com.example.registerandlogindemo.LoginActivity;
import com.example.registerandlogindemo.R;
import com.example.registerandlogindemo.UrlConstance;
import com.example.registerandlogindemo.pic.UploadUtil.OnUploadProcessListener;
import com.example.registerandlogindemo.qiniu.QiniuTool;
import com.example.registerandlogindemo.util.InterfaceBodyJson;
import com.github.iscanner.iscanner_android.MainActivity;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.imtoy.wechatdemo.R;
//import com.imtoy.wechatdemo.activity.UploadUtil.OnUploadProcessListener;

/**
 * @author spring sky<br>
 * Email :vipa1888@163.com<br>
 * QQ: 840950105<br>
 * 说明：主要用于选择文件和上传文件操作
 */
public class WeixinMainActivity extends Activity implements OnClickListener,OnUploadProcessListener{
	private static final String TAG = "uploadImage";
	
	/**
	 * 去上传文件
	 */
	protected static final int TO_UPLOAD_FILE = 1;  
	/**
	 * 上传文件响应
	 */
	protected static final int UPLOAD_FILE_DONE = 2;  //
	/**
	 * 选择文件
	 */
	public static final int TO_SELECT_PHOTO = 3;
	/**
	 * 上传初始化
	 */
	private static final int UPLOAD_INIT_PROCESS = 4;
	/**
	 * 上传中
	 */
	private static final int UPLOAD_IN_PROCESS = 5;
	/***
	 * 这里的这个URL是我服务器的javaEE环境URL
	 */
	private static String requestURL = "http://192.168.10.160:8080/fileUpload/p/file!upload";
//	private static String requestURL = "http://img.epalmpay.cn/order_userpic.php";
	private Button updateButton,uploadButton,scanButton;
	private ImageView imageView;
	private TextView uploadImageResult;
	private ProgressBar progressBar;
	
	private String picPath = null;
	private ProgressDialog progressDialog;
	

	private EditText edittext = null, openidtext;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_from_wxmain);
		imageView = (ImageView) findViewById(R.id.authcode_iv);
		edittext = (EditText) findViewById(R.id.weixinname);
		openidtext = (EditText) findViewById(R.id.openid);
		edittext.setText(UrlConstance.nickname);
		openidtext.setText(UrlConstance.username);
		initImage();
        initView();
    }
	Handler mQQHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what)
			{
				case(0):
					JSONObject response = (JSONObject) msg.obj;
				    break;
				case(1):
					BitmapDrawable bitmap = (BitmapDrawable) msg.obj;
				    if(bitmap!=null)
				       imageView.setImageDrawable(bitmap);
				    else
				    	Toast.makeText(WeixinMainActivity.this, "下载图像出错", Toast.LENGTH_LONG).show();
				    break;
				case(2):
					//Bitmap bitmapq = (Bitmap) msg.obj;
					//Toast.makeText(this, "上传文件成功", Toast.LENGTH_LONG).show();
					 String filename = (String) msg.obj;
				  progressDialog.dismiss();
				   if(filename!=null)
				   {
					System.out.println("上传文件成功");
				   
				    UrlConstance.imgurl = filename;
				    Toast.makeText(WeixinMainActivity.this, "上传图像成功", Toast.LENGTH_LONG).show();
				    
				   }
				   else
					   Toast.makeText(WeixinMainActivity.this, "上传图像失败", Toast.LENGTH_LONG).show();
					
			}
//			if (msg.what == 0) {
//				JSONObject response = (JSONObject) msg.obj;
//				// String nc = response.getString("nickname");
//				// gotoQQMsg(response.toString());
//			} else if (msg.what == 1) {
//				Bitmap bitmap = (Bitmap) msg.obj;
//				imageView.setImageBitmap(bitmap);
//				//imageView.setOnClickListener(this);
//				//imageView.setOnClickListener(this);
//			}
		}

	};
	public void initImage()
	{
		if(UrlConstance.imgurl!=null && !UrlConstance.imgurl.equals(""))
		{
		new Thread() {

			@Override
			public void run() {

				if (true) {
					Bitmap bitmap = null;
					String bitmapurl = null;
					bitmap = Util.getbitmap("http://obiuz0r1q.bkt.clouddn.com/"+UrlConstance.imgurl);
					// bitmapurl = json.getString("figureurl_qq_2");
					Message msg = new Message();
					
					BitmapDrawable  upa = updateBimpSize(bitmap);
					msg.obj = upa;
					msg.what = 1;
					
					mQQHandler.sendMessage(msg);
				}
			}

		}.start();
		}
	}
    
    /**
     * 初始化数据
     */
	private void initView() {
        updateButton = (Button) this.findViewById(R.id.userupdate);
        uploadButton = (Button) this.findViewById(R.id.uploadImage);
        updateButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
       // imageView = (ImageView) this.findViewById(R.id.imageView);
		uploadImageResult = (TextView) findViewById(R.id.uploadImageResult);
        imageView = (ImageView) findViewById(R.id.authcode_iv);
        imageView.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        scanButton = (Button) this.findViewById(R.id.scanbutton);
        scanButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.authcode_iv:
			Intent intent = new Intent(this,SelectPicActivity.class);
			startActivityForResult(intent, TO_SELECT_PHOTO);
			break;
		case R.id.uploadImage:
			if(picPath!=null)
			{
				handler.sendEmptyMessage(TO_UPLOAD_FILE);
			}else{
				Toast.makeText(this, "上传的文件路径出错", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.userupdate:
			doupdateuser();
			break;
		case R.id.scanbutton:
			doScanLogin();
			break;
		default:
			break;
		}
	}
	public void doScanLogin()
	{
	    Intent intent = new Intent();
        intent.setClass(WeixinMainActivity.this, MainActivity.class);
        startActivity(intent);
	}
	public void doupdateuser()
	{
		String username = UrlConstance.username;
		String nickname = edittext.getText().toString();
		HttpUtil.post(InterfaceBodyJson.userupdateurl, getParames(nickname,username), responseHandler);
	}
	private RequestParams getParames(String nickname,String username){
        RequestParams params = new RequestParams();
        params.put(InterfaceBodyJson.nickname, nickname);
        params.put(InterfaceBodyJson.username,username);
        params.put(InterfaceBodyJson.imgurl, UrlConstance.imgurl);
        return params;
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO)
		{
			picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
			Log.i(TAG, "最终选择的图片="+picPath);
			Bitmap bm = BitmapFactory.decodeFile(picPath);
			  int width = bm.getWidth(); 
		        int height = bm.getHeight(); 
		        //定义预转换成的图片的宽度和高度 
		        int newWidth = 100; 
		        int newHeight = 100;
		      //计算缩放率，新尺寸除原始尺寸 
		        float scaleWidth = ((float) newWidth) / width; 
		        float scaleHeight = ((float) newHeight) / height; 
		        // 创建操作图片用的matrix对象 
		        Matrix matrix = new Matrix(); 
		        // 缩放图片动作 
		        matrix.postScale(scaleWidth, scaleHeight); 

		        //旋转图片 动作 
		        //matrix.postRotate(45); 

		        // 创建新的图片 
		        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, 
		                          width, height, matrix, true); 
		        BitmapDrawable bmd = new BitmapDrawable(this.getResources(),resizedBitmap); 
		        imageView.setImageDrawable(bmd); 
			//imageView.setImageBitmap(bm);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	public BitmapDrawable updateBimpSize(Bitmap bm)
	{
		if(bm==null)
			return null;
		int width = bm.getWidth(); 
        int height = bm.getHeight(); 
        //定义预转换成的图片的宽度和高度 
        int newWidth = 100; 
        int newHeight = 100;
      //计算缩放率，新尺寸除原始尺寸 
        float scaleWidth = ((float) newWidth) / width; 
        float scaleHeight = ((float) newHeight) / height; 
        // 创建操作图片用的matrix对象 
        Matrix matrix = new Matrix(); 
        // 缩放图片动作 
        matrix.postScale(scaleWidth, scaleHeight); 

        //旋转图片 动作 
       // matrix.postRotate(45); 

        // 创建新的图片 
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, 
                          width, height, matrix, true); 
        BitmapDrawable bmd = new BitmapDrawable(this.getResources(),resizedBitmap); 
        return bmd;
	}

	/**
	 * 上传服务器响应回调
	 */
	@Override
	public void onUploadDone(int responseCode, String message) {
		progressDialog.dismiss();
		Message msg = Message.obtain();
		msg.what = UPLOAD_FILE_DONE;
		msg.arg1 = responseCode;
		msg.obj = message;
		handler.sendMessage(msg);
	}
	
	private void toUploadFile()
	{
		uploadImageResult.setText("正在上传中...");
		progressDialog.setMessage("正在上传文件...");
		progressDialog.show();
		QiniuTool qiniutoole = new QiniuTool();
    	//String fileKey = "pic";
    	String fileKey = UUID.randomUUID().toString();
    	qiniutoole.upload(picPath, fileKey, mQQHandler);
//		UploadUtil uploadUtil = UploadUtil.getInstance();;
//		uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态
//		
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("orderId", "11111");
//		uploadUtil.uploadFile( picPath,fileKey, requestURL,params);
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TO_UPLOAD_FILE:
				toUploadFile();
				break;
			
			case UPLOAD_INIT_PROCESS:
				progressBar.setMax(msg.arg1);
				break;
			case UPLOAD_IN_PROCESS:
				progressBar.setProgress(msg.arg1);
				break;
			case UPLOAD_FILE_DONE:
				String result = "响应码："+msg.arg1+"\n响应信息："+msg.obj+"\n耗时："+UploadUtil.getRequestTime()+"秒";
				uploadImageResult.setText(result);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};

	@Override
	public void onUploadProcess(int uploadSize) {
		Message msg = Message.obtain();
		msg.what = UPLOAD_IN_PROCESS;
		msg.arg1 = uploadSize;
		handler.sendMessage(msg );
	}

	@Override
	public void initUpload(int fileSize) {
		Message msg = Message.obtain();
		msg.what = UPLOAD_INIT_PROCESS;
		msg.arg1 = fileSize;
		handler.sendMessage(msg );
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
//			    	    UrlConstance.loginsign = true;
//			    	    String imgurl = so.getString("imgurl");
//			    	    String nickname = so.getString("nickname");
//			    	    UrlConstance.imgurl = imgurl;
//			    	    UrlConstance.nickname = nickname;
//			    	    Intent intent = new Intent();
//				        intent.setClass(WeixinMainActivity.this, WeixinMainActivity.class);
//				        startActivity(intent);
//				        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
//				        finish();
			    	 Toast.makeText(WeixinMainActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
			     }
			     else
					{
	                        Toast.makeText(WeixinMainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
					}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
            //Log.e(tag, "onFailure====");
        	 Toast.makeText(WeixinMainActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
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


    };
	
}