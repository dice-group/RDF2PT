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

import org.junit.Test;

import junit.framework.Assert;

// TODO: Repeat tests with plural when plural of NPs is done.
/**
 * This class tests vowel elision in PPs such as "da" (de + a). The mechanism is
 * employed to a PP by the Morphophonology package.
 */
public class Main extends Setup {

	public Main(String name) {
		super(name);
	}

	@Test
	public void testAoHomen() {
		aPP.addComplement(this.homen);
		Assert.assertEquals("ao homen", realiser.realise(aPP).getRealisation());
	}

	@Test
	public void testAaMulher() {
		aPP.addComplement(mulher);
		Assert.assertEquals("à mulher", realiser.realise(aPP).getRealisation());
	}

	public void testDoHomen() {
		dePP.addComplement(homen);
		Assert.assertEquals("do homen", realiser.realise(dePP).getRealisation());
	}

	@Test
	public void testDaMulher() {
		dePP.addComplement(mulher);
		Assert.assertEquals("da mulher", realiser.realise(dePP).getRealisation());
	}

	public void testPeloHomen() {
		porPP.addComplement(homen);
		Assert.assertEquals("pelo homen", realiser.realise(porPP).getRealisation());
	}

	@Test
	public void testPelaMulher() {
		porPP.addComplement(mulher);
		Assert.assertEquals("pela mulher", realiser.realise(porPP).getRealisation());
	}

	public void testProximoAoHomen() {
		proximoAPP.addComplement(homen);
		Assert.assertEquals("próximo ao homen", realiser.realise(proximoAPP).getRealisation());
	}

	@Test
	public void testProximoAaMulher() {
		proximoAPP.addComplement(mulher);
		Assert.assertEquals("próximo à mulher", realiser.realise(proximoAPP).getRealisation());
	}

	public void testLongeDoHomen() {
		longeDePP.addComplement(homen);
		Assert.assertEquals("longe do homen", realiser.realise(longeDePP).getRealisation());
	}

	public void testLongeDaMulher() {
		longeDePP.addComplement(mulher);
		Assert.assertEquals("longe da mulher", realiser.realise(longeDePP).getRealisation());
	}

	public void testContraOHomen() {
		contraPP.addComplement(homen);
		Assert.assertEquals("contra o homen", realiser.realise(contraPP).getRealisation());
	}

	public void testContraAMulher() {
		contraPP.addComplement(mulher);
		Assert.assertEquals("contra a mulher", realiser.realise(contraPP).getRealisation());
	}

	public void testParaOHomen() {
		paraPP.addComplement(homen);
		Assert.assertEquals("para o homen", realiser.realise(paraPP).getRealisation());
	}

	public void testParaAMulher() {
		paraPP.addComplement(mulher);
		Assert.assertEquals("para a mulher", realiser.realise(paraPP).getRealisation());
	}

	public void testDesdeOHomen() {
		desdePP.addComplement(homen);
		Assert.assertEquals("desde o homen", realiser.realise(desdePP).getRealisation());
	}

	public void testDesdeAMulher() {
		desdePP.addComplement(mulher);
		Assert.assertEquals("desde a mulher", realiser.realise(desdePP).getRealisation());
	}

	public void testNaMulher() {
		emPP.addComplement(mulher);
		Assert.assertEquals("na mulher", realiser.realise(emPP).getRealisation());
	}

	public void testNoHomen() {
		emPP.addComplement(homen);
		Assert.assertEquals("no homen", realiser.realise(emPP).getRealisation());
	}

	public void testNesteHomen() {
		emPP.addComplement(esteHomen);
		Assert.assertEquals("neste homen", realiser.realise(emPP).getRealisation());
	}

	public void testDesteHomen() {
		dePP.addComplement(esteHomen);
		Assert.assertEquals("deste homen", realiser.realise(dePP).getRealisation());
	}

	public void testNistoAqui() {
		emPP.addComplement(istoAqui);
		Assert.assertEquals("nisto aqui", realiser.realise(emPP).getRealisation());
	}

	public void testDistoAqui() {
		dePP.addComplement(istoAqui);
		Assert.assertEquals("disto aqui", realiser.realise(dePP).getRealisation());
	}

	public void testNumaMulher() {
		emPP.addComplement(umaMulher);
		Assert.assertEquals("numa mulher", realiser.realise(emPP).getRealisation());
	}

	public void testDumaMulher() {
		dePP.addComplement(umaMulher);
		Assert.assertEquals("duma mulher", realiser.realise(dePP).getRealisation());
	}

}
