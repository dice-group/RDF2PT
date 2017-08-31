package org.aksw.sw2pt;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.aksw.sw2pt.triple2nl.DocumentGeneratorPortuguese;
import org.aksw.sw2pt.triple2nl.TripleConverterPortuguese;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.dllearner.kb.sparql.SparqlEndpoint;

public class sw2pt {
	
	public static String triples(String subject, String predicate, String object) throws IOException{
		// create the triple we want to convert by using JENA API
		Triple t = Triple.create(
					 NodeFactory.createURI(subject),
					 NodeFactory.createURI(predicate),
					 NodeFactory.createURI(object));

		// Optionally, we can declare a knowledge base that contains the triple.
		// This can be useful during the verbalization process, e.g. the KB could contain labels for entities.
		// Here, we use the DBpedia SPARQL endpoint.
		SparqlEndpoint endpoint = SparqlEndpoint.create("http://pt.dbpedia.org/sparql", "http://dbpedia.org");

		// create the triple converter
		TripleConverterPortuguese converter = new TripleConverterPortuguese(endpoint);

		// convert the triple into natural language
		return converter.convert(t);
	}
	
	public static String resumo(String uri) throws IOException{
		
		Sparql sparql = new Sparql();
		Set<Triple> list = new HashSet<>();
		
		list = sparql.getTriples(uri);

		Iterator<Triple> iterator = list.iterator();
		
		while(iterator.hasNext()){
			System.out.println(iterator.next());
		}
		
		//DocumentGeneratorPortuguese gen = new DocumentGeneratorPortuguese(SparqlEndpoint.create("http://pt.dbpedia.org/sparql", "http://dbpedia.org"), "cache");
		DocumentGeneratorPortuguese gen = new DocumentGeneratorPortuguese(SparqlEndpoint.create("http://pt.dbpedia.org/sparql", "http://dbpedia.org"), "cache");
		
		return gen.generateDocument(list);

	}

	public static void main(String[] args) throws IOException {
		

		//
		//System.out.println(resumo("http://pt.dbpedia.org/resource/Carlos_Chagas"));
		//System.out.println(resumo("http://pt.dbpedia.org/resource/Marcos_Pontes"));
		System.out.println(resumo("http://pt.dbpedia.org/resource/Porto_Alegre"));
		
		//System.out.println(triples("http://pt.dbpedia.org/resource/Iuri_Gagarin", "http://pt.dbpedia.org/property/missão", "http://pt.dbpedia.org/resource/Vostok_I"));

	}
}
