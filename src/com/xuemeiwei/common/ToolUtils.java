package com.xuemeiwei.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ToolUtils {
    public static final String SEPERATOR = "  ";

    public static String join(String delimiter, String[] sequence) {
        StringBuilder res = new StringBuilder();
        int length = sequence.length;
        for (int i = 0; i < length; i++) {
            res.append(sequence[i]);
            if (i < length - 1) {
                res.append(delimiter);
            }
        }
        return res.toString();
    }

    public static List<String> andConnection (List<List<String>> lists) {
        if (lists == null || lists.size() == 0) {
            return new ArrayList<>();
        }
        Set<String> set = new HashSet<>(lists.get(0));
        for (int i = 1; i < lists.size(); ++i) {
            Set<String> temp = new HashSet<>(lists.get(1));
            set.retainAll(temp);
        }
        return new ArrayList<>(set);
    }

    public static List<String> orConnection (List<List<String>> lists) {
        if (lists == null || lists.size() == 0) {
            return new ArrayList<>();
        }
        Set<String> set = new HashSet<>(lists.get(0));
        for (int i = 1; i < lists.size(); ++i) {
            if (lists.get(i) == null) {
                continue;
            }
            set.addAll(lists.get(i));
        }
        return new ArrayList<>(set);
    }

    public static List<String> intersection (List<String> list1, List<String> list2) {
        Set<String> set1 = new HashSet<>(list1);
        Set<String> set2 = new HashSet<>(list2);
        set1.retainAll(set2);
        return new ArrayList<>(set1);
    }
}
