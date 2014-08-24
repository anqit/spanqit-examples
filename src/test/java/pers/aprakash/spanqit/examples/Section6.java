package pers.aprakash.spanqit.examples;

import org.junit.Test;

import pers.aprakash.spanqit.constraint.Expressions;
import pers.aprakash.spanqit.core.Prefix;
import pers.aprakash.spanqit.core.Spanqit;
import pers.aprakash.spanqit.core.Variable;
import pers.aprakash.spanqit.graphpattern.GraphPatternNotTriple;
import pers.aprakash.spanqit.graphpattern.GraphPatterns;
import pers.aprakash.spanqit.graphpattern.TriplePattern;

public class Section6 extends BaseExamples {
	@Test
	public void example_6_1() {
		Variable name = Spanqit.var("name"), mbox = Spanqit.var("mbox");
		Variable x = query.var();
		Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS));
		
		GraphPatternNotTriple where = GraphPatterns.and(
				x.has(foaf.iri("name"), name),
				GraphPatterns.optional(x.has(foaf.iri("mbox"), mbox)));

		query.prefix(foaf).select(name, mbox).where(where);
		p();
	}

	@Test
	public void example_6_2() {
		Prefix dc = Spanqit.prefix("dc", iri(DC_NS)),
			   ns = Spanqit.prefix("ns", iri(EXAMPLE_ORG_NS));
		Variable title = Spanqit.var("title"), price = Spanqit
				.var("price"), x = Spanqit.var("x");

		GraphPatternNotTriple pricePattern = GraphPatterns
				.and(x.has(ns.iri("price"), price))
				.filter(Expressions.lt(price, 30)).optional();

		query.prefix(dc, ns).select(title, price)
				.where(x.has(dc.iri("title"), title), pricePattern);
		p();
	}

	@Test
	public void example_6_3() {
		Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS));
		Variable name = Spanqit.var("name"), mbox = Spanqit.var("mbox"), hpage = Spanqit
				.var("hpage");
		Variable x = query.var();

		TriplePattern namePattern = x.has(iri(FOAF_NS, "name"), name);

		query.prefix(foaf)
				.select(name, mbox, hpage)
				.where(namePattern,
						GraphPatterns.and(x.has(foaf.iri("mbox"), mbox))
								.optional(),
						GraphPatterns.and(
								x.has(foaf.iri("homepage"), hpage))
								.optional());
		p();
	}
}