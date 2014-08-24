package pers.aprakash.spanqit.examples;

import org.junit.Test;

import pers.aprakash.spanqit.constraint.Expression;
import pers.aprakash.spanqit.constraint.ExpressionOperand;
import pers.aprakash.spanqit.constraint.ExpressionOperands;
import pers.aprakash.spanqit.constraint.Expressions;
import pers.aprakash.spanqit.core.Assignment;
import pers.aprakash.spanqit.core.Prefix;
import pers.aprakash.spanqit.core.Projection;
import pers.aprakash.spanqit.core.Spanqit;
import pers.aprakash.spanqit.core.Variable;

public class Section16 extends BaseExamples {
	@Test
	public void example_16_1_2() {
		Prefix dc = Spanqit.prefix("dc", iri(DC_NS));
		Prefix ns = Spanqit.prefix("ns", iri(EXAMPLE_ORG_NS));
		Variable title = query.var(), p = query.var(), discount = query.var(),
				price = query.var(), x = query.var();
		ExpressionOperand one = ExpressionOperands.numberOperand(1);
		
		// TODO: fix parentheses
		Assignment discountedPrice = Expressions.multiply(
				p, Expressions.subtract(one, discount)).as(price);
		
		query.prefix(dc, ns).select(title, discountedPrice)
			.where(x.has(ns.iri("price"), p),
				   x.has(dc.iri("title"), title),
				   x.has(ns.iri("discount"), discount));
		p(); // again, almost...
		
		Variable fullPrice = query.var(), customerPrice = query.var();
		Expression<?> cPrice = Expressions.multiply(fullPrice, Expressions.subtract(one, discount));
		Projection newProjection = Spanqit.select(title, p.as(fullPrice), cPrice.as(customerPrice));
		
		// similar to other elements, calling select() with a Projection instance
		// (rather than Projectable instances) replaces (rather than augments)
		// the query's projections
		query.select(newProjection);
		p();
	}
}