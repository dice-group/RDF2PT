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

package org.aksw.sw2pt.verb.morphology;

import org.junit.Test;

import junit.framework.Assert;
import simplenlg.features.Feature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Person;
import simplenlg.features.Tense;

/**
 * This class tests conjugation of regular verbs. The mechanism is employed to a
 * VP by the Morphophonology package.
 */
public class RegularVerbs extends Setup {

	public RegularVerbs(String name) {
		super(name);
	}

	@Test
	public void testFirstConjugation() {

		// Tense = Conditional
		cantar.setFeature(Feature.TENSE, Tense.CONDITIONAL);
		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("cantaria", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantarias", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cantaria", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("cantaríamos", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantaríeis", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cantariam", realiser.realise(cantar).getRealisation());

		// Tense = Future
		cantar.setFeature(Feature.TENSE, Tense.FUTURE);
		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("cantarei", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantarás", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cantará", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("cantaremos", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantareis", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cantarão", realiser.realise(cantar).getRealisation());

		// Tense = Imperfect
		cantar.setFeature(Feature.TENSE, Tense.IMPERFECT);
		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("cantava", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantavas", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cantava", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("cantávamos", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantáveis", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cantavam", realiser.realise(cantar).getRealisation());

		// Tense = Past
		cantar.setFeature(Feature.TENSE, Tense.PAST);
		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("cantei", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantaste", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cantou", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("cantamos", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantastes", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cantaram", realiser.realise(cantar).getRealisation());

		// Tense = Pluperfect
		cantar.setFeature(Feature.TENSE, Tense.PLUPERFECT);
		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("cantara", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantaras", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cantara", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("cantáramos", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantáreis", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cantaram", realiser.realise(cantar).getRealisation());

		// Tense = Present
		cantar.setFeature(Feature.TENSE, Tense.PRESENT);
		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("canto", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantas", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("canta", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("cantamos", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantais", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cantam", realiser.realise(cantar).getRealisation());

		// Tense = Subjunctive Present
		cantar.setFeature(Feature.TENSE, Tense.SUBJUNCTIVE_PRESENT);
		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("cante", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantes", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cante", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("cantemos", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("canteis", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cantem", realiser.realise(cantar).getRealisation());

		// Tense = Subjunctive Future
		cantar.setFeature(Feature.TENSE, Tense.SUBJUNCTIVE_FUTURE);
		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("cantar", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantares", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cantar", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("cantarmos", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantardes", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cantarem", realiser.realise(cantar).getRealisation());

		// Tense = Subjunctive Imperfect
		cantar.setFeature(Feature.TENSE, Tense.SUBJUNCTIVE_IMPERFECT);
		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("cantasse", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantasses", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cantasse", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("cantássemos", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("cantásseis", realiser.realise(cantar).getRealisation());

		cantar.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("cantassem", realiser.realise(cantar).getRealisation());

	}

	@Test
	public void testSecondConjugation() {

		// Tense = Conditional
		vender.setFeature(Feature.TENSE, Tense.CONDITIONAL);
		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("venderia", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("venderias", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("venderia", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("venderíamos", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("venderíeis", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("venderiam", realiser.realise(vender).getRealisation());

		// Tense = Future
		vender.setFeature(Feature.TENSE, Tense.FUTURE);
		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("venderei", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("venderás", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("venderá", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("venderemos", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("vendereis", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("venderão", realiser.realise(vender).getRealisation());

		// Tense = Imperfect
		vender.setFeature(Feature.TENSE, Tense.IMPERFECT);
		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("vendia", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("vendias", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("vendia", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("vendíamos", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("vendíeis", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("vendiam", realiser.realise(vender).getRealisation());

		// Tense = Past
		vender.setFeature(Feature.TENSE, Tense.PAST);
		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("vendi", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("vendeste", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("vendeu", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("vendemos", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("vendestes", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("venderam", realiser.realise(vender).getRealisation());

		// Tense = Pluperfect
		vender.setFeature(Feature.TENSE, Tense.PLUPERFECT);
		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("vendera", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("venderas", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("vendera", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("vendêramos", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("vendêreis", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("venderam", realiser.realise(vender).getRealisation());

		// Tense = Present
		vender.setFeature(Feature.TENSE, Tense.PRESENT);
		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("vendo", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("vendes", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("vende", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("vendemos", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("vendeis", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("vendem", realiser.realise(vender).getRealisation());

		// Tense = Subjunctive Present
		vender.setFeature(Feature.TENSE, Tense.SUBJUNCTIVE_PRESENT);
		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("venda", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("vendas", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("venda", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("vendamos", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("vendais", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("vendam", realiser.realise(vender).getRealisation());

		// Tense = Subjunctive Future
		vender.setFeature(Feature.TENSE, Tense.SUBJUNCTIVE_FUTURE);
		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("vender", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("venderes", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("vender", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("vendermos", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("venderdes", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("venderem", realiser.realise(vender).getRealisation());

		// Tense = Subjunctive Imperfect
		vender.setFeature(Feature.TENSE, Tense.SUBJUNCTIVE_IMPERFECT);
		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("vendesse", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("vendesses", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("vendesse", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.FIRST);
		vender.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("vendêssemos", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("vendêsseis", realiser.realise(vender).getRealisation());

		vender.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("vendessem", realiser.realise(vender).getRealisation());

	}

	@Test
	public void testThirdConjugation() {

		// Tense = Conditional
		partir.setFeature(Feature.TENSE, Tense.CONDITIONAL);
		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("partiria", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partirias", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("partiria", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("partiríamos", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partiríeis", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("partiriam", realiser.realise(partir).getRealisation());

		// Tense = Future
		partir.setFeature(Feature.TENSE, Tense.FUTURE);
		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("partirei", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partirás", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("partirá", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("partiremos", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partireis", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("partirão", realiser.realise(partir).getRealisation());

		// Tense = Imperfect
		partir.setFeature(Feature.TENSE, Tense.IMPERFECT);
		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("partia", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partias", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("partia", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("partíamos", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partíeis", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("partiam", realiser.realise(partir).getRealisation());

		// Tense = Past
		partir.setFeature(Feature.TENSE, Tense.PAST);
		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("parti", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partiste", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("partiu", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("partimos", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partistes", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("partiram", realiser.realise(partir).getRealisation());

		// Tense = Pluperfect
		partir.setFeature(Feature.TENSE, Tense.PLUPERFECT);
		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("partira", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partiras", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("partira", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("partíramos", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partíreis", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("partiram", realiser.realise(partir).getRealisation());

		// Tense = Present
		partir.setFeature(Feature.TENSE, Tense.PRESENT);
		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("parto", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partes", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("parte", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("partimos", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partis", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("partem", realiser.realise(partir).getRealisation());

		// Tense = Subjunctive Present
		partir.setFeature(Feature.TENSE, Tense.SUBJUNCTIVE_PRESENT);
		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("parta", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partas", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("parta", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("partamos", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partais", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("partam", realiser.realise(partir).getRealisation());

		// Tense = Subjunctive Future
		partir.setFeature(Feature.TENSE, Tense.SUBJUNCTIVE_FUTURE);
		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("partir", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partires", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("partir", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("partirmos", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partirdes", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("partirem", realiser.realise(partir).getRealisation());

		// Tense = Subjunctive Imperfect
		partir.setFeature(Feature.TENSE, Tense.SUBJUNCTIVE_IMPERFECT);
		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("partisse", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partisses", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("partisse", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.FIRST);
		partir.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("partíssemos", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.SECOND);
		Assert.assertEquals("partísseis", realiser.realise(partir).getRealisation());

		partir.setFeature(Feature.PERSON, Person.THIRD);
		Assert.assertEquals("partissem", realiser.realise(partir).getRealisation());

	}

}
