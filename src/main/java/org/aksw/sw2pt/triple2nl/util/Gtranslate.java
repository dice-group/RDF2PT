package org.aksw.sw2pt.triple2nl.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;


public class Gtranslate {
	
	 public static void main(String[] args) throws Exception {
		 
	  Gtranslate http = new Gtranslate();
	  String word = http.callUrlAndParseResult("auto", "pt", "dela");
	  //System.out.println(word);
	 }
	 
	 public String callUrlAndParseResult(String langFrom, String langTo,
	                                             String word) throws Exception 
	 {

	  String url = "https://translate.googleapis.com/translate_a/single?"+
	    "client=gtx&"+
	    "sl=" + langFrom + 
	    "&tl=" + langTo + 
	    "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");    
	  
	  URL obj = new URL(url);
	  HttpURLConnection con = (HttpURLConnection) obj.openConnection(); 
	  con.setRequestProperty("User-Agent", "Mozilla/5.0");
	 
	  BufferedReader in = new BufferedReader(
	    new InputStreamReader(con.getInputStream()));
	  String inputLine;
	  StringBuffer response = new StringBuffer();
	 
	  while ((inputLine = in.readLine()) != null) {
	   response.append(inputLine);
	  }
	  in.close();
	 
	  
	  String result = parseResult(response.toString());
	  
	  if(result.equals("")){
		  return word;
	  } else {
	  return result;}
	  
	  
	 }
	 
	 private String parseResult(String inputJson) throws Exception
	 {
	  JSONArray jsonArray = new JSONArray(inputJson);
	  //System.out.println(jsonArray.toString());
	  JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);
	  JSONArray jsonArray3 = (JSONArray) jsonArray2.get(0); 
	  JSONArray jsonArrayDetect = (JSONArray) jsonArray.get(8);
	  JSONArray detect = (JSONArray) jsonArrayDetect.get(0);
	  //System.out.println(detect.get(0).toString());
	  
	  if(!(detect.get(0).toString().equals("en"))){
		  return "";
	  } else {
	  return jsonArray3.get(0).toString();
	 }
	 }
}
