/**
 * 文件名：ConvertXmlAndJsonUtil.java
 * 描述：
 **/
package com.hand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hand.util.FileUtil;
import org.dom4j.*;

import java.io.IOException;
import java.util.List;

/**
 * xml转json工具类
 *
 * @author yangzhuo-hd@139.com
 * @version 1.0，2019/11/13
 * @date 2019/11/13 8:06
 */
public class ConvertXmlAndJsonUtil {


    /**
      * @desc 读取文件中的xml转换为json并写入到指定文件中
      * @param readFilePath 读取文件路径
      * @param writeFilePath 写入文件路径
      * @date 2019/11/13 9:08
      * @return void
      **/
    public static void convertXmlToJsonByPath(String readFilePath
            , String writeFilePath){
        String s = null;
        try {
            s = FileUtil.readFile(readFilePath);
            Document document = strToDocument(s);
            String jsonString = documentToJSONObject(document).toJSONString();
            FileUtil.writeFile(jsonString, writeFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
      * @desc 将入参的xmlString转换成json并返回
      * @param xmlString xml格式字符串
      * @date 2019/11/13 9:11
      * @return java.lang.String
      **/
    public static String convertXmlToJsonByString(String xmlString){
        Document document = strToDocument(xmlString);
        String jsonString = documentToJSONObject(document).toJSONString();
        return jsonString;
    }

    /**
      * @desc 将xml字符串转换为Document
      * @param xml xml格式字符串
      * @date 2019/11/13 9:01
      * @return org.dom4j.Document
      **/
    public static Document strToDocument(String xml){
        try {
            return DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            return null;
        }
    }

    /**
      * @desc 将Document转换为Json对象
      * @param document document对象
      * @date 2019/11/13 9:02
      * @return com.alibaba.fastjson.JSONObject
      **/
    public static JSONObject documentToJSONObject(Document document){
        Element element = document.getRootElement();
        return elementToJSONObject(element);
    }

    /**
      * @desc 将element节点转换为json对象
      * @param node 节点
      * @date 2019/11/13 9:05
      * @return com.alibaba.fastjson.JSONObject
      **/
    public static JSONObject elementToJSONObject(Element node) {
        JSONObject result = new JSONObject();
        // 当前节点的名称、文本内容和属性
        List<Attribute> listAttr = node.attributes();// 当前节点的所有属性的list
        /*for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
            result.put(attr.getName(), attr.getValue());
        }*/
        // 递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();// 所有一级子节点的list
        if (!listElement.isEmpty()) {
            for (Element e : listElement) {// 遍历所有一级子节点
                //if (e.attributes().isEmpty() && e.elements().isEmpty())
                if (e.elements().isEmpty()) // 判断一级节点是否有子节点
                    result.put(e.getName(), e.getTextTrim());// 沒有则将当前节点作为上级节点的属性对待
                else {
                    if (!result.containsKey(e.getName())) // 判断父节点是否存在该一级节点名称的属性
                        result.put(e.getName(), new JSONArray());// 没有则创建
                    if(listElement.size() == 1) {  //判断子节点是一个list还是一个object
                        result.put(e.getName(),elementToJSONObject(e));// 如果是一个object,将该一级节点放入该节点名称的属性对应的值中
                    } else {
                        ((JSONArray) result.get(e.getName())).add(elementToJSONObject(e));// 如果是一个list,将该一级节点放入该节点名称的属性对应的值中
                    }
                }
            }
        }
        return result;
    }
}
