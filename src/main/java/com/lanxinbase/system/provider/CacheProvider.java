package com.lanxinbase.system.provider;


import com.lanxinbase.system.provider.handler.CacheRedisHandler;
import com.lanxinbase.system.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by alan.luo on 2017/8/4.
 */
public class CacheProvider {

//    protected CacheMemHandler cache;
    private CacheRedisHandler cache;

    /**
     * default expired time is one hour.
     */
    public final long EXPIRED_TIME = 3600;

    public CacheProvider(){
        this("100");
    }

    public CacheProvider(String id){
        cache = new CacheRedisHandler(id);
    }

    public static CacheProvider getInstance(){
        return new CacheProvider();
    }

    public Object get(String key){
        return cache.get(key);
    }

    public void put(String key,Object val,long expired){
        if (StringUtils.isEmptyTrim(key)){
            return;
        }
        if (expired <= 0){
            expired = EXPIRED_TIME ;
        }
        cache.put(key,val,expired);
    }

    public boolean has(String key){
        return cache.has(key);
    }

    public void remove(String key){
        cache.remove(key);
    }

    public void clear(){
        cache.clear();
    }

    public int count(){
       return cache.count();
    }

    /**
     * 获取所有的缓存数据
     * @return Map<String,Object>对象
     */
    public Object getAll(){
       return cache.getAll();
    }

    /**
     * 清除无用的缓存
     */
    public void checkAll(){
        cache.checkAll();
    }

}
