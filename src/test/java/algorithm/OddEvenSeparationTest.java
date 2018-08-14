package algorithm;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Description: algorithm.OddEvenSeparation.java 的测试类
 * @author: HochenChong
 * @date: 2018-08-13
 * @version v0.1
 */

public class OddEvenSeparationTest {

	@Test
	public void testOddEvenSeparation() {
		int[] arrays1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		int[] arrays2 = { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
		int[] arrays3 = { 10, 1, 9, 2, 8, 3, 7, 4, 6, 5};
		
		Assert.assertArrayEquals(new int[] {1, 9, 3, 7, 5, 6, 4, 8, 2, 10}, 
				OddEvenSeparation.oddEvenSeparation(arrays1));
		Assert.assertArrayEquals(new int[] {1, 9, 3, 7, 5, 6, 4, 8, 2, 10}, 
				OddEvenSeparation.oddEvenSeparation(arrays2));
		Assert.assertArrayEquals(new int[] {5, 1, 9, 7, 3, 8, 2, 4, 6, 10}, 
				OddEvenSeparation.oddEvenSeparation(arrays3));
	}
}
