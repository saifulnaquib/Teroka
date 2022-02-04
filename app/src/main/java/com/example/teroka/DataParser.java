package com.example.teroka;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {
    private HashMap<String, String> getPlace (JSONObject googlePlaceJSon) {
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String placeName = "--NA--";
        String vicinity = "--NA--";
        String latitude = "";
        String longitude = "";
        String reference = "";

        try {
            if (!googlePlaceJSon.isNull( name: "name")){
                placeName = googlePlaceJSon.getString( name: "name");
            }
            if (!googlePlaceJSon.isNull( name: "vicinity")){
                placeName = googlePlaceJSon.getString( name: "name");
            }

            latitude = googlePlaceJSon.getJSONObject("geometry").getJSONObject("location").getString( name: "lat");
            longitude = googlePlaceJSon.getJSONObject("geometry").getJSONObject("location").getString( name: "lgn");

            reference = googlePlaceJSon.getString( name: "reference");

            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return googlePlaceMap;
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int count = jsonArray.length();
        List<HashMap<String, String>> placeList = new ArrayList<>();
        HashMap<String, String> placeMap = null;

        for(int i = 0; i<count; i++) {
            try {
                placeMap = getPlace(JSONObject) jsonArray.get(i));
                placeList.add(placeMap);
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return placeList;
    }

    public List<HashMap<String, String>> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray( name: "result");
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }
}
