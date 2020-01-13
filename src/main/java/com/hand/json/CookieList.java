/**
 * 文件名：CookieList.java
 * 描述：
 **/
package com.hand.json;

import java.util.Iterator;

/**
 *
 *
 * @author yangzhuo-hd@139.com
 * @version 1.0，2020/1/13
 * @date 2020/1/13 11:13
 */
public class CookieList {
    public CookieList() {
    }

    public static JSONObject toJSONObject(String var0) throws JSONException {
        JSONObject var1 = new JSONObject();
        JSONTokener var2 = new JSONTokener(var0);

        while(var2.more()) {
            String var3 = Cookie.unescape(var2.nextTo('='));
            var2.next('=');
            var1.put(var3, Cookie.unescape(var2.nextTo(';')));
            var2.next();
        }

        return var1;
    }

    public static String toString(JSONObject var0) throws JSONException {
        boolean var1 = false;
        Iterator var2 = var0.keys();
        StringBuffer var4 = new StringBuffer();

        while(var2.hasNext()) {
            String var3 = var2.next().toString();
            if (!var0.isNull(var3)) {
                if (var1) {
                    var4.append(';');
                }

                var4.append(Cookie.escape(var3));
                var4.append("=");
                var4.append(Cookie.escape(var0.getString(var3)));
                var1 = true;
            }
        }

        return var4.toString();
    }
}

