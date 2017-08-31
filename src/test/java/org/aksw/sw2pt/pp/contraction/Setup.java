/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is "Simplenlg".
 *
 * The Initial Developer of the Original Code is Ehud Reiter, Albert Gatt and Dave Westwater.
 * Portions created by Ehud Reiter, Albert Gatt and Dave Westwater are Copyright (C) 2010-11 The University of Aberdeen. All Rights Reserved.
 *
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell.
 */
package org.aksw.sw2pt.pp.contraction;

import org.junit.Before;

import junit.framework.TestCase;
import simplenlg.framework.NLGFactory;
//import simplenlg.framework.PhraseElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.portuguese.XMLLexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
//import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.Realiser;

/**
 * This class is the base class for all JUnit test cases in this package.
 * 
 * @author R. de Oliveira, University of Aberdeen.
 */
public abstract class Setup extends TestCase {

	/** The realiser. */
	Realiser realiser;

	NLGFactory phraseFactory;

	Lexicon lexicon;

	// General PPs with preposition that undergo contraction. These are "a",
	// "de"
	// "em", and "por" or prepositional complexes formed with those such as
	// "próximo
	// a" or "longe de". Note that "contra" and "para", both prepositions, end
	// in -a but should not undergo elision. The same applies for "desde",
	// which ends in -de.
	PPPhraseSpec aPP, dePP, emPP, porPP, proximoAPP, longeDePP, contraPP, paraPP, desdePP;

	NPPhraseSpec homen, mulher, esteHomen, istoAqui, umaMulher;

	/**
	 * Instantiates a new SimpleNLG test.
	 * 
	 * @param name
	 *            the name
	 */
	public Setup(String name) {
		super(name);
	}

	/**
	 * Set up the variables we'll need for this simplenlg.test to run (Called
	 * automatically by JUnit)
	 */
	@Override
	@Before
	protected void setUp() {
		lexicon = new XMLLexicon();
		this.phraseFactory = new NLGFactory(this.lexicon);
		this.realiser = new Realiser();

		this.aPP = this.phraseFactory.createPrepositionPhrase("a");
		this.dePP = this.phraseFactory.createPrepositionPhrase("de");
		this.emPP = this.phraseFactory.createPrepositionPhrase("em");
		this.porPP = this.phraseFactory.createPrepositionPhrase("por");
		this.proximoAPP = this.phraseFactory.createPrepositionPhrase("próximo a");
		this.longeDePP = this.phraseFactory.createPrepositionPhrase("longe de");
		this.contraPP = this.phraseFactory.createPrepositionPhrase("contra");
		this.paraPP = this.phraseFactory.createPrepositionPhrase("para");
		this.desdePP = this.phraseFactory.createPrepositionPhrase("desde");

		this.homen = this.phraseFactory.createNounPhrase("o", "homen");
		this.mulher = this.phraseFactory.createNounPhrase("a", "mulher");
		this.esteHomen = this.phraseFactory.createNounPhrase("este", "homen");
		this.istoAqui = this.phraseFactory.createNounPhrase("isto", "aqui");
		this.umaMulher = this.phraseFactory.createNounPhrase("uma", "mulher");
	}
}
