package pers.aprakash.spanqit.examples;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import pers.aprakash.spanqit.core.Queries;
import pers.aprakash.spanqit.core.SelectQuery;
import pers.aprakash.spanqit.test.BaseSpanqitTest;

/**
 * 
 * @author Ankit
 * @see <a href="http://www.w3.org/TR/2013/REC-sparql11-query-20130321/"> The
 *      referenced SPARQL 1.1 Spec</a>
 */

public class BaseExamples extends BaseSpanqitTest {
	protected static final String EXAMPLE_COM_NS = "https://example.com/ns#";
	protected static final String EXAMPLE_ORG_NS = "https://example.org/ns#";
	protected static final String EXAMPLE_ORG_BOOK_NS = "http://example.org/book/";
	protected static final String EXAMPLE_DATATYPE_NS = "http://example.org/datatype#";
	protected static final String DC_NS = "http://purl.org/dc/elements/1.1/";
	protected static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";

	protected SelectQuery query;

	@Rule
	public TestName testName = new TestName();

	@Before
	public void before() {
		resetQuery();
		printTestHeader();
	}

	protected void resetQuery() {
		query = Queries.SELECT();
	}

	protected void p() {
		p(query);
	}

	private void printTestHeader() {
		String name = testName.getMethodName();
		String[] tokens = name.split("_");

		StringBuilder sb = new StringBuilder();
		sb.append(tokens[0].toUpperCase()).append(" ");

		boolean first = true;
		for (int i = 1; i < tokens.length; i++) {
			if (!first) {
				sb.append('.');
			}
			sb.append(tokens[i]);
			first = false;
		}

		sb.append(":");
		p(sb.toString());
	}
}