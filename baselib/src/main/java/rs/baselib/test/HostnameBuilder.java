package rs.baselib.test;

import rs.baselib.util.CommonUtils;

/**
 * Creates APEX names in (.com, .de, .net, .org)
 * @author ralph
 *
 */
public class HostnameBuilder extends AbstractBuilder<String> {

	private Builder<String> apexDomains;
	private Builder<String> simpleHostnames;
	
	public HostnameBuilder() {
		this.apexDomains     = BuilderUtils.$ApexDomain();
		this.simpleHostnames = BuilderUtils.$RandomString().withChars(CommonUtils.ALPHA_LOWER_CHARS).withLength(15);
	}

	/**
	 * Use this builder for the apex domains.
	 * @param apexDomains apex domain names
	 * @return this builder for chaining
	 */
	public HostnameBuilder withApexDomains(Builder<String> apexDomains) {
		this.apexDomains = apexDomains;
		return this;
	}
	
	/**
	 * Use this builder for TLDs.
	 * @param simpleHostnames Builder for TLDs
	 * @return this builder for chaining
	 */
	public HostnameBuilder withSimpleHostnames(Builder<String> simpleHostnames) {
		this.simpleHostnames = simpleHostnames;
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String _build() {
		return simpleHostnames.build()+"."+apexDomains.build();
	}
	
	
}
