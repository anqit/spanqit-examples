package pers.aprakash.spanqit.test.graphpattern;

import static pers.aprakash.spanqit.constraint.Expressions.not;
import static pers.aprakash.spanqit.core.Elements.var;

import org.junit.Test;

import pers.aprakash.spanqit.constraint.Expressions;
import pers.aprakash.spanqit.core.Elements;
import pers.aprakash.spanqit.core.QueryPattern;
import pers.aprakash.spanqit.core.SparqlVariable;
import pers.aprakash.spanqit.graphpattern.GraphPatternNotTriple;
import pers.aprakash.spanqit.graphpattern.GraphPatterns;
import pers.aprakash.spanqit.graphpattern.SubSelect;
import pers.aprakash.spanqit.rdf.IRI;
import pers.aprakash.spanqit.test.BaseSpanqitTest;

public class GraphPatternTest extends BaseSpanqitTest {
	SparqlVariable name = var("name");
	SparqlVariable address = var("address");
	SparqlVariable name2 = var("name2");
	SparqlVariable name3 = var("name3");
	IRI hasAddress = uri(namespace, "hasAddress");

	@Test
	public void testFilterOnAGP() {
		GraphPatternNotTriple agp = GraphPatterns.union(
				GraphPatterns.tp(name, hasAddress, address));
		p("SINGLE:");
		p(agp);
		
		agp.union(GraphPatterns.tp(name2, hasAddress, address));
		p("\nMULTIPLE:");
		p(agp);

		agp.filter(not(name2));
		p("\nFILTER:");
		p(agp);

		agp.optional(true);
		p("\nOPTIONAL:");
		p(agp);

		GraphPatternNotTriple u2 = GraphPatterns.union(GraphPatterns.tp(name,
				hasAddress, name3));
		p("\nU2:");
		p(u2);

		p("\nUNIONS:");
		p(agp.union(u2));
		p(agp.union());
	}

	@Test
	public void emptyUnion() {
		p(GraphPatterns.union());
	}
	
	@Test
	public void ggp() {
		GraphPatternNotTriple and1 = GraphPatterns.and(GraphPatterns.tp(name, address, name2), GraphPatterns.tp(name2, address, name3));
		GraphPatternNotTriple and2 = GraphPatterns.and(GraphPatterns.tp(name, address, name2), GraphPatterns.tp(name2, address, name3));
//
//		p(GraphPatterns.and(and1, and2));
		
		GraphPatternNotTriple patternA = GraphPatterns.union().optional();
		
		QueryPattern where = Elements.where();
		where.where(patternA);
		
		p(where);
		
		patternA.union().filter(Expressions.bnode());
		p(where);
		patternA.union(and1.optional());
		p(where);
		patternA.union(and2);
		p(where);
		
		where = Elements.where();
		GraphPatternNotTriple patternB = GraphPatterns.and(and1, and2);
		p(where.where(patternB));
		
		where = Elements.where();
		GraphPatternNotTriple patternC = GraphPatterns.and(GraphPatterns.tp(name, address, name2)); 
		p(where.where(patternC));
	}

	@Test
	public void subSelect() {
		SubSelect sub = GraphPatterns.select();
		p(sub);
	}
}