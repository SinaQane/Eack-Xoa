package utils;

import java.util.*;

public class DataStructuresUtil
{
    // Found this at https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values.
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map)
    {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    // Found this at https://stackoverflow.com/questions/5283047/intersection-and-union-of-arraylists-in-java.
    public static <T> ArrayList<T> unionArrayLists(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<>(set);
    }
}