package com.github.florent37.materialviewpager.sample;

import android.util.Log;

import org.json.JSONObject;

import java.util.Comparator;
import java.util.Date;
import java.lang.*;

public class TrendingComparator implements Comparator<Object>
{
    private double getRank(Object obj) {
        try {
            Date postdate = ISO8601DateParser.parse(((JSONObject) obj).getString("createdAt"));
            Date now = new Date();
            double secs = (now.getTime() - postdate.getTime()) / 1000;
            double t = (secs / 3600);
            double p = (double) Integer.parseInt(((JSONObject) obj).getString("numLikes"));
            double g = 1.8;
            return p / Math.pow(t+2, g);

        } catch (org.json.JSONException e) {
            Log.d("TrendingComparator", e.getMessage());
        } catch (java.text.ParseException e) {
            Log.d("TrendingComparator", e.getMessage());
        } catch (ClassCastException e) {
            Log.d("TrendingComparator", e.getMessage());
        }
        return 0;
    }


    @Override
    public int compare(Object obj1, Object obj2) {
        return Double.compare(getRank(obj2),getRank(obj1));
    }
}
