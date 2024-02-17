package com.cloudbees.project.constants;

import java.util.HashMap;
import java.util.Map;

public enum Section {
    A("A", 2),
    B("B", 2);

    private final String section;
    private final int capacity;

    Section(String section, int capacity) {
        this.section = section;
        this.capacity = capacity;
    }

    public String getSection() {
        return this.section;
    }

    public int getCapacity() {
        return this.capacity;
    }

    private static final Map<String, Section> sectionMap = new HashMap<>(2);
    static {
        sectionMap.put("A", A); sectionMap.put("B", B);
    }

    public static Section getSectionFromString(String section) {
        return sectionMap.getOrDefault(section, null);
    }
}
