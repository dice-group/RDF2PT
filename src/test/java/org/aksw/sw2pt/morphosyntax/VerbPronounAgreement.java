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

package org.aksw.sw2pt.morphosyntax;

import org.junit.Test;

import junit.framework.Assert;

/**
 * This jUnit class tests verb-pronoun agreement, so that features for person,
 * number and gender are passed on the verb (group) just by using a particular
 * pronoun -- instead of manually having to state those features for each verb
 * (group) generated.
 * 
 * @author R. de Oliveira, University of Aberdeen.
 */
public class VerbPronounAgreement extends Setup {

	public VerbPronounAgreement(String name) {
		super(name);
	}

	@Test
	public void testEu() {

		clause.setSubject(eu);
		clause.setVerb(cantar);
		String realisation = realiser.realise(clause).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("eu canto", realisation);
	}

	@Test
	public void testTu() {

		clause.setSubject(tu);
		clause.setVerb(cantar);
		String realisation = realiser.realise(clause).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("tu cantas", realisation);
	}

	@Test
	public void testVoce() {

		clause.setSubject(voce);
		clause.setVerb(cantar);
		String realisation = realiser.realise(clause).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("você canta", realisation);
	}

	@Test
	public void testEla() {

		clause.setSubject(ela);
		clause.setVerb(cantar);
		String realisation = realiser.realise(clause).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("ela canta", realisation);
	}

	@Test
	public void testEle() {

		clause.setSubject(ele);
		clause.setVerb(cantar);
		String realisation = realiser.realise(clause).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("ele canta", realisation);
	}

	@Test
	public void testNos() {

		clause.setSubject(nos);
		clause.setVerb(cantar);
		String realisation = realiser.realise(clause).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("nós cantamos", realisation);
	}

	@Test
	public void testAGente() {

		clause.setSubject(aGente);
		clause.setVerb(cantar);
		String realisation = realiser.realise(clause).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("a gente canta", realisation);
	}

	@Test
	public void testVos() {

		clause.setSubject(vos);
		clause.setVerb(cantar);
		String realisation = realiser.realise(clause).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("vós cantais", realisation);
	}

	@Test
	public void testVoces() {

		clause.setSubject(voces);
		clause.setVerb(cantar);
		String realisation = realiser.realise(clause).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("vocês cantam", realisation);
	}

	@Test
	public void testElas() {

		clause.setSubject(elas);
		clause.setVerb(cantar);
		String realisation = realiser.realise(clause).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("elas cantam", realisation);
	}

	@Test
	public void testEles() {

		clause.setSubject(eles);
		clause.setVerb(cantar);
		String realisation = realiser.realise(clause).getRealisation();
		// System.out.println(realisation);
		Assert.assertEquals("eles cantam", realisation);
	}

}