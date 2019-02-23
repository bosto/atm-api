package com.bosto.atmapi.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Maps {
    public static Map pair(String key, Object object) {
        Map resultMap = new HashMap<String, Object>();
        resultMap.put(key,object);
        return resultMap;
    }

    public static Map map(String key, Object object) {
        Map resultMap = new HashMap<String, Object>();
        resultMap.put(key,object);
        return resultMap;
    }

    public static Map map(Map ... maps) {
        Map resultMap = new HashMap<String, Object>();
        for (Map map : maps) {
            resultMap.putAll(map);
        }
        return resultMap;
    }

    public static List<Map> sequence(Map ... maps) {
        List<Map> list = new ArrayList<Map>();
        for (Map map: maps) {
            list.add(map);
        }
        return list;
    }
}
