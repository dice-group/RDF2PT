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
 * This jUnit class tests the many possible forms of periphrastic verb groups,
 * i.e. when main and auxiliary verbs compose the verb group.
 * 
 * @author R. de Oliveira, University of Aberdeen.
 */
public class VerbPeriphrasis extends Setup {

	public VerbPeriphrasis(String name) {
		super(name);
	}

	@Test
	public void testProgressive() {

		cantar.setFeature(Feature.TENSE, Tense.FUTURE);
		cantar.setFeature(Feature.PERSON, Person.THIRD);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		cantar.setFeature(Feature.PROGRESSIVE, true);
		String realisation = realiser.realise(cantar).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("estarão cantando", realisation);
	}

	@Test
	public void testAlternativeProgressive() {

		cantar.setFeature(Feature.TENSE, Tense.FUTURE);
		cantar.setFeature(Feature.PERSON, Person.SECOND);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		cantar.setFeature(Feature.PROGRESSIVE, "continuar");
		String realisation = realiser.realise(cantar).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("continuareis cantando", realisation);
	}

	@Test
	public void testPerfective() {

		cantar.setFeature(Feature.TENSE, Tense.IMPERFECT);
		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		cantar.setFeature(Feature.PERFECT, true);
		String realisation = realiser.realise(cantar).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("tinha cantado", realisation);
	}

	@Test
	public void testProspective() {

		cantar.setFeature(Feature.TENSE, Tense.PRESENT);
		cantar.setFeature(Feature.PERSON, Person.THIRD);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		cantar.setFeature(Feature.PROSPECTIVE, true);
		String realisation = realiser.realise(cantar).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("vai cantar", realisation);
	}

	@Test
	public void testProgressiveProspective() {

		cantar.setFeature(Feature.TENSE, Tense.PRESENT);
		cantar.setFeature(Feature.PERSON, Person.THIRD);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		cantar.setFeature(Feature.PROGRESSIVE, true);
		cantar.setFeature(Feature.PROSPECTIVE, true);
		String realisation = realiser.realise(cantar).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("vai estar cantando", realisation);
	}

	@Test
	public void testAlternativeProgressiveProspective() {

		cantar.setFeature(Feature.TENSE, Tense.PRESENT);
		cantar.setFeature(Feature.PERSON, Person.THIRD);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		cantar.setFeature(Feature.PROGRESSIVE, "viver");
		cantar.setFeature(Feature.PROSPECTIVE, true);
		String realisation = realiser.realise(cantar).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("vai viver cantando", realisation);
	}

	@Test
	public void testPerfectProspective() {

		cantar.setFeature(Feature.TENSE, Tense.CONDITIONAL);
		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		cantar.setFeature(Feature.PERFECT, true);
		cantar.setFeature(Feature.PROSPECTIVE, true);
		String realisation = realiser.realise(cantar).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("iríamos ter cantado", realisation);
	}

	@Test
	public void testModal() {

		cantar.setFeature(Feature.TENSE, Tense.SUBJUNCTIVE_PRESENT);
		cantar.setFeature(Feature.PERSON, Person.SECOND);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		cantar.setFeature(Feature.MODAL, "dever");
		String realisation = realiser.realise(cantar).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("devas cantar", realisation);
	}

	@Test
	public void testModalProgressive() {

		cantar.setFeature(Feature.TENSE, Tense.CONDITIONAL);
		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		cantar.setFeature(Feature.MODAL, "dever");
		cantar.setFeature(Feature.PROGRESSIVE, true);
		String realisation = realiser.realise(cantar).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("deveria estar cantando", realisation);
		//
	}

	@Test
	public void testAlternativeModalProgressive() {

		cantar.setFeature(Feature.TENSE, Tense.CONDITIONAL);
		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		cantar.setFeature(Feature.MODAL, "dever");
		cantar.setFeature(Feature.PROGRESSIVE, "ficar");
		String realisation = realiser.realise(cantar).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("deveria ficar cantando", realisation);
	}

	@Test
	public void testModalPerfective() {

		cantar.setFeature(Feature.TENSE, Tense.SUBJUNCTIVE_FUTURE);
		cantar.setFeature(Feature.PERSON, Person.SECOND);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		cantar.setFeature(Feature.MODAL, "poder");
		cantar.setFeature(Feature.PROSPECTIVE, true);
		String realisation = realiser.realise(cantar).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("forem poder cantar", realisation);
	}

	@Test
	public void testAPrepositionedModal() {

		cantar.setFeature(Feature.TENSE, Tense.PRESENT);
		cantar.setFeature(Feature.PERSON, Person.THIRD);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		cantar.setFeature(Feature.MODAL, "começar");
		cantar.setFeature(Feature.PROSPECTIVE, true);
		String realisation = realiser.realise(cantar).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("vai começar a cantar", realisation);
	}

	@Test
	public void testDePrepositionedModal() {

		cantar.setFeature(Feature.TENSE, Tense.CONDITIONAL);
		cantar.setFeature(Feature.PERSON, Person.FIRST);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		cantar.setFeature(Feature.MODAL, "gostar");
		cantar.setFeature(Feature.PROGRESSIVE, "ficar");
		String realisation = realiser.realise(cantar).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("gostaria de ficar cantando", realisation);
	}

}