package pers.aprakash.spanqit.test;

import static pers.aprakash.spanqit.constraint.Expressions.bnode;
import static pers.aprakash.spanqit.constraint.Expressions.lt;
import static pers.aprakash.spanqit.constraint.Expressions.not;
import static pers.aprakash.spanqit.constraint.Expressions.or;
import static pers.aprakash.spanqit.constraint.Expressions.regex;
import static pers.aprakash.spanqit.core.Spanqit.asc;
import static pers.aprakash.spanqit.core.Spanqit.base;
import static pers.aprakash.spanqit.core.Spanqit.desc;
import static pers.aprakash.spanqit.core.Spanqit.prefix;
import static pers.aprakash.spanqit.graphpattern.GraphPatterns.and;
import static pers.aprakash.spanqit.graphpattern.GraphPatterns.optional;
import static pers.aprakash.spanqit.graphpattern.GraphPatterns.tp;
import static pers.aprakash.spanqit.graphpattern.GraphPatterns.union;

import org.junit.Test;

import pers.aprakash.spanqit.core.ConstructQuery;
import pers.aprakash.spanqit.core.Queries;
//import pers.aprakash.spanqit.core.ConstructQuery;
import pers.aprakash.spanqit.core.SelectQuery;
import pers.aprakash.spanqit.core.Variable;

public class SpanqitTest extends BaseSpanqitTest {
	String namespace = "http://www.spanqit.com/#";

	@Test
	public void constraintTest() {
		SelectQuery sq = new SelectQuery();

		Variable name = sq.var();
		Variable test = sq.var();
		Variable person = sq.var();

		sq.select(name).where(
				and(tp(name, test, person)).filter(
						not(or(lt(test, 5.)/*,
								gte(sq.createQueryVariable(),
										namespace)*/))),
				and(tp(name, test, person)).filter(bnode()));

		p(sq);
		p("\n");
		p(sq.getQueryString());
	}

	@Test
	public void test() {
		SelectQuery query = new SelectQuery();

		Variable name = query.var();
		Variable test = query.var();
		Variable person = query.var();

		query.base(base(iri(namespace, "base")))
				.prefix(prefix("ns", iri(namespace)))
				.prefix(prefix("ns", iri(namespace)))
				.prefix(prefix("", iri("http://www.default.com/#")))
				.select(name, test, person)
				.where(and(
						optional(tp(name, test, person)),
						optional(union(
								tp(name, person, test),
								tp(iri(namespace, "base"), person,
										literal(5.)),
								and(tp(name, person, test),
										tp(name, person, test))))))
				.orderBy(asc(name), desc(query.var())).limit(9)
				.offset(654);

		System.out.println(query.getQueryString());
	}

	@Test
	public void customTest() {
		String foaf = "http://xmlns.com/foaf/0.1/";
		SelectQuery sq = new SelectQuery();
		Variable name = sq.var();
		Variable mbox = sq.var();
		Variable x = sq.var();

		sq.select(name, mbox).where(tp(x, iri(foaf, "name"), name),
				tp(x, iri(foaf, "mbox"), mbox));

		p(sq);
	}

	@Test
	public void constructTest() {
		String foaf = "http://xmlns.com/foaf/0.1/";
		String org = "http://example.com/ns#";
		
		ConstructQuery cq = Queries.CONSTRUCT();

		Variable x = cq.var();
		Variable name = cq.var();

		cq.construct(tp(x, iri(foaf, "name"), name)).where(
				tp(x, iri(org, "name"), name));

		p(cq);
	}

	@Test
	public void regexTest() {
		String dc = "http://purl.org/dc/elements/1.2/";
		SelectQuery sq = new SelectQuery();
		Variable title = sq.var();
		Variable x = sq.var();

		sq.select(title).where(and(
				tp(x, iri(dc, "title"), title)).filter(
						regex(title, "^SPARQL", "ig")));

		p(sq);
	}

	@Test
	public void lessThanTest() {
		String dc = "http://purl.org/dc/elements/1.1/";
		String ns = "http://example.org/ns#";
		SelectQuery sq = new SelectQuery();
		Variable title = sq.var();
		Variable price = sq.var();
		Variable x = sq.var();

		sq.select(title, price)
		  .where(and(tp(x, iri(ns, "price"), price),
		  			 tp(x, iri(dc, "title"), title))
		  		 .filter(lt(price, 30.5)));

		p(sq);
	}

	@Test
	public void testEmpty() {
		p(new SelectQuery());
	}
	
	@Test
	public void singleGraphPatternInQueryPattern() {
		SelectQuery select = new SelectQuery();
		Variable v1 = select.var(), v2 = select.var(), v3 = select.var();
//		select.where(and(tp(v2, v2, v3)));
		select.where(tp(v1, v2, v3), union(tp(v2, v2, v3)));
		p(select);
	}
}