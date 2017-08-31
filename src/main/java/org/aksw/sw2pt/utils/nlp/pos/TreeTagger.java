package org.aksw.sw2pt.utils.nlp.pos;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.aliasi.tag.Tagging;

public class TreeTagger implements PartOfSpeechTagger {

	TreeTaggerWrapper<String> tt;

	private static final Logger logger = LoggerFactory.getLogger(TreeTagger.class);
	
	private String tagging;

	public TreeTagger() throws IOException {
		System.setProperty("treetagger.home","/Users/diegomoussallem/Documents/workspace/sw2pt/src/main/resources/models/pt/TreeTagger/");
		tt = new TreeTaggerWrapper<String>();
		tt.setModel("portuguese-finegrained-utf8.par");
	}

	public String tag(String s) {
		//System.out.println("Word s: "+ s);
		tagging = "";
		List<String> input = Arrays.asList(s.split(" "));
		try {
		     tt.setHandler(new TokenHandler<String>() {
		         public void token(String token, String pos, String lemma) {
		             //tagging += token+"/"+pos + " ";
		             tagging += pos + " ";
		         }
		     });
		     tt.process(input);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TreeTaggerException e) {
			e.printStackTrace();
		}

		//System.out.println("Tag: "+ tagging);
		return tagging.trim();
	}
	
	public String getLemma(String s) {
		tagging = "";
		List<String> input = Arrays.asList(s.split(" "));
		try {
		     tt.setHandler(new TokenHandler<String>() {
		         public void token(String token, String pos, String lemma) {
		             tagging += lemma + " ";
		            		 //token+"/"+pos + " ";
		             
		         }
		     });
		     tt.process(input);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TreeTaggerException e) {
			e.printStackTrace();
		}

		return tagging.trim();
	}

	public void close(){
		tt.destroy();
	}

	@Override
	public String getName() {
		return "Tree Tagger";
	}

	@Override
	public Tagging<String> getTagging(String sentence) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> tagTopK(String sentence) {
		return Collections.singletonList(tag(sentence));
	}

	  public static void main(String[] args) throws Exception {
	    // Point TT4J to the TreeTagger installation directory. The executable is expected
	    // in the "bin" subdirectory - in this example at "/opt/treetagger/bin/tree-tagger"
	    
		  //System.setProperty("treetagger.home", "/opt/treetagger");
	    
		  TreeTagger tt = new TreeTagger();
		  //String tags = tt.tag("Albert Einstein é um cientista");
		  String tags = tt.tag("Equivalência");
		  System.out.println(tags);
		  
		  boolean saber = StringUtils.isNumeric("123");
		  System.out.println(saber);
		  
		  String lemmas = tt.getLemma("armários");
		  System.out.println(lemmas);
		  
		  
//		  TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
//		  System.setProperty("treetagger.home","/Users/diegomoussallem/Documents/workspace/sw2pt/src/main/resources/models/pt/TreeTagger/");
//		  tt = new TreeTaggerWrapper<String>();
//		  tt.setModel("portuguese-finegrained-utf8.par");
//		  
//	    try {
//	      //tt.setModel("/opt/treetagger/models/english.par:iso8859-1");
//	      tt.setHandler(new TokenHandler<String>() {
//	        public void token(String token, String pos, String lemma) {
//	          System.out.println(token + "\t" + pos + "\t" + lemma);
//	        }
//	      });
//	      tt.process(asList(new String[] { "Albert", "é", "um", "físico", "." }));
//	    }
//	    finally {
//	      tt.destroy();
//	    }

	  
	  }

}
