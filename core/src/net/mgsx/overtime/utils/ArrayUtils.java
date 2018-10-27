package net.mgsx.overtime.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class ArrayUtils {
	public static <T> Array<T> randomize(Array<T> array){
		for(int i=0 ; i<array.size ; i++){
			array.swap(i, MathUtils.random(array.size-1));
		}
		return array;
	}

	public static <T> T random(T[] values) {
		return values[MathUtils.random(values.length-1)];
	}
}
