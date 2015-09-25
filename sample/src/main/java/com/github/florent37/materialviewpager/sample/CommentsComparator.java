package com.github.florent37.materialviewpager.sample;

import android.util.Log;

import org.json.JSONObject;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommentsComparator implements Comparator<Object>
{

    @Override
    public int compare(Object obj1, Object obj2) {
        try {
            Date date1 = ISO8601DateParser.parse(((JSONObject) obj1).getString("createdAt"));
            Date date2 = ISO8601DateParser.parse(((JSONObject) obj2).getString("createdAt"));
//            Log.d("RecentComparator", "date1: " + date1);
//            Log.d("RecentComparator", "date2: " + date2);
            return date1.compareTo(date2);
        } catch (org.json.JSONException e) {
            Log.d("RecentComparator", e.getMessage());
        } catch (java.text.ParseException e) {
            Log.d("RecentComparator", e.getMessage());
        } catch (java.lang.ClassCastException e) {
            Log.d("RecentComparator", e.getMessage());
        }
        return 0;
    }
}