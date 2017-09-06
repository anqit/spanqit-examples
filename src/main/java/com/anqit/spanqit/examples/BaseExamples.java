package com.anqit.spanqit.examples;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import com.anqit.spanqit.core.Queries;
import com.anqit.spanqit.core.QueryElement;
import com.anqit.spanqit.core.SelectQuery;

/**
 * The classes inheriting from this pose as examples on how to use Spanqit.
 * They follow the SPARQL 1.1 Spec linked below. Each class covers a section
 * of the spec, documenting how to create the example SPARQL queries in each
 * section using Spanqit.
 *  
 * @author Ankit
 * @see <a href="http://www.w3.org/TR/2013/REC-sparql11-query-20130321/"> The
 *      referenced SPARQL 1.1 Spec</a>
 */

public class BaseExamples {
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

	protected void p() {
		p(query);
	}

	protected void p(QueryElement qe) {
		p(qe.getQueryString());
	}

	protected void p(String s) {
		System.out.println(s);
	}

	protected void resetQuery() {
		query = Queries.SELECT();
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