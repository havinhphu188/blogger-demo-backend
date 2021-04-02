package com.example.bloggerdemo.service.strategy.checkunique;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum CheckUniqueStrategy {
    USERNAME("username"),
    DISPLAY_NAME("displayName");

    private static final Map<String, CheckUniqueStrategy> lookup = new HashMap<>();
    private final String abbreviation;

    CheckUniqueStrategy(String abbr) {
        this.abbreviation = abbr;
    }

    public static CheckUniqueStrategy fromAbbr(String abbr){
        if (!lookup.containsKey(abbr))
            throw new IllegalArgumentException("Strategy " + abbr + " can not be found");
        return lookup.get(abbr);
    }

    static {
        for (CheckUniqueStrategy c : CheckUniqueStrategy.values())
            lookup.put(c.getAbbreviation(), c);
    }

}
