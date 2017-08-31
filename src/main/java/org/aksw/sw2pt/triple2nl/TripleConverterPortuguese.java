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
package org.aksw.sw2pt.triple2nl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.aksw.jena_sparql_api.core.QueryExecutionFactory;
import org.aksw.jena_sparql_api.http.QueryExecutionFactoryHttp;
import org.aksw.jena_sparql_api.model.QueryExecutionFactoryModel;
import org.aksw.sw2pt.Sparql;
import org.aksw.sw2pt.triple2nl.converter.DefaultIRIConverterPortuguese;
import org.aksw.sw2pt.triple2nl.converter.IRIConverter;
import org.aksw.sw2pt.triple2nl.converter.LiteralConverterPortuguese;
import org.aksw.sw2pt.triple2nl.gender.DictionaryBasedGenderDetector;
import org.aksw.sw2pt.triple2nl.gender.Gender;
import org.aksw.sw2pt.triple2nl.gender.GenderDetector;
import org.aksw.sw2pt.triple2nl.nlp.stemming.PlingStemmer;
import org.aksw.sw2pt.triple2nl.property.PropertyVerbalization;
import org.aksw.sw2pt.triple2nl.property.PropertyVerbalizationType;
import org.aksw.sw2pt.triple2nl.property.PropertyVerbalizerPortuguese;
import org.aksw.sw2pt.triple2nl.util.GenericType;
import org.aksw.sw2pt.triple2nl.util.Gtranslate;
import org.aksw.sw2pt.utils.nlp.pos.TreeTagger;
import org.apache.commons.collections15.ListUtils;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.graph.impl.LiteralLabel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.dllearner.kb.sparql.SparqlEndpoint;
import org.dllearner.reasoning.SPARQLReasoner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import net.sf.extjwnl.dictionary.Dictionary;
import simplenlg.features.Feature;
import simplenlg.features.InternalFeature;
import simplenlg.features.LexicalFeature;
import simplenlg.features.Tense;
import simplenlg.features.portuguese.PortugueseFeature;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.portuguese.XMLLexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.Realiser;
import uk.ac.manchester.cs.owl.owlapi.OWLDataPropertyImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

/**
 * Convert triple(s) into natural language.
 * 
 * @author Diego Moussallem
 * 
 */
public class TripleConverterPortuguese {

	private static final Logger logger = LoggerFactory.getLogger(TripleConverterPortuguese.class);

	private static String DEFAULT_CACHE_BASE_DIR = System.getProperty("java.io.tmpdir");
	private static String DEFAULT_CACHE_DIR = DEFAULT_CACHE_BASE_DIR + "/triple2nl-cache/portuguese";

	private NLGFactory nlgFactory;
	private Realiser realiser;

	private IRIConverter uriConverter;
	private LiteralConverterPortuguese literalConverter;
	private PropertyVerbalizerPortuguese pp;
	private SPARQLReasoner reasoner;

	private boolean determinePluralForm = false;
	// show language as adjective for literals
	private boolean considerLiteralLanguage = false;
	// encapsulate string literals in quotes ""
	private boolean encapsulateStringLiterals = true;
	// for multiple types use 'as well as' to coordinate the last type
	private boolean useAsWellAsCoordination = true;

	private boolean returnAsSentence = true;

	private boolean useGenderInformation = true;

	private GenderDetector genderDetector;

	public TripleConverterPortuguese() {
		this(new QueryExecutionFactoryModel(ModelFactory.createDefaultModel()), DEFAULT_CACHE_DIR, new XMLLexicon());
	}

	public TripleConverterPortuguese(SparqlEndpoint endpoint) {
		this(endpoint, DEFAULT_CACHE_DIR);
	}

	public TripleConverterPortuguese(QueryExecutionFactory qef, String cacheDirectory, Dictionary wordnetDirectory) {
		this(qef, null, null, cacheDirectory, wordnetDirectory, null);
	}

	public TripleConverterPortuguese(SparqlEndpoint endpoint, String cacheDirectory) {
		this(endpoint, cacheDirectory, null);
	}

	public TripleConverterPortuguese(SparqlEndpoint endpoint, String cacheDirectory, Dictionary wordnetDirectory) {
		this(new QueryExecutionFactoryHttp(endpoint.getURL().toString(), endpoint.getDefaultGraphURIs()), null, null,
				cacheDirectory, wordnetDirectory, new XMLLexicon());
	}

	public TripleConverterPortuguese(SparqlEndpoint endpoint, String cacheDirectory, Dictionary wordnetDirectory,
			Lexicon lexicon) {
		this(new QueryExecutionFactoryHttp(endpoint.getURL().toString(), endpoint.getDefaultGraphURIs()), null, null,
				cacheDirectory, wordnetDirectory, lexicon);
	}

	public TripleConverterPortuguese(QueryExecutionFactory qef, IRIConverter uriConverter, String cacheDirectory,
			Dictionary wordnetDirectory) {
		this(qef, null, uriConverter, cacheDirectory, wordnetDirectory, new XMLLexicon());
	}

	public TripleConverterPortuguese(QueryExecutionFactory qef, String cacheDirectory, Lexicon lexicon) {
		this(qef, null, null, cacheDirectory, null, lexicon);
	}

	public TripleConverterPortuguese(QueryExecutionFactory qef, PropertyVerbalizerPortuguese propertyVerbalizer,
			IRIConverter uriConverter, String cacheDirectory, Dictionary wordnetDirectory, Lexicon lexicon) {
		if (uriConverter == null) {
			uriConverter = new DefaultIRIConverterPortuguese(qef, cacheDirectory);
		}
		this.uriConverter = uriConverter;

		if (propertyVerbalizer == null) {
			propertyVerbalizer = new PropertyVerbalizerPortuguese(uriConverter, wordnetDirectory);
		}
		pp = propertyVerbalizer;

		if (lexicon == null) {
			lexicon = new XMLLexicon();
		}

		nlgFactory = new NLGFactory(lexicon);
		realiser = new Realiser();

		literalConverter = new LiteralConverterPortuguese(uriConverter);
		literalConverter.setEncapsulateStringLiterals(encapsulateStringLiterals);

		reasoner = new SPARQLReasoner(qef);

		genderDetector = new DictionaryBasedGenderDetector();
	}

	/**
	 * Return a textual representation for the given triple.
	 *
	 * @param t
	 *            the triple to convert
	 * @return the textual representation
	 * @throws IOException
	 */
	public String convert(Triple t) throws IOException {
		return convert(t, false);
	}

	/**
	 * Return a textual representation for the given triple.
	 *
	 * @param t
	 *            the triple to convert
	 * @param negated
	 *            if phrase is negated
	 * @return the textual representation
	 * @throws IOException
	 */
	public String convert(Triple t, boolean negated) throws IOException {
		NLGElement phrase = convertToPhrase(t, negated, false, false);
		String text;
		if (returnAsSentence) {
			text = realiser.realiseSentence(phrase);
		} else {
			text = realiser.realise(phrase).getRealisation();
		}
		return text;
	}

	/**
	 * Return a textual representation for the given triples. Currently we
	 * assume that all triples have the same subject!
	 * 
	 * @param triples
	 *            the triples to convert
	 * @return the textual representation
	 * @throws IOException
	 */
	public String convert(List<Triple> triples) throws IOException {
		// combine with conjunction
		CoordinatedPhraseElement typesConjunction = nlgFactory.createCoordinatedPhrase();

		// separate type triples from others
		List<Triple> typeTriples = triples.stream().filter(t -> t.predicateMatches(RDF.type.asNode()))
				.collect(Collectors.toList());
		List<Triple> otherTriples = ListUtils.subtract(triples, typeTriples);

		// convert the type triples
		List<SPhraseSpec> typePhrases = convertToPhrases(typeTriples, false, false);

		// if there is more than one type, we combine them into a single clause
		if (typePhrases.size() > 1) {
			// combine all objects in a coordinated phrase
			CoordinatedPhraseElement combinedObject = nlgFactory.createCoordinatedPhrase();

			// the last 2 phrases are combined via 'as well as'
			if (useAsWellAsCoordination) {
				SPhraseSpec phrase1 = typePhrases.remove(typePhrases.size() - 1);
				SPhraseSpec phrase2 = typePhrases.get(typePhrases.size() - 1);
				// combine all objects in a coordinated phrase
				CoordinatedPhraseElement combinedLastTwoObjects = nlgFactory
						.createCoordinatedPhrase(phrase1.getObject(), phrase2.getObject());
				combinedLastTwoObjects.setConjunction("assim como");
				combinedLastTwoObjects.setFeature(Feature.RAISE_SPECIFIER, false);
				combinedLastTwoObjects.setFeature(InternalFeature.SPECIFIER, "um"); // fix
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
			typesConjunction.addCoordinate(phrase);
		}

		// convert the other triples
		CoordinatedPhraseElement othersConjunction = nlgFactory.createCoordinatedPhrase();
		List<SPhraseSpec> otherPhrases = convertToPhrases(otherTriples, false, false);
		// we have to keep one triple with subject if we have no type triples
		if (typeTriples.isEmpty()) {
			othersConjunction.addCoordinate(otherPhrases.remove(0));
		}
		// make subject pronominal, i.e. -> he/she/it
		otherPhrases.stream().forEach(p -> asPronoun(p.getSubject()));
		for (SPhraseSpec phrase : otherPhrases) {
			othersConjunction.addCoordinate(phrase);
		}

		List<DocumentElement> sentences = new ArrayList<DocumentElement>();
		if (!typeTriples.isEmpty()) {
			sentences.add(nlgFactory.createSentence(typesConjunction));
		}

		if (!otherTriples.isEmpty()) {
			sentences.add(nlgFactory.createSentence(othersConjunction));
		}

		DocumentElement paragraph = nlgFactory.createParagraph(sentences);
		String realisation = realiser.realise(paragraph).getRealisation().trim();

		return realisation;
	}

	private void asPronoun(NLGElement el) {
		if (el.hasFeature(InternalFeature.SPECIFIER)) {
			NLGElement specifier = el.getFeatureAsElement(InternalFeature.SPECIFIER);
			if (specifier.hasFeature(Feature.POSSESSIVE)) {
				specifier.setFeature(Feature.PRONOMINAL, true);
			}
		} else {
			el.setFeature(Feature.PRONOMINAL, true);
		}
	}

	/**
	 * Convert a triple into a phrase object
	 * 
	 * @param t
	 *            the triple
	 * @return the phrase
	 * @throws IOException
	 */
	public SPhraseSpec convertToPhrase(Triple t, boolean dead, boolean summary, boolean changePronoun)
			throws IOException {
		return convertToPhrase(t, false, dead, summary, changePronoun);
	}

	/**
	 * Convert a triple into a phrase object
	 * 
	 * @param t
	 *            the triple
	 * @return the phrase
	 * @throws IOException
	 */
	public SPhraseSpec convertToPhrase(Triple t, boolean negated, boolean dead, boolean summary, boolean changePronoun)
			throws IOException {
		return convertToPhrase(t, negated, false, dead, summary, changePronoun);
	}

	/**
	 * Convert a triple into a phrase object
	 *
	 * @param t
	 *            the triple
	 * @param negated
	 *            if phrase is negated
	 * @param reverse
	 *            whether subject and object should be changed during
	 *            verbalization
	 * @return the phrase
	 * @throws IOException
	 */
	public SPhraseSpec convertToPhrase(Triple t, boolean negated, boolean reverse, boolean dead, boolean summary,
			boolean changePronoun) throws IOException {
		logger.debug("Verbalizing triple " + t);

		SPhraseSpec p = nlgFactory.createClause();
		Node subject = t.getSubject();
		Node predicate = t.getPredicate();
		Node object = t.getObject();

		// check if object is class
		boolean objectIsClass = predicate.matches(RDF.type.asNode());

		// first get the string representation for the subject
		NLGElement subjectElement = processSubject(subject);

		// then process the object
		NPPhraseSpec objectElement = nlgFactory.createNounPhrase(processObject(object, objectIsClass));
		// handle the predicate
		PropertyVerbalization propertyVerbalization = pp.verbalize(predicate.getURI());
		String predicateAsString = propertyVerbalization.getVerbalizationText();
		// if the object is a class we generate 'SUBJECT be a(n) OBJECT'
		if (objectIsClass) {
			logger.debug("Object is a class");

			p.setSubject(subjectElement);

			p.setVerb("ser");
			
			if (dead == true) { // in case if this a dead person, set PAST as a
								// tense
				logger.debug("dead");
				p.setFeature(PortugueseFeature.TENSE, Tense.PAST);
			}
			Sparql sparql = new Sparql();
			Gender gender = genderDetector.getGender(uriConverter.convert(subject.toString()));
			String genericType = "";
			String specificType = "";
			String artigo = "";
			
			genericType = sparql.mostGenericClass(subject.toString());
			specificType = sparql.mostSpecificClass(subject.toString());

			artigo = setArtigoClass(genericType, specificType, gender);
			
			if(gender.equals(Gender.FEMALE)){
			objectElement.setFeature(simplenlg.features.LexicalFeature.GENDER, simplenlg.features.Gender.FEMININE);}
			
			objectElement.setSpecifier(artigo);
			p.setObject(objectElement);

		} else {
			logger.debug("Object is not a class");
			// get the lexicalization type of the predicate

			PropertyVerbalizationType type;
			if (predicate.matches(RDFS.label.asNode())) {
				type = PropertyVerbalizationType.NOUN;
			} else {
				type = propertyVerbalization.getVerbalizationType();
			}

			/*-
			 * if the predicate is a noun we generate a possessive form, i.e. 'article + PREDICATE + SUBJECT  be OBJECT'
			 */
			if (type == PropertyVerbalizationType.NOUN) {
				logger.debug("Type is NOUN");

				TreeTagger treetagger;
				NPPhraseSpec predicateNounPhrase = null;
				String predicado;
				try {
					treetagger = new TreeTagger();
					predicado = treetagger.getLemma(predicateAsString);
					logger.debug("predicado" + predicado);
					if (predicado.contains("de+a")) {
						predicado = predicado.replace("de+a", "da");
					} else if (predicado.contains("de+o")) {
						predicado = predicado.replace("de+o", "do");
					}
					predicateNounPhrase = nlgFactory.createNounPhrase(predicateAsString);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (summary == true) {

					if (changePronoun == false) { // use "dele" as a possessive form

						predicateNounPhrase.setFeature(InternalFeature.POSTMODIFIERS, subjectElement); // insert
																										// the
																										// subject
																										// as
																										// possessive
																										// like
																										// "dele"
																										// and
																										// "dela"

						// check if object is a string literal with a language
						// tag
						if (considerLiteralLanguage) {
							if (object.isLiteral() && object.getLiteralLanguage() != null
									&& !object.getLiteralLanguage().isEmpty()) {
								String languageTag = object.getLiteralLanguage();
								String language = Locale.forLanguageTag(languageTag).getDisplayLanguage(Locale.ROOT);
								predicateNounPhrase.addPreModifier(language);
							}
						}
						// ------------------------------------------------------------------------------------
						
						predicateNounPhrase.setSpecifier(setArtigoPredicateNoun(predicateAsString.toString()));
						
						p.setSubject(predicateNounPhrase);

						// we use 'be' as the new predicate

						logger.debug("URI: " + subject.toString());

						p.setVerb("ser");
						if (dead == true) {
							p.setFeature(PortugueseFeature.TENSE, Tense.PAST);
						}
						// ------------------------------------------------------------------------------------
						// add object

						String objectAsString = objectElement.getHead().toString().substring(12).replace(":NOUN]", "");
						
						objectElement.setSpecifier(setArtigoObject(objectAsString));

						p.setObject(objectElement);
						// ------------------------------------------------------------------------------------
						// check if we have to use the plural form
						// simple heuristic: OBJECT is variable and predicate is
						// of type
						// owl:FunctionalProperty or rdfs:range is xsd:boolean
						boolean isPlural = determinePluralForm && usePluralForm(t);
						predicateNounPhrase.setPlural(isPlural);
						p.setPlural(isPlural);

						// check if we reverse the triple representation
						if (reverse) {
							subjectElement.setFeature(Feature.POSSESSIVE, false);
							p.setSubject(subjectElement);
							p.setVerbPhrase(nlgFactory.createVerbPhrase("be " + predicateAsString + " of"));
							p.setObject(objectElement);
						}
					} else { // use "seu" as a possive form

						predicado = predicateAsString.toString();
						List<String> input = Arrays.asList(predicado.split(" "));
						String tag = null;
						try {
							treetagger = new TreeTagger();
							tag = treetagger.tag(input.get(0).toString());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						logger.debug("Predicado: " + predicado + " Primeiro token: " + input.get(0).toString()
								+ " Tag: " + tag.toString());

						if (tag.contains("NCM")) {
							predicateNounPhrase.setSpecifier("seu");
						} else if (tag.contains("NCF")) {
							predicateNounPhrase.setSpecifier("sua");
						} else {
							predicateNounPhrase.setSpecifier("seu");
						}
						;

						p.setSubject(predicateNounPhrase);

						// we use 'be' as the new predicate

						logger.debug("URI: " + subject.toString());

						p.setVerb("ser");
						if (dead == true) {
							p.setFeature(PortugueseFeature.TENSE, Tense.PAST);
						}
						// ------------------------------------------------------------------------------------
						// add object

						String objectAsString = objectElement.getHead().toString().substring(12).replace(":NOUN]", "");
						
						objectElement.setSpecifier(setArtigoObject(objectAsString));

						p.setObject(objectElement);
						// ------------------------------------------------------------------------------------
						// check if we have to use the plural form
						// simple heuristic: OBJECT is variable and predicate is
						// of type
						// owl:FunctionalProperty or rdfs:range is xsd:boolean
						boolean isPlural = determinePluralForm && usePluralForm(t);
						predicateNounPhrase.setPlural(isPlural);
						p.setPlural(isPlural);

						// check if we reverse the triple representation
						if (reverse) {
							subjectElement.setFeature(Feature.POSSESSIVE, false);
							p.setSubject(subjectElement);
							p.setVerbPhrase(nlgFactory.createVerbPhrase("be " + predicateAsString + " of"));
							p.setObject(objectElement);
						}

					}
				} else {

					// set the possessive subject as specifier
					predicateNounPhrase.addComplement("de"); // insert the
																// possive form.
					predicateNounPhrase.setFeature(InternalFeature.POSTMODIFIERS, subjectElement); // insert
																									// the
																									// subject
																									// here.

					// check if object is a string literal with a language tag
					if (considerLiteralLanguage) {
						if (object.isLiteral() && object.getLiteralLanguage() != null
								&& !object.getLiteralLanguage().isEmpty()) {
							String languageTag = object.getLiteralLanguage();
							String language = Locale.forLanguageTag(languageTag).getDisplayLanguage(Locale.ROOT);
							predicateNounPhrase.addPreModifier(language);
						}
					}

					predicateNounPhrase.setSpecifier(setArtigoPredicateNoun(predicateAsString.toString()));
					
					p.setSubject(predicateNounPhrase);

					// we use 'be' as the new predicate

					logger.debug("URI: " + subject.toString());

					p.setVerb("ser");
					if (dead == true) {
						p.setFeature(PortugueseFeature.TENSE, Tense.PAST);
					}
					// ------------------------------------------------------------------------------------
					// add object
					String objectAsString = objectElement.getHead().toString().substring(12).replace(":NOUN]", "");
					
					objectElement.setSpecifier(setArtigoObject(objectAsString));

					p.setObject(objectElement);
					// ------------------------------------------------------------------------------------
					// check if we have to use the plural form
					// simple heuristic: OBJECT is variable and predicate is of
					// type
					// owl:FunctionalProperty or rdfs:range is xsd:boolean
					boolean isPlural = determinePluralForm && usePluralForm(t);
					predicateNounPhrase.setPlural(isPlural);
					p.setPlural(isPlural);

					// check if we reverse the triple representation
					if (reverse) {
						subjectElement.setFeature(Feature.POSSESSIVE, false);
						p.setSubject(subjectElement);
						p.setVerbPhrase(nlgFactory.createVerbPhrase("be " + predicateAsString + " of"));
						p.setObject(objectElement);
					}
				}
			} // if the predicate is a verb
			else if (type == PropertyVerbalizationType.VERB) {
				if (subject.toString().contains("http://sparql2nl.aksw.org/placeHolder/dele")) {
					p.setSubject("ele");
				} else if (subject.toString().contains("http://sparql2nl.aksw.org/placeHolder/dela")) {
					p.setSubject("ela");
				}

				String pattern[] = new String[3];
				pattern = hasPattern(predicate.getURI());

				if (pattern[0] != null) {
					p.setVerb(pattern[0]);

					PPPhraseSpec generalizedLocation = nlgFactory.createPrepositionPhrase();
					NPPhraseSpec relatum = nlgFactory.createNounPhrase(objectElement);

					relatum.setSpecifier(setArtigoObject(object.toString()));
				
					generalizedLocation.addComplement(relatum);
					generalizedLocation.setPreposition(pattern[1]);

					p.addComplement(generalizedLocation);

					if (dead == true) {
						p.setFeature(PortugueseFeature.TENSE, Tense.PAST);
					} else {
						if (pattern[0].equals("nascer")) {
							p.setFeature(PortugueseFeature.TENSE, Tense.PAST);
						} else {
							p.setFeature(PortugueseFeature.TENSE, propertyVerbalization.getTense());
						}
					}
					// p.setObject(objectElement);

				} else {

					String aux = pp.getInfinitiveForm(predicateAsString);
					if (aux.contains("ser")) {
						aux = aux.substring(3, aux.length());
						p.setVerb("ser");
						
						List<String> inputObject = Arrays.asList(aux.split(" "));
						
						p.addPreModifier(inputObject.get(1).trim());
						
						NPPhraseSpec relatum = nlgFactory.createNounPhrase(objectElement);
						PPPhraseSpec generalizedLocation = nlgFactory.createPrepositionPhrase();
	
						relatum.setSpecifier(setArtigoObject(objectElement.getHead().toString().substring(12).replace(":NOUN]", "")));
	
						generalizedLocation.setPreposition(inputObject.get(2).trim());
						generalizedLocation.addComplement(relatum);
						
						p.addComplement(generalizedLocation);
					} else {
						p.setVerb(aux);
						p.addComplement(objectElement);
					}
					
					p.setFeature(PortugueseFeature.TENSE, propertyVerbalization.getTense());
				}
			} 
		}
		// }
		// check if the meaning of the triple is it's negation, which holds for
		// boolean properties with FALSE as value
		if (!negated) {
			// check if object is boolean literal
			if (object.isLiteral() && object.getLiteralDatatype() != null
					&& object.getLiteralDatatype().equals(XSDDatatype.XSDboolean)) {
				// omit the object
				p.setObject(null);

				negated = !(boolean) object.getLiteralValue();

			}
		}

		// set negation
		if (negated) {
			p.setFeature(Feature.NEGATED, negated);
		}

		// set present time as tense
		// p.setFeature(Feature.TENSE, Tense.PRESENT);
		// System.out.println(realiser.realise(p));
		return p;
	}

	private String setArtigoObject(String objectAsString) {
		List<String> inputObject = Arrays.asList(objectAsString.split(" "));
		String tagObject = "";
		try {
			TreeTagger treetagger = new TreeTagger();
			tagObject = treetagger.tag(inputObject.get(0).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (tagObject.contains("M")) {
			if (tagObject.contains("P")) {
			return "os";	
			} else {
			return "o";
			}
		} else if (tagObject.contains("F")) {
			if (tagObject.contains("P")) {
				return "as";	
				} else {
				return "a";
				}
//		} else if (tagObject.contains("Z")) {
//			return "de";
		} else {
			return "";
		}
	}

	private String setArtigoPredicateNoun(String predicado) {
		TreeTagger treetagger;
		
		List<String> input = Arrays.asList(predicado.split(" "));
		String tag = null;
		try {
			treetagger = new TreeTagger();
			tag = treetagger.tag(input.get(0).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.debug("Predicado: " + predicado + " Primeiro token: " + input.get(0).toString()
				+ " Tag: " + tag.toString());

		if (tag.contains("NCM")) {
			if (tag.contains("P")) {
				return "os";	
				} else {
				return "o";
				}
		} else if (tag.contains("NCF")) {
			if (tag.contains("P")) {
				return "as";	
				} else {
				return "a";
				}
		} else {
			return "o";
		}
	}

	private String setArtigoClass(String genericType, String specificType, Gender gender) {
		if (genericType.equals("http://dbpedia.org/ontology/Person")) {
			if (gender.name().equals("FEMALE")) {
				return "uma";
			} else {
				return "um";
			}
		} else if (genericType.equals("http://dbpedia.org/ontology/Organisation")) {
			if (specificType.equals("http://dbpedia.org/ontology/SoccerClub")) {
				return "um";
			} else {
				return "uma";
			}
		} else if (genericType.equals("http://dbpedia.org/ontology/PopulatedPlace")) {
			if (specificType.equals("http://dbpedia.org/ontology/City")) {
				return "uma";
			} else {
				return "um";
			}
		} else if (genericType.equals("http://dbpedia.org/ontology/ArchitecturalStructure")) {
			if (specificType.equals("http://dbpedia.org/ontology/Church")) {
				return "uma";
			} else {
				return "um";
			}
		} else if (genericType.equals("")) {
			if (specificType.equals("http://dbpedia.org/ontology/University")) {
				return "uma";
			} else {
				return "um";
			}
		} else {
			return "um";
		}
	}

	/**
	 * Converts a collection of triples into a list of phrases.
	 *
	 * @param triples
	 *            the triples
	 * @return a list of phrases
	 * @throws IOException
	 */
	public List<SPhraseSpec> convertToPhrases(Collection<Triple> triples, boolean dead, boolean summary)
			throws IOException {
		List<SPhraseSpec> phrases = new ArrayList<>();
		boolean changePronoun = false;
		int sizeOfTriples = triples.size();
		// System.out.println("triples size" + sizeOfTriples);
		int count = 0;
		for (Triple triple : triples) {
			// System.out.println("count of triples" + count);
			if (sizeOfTriples > 2 && count == 2 || count == 1) {
				// System.out.println("usar o seu");
				changePronoun = true;
				phrases.add(convertToPhrase(triple, dead, summary, changePronoun));
			} else {
				// System.out.println("usar o dele");
				phrases.add(convertToPhrase(triple, dead, summary, changePronoun));
			}
			changePronoun = false;
			count++;
		}
		return phrases;
	}

	/**
	 * Whether to encapsulate the value of string literals in "".
	 * {@see LiteralConverter#setEncapsulateStringLiterals(boolean)}
	 * 
	 * @param encapsulateStringLiterals
	 *            TRUE if string has to be wrapped in "", otherwise FALSE
	 */
	public void setEncapsulateStringLiterals(boolean encapsulateStringLiterals) {
		this.literalConverter.setEncapsulateStringLiterals(encapsulateStringLiterals);
	}

	/**
	 * @param determinePluralForm
	 *            the determinePluralForm to set
	 */
	public void setDeterminePluralForm(boolean determinePluralForm) {
		this.determinePluralForm = determinePluralForm;
	}

	/**
	 * @param considerLiteralLanguage
	 *            the considerLiteralLanguage to set
	 */
	public void setConsiderLiteralLanguage(boolean considerLiteralLanguage) {
		this.considerLiteralLanguage = considerLiteralLanguage;
	}

	private boolean usePluralForm(Triple triple) {
		return triple.getObject().isVariable()
				&& !(reasoner.isFunctional(new OWLObjectPropertyImpl(IRI.create(triple.getPredicate().getURI())))
						|| reasoner.getRange(new OWLDataPropertyImpl(IRI.create(triple.getPredicate().getURI())))
								.asOWLDatatype().getIRI().equals(OWL2Datatype.XSD_BOOLEAN.getIRI()));
	}

	/**
	 * @param returnAsSentence
	 *            whether the style of the returned result is a proper English
	 *            sentence or just a phrase
	 */
	public void setReturnAsSentence(boolean returnAsSentence) {
		this.returnAsSentence = returnAsSentence;
	}

	/**
	 * @param useGenderInformation
	 *            whether to use the gender information about a resource
	 */
	public void setUseGenderInformation(boolean useGenderInformation) {
		this.useGenderInformation = useGenderInformation;
	}

	public void setGenderDetector(GenderDetector genderDetector) {
		this.genderDetector = genderDetector;
	}

	/**
	 * Process the node and return an NLG element that contains the textual
	 * representation. The output depends on the node type, i.e. variable, URI
	 * or literal.
	 * 
	 * @param node
	 *            the node to process
	 * @return the NLG element containing the textual representation of the node
	 * @throws IOException
	 */
	public NLGElement processNode(Node node) throws IOException {
		NLGElement element;
		if (node.isVariable()) {
			element = processVarNode(node);
		} else if (node.isURI()) {
			element = processResourceNode(node);
		} else if (node.isLiteral()) {
			element = processLiteralNode(node);
		} else {
			throw new UnsupportedOperationException("Can not convert blank node.");
		}
		return element;
	}

	/**
	 * Converts the node that is supposed to represent a class in the knowledge
	 * base into an NL phrase.
	 * 
	 * @param node
	 *            the node
	 * @param plural
	 *            whether the plural form should be used
	 * @return the NL phrase
	 */
	public NPPhraseSpec processClassNode(Node node, boolean plural) {
		NPPhraseSpec object;
		if (node.equals(OWL.Thing.asNode())) {
			object = nlgFactory.createNounPhrase(GenericType.ENTITY.getNlr());
		} else if (node.equals(RDFS.Literal.asNode())) {
			object = nlgFactory.createNounPhrase(GenericType.VALUE.getNlr());
		} else if (node.equals(RDF.Property.asNode())) {
			object = nlgFactory.createNounPhrase(GenericType.RELATION.getNlr());
		} else if (node.equals(RDF.type.asNode())) {
			object = nlgFactory.createNounPhrase(GenericType.TYPE.getNlr());
		} else {
			String label = uriConverter.convert(node.getURI());
			if (label != null) {
				// get the singular form
				// label = PlingStemmer.stem(label);
				// we assume that classes are always used in lower case format
				label = label.toLowerCase();
				object = nlgFactory.createNounPhrase(nlgFactory.createWord(label, LexicalCategory.NOUN));
			} else {
				object = nlgFactory.createNounPhrase(GenericType.ENTITY.getNlr());
			}

		}
		// set plural form
		object.setPlural(plural);
		return object;
	}

	public NPPhraseSpec processVarNode(Node varNode) {
		return nlgFactory.createNounPhrase(nlgFactory.createWord(varNode.toString(), LexicalCategory.NOUN));
	}

	public NPPhraseSpec processLiteralNode(Node node) throws IOException {
		LiteralLabel lit = node.getLiteral();
		String tag = "";
		// convert the literal
		String literalText = literalConverter.convert(lit);
		literalText = literalText.replace('"', ' ');
		literalText = literalText.replace('*', ' ');
		literalText = literalText.trim();
		Gtranslate http = new Gtranslate();
		try {
			literalText = http.callUrlAndParseResult("auto", "pt", literalText);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			TreeTagger treetagger = new TreeTagger();
			tag = treetagger.tag(literalText);

			if (!tag.contains("NC")) {
				literalText = literalText.toLowerCase();
			}
		} catch (Exception e) {

		}

		NPPhraseSpec np = nlgFactory.createNounPhrase(nlgFactory.createWord(literalText, LexicalCategory.NOUN));
		np.setPlural(literalConverter.isPlural(lit));

		return np;
	}

	public NPPhraseSpec processResourceNode(Node node) {
		// get string from URI
		String s = uriConverter.convert(node.getURI());
		s = s.replace('"', ' ');
		s = s.trim();

		Gtranslate http = new Gtranslate();
		try {
			s = http.callUrlAndParseResult("auto", "pt", s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			TreeTagger treetagger = new TreeTagger();
			String tag = treetagger.tag(s);
			if (!tag.contains("NC")) {
				s = s.toLowerCase();
			}
		} catch (Exception e) {

		}
		// System.out.println(word);
		// create word
		NLGElement word = nlgFactory.createWord(s, LexicalCategory.NOUN);

		// add gender information if enabled
		if (useGenderInformation) {
			Gender gender = genderDetector.getGender(s);

			if (gender == Gender.FEMALE) {
				word.setFeature(LexicalFeature.GENDER, simplenlg.features.Gender.FEMININE);
			} else if (gender == Gender.MALE) {
				word.setFeature(LexicalFeature.GENDER, simplenlg.features.Gender.MASCULINE);
			}
		}

		// should be a proper noun, thus, will not be pluralized by morphology
		word.setFeature(LexicalFeature.PROPER, true);

		// wrap in NP
		NPPhraseSpec np = nlgFactory.createNounPhrase(word);
		return np;
	}

	private NLGElement processSubject(Node subject) throws IOException {
		NLGElement element;
		if (subject.isVariable()) {
			element = processVarNode(subject);
		} else if (subject.isURI()) {
			element = processResourceNode(subject);
		} else if (subject.isLiteral()) {
			element = processLiteralNode(subject);
		} else {
			throw new UnsupportedOperationException("Can not convert " + subject);
		}
		return element;
	}

	private NPPhraseSpec processObject(Node object, boolean isClass) throws IOException {
		NPPhraseSpec element;
		if (object.isVariable()) {
			element = processVarNode(object);
		} else if (object.isLiteral()) {
			element = processLiteralNode(object);
		} else if (object.isURI()) {
			if (isClass) {
				element = processClassNode(object, false);
			} else {
				element = processResourceNode(object);
			}
		} else {
			throw new IllegalArgumentException("Can not convert blank node " + object + ".");
		}
		return element;
	}

	/**
	 * Takes a URI and returns a noun phrase for it
	 * 
	 * @param uri
	 *            the URI to convert
	 * @param plural
	 *            whether it is in plural form
	 * @param isClass
	 *            if URI is supposed to be a class
	 * @return the noun phrase
	 */
	public NPPhraseSpec getNPPhrase(String uri, boolean plural, boolean isClass) {
		NPPhraseSpec object;
		if (uri.equals(OWL.Thing.getURI())) {
			object = nlgFactory.createNounPhrase(GenericType.ENTITY.getNlr());
		} else if (uri.equals(RDFS.Literal.getURI())) {
			object = nlgFactory.createNounPhrase(GenericType.VALUE.getNlr());
		} else if (uri.equals(RDF.Property.getURI())) {
			object = nlgFactory.createNounPhrase(GenericType.RELATION.getNlr());
		} else if (uri.equals(RDF.type.getURI())) {
			object = nlgFactory.createNounPhrase(GenericType.TYPE.getNlr());
		} else {
			String label = uriConverter.convert(uri);
			if (label != null) {
				if (isClass) {
					// get the singular form
					label = PlingStemmer.stem(label);
					// we assume that classes are always used in lower case
					// format
					label = label.toLowerCase();
				}
				object = nlgFactory.createNounPhrase(nlgFactory.createWord(label, LexicalCategory.NOUN));
			} else {
				object = nlgFactory.createNounPhrase(GenericType.ENTITY.getNlr());
			}

		}
		object.setPlural(plural);

		return object;
	}

	private String[] hasPattern(String uri) {

		String[] pattern = new String[3];
		;
		String csvFile = "/Users/diegomoussallem/Documents/workspace/sw2pt/src/main/resources/patterns.csv";

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		List<String> lines = new ArrayList<>();
		List<String> uris = new ArrayList<>();
		List<String> triplas = new ArrayList<>();
		String[] resource = new String[3];
		int i = 0;

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				resource = line.split(cvsSplitBy);

				if (uri.equals(resource[0])) {
					pattern[0] = resource[1];
					pattern[1] = resource[2];
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return pattern;
	}

	// public static void main(String[] args) throws Exception {
	// // http://protege.stanford.edu/plugins/owl/owl-library/koala.owl#Felix
	// http://protege.stanford.edu/plugins/owl/owl-library/koala.owl#isHardWorking
	// ""false""^^http://www.w3.org/2001/XMLSchema#boolean"
	// System.out.println(new TripleConverterPortuguese().convert(
	// Triple.create(
	// NodeFactory.createURI("http://dbpedia.org/resource/Albert_Einstein"),
	// NodeFactory.createURI("http://dbpedia.org/ontology/birthPlace"),
	// NodeFactory.createURI("http://dbpedia.org/resource/Ulm")
	// )));
	//
	// System.out.println(new TripleConverterPortuguese().convert(
	// Triple.create(
	// NodeFactory.createURI("http://dbpedia.org/resource/Brazil"),
	// NodeFactory.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
	// NodeFactory.createURI("http://dbpedia.org/resource/Country")
	// )));
	//
	// System.out.println(new TripleConverterPortuguese().convert(
	// Triple.create(
	// NodeFactory.createURI("http://dbpedia.org/resource/Albert_Einstein"),
	// NodeFactory.createURI("http://dbpedia.org/ontology/isHardWorking"),
	// NodeFactory.createLiteral("false", XSDDatatype.XSDboolean)
	// )));
	// }
}
