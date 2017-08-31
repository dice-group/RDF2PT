package org.aksw.sw2pt;

import simplenlg.features.Feature;
import simplenlg.features.Gender;
import simplenlg.features.LexicalFeature;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.portuguese.XMLLexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.Realiser;

public class SingleExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Lexicon lexicon = Lexicon.getDefaultLexicon();
		Lexicon lexicon = new XMLLexicon();
		NLGFactory nlgFactory = new NLGFactory(lexicon);
		Realiser realiser = new Realiser();
		// realiser.setDebugMode(true);
		SPhraseSpec ex = nlgFactory.createClause();

		NPPhraseSpec locatum = nlgFactory.createNounPhrase("edifício");
		// System.out.println(ex+"\n");
		locatum.setSpecifier("o");
		// System.out.println(ex+"\n");
		ex.setSubject(locatum);
		ex.setVerb("ficar");
		PPPhraseSpec generalizedLocation = nlgFactory.createPrepositionPhrase();
		NPPhraseSpec relatum = nlgFactory.createNounPhrase("praça");
		relatum.setSpecifier("a");
		generalizedLocation.addComplement(relatum);
		generalizedLocation.setPreposition("em");
		// System.out.println(ex+"\n");
		ex.addComplement(generalizedLocation);
		// System.out.println(ex+"\n");
		// System.out.println(locatum+"\n");
		// System.out.println(generalizedLocation+"\n");

		String target = "O edifício fica na praça.";
		String gloss = "The building lies at the square.";
		String output = realiser.realiseSentence(ex);
		if (!output.equals(target)) {
			System.out.println("* " + output + " TARGET: " + target);
		} else {
			System.out.println(output + " (" + gloss + ")");
		}

		// EX100
		String target100 = "Mariah Carey é uma cantora.";
		String gloss100 = "Mariah Carey is a singer.";
		SPhraseSpec ex100 = nlgFactory.createClause();
		NPPhraseSpec subject100 = nlgFactory.createNounPhrase("Mariah Carey");
		// subject100.setFeature(Feature.PERSON, Person.THIRD);
		ex100.setSubject(subject100);
		ex100.setVerb("ser");
		ex100.setFeature(Feature.TENSE, Tense.PRESENT);
		NPPhraseSpec object100 = nlgFactory.createNounPhrase("cantor");
		object100.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
		object100.setSpecifier("um");
		ex100.setObject(object100);
		String output100 = realiser.realiseSentence(ex100);
		System.out.println(output100);

	}
}