/**
 * 文件名：FileUtil.java
 * 描述：
 **/
package com.hand.util;

import java.io.*;
import java.nio.charset.Charset;

/**
 *
 *
 * @author yangzhuo-hd@139.com
 * @version 1.0，2019/11/13
 * @date 2019/11/13 8:13
 */
public class FileUtil {

    /**
      * @desc 根据文件目录读取文件
      * @param filepath 文件读取路径
      * @date 2019/11/13 8:14
      * @return java.lang.String
      **/
    public static String readFile(String filepath) throws FileNotFoundException, IOException
    {

        StringBuilder sb = new StringBuilder();
        InputStream in = new FileInputStream(filepath);
        Charset encoding = Charset.defaultCharset();

        Reader reader = new InputStreamReader(in, encoding);

        int r = 0;
        while ((r = reader.read()) != -1)//Note! use read() rather than readLine()
        //Can process much larger files with read()
        {
            char ch = (char) r;
            sb.append(ch);
        }

        in.close();
        reader.close();

        return sb.toString();
    }

    /**
      * @desc 将输出内容写入到指定目录中
      * @param output 输出内容
      * @param filepath 输出路径
      * @date 2019/11/13 8:17
      * @return void
      **/
    public static void writeFile(String output, String filepath) throws FileNotFoundException, IOException
    {
        FileWriter ofstream = new FileWriter(filepath);
        try (BufferedWriter out = new BufferedWriter(ofstream)) {
            out.write(output);
        }
    }
}
