/**
 * 文件名：ConvertJsonToXmlTest.java
 * 描述：
 **/
package com.example.demo;

import com.hand.util.FileUtil;
import org.json.JSONException;

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
        String json = FileUtil.readFile("C:\\Users\\92074\\Desktop\\test02.txt");
        String xml = convert(json, "qwer", "n01","xmlns:n01=\"http://soa.cmcc.com/JMS_BP_SOA_HQ_DistEipUserSrv\"", "unicode");
        FileUtil.writeFile(xml, "C:\\Users\\92074\\Desktop\\test03.xml");
    }
}
