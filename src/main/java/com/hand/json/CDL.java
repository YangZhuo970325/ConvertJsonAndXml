/**
 * 文件名：CDL.java
 * 描述：
 **/
package com.hand.json;

/**
 *
 *
 * @author yangzhuo-hd@139.com
 * @version 1.0，2020/1/13
 * @date 2020/1/13 11:12
 */
public class CDL {
    public CDL() {
    }

    private static String getValue(JSONTokener var0) throws JSONException {
        char var1;
        do {
            var1 = var0.next();
        } while(var1 == ' ' || var1 == '\t');

        switch(var1) {
            case '\u0000':
                return null;
            case '"':
            case '\'':
                return var0.nextString(var1);
            case ',':
                var0.back();
                return "";
            default:
                var0.back();
                return var0.nextTo(',');
        }
    }

    public static JSONArray rowToJSONArray(JSONTokener var0) throws JSONException {
        JSONArray var1 = new JSONArray();

        label33:
        while(true) {
            String var2 = getValue(var0);
            if (var2 == null || var1.length() == 0 && var2.length() == 0) {
                return null;
            }

            var1.put(var2);

            char var3;
            do {
                var3 = var0.next();
                if (var3 == ',') {
                    continue label33;
                }
            } while(var3 == ' ');

            if (var3 != '\n' && var3 != '\r' && var3 != 0) {
                throw var0.syntaxError("Bad character '" + var3 + "' (" + var3 + ").");
            }

            return var1;
        }
    }

    public static JSONObject rowToJSONObject(JSONArray var0, JSONTokener var1) throws JSONException {
        JSONArray var2 = rowToJSONArray(var1);
        return var2 != null ? var2.toJSONObject(var0) : null;
    }

    public static JSONArray toJSONArray(String var0) throws JSONException {
        return toJSONArray(new JSONTokener(var0));
    }

    public static JSONArray toJSONArray(JSONTokener var0) throws JSONException {
        return toJSONArray(rowToJSONArray(var0), var0);
    }

    public static JSONArray toJSONArray(JSONArray var0, String var1) throws JSONException {
        return toJSONArray(var0, new JSONTokener(var1));
    }

    public static JSONArray toJSONArray(JSONArray var0, JSONTokener var1) throws JSONException {
        if (var0 != null && var0.length() != 0) {
            JSONArray var2 = new JSONArray();

            while(true) {
                JSONObject var3 = rowToJSONObject(var0, var1);
                if (var3 == null) {
                    return var2.length() == 0 ? null : var2;
                }

                var2.put(var3);
            }
        } else {
            return null;
        }
    }

    public static String rowToString(JSONArray var0) {
        StringBuffer var1 = new StringBuffer();

        for(int var2 = 0; var2 < var0.length(); ++var2) {
            if (var2 > 0) {
                var1.append(',');
            }

            Object var3 = var0.opt(var2);
            if (var3 != null) {
                String var4 = var3.toString();
                if (var4.indexOf(44) >= 0) {
                    if (var4.indexOf(34) >= 0) {
                        var1.append('\'');
                        var1.append(var4);
                        var1.append('\'');
                    } else {
                        var1.append('"');
                        var1.append(var4);
                        var1.append('"');
                    }
                } else {
                    var1.append(var4);
                }
            }
        }

        var1.append('\n');
        return var1.toString();
    }

    public static String toString(JSONArray var0) throws JSONException {
        JSONObject var1 = var0.optJSONObject(0);
        if (var1 != null) {
            JSONArray var2 = var1.names();
            if (var2 != null) {
                return rowToString(var2) + toString(var2, var0);
            }
        }

        return null;
    }

    public static String toString(JSONArray var0, JSONArray var1) throws JSONException {
        if (var0 != null && var0.length() != 0) {
            StringBuffer var2 = new StringBuffer();

            for(int var3 = 0; var3 < var1.length(); ++var3) {
                JSONObject var4 = var1.optJSONObject(var3);
                if (var4 != null) {
                    var2.append(rowToString(var4.toJSONArray(var0)));
                }
            }

            return var2.toString();
        } else {
            return null;
        }
    }
}

