package algorithm;

import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 面试题：计算字符串中各个字符出现的次数
 * @author: HochenChong
 * @date: 2020-05-16
 * @version v0.1
 */

public class CharCountTest {
    /*
     * 这里直接以输出到控制台的方式
     * 前三种方法对字符进行了排序处理
     */
    @Test
    public void testCharCount() {
        String s1 = "abasdcc";
        String s2 = "aba sdcc";
        String s3 = "aaabbbdddccc";

        System.out.println("---- charCountByCharArray ----");
        charCountByCharArray(s1);
        charCountByCharArray(s2);
        charCountByCharArray(s3);
        System.out.println();

        System.out.println("---- charCountBySubString ----");
        charCountBySubString(s1);
        charCountBySubString(s2);
        charCountBySubString(s3);
        System.out.println();

        System.out.println("---- charCountByGuavaSplitter ----");
        charCountByGuavaSplitter(s1);
        charCountByGuavaSplitter(s2);
        charCountByGuavaSplitter(s3);
        System.out.println();

        System.out.println("---- charCountByReplace ----");
        charCountByReplace(s1);
        charCountByReplace(s2);
        charCountByReplace(s3);
        System.out.println();
    }

    public Boolean isNullOrEmpty(String string) {
        return string == null || "".equals(string);
    }

    /*
    使用 toCharArray() 方法，调用的是 native 方法：System.arraycopy()
     */
    public void charCountByCharArray(String string) {
        if (!isNullOrEmpty(string)) {
            // 使用 TreeMap 进行了排序，直接使用 HashMap 也可以，在输出时对流调用 sorted() 方法进行排序即可
            Map<String, Integer> charGroup = new TreeMap<>();
            char[] chars = string.toCharArray();

            for (char aChar : chars) {
                String charString = String.valueOf(aChar);
                Integer count = 1;
                if (charGroup.containsKey(charString)) {
                    count += charGroup.get(charString);
                }

                charGroup.put(charString, count);
            }
            charGroup.keySet().forEach(s -> System.out.print(s + charGroup.get(s)));
        }

        System.out.println();
    }

    /*
    使用 String.subString() 来分割字符串，每当获取一个字符时，就去 Map 里判断是否有，有则 value 加 1；没有则 put 进去一个 value 为 1 的键值对
     */
    public void charCountBySubString(String string) {
        if (!isNullOrEmpty(string)) {
            Map<String, Integer> charGroup = new HashMap<>();
            for (int i = 0; i < string.length(); i++) {
                String charString = string.substring(i, i + 1);
                Integer count = 1;

                if (charGroup.containsKey(charString)) {
                    count += charGroup.get(charString);
                }

                charGroup.put(charString, count);
            }
            charGroup.keySet().stream().sorted().forEach(s -> System.out.print(s + charGroup.get(s)));
        }

        System.out.println();
    }

    /*
    使用 Splitter.fixedLength() 方法按长度分割，并对字符进行排序输出
     */
    public void charCountByGuavaSplitter(String string) {
        if (!isNullOrEmpty(string)) {
            Map<String, List<String>> charGroup = Splitter.fixedLength(1).splitToList(string).stream().collect(Collectors.groupingBy(s -> s));
            charGroup.keySet().stream().sorted().forEach(s -> System.out.print(s + charGroup.get(s).size()));
        }

        System.out.println();
    }

    /*
    使用替换的方式进行计算，没有对字符进行排序
     */
    public void charCountByReplace(String string) {
        charCountByReplace1(string);
        System.out.println();
    }

    private void charCountByReplace1(String string) {
        if (!isNullOrEmpty(string)) {
            String headChar = string.substring(0, 1);
            String replaceString = string.replace(headChar, "");
            System.out.print(headChar + (string.length() - replaceString.length()));

            charCountByReplace1(replaceString);
        }
    }
}
