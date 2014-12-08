package rs.poc.models;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class User {

	private String user_id;
	private int birthYear;
	private boolean gender;
	private Timestamp joinedAt;
	private String location;
	private HashSet<String> friendIds;

	public User() {
		friendIds = new HashSet<String>();
	}

	public void addFriend(String friendId) {
		friendIds.add(friendId);
	}

	public Set<String> getFriends() {
		return friendIds;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public Timestamp getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(Timestamp joinedAt) {
		this.joinedAt = joinedAt;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(obj.getClass() != this.getClass())
			return false;
		User evt = (User) obj;
		return this.user_id.equals(evt.user_id);
	}

	@Override
	public String toString() {
		return user_id + "|" + gender + "|" + birthYear + "|" + joinedAt + "|"
				+ location;
	}

}
