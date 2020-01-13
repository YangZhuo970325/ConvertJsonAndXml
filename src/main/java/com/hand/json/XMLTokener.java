/**
 * 文件名：XMLTokener.java
 * 描述：
 **/
package com.hand.json;

import java.util.HashMap;

/**
 *
 *
 * @author yangzhuo-hd@139.com
 * @version 1.0，2020/1/13
 * @date 2020/1/13 11:22
 */
public class XMLTokener extends JSONTokener {
    public static final HashMap entity = new HashMap(8);

    public XMLTokener(String var1) {
        super(var1);
    }

    public String nextCDATA() throws JSONException {
        StringBuffer var3 = new StringBuffer();

        int var2;
        do {
            char var1 = this.next();
            if (var1 == 0) {
                throw this.syntaxError("Unclosed CDATA");
            }

            var3.append(var1);
            var2 = var3.length() - 3;
        } while(var2 < 0 || var3.charAt(var2) != ']' || var3.charAt(var2 + 1) != ']' || var3.charAt(var2 + 2) != '>');

        var3.setLength(var2);
        return var3.toString();
    }

    public Object nextContent() throws JSONException {
        char var1;
        do {
            var1 = this.next();
        } while(Character.isWhitespace(var1));

        if (var1 == 0) {
            return null;
        } else if (var1 == '<') {
            return XML.LT;
        } else {
            StringBuffer var2;
            for(var2 = new StringBuffer(); var1 != '<' && var1 != 0; var1 = this.next()) {
                if (var1 == '&') {
                    var2.append(this.nextEntity(var1));
                } else {
                    var2.append(var1);
                }
            }

            this.back();
            return var2.toString().trim();
        }
    }

    public Object nextEntity(char var1) throws JSONException {
        StringBuffer var2 = new StringBuffer();

        while(true) {
            char var3 = this.next();
            if (!Character.isLetterOrDigit(var3) && var3 != '#') {
                if (var3 == ';') {
                    String var5 = var2.toString();
                    Object var4 = entity.get(var5);
                    return var4 != null ? var4 : var1 + var5 + ";";
                }

                throw this.syntaxError("Missing ';' in XML entity: &" + var2);
            }

            var2.append(Character.toLowerCase(var3));
        }
    }

    public Object nextMeta() throws JSONException {
        char var1;
        do {
            var1 = this.next();
        } while(Character.isWhitespace(var1));

        switch(var1) {
            case '\u0000':
                throw this.syntaxError("Misshaped meta tag");
            case '!':
                return XML.BANG;
            case '"':
            case '\'':
                char var2 = var1;

                do {
                    var1 = this.next();
                    if (var1 == 0) {
                        throw this.syntaxError("Unterminated string");
                    }
                } while(var1 != var2);

                return Boolean.TRUE;
            case '/':
                return XML.SLASH;
            case '<':
                return XML.LT;
            case '=':
                return XML.EQ;
            case '>':
                return XML.GT;
            case '?':
                return XML.QUEST;
            default:
                while(true) {
                    var1 = this.next();
                    if (Character.isWhitespace(var1)) {
                        return Boolean.TRUE;
                    }

                    switch(var1) {
                        case '\u0000':
                        case '!':
                        case '"':
                        case '\'':
                        case '/':
                        case '<':
                        case '=':
                        case '>':
                        case '?':
                            this.back();
                            return Boolean.TRUE;
                    }
                }
        }
    }

    public Object nextToken() throws JSONException {
        char var1;
        do {
            var1 = this.next();
        } while(Character.isWhitespace(var1));

        StringBuffer var3;
        switch(var1) {
            case '\u0000':
                throw this.syntaxError("Misshaped element");
            case '!':
                return XML.BANG;
            case '"':
            case '\'':
                char var2 = var1;
                var3 = new StringBuffer();

                while(true) {
                    var1 = this.next();
                    if (var1 == 0) {
                        throw this.syntaxError("Unterminated string");
                    }

                    if (var1 == var2) {
                        return var3.toString();
                    }

                    if (var1 == '&') {
                        var3.append(this.nextEntity(var1));
                    } else {
                        var3.append(var1);
                    }
                }
            case '/':
                return XML.SLASH;
            case '<':
                throw this.syntaxError("Misplaced '<'");
            case '=':
                return XML.EQ;
            case '>':
                return XML.GT;
            case '?':
                return XML.QUEST;
            default:
                var3 = new StringBuffer();

                while(true) {
                    var3.append(var1);
                    var1 = this.next();
                    if (Character.isWhitespace(var1)) {
                        return var3.toString();
                    }

                    switch(var1) {
                        case '\u0000':
                            return var3.toString();
                        case '!':
                        case '/':
                        case '=':
                        case '>':
                        case '?':
                        case '[':
                        case ']':
                            this.back();
                            return var3.toString();
                        case '"':
                        case '\'':
                        case '<':
                            throw this.syntaxError("Bad character in a name");
                    }
                }
        }
    }

    public boolean skipPast(String var1) throws JSONException {
        int var6 = 0;
        int var7 = var1.length();
        char[] var8 = new char[var7];

        char var3;
        int var4;
        for(var4 = 0; var4 < var7; ++var4) {
            var3 = this.next();
            if (var3 == 0) {
                return false;
            }

            var8[var4] = var3;
        }

        while(true) {
            int var5 = var6;
            boolean var2 = true;

            for(var4 = 0; var4 < var7; ++var4) {
                if (var8[var5] != var1.charAt(var4)) {
                    var2 = false;
                    break;
                }

                ++var5;
                if (var5 >= var7) {
                    var5 -= var7;
                }
            }

            if (var2) {
                return true;
            }

            var3 = this.next();
            if (var3 == 0) {
                return false;
            }

            var8[var6] = var3;
            ++var6;
            if (var6 >= var7) {
                var6 -= var7;
            }
        }
    }

    static {
        entity.put("amp", XML.AMP);
        entity.put("apos", XML.APOS);
        entity.put("gt", XML.GT);
        entity.put("lt", XML.LT);
        entity.put("quot", XML.QUOT);
    }
}