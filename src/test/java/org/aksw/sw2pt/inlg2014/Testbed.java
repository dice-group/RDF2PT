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

package org.aksw.sw2pt.inlg2014;

import org.junit.Test;

import junit.framework.Assert;
import simplenlg.features.Feature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Tense;
import simplenlg.framework.NLGElement;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;

/**
 * This class contains tests for the demo session at INLG 2014, Philadelphia.
 * There are 3 initial examples to help users get started. After those, users
 * should create their own examples, to test the system.
 * 
 */
public class Testbed extends Setup {

	public Testbed(String name) {
		super(name);
	}

	private String getResult(NLGElement example, String number) {
		// this.realiser.setDebugMode(true);
		String realisation = this.realiser.realiseSentence(example);
		System.out.println(number + ". " + realisation);
		return realisation;
	}

	/**
	 * This simple example shows only the SVO syntax.
	 */
	@Test
	public void testEx00() {
		NPPhraseSpec subject = this.phraseFactory.createNounPhrase("Junho");
		VPPhraseSpec verb = this.phraseFactory.createVerbPhrase("ser");
		NPPhraseSpec object = this.phraseFactory.createNounPhrase("mês de São João");
		SPhraseSpec s = this.phraseFactory.createClause(subject, verb, object);

		Assert.assertEquals("Junho é mês de São João.", getResult(s, "00"));
	}

	/**
	 * This example shows: a) -ão plural: folião + PLURAL = foliões b) -al
	 * plural: arraial + PLURAL = arraiais c) verb periphrasis build: foliões
	 * animados + gostar + brincar = foliões animados gostam de brincar
	 */
	@Test
	public void testEx01() {
		NPPhraseSpec subject = this.phraseFactory.createNounPhrase("folião");
		subject.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		subject.addPostModifier("animados");
		VPPhraseSpec verb = this.phraseFactory.createVerbPhrase("brincar");
		verb.setFeature(Feature.MODAL, "gostar");
		NPPhraseSpec object = this.phraseFactory.createNounPhrase("em", "arraial");
		object.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		SPhraseSpec s = this.phraseFactory.createClause(subject, verb, object);

		Assert.assertEquals("Foliões animados gostam de brincar em arraiais.", getResult(s, "01"));
	}

	/**
	 * This example shows: a) preposition contraction: em + uma = numa b) no
	 * preposition contraction: de + São João = de + São João d) preposition
	 * contraction: em + o = no e) preposition contraction: de + a = da
	 */
	@Test
	public void testEx02() {
		NPPhraseSpec subject = this.phraseFactory.createNounPhrase("meus", "amigo");
		subject.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		VPPhraseSpec verb = this.phraseFactory.createVerbPhrase("estar");
		NPPhraseSpec place = this.phraseFactory.createNounPhrase("uma", "festa");
		PPPhraseSpec placePhrase = this.phraseFactory.createPrepositionPhrase("em", place);
		NPPhraseSpec type = this.phraseFactory.createNounPhrase("São João");
		PPPhraseSpec typePhrase = this.phraseFactory.createPrepositionPhrase("de", type);
		place.addComplement(typePhrase);
		SPhraseSpec s = this.phraseFactory.createClause(subject, verb, placePhrase);

		Assert.assertEquals("Meus amigos estão numa festa de São João.", getResult(s, "02a"));

		NPPhraseSpec subject2 = this.phraseFactory.createNounPhrase("eu");
		VPPhraseSpec verb2 = this.phraseFactory.createVerbPhrase("estar");
		NPPhraseSpec place2 = this.phraseFactory.createNounPhrase("o", "quarto");
		PPPhraseSpec place2Phrase = this.phraseFactory.createPrepositionPhrase("em", place2);
		NPPhraseSpec place3 = this.phraseFactory.createNounPhrase("a", "minha casa");
		PPPhraseSpec place3Phrase = this.phraseFactory.createPrepositionPhrase("de", place3);
		place2.addComplement(place3Phrase);
		SPhraseSpec s2 = this.phraseFactory.createClause(subject2, verb2, place2Phrase);

		Assert.assertEquals("Eu estou no quarto da minha casa.", getResult(s2, "02b"));
	}

	/**
	 * This example shows a long verb periphrasis.
	 * 
	 */
	@Test
	public void testEx03() {
		NPPhraseSpec subject = this.phraseFactory.createNounPhrase("você");
		VPPhraseSpec verb = this.phraseFactory.createVerbPhrase("criar");
		verb.setFeature(Feature.NEGATED, true);
		verb.setFeature(Feature.PROSPECTIVE, true);
		verb.setFeature(Feature.PROGRESSIVE, "ficar");
		NPPhraseSpec object = this.phraseFactory.createNounPhrase("exemplo");
		object.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		SPhraseSpec s = this.phraseFactory.createClause(subject, verb, object);
		NPPhraseSpec subject2 = this.phraseFactory.createNounPhrase("a", "festa");
		VPPhraseSpec verb2 = this.phraseFactory.createVerbPhrase("acabar");
		verb2.setFeature(Feature.TENSE, Tense.IMPERSONAL_INFINITIVE);
		SPhraseSpec s2 = this.phraseFactory.createClause(subject2, verb2);
		s.addPostModifier("até");
		s.addPostModifier(s2);

		Assert.assertEquals("Você não vai ficar criando exemplos até a festa acabar.", getResult(s, "03"));
	}

}
