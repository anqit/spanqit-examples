package com.anqit.spanqit.examples.sparql11spec;

import org.junit.Test;

import com.anqit.spanqit.constraint.Expressions;
import com.anqit.spanqit.core.Prefix;
import com.anqit.spanqit.core.Spanqit;
import com.anqit.spanqit.core.Variable;
import com.anqit.spanqit.examples.BaseExamples;
import com.anqit.spanqit.graphpattern.GraphPatterns;
import com.anqit.spanqit.graphpattern.SubSelect;

import static pers.aprakash.spanqit.rdf.adapter.OpenRdfAdapter.*;

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