package rs.baselib.test;

import rs.baselib.util.CommonUtils;

/**
 * Generates international random phone number
 * @author ralph
 *
 */
public class RandomPhoneBuilder implements Builder<String> {

	/** +49 (0)610 12345678 */
	public static final String INTERNATIONAL_FORMAT       = "+${countryCode} (0)${areaCode} ${extension}";
	/** +49 610 12345678 */
	public static final String INTERNATIONAL_FORMAT_PLAIN = "+${countryCode} ${areaCode} ${extension}";
	/** 0610 12345678 */
	public static final String NATIONAL_FORMAT            = "0${areaCode} ${extension}";
	
	private Builder<Integer> countryCodes;
	private Builder<Integer> areaCodes;
	private Builder<Integer> numbers;
	private String           format;
	private String           last;
	
	/**
	 * Constructor.
	 */
	public RandomPhoneBuilder() {
		this.countryCodes = BuilderUtils.$Int().withRandom().withStart(1).withEnd(99);
		this.areaCodes    = BuilderUtils.$Int().withRandom().withStart(1).withEnd(999);
		this.numbers      = BuilderUtils.$Int().withRandom().withStart(1000000).withEnd(9999999);
		this.format       = INTERNATIONAL_FORMAT;
	}

	/**
	 * Sets the format of the number to be produced.
	 * <p>The string can be any with these placeholders:
	 * <ul>
	 * <li>${countryCode} - the country code, e.g. 49 (without plus or prefix)
	 * <li>${areaCode} - the are code, e.g. 610 (without prefix, e.g. leading 0)
	 * <li>${extension} - the extension number, e.g. 1234567
	 * </ul>
	 * @param format format to be used
	 * @return the builder for chaining
	 */
	public RandomPhoneBuilder withFormat(String format) {
		this.format = format;
		return this;
	}
	
	/**
	 * Returns the format used.
	 * @return the format
	 * @see #withFormat(String)
	 */
	public String format() {
		return format;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String build() {
		String rc = format();
		if (CommonUtils.isEmpty(rc)) rc = INTERNATIONAL_FORMAT;
		rc = rc
			.replace("${countryCode}", ""+countryCodes.build())
			.replace("${areaCode}",    ""+areaCodes.build())
			.replace("${extension}",   ""+numbers.build());
		
		last = rc;
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String last() {
		return last;
	}

	
}
