/**
 * 文件名：HTTP.java
 * 描述：
 **/
package com.hand.json;

import java.util.Iterator;

/**
 *
 *
 * @author yangzhuo-hd@139.com
 * @version 1.0，2020/1/13
 * @date 2020/1/13 11:17
 */
public class HTTP {
    public static final String CRLF = "\r\n";

    public HTTP() {
    }

    public static JSONObject toJSONObject(String var0) throws JSONException {
        JSONObject var1 = new JSONObject();
        HTTPTokener var2 = new HTTPTokener(var0);
        String var3 = var2.nextToken();
        if (var3.toUpperCase().startsWith("HTTP")) {
            var1.put("HTTP-Version", var3);
            var1.put("Status-Code", var2.nextToken());
            var1.put("Reason-Phrase", var2.nextTo('\u0000'));
            var2.next();
        } else {
            var1.put("Method", var3);
            var1.put("Request-URI", var2.nextToken());
            var1.put("HTTP-Version", var2.nextToken());
        }

        while(var2.more()) {
            String var4 = var2.nextTo(':');
            var2.next(':');
            var1.put(var4, var2.nextTo('\u0000'));
            var2.next();
        }

        return var1;
    }

    public static String toString(JSONObject var0) throws JSONException {
        Iterator var1 = var0.keys();
        StringBuffer var3 = new StringBuffer();
        if (var0.has("Status-Code") && var0.has("Reason-Phrase")) {
            var3.append(var0.getString("HTTP-Version"));
            var3.append(' ');
            var3.append(var0.getString("Status-Code"));
            var3.append(' ');
            var3.append(var0.getString("Reason-Phrase"));
        } else {
            if (!var0.has("Method") || !var0.has("Request-URI")) {
                throw new JSONException("Not enough material for an HTTP header.");
            }

            var3.append(var0.getString("Method"));
            var3.append(' ');
            var3.append('"');
            var3.append(var0.getString("Request-URI"));
            var3.append('"');
            var3.append(' ');
            var3.append(var0.getString("HTTP-Version"));
        }

        var3.append("\r\n");

        while(var1.hasNext()) {
            String var2 = var1.next().toString();
            if (!var2.equals("HTTP-Version") && !var2.equals("Status-Code") && !var2.equals("Reason-Phrase") && !var2.equals("Method") && !var2.equals("Request-URI") && !var0.isNull(var2)) {
                var3.append(var2);
                var3.append(": ");
                var3.append(var0.getString(var2));
                var3.append("\r\n");
            }
        }

        var3.append("\r\n");
        return var3.toString();
    }
}

