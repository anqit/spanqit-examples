package com.anqit.spanqit.examples.sparql11spec;

import static com.anqit.spanqit.rdf.Rdf.iri;

import org.junit.Test;

import com.anqit.spanqit.constraint.Expressions;
import com.anqit.spanqit.core.Assignment;
import com.anqit.spanqit.core.Prefix;
import com.anqit.spanqit.core.PrefixDeclarations;
import com.anqit.spanqit.core.Spanqit;
import com.anqit.spanqit.core.Variable;
import com.anqit.spanqit.core.query.ConstructQuery;
import com.anqit.spanqit.core.query.Queries;
import com.anqit.spanqit.core.query.SelectQuery;
import com.anqit.spanqit.examples.BaseExamples;
import com.anqit.spanqit.graphpattern.GraphPatterns;
import com.anqit.spanqit.graphpattern.TriplePattern;
import com.anqit.spanqit.rdf.RdfLiteral;

public class Section2 extends BaseExamples {
	@Test
	public void example_2_1() {
		Variable title = Spanqit.var("title");

		TriplePattern book1_has_title = GraphPatterns.tp(iri(EXAMPLE_ORG_BOOK_NS, "book1"), iri(DC_NS, "title"), title);

		query.select(title).where(book1_has_title);

		p();
	}

	@Test
	public void example_2_2() {
		Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS));

		/**
		 * As a shortcut, Query objects can create variables that will be unique to the
		 * query instance.
		 */
		Variable name = query.var(), mbox = query.var(), x = query.var();

		TriplePattern x_hasFoafName_name = GraphPatterns.tp(x, foaf.iri("name"), name);
		TriplePattern x_hasFoafMbox_mbox = GraphPatterns.tp(x, foaf.iri("mbox"), mbox);

		query.prefix(foaf).select(name, mbox).where(x_hasFoafName_name, x_hasFoafMbox_mbox);

		p();
	}

	@Test
	public void example_2_3() {
		Variable v = query.var(), p = query.var();

		TriplePattern v_hasP_cat = GraphPatterns.tp(v, p, RdfLiteral.of("cat"));

		query.select(v).where(v_hasP_cat);
		p();

		SelectQuery queryWithLangTag = Queries.SELECT();
		TriplePattern v_hasP_cat_en = GraphPatterns.tp(v, p, RdfLiteral.ofLanguage("cat", "en"));
		queryWithLangTag.select(v).where(v_hasP_cat_en);
		p(queryWithLangTag);
	}

	@Test
	public void example_2_3_2() {
		Variable v = query.var(), p = query.var();

		TriplePattern v_hasP_42 = GraphPatterns.tp(v, p, RdfLiteral.of(42));

		query.select(v).where(v_hasP_42);
		p();
	}

	@Test
	public void example_2_3_3() {
		String datatype = "specialDatatype";
		Variable v = query.var(), p = query.var();
		TriplePattern v_hasP_abc_dt = GraphPatterns.tp(v, p, RdfLiteral.ofType("abc", iri(EXAMPLE_DATATYPE_NS, datatype)));

		query.select(v).where(v_hasP_abc_dt);
		p();
	}
	
	@Test
	public void example_2_4() {
		Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS));

		Variable x = query.var(), name = query.var();
		query.prefix(foaf).select(x, name).where(x.has(foaf.iri("name"), name));
		p();
	}

	@Test
	public void example_2_5() {
		Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS));
		Variable G = Spanqit.var("G"),
				P = Spanqit.var("P"), 
				S = Spanqit.var("S"),
				name = Spanqit.var("name");

		Assignment concatAsName = Spanqit.as(Expressions.concat(G, RdfLiteral.of(" "), S), name);

		query.prefix(foaf).select(concatAsName).where(
				GraphPatterns.tp(P, foaf.iri("givenName"), G).andHas(foaf.iri("surname"), S));
		p();

		// TODO add BIND() capability in graph patterns (also show example of
		// saving PrefixDeclarations object and using it in both queries)
		p("Missing BIND capability right now");
	}

	@Test
	public void example_2_6() {
		Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS)),
				org = Spanqit.prefix("org", iri(EXAMPLE_COM_NS));
		PrefixDeclarations prefixes = Spanqit.prefixes(foaf, org);

		ConstructQuery graphQuery = Queries.CONSTRUCT();
		Variable x = graphQuery.var(), name = Spanqit.var("name");

		TriplePattern foafName = GraphPatterns.tp(x, foaf.iri("name"), name);
		TriplePattern orgName = GraphPatterns.tp(x, org.iri("employeeName"), name);

		graphQuery.prefix(prefixes).construct(foafName).where(orgName);
		p(graphQuery);
	}
}