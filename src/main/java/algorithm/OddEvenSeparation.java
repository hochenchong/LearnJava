package algorithm;

import java.util.Arrays;

/**
 * @Description: 一个整型数组中，奇数偶数进行分离，不引入新的数组
 *     例如：
 *         输入数组 [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
 *         输出数组 [1, 9, 3, 7, 5, 6, 4, 8, 2, 10]
 *     扩展：
 *         可以奇偶分离后，让奇数，偶数分别从小到大排序
 * @author: HochenChong
 * @date: 2018-08-13
 * @version v0.1
 */

public class OddEvenSeparation {
	/**
	 * 将一个整型数组，奇偶分离，前面放奇数，后面放偶数
	 * @param arrays
	 * @return
	 */
	public static int[] oddEvenSeparation(int[] arrays) {
		// 两头同时查找
		int first_index;
		int last_index = arrays.length - 1;
		
		for (first_index = 0; first_index < last_index; first_index++) {
			// 当 arrays[first] 为偶数，arrays[last] 为奇数时，两者交换位置，并且 last 减 1
			if (arrays[first_index] % 2 == 0 && arrays[last_index] % 2 == 1) {
				int temp = arrays[first_index];
				arrays[first_index] = arrays[last_index];
				arrays[last_index] = temp;
				last_index --;
			} else if (arrays[first_index] % 2 == 0 && arrays[last_index] % 2 == 0) {
				// 当 arrays[first] 为偶数，arrays[last] 为偶数时，并且 last 减 1，first 减 1
				first_index--;
				last_index--;
			} else if (arrays[first_index] % 2 == 1 && arrays[last_index] % 2 == 0) {
				// 当 arrays[first] 为奇数，arrays[last] 为偶数时，并且 last 减 1
				last_index--;
			} 
			// 当 arrays[first] 为奇数，arrays[last] 为奇数时，不做任何处理
		}
		
		return arrays;
	}
	
	/**
	 * 选择排序 —— 从小到大排序
	 * @param arrays   要排序的数组
	 * @param first    要排序的第一个下标值
	 * @param last     要排序的最后一个下标值
	 * @return         返回排好序的数组
	 */
	public static int[] selectSort(int[] arrays, int first, int last) {
		for (; first < last; first++) {
			int min = first;
			for (int i = first + 1; i <= last; i++) {
				if (arrays[min] > arrays[i]) {
					min = i;
				}
			}
			if (min != first) {
				int temp = arrays[first];
				arrays[first] = arrays[min];
				arrays[min] = temp;
			}
		}

		return arrays;
	}
	
	/**
	 * 将一个整型数组，奇偶分离，前面放奇数，后面放偶数，并且奇数和偶数分别从小到大排序
	 *     一边分离时，一边做判断，不过感觉这样子揉在一起不太好，故自认为还是用上面那种方法组合使用即可
	 *     以下代码可以重构以下，例如提取个冒泡排序，这样子看起来会简洁很多
	 * 
	 * @param arrays
	 * @return
	 */
	public static int[] oddEvenSeparationSort(int[] arrays) {
		// 两头同时查找
		int first_index;
		int last_index = arrays.length - 1;

		for (first_index = 0; first_index < last_index; first_index++) {
			// 当 arrays[first] 为偶数，arrays[last] 为奇数时，两者交换位置，并且 last 减 1
			if (arrays[first_index] % 2 == 0 && arrays[last_index] % 2 == 1) {
				int temp = arrays[first_index];
				arrays[first_index] = arrays[last_index];
				arrays[last_index] = temp;

				// 奇数与前面的数字比较大小，如果比前面一个数字小，则交换位置，继续比较
				int now_index = first_index;
				for (int before_index = first_index - 1; before_index >= 0; before_index--) {
					// 如果当前位置的数字小于前一位数，则交换位置，并与前一个继续比较
					if (arrays[now_index] < arrays[before_index]) {
						temp = arrays[now_index];
						arrays[now_index] = arrays[before_index];
						arrays[before_index] = temp;

						// 当前位置减 1
						now_index--;
					} else {
						// 如果大于前一个数，则跳出循环
						break;
					}
				}

				// 偶数与后面的数字比较大小
				now_index = last_index;
				for (int after_index = last_index + 1; after_index < arrays.length; after_index++) {
					// 如果当前位置的数字大于前一位数，则交换位置，并与后一个继续比较
					if (arrays[now_index] > arrays[after_index]) {
						temp = arrays[now_index];
						arrays[now_index] = arrays[after_index];
						arrays[after_index] = temp;

						// 当前位置加 1
						now_index++;
					} else {
						// 如果小于后一个数，则跳出循环
						break;
					}
				}

				last_index--;

			} else if (arrays[first_index] % 2 == 0 && arrays[last_index] % 2 == 0) {
				// 偶数与后面的数字比较大小
				int now_index = last_index;
				for (int after_index = last_index + 1; after_index < arrays.length; after_index++) {
					// 如果当前位置的数字大于前一位数，则交换位置，并与后一个继续比较
					if (arrays[now_index] > arrays[after_index]) {
						int temp = arrays[now_index];
						arrays[now_index] = arrays[after_index];
						arrays[after_index] = temp;

						// 当前位置加 1
						now_index++;
					} else {
						// 如果小于后一个数，则跳出循环
						break;
					}
				}

				// 当 arrays[first] 为偶数，arrays[last] 为偶数时，并且 last 减 1，first 减 1
				first_index--;
				last_index--;

			} else if (arrays[first_index] % 2 == 1 && arrays[last_index] % 2 == 0) {
				// 奇数与前面的数字比较大小，如果比前面一个数字小，则交换位置，继续比较
				int now_index = first_index;
				int temp = 0;
				for (int before_index = first_index - 1; before_index >= 0; before_index--) {
					// 如果当前位置的数字小于前一位数，则交换位置，并与前一个继续比较
					if (arrays[now_index] < arrays[before_index]) {
						temp = arrays[now_index];
						arrays[now_index] = arrays[before_index];
						arrays[before_index] = temp;

						// 当前位置减 1
						now_index--;
					} else {
						// 如果大于前一个数，则跳出循环
						break;
					}
				}

				// 偶数与后面的数字比较大小
				now_index = last_index;
				for (int after_index = last_index + 1; after_index < arrays.length; after_index++) {
					// 如果当前位置的数字大于前一位数，则交换位置，并与后一个继续比较
					if (arrays[now_index] > arrays[after_index]) {
						temp = arrays[now_index];
						arrays[now_index] = arrays[after_index];
						arrays[after_index] = temp;

						// 当前位置加 1
						now_index++;
					} else {
						// 如果小于后一个数，则跳出循环
						break;
					}
				}

				// 当 arrays[first] 为奇数，arrays[last] 为偶数时，并且 last 减 1
				last_index--;
			} else {
				// 奇数与前面的数字比较大小，如果比前面一个数字小，则交换位置，继续比较
				int now_index = first_index;
				int temp = 0;
				for (int before_index = first_index - 1; before_index >= 0; before_index--) {
					// 如果当前位置的数字小于前一位数，则交换位置，并与前一个继续比较
					if (arrays[now_index] < arrays[before_index]) {
						temp = arrays[now_index];
						arrays[now_index] = arrays[before_index];
						arrays[before_index] = temp;

						// 当前位置减 1
						now_index--;
					} else {
						// 如果大于前一个数，则跳出循环
						break;
					}
				}
			}
		}
		System.out.println(Arrays.toString(arrays));

		return arrays;
	}
}
