package model;

import java.util.HashMap;

public class UserCache {

	private static HashMap<String, User> cache = new HashMap<>();
	/**
	 * 通过解析字符串获得user对象，如果是第一次解析，需要确保uString是正确的格式，key:value形式，以";"作为分隔符，否则会出错
	 * 如果可以确定缓存中有这个对象了，那么也可以使用user的nickName来获取该对象
	 * 代表服务器的字符串是"nickName:聊天大厅;listenPort:1088;ip:127.0.0.1;count:0000"
	 * @param uString
	 * @return
	 */
	public static User getUserBy(String uString) {
		User user;
		//如果缓存中有uString，则直接返回相对应的user对象
		if ((user = cache.get(uString)) != null) {
			return user;
		}
		//如果ustring是昵称，则返回缓存中相对应的user对象
		for (User u : cache.values()) {
			if (u.getNickName().equals(uString)) {
				return u;
			}
		}
		String[] info = uString.split(":|;");
		String nickName = info[1];
		int listenPort = Integer.parseInt(info[3]);
		String ip = info[5];
		String count = info[7];
		user = new User();
		user.setListenPort(listenPort);
		user.setIp(ip);
		user.setCount(count);
		user.setNickName(nickName);
		cache.put(uString, user);
		return user;
	}
}