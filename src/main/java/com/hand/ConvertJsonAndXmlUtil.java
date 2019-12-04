/**
 * 文件名：ConvertJsonAndXmlUtil.java
 * 描述：
 **/
package com.hand;

import com.hand.util.FileUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.Charset;

/**
 * json转xml工具类
 *
 * @author yangzhuo-hd@139.com
 * @version 1.0，2019/11/12
 * @date 2019/11/12 15:41
 */
public class ConvertJsonAndXmlUtil {


    /**
      * @desc 从目录中读取json文件转换成xml写入到指定文件中
      * @param readFilePath 读取文件目录
      * @param writeFilePath 写入文件目录
      * @param rootNode 根节点
      * @param elementNode 自定义命名空间前缀
      * @param namespace 自定义命名空间
      * @date 2019/11/13 8:29
      * @return void
      **/
    public static void convertJsonToXmlByPath(String readFilePath
            , String writeFilePath, String rootNode, String elementNode
            , String namespace, String encoding){
        String json = null;
        try {
            json = FileUtil.readFile(readFilePath);
            String xml = convert(json, rootNode, elementNode, namespace, encoding);
            FileUtil.writeFile(xml, writeFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
      * @desc 将入参的jsonString转换成xml并返回
      * @param jsonString json格式字符串
      * @param rootNode 根节点
      * @param elementNode 自定义命名空间前缀
      * @param namespace 自定义命名空间
      * @date 2019/11/13 8:39
      * @return java.lang.String
      **/
    public static String convertJsonToXmlByString(String jsonString
            , String rootNode, String elementNode, String namespace, String encoding){
        if(jsonString == null || jsonString ==""){
            return null;
        }else{
            String xml ="";
            try {
                xml = convert(jsonString, rootNode, elementNode, namespace, encoding);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return xml;
        }
    }

    /**
      * @desc JSON转XML具体过程
      * @param json json字符串
      * @param rootNode 根节点
      * @date 2019/11/13 8:57
      * @return java.lang.String
      **/
    public static String convert(String json, String rootNode
            , String elementNode, String namespace, String encoding) throws JSONException
    {
        org.json.JSONObject jsonFileObject = new org.json.JSONObject(json);
        String xml = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n<"+ elementNode
                + ":" +rootNode + " " + namespace + ">"
                + org.json.XML.toString(jsonFileObject, elementNode)
                + "</" + elementNode + ":" +rootNode+">";
        return xml;
    }
}
