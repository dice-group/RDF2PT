package org.aksw.sw2pt;

import java.util.ArrayList;

import simplenlg.features.Feature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.portuguese.XMLLexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.Realiser;

public class CompleteExample {

	/**
	 * This class generates instances of the test-bed explored in the MA thesis
	 * on spatial language by Rodrigo Gomes de Oliveira on 24 January 2013. It
	 * uses a trilingual (En, Fr and Pt) extension of SimpleNLG 4.0. The testbed
	 * is available on:
	 * https://docs.google.com/spreadsheet/ccc?key=0AjjU8ITs-OqudDE1MkZoS19IQWJ2Tks0NE5ONFhrZEE&usp=sharing
	 */
	public static void main(String[] args) {

		// arrays to store realized forms, target forms and translations
		ArrayList<SPhraseSpec> testBed = new ArrayList<SPhraseSpec>();
		ArrayList<String> targets = new ArrayList<String>();
		ArrayList<String> glosses = new ArrayList<String>();

		Lexicon lexicon = new XMLLexicon(); // uses Portuguese XML lexicon
		NLGFactory nlgFactory = new NLGFactory(lexicon); // same as usual
		Realiser realiser = new Realiser(); // general realizer
		// realiser.setDebugMode(true); // uncomment this to see features tree

		// BEGINNING OF TESTBED

		// EX1
		String target1 = "Eles trouxeram as receitas.";
		String gloss1 = "They brought the recipes.";
		SPhraseSpec ex1 = nlgFactory.createClause();
		NPPhraseSpec subject1 = nlgFactory.createNounPhrase("eles");
		subject1.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		subject1.setFeature(Feature.PERSON, Person.THIRD);
		ex1.setSubject(subject1);
		ex1.setVerb("trazer");
		ex1.setFeature(Feature.TENSE, Tense.PAST);
		NPPhraseSpec object1 = nlgFactory.createNounPhrase("receita");
		object1.setSpecifier("as");
		object1.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		ex1.setObject(object1);
		testBed.add(ex1);
		targets.add(target1);
		glosses.add(gloss1);

		// EX2
		String target2 = "Cuja festa reúne os curitibanos.";
		String gloss2 = "Whose fest reunites the Curitibans.";
		SPhraseSpec ex2 = nlgFactory.createClause();
		NPPhraseSpec subject2 = nlgFactory.createNounPhrase("cuja festa");
		subject2.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		subject2.setFeature(Feature.PERSON, Person.THIRD);
		ex2.setSubject(subject2);
		ex2.setVerb("reunir");
		ex2.setFeature(Feature.TENSE, Tense.PRESENT);
		NPPhraseSpec object2 = nlgFactory.createNounPhrase("curitibano");
		object2.setSpecifier("os");
		object2.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		ex2.setObject(object2);
		testBed.add(ex2);
		targets.add(target2);
		glosses.add(gloss2);

		// EX3
		String target3 = "Curitiba está a 930 metros acima do nível do mar.";
		String gloss3 = "Curitiba is at 930 meters above sea level.";
		SPhraseSpec ex3 = nlgFactory.createClause();
		NPPhraseSpec subject3 = nlgFactory.createNounPhrase("Curitiba");
		subject3.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		subject3.setFeature(Feature.PERSON, Person.THIRD);
		ex3.setSubject(subject3);
		ex3.setVerb("estar");
		ex3.setFeature(Feature.TENSE, Tense.PRESENT);
		PPPhraseSpec pp3 = nlgFactory.createPrepositionPhrase();
		pp3.setPreposition("acima de");
		pp3.addPreModifier("a 930 metros");
		NPPhraseSpec location3 = nlgFactory.createNounPhrase("nível do mar");
		location3.setSpecifier("o");
		pp3.addComplement(location3);
		ex3.addComplement(pp3);
		testBed.add(ex3);
		targets.add(target3);
		glosses.add(gloss3);

		// EX4
		String target4 = "Quem gosta deve visitar a capela.";
		String gloss4 = "Who likes [it] should visit the chapel.";
		SPhraseSpec ex4 = nlgFactory.createClause();
		ex4.setSubject("quem gosta");
		ex4.setFeature(Feature.MODAL, "dever");
		ex4.setVerb("visitar");
		NPPhraseSpec object4 = nlgFactory.createNounPhrase("a", "capela");
		ex4.addComplement(object4);
		testBed.add(ex4);
		targets.add(target4);
		glosses.add(gloss4);

		// EX35
		String target35 = "Você vai passar pelo bairro.";
		String gloss35 = "You are going to drive through the neighborhood.";
		SPhraseSpec ex35 = nlgFactory.createClause();
		ex35.setSubject("você");
		VPPhraseSpec verb35 = nlgFactory.createVerbPhrase("passar");
		verb35.setFeature(Feature.PROSPECTIVE, true);
		ex35.setVerb(verb35);
		// ex35.setFeature(Feature.PROSPECTIVE, true);
		PPPhraseSpec generalizedRoute = nlgFactory.createPrepositionPhrase();
		NPPhraseSpec relatum35 = nlgFactory.createNounPhrase("bairro");
		relatum35.setSpecifier("o");
		generalizedRoute.addComplement(relatum35);
		generalizedRoute.setPreposition("por");
		ex35.addComplement(generalizedRoute);
		testBed.add(ex35);
		targets.add(target35);
		glosses.add(gloss35);

		// EX41
		String target41 = "O edifício fica na praça.";
		String gloss41 = "The building lies at the square.";
		SPhraseSpec ex41 = nlgFactory.createClause();
		NPPhraseSpec locatum = nlgFactory.createNounPhrase("edifício");
		locatum.setSpecifier("o");
		ex41.setSubject(locatum);
		ex41.setVerb("ficar");
		PPPhraseSpec generalizedLocation = nlgFactory.createPrepositionPhrase();
		NPPhraseSpec relatum = nlgFactory.createNounPhrase("praça");
		relatum.setSpecifier("a");
		generalizedLocation.addComplement(relatum);
		generalizedLocation.setPreposition("em");
		ex41.addComplement(generalizedLocation);
		testBed.add(ex41);
		targets.add(target41);
		glosses.add(gloss41);

		// EX42
		String target42 = "O calçadão termina na praça.";
		String gloss42 = "The pedestrian zone ends at the square.";
		SPhraseSpec ex42 = nlgFactory.createClause();
		ex42.setSubject("o calçadão");
		ex42.setVerb("terminar");
		ex42.addComplement("na praça");
		testBed.add(ex42);
		targets.add(target42);
		glosses.add(gloss42);

		for (int i = 0; i < testBed.size(); i++) {

			String output = realiser.realiseSentence(testBed.get(i));

			if (!output.equals(targets.get(i))) {
				System.out.println("* " + output + " TARGET: " + targets.get(i));
			} else {
				System.out.println(output + " (" + glosses.get(i) + ")");
			}
		}
	}
}
