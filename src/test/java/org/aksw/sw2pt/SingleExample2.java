package org.aksw.sw2pt;

import java.io.IOException;

import simplenlg.features.Feature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.portuguese.XMLLexicon;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.Realiser;

public class SingleExample2 {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// int dummy = System.in.read();
		long startTime;
		long endTime;
		double elapsed;

		Realiser realiser = new Realiser();

		System.out.println("constructing lexicon");
		startTime = System.nanoTime();
		Lexicon lexicon = new XMLLexicon();
		endTime = System.nanoTime();
		elapsed = (endTime - startTime) / 1000000000.0;
		System.out.println("lexicon constructed");
		System.out.println("elapsed time: " + elapsed + " seconds\n");

		NLGFactory phraseFactory = new NLGFactory(lexicon);

		VPPhraseSpec caber = phraseFactory.createVerbPhrase("caber");
		caber.setFeature(Feature.TENSE, Tense.PRESENT);
		caber.setFeature(Feature.PERSON, Person.FIRST);
		caber.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);

		System.out.println("realising example");
		startTime = System.nanoTime();
		String r = realiser.realise(caber).getRealisation();
		endTime = System.nanoTime();
		elapsed = (endTime - startTime) / 1000000000.0;
		System.out.println("example realised");
		System.out.println("elapsed time: " + elapsed + " seconds\n");
	}
}
