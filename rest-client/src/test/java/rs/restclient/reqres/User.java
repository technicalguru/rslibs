/**
 * 
 */
package rs.restclient.reqres;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * Payload class User
 */
public class User {

	private long   id;
	private String email;
	private String firstName;
	private String lastName;
	private String avatar;
	private MetaData meta;
	
	public User() {}
	
	public User(long id, String email, String firstName, String lastName, String avatar) {
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.avatar = avatar;
	}


	/**
	 * Returns the id.
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * Sets the id.
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * Returns the email.
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * Sets the email.
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * Returns the firstName.
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * Sets the firstName.
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * Returns the lastName.
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * Sets the lastName.
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * Returns the avatar.
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}
	/**
	 * Sets the avatar.
	 * @param avatar the avatar to set
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	/**
	 * Returns the meta.
	 * @return the meta
	 */
	@JsonAlias("_meta")
	public MetaData getMeta() {
		return meta;
	}

	/**
	 * Sets the meta.
	 * @param meta the meta to set
	 */
	@JsonAlias("_meta")
	public void setMeta(MetaData meta) {
		this.meta = meta;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", avatar=" + avatar + "]";
	}
	
	
}
