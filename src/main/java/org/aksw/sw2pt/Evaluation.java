package org.aksw.sw2pt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.aksw.sw2pt.triple2nl.DocumentGeneratorPortuguese;
import org.apache.jena.graph.Triple;
import org.dllearner.kb.sparql.SparqlEndpoint;


public class Evaluation {
	
	public static void evaluation(String name){
		
        String csvFile = "evaluation/"+ name +"/"+ name +".csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        List<String> lines = new ArrayList<>();
        List<String> triplas = new ArrayList<>();
        		//Arrays.asList("Scientists");
        int i = 0;
        
        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] resource = line.split(cvsSplitBy);
                
        		Sparql sparql = new Sparql();
        		Set<Triple> list = new HashSet<>();

        		list = sparql.getTriples(resource[0]);
        		
        		if(!list.isEmpty()) {
        		System.out.println(resource[0]);
        		//System.out.println("Triplas");
        		Iterator<Triple> iterator = list.iterator();
        		while(iterator.hasNext()){
        			//System.out.println(iterator.next());
        			triplas.add(iterator.next().toString());
        		}
        		
        		DocumentGeneratorPortuguese gen = new DocumentGeneratorPortuguese(SparqlEndpoint.create("http://pt.dbpedia.org/sparql",""), "cache");
        		String document = gen.generateDocument(list);
        		//System.out.println("Resumo: " + document);
        		String newline = System.getProperty("line.separator");
        		lines.add( "Resource: " +resource[0] + newline + newline + "Triplas: " + list.toString() + newline +  newline +"Resumo gerado: " + document);
        		//Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        		} else{
        		String newline = System.getProperty("line.separator");
        		lines.add( "Resource: " +resource[0] + newline + "Triplas: " + "vazio" + newline +  "Resumo gerado: " + "vazio");	
        		}
            }
            Path file = Paths.get(""+ name +".txt");
    		Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}

	public static void main (String args[]) throws ParseException{
		
//       evaluation("Astronaut");
//		System.out.println("Gerado");
//		evaluation("Buildings");
//		System.out.println("Gerado");
//		evaluation("city");
//		System.out.println("Gerado");
		evaluation("Scientist");
		System.out.println("Gerado");
		evaluation("SportsTeam");
		System.out.println("Gerado");
		evaluation("University");
		System.out.println("Gerado");
		evaluation("WrittenWork");
		System.out.println("Gerado");

	}
}
