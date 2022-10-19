package cn.hdudragonking.cherry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) {
        HashMap<Integer, List<Integer>> listHashMap = new HashMap<>();
        listHashMap.put(1, new ArrayList<>());
        List<Integer> list = listHashMap.get(1);
        list.add(2);
        list.add(3);
        System.out.println(listHashMap.get(1));
    }

}
