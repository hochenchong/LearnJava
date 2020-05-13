package guava;

import com.google.common.base.Strings;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @Description: Guava 中的 Strings 字符串处理类使用
 * <p>
 * @author: HochenChong
 * @date: 2020-5-13
 * @version v0.1
 */

public class StringsTest {
    /*
    空字符串转为 null
        但是，如果是 " " 这样子没内容的空字符串是不会转为 null
     */
    @Test
    public void testEmptyToNull() {
        assertThat(Strings.emptyToNull(""), nullValue());
        assertThat(Strings.emptyToNull(""), equalTo(null));
        assertThat(Strings.emptyToNull(" "), equalTo(" "));
    }

    /*
    null 转为空字符串
    如果不是 null，则保持不变，可以用来对 String 进行处理，避免抛空指针异常
     */
    @Test
    public void testNullToEmpty() {
        assertThat(Strings.nullToEmpty(null), equalTo(""));
        assertThat(Strings.nullToEmpty("a"), equalTo("a"));
    }

    /*
    获取两个字符串的公共前缀
     */
    @Test
    public void testCommonPrefix() {
        assertThat(Strings.commonPrefix("hello", "hi"), equalTo("h"));
        assertThat(Strings.commonPrefix("hello", "world"), equalTo(""));
    }

    /*
    获取两个字符串的公共后缀
     */
    @Test
    public void testCommonSuffix() {
        assertThat(Strings.commonSuffix("hello", "lo"), equalTo("lo"));
        assertThat(Strings.commonSuffix("hello", "world"), equalTo(""));
    }

    /*
    判断字符串是否是 null 或者空字符串
        但是，如果是 " " 这样子没内容的空字符串会认为是 FALSE
     */
    @Test
    public void testIsNullOrEmpty() {
        assertThat(Strings.isNullOrEmpty(null), equalTo(Boolean.TRUE));
        assertThat(Strings.isNullOrEmpty(""), equalTo(Boolean.TRUE));
    }

    /*
    宽松的格式化
        每次出现 %s，就在参数中适配
        如果出现的 %s 的个数与参数个数不匹配
            %s 的个数比参数个数多时，没匹配到的 %s 保持 %s
            %s 的个数比参数个数少时，多余的参数，用 [] 包起来，拼接在字符串后面
     */
    @Test
    public void testLenientFormat() {
        assertThat(Strings.lenientFormat("测试一下：%s", "张三"), equalTo("测试一下：张三"));
        assertThat(Strings.lenientFormat("测试一下：%s", 1), equalTo("测试一下：1"));
        assertThat(Strings.lenientFormat("测试一下：%s"), equalTo("测试一下：%s"));
        assertThat(Strings.lenientFormat("测试一下：%s", null), equalTo("测试一下：(Object[])null"));
        assertThat(Strings.lenientFormat("测试一下：%s，%s", "null"), equalTo("测试一下：null，%s"));
        assertThat(Strings.lenientFormat("测试一下：%s", "aaa", "bbb", "ccc"), equalTo("测试一下：aaa [bbb, ccc]"));
    }

    /*
    设置最小长度，字符串未达到最小长度时，使用设置字符补全长度
        padStart：在字符串的前面补全
        padEnd：在字符串的后面补全
     */
    @Test
    public void testPad() {
        assertThat(Strings.padStart("str", 6, 'a'), equalTo("aaastr"));
        assertThat(Strings.padStart("str", 2, 'a'), equalTo("str"));

        assertThat(Strings.padEnd("str", 6, 'a'), equalTo("straaa"));
        assertThat(Strings.padEnd("str", 2, 'a'), equalTo("str"));
    }

    /*
    将字符串重复多次
     */
    @Test
    public void testRepeat() {
        assertThat(Strings.repeat("a", 3), equalTo("aaa"));
    }
}
