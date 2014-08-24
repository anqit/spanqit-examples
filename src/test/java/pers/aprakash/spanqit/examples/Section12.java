package pers.aprakash.spanqit.examples;

import org.junit.Test;

import pers.aprakash.spanqit.constraint.Expressions;
import pers.aprakash.spanqit.core.Prefix;
import pers.aprakash.spanqit.core.Spanqit;
import pers.aprakash.spanqit.core.Variable;
import pers.aprakash.spanqit.graphpattern.GraphPatterns;
import pers.aprakash.spanqit.graphpattern.SubSelect;

public class Section12 extends BaseExamples {
	@Test
	public void example_12() {
		Prefix base = Spanqit.prefix(iri("http://people.example/"));
		
		// using this method of variable creation, as ?y and ?minName will be 
		// used in both the outer and inner queries
		Variable y = Spanqit.var("y"), minName = Spanqit.var("minName");
	
		SubSelect sub = GraphPatterns.select();
		Variable name = sub.var();
		sub.select(y, Expressions.min(name).as(minName)).where(y.has(base.iri("name"), name)).groupBy(y);
		
		query.prefix(base).select(y, minName).where(base.iri("alice").has(base.iri("knows"), y), sub);
		p();
	}
}