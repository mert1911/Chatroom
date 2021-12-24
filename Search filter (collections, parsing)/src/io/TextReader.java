package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import general.Parameters;

public class TextReader {
	 public static StringBuffer getText(String path){
	        StringBuffer sBuf = new StringBuffer();
	        try (BufferedReader bufR = new BufferedReader(new FileReader(path))){
	            String line;
	            while ((line = bufR.readLine()) != null){
	                sBuf.append(line + "\n");
	            }
	        } catch (IOException e){
	            System.out.println(e);
	        }
	        return sBuf;
	    }

	    public static void main(String[] args){
	    	// Test 
	        String path = Parameters.actorsFile;
	        //String path = Parameters.nomineesFile;
	        System.out.println(getText(path));
	    }
}
