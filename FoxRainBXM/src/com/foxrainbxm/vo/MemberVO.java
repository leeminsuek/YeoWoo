package com.foxrainbxm.vo;

/**
 * 멤�? ???��? ?��?��?? ???��?? VO
 * 
 * @version 1.0
 * @author ?��???
 * @since 2014.09.26
 */
public class MemberVO {
	/** ID */
	private String id = "";
	/** ?��?��???? */
	private String pswd = "";
	/** ?��? */
	private String name = "";
	/** ???��?? */
	private String nickName = "";
	/** �??��?��?��?? */
	private int myPoint = 0;
	/** ??�????��? */
	private String profilephoto = "";
	/** ?��??�?(주�??) */
	private String region = "";
	/** ?��?��? */
	private String greeting = "";
	/** ?��??�??��? */
	private String motto = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPswd() {
		return pswd;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getMyPoint() {
		return myPoint;
	}

	public void setMyPoint(int myPoint) {
		this.myPoint = myPoint;
	}

	public String getProfilephoto() {
		return profilephoto;
	}

	public void setProfilephoto(String profilephoto) {
		this.profilephoto = profilephoto;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getGreeting() {
		return greeting;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	public String getMotto() {
		return motto;
	}

	public void setMotto(String motto) {
		this.motto = motto;
	}
}
