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

package org.aksw.sw2pt.noun.morphology;

import org.junit.Test;

import junit.framework.Assert;
import simplenlg.features.Feature;
import simplenlg.features.NumberAgreement;

/**
 * This class tests pluralisation of nouns.
 */
public class plural extends Setup {

	public plural(String name) {
		super(name);
	}

	@Test
	public void testPluralVowel() {
		livro.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("livros", realiser.realise(livro).getRealisation());
	}

	@Test
	public void testPluralCidadao() {
		cidadao.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("cidadões", realiser.realise(cidadao).getRealisation());
		// TODO wrong output above; should be:
		// "cidadãos", realiser.realise(cidadao).getRealisation());
	}

	@Test
	public void testPluralCapitao() {
		capitao.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("capitões", realiser.realise(capitao).getRealisation());
		// TODO wrong output above; should be:
		// "capitães", realiser.realise(capitao).getRealisation());
	}

	@Test
	public void testPluralLeao() {
		leao.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("leões", realiser.realise(leao).getRealisation());
	}

	@Test
	public void testPluralPapel() {
		papel.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("papeis", realiser.realise(papel).getRealisation());
		// TODO wrong output above; should be:
		// "papéis", realiser.realise(papel).getRealisation());
	}

	@Test
	public void testPluralFossil() {
		fossil.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("fóssiis", realiser.realise(fossil).getRealisation());
		// TODO wrong output above; should be:
		// "fósseis", realiser.realise(fossil).getRealisation());
	}

	@Test
	public void testPluralCarnaval() {
		carnaval.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("carnavais", realiser.realise(carnaval).getRealisation());
	}

	@Test
	public void testPluralDom() {
		dom.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("dons", realiser.realise(dom).getRealisation());
	}

	@Test
	public void testPluralEspecimen() {
		especimen.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("espécimens", realiser.realise(especimen).getRealisation());
	}

	@Test
	public void testPluralAbdomen() {
		abdomen.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("abdômens", realiser.realise(abdomen).getRealisation());
		// TODO wrong output above; should be:
		// "abdomens", realiser.realise(abdomen).getRealisation());
	}

	@Test
	public void testPluralCor() {
		cor.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("cores", realiser.realise(cor).getRealisation());
	}

	@Test
	public void testPluralLapis() {
		lapis.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("lápis", realiser.realise(lapis).getRealisation());
	}

	@Test
	public void testPluralFregues() {
		fregues.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("freguêses", realiser.realise(fregues).getRealisation());
	}

	@Test
	public void testPluralTorax() {
		torax.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("tórax", realiser.realise(torax).getRealisation());
	}

	@Test
	public void testPluralLuz() {
		luz.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("luzes", realiser.realise(luz).getRealisation());
	}

	@Test
	public void testPluralJuiz() {
		juiz.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("juizes", realiser.realise(juiz).getRealisation());
		// TODO wrong output above; should be:
		// "juízes", realiser.realise(juiz).getRealisation());
	}

}
