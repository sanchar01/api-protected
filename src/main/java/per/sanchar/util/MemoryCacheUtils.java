package per.sanchar.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: 常用数据缓存工具
 *
 * @author shencai.huang@hand-china.com
 * @date 2021/5/3 4:02 下午
 * lastUpdateBy: shencai.huang@hand-china.com
 * lastUpdateDate: 2021/5/3
 */
public class MemoryCacheUtils {

    /**
     * 容器
     */
    private static Map<String, CacheData> CACHE_DATA = new ConcurrentHashMap<>();

    /**
     * @throws
     * @Description: 根据key获取数据（数据可再处理）
     * @param: [key, load, expire]
     * @return: T
     * @author: wangweitao
     * @date: 2019/3/15 9:46
     */
    public static <T> T getData(String key, Load<T> load, int expire) {
        T data = getData(key);
        if (data == null && load != null) {
            data = load.load();
            if (data != null) {
                setData(key, data, expire);
            }
        }
        return data;
    }

    /**
     * @throws
     * @Description: 根据key获取数据（数据不可再处理）
     * @param: [key]
     * @return: T
     * @author: wangweitao
     * @date: 2019/3/15 9:47
     */
    public static <T> T getData(String key) {
        CacheData<T> data = CACHE_DATA.get(key);
        // 数据未过期则返回
        if (data != null && (data.getExpire() <= 0 || data.getSaveTime() >= System.currentTimeMillis())) {
            return data.getData();
        } else {// 数据过期则清除key
            clear(key);
        }
        return null;
    }

    /**
     * @throws
     * @Description: 获取集合大小
     * @author: wangweitao
     * @date: 2019/3/15 9:47
     */
    public static int getDataSize() {
        return CACHE_DATA.size();
    }

    /**
     * @throws
     * @Description: 新增缓存数据
     * @param: [key, data, expire]
     * @return: void
     * @author: wangweitao
     * @date: 2019/3/15 9:47
     */
    public static <T> void setData(String key, T data, int expire) {
        CACHE_DATA.put(key, new CacheData(data, expire));
    }

    /**
     * @throws
     * @Description: 根据key删除数据
     * @param: [key]
     * @return: void
     * @author: wangweitao
     * @date: 2019/3/15 9:47
     */
    public static void clear(String key) {
        CACHE_DATA.remove(key);
    }

    /**
     * @throws
     * @Description: 清空缓存容器
     * @param: []
     * @return: void
     * @author: wangweitao
     * @date: 2019/3/15 9:47
     */
    public static void clearAll() {
        CACHE_DATA.clear();
    }

    /**
     * @Description: 内部接口：缓存数据再处理功能
     * @param:
     * @return:
     * @throws
     * @author: wangweitao
     * @date: 2019/3/21 11:02
     */
    public interface Load<T> {
        T load();
    }

    /**
     * @Description: 缓存数据实体
     * @param:
     * @return:
     * @throws
     * @author: wangweitao
     * @date: 2019/3/15 9:48
     */
    private static class CacheData<T> {
        private T data;
        private long saveTime; // 存活时间
        private long expire;   // 过期时间 小于等于0标识永久存活

        CacheData(T t, int expire) {
            this.data = t;
            this.expire = expire <= 0 ? 0 : expire * 1000;
            this.saveTime = System.currentTimeMillis() + this.expire;
        }
        public T getData() {
            return data;
        }
        public long getExpire() {
            return expire;
        }
        public long getSaveTime() {
            return saveTime;
        }
    }
}
