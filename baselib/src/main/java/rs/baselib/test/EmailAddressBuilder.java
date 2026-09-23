package rs.baselib.test;

import rs.baselib.test.PersonBuilder.Person;

/**
 * Builds valid email addresses
 * @author ralph
 *
 */
public class EmailAddressBuilder extends AbstractBuilder<String> {

	private Builder<PersonBuilder.Person> persons;
	private Builder<String>               domains;
	
	/**
	 * Constructor.
	 */
	public EmailAddressBuilder() {
		persons = BuilderUtils.$Person();
		domains = new ApexDomainBuilder();
	}

	/**
	 * Use the given builder for person names.
	 * @param persons builder for persons
	 * @return the {@link EmailAddressBuilder} for chaining
	 */
	public EmailAddressBuilder withPersons(Builder<PersonBuilder.Person> persons) {
		this.persons = persons;
		return this;
	}

	/**
	 * Use the given builder for email domains.
	 * @param domains builder for email domains
	 * @return the {@link EmailAddressBuilder} for chaining
	 */
	public EmailAddressBuilder withDomains(Builder<String> domains) {
		this.domains = domains;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String _build() {
		Person person = persons.build();
		return person.firstName+"."+person.lastName+"@"+domains.build();
	}
}
