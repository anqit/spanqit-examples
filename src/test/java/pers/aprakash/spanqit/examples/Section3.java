package pers.aprakash.spanqit.examples;

import org.junit.Test;

import pers.aprakash.spanqit.constraint.Expression;
import pers.aprakash.spanqit.constraint.Expressions;
import pers.aprakash.spanqit.core.Prefix;
import pers.aprakash.spanqit.core.Spanqit;
import pers.aprakash.spanqit.core.Variable;
import pers.aprakash.spanqit.graphpattern.GraphPattern;
import pers.aprakash.spanqit.graphpattern.GraphPatternNotTriple;
import pers.aprakash.spanqit.graphpattern.GraphPatterns;
import pers.aprakash.spanqit.graphpattern.TriplePattern;

public class Section3 extends BaseExamples {
	@Test
	public void example_3_1() {
		Prefix dc = Spanqit.prefix("dc", iri(DC_NS));
		
		Variable x = query.var(), title = Spanqit.var("title");
		TriplePattern xTitle = GraphPatterns.tp(x, dc.iri("title"), title);
		
		Expression<?> regex = Expressions.regex(title, literal("^SPARQL"));
		GraphPatternNotTriple where = GraphPatterns.and(xTitle).filter(regex);

		query.prefix(dc).select(title).where(where);
		p();

		where.filter(Expressions.regex(title, "^SPARQL", "i"));
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