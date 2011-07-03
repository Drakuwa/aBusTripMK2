package com.app.busmk2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

public class Model {

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

	public AlertDialog create_list(final String[] items, Context ctx) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle("Избери станица:");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {

			}
		});
		AlertDialog alert = builder.create();
		return alert;
		// alert.show();
	}
}