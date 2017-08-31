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
 * This jUnit class tests the negative forms of 1-verb verb groups and
 * periphrastic verb groups, i.e. when more than 1 verb (e.g. main and auxiliary
 * verbs) compose the verb group.
 * 
 * @author R. de Oliveira, University of Aberdeen.
 */
public class Negative extends Setup {

	public Negative(String name) {
		super(name);
	}

	@Test
	public void test1Verb() {

		cantar.setFeature(Feature.TENSE, Tense.FUTURE);
		cantar.setFeature(Feature.PERSON, Person.THIRD);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		cantar.setFeature(Feature.NEGATED, true);
		String realisation = realiser.realise(cantar).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("não cantarão", realisation);
	}

	@Test
	public void test2Verbs() {

		cantar.setFeature(Feature.TENSE, Tense.FUTURE);
		cantar.setFeature(Feature.PERSON, Person.SECOND);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		cantar.setFeature(Feature.PROGRESSIVE, "continuar");
		cantar.setFeature(Feature.NEGATED, true);
		String realisation = realiser.realise(cantar).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("não continuareis cantando", realisation);
	}

	@Test
	public void test3Verbs() {

		cantar.setFeature(Feature.TENSE, Tense.PRESENT);
		cantar.setFeature(Feature.PERSON, Person.THIRD);
		cantar.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		cantar.setFeature(Feature.MODAL, "poder");
		cantar.setFeature(Feature.PROSPECTIVE, true);
		cantar.setFeature(Feature.NEGATED, true);
		String realisation = realiser.realise(cantar).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("não vai poder cantar", realisation);
	}

}