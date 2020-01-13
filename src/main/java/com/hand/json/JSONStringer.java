/**
 * 文件名：JSONStringer.java
 * 描述：
 **/
package com.hand.json;

import java.io.StringWriter;

/**
 *
 *
 * @author yangzhuo-hd@139.com
 * @version 1.0，2020/1/13
 * @date 2020/1/13 11:20
 */
public class JSONStringer extends JSONWriter {
    public JSONStringer() {
        super(new StringWriter());
    }

    public String toString() {
        return this.mode == 'd' ? this.writer.toString() : null;
    }
}
