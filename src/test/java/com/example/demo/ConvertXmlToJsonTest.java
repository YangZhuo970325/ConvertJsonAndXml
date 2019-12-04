/**
 * 文件名：ConvertXmlToJsonTest.java
 * 描述：
 **/
package com.example.demo;

import com.hand.util.FileUtil;
import org.dom4j.Document;

import static com.hand.ConvertXmlAndJsonUtil.documentToJSONObject;
import static com.hand.ConvertXmlAndJsonUtil.strToDocument;

/**
 *
 *
 * @author yangzhuo-hd@139.com
 * @version 1.0，2019/11/13
 * @date 2019/11/13 11:53
 */
public class ConvertXmlToJsonTest {
    public static void main(String[] args) throws Exception {
        String s = FileUtil.readFile("C:\\Users\\92074\\Desktop\\test01.xml");
        Document document = strToDocument(s);
        String jsonString = documentToJSONObject(document).toJSONString();
        FileUtil.writeFile(jsonString, "C:\\Users\\92074\\Desktop\\test02.txt");
    }
}
