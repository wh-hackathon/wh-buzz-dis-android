package com.whitehedge.pravinm.hackthonwh;

import java.util.HashSet;
import java.util.TreeMap;

/**
 * Created by sachin on 24/06/16.
 */
public class Constants {
    static final String APP_NAME = "ProDeals";
    static final String SETTINGS_REGIONS_STR = "SELECTED_REGIONS";
    static final String SETTINGS_CATEGORIES_STR = "SELECTED_CATEGORIES";
    static final int REGIONS_CHECKBOX_START_INDEX = 100;
    static final int GEOFENCE_RADIUS_KM = 1;
    static final int DISCOUNT_CATEGORIES_CHECKBOX_START_INDEX = 30;
    static final TreeMap<String, TreeMap<Float,Float>> regions;
    static final HashSet<String> disCategories;

    static  {
        TreeMap<Float, Float> tmp;
        regions = new TreeMap<String, TreeMap<Float, Float>>();
        tmp = new TreeMap<Float, Float>();
        tmp.put(new Float(18.5156040), new Float(73.7819050));
        regions.put("Bavdhan", tmp);
        tmp = new TreeMap<>();
        tmp.put(new Float(18.507399), new Float(73.807650));
        regions.put("Kothrud", tmp);
        tmp = new TreeMap<>();
        tmp.put(new Float(18.513475), new Float(73.845281));
        regions.put("LaxmiRoad", tmp);

        disCategories = new HashSet<String>();
        disCategories.add("Apparels");
        disCategories.add("Food");
        disCategories.add("Grocery");
        disCategories.add("LifeStyle");
    }
}
