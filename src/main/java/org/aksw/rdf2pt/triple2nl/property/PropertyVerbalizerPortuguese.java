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
package org.aksw.rdf2pt.triple2nl.property;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.aksw.jena_sparql_api.core.QueryExecutionFactory;
import org.aksw.jena_sparql_api.http.QueryExecutionFactoryHttp;
import org.aksw.rdf2pt.Sparql;
import org.aksw.rdf2pt.triple2nl.DocumentGeneratorPortuguese;
import org.aksw.rdf2pt.triple2nl.converter.DefaultIRIConverterPortuguese;
import org.aksw.rdf2pt.triple2nl.converter.IRIConverter;
import org.aksw.rdf2pt.triple2nl.util.Preposition;
import org.aksw.rdf2pt.utils.nlp.pos.TreeTagger;
import org.apache.jena.graph.Triple;
import org.apache.log4j.Logger;
import org.dllearner.kb.sparql.SparqlEndpoint;

//import edu.stanford.nlp.ling.CoreAnnotations.*;
//import edu.stanford.nlp.ling.CoreLabel;
//import edu.stanford.nlp.pipeline.Annotation;
//import edu.stanford.nlp.pipeline.StanfordCoreNLP;
//import edu.stanford.nlp.trees.Tree;
//import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
//import edu.stanford.nlp.util.CoreMap;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.dictionary.Dictionary;
import simplenlg.features.Tense;

/**
 * Verbalize a property.
 * @author Diego Moussallem
 *
 */
public class PropertyVerbalizerPortuguese {
	
    private static final Logger logger = Logger.getLogger(PropertyVerbalizerPortuguese.class);
    
    private double threshold = 2.0;
    private Preposition preposition;
    private Dictionary database;
    private TreeTagger treetagger;
    
   // private final String VERB_PATTERN = "^((VP)|(have NP)|(be NP P)|(be VP P)|(VP NP)).*";
	//private StanfordCoreNLPWrapper pipeline;
	//private boolean useLinguisticalAnalysis = true;
	
	//private final List<String> auxiliaryVerbs = Lists.newArrayList("do", "have", "be", "shall", "can", "may");

	private IRIConverter uriConverter;

	public PropertyVerbalizerPortuguese(QueryExecutionFactory qef, String cacheDirectory, Dictionary wordnetDictionary) {
		this(new DefaultIRIConverterPortuguese(qef, cacheDirectory), wordnetDictionary);
	}
	
    public PropertyVerbalizerPortuguese(IRIConverter uriConverter, Dictionary wordnetDictionary) {
        this.uriConverter = uriConverter;
        try {
			this.database = wordnetDictionary == null ? Dictionary.getDefaultResourceInstance() : wordnetDictionary;
		} catch (JWNLException e) {
			throw new RuntimeException("Failed to create WordNet instance.", e);
		}
        
        preposition = new Preposition();
        try {
			treetagger = new TreeTagger();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, parse");
		props.put("ssplit.isOneSentence","true");
		//pipeline = new StanfordCoreNLPWrapper(new StanfordCoreNLP(props));
//		pipeline = new StanfordCoreNLPWrapper(new StanfordCoreNLPClient(props, "titan.informatik.uni-leipzig.de", 9000));
    }
    
    public PropertyVerbalization verbalize(String propertyURI){
    	logger.debug("Getting lexicalization type for \"" + propertyURI + "\"...");
    	
		// get textual representation for the property URI
		String propertyText = uriConverter.convert(propertyURI);

		// normalize the text, e.g. to lower case
		propertyText = normalize(propertyText);
		//propertyText = propertyText.replace('"', ' ');
    	
    	// try to use linguistic information
		PropertyVerbalizationType verbalizationType = PropertyVerbalizationType.UNSPECIFIED;
		PropertyVerbalization propertyVerbalization = new PropertyVerbalization(propertyURI, propertyText, verbalizationType);
		
		//if(useLinguisticalAnalysis){
    	//propertyVerbalization = getTypeByLinguisticAnalysis(propertyURI, propertyText);} 

    	if(propertyVerbalization.getVerbalizationType() == PropertyVerbalizationType.UNSPECIFIED || propertyText.split(" ").length == 1) {
    			logger.debug("...using Treetagger analysis...");
    			verbalizationType = getTypeByLinguisticAnalysisTreeTagger(propertyText);
    			propertyVerbalization.setVerbalizationType(verbalizationType);
    	}
    	
    	
    	boolean has = hasPattern(propertyURI);
    	if(has==true){
    		propertyVerbalization.setVerbalizationType(PropertyVerbalizationType.VERB);
    	}
 
    	
    	// if this failed use WordNet heuristic
//		if(propertyVerbalization.getVerbalizationType() == PropertyVerbalizationType.UNSPECIFIED || propertyText.split(" ").length == 1) {
//			logger.info("...using WordNet-based analysis...");
//			PropertyVerbalizationType verbalizationType = getTypeByWordnet(propertyText);
//			propertyVerbalization.setVerbalizationType(verbalizationType);
//		}
		

    	// compute expanded form
    	//computeExpandedVerbalization(propertyVerbalization);

		// determine tense
		Tense tense = getTense(propertyText);
		propertyVerbalization.setTense(tense);

		logger.debug("Done.");
    	
    	return propertyVerbalization;
    }
    
	/**
	 * Determine the verbalization type of a property, i.e. whether it is a verb
	 * or a noun, by using WordNet statistics.
	 * 
	 * @param property the property
	 * @return the type of verbalization
	 */
    public PropertyVerbalizationType getTypeByWordnet(String property){
    	 //length is > 1
        if (property.contains(" ")) {
            String split[] = property.split(" ");
            String lastToken = split[split.length - 1];
            //first check if the ending is a preposition
            //if yes, then the type is that of the first word
            if (preposition.isPreposition(lastToken)) {
            	String firstToken = split[0];
                if (getTypeByWordnet(firstToken) == PropertyVerbalizationType.NOUN) {
                    return PropertyVerbalizationType.NOUN;
                } else if (getTypeByWordnet(firstToken) == PropertyVerbalizationType.VERB) {
                    return PropertyVerbalizationType.VERB;
                }
            }
            if (getTypeByWordnet(lastToken) == PropertyVerbalizationType.NOUN) {
                return PropertyVerbalizationType.NOUN;
            } else if (getTypeByWordnet(split[0]) == PropertyVerbalizationType.VERB) {
                return PropertyVerbalizationType.VERB;
            } else {
                return PropertyVerbalizationType.NOUN;
            }
        } else {
            double score = getScore(property);
			if (score < 0) {// some count did not work
				return PropertyVerbalizationType.UNSPECIFIED;
			}
			if (score >= threshold) {
				return PropertyVerbalizationType.NOUN;
			} else if (score < 1 / threshold) {
				return PropertyVerbalizationType.VERB;
			} else {
				return PropertyVerbalizationType.NOUN;
			}
        }
    }
    
    public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
    
    /**
     * Whether to apply an analysis based in linguistic features in addition to WordNet.
	 * @param useLinguisticalAnalysis the useLinguisticalAnalysis to set
	 */
//	public void setUseLinguisticalAnalysis(boolean useLinguisticalAnalysis) {
//		this.useLinguisticalAnalysis = useLinguisticalAnalysis;
//	}

    /**
     * Returns log(nounCount/verbCount), i.e., positive for noun, negative for
     * verb
     *
     * @param token Input token
     * @return "Typicity"
     */
    public double getScore(String token) {
        logger.debug("Checking " + token);
        
        double nounCount = 0;
        double verbCount = 0;
        
        List<Synset> synsets;
        
        try {
			// number of occurrences as noun
			IndexWord iw = database.lookupIndexWord(POS.NOUN, token);
			if(iw != null) {
				synsets = iw.getSenses();
				
				for (Synset synset : synsets) {
					List<Word> words = synset.getWords();
					
					for (Word word : words) {//System.out.println(s[j] + ":" + synsets[i].getTagCount(s[j]));
						nounCount += Math.log(word.getUseCount() + 1.0);
					}
				}
			}
			

			// number of occurrences as verb
			iw = database.lookupIndexWord(POS.VERB, token); // does morphological processing, otherwise use getIndexWord
			if(iw != null) {
				synsets = iw.getSenses();
				for (Synset synset : synsets) {
					List<Word> words = synset.getWords();
					
					for (Word word : words) {//System.out.println(s[j] + ":" + synsets[i].getTagCount(s[j]));
						verbCount += Math.log(word.getUseCount() + 1.0);
					}
				}
			}
			
			logger.debug("Noun count = "+nounCount);
			logger.debug("Verb count = "+verbCount);
		} catch (JWNLException e) {
			logger.error("WordNet lookup failed.", e);
		}

        if (verbCount == 0 && nounCount == 0) {
            return 1.0;
        }
        if (verbCount == 0) {
            return Double.MAX_VALUE;
        }
        if (nounCount == 0) {
            return 0.0;
        } else {
            return nounCount / verbCount;
        }
    }

	/**
	 * Returns the infinitive form for a given word.
	 * 
	 * @param word the word
	 * @return the infinitive form
	 */
    public String getInfinitiveForm(String word) {
    	//System.out.println("Word: " + word);
        String[] split = word.split(" ");
        String verb = split[0];

        if(verb.endsWith("ed") && split.length == 1) { 
        	// probably past tense
        	
        } else if (verb.endsWith("do")) { // termina com participio
        	
        	// check for past construction that simply need an auxiliary
        	return "ser " + word;
        }

        try {
			IndexWord iw = database.getIndexWord(POS.VERB, word);
			if(iw != null) {
				List<Synset> synsets = iw.getSenses();
				//double min = verb.length();
				String result = verb;
				for (Synset synset : synsets) {
				    for (Word w : synset.getWords()) {
				        if (verb.contains(w.getLemma())) {
				            result = w.getLemma();
				            if (split.length > 1) {
				                for (int k = 1; k < split.length; k++) {
				                    result = result + " " + split[k];
				                }
				            }
				            return result;
				        }
				    }
				}
			}
		} catch (JWNLException e) {
			logger.error("WordNet lookup failed.", e);
		}
        return word;
    }

	private Tense getTense(String word) {
		String[] split = word.split(" ");
		String verb = split[0];

		if(verb.endsWith("ed") && split.length == 1) {
			// probably past tense
			return Tense.PAST;
		} else {
			return Tense.PRESENT;
		}
	}
	
	private boolean hasPattern(String uri){
		
		boolean has = false;
		
		 String csvFile = "/Users/diegomoussallem/Documents/workspace/RDF2PT/src/main/resources/patterns.csv";

	        BufferedReader br = null;
	        String line = "";
	        String cvsSplitBy = ",";
	        List<String> lines = new ArrayList<>();
	        List<String> uris = new ArrayList<>();
	        List<String> triplas = new ArrayList<>();
	        int i = 0;
	        
	        try {

	            br = new BufferedReader(new FileReader(csvFile));
	            while ((line = br.readLine()) != null) {

	                // use comma as separator
	                String[] resource = line.split(cvsSplitBy);

	        		uris.add(resource[0]);
	        		
	            }
	            
	            if(uris.contains(uri)){
	            	return true;
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
		
		return has;
	}
    
//	private PropertyVerbalization getTypeByLinguisticAnalysis(String propertyURI, String propertyText) {
//		logger.info("...using linguistical analysis...");
//		Annotation document = new Annotation(propertyText);
//		pipeline.annotate(document);
//		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
//
//		String pattern = "";
//		PropertyVerbalizationType verbalizationType = PropertyVerbalizationType.UNSPECIFIED;
//		boolean firstTokenAuxiliary = false;
//		for (CoreMap sentence : sentences) {
//			List<CoreLabel> tokens = sentence.get(TokensAnnotation.class);
//			//get the first word and check if it's 'is' or 'has'
//			CoreLabel token = tokens.get(0);
//			String word = token.get(TextAnnotation.class);
//			String pos = token.get(PartOfSpeechAnnotation.class);
//			String lemma = token.getString(LemmaAnnotation.class);
//			
//			firstTokenAuxiliary = auxiliaryVerbs.contains(lemma);
//			
//			if(lemma.equals("be") || word.equals("have")){
//				pattern += lemma.toUpperCase();
//			} else {
//				if(pos.startsWith("N")){
//					pattern += "NP";
//				} else if(pos.startsWith("V")){
//					pattern += "VP";
//				} else {
//					pattern += pos;
//				}
//			}
//			if(tokens.size() > 1){
//				pattern += " ";
//				for (int i = 1; i < tokens.size(); i++) {
//					token = tokens.get(i);
//					pos = token.get(PartOfSpeechAnnotation.class);
//					if(pos.startsWith("N")){
//						pattern += "NP";
//					} else if(pos.startsWith("V")){
//						pattern += "VP";
//					} else {
//						pattern += pos;
//					}
//					pattern += " ";
//				}
//			}
//			//get the parse tree
//			Tree tree = sentence.get(TreeAnnotation.class);
//			//skip ROOT tag
//			tree = tree.skipRoot();
//			logger.debug("Parse tree:" + tree.pennString());
////			tree.pennPrint();
//			//check if VP is directly followed by NP
//			//sometimes parent node is S,SINV,etc.
//			if(tree.value().matches(Joiner.on('|').join(Lists.newArrayList(S, SBAR, SBARQ, SINV, FRAGMENT)))){
//				tree = tree.getChild(0);
//			}
//			boolean useDeterminer = false;
//			if(tree.value().equals(VERB_PHRASE.getTag())){
//				for (Tree child : tree.getChildrenAsList()) {
//					//check if first non terminal is NP and not contains a determiner
//					if(!child.isPreTerminal()){
//						if(child.value().equals(NOUN_PHRASE.getTag()) && !child.getChild(0).value().equals(DETERMINER.getTag())){
//							useDeterminer = true;
//						} 
//						break;
//					}
//				}
//			}
//			// add determiner tag
//			if(useDeterminer) {
//				String[] split = pattern.split(" ");
//				pattern = split[0] + " DET " + Joiner.on(" ").join(Arrays.copyOfRange(split, 1, split.length));
//			}
//		}
//		pattern = pattern.trim();
//		
//		//if first token is an auxiliary can return verb
//		if(firstTokenAuxiliary){
//			verbalizationType = PropertyVerbalizationType.VERB;
//		}
//		
//		//check if pattern matches
//		if(pattern.matches(VERB_PATTERN)){
//			logger.debug("...successfully determined type.");
//			verbalizationType = PropertyVerbalizationType.VERB;
//		} 
//		return new PropertyVerbalization(propertyURI, propertyText, pattern, verbalizationType);
//	}
	
	private PropertyVerbalizationType getTypeByLinguisticAnalysisTreeTagger(String propertyText) {
	    	 //length is > 1
		//System.out.println("PropertyText: " + propertyText);
	        if (propertyText.contains(" ")) {
	            String split[] = propertyText.split(" ");
	            String lastToken = split[split.length - 1];
	            //first check if the ending is a preposition
	            //if yes, then the type is that of the first word
	            if (preposition.isPreposition(lastToken)) {
	            	String firstToken = split[0];
	                if (getTypeByLinguisticAnalysisTreeTagger(firstToken) == PropertyVerbalizationType.NOUN) {
	                    return PropertyVerbalizationType.NOUN;
	                } else if (getTypeByLinguisticAnalysisTreeTagger(firstToken) == PropertyVerbalizationType.VERB) {
	                    return PropertyVerbalizationType.VERB;
	                }
	            }
	            if (getTypeByLinguisticAnalysisTreeTagger(lastToken) == PropertyVerbalizationType.NOUN) {
	                return PropertyVerbalizationType.NOUN;
	            } else if (getTypeByLinguisticAnalysisTreeTagger(split[0]) == PropertyVerbalizationType.VERB) {
	                return PropertyVerbalizationType.VERB;
	            } else {
	                return PropertyVerbalizationType.NOUN;
	            }
	        } else {
	        	
	        	String tag = treetagger.tag(propertyText);
	            getScore(propertyText);
				//if (tag.contains("")) {// some count did not work
					//return PropertyVerbalizationType.UNSPECIFIED;
				//}
				if (tag.startsWith("N")) {
					//System.out.println("PropertyText is NOUN");
					return PropertyVerbalizationType.NOUN;
				} else if (tag.startsWith("V")) {
					//System.out.println("PropertyText is VERB");
					return PropertyVerbalizationType.VERB;
				} else {
					return PropertyVerbalizationType.NOUN;
				}
	        }
	}
	
	private String normalize(String propertyText){
		//lower case
		propertyText = propertyText.toLowerCase();
		
		return propertyText;
	}
	
//	private void computeExpandedVerbalization(PropertyVerbalization propertyVerbalization){
//		
//		String text = propertyVerbalization.getVerbalizationText();
//		String expandedForm = text;
//		
//		// get POS tag of property verbalization
//		String pos = propertyVerbalization.getPOSTags();
//		
//		// VBN IN
//		if(pos.equals("VBN IN")){
//			expandedForm = "is" + " " + text;
//		} else if(pos.startsWith("BE DET")) {
//			String[] split = text.split(" ");
//			expandedForm = "is" + " a " + Joiner.on(" ").join(Arrays.copyOfRange(text.split(" "), 1, split.length));
//		}
//		
//		propertyVerbalization.setExpandedVerbalizationText(expandedForm);
//	}
	
    public static void main(String args[]) throws Exception{
        PropertyVerbalizerPortuguese pp = new PropertyVerbalizerPortuguese(new QueryExecutionFactoryHttp("http://pt.dbpedia.org/sparql"), "cache", null);
        
        String propertyURI = "http://dbpedia.org/ontology/birthPlace";
        System.out.println(pp.verbalize(propertyURI));
        
        propertyURI = "http://dbpedia.org/ontology/birthPlace";
        System.out.println(pp.verbalize(propertyURI));
        
        propertyURI = "http://dbpedia.org/ontology/hasColor";
        System.out.println(pp.verbalize(propertyURI));
        
        propertyURI = "http://dbpedia.org/ontology/isHardWorking";
        System.out.println(pp.verbalize(propertyURI));
        
        propertyURI = "http://dbpedia.org/ontology/bornIn";
        System.out.println(pp.verbalize(propertyURI));
        
        propertyURI = "http://dbpedia.org/ontology/cross";
        System.out.println(pp.verbalize(propertyURI));
        
        propertyURI = "http://dbpedia.org/ontology/producedBy";
        System.out.println(pp.verbalize(propertyURI));
        
        propertyURI = "http://dbpedia.org/ontology/worksFor";
        System.out.println(pp.verbalize(propertyURI));
        
        propertyURI = "http://dbpedia.org/ontology/workedFor";
        System.out.println(pp.verbalize(propertyURI));
        
        propertyURI = "http://dbpedia.org/ontology/knownFor";
        System.out.println(pp.verbalize(propertyURI));
        
        propertyURI = "http://dbpedia.org/ontology/name";
        System.out.println(pp.verbalize(propertyURI));
        
        propertyURI = "http://dbpedia.org/ontology/isGoldMedalWinner";
        System.out.println(pp.verbalize(propertyURI));
    }
}
