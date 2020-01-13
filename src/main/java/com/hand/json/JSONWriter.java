/**
 * 文件名：JSONWriter.java
 * 描述：
 **/
package com.hand.json;

import java.io.IOException;
import java.io.Writer;

/**
 *
 *
 * @author yangzhuo-hd@139.com
 * @version 1.0，2020/1/13
 * @date 2020/1/13 11:21
 */
public class JSONWriter {
    private static final int maxdepth = 20;
    private boolean comma = false;
    protected char mode = 'i';
    private JSONObject[] stack = new JSONObject[20];
    private int top = 0;
    protected Writer writer;

    public JSONWriter(Writer var1) {
        this.writer = var1;
    }

    private JSONWriter append(String var1) throws JSONException {
        if (var1 == null) {
            throw new JSONException("Null pointer");
        } else if (this.mode != 'o' && this.mode != 'a') {
            throw new JSONException("Value out of sequence.");
        } else {
            try {
                if (this.comma && this.mode == 'a') {
                    this.writer.write(44);
                }

                this.writer.write(var1);
            } catch (IOException var3) {
                throw new JSONException(var3);
            }

            if (this.mode == 'o') {
                this.mode = 'k';
            }

            this.comma = true;
            return this;
        }
    }

    public JSONWriter array() throws JSONException {
        if (this.mode != 'i' && this.mode != 'o' && this.mode != 'a') {
            throw new JSONException("Misplaced array.");
        } else {
            this.push((JSONObject)null);
            this.append("[");
            this.comma = false;
            return this;
        }
    }

    private JSONWriter end(char var1, char var2) throws JSONException {
        if (this.mode != var1) {
            throw new JSONException(var1 == 'o' ? "Misplaced endObject." : "Misplaced endArray.");
        } else {
            this.pop(var1);

            try {
                this.writer.write(var2);
            } catch (IOException var4) {
                throw new JSONException(var4);
            }

            this.comma = true;
            return this;
        }
    }

    public JSONWriter endArray() throws JSONException {
        return this.end('a', ']');
    }

    public JSONWriter endObject() throws JSONException {
        return this.end('k', '}');
    }

    public JSONWriter key(String var1) throws JSONException {
        if (var1 == null) {
            throw new JSONException("Null key.");
        } else if (this.mode == 'k') {
            try {
                if (this.comma) {
                    this.writer.write(44);
                }

                this.stack[this.top - 1].putOnce(var1, Boolean.TRUE);
                this.writer.write(JSONObject.quote(var1));
                this.writer.write(58);
                this.comma = false;
                this.mode = 'o';
                return this;
            } catch (IOException var3) {
                throw new JSONException(var3);
            }
        } else {
            throw new JSONException("Misplaced key.");
        }
    }

    public JSONWriter object() throws JSONException {
        if (this.mode == 'i') {
            this.mode = 'o';
        }

        if (this.mode != 'o' && this.mode != 'a') {
            throw new JSONException("Misplaced object.");
        } else {
            this.append("{");
            this.push(new JSONObject());
            this.comma = false;
            return this;
        }
    }

    private void pop(char var1) throws JSONException {
        if (this.top <= 0) {
            throw new JSONException("Nesting error.");
        } else {
            int var2 = this.stack[this.top - 1] == null ? 97 : 107;
            if (var2 != var1) {
                throw new JSONException("Nesting error.");
            } else {
                --this.top;
                this.mode = (char)(this.top == 0 ? 100 : (this.stack[this.top - 1] == null ? 97 : 107));
            }
        }
    }

    private void push(JSONObject var1) throws JSONException {
        if (this.top >= 20) {
            throw new JSONException("Nesting too deep.");
        } else {
            this.stack[this.top] = var1;
            this.mode = (char)(var1 == null ? 97 : 107);
            ++this.top;
        }
    }

    public JSONWriter value(boolean var1) throws JSONException {
        return this.append(var1 ? "true" : "false");
    }

    public JSONWriter value(double var1) throws JSONException {
        return this.value(new Double(var1));
    }

    public JSONWriter value(long var1) throws JSONException {
        return this.append(Long.toString(var1));
    }

    public JSONWriter value(Object var1) throws JSONException {
        return this.append(JSONObject.valueToString(var1));
    }
}

