package com.app.busmk2;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class a3 extends Activity {
    
	private WebView myWebView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_pic);
        
        this.setContentView(R.layout.line_pic);
        this.myWebView = (WebView) this.findViewById(R.id.line_pic);
        
        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setBuiltInZoomControls(true);

        this.myWebView.loadUrl("file:///android_asset/a3.jpg");
    }
}