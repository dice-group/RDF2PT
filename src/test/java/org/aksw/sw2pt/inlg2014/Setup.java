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

import org.junit.Before;

import junit.framework.TestCase;
import simplenlg.framework.NLGFactory;
//import simplenlg.framework.PhraseElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.portuguese.XMLLexicon;
//import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.Realiser;

/**
 * This class is the base class for all JUnit simplenlg.test cases for
 * SimpleNLG.
 * 
 * @author R. de Oliveira, University of Aberdeen.
 */
public abstract class Setup extends TestCase {

	Realiser realiser;
	NLGFactory phraseFactory;
	Lexicon lexicon;

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
	}
}
