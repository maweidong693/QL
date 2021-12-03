package com.weiwu.ql.main.mine.friends.data;
/**
 * 
* @ClassName: User 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author yiw
* @date 2015-12-28 下午3:45:04 
*
 */
public class User {
	private String id;    //uid
	private String im_id;
	private String name;
	private String headUrl;
	public User(String id, String name, String headUrl){
		this.id = id;
		this.name = name;
		this.headUrl = headUrl;
	}
	public User(String id, String im_id, String name, String headUrl){
		this.id = id;
		this.im_id = im_id;
		this.name = name;
		this.headUrl = headUrl;
	}

	public String getIm_id() {
		return im_id;
	}

	public void setIm_id(String im_id) {
		this.im_id = im_id;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeadUrl() {
		return headUrl;
	}
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	@Override
	public String toString() {
		return "id = " + id
				+ "; name = " + name
				+ "; headUrl = " + headUrl;
	}
}
