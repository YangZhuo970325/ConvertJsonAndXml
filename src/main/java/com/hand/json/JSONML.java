/**
 * 文件名：JSONML.java
 * 描述：
 **/
package com.hand.json;

import java.util.Iterator;

/**
 *
 *
 * @author yangzhuo-hd@139.com
 * @version 1.0，2020/1/13
 * @date 2020/1/13 11:18
 */
public class JSONML {
    public JSONML() {
    }

    private static Object parse(XMLTokener var0, boolean var1, JSONArray var2) throws JSONException {
        String var5 = null;
        JSONArray var7 = null;
        JSONObject var8 = null;
        String var10 = null;

        while(true) {
            while(true) {
                Object var9 = var0.nextContent();
                if (var9 == XML.LT) {
                    var9 = var0.nextToken();
                    if (var9 instanceof Character) {
                        if (var9 == XML.SLASH) {
                            var9 = var0.nextToken();
                            if (!(var9 instanceof String)) {
                                throw new JSONException("Expected a closing name instead of '" + var9 + "'.");
                            }

                            if (var0.nextToken() != XML.GT) {
                                throw var0.syntaxError("Misshaped close tag");
                            }

                            return var9;
                        }

                        if (var9 != XML.BANG) {
                            if (var9 != XML.QUEST) {
                                throw var0.syntaxError("Misshaped tag");
                            }

                            var0.skipPast("?>");
                        } else {
                            char var4 = var0.next();
                            if (var4 == '-') {
                                if (var0.next() == '-') {
                                    var0.skipPast("-->");
                                }

                                var0.back();
                            } else if (var4 == '[') {
                                var9 = var0.nextToken();
                                if (!var9.equals("CDATA") || var0.next() != '[') {
                                    throw var0.syntaxError("Expected 'CDATA['");
                                }

                                if (var2 != null) {
                                    var2.put(var0.nextCDATA());
                                }
                            } else {
                                int var6 = 1;

                                while(true) {
                                    var9 = var0.nextMeta();
                                    if (var9 == null) {
                                        throw var0.syntaxError("Missing '>' after '<!'.");
                                    }

                                    if (var9 == XML.LT) {
                                        ++var6;
                                    } else if (var9 == XML.GT) {
                                        --var6;
                                    }

                                    if (var6 <= 0) {
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        if (!(var9 instanceof String)) {
                            throw var0.syntaxError("Bad tagName '" + var9 + "'.");
                        }

                        var10 = (String)var9;
                        var7 = new JSONArray();
                        var8 = new JSONObject();
                        if (var1) {
                            var7.put(var10);
                            if (var2 != null) {
                                var2.put(var7);
                            }
                        } else {
                            var8.put("tagName", var10);
                            if (var2 != null) {
                                var2.put(var8);
                            }
                        }

                        var9 = null;

                        while(true) {
                            if (var9 == null) {
                                var9 = var0.nextToken();
                            }

                            if (var9 == null) {
                                throw var0.syntaxError("Misshaped tag");
                            }

                            if (!(var9 instanceof String)) {
                                if (var1 && var8.length() > 0) {
                                    var7.put(var8);
                                }

                                if (var9 == XML.SLASH) {
                                    if (var0.nextToken() != XML.GT) {
                                        throw var0.syntaxError("Misshaped tag");
                                    }

                                    if (var2 == null) {
                                        if (var1) {
                                            return var7;
                                        }

                                        return var8;
                                    }
                                } else {
                                    if (var9 != XML.GT) {
                                        throw var0.syntaxError("Misshaped tag");
                                    }

                                    var5 = (String)parse(var0, var1, var7);
                                    if (var5 != null) {
                                        if (!var5.equals(var10)) {
                                            throw var0.syntaxError("Mismatched '" + var10 + "' and '" + var5 + "'");
                                        }

                                        var10 = null;
                                        if (!var1 && var7.length() > 0) {
                                            var8.put("childNodes", var7);
                                        }

                                        if (var2 == null) {
                                            if (var1) {
                                                return var7;
                                            }

                                            return var8;
                                        }
                                    }
                                }
                                break;
                            }

                            String var3 = (String)var9;
                            if (!var1 && (var3 == "tagName" || var3 == "childNode")) {
                                throw var0.syntaxError("Reserved attribute.");
                            }

                            var9 = var0.nextToken();
                            if (var9 == XML.EQ) {
                                var9 = var0.nextToken();
                                if (!(var9 instanceof String)) {
                                    throw var0.syntaxError("Missing value");
                                }

                                var8.accumulate(var3, JSONObject.stringToValue((String)var9));
                                var9 = null;
                            } else {
                                var8.accumulate(var3, "");
                            }
                        }
                    }
                } else if (var2 != null) {
                    var2.put(var9 instanceof String ? JSONObject.stringToValue((String)var9) : var9);
                }
            }
        }
    }

    public static JSONArray toJSONArray(String var0) throws JSONException {
        return toJSONArray(new XMLTokener(var0));
    }

    public static JSONArray toJSONArray(XMLTokener var0) throws JSONException {
        return (JSONArray)parse(var0, true, (JSONArray)null);
    }

    public static JSONObject toJSONObject(XMLTokener var0) throws JSONException {
        return (JSONObject)parse(var0, false, (JSONArray)null);
    }

    public static JSONObject toJSONObject(String var0) throws JSONException {
        return toJSONObject(new XMLTokener(var0));
    }

    public static String toString(JSONArray var0) throws JSONException {
        StringBuffer var7 = new StringBuffer();
        String var8 = var0.getString(0);
        XML.noSpace(var8);
        var8 = XML.escape(var8);
        var7.append('<');
        var7.append(var8);
        Object var1 = var0.opt(1);
        int var2;
        if (var1 instanceof JSONObject) {
            var2 = 2;
            JSONObject var3 = (JSONObject)var1;
            Iterator var5 = var3.keys();

            while(var5.hasNext()) {
                String var4 = var5.next().toString();
                XML.noSpace(var4);
                String var9 = var3.optString(var4);
                if (var9 != null) {
                    var7.append(' ');
                    var7.append(XML.escape(var4));
                    var7.append('=');
                    var7.append('"');
                    var7.append(XML.escape(var9));
                    var7.append('"');
                }
            }
        } else {
            var2 = 1;
        }

        int var6 = var0.length();
        if (var2 >= var6) {
            var7.append('/');
            var7.append('>');
        } else {
            var7.append('>');

            do {
                var1 = var0.get(var2);
                ++var2;
                if (var1 != null) {
                    if (var1 instanceof String) {
                        var7.append(XML.escape(var1.toString()));
                    } else if (var1 instanceof JSONObject) {
                        var7.append(toString((JSONObject)var1));
                    } else if (var1 instanceof JSONArray) {
                        var7.append(toString((JSONArray)var1));
                    }
                }
            } while(var2 < var6);

            var7.append('<');
            var7.append('/');
            var7.append(var8);
            var7.append('>');
        }

        return var7.toString();
    }

    public static String toString(JSONObject var0) throws JSONException {
        StringBuffer var1 = new StringBuffer();
        String var8 = var0.optString("tagName");
        if (var8 == null) {
            return XML.escape(var0.toString());
        } else {
            XML.noSpace(var8);
            var8 = XML.escape(var8);
            var1.append('<');
            var1.append(var8);
            Iterator var6 = var0.keys();

            while(var6.hasNext()) {
                String var5 = var6.next().toString();
                if (!var5.equals("tagName") && !var5.equals("childNodes")) {
                    XML.noSpace(var5);
                    String var9 = var0.optString(var5);
                    if (var9 != null) {
                        var1.append(' ');
                        var1.append(XML.escape(var5));
                        var1.append('=');
                        var1.append('"');
                        var1.append(XML.escape(var9));
                        var1.append('"');
                    }
                }
            }

            JSONArray var4 = var0.optJSONArray("childNodes");
            if (var4 == null) {
                var1.append('/');
                var1.append('>');
            } else {
                var1.append('>');
                int var7 = var4.length();

                for(int var3 = 0; var3 < var7; ++var3) {
                    Object var2 = var4.get(var3);
                    if (var2 != null) {
                        if (var2 instanceof String) {
                            var1.append(XML.escape(var2.toString()));
                        } else if (var2 instanceof JSONObject) {
                            var1.append(toString((JSONObject)var2));
                        } else if (var2 instanceof JSONArray) {
                            var1.append(toString((JSONArray)var2));
                        }
                    }
                }

                var1.append('<');
                var1.append('/');
                var1.append(var8);
                var1.append('>');
            }

            return var1.toString();
        }
    }
}

