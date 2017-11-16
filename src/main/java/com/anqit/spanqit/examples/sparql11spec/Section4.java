package com.anqit.spanqit.examples.sparql11spec;

import static com.anqit.spanqit.rdf.Rdf.iri;

import org.junit.Test;

import com.anqit.spanqit.core.Prefix;
import com.anqit.spanqit.core.Spanqit;
import com.anqit.spanqit.core.Variable;
import com.anqit.spanqit.examples.BaseExamples;
import com.anqit.spanqit.graphpattern.GraphPatterns;
import com.anqit.spanqit.graphpattern.TriplePattern;
import com.anqit.spanqit.rdf.Iri;
import com.anqit.spanqit.rdf.RdfBlankNode;
import com.anqit.spanqit.rdf.RdfBlankNode.PropertiesBlankNode;
import com.anqit.spanqit.rdf.RdfLiteral;
import com.anqit.spanqit.rdf.RdfPredicate;
import com.anqit.spanqit.rdf.RdfLiteral.StringLiteral;

public class Section4 extends BaseExamples {
	Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS));
	Variable x = Spanqit.var("x"), name = Spanqit.var("name");

	@Test
	public void example_4_1_4() {
		Prefix defPrefix = Spanqit.prefix(iri(DC_NS));
		
		// [ :p "v" ] .
		PropertiesBlankNode bnode = RdfBlankNode.bNode(defPrefix.iri("p"), RdfLiteral.of("v"));
		p(bnode.toTp());
		
		// [] :p "v" .
		TriplePattern tp = RdfBlankNode.bNode().has(defPrefix.iri("p"), RdfLiteral.of("v"));
		p(tp);
		
		//	[ :p "v" ] :q "w" .
		tp = bnode.has(defPrefix.iri("q"), RdfLiteral.of("w"));
		p(tp);

		// :x :q [ :p "v" ] .
		tp = defPrefix.iri("x").has(defPrefix.iri("q"), bnode);
		p(tp);
		
		// [ foaf:name  ?name ;
		//	 foaf:mbox  <mailto:alice@example.org> ]
		bnode = RdfBlankNode.bNode(foaf.iri("name"), name).andHas(foaf.iri("mbox"), iri("mailto:alice@example.org"));
		p(bnode);
	}

	@Test
	public void example_4_2_1() {
		Variable mbox = Spanqit.var("mbox");
		
		TriplePattern tp = GraphPatterns.tp(x, foaf.iri("name"), name).andHas(foaf.iri("mbox"), mbox);
		p(tp);
	}

	@Test
	public void example_4_2_2() {
		Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS));
		Variable x = Spanqit.var("x"),
				name = Spanqit.var("name");
		Iri nick = foaf.iri("nick");
		StringLiteral aliceNick = RdfLiteral.of("Alice"),
				alice_Nick = RdfLiteral.of("Alice_");
		
		TriplePattern tp = GraphPatterns.tp(x, nick, alice_Nick, aliceNick);
		p(tp);
		
		tp = x.has(nick, aliceNick, alice_Nick).andHas(foaf.iri("name"), name);
		p(tp);
	}
	
	@Test
	public void example_4_2_4() {
		Prefix defPrefix = Spanqit.prefix(iri(DC_NS));

		// isA() is a shortcut method to create triples using the "a" keyword
		p(Spanqit.var("x").isA(defPrefix.iri("Class1")));

		// the isA predicate is a static member of RdfPredicate
		p(RdfBlankNode.bNode(RdfPredicate.isA, defPrefix.iri("appClass")).has(defPrefix.iri("p"), "v"));
	}
}
