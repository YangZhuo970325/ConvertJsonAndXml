/**
 * 文件名：JSONException.java
 * 描述：
 **/
package com.hand.json;

/**
 *
 *
 * @author yangzhuo-hd@139.com
 * @version 1.0，2020/1/13
 * @date 2020/1/13 11:18
 */
public class JSONException extends Exception {
    private Throwable cause;

    public JSONException(String var1) {
        super(var1);
    }

    public JSONException(Throwable var1) {
        super(var1.getMessage());
        this.cause = var1;
    }

    public Throwable getCause() {
        return this.cause;
    }
}
