package com.example.imgview;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	// 声明地址
	private Button btn, btn_dialog;
	private ImageView img;
	private String url = "http://192.168.0.120:9090/wish/Public/tmp/verifyCode.jpg";

	// 在消息队列中实现对控件的更改
	@SuppressLint("HandlerLeak")
	private Handler handle = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				System.out.println("111");
				Bitmap bmp = (Bitmap) msg.obj;
				img.setImageBitmap(bmp);
				showCustomAlerDialog(bmp);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btn = (Button) findViewById(R.id.btn);
		btn_dialog = (Button) findViewById(R.id.btn_dialog);
		img = (ImageView) findViewById(R.id.img);

		btn.setOnClickListener(this);
		btn_dialog.setOnClickListener(this);

		btn.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// 新建线程加载图片信息，发送到消息队列中
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Bitmap bmp = getURLimage(url);
						Message msg = new Message();
						msg.what = 0;
						msg.obj = bmp;
						System.out.println("000");
						handle.sendMessage(msg);
					}
				}).start();
				return false;
			}

		});
	}

	// 加载图片
	public Bitmap getURLimage(String url) {
		Bitmap bmp = null;
		try {
			URL myurl = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
			conn.setConnectTimeout(6000);// 设置超时
			conn.setDoInput(true);
			conn.setUseCaches(false);// 不缓存
			conn.connect();
			InputStream is = conn.getInputStream();// 获得图片的数据流
			bmp = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}

	private CreateUserDialog createUserDialog;

	public void showEditDialog(View view) {
		createUserDialog = new CreateUserDialog(this, R.style.loading_dialog, onClickListener);
		createUserDialog.show();
	}

	private CreateUserPopWin createUserPopWin;

	public void showEditPopWin(View view) {
		createUserPopWin = new CreateUserPopWin(this, onClickListener);
		//createUserPopWin.showAtLocation(findViewById(R.id.main_view), Gravity.CENTER, 0, 0);
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.btn_save:

				String name = createUserDialog.text_name.getText().toString().trim();
				String mobile = createUserDialog.text_mobile.getText().toString().trim();
				String info = createUserDialog.text_info.getText().toString().trim();

				System.out.println(name + "——" + mobile + "——" + info);
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn:
			// TODO Auto-generated method stub
			// 新建线程加载图片信息，发送到消息队列中
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Bitmap bmp = getURLimage(url);
					Message msg = new Message();
					msg.what = 0;
					msg.obj = bmp;
					System.out.println("000");
					handle.sendMessage(msg);
				}
			}).start();
			break;
		case R.id.btn_dialog:
			Intent intent = new Intent();
			intent.setClass(this, DialogTestActivity.class);
			startActivity(intent);
		}
	}

	private void showCustomAlerDialog(Bitmap bmp) {
		CustomDialog.Builder builder = new CustomDialog.Builder(MainActivity.this);
		builder.setMessage("输入验证码点击确定");
		builder.setTitle("输入验证码以继续");
		builder.setVerify(bmp);
		// LayoutInflater factory = LayoutInflater.from(MainActivity.this);
		// final View dialogView
		// =factory.inflate(R.layout.dialog_normal_layout,null);
		LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// final CustomDialog dialog = new
		// CustomDialog(MainActivity.this,R.style.Dialog);
		final View dialogView = inflater.inflate(R.layout.dialog_normal_layout, null);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// dialog.dismiss();
				// 设置你的操作事项
				TextView tv = (TextView) findViewById(R.id.tv_verifycode);
				EditText et_verifycode = (EditText) dialogView.findViewById(R.id.et_verifycode);
				tv.setText(et_verifycode.getText().toString());
				String temp_str = et_verifycode.getText().toString();
				Log.i("dialog", et_verifycode.getText().toString());
				// dialog.dismiss();
			}
		});

		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// finish();
			}
		});

		builder.create().show();
	}
}
