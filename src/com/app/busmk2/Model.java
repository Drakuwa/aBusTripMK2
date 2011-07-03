package com.app.busmk2;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.widget.Toast;

public class Model extends Activity {

	private Context ctx;

	public Model(Context context) {
		ctx = context;
	}

	public void email_toast() {
		Toast
				.makeText(
						ctx,
						"Android верзија "
								+ Build.VERSION.RELEASE
								+ " не подржува директно повикување "
								+ "на Email клиент. Испратете mail на drakuwa@gmail.com",
						Toast.LENGTH_LONG).show();
	}

	public void first_run() {
		boolean exists = (new File("/data/data/com.app.busmk2/notwelcomefirst"))
				.exists();

		if (!exists) {
			// Welcome note...
			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setMessage(
					"Добредојдовте во aBusTripMK2, "
							+ "за повеќе информации и начин на користење, "
							+ "погледнете во 'More...' делот ").setIcon(
					R.drawable.icon).setTitle(R.string.app_name).setCancelable(
					false).setPositiveButton("OK..",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
			try {
				new File("/data/data/com.app.busmk2/notwelcomefirst")
						.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//TODO
	
}