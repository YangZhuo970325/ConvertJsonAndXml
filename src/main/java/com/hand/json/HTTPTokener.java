/**
 * 文件名：HTTPTokener.java
 * 描述：
 **/
package com.hand.json;

/**
 *
 *
 * @author yangzhuo-hd@139.com
 * @version 1.0，2020/1/13
 * @date 2020/1/13 11:17
 */
public class HTTPTokener extends JSONTokener {
    public HTTPTokener(String var1) {
        super(var1);
    }

    public String nextToken() throws JSONException {
        StringBuffer var3 = new StringBuffer();

        char var1;
        do {
            var1 = this.next();
        } while(Character.isWhitespace(var1));

        if (var1 != '"' && var1 != '\'') {
            while(var1 != 0 && !Character.isWhitespace(var1)) {
                var3.append(var1);
                var1 = this.next();
            }

            return var3.toString();
        } else {
            char var2 = var1;

            while(true) {
                var1 = this.next();
                if (var1 < ' ') {
                    throw this.syntaxError("Unterminated string.");
                }

                if (var1 == var2) {
                    return var3.toString();
                }

                var3.append(var1);
            }
        }
    }
}

