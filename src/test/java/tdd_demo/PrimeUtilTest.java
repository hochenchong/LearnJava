package tdd_demo;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Description: PrimeUtil 单元测试
 * @author: HochenChong
 * @date: 2018-08-12
 * @version v0.1
 */

public class PrimeUtilTest {
	// 边界条件测试
	@Test
	public void testGetPrimesForEmptyResult() {
		int[] expected = {};
		
		Assert.assertArrayEquals(expected, PrimeUtil.getPrime(2));
		Assert.assertArrayEquals(expected, PrimeUtil.getPrime(0));
		Assert.assertArrayEquals(expected, PrimeUtil.getPrime(-1));
	}
	
	// 正常输入测试
	@Test
	public void testGetPrimes() {
		Assert.assertArrayEquals(new int[]{2, 3, 5, 7}, PrimeUtil.getPrime(9));
		Assert.assertArrayEquals(new int[]{2, 3, 5, 7, 11, 13}, PrimeUtil.getPrime(17));
		Assert.assertArrayEquals(new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29}, 
				PrimeUtil.getPrime(30));
	}
}
