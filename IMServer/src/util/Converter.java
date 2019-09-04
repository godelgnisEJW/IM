package util;

public class Converter {
	public static int changeToInt(byte[] num) {
		int result = 0;
		for(int i = 1; i <= num.length; i++) {
			int tmp = num[i - 1];
			if (tmp < 0) {
				tmp += 256;
			}
			tmp = tmp << (i - 1) * 8;
			result += tmp;
		}
		return result;
	}
	public static byte[] changeToBytes(int num) {
	    byte[] result = new byte[4];
	    for(int i = 1; i <= 4; i++) {
	    	result[i - 1] = (byte)(num >> (i - 1) * 8);
	    }
	    return result;
	}
	
	public static void main(String[] args) {
		int t = 257;
		byte[] b = {1,1};
		
		byte[] result = changeToBytes(t);
		for (byte c : result) {
			System.out.println((byte)c);
		}
		
		System.out.println(changeToInt(b));
	}
}
