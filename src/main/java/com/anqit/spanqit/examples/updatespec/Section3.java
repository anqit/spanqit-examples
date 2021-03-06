package com.anqit.spanqit.examples.updatespec;

import static com.anqit.spanqit.graphpattern.GraphPatterns.and;
import static com.anqit.spanqit.rdf.Rdf.iri;

import org.junit.Test;

import com.anqit.spanqit.constraint.Expressions;
import com.anqit.spanqit.core.Prefix;
import com.anqit.spanqit.core.Spanqit;
import com.anqit.spanqit.core.Variable;
import com.anqit.spanqit.core.query.DeleteDataQuery;
import com.anqit.spanqit.core.query.InsertDataQuery;
import com.anqit.spanqit.core.query.ModifyQuery;
import com.anqit.spanqit.core.query.Queries;
import com.anqit.spanqit.examples.BaseExamples;
import com.anqit.spanqit.graphpattern.GraphPattern;
import com.anqit.spanqit.graphpattern.GraphPatterns;
import com.anqit.spanqit.graphpattern.TriplePattern;
import com.anqit.spanqit.rdf.Iri;
import com.anqit.spanqit.rdf.Rdf;

public class Section3 extends BaseExamples {
	Prefix dc = Spanqit.prefix("dc", iri("http://purl.org/dc/elements/1.1/"));
	Prefix ns = Spanqit.prefix("ns", iri(EXAMPLE_ORG_NS));
	Prefix foaf = Spanqit.prefix("foaf", iri("http://xmlns.com/foaf/0.1/"));
	Prefix xsd = Spanqit.prefix("xsd", iri("http://www.w3.org/2001/XMLSchema#"));
	Prefix rdf = Spanqit.prefix("rdf", iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#"));

	@Test
	public void example_1() {
		// PREFIX dc: <http://purl.org/dc/elements/1.1/>
		// INSERT DATA
		// {
		// <http://example/book1> dc:title "A new book" ;
		// dc:creator "A.N.Other" .
		// }

		InsertDataQuery insertDataQuery = Queries.INSERT_DATA();

		insertDataQuery.prefix(dc)
				.insertData(iri("http://example/book1").has(dc.iri("title"), Rdf.literalOf("A new book"))
						.andHas(dc.iri("creator"), Rdf.literalOf("A.N.Other")));

		p(insertDataQuery);
	}

	@Test
	public void example_2() {
		// PREFIX dc: <http://purl.org/dc/elements/1.1/>
		// PREFIX ns: <http://example.org/ns#>
		// INSERT DATA
		// { GRAPH <http://example/bookStore>
		// { <http://example/book1> ns:price 42 }
		// }

		InsertDataQuery insertDataQuery = Queries.INSERT_DATA();

		insertDataQuery.prefix(dc, ns).insertData(iri("http://example/book1").has(ns.iri("price"), Rdf.literalOf(42)))
				.into(iri("http://example/bookStore"));

		p(insertDataQuery);
	}

	@Test
	public void example_3() {
		// PREFIX dc: <http://purl.org/dc/elements/1.1/>
		//
		// DELETE DATA
		// {
		// <http://example/book2> dc:title "David Copperfield" ;
		// dc:creator "Edmund Wells" .
		// }

		DeleteDataQuery deleteDataQuery = Queries.DELETE_DATA().prefix(dc);

		deleteDataQuery.deleteData(iri("http://example/book2").has(dc.iri("title"), Rdf.literalOf("David Copperfield"))
				.andHas(dc.iri("creator"), Rdf.literalOf("Edmund Wells")));

		p(deleteDataQuery);
	}

	@Test
	public void example_4() {
		// PREFIX dc: <http://purl.org/dc/elements/1.1/>
		// DELETE DATA
		// { GRAPH <http://example/bookStore> { <http://example/book1> dc:title
		// "Fundamentals of Compiler Desing" } } ;
		//
		// PREFIX dc: <http://purl.org/dc/elements/1.1/>
		// INSERT DATA
		// { GRAPH <http://example/bookStore> { <http://example/book1> dc:title
		// "Fundamentals of Compiler Design" } }

		Iri bookStore = iri("http://example/bookStore"), exampleBook = iri("http://example/book1"),
				title = dc.iri("title");

		DeleteDataQuery deleteTypoQuery = Queries.DELETE_DATA().prefix(dc)
				.deleteData(exampleBook.has(title, "Fundamentals of Compiler Desing")).from(bookStore);
		InsertDataQuery insertFixedTitleQuery = Queries.INSERT_DATA().prefix(dc)
				.insertData(exampleBook.has(title, "Fundamentals of Compiler Design")).into(bookStore);
		p(deleteTypoQuery, insertFixedTitleQuery);
	}

	@Test
	public void example_with() {
		TriplePattern abc = GraphPatterns.tp(Spanqit.var("a"), Spanqit.var("b"), Spanqit.var("c"));
		TriplePattern xyz = GraphPatterns.tp(Spanqit.var("x"), Spanqit.var("y"), Spanqit.var("z"));
		ModifyQuery modify = Queries.MODIFY();
		Iri g1 = () -> "<g1>";
		GraphPattern examplePattern = () -> " ... ";

		// WITH <g1> DELETE { a b c } INSERT { x y z } WHERE { ... }
		modify.with(g1).delete(abc).insert(xyz).where(examplePattern);
		p("To illustrate the use of the WITH clause, an operation of the general form:");
		p(modify);

		// DELETE { GRAPH <g1> { a b c } } INSERT { GRAPH <g1> { x y z } } USING <g1>
		// WHERE { ... }
		modify.with(null).delete(abc).from(g1).insert(xyz).into(g1).using(g1).where(examplePattern);
		p("is considered equivalent to:");
		p(modify);
	}

	@Test
	public void example_5() {
		// PREFIX foaf: <http://xmlns.com/foaf/0.1/>
		//
		// WITH <http://example/addresses>
		// DELETE { ?person foaf:givenName 'Bill' }
		// INSERT { ?person foaf:givenName 'William' }
		// WHERE
		// { ?person foaf:givenName 'Bill'
		// }

		Variable person = Spanqit.var("person");
		ModifyQuery modify = Queries.MODIFY();

		modify.prefix(foaf).with(iri("http://example/addresses")).delete(person.has(foaf.iri("givenName"), "Bill"))
				.insert(person.has(foaf.iri("givenName"), "William")).where(person.has(foaf.iri("givenName"), "Bill"));

		p(modify);
	}

	@Test
	public void example_6() {
		// PREFIX dc: <http://purl.org/dc/elements/1.1/>
		// PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
		//
		// DELETE
		// { ?book ?p ?v }
		// WHERE
		// { ?book dc:date ?date .
		// FILTER ( ?date > "1970-01-01T00:00:00-02:00"^^xsd:dateTime )
		// ?book ?p ?v
		// }

		Variable book = Spanqit.var("book"), p = Spanqit.var("p"), v = Spanqit.var("v"), date = Spanqit.var("date");

		ModifyQuery modify = Queries.MODIFY();

		modify.prefix(dc, xsd).delete(book.has(p, v))
				.where(GraphPatterns.and(book.has(dc.iri("date"), date), book.has(p, v)).filter(
						Expressions.gt(date, Rdf.literalOfType("1970-01-01T00:00:00-02:00", xsd.iri("dateTime")))));
		p(modify);
	}

	@Test
	public void example_7() {
		// PREFIX foaf: <http://xmlns.com/foaf/0.1/>
		//
		// WITH <http://example/addresses>
		// DELETE { ?person ?property ?value }
		// WHERE { ?person ?property ?value ; foaf:givenName 'Fred' }
		Variable person = Spanqit.var("person"), property = Spanqit.var("property"), value = Spanqit.var("value");

		ModifyQuery modify = Queries.MODIFY().prefix(foaf).with(iri("http://example/addresses"))
				.delete(person.has(property, value))
				.where(person.has(property, value).andHas(foaf.iri("givenName"), "Fred"));

		p(modify);
	}

	@Test
	public void example_8() {
		// PREFIX dc: <http://purl.org/dc/elements/1.1/>
		// PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
		//
		// INSERT
		// { GRAPH <http://example/bookStore2> { ?book ?p ?v } }
		// WHERE
		// { GRAPH <http://example/bookStore>
		// { ?book dc:date ?date .
		// FILTER ( ?date > "1970-01-01T00:00:00-02:00"^^xsd:dateTime )
		// ?book ?p ?v
		// } }
		Variable book = Spanqit.var("book"), p = Spanqit.var("p"), v = Spanqit.var("v"), date = Spanqit.var("date");

		p(Queries.MODIFY().prefix(dc, xsd).insert(book.has(p, v)).into(iri("http://example/bookStore2"))
				.where(and(book.has(dc.iri("date"), date), book.has(p, v)).from(iri("http://example/bookStore")).filter(
						Expressions.gt(date, Rdf.literalOfType("1970-01-01T00:00:00-02:00", xsd.iri("dateTime"))))));
	}

	@Test
	public void example_9() {
		// PREFIX foaf: <http://xmlns.com/foaf/0.1/>
		// PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
		//
		// INSERT
		// { GRAPH <http://example/addresses>
		// {
		// ?person foaf:name ?name .
		// ?person foaf:mbox ?email
		// } }
		// WHERE
		// { GRAPH <http://example/people>
		// {
		// ?person foaf:name ?name .
		// OPTIONAL { ?person foaf:mbox ?email }
		// } }
		Variable person = Spanqit.var("person"), name = Spanqit.var("name"), email = Spanqit.var("email");
		TriplePattern personNameTriple = person.has(foaf.iri("name"), name),
				personEmailTriple = person.has(foaf.iri("mbox"), email);

		ModifyQuery insertAddressesQuery = Queries.MODIFY().prefix(foaf, rdf)
				.insert(personNameTriple, personEmailTriple).into(iri("http://example/addresses"))
				.where(and(personNameTriple, GraphPatterns.optional(personEmailTriple))
						.from(iri("http://example/people")));

		p(insertAddressesQuery);
	}

	@Test
	public void example_10() {
//		PREFIX dc:  <http://purl.org/dc/elements/1.1/>
//		PREFIX dcmitype: <http://purl.org/dc/dcmitype/>
//		PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
//
//		INSERT
//		  { GRAPH <http://example/bookStore2> { ?book ?p ?v } }
//		WHERE
//		  { GRAPH  <http://example/bookStore>
//		     { ?book dc:date ?date . 
//		       FILTER ( ?date < "2000-01-01T00:00:00-02:00"^^xsd:dateTime )
//		       ?book ?p ?v
//		     }
//		  } ;
//
//		WITH <http://example/bookStore>
//		DELETE
//		 { ?book ?p ?v }
//		WHERE
//		 { ?book dc:date ?date ;
//		         dc:type dcmitype:PhysicalObject .
//		   FILTER ( ?date < "2000-01-01T00:00:00-02:00"^^xsd:dateTime ) 
//		   ?book ?p ?v
//		 } 
		Variable book = Spanqit.var("book"), p = Spanqit.var("p"), v = Spanqit.var("v"), date = Spanqit.var("date");
		Prefix dcmitype = Spanqit.prefix("dcmitype", iri("http://purl.org/dc/dcmitype/"));

		ModifyQuery insertIntobookStore2Query = Queries.MODIFY().prefix(dcmitype, dc, xsd)
				.insert(book.has(p, v)).into(iri("http://example/bookStore2"))
				.where(and(book.has(dc.iri("date"), date), book.has(p, v))
						.from(iri("http://example/bookStore"))
						.filter(Expressions.lt(date, Rdf.literalOfType("1970-01-01T00:00:00-02:00", xsd.iri("dateTime")))));
		ModifyQuery deleteFromBookStoreQuery = Queries.MODIFY().with(iri("http://example/bookStore"))
				.delete(book.has(p, v))
				.where(and(book.has(dc.iri("date"), date).andHas(dc.iri("type"), dcmitype.iri("PhysicalObject")),
						   book.has(p, v))
						.filter(Expressions.lt(date, Rdf.literalOfType("2000-01-01T00:00:00-02:00", xsd.iri("dateTime")))));
		
		p(insertIntobookStore2Query, deleteFromBookStoreQuery);
	}
	
	@Test
	public void example_11() {
//		PREFIX foaf:  <http://xmlns.com/foaf/0.1/>
//
//		DELETE WHERE { ?person foaf:givenName 'Fred';
//		                       ?property      ?value }
		
		Variable person = Spanqit.var("person"), property = Spanqit.var("property"), value = Spanqit.var("value");

		p(Queries.MODIFY().prefix(foaf).delete().where(person.has(foaf.iri("givenName"), "Fred").andHas(property, value)));
	}
	
	@Test
	public void example_12() {
//		PREFIX foaf:  <http://xmlns.com/foaf/0.1/>
//
//		DELETE WHERE {
//		  GRAPH <http://example.com/names> {
//		    ?person foaf:givenName 'Fred' ;
//		            ?property1 ?value1
//		  }
//		  GRAPH <http://example.com/addresses> {
//		    ?person ?property2 ?value2
//		  }
//		}
		Variable person = Spanqit.var("person"), property1 = Spanqit.var("property1"), value1 = Spanqit.var("value1"),
				property2 = Spanqit.var("property2"), value2 = Spanqit.var("value2");
		Iri namesGraph = iri("http://example.com/names"), addressesGraph = iri("http://example.com/addresses");

		ModifyQuery deleteFredFromNamesAndAddressesQuery = Queries.MODIFY().prefix(foaf)
				.delete().where(and(person.has(foaf.iri("givenName"), "Fred").andHas(property1, value1)).from(namesGraph),
						and(person.has(property2, value2)).from(addressesGraph));
		
		p(deleteFredFromNamesAndAddressesQuery);
	}
	
	@Test
	public void example_load() {
		p(Queries.LOAD().from(iri(EXAMPLE_ORG_NS)));
		p(Queries.LOAD().silent().from(iri(EXAMPLE_ORG_NS)).to(iri(EXAMPLE_COM_NS)));
	}
	
	@Test
	public void example_clear() {
		p(Queries.CLEAR().def());
		p(Queries.CLEAR().silent().graph(iri(EXAMPLE_ORG_NS)));
	}

	@Test
	public void example_create() {
		p(Queries.CREATE().graph(iri(EXAMPLE_ORG_NS)));
		p(Queries.CREATE().silent().graph(iri(EXAMPLE_ORG_NS)));
	}
	
	@Test
	public void example_drop() {
		p(Queries.DROP().def());
		p(Queries.DROP().silent().graph(iri(EXAMPLE_ORG_NS)));
	}
	
	/* COPY DEFAULT TO <http://example.org/named> */
	@Test
	public void example_13() {
		p(Queries.COPY().fromDefault().to(iri("http://example.org/named")));
	}
	
	/* MOVE DEFAULT TO <http://example.org/named> */
	@Test
	public void example_14() {
		p(Queries.MOVE().fromDefault().to(iri("http://example.org/named")));
	}
	
	/* ADD DEFAULT TO <http://example.org/named> */
	@Test
	public void example_15() {
		p(Queries.ADD().fromDefault().to(iri("http://example.org/named")));
	}
}
