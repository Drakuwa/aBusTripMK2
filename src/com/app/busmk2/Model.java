package com.app.busmk2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

public class Model {
	
	private Context ctx;
	
	public Model(Context context) {
		ctx = context;
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
		//alert.show();
	}
}