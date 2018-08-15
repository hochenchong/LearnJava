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

	/*
	 * 测试奇偶分离
	 */
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
	
	/*
	 * 选择排序测试
	 */
	@Test
	public void testSelectSort() {
		int[] arrays1 = { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
		int[] arrays2 = { 10, 1, 9, 2, 8, 3, 7, 4, 6, 5};
		
		Assert.assertArrayEquals(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 
				OddEvenSeparation.selectSort(arrays1, 0, arrays1.length-1));
		Assert.assertArrayEquals(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 
				OddEvenSeparation.selectSort(arrays2, 0, arrays2.length-1));
	}
	
	/*
	 * 测试奇偶分离后，分别按从小到大排序
	 */
	@Test
	public void testOddEvenSeparationAndSelectSort() {
		int[] arrays = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

		arrays = OddEvenSeparation.oddEvenSeparation(arrays);
		// 奇偶分离
		Assert.assertArrayEquals(new int[] {1, 9, 3, 7, 5, 6, 4, 8, 2, 10}, arrays);
		
		// 找到奇偶分离的交界，下标为 limit 为该数组中的奇偶交界处中的奇数下标
		int limit = 0;
		for (limit = 0; limit < arrays.length - 1; limit++) {
			if (arrays[limit] % 2 == 1 && arrays[limit + 1] % 2 == 0) {
				break;
			}
		}
		
		// 奇偶分离后奇数从小到大排序
		arrays = OddEvenSeparation.selectSort(arrays, 0, limit);
		Assert.assertArrayEquals(new int[] {1, 3, 5, 7, 9, 6, 4, 8, 2, 10}, arrays);
		
		// 偶数从小到大排序
		arrays = OddEvenSeparation.selectSort(arrays, limit + 1, arrays.length - 1);
		Assert.assertArrayEquals(new int[] {1, 3, 5, 7, 9, 2, 4, 6, 8, 10}, arrays);
	}
	
	// 测试一下 oddEvenSeparationSort 方法，是否奇偶分离，并从小到大排序
	@Test
	public void testOddEvenSeparationSort() {
		int[] arrays1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		int[] arrays2 = { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
		int[] arrays3 = { 10, 1, 9, 2, 8, 3, 7, 4, 6, 5};
		
		Assert.assertArrayEquals(new int[] {1, 3, 5, 7, 9, 2, 4, 6, 8, 10}, 
				OddEvenSeparation.oddEvenSeparationSort(arrays1));
		Assert.assertArrayEquals(new int[] {1, 3, 5, 7, 9, 2, 4, 6, 8, 10}, 
				OddEvenSeparation.oddEvenSeparationSort(arrays2));
		Assert.assertArrayEquals(new int[] {1, 3, 5, 7, 9, 2, 4, 6, 8, 10}, 
				OddEvenSeparation.oddEvenSeparationSort(arrays3));
	}
}
