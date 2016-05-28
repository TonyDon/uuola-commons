package com.uuola.commons.constant;

import java.util.regex.Pattern;

public class CST_REGEX {

    public static final Pattern RE_EMAIL = Pattern.compile("[a-zA-Z0-9\\-_\\.]{3,}+@+[a-zA-Z0-9\\-_\\.]{3,}");
    public static final Pattern RE_BYTE = Pattern.compile("\\d{1,2}");
    public static final Pattern RE_SHORT = Pattern.compile("\\d{1,5}");
    public static final Pattern RE_INT = Pattern.compile("\\d{1,9}");
    public static final Pattern RE_LONG = Pattern.compile("\\d{1,18}");
    public static final Pattern RE_TITLE = Pattern.compile("[^<>=\'\"]{2,64}");
    public static final Pattern RE_NICK = Pattern.compile("[a-zA-Z0-9\u4E00-\u9FA5]{2,16}");
    public static final Pattern RE_STRARR = Pattern.compile("[a-zA-Z0-9,_\\-/@]{1,512}");
    public static final Pattern RE_DATE = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})");
    public static final Pattern RE_DATETIME = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2}) (\\d{1,2}):(\\d{1,2}):(\\d{1,2})");
    public static final Pattern RE_DATETIME_NOT_SS = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2}) (\\d{1,2}):(\\d{1,2})");
    public static final Pattern RE_DOUBLE = Pattern.compile("([\\d\\-]{1,15})\\.([0-9]{1,2})");
    public static final Pattern RE_TEL = Pattern.compile("((\\(\\d{2,3}\\))|(\\d{3}\\-))?(13|15|18)\\d{9}");
    // 去掉 /* ... */ //...  注释
    public static final Pattern RE_CLR_NOTE = Pattern.compile("(/\\*[\\s\\S]*\\*/)|(\\s//.*)");
    public static final Pattern RE_CLR_XML = Pattern.compile("(<[.[^<]]*>)|\n|\r");
}
