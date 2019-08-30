/**
 * 
 */
package utils;

import java.util.UUID;

/**
 * 作者： 岳林辉
 * 日期： 2017年10月19日
*  描述：
 */
public class RandomUtils {
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
