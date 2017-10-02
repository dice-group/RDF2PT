/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aksw.rdf2pt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.sparql.function.library.leviathan.log;
import org.semanticweb.elk.reasoner.stages.LoggingStageExecutor;

/**
 *
 * @author DiegoMoussallem
 */
public class Sparql {

	public Set<Triple> getTriples(String resource) {

		String type = null;

		type = mostSpecificClass(resource);
		//System.out.println("Type: " + type);
		Set<Triple> result = new HashSet<>();
		List<String> predicates = new ArrayList<>();
		
		String ontology_service = "http://pt.dbpedia.org/sparql";
		//String ontology_service = "http://dbpedia.org/sparql";

		String sparqlQuery = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX dbo: <http://dbpedia.org/ontology/>" + "PREFIX owl: <http://www.w3.org/2002/07/owl#> "

				+ "select distinct ?p (COUNT(?p) AS ?po) where {"

				+ "?s rdf:type <" + type + ">." + "?s ?p ?o." + "?p rdfs:label []."
				 + "FILTER ( strstarts(str(?p), 'http://dbpedia.org/ontology') )"
				+ "FILTER ( !strstarts(str(?p), 'http://dbpedia.org/ontology/abstract' ) )"
				+ "FILTER ( !strstarts(str(?o), 'http://commons.wikimedia.org/wiki/Special' ) )"
				+ "FILTER ( !strstarts(str(?o), 'http://pt.wikipedia.org/wiki/Special' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://dbpedia.org/ontology/subtitle' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://dbpedia.org/ontology/thumbnail' ) )"
				+ "FILTER ( !strstarts(str(?p), 'http://www.w3.org/' ) )"
				+ "FILTER ( !strstarts(str(?p), 'http://xmlns.com' ) )"
				+ "FILTER ( !strstarts(str(?p), 'http://purl.org/dc/terms/subject' ) )"
				+ "FILTER ( !strstarts(str(?p), 'http://pt.dbpedia.org/property/wikiPageUsesTemplate' ) )"
				+ "FILTER ( !strstarts(str(?p), 'http://dbpedia.org/ontology/wikiPageExternalLink' ) )"
				+ "FILTER ( !strstarts(str(?p), 'http://dbpedia.org/ontology/wikiPageWikiLink' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://pt.dbpedia.org/property/nome' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://dbpedia.org/ontology/mission' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://wikidata.dbpedia.org/property/' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://pt.dbpedia.org/property/imagem' ) )"
////				+ "FILTER ( !strstarts(str(?p), 'http://dbpedia.org/ontology/nationality' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://pt.dbpedia.org/property/tipo' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://dbpedia.org/ontology/type' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://pt.dbpedia.org/property/legenda' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://pt.dbpedia.org/property/foto' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://pt.dbpedia.org/property/imagesize' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://pt.dbpedia.org/property/premio' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://pt.dbpedia.org/property/tamanho' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://pt.dbpedia.org/property/patente' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://dbpedia.org/ontology/occupation' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://pt.dbpedia.org/property/topo' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://pt.dbpedia.org/property/idioma' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://dbpedia.org/ontology/picture' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://pt.dbpedia.org/property/site' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://pt.dbpedia.org/property/aposentadoria' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://pt.dbpedia.org/property/website' ) )"
////				+ "FILTER ( !strstarts(str(?p), 'http://dbpedia.org/ontology/location' ) )"
//				+ "FILTER ( !strstarts(str(?p), 'http://dbpedia.org/ontology/timeInSpace' ) )"
////				+ "FILTER ( !strstarts(str(?p), 'http://dbpedia.org/ontology/areaTotal' ) )"
				

				+ "}" + "GROUP BY (?p)" + "ORDER BY DESC (?po)" + "LIMIT 50";

		QueryExecution query = QueryExecutionFactory.sparqlService(ontology_service, String.format(sparqlQuery));

		ResultSet results = null;
		try {
			results = query.execSelect();
		} catch (Exception e) {
			return null;
		}

		String pred = null;
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			pred = qs.getResource("p").toString();
			predicates.add(pred);
			//System.out.println(pred.toString());
		}

		result.add(Triple.create(NodeFactory.createURI(resource),
				NodeFactory.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), NodeFactory.createURI(type)));

		Triple t;
		if (predicates.size() >= 6) {

			for (int i = 0; i < 6; i++) {
				// System.out.println(predicates.get(i));
				t = getObjects(resource, predicates.get(i));
				if (t != null) {
					result.add(t);
				}
			}
		} else {

			for (int i = 0; i < predicates.size(); i++) {
				// System.out.println(predicates.get(i));
				t = getObjects(resource, predicates.get(i));
				if (t != null) {
					result.add(t);
				}
			}
		}

		return result;
	}

	public Triple getObjects(String resource, String predicate) {

		//String ontology_service = "http://dbpedia.org/sparql";
		String ontology_service = "http://pt.dbpedia.org/sparql";
		
		Triple triple = null;
		String sparqlQuery = " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ " PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "SELECT DISTINCT ?o WHERE { " + "<" + resource
				+ "> <" + predicate + "> ?o."
				// + "?o rdfs:label ?label."
				// + "FILTER (lang(?o) = 'pt')"
				// + "FILTER (lang(?label) = 'pt') "
				+ "} " 
				//+ "LIMIT 1"
				;

		QueryExecution query = QueryExecutionFactory.sparqlService(ontology_service, String.format(sparqlQuery));

		ResultSet results = null;
		try {
			results = query.execSelect();
		} catch (Exception e) {
			return null;
		}

		Node subject = NodeFactory.createURI(resource);
		Node property = NodeFactory.createURI(predicate);
		Node object;

		while (results.hasNext()) {
			QuerySolution qs = results.next();
			if (qs.get("o").isLiteral()) {
				object = NodeFactory.createLiteral(qs.getLiteral("o").getLexicalForm(),
						qs.getLiteral("o").getLanguage(), qs.getLiteral("o").getDatatype());
				// System.out.println(object.toString());
			} else {
				object = NodeFactory.createURI(qs.getResource("o").toString());
				// System.out.println(object.toString());
			}

			// System.out.println(resource + predicate);
			if (object != null) {
				triple = Triple.create(subject, property, object);
				// System.out.println(triple.toString());
			}
		}

		return triple;

	}

	public String mostGenericClass(String uri) {
		// First query takes the most specific class from a given resource.
		
		String ontology_service = "http://pt.dbpedia.org/sparql";
		//String ontology_service = "http://dbpedia.org/sparql";
		
		String sparqlQuery = " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ " PREFIX dbr: <http://dbpedia.org/resource/>" + " PREFIX dbo: <http://dbpedia.org/ontology/>"
				+ " PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "SELECT DISTINCT ?type WHERE {" + "<" + uri
				+ "> rdf:type ?type." + "?type rdfs:subClassOf ?genericType. "
				+ "?genericType rdfs:subClassOf owl:Thing ;"
				+ "FILTER ( strstarts(str(?type), 'http://dbpedia.org/ontology' ) )}";

		QueryExecution query = QueryExecutionFactory.sparqlService(ontology_service, String.format(sparqlQuery));

		ResultSet results = null;
		try {
			results = query.execSelect();
		} catch (Exception e) {
			return "";
		}

		String property = "";
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			property = qs.getResource("type").toString();

		}
		return property;
	}

	public String mostSpecificClass(String uri) {
		// First query takes the most specific class from a given resource.
		String ontology_service = "http://pt.dbpedia.org/sparql";
		//String ontology_service = "http://dbpedia.org/sparql";
		
		String sparqlQuery = " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ " PREFIX dbr: <http://dbpedia.org/resource/>" + " PREFIX dbo: <http://dbpedia.org/ontology/>"
				+ " PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "SELECT DISTINCT ?lcs WHERE {"
				+ "?lcs ^rdf:type/rdfs:subClassOf* <" + uri + ">;" + "       a owl:Class ." + "  filter not exists {"
				+ "    ?llcs ^(rdf:type/rdfs:subClassOf*) <" + uri + "> ;" + "          a owl:Class ;"
				+ "          rdfs:subClassOf+ ?lcs ." + "  }"
				+ "FILTER ( !strstarts(str(?lcs), 'http://www.wikidata.org/entity/' ) )}";

		QueryExecution query = QueryExecutionFactory.sparqlService(ontology_service, String.format(sparqlQuery));

		ResultSet results = null;
		try {
			results = query.execSelect();
		} catch (Exception e) {
			return "";
		}

		String property = null;
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			property = qs.getResource("lcs").toString();

		}
//		System.out.println(property);
		return property;
	}

	public boolean hasDeathPlace(String uri) {
		String ontology_service = "http://pt.dbpedia.org/sparql";
		//String ontology_service = "http://dbpedia.org/sparql";
		
		String sparqlQuery = " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ " PREFIX dbr: <http://dbpedia.org/resource/>" + " PREFIX dbo: <http://dbpedia.org/ontology/>"
				+ " PREFIX owl: <http://www.w3.org/2002/07/owl#>" 
		 + "SELECT DISTINCT ?o WHERE {<" + uri + "> ?p ?o."
		 		+ "FILTER (?p = <http://dbpedia.org/ontology/deathPlace> || ?p = <http://pt.dbpedia.org/property/localMorte> )}";

		QueryExecution query = QueryExecutionFactory.sparqlService(ontology_service, String.format(sparqlQuery));

		ResultSet results = null;
		try {
			results = query.execSelect();
		} catch (Exception e) {
			return false;
		}

		Node property = null;
		boolean contain = false;
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			if (qs.get("o").isLiteral()) {
				property = NodeFactory.createLiteral(qs.getLiteral("o").getLexicalForm(),
						qs.getLiteral("o").getLanguage(), qs.getLiteral("o").getDatatype());
				// System.out.println(object.toString());
			} else {
				property = NodeFactory.createURI(qs.getResource("o").toString());
				// System.out.println(object.toString());
			}

		}

		if (property != null) {
			// System.out.println("property" + property.toString());
			contain = true;
		}
		return contain;
	}

}
