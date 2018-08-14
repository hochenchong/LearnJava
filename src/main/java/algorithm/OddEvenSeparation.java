package algorithm;

import java.util.Arrays;

/**
 * @Description: 一个整型数组中，奇数偶数进行分离，不引入新的数组
 *     例如：
 *         输入数组 [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
 *         输出数组 [1, 9, 3, 7, 5, 6, 4, 8, 2, 10]
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
		System.out.println(Arrays.toString(arrays));
		
		return arrays;
	}
	
	// 将整型数组奇偶分离，同时按大小排序
	public static int[] oddEvenSort(int[] arrays) {
		arrays = oddEvenSeparation(arrays);
		
		// 找到奇偶分离的交界，下标为 limit 为该数组中的奇偶交界处中的奇数下标
		int limit = 0;
		for (limit = 0; limit < arrays.length - 1; limit++) {
			if (arrays[limit] % 2 == 1 && arrays[limit+1] % 2 == 0) {
				break;
			}
		}
		System.out.println(limit);
		
		// 对奇数部分进行快速排序
		
		// 对偶数部分进行快速排序
		return arrays;
	}
}
