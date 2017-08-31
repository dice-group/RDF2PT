package org.aksw.sw2pt.compiler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This lexicon compiler takes DELAF_PB (Muniz, 2004), an 880,000-entry lexicon
 * of inflected words in Brazilian Portuguese, and produces a lexicon XML output
 * file in SimpleNLG format.
 * 
 * @author R. de Oliveira, University of Aberdeen.
 *
 */
public class delaf2simpleNLG {

	public static void main(String[] args) throws IOException {
		// Name of DELAF_PB lexicon file.
		String delafPBFile = "delaf_pb.txt";
		// Switch full lexicon for a sample to quickly test changes to this
		// compiler.
		// String sourceFile = "delaf_pb_sample.txt";
		// Name of pronouns file.
		String pronounsFile = "pronouns.xml";
		// Name of XML lexicon output file.
		String outputFile = "default-portuguese-lexicon.xml";
		// List of irregular verbs. Only these will be transferred to the XML
		// file.
		// Add more verbs to this list to compile a new XML lexicon file.
		String[] irregularVerbs = {
				// -ar ending
				"estar", "trancar", "laçar", "frear", "largar", "odiar", "coroar", "dar", "saudar", "aguar",
				"averiguar", "adequar",
				// -er ending
				"ser", "ter", "haver", "dizer", "fazer", "trazer", "caber", "saber", "ver", "prover", "poder", "crer",
				"querer", "requerer", "valer", "perder", "aprazer", "erguer", "aquecer", "jazer", "abranger", "roer",
				// -ir ending
				"cair", "ir", "vir", "rir", "ouvir", "medir", "conduzir", "dirigir", "extinguir", "seguir", "conseguir",
				"divergir", "construir", "coibir", "frigir", "arguir", "mentir", "agredir", "dormir", "polir", "umir",
				"extorquir", "falir" };
		// Compilation of above list to a hash set, for quick lookup.
		Set<String> iv = new HashSet<String>(Arrays.asList(irregularVerbs));

		// regex to capture each line of DELAF_PB:
		// group 1 = inflected form
		// group 2 = comma
		// group 3 = base form
		// group 4 = dot
		// group 5 = POS
		// group 6 = colon
		// group 7 = tags
		String entry = "(.+)(\\,)(.+)(\\.)(.+?)(\\:)(.+)";
		Pattern entryP = Pattern.compile(entry);

		// regex to capture a tense-person-number tag in each line of DELAF_PB:
		// group 1 = colon
		// group 2 = tense tag
		// group 3 = person tag
		// group 4 = number tag
		String tag = "(\\:)(.)(.)?(.)?";
		Pattern tagP = Pattern.compile(tag);

		@SuppressWarnings("resource")
		// Read DELAF_PB source file and initiate SimpleNLG output.
		BufferedReader br = new BufferedReader(new FileReader(
				// Path to DELAF_PB file. May need to be edited for different
				// OSs.
				"./lexiconCompiler/input/" + delafPBFile));
		@SuppressWarnings("resource")
		BufferedReader br2 = new BufferedReader(new FileReader(
				// Path to pronouns file. May need to be edited for different
				// OSs.
				"./lexiconCompiler/input/" + pronounsFile));
		FileWriter fw = new FileWriter(
				// Path to output file. May need to be edited for different OSs.
				"./lexiconCompiler/output/" + outputFile);
		fw.write("<lexicon>\n");

		String s;
		String current = "";
		int count = 0;

		// Read each line in pronoun file.
		while ((s = br2.readLine()) != null) {
			fw.write(s + "\n");
		}

		fw.write("<!-- irregular verbs -->\n");
		// Read each line in DEALF_PB file.
		while ((s = br.readLine()) != null) {
			// Try to match an entry with the current read line.
			Matcher entryM = entryP.matcher(s);
			// If a entry is contained in the current line (it always will)...
			if (entryM.find()) {
				// break down all bits of the entry...
				String base = entryM.group(3);
				String inflected = entryM.group(1);
				String pos = entryM.group(5);
				String longPOS = null;
				if (pos.equals("V")) {
					longPOS = "verb";
				}
				String tags = entryM.group(6) + entryM.group(7);
				// and write irregular verb entries to XML lexicon file.
				if (pos.equals("V") && iv.contains(base)) {
					if (!current.equals(base)) {
						if (count != 0) {
							fw.write("\t</word>\n");
						}
						fw.write("\t<word><!-- " + base + "." + pos.toLowerCase() + " -->\n" + "\t\t<base>" + base
								+ "</base>\n" + "\t\t<category>" + longPOS + "</category>\n" + "\t\t<id>" + base + "."
								+ pos.toLowerCase() + "</id>\n");
						current = base;
						count++;
					}
					Matcher tagM = tagP.matcher(tags);
					while (tagM.find()) {
						String tense = tagM.group(2);
						switch (tense) {
						// TODO double-check that these tense names are the same
						// in sNLG
						case "W":
							tense = "infinitive";
							break;
						case "G":
							tense = "present_participle";
							break;
						case "K":
							tense = "past_participle";
							break;
						case "P":
							tense = "present";
							break;
						case "I":
							tense = "imperfect";
							break;
						case "J":
							tense = "past";
							break;
						case "F":
							tense = "future";
							break;
						case "Q":
							tense = "plu_perfect";
							break;
						case "S":
							tense = "subjunctive_present";
							break;
						case "T":
							tense = "subjunctive_imperfect";
							break;
						case "U":
							tense = "subjunctive_future";
							break;
						case "Y":
							tense = "imperative";
							break;
						case "C":
							tense = "conditional";
							break;
						}
						String personNumber = tagM.group(3) + tagM.group(4);
						if (tense.endsWith("participle") && inflected.endsWith("o")) {
							fw.write("\t\t<" + tense + ">" + inflected + "</" + tense + ">\n");
						} else if (!tense.endsWith("participle")) {
							fw.write("\t\t<" + tense + personNumber + ">" + inflected + "</" + tense + personNumber
									+ ">\n");
						}
					}
				}
			}
		}
		// Write closing line in XML lexicon file and close it.
		fw.write("\t</word>\n</lexicon>");
		fw.close();
	}
}