package rs.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import rs.baselib.test.PersonBuilder.Person;

/**
 * Test {@link Json} serialization and deserialization
 * @author ralph
 *
 */
public class YamlTest {

	private static final String YAML1        = "firstName: \"Max\"\nlastName: \"Mustermann\"\nbirthday: \"2020-01-01\"\nage: 6\ngender: \"MALE\"\nphoneNumber: \"+49 610 12345678\"";
	private static final String YAML2        = "firstName: \"Jane\"\nlastName: \"Doe\"\nbirthday: \"2000-01-01\"\nage: 26\ngender: \"FEMALE\"\nphoneNumber: \"+1 555 12345678\"";
	private static final String YAML_ARRAY   = 
			"- firstName: \"Max\"\n  lastName: \"Mustermann\"\n  birthday: \"2020-01-01\"\n  age: 6\n  gender: \"MALE\"\n  phoneNumber: \"+49 610 12345678\"\n"+
			"- firstName: \"Jane\"\n  lastName: \"Doe\"\n  birthday: \"2000-01-01\"\n  age: 26\n  gender: \"FEMALE\"\n  phoneNumber: \"+1 555 12345678\"";
	private static final String YAML_COMPLEX =
			"person1:\n"+
			"  firstName: \"Max\"\n  lastName: \"Mustermann\"\n  birthday: \"2020-01-01\"\n  age: 6\n  gender: \"MALE\"\n  phoneNumber: \"+49 610 12345678\"\n"+
			"person2:\n  data:\n"+
			"    firstName: \"Jane\"\n    lastName: \"Doe\"\n    birthday: \"2000-01-01\"\n    age: 26\n    gender: \"FEMALE\"\n    phoneNumber: \"+1 555 12345678\"";
	
	private static final Person PERSON1 = new Person("Max",  "Mustermann", LocalDate.of(2020, Month.JANUARY, 1),  6, "MALE",   "+49 610 12345678");
	private static final Person PERSON2 = new Person("Jane", "Doe",        LocalDate.of(2000, Month.JANUARY, 1), 26, "FEMALE", "+1 555 12345678");
	
	private Yaml YAML = Yaml.builder().build();
	
	@Test
	public void testFromString() throws Exception {
		test(PERSON1, YAML.fromYaml(YAML1, Person.class));
		test(PERSON2, YAML.fromYaml(YAML2, Person.class));
	}
	
	@Test
	public void testFromList() throws Exception {
		ArrayList<Person> list = YAML.fromYaml(YAML_ARRAY, JacksonUtils.getListType(Person.class));
		assertNotNull(list);
		assertEquals(2, list.size());
		test(PERSON1, list.get(0));
		test(PERSON2, list.get(1));
	}
	
	@Test
	public void testFromString_withNullPath() throws Exception {
		test(PERSON1, YAML.fromYaml(YAML1, null, Person.class));
		test(PERSON2, YAML.fromYaml(YAML2, null, Person.class));
	}
	
	@Test
	public void testFromString_withPath() throws Exception {
		test(PERSON1, YAML.fromYaml(YAML_COMPLEX, "person1",      Person.class));
		test(PERSON2, YAML.fromYaml(YAML_COMPLEX, "person2.data", Person.class));
	}
	
	private static void test(Person expected, Person actual) {
		assertNotNull(actual);
		assertEquals(expected.firstName,   actual.firstName);
		assertEquals(expected.lastName,    actual.lastName);
		assertEquals(expected.birthday,    actual.birthday);
		assertEquals(expected.age,         actual.age);
		assertEquals(expected.gender,      actual.gender);
		assertEquals(expected.phoneNumber, actual.phoneNumber);
	}
}
