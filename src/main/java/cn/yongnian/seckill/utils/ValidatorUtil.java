package cn.yongnian.seckill.utils;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 */
public class ValidatorUtil {
    private static final Pattern mobilePattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src){
        if(StringUtils.isEmpty(src)){
            return false;
        }
        Matcher matcher = mobilePattern.matcher(src);
        return matcher.matches();
    }
}
