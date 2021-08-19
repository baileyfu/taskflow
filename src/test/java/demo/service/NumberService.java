package demo.service;

import java.util.List;

/**
 * 处理业务逻辑；不跟taskflow耦合
 * @author bailey
 * @date 2021年8月19日
 */
public class NumberService {

	public int findMax(List<Integer> source) {
		int max = 0;
		for (int i : source) {
			max = max > i ? max : i;
		}
		return max;
	}
	
	public int findMin(List<Integer> source) {
		int min = 0;
		for (int i : source) {
			min = min < i ? min : i;
		}
		return min;
	}
	
	public boolean checkNumber(int maxValue,int minValue,int diff) {
		return Math.abs(maxValue - minValue) < diff;
	}
	
}
