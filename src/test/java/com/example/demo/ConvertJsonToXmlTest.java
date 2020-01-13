/**
 * 文件名：ConvertJsonToXmlTest.java
 * 描述：
 **/
package com.example.demo;

import com.hand.json.JSONException;
import com.hand.util.FileUtil;

import java.io.IOException;

import static com.hand.ConvertJsonAndXmlUtil.convert;

/**
 *
 *
 * @author yangzhuo-hd@139.com
 * @version 1.0，2019/11/13
 * @date 2019/11/13 11:52
 */
public class ConvertJsonToXmlTest {
    public static void main(String[] args) throws IOException, JSONException {
        String json = FileUtil.readFile("C:\\Users\\92074\\Desktop\\xmlAndjson\\test02.txt");
        String xml = convert(json, "qwer", "n01","xmlns:n01=\"http://esb.iyu.com\"", "utf-8");
        FileUtil.writeFile(xml, "C:\\Users\\92074\\Desktop\\xmlAndjson\\test03.xml");
    }
}
