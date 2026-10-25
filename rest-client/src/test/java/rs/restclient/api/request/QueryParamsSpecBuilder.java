package rs.restclient.api.request;

import rs.baselib.test.AbstractBuilder;
import rs.baselib.test.Builder;
import rs.baselib.test.BuilderUtils;
import rs.restclient.core.api.request.QueryParamsSpec;

/**
 * Test Builder
 * @author ralph
 *
 */
public class QueryParamsSpecBuilder extends AbstractBuilder<QueryParamsSpec> {

	private Builder<String>  names;
	private Builder<String>  values;
	private Builder<Integer> valueCounts;
	
	public QueryParamsSpecBuilder() {
		names  = BuilderUtils.$RandomString().withLength(15);
		values = BuilderUtils.$RandomString().withLength(15);
		valueCounts = BuilderUtils.$Int().withStart(1).withEnd(4).withRandom();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected QueryParamsSpec _build() {
		QueryParamsSpec rc = new QueryParamsSpec();
		for (int i=0; i<10; i++) {
			String name  = names.build();
			int    count = valueCounts.build();
			for (int j=0; j<count; j++) {
				rc.add(name, values.build());
			}
		}
		return rc;
	}
	
	
}
