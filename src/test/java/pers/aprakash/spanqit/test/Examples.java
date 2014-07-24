package pers.aprakash.spanqit.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import pers.aprakash.spanqit.constraint.Expression;
import pers.aprakash.spanqit.constraint.Expressions;
import pers.aprakash.spanqit.core.Assignment;
import pers.aprakash.spanqit.core.ConstructQuery;
import pers.aprakash.spanqit.core.Elements;
import pers.aprakash.spanqit.core.Prefix;
import pers.aprakash.spanqit.core.PrefixDeclarations;
import pers.aprakash.spanqit.core.Queries;
import pers.aprakash.spanqit.core.QueryPattern;
import pers.aprakash.spanqit.core.SelectQuery;
import pers.aprakash.spanqit.core.SparqlVariable;
import pers.aprakash.spanqit.graphpattern.GraphPattern;
import pers.aprakash.spanqit.graphpattern.GraphPatternNotTriple;
import pers.aprakash.spanqit.graphpattern.GraphPatterns;
import pers.aprakash.spanqit.graphpattern.TriplePattern;

/**
 * 
 * @author Ankit
 * @see <a href="http://www.w3.org/TR/2013/REC-sparql11-query-20130321/"> The
 *      referenced SPARQL 1.1 Spec</a>
 */

public class Examples extends BaseSpanqitTest {
	static final String EXAMPLE_COM_NS = "https://example.com/ns#";
	static final String EXAMPLE_BOOK_NS = "http://example.org/book/";
	static final String EXAMPLE_DATATYPE_NS = "http://example.org/datatype#";
	static final String PURL_NS = "http://purl.org/dc/elements/1.1/";
	static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";

	SelectQuery query;

	@Rule
	public TestName name = new TestName();

	@Before
	public void before() {
		resetQuery();
		printTestHeader();
	}

	@Test
	public void example_2_1() {
		query = Queries.SELECT();
		SparqlVariable title = Elements.var("title");

		TriplePattern book1_has_title = GraphPatterns.tp(
				iri(EXAMPLE_BOOK_NS, "book1"), iri(PURL_NS, "title"), title);

		query.select(title).where(book1_has_title);

		p();
	}

	@Test
	public void example_2_2() {
		Prefix foaf = Elements.prefix("foaf", iri(FOAF_NS));

		/**
		 * As a shortcut, Query objects can create variables that will be unique
		 * to the query instance.
		 */
		SparqlVariable name = query.var(), mbox = query.var(), x = query.var();

		TriplePattern x_hasFoafName_name = GraphPatterns.tp(x,
				iri(FOAF_NS, "name"), name);
		TriplePattern x_hasFoafMbox_mbox = GraphPatterns.tp(x,
				iri(FOAF_NS, "mbox"), mbox);

		query.prefix(foaf).select(name, mbox)
				.where(x_hasFoafName_name, x_hasFoafMbox_mbox);

		p();
	}

	@Test
	public void example_2_3() {
		SparqlVariable v = query.var(), p = query.var();

		TriplePattern v_hasP_cat = GraphPatterns.tp(v, p, literal("cat"));

		query.select(v).where(v_hasP_cat);
		p();

		SelectQuery queryWithLangTag = Queries.SELECT();
		TriplePattern v_hasP_cat_en = GraphPatterns.tp(v, p,
				literalWithLangTag("cat", "en"));
		queryWithLangTag.select(v).where(v_hasP_cat_en);
		p(queryWithLangTag);
	}

	@Test
	public void example_2_3_2() {
		SparqlVariable v = query.var(), p = query.var();

		TriplePattern v_hasP_42 = GraphPatterns.tp(v, p, literal(42));

		query.select(v).where(v_hasP_42);
		p();
	}

	@Test
	public void example_2_3_3() {
		String datatype = "specialDatatype";
		SparqlVariable v = query.var(), p = query.var();
		TriplePattern v_hasP_abc_dt = GraphPatterns.tp(v, p,
				literalWithDatatype("abc", EXAMPLE_DATATYPE_NS, datatype));

		query.select(v).where(v_hasP_abc_dt);
		p();
	}

	@Test
	public void example_2_5() {
		// except property paths...

		Prefix foaf = Elements.prefix("foaf", iri(FOAF_NS));
		SparqlVariable G = Elements.var("G"), P = Elements.var("P"), S = Elements
				.var("S"), name = Elements.var("name");

		Assignment concatAsName = Elements.as(
				Expressions.concat(G, literal(" "), S), name);

		query.prefix(foaf)
				.select(concatAsName)
				.where(GraphPatterns.tp(P, iri(FOAF_NS, "givenName"), G),
						GraphPatterns.tp(P, iri(FOAF_NS, "surname"), S));
		p();

		// TODO add BIND() capability in graph patterns (also show example of
		// saving PrefixDeclarations object and using it in both queries
		p("Missing BIND capability right now");
	}

	@Test
	public void example_2_6() {
		Prefix foaf = Elements.prefix("foaf", iri(FOAF_NS)), org = Elements
				.prefix("org", iri(EXAMPLE_COM_NS));
		PrefixDeclarations prefixes = Elements.prefixes(foaf, org);

		ConstructQuery graphQuery = Queries.CONSTRUCT();
		SparqlVariable x = graphQuery.var(), name = Elements.var("name");

		TriplePattern foafName = GraphPatterns
				.tp(x, iri(FOAF_NS, "name"), name);
		TriplePattern orgName = GraphPatterns.tp(x,
				iri(EXAMPLE_COM_NS, "employeeName"), name);

		graphQuery.prefix(prefixes).construct(foafName).where(orgName);
		p(graphQuery);
	}

	@Test
	public void example_3_1() {
		Prefix dc = Elements.prefix("dc", iri(PURL_NS));
		SparqlVariable x = query.var(), title = Elements.var("title");
		TriplePattern xTitle = GraphPatterns
				.tp(x, iri(PURL_NS, "title"), title);
		Expression<?> regex = Expressions.regex(title, literal("^SPARQL"));

		GraphPatternNotTriple where = GraphPatterns.and(xTitle).filter(regex);

		query.prefix(dc).select(title).where(where);
		p();

		where.filter(Expressions.regex(title, "^SPARQL", "i"));
		p();
	}

	@Test
	public void example_3_2() {
		Prefix dc = Elements.prefix("dc", iri(PURL_NS)), ns = Elements.prefix(
				"ns", iri(EXAMPLE_COM_NS));
		SparqlVariable title = Elements.var("title"), price = Elements
				.var("price");
		SparqlVariable x = query.var();
		Expression<?> priceConstraint = Expressions.lt(price, 30.5);

		GraphPattern where = GraphPatterns.and(
				x.has(iri(EXAMPLE_COM_NS, "price"), price),
				x.has(iri(PURL_NS, "title"), title)).filter(priceConstraint);
		
		query.prefix(dc, ns).select(title, price).where(where);
		p();
	}

	@Test
	public void example_5_2() {
		Prefix foaf = Elements.prefix("foaf", iri(FOAF_NS));
		SparqlVariable name = Elements.var("name"), mbox = Elements.var("mbox");
		SparqlVariable x = query.var();
		
		query.prefix(foaf).select(name, mbox).where(x.has(iri(FOAF_NS, "name"), name), x.has(iri(FOAF_NS, "mbox"), mbox));
		p();
		
		GraphPattern namePattern = GraphPatterns.and(x.has(iri(FOAF_NS, "name"), name));
		GraphPattern mboxPattern = GraphPatterns.and(x.has(iri(FOAF_NS, "mbox"), mbox));
		QueryPattern where = Elements.where(GraphPatterns.and(namePattern, mboxPattern));
		query.where(where);
		p();		
	}
	
	@Test
	public void example_5_2_1() {
		p(GraphPatterns.and());
		
		query.select(query.var());
		p();
	}
	
	@Test
	public void example_5_2_3() {
		SparqlVariable x = Elements.var("x"), name = Elements.var("name"), mbox = Elements.var("mbox");
		p(GraphPatterns.and(x.has(iri(FOAF_NS, "name"), name), x.has(iri(FOAF_NS, "mbox"), mbox)));
		p(GraphPatterns.and(x.has(iri(FOAF_NS, "name"), name), x.has(iri(FOAF_NS, "mbox"), mbox)).filter(Expressions.regex(name, "Smith")));
		p(GraphPatterns.and(x.has(iri(FOAF_NS, "name"), name), x.has(iri(FOAF_NS, "mbox"), mbox), GraphPatterns.and()));
	}
	
	private void resetQuery() {
		query = Queries.SELECT();
	}

	private void p() {
		p(query);
	}

	private void printTestHeader() {
		String testName = name.getMethodName();
		String[] tokens = testName.split("_");

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