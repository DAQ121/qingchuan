package com.qingchuan.coderman.web.cache;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 热门标签的缓存
 */
@Component
@SuppressWarnings("all")
public class HotTagCache {

    private  Map<String,Integer> properties=new HashMap<>();

    //存入
    public void setProperties(Map<String, Integer> properties) {
        this.properties = properties;
    }

    //获取
    public Map<String, Integer> getProperties() {
        return properties;
    }

    //更新标签
    public List<String> updateTags(){
        List<String> objects = new ArrayList<>();
        Map<String, Integer> map = HotTagCache.sortByValueDescending(this.properties);
        for(Map.Entry<String,Integer> entry:map.entrySet()){
            String key = entry.getKey();
            if(key!=null&&key.length()!=1&&key.length()<15){
                objects.add(key);
            }
        }
        int size = objects.size();
        if(size>=33){
            return objects.subList(0,33);
        }
        return objects;
    }

    //降序排序
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map) {
        //返回map中各个键值对映射关系的集合。
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        //要用collection的sort方法，就要重写比较器
        //lambda表达式：接收o1,o2返回他们之间的差值
        Collections.sort(list, (o1, o2) -> {
            int compare = (o1.getValue()).compareTo(o2.getValue());
            return -compare;
        });

        //用一个结果集来接收
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}
