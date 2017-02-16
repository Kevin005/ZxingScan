package com.example.myzxingscan;

import java.io.File;

import com.karics.library.zxing.android.CaptureActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	private static final int REQUEST_CODE_SCAN = 0x0000;

	private static final String DECODED_CONTENT_KEY = "codedContent";
	private static final String DECODED_BITMAP_KEY = "codedBitmap";
	
	private Button button,button2;
	private TextView tv;
	private EditText edittext;
	private CheckBox checkBox;
	private ImageView imageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		button = (Button) findViewById(R.id.button1);
		
		tv = (TextView) findViewById(R.id.textView1);
		
		edittext = (EditText) findViewById(R.id.editText1);
		
		checkBox = (CheckBox) findViewById(R.id.checkBox1);
		
		imageView = (ImageView) findViewById(R.id.imageView1);
		
		button2 = (Button) findViewById(R.id.button2);
		
		
		
		button.setOnClickListener(this);
		button2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.button1://扫描二维码
			//跳转到拍照界面扫描二维码
			Intent intent = new Intent(MainActivity.this,
					CaptureActivity.class);
			startActivityForResult(intent, REQUEST_CODE_SCAN);
			
			
			break;
		case R.id.button2://生成二维码 带logo或不带logo
	                final String filePath = getFileRoot(MainActivity.this) + File.separator
	                        + "qr_" + System.currentTimeMillis() + ".jpg";

	                System.out.println("文件目录  ："+filePath);
	                
	                //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
	                new Thread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	//三目表达式  如果checkbox是被选中的二维码带logo否则不带
	                        
	                    	if (!"".equals(edittext.getText().toString().trim())) {
								
	                    		boolean success = QRCodeUtil.createQRImage(edittext.getText().toString().trim(), 800, 800,
		                        		checkBox.isChecked() ? BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher) : null,
		                                filePath);
	                    		
	                    		  if (success) {
	  	                            runOnUiThread(new Runnable() {
	  	                                @Override
	  	                                public void run() {
	  	                                    imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
	  	                                }
	  	                            });
	  	                        }
							}else{
								
								Toast.makeText(getApplicationContext(), "输入内容不能为空", Toast.LENGTH_SHORT).show();
							}

	                    }
	                }).start();

			break;
		default:
			break;
		}
	}

	  //获取文件根目录
    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 扫描二维码/条码回传
		if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
			if (data != null) {

				//扫描的结果
				String content = data.getStringExtra(DECODED_CONTENT_KEY);
				

				tv.setText("解码结果： \n" + content);
				
				//相当于把扫描的二维码显示到一个imageView上
//				Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
//				
//				imageView.setImageBitmap(bitmap);
			}
		}
	}
}
