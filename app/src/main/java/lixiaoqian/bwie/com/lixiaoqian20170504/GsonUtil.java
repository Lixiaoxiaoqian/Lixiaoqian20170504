package lixiaoqian.bwie.com.lixiaoqian20170504;

import com.google.gson.Gson;

/**
 * @类的用途：gson解析工具类
 * @author: 李晓倩
 * @date: 2017/5/4
 */

public class GsonUtil {

        private static Gson gson = null;
        static {
            if (gson == null) {
                gson = new Gson();
            }
        }

        private GsonUtil() {
        }

        /**
         * 转成bean
         *
         * @param gsonString
         * @param cls
         * @return
         */
        public static <T> T GsonToBean(String gsonString, Class<T> cls) {
            T t = null;
            if (gson != null) {
                t = gson.fromJson(gsonString, cls);
            }
            return t;
        }

}
