/**
 * 文件名：XML.java
 * 描述：
 **/
package com.hand.json;

import java.util.Iterator;
/**
 * 因为转xml时需要加上命名空间，重写XML类
 *
 * @author yangzhuo-hd@139.com
 * @version 1.0，2019/11/13
 * @date 2019/11/13 9:30
 */



public class XML {
    public static final Character AMP = new Character('&');
    public static final Character APOS = new Character('\'');
    public static final Character BANG = new Character('!');
    public static final Character EQ = new Character('=');
    public static final Character GT = new Character('>');
    public static final Character LT = new Character('<');
    public static final Character QUEST = new Character('?');
    public static final Character QUOT = new Character('"');
    public static final Character SLASH = new Character('/');

    public XML() {
    }

    public static String escape(String var0) {
        StringBuffer var1 = new StringBuffer();
        int var2 = 0;

        for(int var3 = var0.length(); var2 < var3; ++var2) {
            char var4 = var0.charAt(var2);
            switch(var4) {
                case '"':
                    var1.append("&quot;");
                    break;
                case '&':
                    var1.append("&amp;");
                    break;
                case '<':
                    var1.append("&lt;");
                    break;
                case '>':
                    var1.append("&gt;");
                    break;
                default:
                    var1.append(var4);
            }
        }

        return var1.toString();
    }

    public static void noSpace(String var0) throws JSONException {
        int var2 = var0.length();
        if (var2 == 0) {
            throw new JSONException("Empty string.");
        } else {
            for(int var1 = 0; var1 < var2; ++var1) {
                if (Character.isWhitespace(var0.charAt(var1))) {
                    throw new JSONException("'" + var0 + "' contains a space character.");
                }
            }

        }
    }

    private static boolean parse(XMLTokener var0, JSONObject var1, String var2) throws JSONException {
        JSONObject var6 = null;
        Object var8 = var0.nextToken();
        String var7;
        if (var8 == BANG) {
            char var3 = var0.next();
            if (var3 == '-') {
                if (var0.next() == '-') {
                    var0.skipPast("-->");
                    return false;
                }

                var0.back();
            } else if (var3 == '[') {
                var8 = var0.nextToken();
                if (var8.equals("CDATA") && var0.next() == '[') {
                    var7 = var0.nextCDATA();
                    if (var7.length() > 0) {
                        var1.accumulate("content", var7);
                    }

                    return false;
                }

                throw var0.syntaxError("Expected 'CDATA['");
            }

            int var4 = 1;

            do {
                var8 = var0.nextMeta();
                if (var8 == null) {
                    throw var0.syntaxError("Missing '>' after '<!'.");
                }

                if (var8 == LT) {
                    ++var4;
                } else if (var8 == GT) {
                    --var4;
                }
            } while(var4 > 0);

            return false;
        } else if (var8 == QUEST) {
            var0.skipPast("?>");
            return false;
        } else if (var8 == SLASH) {
            var8 = var0.nextToken();
            if (var2 == null) {
                throw var0.syntaxError("Mismatched close tag" + var8);
            } else if (!var8.equals(var2)) {
                throw var0.syntaxError("Mismatched " + var2 + " and " + var8);
            } else if (var0.nextToken() != GT) {
                throw var0.syntaxError("Misshaped close tag");
            } else {
                return true;
            }
        } else if (var8 instanceof Character) {
            throw var0.syntaxError("Misshaped tag");
        } else {
            String var5 = (String)var8;
            var8 = null;
            var6 = new JSONObject();

            while(true) {
                if (var8 == null) {
                    var8 = var0.nextToken();
                }

                if (!(var8 instanceof String)) {
                    if (var8 == SLASH) {
                        if (var0.nextToken() != GT) {
                            throw var0.syntaxError("Misshaped tag");
                        }

                        var1.accumulate(var5, var6);
                        return false;
                    }

                    if (var8 != GT) {
                        throw var0.syntaxError("Misshaped tag");
                    }

                    while(true) {
                        var8 = var0.nextContent();
                        if (var8 == null) {
                            if (var5 != null) {
                                throw var0.syntaxError("Unclosed tag " + var5);
                            }

                            return false;
                        }

                        if (var8 instanceof String) {
                            var7 = (String)var8;
                            if (var7.length() > 0) {
                                var6.accumulate("content", JSONObject.stringToValue(var7));
                            }
                        } else if (var8 == LT && parse(var0, var6, var5)) {
                            if (var6.length() == 0) {
                                var1.accumulate(var5, "");
                            } else if (var6.length() == 1 && var6.opt("content") != null) {
                                var1.accumulate(var5, var6.opt("content"));
                            } else {
                                var1.accumulate(var5, var6);
                            }

                            return false;
                        }
                    }
                }

                var7 = (String)var8;
                var8 = var0.nextToken();
                if (var8 == EQ) {
                    var8 = var0.nextToken();
                    if (!(var8 instanceof String)) {
                        throw var0.syntaxError("Missing value");
                    }

                    var6.accumulate(var7, JSONObject.stringToValue((String)var8));
                    var8 = null;
                } else {
                    var6.accumulate(var7, "");
                }
            }
        }
    }

    public static JSONObject toJSONObject(String var0) throws JSONException {
        JSONObject var1 = new JSONObject();
        XMLTokener var2 = new XMLTokener(var0);

        while(var2.more() && var2.skipPast("<")) {
            parse(var2, var1, (String)null);
        }

        return var1;
    }

    public static String toString(Object var0) throws JSONException {
        return toString(var0, (String)null);
    }

    public static String toString(Object var0, String elementNode) throws JSONException {
        return toString(var0, (String)null, elementNode);
    }

    public static String toString(Object var0, String var1, String elementNode) throws JSONException {
        StringBuffer var2 = new StringBuffer();
        int var3;
        JSONArray var4;
        int var8;
        String var9;
        Object var10;
        if (!(var0 instanceof JSONObject)) {    //判断是否为jsonObject
            if (var0 instanceof JSONArray) {    //判断是否时jsonArray
                var4 = (JSONArray)var0;
                var8 = var4.length();

                for(var3 = 0; var3 < var8; ++var3) {
                    var10 = var4.opt(var3);
                    var2.append(toString(var10, var1 == null ? "array" : var1, elementNode));
                }

                return var2.toString();
            } else {
                var9 = var0 == null ? "null" : escape(var0.toString());
                return var1 == null ? "\"" + var9 + "\"" : (var9.length() == 0 ? "<" + elementNode + ":" + var1 + "/>" : "<" + elementNode + ":" + var1 + ">" + var9 + "</" + elementNode + ":" + var1 + ">");
            }
        } else {
            if (var1 != null) {
                var2.append("<" + elementNode + ":");
                var2.append(var1);
                var2.append(">");
            }

            JSONObject var5 = (JSONObject)var0;
            Iterator var7 = var5.keys();

            while(true) {
                while(true) {
                    while(var7.hasNext()) {
                        String var6 = var7.next().toString();
                        var10 = var5.opt(var6);
                        if (var10 == null) {
                            var10 = "";
                        }

                        if (var10 instanceof String) {
                            var9 = (String)var10;
                        } else {
                            var9 = null;
                        }

                        if (var6.equals("content")) {
                            if (var10 instanceof JSONArray) {
                                var4 = (JSONArray)var10;
                                var8 = var4.length();

                                for(var3 = 0; var3 < var8; ++var3) {
                                    if (var3 > 0) {
                                        var2.append('\n');
                                    }

                                    var2.append(escape(var4.get(var3).toString()));
                                }
                            } else {
                                var2.append(escape(var10.toString()));
                            }
                        } else if (var10 instanceof JSONArray) {
                            var4 = (JSONArray)var10;
                            var8 = var4.length();

                            for(var3 = 0; var3 < var8; ++var3) {
                                var10 = var4.get(var3);
                                if (var10 instanceof JSONArray) {
                                    var2.append("<" + elementNode + ":");
                                    var2.append(var6);
                                    var2.append('>');
                                    var2.append(toString(var10, elementNode));
                                    var2.append("</" + elementNode + ":");
                                    var2.append(var6);
                                    var2.append('>');
                                } else {
                                    var2.append(toString(var10, var6, elementNode));
                                }
                            }
                        } else if (var10.equals("")) {
                            var2.append("<" + elementNode + ":");
                            var2.append(var6);
                            var2.append("/>");
                        } else {
                            var2.append(toString(var10, var6, elementNode));
                        }
                    }

                    if (var1 != null) {
                        var2.append("</" + elementNode +":");
                        var2.append(var1);
                        var2.append(">");
                    }

                    return var2.toString();
                }
            }
        }
    }
}
