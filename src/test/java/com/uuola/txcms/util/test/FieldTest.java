package com.uuola.txcms.util.test;

import java.lang.reflect.Field;


public class FieldTest {

    private Long size;
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Field[] fs = FieldTest.class.getDeclaredFields();
        for(Field f : fs){
            System.out.println(f.getType().getName() +" " + f.getClass());
        }
    }

}
