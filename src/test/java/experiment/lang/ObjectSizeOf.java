package experiment.lang;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class ObjectSizeOf {
	byte[] b = new byte[1];

	public static void main(String[] args) throws Exception{
		byUnsafe();
	}

	public static void byUnsafe() throws Exception{
		// sun.misc.Unsafe对象的objectFieldOffset(field)等方法结合反射来计算对象的大小。基本的思路如下:
		// 1. 通过反射获得一个类的Field
		// 2. 通过Unsafe的objectFieldOffset()获得每个Field的offSet
		// 3. 对Field按照offset排序，取得最大的offset，然后加上这个field的长度，再加上Padding对齐
		//Oop指针是4还是未压缩的8也可以通过unsafe.arrayIndexScale(Object[].class)来获得，这个方法返回一个引用所占用的长度
		Field field = Unsafe.class.getDeclaredField("theUnsafe");  
        field.setAccessible(true);  
        Unsafe unsafe = (Unsafe) field.get(null); 
        int objectRefSize = unsafe.arrayIndexScale(ObjectSizeOf.class);  
        System.out.println(objectRefSize);
	}

}
