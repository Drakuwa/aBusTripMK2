package com.app.busmk2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class map extends MapActivity {

	private LocationManager lm;
	
	private static final int MENU_MY_LOCATION = Menu.FIRST;
    private static final int MENU_MAP_MODE = MENU_MY_LOCATION + 1;
    private static final int MENU_CLEAR_MAP = MENU_MAP_MODE + 1;
    private static final int MENU_ABOUT = MENU_CLEAR_MAP + 1;
    
    private static final int DIALOG_ABOUT_ID = 1;
    private MapView mapView;
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		
		DataBaseHelper myDb = new DataBaseHelper(null);
        myDb = new DataBaseHelper(this);
 
        try {
        		myDb.createDataBase();
        } catch (IOException ioe) {
 			throw new Error("Unable to create database");
 		}
 
 		try {
 			myDb.openDataBase();
 		}catch(SQLException sqle){
 			throw sqle;
 		}
 		
 		{
 		myDb.getReadableDatabase();
 		}

        lm = (LocationManager) 
        getSystemService(Context.LOCATION_SERVICE);
        
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){  
                   createGpsDisabledAlert();  
        	      }
        
        {//TODO some initial map overlays, ovakoj treba da se dodadev i crtanje na putishta
        	//u MyItemizedOverlay...
        
        }
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.person);
        MyItemizedOverlay itemizedoverlay = new MyItemizedOverlay(drawable, this);
        
        //GeoPoint point = new GeoPoint(19240000,-99120000);
        //OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
        
        //GeoPoint point2 = new GeoPoint(35410000, 139460000);
        //OverlayItem overlayitem2 = new OverlayItem(point2, "Sekai, konichiwa!", "I'm in Japan!");
        
        //itemizedoverlay.addOverlay(overlayitem);
        //itemizedoverlay.addOverlay(overlayitem2);
        //mapOverlays.add(itemizedoverlay);
        
        /* Itemized Overlay */
        {
                final ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
                
                //pristapuvanje na baza
                {   
                	Cursor c = myDb.getStanici();
                	if (c.moveToFirst())
                	{
                		do {          
                			//String id = c.getString(0);
                			String ime = c.getString(1);

                			double flon = c.getDouble(3);
                			double flat = c.getDouble(2);
                			
                			int lat = (int)(flat*1000000);
                			int lon = (int)(flon*1000000);
                			
                			GeoPoint stanica = new GeoPoint(lat, lon);
                			//OverlayItem temp = new OverlayItem(stanica, ime, " ");
                			//itemizedoverlay.addOverlay(temp);
                			items.add(new OverlayItem(stanica, ime, " "));
                			
                			//Log.d("xxx", "ID: " + id + "  ime=" + ime + " lat/lon=" + lon + " / " + lat);
                			
                		} while (c.moveToNext());	
                	}
                	myDb.close();
                }      
                
                //GeoPoint point = new GeoPoint(19240000,-99120000);
                //OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
                
                itemizedoverlay.addOverlay(items);
                mapOverlays.add(itemizedoverlay);
                
                //TODO
                //for(int i=0;i<items.size();i++) {
                //	itemizedoverlay.addOverlay(items.get(i));
                //	mapOverlays.add(itemizedoverlay);
                //}
        }
        
        /*
        MapController mCtrl = mapView.getController();
        mCtrl.setZoom(4);
        mCtrl.setCenter(anchor_Europe);

        Projection pr;
        pr = mapView.getProjection();
        */
        
	}
	
	
	
	private void createGpsDisabledAlert(){  
	   	 AlertDialog.Builder builder = new AlertDialog.Builder(this);  
	   	 builder.setMessage("Вашиот GPS е оневозможен! Потребно е да биде активиран, за нормално функционирање на апликацијата (пронаоѓање на Вашата локација). Дали би сакале да го овозможите?")
	   	 	  .setIcon(R.drawable.icon)
	          .setTitle(R.string.app_name)
	   	      .setCancelable(false)  
	   	      .setPositiveButton("Овозможи GPS",  
	   	           new DialogInterface.OnClickListener(){  
	   	           public void onClick(DialogInterface dialog, int id){  
	   	                showGpsOptions();  
	   	           }  
	   	      });  
	   	      builder.setNegativeButton("Откажи",  
	   	           new DialogInterface.OnClickListener(){  
	   	           public void onClick(DialogInterface dialog, int id){  
	   	                dialog.cancel();  
	   	           }  
	   	      });  
	   	 AlertDialog alert = builder.create();  
	   	 alert.show();  
	   	 }  
	   	   
	   	 private void showGpsOptions(){  
	   	         Intent gpsOptionsIntent = new Intent(  
	   	                 android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
	   	         startActivity(gpsOptionsIntent);  
	   	 }

}
