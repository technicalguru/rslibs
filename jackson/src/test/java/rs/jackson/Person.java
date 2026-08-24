package rs.jackson;

import java.time.LocalDate;

/**
 * Test class for serializing and deserializing.
 * 
 * @author ralph
 *
 */
public class Person {
	public String    firstName;
	public String    lastName;
	public LocalDate birthday;
	public int       age;
	public String    gender;
	public String    phoneNumber;
	
	public Person() {}
	
	public Person(String firstName, String lastName, LocalDate birthday, int age, String gender, String phoneNumber) {
		this.firstName   = firstName;
		this.lastName    = lastName;
		this.birthday    = birthday;
		this.age         = age;
		this.gender      = gender;
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "Person [firstName=" + firstName + ", lastName=" + lastName + ", birthday=" + birthday + ", age="
				+ age + ", gender=" + gender + ", phoneNumber=" + phoneNumber + "]";
	}
}
