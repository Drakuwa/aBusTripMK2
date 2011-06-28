package com.app.busmk2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

public class a3 extends Activity {
    
	private WebView myWebView;
	private static final FrameLayout.LayoutParams ZOOM_PARAMS =
	new FrameLayout.LayoutParams(
	ViewGroup.LayoutParams.FILL_PARENT,
	ViewGroup.LayoutParams.WRAP_CONTENT,
	Gravity.BOTTOM);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_pic);
        
        this.setContentView(R.layout.line_pic);
        this.myWebView = (WebView) this.findViewById(R.id.line_pic);
        
        FrameLayout mContentView = (FrameLayout) getWindow().
        getDecorView().findViewById(android.R.id.content);
        final View zoom = this.myWebView.getZoomControls();
        mContentView.addView(zoom, ZOOM_PARAMS);
        zoom.setVisibility(View.GONE);

        this.myWebView.loadUrl("file:///android_asset/a3.jpg");
        //this.myWebView.setBackgroundResource(R.drawable.a3);
    }
}
