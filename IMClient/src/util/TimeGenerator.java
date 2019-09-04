package util;

import java.util.Date;

public class TimeGenerator {
	public static String getTime() {
		Date now = new Date();
		String[] tokens = now.toString().split("\\s");
		String time = tokens[5]+"-"+now.getMonth()+"-"+tokens[2]
		+"\t"+tokens[3];
		return time;
	}
//	public static void main(String[] args) {
//		System.out.println(TimeGenerator.getTime());
//	}
}
