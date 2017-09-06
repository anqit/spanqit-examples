package com.anqit.spanqit.examples.sparql11spec;

import static com.anqit.spanqit.rdf.Rdf.iri;

import org.junit.Test;

import com.anqit.spanqit.constraint.Expression;
import com.anqit.spanqit.constraint.Expressions;
import com.anqit.spanqit.core.Prefix;
import com.anqit.spanqit.core.Spanqit;
import com.anqit.spanqit.core.Variable;
import com.anqit.spanqit.examples.BaseExamples;
import com.anqit.spanqit.graphpattern.GraphPattern;
import com.anqit.spanqit.graphpattern.GraphPatternNotTriple;
import com.anqit.spanqit.graphpattern.GraphPatterns;
import com.anqit.spanqit.graphpattern.TriplePattern;
import com.anqit.spanqit.rdf.Rdf;

public class Section3 extends BaseExamples {
	@Test
	public void example_3_1() {
		Prefix dc = Spanqit.prefix("dc", iri(DC_NS));
		
		Variable x = query.var(), title = Spanqit.var("title");
		TriplePattern xTitle = GraphPatterns.tp(x, dc.iri("title"), title);
		
		Expression<?> regex = Expressions.regex(title, Rdf.literalOf("^SPARQL"));
		GraphPatternNotTriple where = GraphPatterns.and(xTitle).filter(regex);

		query.prefix(dc).select(title).where(where);
		p();

		where.filter(Expressions.regex(title, "web", "i"));
		p();
	}

	@Test
	public void example_3_2() {
		Prefix dc = Spanqit.prefix("dc", iri(DC_NS)),
			   ns = Spanqit.prefix("ns", iri(EXAMPLE_COM_NS));
		
		Variable title = Spanqit.var("title"), price = Spanqit
				.var("price");
		Variable x = query.var();
		Expression<?> priceConstraint = Expressions.lt(price, 30.5);

		GraphPattern where = GraphPatterns.and(
				x.has(ns.iri("price"), price),
				x.has(dc.iri("title"), title)).filter(priceConstraint);

		query.prefix(dc, ns).select(title, price).where(where);
		p();
	}
}