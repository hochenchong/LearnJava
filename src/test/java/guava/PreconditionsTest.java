package guava;

import com.google.common.base.Preconditions;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @Description: Guava 中的 Preconditions 断言，用的不多
 * <p>
 * @author: HochenChong
 * @date: 2020-5-10
 * @version v0.1
 */
public class PreconditionsTest {
    @Test(expected = NullPointerException.class)
    public void testCheckNotNull() {
        Preconditions.checkNotNull(null);
    }

    @Test
    public void testCheckNotNullWithErrorMessage() {
        try {
            Preconditions.checkNotNull(null, "错误信息");
        } catch (NullPointerException e) {
            assertThat(e.getMessage(), equalTo("错误信息"));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testCheckStatus() {
        Preconditions.checkState("a".equals("b"));
    }
}
