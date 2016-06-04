package com.evin.util;

import java.util.HashMap;

/**
 * Created by colarking on 14-8-9.
 * 22:39
 */
public class AmayaDataCenter {

    public static HashMap<Integer, Object> amaya = new HashMap<Integer, Object>();

    public static void put(int key, Object data) {
        amaya.put(key, data);
    }

    public static Object get(boolean remove, int key) {
        Object obj = amaya.get(key);
        if (remove) amaya.remove(key);
        return obj;
    }

    public static void clear() {
        if (amaya != null) amaya.clear();
    }
}
