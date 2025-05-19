package com.example.mgmgapp.util;

import java.util.HashMap;
import java.util.Map;

public class CategoryDirectoryMapper {

    private static final Map<String, String> CATEGORY_ROMAJI_MAP = new HashMap<>();

    static {
        CATEGORY_ROMAJI_MAP.put("和食", "washoku");
        CATEGORY_ROMAJI_MAP.put("洋食", "yoshoku");
        CATEGORY_ROMAJI_MAP.put("中華", "chuka");
        CATEGORY_ROMAJI_MAP.put("その他", "other");
    }

    public static String toRomaji(String japaneseCategory) {
        return CATEGORY_ROMAJI_MAP.getOrDefault(japaneseCategory, "other");
    }
}