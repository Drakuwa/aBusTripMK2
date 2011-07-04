package com.app.busmk2.lines;

import com.app.busmk2.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * A simple WebView activity class that shows a picture. A WebView is used
 * because of its native zoom controls, and the support for multitouch zoom
 * (pinch to zoom).
 * 
 * @author drakuwa
 * 
 */
public class a19 extends Activity {

	private WebView myWebView;

	/**
	 * An override of the onCreate method that initializes layout, zoom controls
	 * and source of the picture.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_pic);

		//Initialize the layout, and connect the layout object with the webView
		this.setContentView(R.layout.line_pic);
		this.myWebView = (WebView) this.findViewById(R.id.line_pic);

		//Add the native zoom controls.
		myWebView.getSettings().setSupportZoom(true);
		myWebView.getSettings().setBuiltInZoomControls(true);

		//Load the picture from the given file location.
		this.myWebView.loadUrl("file:///android_asset/a19.jpg");
	}
}
