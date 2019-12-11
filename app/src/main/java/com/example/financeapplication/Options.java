package com.example.financeapplication;

import java.util.HashMap;
import java.util.Map;

abstract public class Options implements Map {
    private Map<String, String> map = new HashMap();

    public Options() {
        map.put("Chick-Fil-A", "Restaurants/Bars");
        map.put("McDonalds", "Restaurants/Bars");
        map.put("He's Not Here", "Restaurants/Bars");
        map.put("Cookout", "Restaurants/Bars");
        map.put("Bojangles", "Restaurants/Bars");
        map.put("Moe's", "Restaurants/Bars");
        map.put("Harris Teeter", "Groceries");
        map.put("Food Lion", "Groceries");
        map.put("Trader Joe's", "Groceries");
        map.put("Costco", "Groceries");
        map.put("Whole Foods", "Groceries");
        map.put("Electric", "Bills");
        map.put("Water", "Bills");
        map.put("Internet", "Bills");
        map.put("Rent", "Bills");
        map.put("Insurance", "Bills");
        map.put("Gas", "Transportation");
        map.put("Car Service", "Transportation");
        map.put("Subway Ticket", "Transportation");
        map.put("Uber", "Transportation");
        map.put("Lyft", "Transportation");
        map.put("Airplane Ticket", "Transportation");
        map.put("Target", "Misc");
        map.put("Walmart", "Misc");
        map.put("Makeup", "Misc");
        map.put("Toiletries", "Misc");
        map.put("Skydiving", "Misc");
    }

    public void add(String key, String value) {
        map.put(key, value);
    }

    public Map<String, String> getMap() {
        return map;
    }




}
