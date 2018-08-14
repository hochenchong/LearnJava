package tdd_demo;

import java.util.Arrays;

/**
 * @Description: 返回小于给定值 max 的所有素数组成的数组
 *     素数（质数）：在大于1的自然数中，除了1和它本身以外不再有其他因数。
 * @author: HochenChong
 * @date: 2018-08-12
 * @version v0.1
 */

public class PrimeUtil {
	/*public static int[] getPrime(int max) {
		if (max <= 2) {
			return new int[]{};
		} else {
			int[] newArray = new int[max];
			int size = 0;
			int j = 0;
			// 判断一个数是否是素数
			for (int i = 2; i < max; i++) {
				for (j = 2; j < i/2 + 1; j++) {
					if (i % j == 0) {
						break;
					}
				}
				if (j == i/2 + 1) {
					newArray[size++] = i;
				}
			}
			newArray = Arrays.copyOf(newArray, size);
			return newArray;
		}
	}*/
	
	/*
	 * 重构：
	 *     变量命名，IDE 支持的
	 *     函数抽取出来
	 *     控制与策略分开来
	 */
	public static int[] getPrime(int max) {
		if (max <= 2) {
			return new int[]{};
		} 
		
		int[] primes = new int[max];
		int count = 0;
		// 控制
		for (int num = 2; num < max; num++) {
			if (isPrime(num)) {
				primes[count++] = num;
			}
		}
		primes = Arrays.copyOf(primes, count);
		return primes;
	}

	// 策略
	/**
	 * 判断一个数是否是素数
	 * @param num
	 * @return
	 */
	private static boolean isPrime(int num) {
		for (int i = 2; i < num/2 + 1; i++) {
			if (num % i == 0) {
				return false;
			}
		}
		return true;
	}
	
}
