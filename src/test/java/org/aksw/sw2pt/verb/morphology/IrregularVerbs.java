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
 * This class tests conjugation of some irregular verbs.
 */
public class IrregularVerbs extends Setup {

	public IrregularVerbs(String name) {
		super(name);
	}

	@Test
	public void testCaber() {
		caber.setFeature(Feature.TENSE, Tense.PRESENT);
		caber.setFeature(Feature.PERSON, Person.FIRST);
		caber.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("caibo", realiser.realise(caber).getRealisation());
	}

	public void testFazer() {
		fazer.setFeature(Feature.TENSE, Tense.PRESENT);
		fazer.setFeature(Feature.PERSON, Person.FIRST);
		fazer.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("faço", realiser.realise(fazer).getRealisation());
		fazer.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("fiz", realiser.realise(fazer).getRealisation());
	}

	public void testOuvir() {
		ouvir.setFeature(Feature.TENSE, Tense.PRESENT);
		ouvir.setFeature(Feature.PERSON, Person.FIRST);
		ouvir.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals("ouço", realiser.realise(ouvir).getRealisation());
	}

}