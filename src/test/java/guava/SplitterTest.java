package guava;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @Description: Guava 中的 Splitter 类使用
 * <p>
 * @author: HochenChong
 * @date: 2020-5-8
 * @version v0.1
 */

public class SplitterTest {

    /**
     * 使用 Splitter 切割字符串
     */
    @Test
    public void testSplitString() {
        List<String> splitterList = Splitter.on("#").splitToList("a#b#c#d");
        assertThat(splitterList, notNullValue());
        assertThat(splitterList.size(), equalTo(4));
        assertThat(splitterList.get(0), equalTo("a"));
        assertThat(splitterList.get(1), equalTo("b"));
        assertThat(splitterList.get(2), equalTo("c"));
        assertThat(splitterList.get(3), equalTo("d"));
    }

    /**
     * 使用 Splitter 切割字符串，忽略空值
     */
    @Test
    public void testSplitStringOmitEmpty() {
        List<String> splitterList = Splitter.on("#").splitToList("a#b#c###d#");
        assertThat(splitterList, notNullValue());
        assertThat(splitterList.size(), equalTo(7));

        // 忽略空字符串
        List<String> splitterOmitEmptyList = Splitter.on("#").omitEmptyStrings().splitToList("a#b#c###d#");
        assertThat(splitterOmitEmptyList.size(), equalTo(4));
        assertThat(splitterOmitEmptyList.get(0), equalTo("a"));
        assertThat(splitterOmitEmptyList.get(1), equalTo("b"));
        assertThat(splitterOmitEmptyList.get(2), equalTo("c"));
        assertThat(splitterOmitEmptyList.get(3), equalTo("d"));
    }

    /**
     * 使用 Splitter 切割字符串，去除前后的空格
     */
    @Test
    public void testSplitTrimResult() {
        List<String> splitterList = Splitter.on("#").splitToList("a # b#c#d");
        assertThat(splitterList, notNullValue());
        assertThat(splitterList.size(), equalTo(4));
        assertThat(splitterList.get(0), equalTo("a "));
        assertThat(splitterList.get(1), equalTo(" b"));
        assertThat(splitterList.get(2), equalTo("c"));
        assertThat(splitterList.get(3), equalTo("d"));

        List<String> splitterTrimResultList = Splitter.on("#").trimResults().splitToList("a # b#c #d");
        assertThat(splitterTrimResultList, notNullValue());
        assertThat(splitterTrimResultList.size(), equalTo(4));
        assertThat(splitterTrimResultList.get(0), equalTo("a"));
        assertThat(splitterTrimResultList.get(1), equalTo("b"));
        assertThat(splitterTrimResultList.get(2), equalTo("c"));
        assertThat(splitterTrimResultList.get(3), equalTo("d"));

        /*
        将所有有空格的地方都去掉
            先对空格进行处理，再进行分割

        Strings.nullToEmpty() 将 null 转换为 '' 空字符串，避免传入 removeFrom 传入的是 null 而报空指针异常
         */
        String removeWhitespaceString = CharMatcher.whitespace().removeFrom(Strings.nullToEmpty("a #  b#c  c#d"));
        assertThat(removeWhitespaceString, equalTo("a#b#cc#d"));
        List<String> removeWhitespaceSplitterList = Splitter.on("#").splitToList(removeWhitespaceString);
        // 写成一行
        // List<String> removeWhitespaceSplitterList = Splitter.on("#").splitToList(CharMatcher.whitespace().removeFrom(Strings.nullToEmpty("a #  b#c  c#d")));
        assertThat(removeWhitespaceSplitterList, notNullValue());
        assertThat(removeWhitespaceSplitterList.size(), equalTo(4));
        assertThat(removeWhitespaceSplitterList.get(0), equalTo("a"));
        assertThat(removeWhitespaceSplitterList.get(1), equalTo("b"));
        assertThat(removeWhitespaceSplitterList.get(2), equalTo("cc"));
        assertThat(removeWhitespaceSplitterList.get(3), equalTo("d"));

        String testString = " c c ";
        assertThat(CharMatcher.javaLetterOrDigit().retainFrom(testString), equalTo("cc"));
    }

    /**
     * 使用 Splitter 切割字符串，设置拆分的最多个分割数，最后一个字符串为剩余的内容
     */
    @Test
    public void testSplitLimit() {
        List<String> splitterList = Splitter.on("#").limit(3).splitToList("a#b#c#d");
        assertThat(splitterList, notNullValue());
        assertThat(splitterList.size(), equalTo(3));
        assertThat(splitterList.get(0), equalTo("a"));
        assertThat(splitterList.get(1), equalTo("b"));
        assertThat(splitterList.get(2), equalTo("c#d"));
    }

    /**
     * 使用 Splitter 切割字符串，根据长度分割
     */
    @Test
    public void testSplitFixedLength() {
        List<String> splitterList = Splitter.fixedLength(3).splitToList("aaabbbcccddd");
        assertThat(splitterList, notNullValue());
        assertThat(splitterList.size(), equalTo(4));
        assertThat(splitterList.get(0), equalTo("aaa"));
        assertThat(splitterList.get(1), equalTo("bbb"));
        assertThat(splitterList.get(2), equalTo("ccc"));
        assertThat(splitterList.get(3), equalTo("ddd"));
    }

    /**
     * 使用 Splitter 切割字符串，设置最多个分割数，最后一个字符串为剩余的内容
     */
    @Test
    public void testSplitPattern() {
        List<String> splitterList = Splitter.onPattern("\\#").splitToList("a#b#c#d");
        assertThat(splitterList, notNullValue());
        assertThat(splitterList.size(), equalTo(4));
        assertThat(splitterList.get(0), equalTo("a"));
        assertThat(splitterList.get(1), equalTo("b"));
        assertThat(splitterList.get(2), equalTo("c"));
        assertThat(splitterList.get(3), equalTo("d"));
    }

    /**
     * 使用 Splitter 切割字符串，通过 withKeyValueSeparator 方法切割 key 与 value
     */
    @Test
    public void testSplitToMap() {
        Map<String, String> splitterMap = Splitter.on("#").withKeyValueSeparator("=").split("a=b#c=d");
        assertThat(splitterMap, notNullValue());
        assertThat(splitterMap.size(), equalTo(2));
        assertThat(splitterMap.get("a"), equalTo("b"));
        assertThat(splitterMap.get("c"), equalTo("d"));
    }
}
