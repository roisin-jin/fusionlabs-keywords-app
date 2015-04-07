package com.fusionlabs.keywords.app;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class KeyWordCaller implements Runnable {
	
	private Thread t;
	private String keyword;
	private List<AnalysisResult> results;
	private String jsonResult;
	
	private static String MAIN_URL = "http://somecompany.com/api/categorise?apikey=ABC123&keyword=";
	private static Boolean CALL_UNSUCCESSFUL = Boolean.FALSE;
	
	public KeyWordCaller(String keyword) {
		this.keyword = keyword;
	}

	public void run() {
		// TODO Auto-generated method stub
		long callStartTime = System.currentTimeMillis();
		
		
		
		if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - callStartTime) > 2 && jsonResult == null) {
			setCallUnsuccessful(true);
		}
	}
	
	public void call() {
		
		if (t == null) {
			t = new Thread(this);
	        t.start ();
	    }
	}
	
	public void terminateThreadsIfCallingUnsuccessfull() {
		
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Thread getT() {
		return t;
	}

	public void setT(Thread t) {
		this.t = t;
	}

	public List<AnalysisResult> getResults() {
		return results;
	}

	public void setResults(List<AnalysisResult> results) {
		this.results = results;
	}

	public String getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(String jsonResult) {
		this.jsonResult = jsonResult;
	}

	public static Boolean isCallUnsuccessful() {
		return CALL_UNSUCCESSFUL;
	}

	public static void setCallUnsuccessful(boolean callUnsuccessful) {
		KeyWordCaller.CALL_UNSUCCESSFUL = callUnsuccessful;
	}

}
