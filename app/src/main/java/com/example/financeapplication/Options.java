package com.example.financeapplication;

import java.util.HashMap;
import java.util.Map;

public class Options {
    private Map<String, String> map = new HashMap<>();

    public Options() {
        map.put("chick-fil-a", "Restaurants/Bars");
        map.put("McDonalds", "Restaurants/Bars");
        map.put("he's not here", "Restaurants/Bars");
        map.put("cookout", "Restaurants/Bars"); //check
        map.put("Bojangles", "Restaurants/Bars");
        map.put("Moe's", "Restaurants/Bars"); //check
        map.put("Harris Teeter", "Groceries");
        map.put("Food Lion", "Groceries");
        map.put("Trader Joe's", "Groceries");
        map.put("Costco", "Groceries");
        map.put("Whole Foods", "Groceries");
        map.put("electric", "Bills");
        map.put("water", "Bills");
        map.put("internet", "Bills");
        map.put("Rent", "Bills"); //check
        map.put("insurance", "Bills");
        map.put("gas", "Transportation");
        map.put("car service", "Transportation");
        map.put("subway ticket", "Transportation");
        map.put("uber", "Transportation");
        map.put("lift", "Transportation");
        map.put("airplane ticket", "Transportation");
        map.put("Target", "Misc");
        map.put("Walmart", "Misc");
        map.put("makeup", "Misc");
        map.put("toiletries", "Misc");
        map.put("skydiving", "Misc");
    }

    public void add(String key, String value) {
        map.put(key, value);
    }

    public Map<String, String> getMap() {
        return map;
    }

}
