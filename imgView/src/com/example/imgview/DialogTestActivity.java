package com.example.imgview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DialogTestActivity extends Activity {
	Button btnTEDlg;
	Button btnTEDlg2;
	private Button okButton;
	private Button cancelButton;
	private EditText nameEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog_test);

		System.out.println("-------------->5");
		btnTEDlg = (Button) findViewById(R.id.btn_dialog_click);
		btnTEDlg.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				System.out.println("-------------->3");
				LayoutInflater factory = LayoutInflater.from(DialogTestActivity.this);
				final View textEntryView = factory.inflate(R.layout.dialog, null);
				AlertDialog dlg = new AlertDialog.Builder(DialogTestActivity.this)

				.setTitle("User Login").setView(textEntryView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						System.out.println("-------------->6");
						EditText secondPwd = (EditText) textEntryView.findViewById(R.id.username_edit);
						String inputPwd = secondPwd.getText().toString();
						setTitle(inputPwd);
						System.out.println("-------------->1");
						TextView tv = (TextView) findViewById(R.id.tv_password);
						tv.setText(inputPwd);
						// 输入的内容会在页面上显示来因为是做来测试，所以功能不是很全，只写了username没有学password

					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						System.out.println("-------------->2");

					}
				}).create();
				dlg.show();
			}
		});
	}
}
