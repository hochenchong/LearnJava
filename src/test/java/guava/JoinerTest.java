package guava;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @version v0.1
 * @Description: Guava 中的 Joiner 类使用
 * @author: HochenChong
 * @date: 2020-5-7
 */

public class JoinerTest {

    /**
     * 使用 Joiner 合并字符串，以及 Java 8 Stream 合并字符串
     * <p>
     * Joiner 中的 join 方法里，实际上是调用了 Joiner 里的另一个方法：
     * appendTo(new StringBuilder(), parts).toString()
     */
    @Test
    public void testJoinString() {
        List<String> stringList = Arrays.asList("a", "b", "c", "d");

        // 使用 Guava Joiner 处理
        String joinerResult = Joiner.on("#").join(stringList);
        assertThat(joinerResult, equalTo("a#b#c#d"));

        // 使用 Java 8 Stream 处理
        String streamResult = stringList.stream().collect(Collectors.joining("#"));
        assertThat(streamResult, equalTo("a#b#c#d"));
    }

    /**
     * 合并的字符串中有空值时抛异常
     */
    @Test(expected = NullPointerException.class)
    public void testJoinStringHaveNullValue() {
        List<String> stringList = Arrays.asList("a", "b", "c", "d", null);

        // 使用 Guava Joiner 处理
        String joinerResult = Joiner.on("#").join(stringList);
        assertThat(joinerResult, equalTo("a#b#c#d"));
    }

    /**
     * 要合并的字符串集合中有空值时跳过空值
     */
    @Test
    public void testJoinStringSkipNullValue() {
        List<String> stringList = Arrays.asList("a", "b", "c", "d", null);

        // 使用 Guava Joiner 处理
        StringBuilder joinerResult = Joiner.on("#").skipNulls().appendTo(new StringBuilder(), stringList);
        assertThat(joinerResult.toString(), equalTo("a#b#c#d"));

        // 使用 Java 8 Stream 处理
        String streamResult = stringList.stream().filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining("#"));
        assertThat(streamResult, equalTo("a#b#c#d"));
    }

    /**
     * 要合并的字符串集合中有空值时跳过空值，并写入到文件中
     */
    @Test
    public void testJoinStringSkipNullValueToWriter() {
        String fileNamePath = "a.txt";
        List<String> stringList = Arrays.asList("a", "b", "c", "d", null);

        try (FileWriter fileWriter = new FileWriter(new File(fileNamePath))) {
            Joiner.on("#").skipNulls().appendTo(fileWriter, stringList);
            assertThat(Files.isFile().test(new File(fileNamePath)), equalTo(Boolean.TRUE));
        } catch (IOException e) {
            fail();
        }

        new File(fileNamePath).delete();
    }

    /**
     * 要合并的字符串集合中有空值时，将空值替换为默认值
     */
    @Test
    public void testJoinStringReplaceNullValueWithDefaultValue() {
        String defaultValue = "DEFAULT";
        List<String> stringList = Arrays.asList("a", "b", null, "c", "d", null);

        // 使用 Guava Joiner 处理
        StringBuilder joinerResult = Joiner.on("#").useForNull(defaultValue).appendTo(new StringBuilder(), stringList);
        assertThat(joinerResult.toString(), equalTo("a#b#DEFAULT#c#d#DEFAULT"));

        // 使用 Java 8 Stream 处理
        String streamResult = stringList.stream().map(s -> (s == null || s.isEmpty()) ? defaultValue : s)
                .collect(Collectors.joining("#"));
        assertThat(streamResult, equalTo("a#b#DEFAULT#c#d#DEFAULT"));
    }

    /**
     * 合并 Map 中的 key value
     */
    @Test
    public void testJoinStringWithMap() {
        Map<String, String> stringMap = ImmutableMap.of("hello", "world", "Hi", "Java");

        // 使用 Guava Joiner 处理
        String joinerResult = Joiner.on("#").withKeyValueSeparator(":").join(stringMap);
        assertThat(joinerResult, equalTo("hello:world#Hi:Java"));

        // 使用 Java 8 Stream 处理
        String streamResult = stringMap.entrySet().stream().map(stringEntry -> stringEntry.getKey() + ":" + stringEntry.getValue())
                .collect(Collectors.joining("#"));
        assertThat(streamResult, equalTo("hello:world#Hi:Java"));
    }
}
