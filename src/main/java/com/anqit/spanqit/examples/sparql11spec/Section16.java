package com.anqit.spanqit.examples.sparql11spec;

import org.junit.Test;

import com.anqit.spanqit.constraint.Expression;
import com.anqit.spanqit.constraint.ExpressionOperand;
import com.anqit.spanqit.constraint.Expressions;
import com.anqit.spanqit.core.Assignment;
import com.anqit.spanqit.core.Prefix;
import com.anqit.spanqit.core.Projection;
import com.anqit.spanqit.core.Spanqit;
import com.anqit.spanqit.core.Variable;
import com.anqit.spanqit.examples.BaseExamples;
import com.anqit.spanqit.rdf.RdfLiteral;

import static com.anqit.spanqit.rdf.Rdf.iri;

public class Section16 extends BaseExamples {
	@Test
	public void example_16_1_2() {
		Prefix dc = Spanqit.prefix("dc", iri(DC_NS));
		Prefix ns = Spanqit.prefix("ns", iri(EXAMPLE_ORG_NS));
		Variable title = query.var(), p = query.var(), discount = query.var(), price = query
				.var(), x = query.var();
		ExpressionOperand one = RdfLiteral.of(1);

		// TODO: fix parentheses
		Assignment discountedPrice = Expressions.multiply(p,
				Expressions.subtract(one, discount)).as(price);

		query.prefix(dc, ns)
				.select(title, discountedPrice)
				.where(x.has(ns.iri("price"), p),
						x.has(dc.iri("title"), title),
						x.has(ns.iri("discount"), discount));
		p(); // again, almost...

		Variable fullPrice = query.var(), customerPrice = query.var();
		Expression<?> cPrice = Expressions.multiply(fullPrice,
				Expressions.subtract(one, discount));
		Projection newProjection = Spanqit.select(title, p.as(fullPrice),
				cPrice.as(customerPrice));

		// similar to other elements, calling select() with a Projection
		// instance
		// (rather than Projectable instances) replaces (rather than augments)
		// the query's projections
		query.select(newProjection);
		p();
	}
}