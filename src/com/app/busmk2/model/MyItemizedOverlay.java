package com.app.busmk2.model;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * A class that extends and overrides some of the ItemizedOverlay
 * functionalities for placing markers on a map overlay.
 * 
 * @author drakuwa
 * 
 */
public class MyItemizedOverlay extends ItemizedOverlay {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;

	/**
	 * Constructor of the class which initializes the drawable passed as
	 * parameter of the method.
	 * 
	 * @param defaultMarker
	 */
	public MyItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	/**
	 * Method that adds an overlay item to an existing array list.
	 * 
	 * @param overlay
	 */
	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	/**
	 * Method that copies a list of overlay items to an existing array list.
	 * 
	 * @param overlay
	 */
	public void addOverlay(ArrayList<OverlayItem> items) {
		mOverlays.addAll(items);
		populate();
	}

	/**
	 * Method that returns the i-th element of the overlay items array list.
	 */
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	/**
	 * Method that returns the size of the overlay items array list.
	 */
	@Override
	public int size() {
		return mOverlays.size();
	}

	/**
	 * Method that deletes all the items in the overlay items array list.
	 */
	public void clear() {
		mOverlays.clear();
	}

	/**
	 * Method that returns element of the overlay items array list given with
	 * its index of appearance.
	 */
	public OverlayItem get(int index) {
		return mOverlays.get(index);
	}

	/**
	 * An override of the default constructor that initializes the
	 * MyItemizesOverlay list and connects it with the given activity context
	 * and drawable.
	 * 
	 * @param defaultMarker
	 * @param context
	 */
	public MyItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}
}
