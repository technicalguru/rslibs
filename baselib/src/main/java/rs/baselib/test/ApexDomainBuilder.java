package rs.baselib.test;

import java.util.Set;

import rs.baselib.type.Country;
import rs.baselib.util.CommonUtils;

/**
 * Creates APEX names in (.com, .de, .net, .org)
 * @author ralph
 *
 */
public class ApexDomainBuilder extends AbstractBuilder<String> {

	private Builder<String> domains;
	private Builder<String> tlds;
	
	public ApexDomainBuilder() {
		this.domains = BuilderUtils.$RandomString().withChars(CommonUtils.ALPHA_LOWER_CHARS).withLength(15);
		Set<String> tlds = CommonUtils.newSet(".net", ".org", ".info", ".biz", ".int", ".edu", ".aero", ".name", ".eu", ".asia", ".travel", ".online");
		for (Country country : Country.values()) {
			if (country.getTld() != null) tlds.add(country.getTld());
		}
		this.tlds    = new RandomSelectBuilder<String>().withValues(tlds.toArray(new String[tlds.size()]));
	}

	/**
	 * Use this builder for domain names.
	 * @param domains domain names
	 * @return this builder for chaining
	 */
	public ApexDomainBuilder withDomains(Builder<String> domains) {
		this.domains = domains;
		return this;
	}
	
	/**
	 * Use this builder for TLDs.
	 * @param tlds Builder for TLDs
	 * @return this builder for chaining
	 */
	public ApexDomainBuilder withTlds(Builder<String> tlds) {
		this.tlds = tlds;
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String _build() {
		String rc = domains.build();
		String tld = tlds.build();
		return tld.startsWith(".") ? rc+tld : rc+"."+tld;
	}
	
	
}
