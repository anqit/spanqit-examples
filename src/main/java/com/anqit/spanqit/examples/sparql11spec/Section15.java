package com.anqit.spanqit.examples.sparql11spec;

import org.junit.Test;

import com.anqit.spanqit.core.OrderBy;
import com.anqit.spanqit.core.OrderCondition;
import com.anqit.spanqit.core.Prefix;
import com.anqit.spanqit.core.PrefixDeclarations;
import com.anqit.spanqit.core.Spanqit;
import com.anqit.spanqit.core.Variable;
import com.anqit.spanqit.examples.BaseExamples;

import static com.anqit.spanqit.rdf.Rdf.iri;

public class Section15 extends BaseExamples {
	@Test
	public void example_15_1() {
		Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS));
		Variable name = query.var(), x = query.var();
		
		query.prefix(foaf).select(name).where(x.has(foaf.iri("name"), name)).orderBy(name);
		p();
		
		Prefix base = Spanqit.prefix(iri("http://example.org/ns#"));
		PrefixDeclarations prefixes = Spanqit.prefixes(foaf, base);
		Variable emp = query.var();
		
		OrderCondition empDesc = Spanqit.desc(emp);
		
		// calling prefix() with a PrefixDeclarations instance (rather than
		// Prefix objects) replaces (rather than augments) the query's
		// prefixes
		query.prefix(prefixes)
			// we can still add graph patterns to the query pattern
			.where(x.has(base.iri("empId"), emp))
			// similarly, calling orderBy() with an OrderBy instance (rather
			// than Orderable instances) replaces (rather than augments)
			// the query's order conditions
			.orderBy(Spanqit.orderBy(empDesc));
		p();
		
		OrderBy order = Spanqit.orderBy(name, empDesc);
		query.orderBy(order);
		p();
	}
	
	@Test
	public void example_15_3_1() {
		Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS));
		Variable name = query.var(), x = query.var();
		
		query.prefix(foaf).select(name).distinct().where(x.has(foaf.iri("name"), name));
		p();
	}
	
	@Test
	public void example_15_3_2() {
		p("REDUCED not yet implemented");
	}
	
	@Test
	public void example_15_4() {
		Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS));
		Variable name = query.var(), x = query.var();
		
		query.prefix(foaf).select(name).where(x.has(foaf.iri("name"), name))
			.orderBy(name)
			.limit(5)
			.offset(10);
		p();
	}
	
	@Test
	public void example_15_5() {
		Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS));
		Variable name = query.var(), x = query.var();
		
		query.prefix(foaf).select(name).where(x.has(foaf.iri("name"), name))
			.limit(20);
		p();
	}
}