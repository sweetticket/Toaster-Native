package com.github.florent37.materialviewpager.sample;

import android.util.Log;

import org.json.JSONObject;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ByDateComparator implements Comparator<Object>
{

    @Override
    public int compare(Object obj1, Object obj2) {
        try {
            Date date1 = ISO8601DateParser.parse(((JSONObject) obj1).getString("createdAt"));
            Date date2 = ISO8601DateParser.parse(((JSONObject) obj2).getString("createdAt"));
//            Log.d("ByDateComparator", "date1: " + date1);
//            Log.d("ByDateComparator", "date2: " + date2);
            return date2.compareTo(date1);
        } catch (org.json.JSONException e) {
            Log.d("ByDateComparator", e.getMessage());
        } catch (java.text.ParseException e) {
            Log.d("ByDateComparator", e.getMessage());
        } catch (java.lang.ClassCastException e) {
            Log.d("ByDateComparator", e.getMessage());
        }
        return 0;
    }
}
