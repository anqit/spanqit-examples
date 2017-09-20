package com.anqit.spanqit.examples.sparql11spec;

import org.junit.Test;

import com.anqit.spanqit.constraint.Expressions;
import com.anqit.spanqit.core.Prefix;
import com.anqit.spanqit.core.QueryPattern;
import com.anqit.spanqit.core.Spanqit;
import com.anqit.spanqit.core.Variable;
import com.anqit.spanqit.examples.BaseExamples;
import com.anqit.spanqit.graphpattern.GraphPattern;
import com.anqit.spanqit.graphpattern.GraphPatterns;

import static pers.aprakash.spanqit.rdf.adapter.OpenRdfAdapter.*;

public class Section5 extends BaseExamples {
	@Test
	public void example_5_2() {
		Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS));
		Variable name = Spanqit.var("name"), mbox = Spanqit.var("mbox");
		Variable x = query.var();

		query.prefix(foaf)
				.select(name, mbox)
				.where(x.has(foaf.iri("name"), name),
						x.has(foaf.iri("mbox"), mbox));
		p();

		GraphPattern namePattern = GraphPatterns.and(x.has(
				foaf.iri("name"), name));
		GraphPattern mboxPattern = GraphPatterns.and(x.has(
				foaf.iri("mbox"), mbox));
		QueryPattern where = Spanqit.where(GraphPatterns.and(namePattern,
				mboxPattern));
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
		Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS));
		Variable x = Spanqit.var("x"), name = Spanqit.var("name"), mbox = Spanqit
				.var("mbox");

		p(GraphPatterns.and(x.has(foaf.iri("name"), name),
				x.has(foaf.iri("mbox"), mbox)));
		p(GraphPatterns.and(x.has(foaf.iri("name"), name),
				x.has(foaf.iri("mbox"), mbox)).filter(
				Expressions.regex(name, "Smith")));
		p(GraphPatterns.and(x.has(foaf.iri("name"), name),
				GraphPatterns.and(), x.has(foaf.iri("mbox"), mbox)));
	}
}