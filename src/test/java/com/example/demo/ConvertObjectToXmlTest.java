/**
 * 文件名：ConvertObjectToXmlTest.java
 * 描述：
 **/
package com.example.demo;

import com.hand.ConvertObjectAndXml;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author yangzhuo-hd@139.com
 * @version 1.0，2019/11/14
 * @date 2019/11/14 12:01
 */
public class ConvertObjectToXmlTest {
    static class Employee{
        public int employee_id;
        public String last_name;
        public double salary;

        public int getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(int employee_id) {
            this.employee_id = employee_id;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public double getSalary() {
            return salary;
        }

        public void setSalary(double salary) {
            this.salary = salary;
        }
    }

    public static void main(String[] args) {
        List<Employee> list = new ArrayList<Employee>();
        for(int i = 0; i < 3; i++){
            Employee e = new Employee();
            e.setEmployee_id(i);
            e.setLast_name("employee0" + i);
            e.setSalary(1231.1 + i);
            list.add(e);
        }
        try {
            String xmlString = ConvertObjectAndXml.listtoXml(list);
            System.out.println(xmlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
