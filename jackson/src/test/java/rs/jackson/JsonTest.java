package rs.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * Test {@link Json} serialization and deserialization
 * @author ralph
 *
 */
public class JsonTest {

	private static final String JSON1        = "{ \"firstName\": \"Max\",  \"lastName\": \"Mustermann\", \"birthday\":\"2020-01-01\", \"age\":6,  \"gender\":\"MALE\",   \"phoneNumber\":\"+49 610 12345678\" }";
	private static final String JSON2        = "{ \"firstName\": \"Jane\", \"lastName\": \"Doe\",        \"birthday\":\"2000-01-01\", \"age\":26, \"gender\":\"FEMALE\", \"phoneNumber\":\"+1 555 12345678\" }";
	private static final String JSON_ARRAY   = "["+JSON1+","+JSON2+"]";
	private static final String JSON_COMPLEX = "{ \"person1\":"+JSON1+", \"person2\":{ \"data\":"+JSON2+"}}";
	
	private static final Person PERSON1 = new Person("Max",  "Mustermann", LocalDate.of(2020, Month.JANUARY, 1),  6, "MALE",   "+49 610 12345678");
	private static final Person PERSON2 = new Person("Jane", "Doe",        LocalDate.of(2000, Month.JANUARY, 1), 26, "FEMALE", "+1 555 12345678");
	
	private Json JSON = Json.builder().build();
	
	@Test
	public void testFromString() throws Exception {
		test(PERSON1, JSON.fromJson(JSON1, Person.class));
		test(PERSON2, JSON.fromJson(JSON2, Person.class));
	}
	
	@Test
	public void testFromList() throws Exception {
		ArrayList<Person> list = JSON.fromJson(JSON_ARRAY, JacksonUtils.getListType(Person.class));
		assertNotNull(list);
		assertEquals(2, list.size());
		test(PERSON1, list.get(0));
		test(PERSON2, list.get(1));
	}
	
	@Test
	public void testFromString_withNullPath() throws Exception {
		test(PERSON1, JSON.fromJson(JSON1, null, Person.class));
		test(PERSON2, JSON.fromJson(JSON2, null, Person.class));
	}
	
	@Test
	public void testFromString_withPath() throws Exception {
		test(PERSON1, JSON.fromJson(JSON_COMPLEX, "person1",      Person.class));
		test(PERSON2, JSON.fromJson(JSON_COMPLEX, "person2.data", Person.class));
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
