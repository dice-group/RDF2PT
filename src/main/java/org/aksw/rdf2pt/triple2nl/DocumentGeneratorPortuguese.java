/*
 * #%L
 * Triple2NL
 * %%
 * Copyright (C) 2015 Agile Knowledge Engineering and Semantic Web (AKSW)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
/**
 * 
 */
package org.aksw.rdf2pt.triple2nl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.aksw.jena_sparql_api.core.QueryExecutionFactory;
import org.aksw.jena_sparql_api.http.QueryExecutionFactoryHttp;
import org.aksw.rdf2pt.Sparql;
import org.aksw.rdf2pt.triple2nl.converter.DefaultIRIConverterPortuguese;
import org.aksw.rdf2pt.triple2nl.converter.IRIConverter;
import org.aksw.rdf2pt.triple2nl.gender.DictionaryBasedGenderDetector;
import org.aksw.rdf2pt.triple2nl.gender.Gender;
import org.aksw.rdf2pt.triple2nl.gender.GenderDetector;
import org.apache.jena.atlas.logging.Log;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDF;
import org.dllearner.kb.sparql.SparqlEndpoint;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import simplenlg.features.Feature;
import simplenlg.features.InternalFeature;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.portuguese.XMLLexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.Realiser;

/**
 * @author Diego Moussallem
 *
 */
public class DocumentGeneratorPortuguese {

	private TripleConverterPortuguese tripleConverter;
	private NLGFactory nlgFactory;
	private Realiser realiser;
	private GenderDetector genderDetector;
	private IRIConverter uriConverter;

	private boolean useAsWellAsCoordination = true;

	public DocumentGeneratorPortuguese(SparqlEndpoint endpoint, String cacheDirectory) {
		this(endpoint, cacheDirectory, new XMLLexicon());
	}

	public DocumentGeneratorPortuguese(SparqlEndpoint endpoint, String cacheDirectory, Lexicon lexicon) {
		this(new QueryExecutionFactoryHttp(endpoint.getURL().toString(), endpoint.getDefaultGraphURIs()),
				cacheDirectory, lexicon);
	}

	public DocumentGeneratorPortuguese(QueryExecutionFactory qef, String cacheDirectory, Lexicon lexicon) {
		tripleConverter = new TripleConverterPortuguese(qef, null, null, cacheDirectory, null, lexicon);
		nlgFactory = new NLGFactory(lexicon);
		realiser = new Realiser();
	}

	public String generateDocument(Model model) throws IOException {
		Set<Triple> triples = asTriples(model);
		return generateDocument(triples);
	}

	private Set<Triple> asTriples(Model model) {
		Set<Triple> triples = new HashSet<>((int) model.size());
		StmtIterator iterator = model.listStatements();
		while (iterator.hasNext()) {
			Statement statement = iterator.next();
			triples.add(statement.asTriple());
		}
		return triples;
	}

	public String generateDocument(Set<Triple> documentTriples) throws IOException {
		
		DefaultDirectedGraph<Node, DefaultEdge> graph = asGraph(documentTriples);

		// divide the document into paragraphs for each connected component in
		// the graph
		ConnectivityInspector<Node, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(graph);
		List<Set<Node>> connectedNodes = connectivityInspector.connectedSets();

		// for (Set<Node> nodes : connectedNodes) {
		// System.out.println(nodes);
		// }

		// group triples by subject
		Map<Node, Collection<Triple>> subject2Triples = groupBySubject(documentTriples);

		// do some sorting
		subject2Triples = sort(documentTriples, subject2Triples);
		//System.out.println(documentTriples.size());
		Sparql sparql = new Sparql();
		boolean dead = false;
		boolean summary = true;
		List<DocumentElement> sentences = new ArrayList<>();
		for (Entry<Node, Collection<Triple>> entry : subject2Triples.entrySet()) {
			Node subject = entry.getKey();
			dead = sparql.hasDeathPlace(subject.toString());
			String genericType = sparql.mostGenericClass(subject.toString());
			String specificType = sparql.mostSpecificClass(subject.toString());
			//System.out.println("Subject: " + subject.toString() + "  Generic type: " + genericType + "  Dead? " + dead);

			genderDetector = new DictionaryBasedGenderDetector();
			uriConverter = new DefaultIRIConverterPortuguese(SparqlEndpoint.create("http://pt.dbpedia.org/sparql", "http://dbpedia.org"));
			Gender gender = genderDetector.getGender(uriConverter.convert(subject.toString()));

			Collection<Triple> triples = entry.getValue();

			// combine with conjunction
			CoordinatedPhraseElement conjunction = nlgFactory.createCoordinatedPhrase();
			CoordinatedPhraseElement conjunction2 = nlgFactory.createCoordinatedPhrase();
			
			// get the type triples first
			Set<Triple> typeTriples = new HashSet<>();
			Set<Triple> otherTriples = new HashSet<>();

			for (Triple triple : triples) {
				if (triple.predicateMatches(RDF.type.asNode())) {
					typeTriples.add(triple);
				} else {
					otherTriples.add(triple);
				}
			}

			// convert the type triples
			List<SPhraseSpec> typePhrases = tripleConverter.convertToPhrases(typeTriples, dead, summary);

			// if there are more than one types, we combine them in a single
			// clause
			if (typePhrases.size() > 1) {
				// combine all objects in a coordinated phrase
				CoordinatedPhraseElement combinedObject = nlgFactory.createCoordinatedPhrase();

				// here will be necessary to treat the gramatical gender of
				// subjects
				// the last 2 phrases are combined via 'assim como'
				if (useAsWellAsCoordination) {
					SPhraseSpec phrase1 = typePhrases.remove(typePhrases.size() - 1);
					SPhraseSpec phrase2 = typePhrases.get(typePhrases.size() - 1);
					// combine all objects in a coordinated phrase
					CoordinatedPhraseElement combinedLastTwoObjects = nlgFactory
							.createCoordinatedPhrase(phrase1.getObject(), phrase2.getObject());
					combinedLastTwoObjects.setConjunction("assim como");
					combinedLastTwoObjects.setFeature(Feature.RAISE_SPECIFIER, false);
					if (gender.name().equals("FEMALE")) {
						combinedLastTwoObjects.setFeature(InternalFeature.SPECIFIER, "uma"); // here
					} else {
						if (specificType.equals("http://dbpedia.org/ontology/SoccerClub")) {
							combinedLastTwoObjects.setFeature(InternalFeature.SPECIFIER, "um"); // here
						}
					}
					phrase2.setObject(combinedLastTwoObjects);
				}

				Iterator<SPhraseSpec> iterator = typePhrases.iterator();
				// pick first phrase as representative
				SPhraseSpec representative = iterator.next();
				combinedObject.addCoordinate(representative.getObject());

				while (iterator.hasNext()) {
					SPhraseSpec phrase = iterator.next();
					NLGElement object = phrase.getObject();
					combinedObject.addCoordinate(object);
				}

				combinedObject.setFeature(Feature.RAISE_SPECIFIER, true);

				// set the coordinated phrase as the object
				representative.setObject(combinedObject);
				// return a single phrase
				typePhrases = Lists.newArrayList(representative);
			}
			for (SPhraseSpec phrase : typePhrases) {
				conjunction.addCoordinate(phrase);
			}

			// convert the other triples, but use place holders for the subject
			String placeHolderToken;

			if (genericType.equals("http://dbpedia.org/ontology/Person")) {
				if (gender.name().equals("FEMALE")) {
					placeHolderToken = (typeTriples.isEmpty() || otherTriples.size() == 1) ? "dela" : "dela";
				} else {
					placeHolderToken = (typeTriples.isEmpty() || otherTriples.size() == 1) ? "dele" : "dele";
				}
			} else if (genericType.equals("http://dbpedia.org/ontology/Organisation")) {
				if (specificType.equals("http://dbpedia.org/ontology/SoccerClub")) {
					placeHolderToken = (typeTriples.isEmpty() || otherTriples.size() == 1) ? "dele" : "dele";
				} else {
					placeHolderToken = (typeTriples.isEmpty() || otherTriples.size() == 1) ? "dela" : "dela";
				}

			} else if (genericType.equals("http://dbpedia.org/ontology/PopulatedPlace")) {
				if (specificType.equals("http://dbpedia.org/ontology/City")) {
					placeHolderToken = (typeTriples.isEmpty() || otherTriples.size() == 1) ? "dela" : "dela";
				} else {
					placeHolderToken = (typeTriples.isEmpty() || otherTriples.size() == 1) ? "dele" : "dele";
				}
			} else if (genericType.equals("http://dbpedia.org/ontology/ArchitecturalStructure")) {
				if (specificType.equals("http://dbpedia.org/ontology/Church")) {
					placeHolderToken = (typeTriples.isEmpty() || otherTriples.size() == 1) ? "dela" : "dela";
				} else {
					placeHolderToken = (typeTriples.isEmpty() || otherTriples.size() == 1) ? "dele" : "dele";
				}
			
			}
			else {
				placeHolderToken = (typeTriples.isEmpty() || otherTriples.size() == 1) ? "dele" : "dele";
			}
			Node placeHolder = NodeFactory.createURI("http://sparql2nl.aksw.org/placeHolder/" + placeHolderToken);

			// Node placeHolder = subject;
			Collection<Triple> placeHolderTriples = new ArrayList<>(otherTriples.size());
			Iterator<Triple> iterator = otherTriples.iterator();
			// we have to keep one triple with subject if we have no type
			// triples
			if (typeTriples.isEmpty() && iterator.hasNext()) {
				placeHolderTriples.add(iterator.next());
			}
			while (iterator.hasNext()) {
				Triple triple = iterator.next();
				Triple newTriple = Triple.create(placeHolder, triple.getPredicate(), triple.getObject());
				placeHolderTriples.add(newTriple);
			}

			Collection<SPhraseSpec> otherPhrases = tripleConverter.convertToPhrases(placeHolderTriples, dead, summary);

			int count = 0;
			boolean nextSentence = false;
			for (SPhraseSpec phrase : otherPhrases) {
				if (count < 2 ){
				conjunction.addCoordinate(phrase);
				}
				else if(count >= 2 ) {
					if(count == 2) {
						conjunction2.addPreModifier("Além disso,");}
				conjunction2.addCoordinate(phrase);
				nextSentence = true;
				}
				count++;
			}
			
			DocumentElement sentence = nlgFactory.createSentence(conjunction);
			sentences.add(sentence);
			if(nextSentence == true){
			DocumentElement sentence2 = nlgFactory.createSentence(conjunction2);
			sentences.add(sentence2);
			}
			
		}
		//DocumentElement par1 = nlgFactory.createParagraph(Arrays.asList(s1, s2, s3)); [1]
		DocumentElement paragraph = nlgFactory.createParagraph(sentences);

		String paragraphText = realiser.realise(paragraph).getRealisation();
		paragraphText = paragraphText.replaceAll("an ", "a ");
		paragraphText = paragraphText.replaceAll(", e ", " e ");
		return paragraphText;
	}

	/**
	 * @param documentTriples
	 *            the set of triples
	 * @param subject2Triples
	 *            a map that contains for each node the triples in which it
	 *            occurs as subject
	 */
	private Map<Node, Collection<Triple>> sort(Set<Triple> documentTriples,
			Map<Node, Collection<Triple>> subject2Triples) {
		Map<Node, Collection<Triple>> sortedTriples = new LinkedHashMap<>();
		// we can order by occurrence, i.e. which subjects do not occur in
		// object position
		// for each subject we check how often they occur in subject/object
		// position
		Multimap<Node, Node> outgoing = HashMultimap.create();
		Multimap<Node, Node> incoming = HashMultimap.create();
		for (Node subject : subject2Triples.keySet()) {
			for (Triple triple : documentTriples) {
				if (triple.subjectMatches(subject)) {
					outgoing.put(subject, triple.getObject());
				} else if (triple.objectMatches(subject)) {
					incoming.put(subject, triple.getSubject());
				}
			}
		}
		// prefer subjects that do not occur in object position first
		for (Iterator<Entry<Node, Collection<Triple>>> iterator = subject2Triples.entrySet().iterator(); iterator
				.hasNext();) {
			Entry<Node, Collection<Triple>> entry = iterator.next();
			Node subject = entry.getKey();
			if (!incoming.containsKey(subject)) {
				sortedTriples.put(subject, new HashSet<>(entry.getValue()));
				iterator.remove();
			}
		}
		// add the rest
		sortedTriples.putAll(subject2Triples);

		// TODO order by triple count

		// TODO order by prominence

		return sortedTriples;
	}

	private Map<Node, Collection<Triple>> groupBySubject(Set<Triple> triples) {
		Multimap<Node, Triple> subject2Triples = HashMultimap.create();
		for (Triple triple : triples) {
			subject2Triples.put(triple.getSubject(), triple);
		}
		return subject2Triples.asMap();
	}

	private DefaultDirectedGraph<Node, DefaultEdge> asGraph(Set<Triple> triples) {
		DefaultDirectedGraph<Node, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
		for (Triple triple : triples) {
			// we have to omit type triples to get connected subgraphs later on
			if (!triple.predicateMatches(RDF.type.asNode())) {
				graph.addVertex(triple.getSubject());
				graph.addVertex(triple.getObject());
				graph.addEdge(triple.getSubject(), triple.getObject());
			}
		}
		return graph;
	}

	public static void main(String[] args) throws Exception {
		String triples = "@prefix dbr: <http://dbpedia.org/resource/>." + "@prefix dbo: <http://dbpedia.org/ontology/>."
				+ "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>."
				+ "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>."

				+ "dbr:Albert_Einstein rdf:type dbo:Physican, dbo:Philosopher;" + "dbo:birthPlace dbr:Ulm;"
				+ "dbo:birthDate \"1879-03-14\"^^xsd:date;" + "dbo:academicAdvisor dbr:Heinrich_Friedrich_Weber;"
				// + "dbo:almaMater dbr:ETH_Zurich."; //fix from ex-aluno to
				// ex-instituto.
				// + "dbo:almaMater dbr:University_of_Zurich;"
				+ "dbo:award dbr:Max_Planck_Medal;"
				// + "dbo:award
				// dbr:Time_100:_The_Most_Important_People_of_the_Century;"
				// + "dbo:award dbr:Copley_Medal;"
				// + "dbo:award dbr:Nobel_Prize_in_Physics;"
				// + "dbo:award
				// dbr:Barnard_Medal_for_Meritorious_Service_to_Science;"
				// + "dbo:award dbr:Matteucci_Medal;"
				// + "dbo:award dbr:Royal_Society.";
				+ "dbo:citizenship dbr:Austria-Hungary.";
		// + "dbo:citizenship dbr:Kingdom_of_Württemberg;"
		// + "dbo:citizenship dbr:Statelessness;"
		// + "dbo:citizenship dbr:Switzerland;"
		// + "dbo:doctoralAdvisor dbr:Alfred_Kleiner.";
		// + "dbo:field dbr:Physics;"
		// + "dbo:field dbr:Philosophy;"
		// + "dbo:influenced dbr:Nathan_Rosen;"
		// + "dbo:influenced dbr:Leo_Szilard;"
		// + "dbo:influenced dbr:Ernst_G._Straus;"
		// + "dbo:knownFor dbr:Bose–Einstein_condensate;"
		// + "dbo:knownFor dbr:Brownian_motion;"
		// + "dbo:knownFor dbr:EPR_paradox;"
		// + "dbo:knownFor dbr:General_relativity;"
		// + "dbo:knownFor dbr:Photoelectric_effect;"
		// + "dbo:knownFor dbr:Special_relativity;"
		// + "dbo:knownFor dbr:Cosmological_constant;"
		// + "dbo:knownFor dbr:Mass–energy_equivalence;"
		// + "dbo:knownFor dbr:Gravitational_wave;"
		// + "dbo:knownFor dbr:Einstein_field_equations;"
		// + "dbo:knownFor dbr:Classical_unified_field_theories;"
		// + "dbo:knownFor dbr:Bose–Einstein_statistics;"
		// + "dbo:residence dbr:Switzerland;"
		// + "dbo:spouse dbr:Elsa_Einstein;"
		// + "dbo:spouse dbr:Mileva_Marić;"
		// + "dbo:deathPlace dbr:Princeton,_New_Jersey;"
		// + "dbo:deathDate \"1955-04-18\"^^xsd:date .";
		// + "dbr:Ulm rdf:type dbo:city.";
		// + "dbo:country dbr:Germany.";
		// + "";
		// + "dbo:federalState :Baden_Württemberg ."
		// + ":Leipzig a dbo:City;"
		// + "dbo:country :Germany;"
		// + "dbo:federalState :Saxony .";

		// String triples =
		// "@prefix dbr: <http://dbpedia.org/resource/>."
		// + "@prefix dbo: <http://dbpedia.org/ontology/>."
		// + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>."
		// + "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>."
		// + "dbr:Angela_Merkel rdf:type dbo:Scientist, dbo:OfficeHolder;"
		// + "dbo:birthPlace dbr:Hamburg.";
		// //+ "dbo:birthDate \"1954-07-17\"^^xsd:date,"
		// //+ "dbo:studiedIn dbr:Leipzig."
		// + "dbr:Hamburg rdf:type dbo:City;"
		// + "dbo:country dbr:Germany.";
		// //+ "dbr:Leipzig rdf:type dbo:City,"
		// //+ "dbo:country dbr:Germany,"
		// //+ "dbo:federalState dbr:Saxony.";

		Model model = ModelFactory.createDefaultModel();
		model.read(new ByteArrayInputStream(triples.getBytes()), null, "TURTLE");

		DocumentGeneratorPortuguese gen = new DocumentGeneratorPortuguese(
				SparqlEndpoint.create("http://pt.dbpedia.org/sparql", ""), "cache");
		String document = gen.generateDocument(model);
		System.out.println(document);
	}

}
