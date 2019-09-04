package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User extends RecursiveTreeObject<User> implements Serializable{
	//账号
	private StringProperty count = new SimpleStringProperty();
	//密码
	private StringProperty password = new SimpleStringProperty();
	//昵称
	private StringProperty nickName = new SimpleStringProperty();
	//年龄
	private IntegerProperty age = new SimpleIntegerProperty();
	//性别
	private StringProperty sex = new SimpleStringProperty();
	//ip地址
	private transient StringProperty ip = new SimpleStringProperty();
	//端口
	private transient IntegerProperty port = new SimpleIntegerProperty();
	//监听端口
	private  transient IntegerProperty listenPort = new SimpleIntegerProperty();
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String count, String password, String nickName, int age, String sex) {
		this.count.set(count);
		this.password.set(password);
		this.nickName.set(nickName);
		this.age.set(age);
		this.sex.set(sex);
	}

	private void writeObject(ObjectOutputStream out) throws IOException{
		out.writeObject(count.get());
		out.writeObject(password.get());
		out.writeObject(nickName.get());
		out.writeInt(age.get());
		out.writeObject(sex.get());
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		count = new SimpleStringProperty((String)in.readObject());
		password = new SimpleStringProperty((String)in.readObject());
		nickName = new SimpleStringProperty((String)in.readObject());
		age = new SimpleIntegerProperty(in.readInt());
		sex = new SimpleStringProperty((String)in.readObject());
		ip = new SimpleStringProperty();
		port = new SimpleIntegerProperty();
		listenPort = new SimpleIntegerProperty();
	}
	
	public String info() {
		return  "nickName:" + nickName.get() + ";" +
				"listenPort:" + listenPort.get()  + ";" + 
				"ip:" + ip.get() + ";" +
				"count:" + count.get();
	}
	
	@Override
	public String toString() {
		return  nickName.get() + "\t" + listenPort.get();
	}


	public User(String ip, int port) {
		this.ip.set(ip);
		this.port.set(port);
	}
	
	public String getIp() {
		return ip.get();
	}

	public int getPort() {
		return port.get();
	}

	public int getListenPort() {
		return listenPort.get();
	}

	public StringProperty ipProperty() {
		return ip;
	}

	public IntegerProperty portProperty() {
		return port;
	}

	public IntegerProperty listentPortProperty() {
		return listenPort;
	}
	
	public void setIp(String ip) {
		this.ip.set(ip);
	}

	public void setPort(int port) {
		this.port.set(port);
	}

	public void setListenPort(int listenPort) {
		this.listenPort.set(listenPort);
	}

	public StringProperty passwordProperty() {
		return password;
	}

	public void setPassword(String password) {
		this.password.set(password);
	}

	public StringProperty nickNameProperty() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName.set(nickName);
	}

	public IntegerProperty ageProperty() {
		return age;
	}

	public void setAge(int age) {
		this.age.set(age);
	}

	public StringProperty sexProperty() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex.set(sex);
	}

	public StringProperty countProperty() {
		return count;
	}

	public void setCount(String count) {
		this.count.set(count);
	}

	public String getCount() {
		return count.get();
	}

	public String getPassword() {
		return password.get();
	}

	public String getNickName() {
		return nickName.get();
	}

	public int getAge() {
		return age.get();
	}

	public String getSex() {
		return sex.get();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof User)) {
			return false;
		}
		User otherUser = (User)obj;
		if(otherUser.getCount().equals(this.getCount()) 
				&& otherUser.getAge() == this.getAge()
				&& otherUser.getPassword().equals(this.getPassword())
				&& otherUser.getNickName().equals(this.getNickName())
				&& otherUser.getSex().equals(this.getSex())
				) {
			return true;
		}else {
			return false;
		}
	}
	
	
}
