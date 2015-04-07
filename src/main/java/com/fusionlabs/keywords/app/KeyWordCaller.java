package com.fusionlabs.keywords.app;

import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * This caller is responsible for calling the external service to do the categorizing of the keywords
 * The static flag CALL_UNSUCCESSFUL is the flag to tell controller that from this request, the call has failed
 * 
 * @author ruchun_jin
 *
 */
public class KeyWordCaller implements Runnable {
	
	private Thread t;
	private String params;
	private List<AnalysisResult> results;
	
	private static String MAIN_URL = "http://somecompany.com/api/categorise";
	private static Boolean CALL_UNSUCCESSFUL = Boolean.FALSE;
	
	public KeyWordCaller(String keyword) {
		this.params = "apikey=ABC123&keyword=" + keyword;
	}

	public void run() {
		
		WebTarget target = ClientBuilder.newClient().target(MAIN_URL);
		
		long callStartTime = System.currentTimeMillis();
		ClientResponse response = target.request().post(Entity.text(getParams()), ClientResponse.class);
		
		if (response.getStatus() != 200 || (System.currentTimeMillis() - callStartTime) > 2000) {
			setCallUnsuccessful(true);
		} else {
			String jsonResult = response.readEntity(String.class);
			results = new Gson().fromJson(jsonResult, new TypeToken<List<AnalysisResult>>(){}.getType());
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

	public static Boolean isCallUnsuccessful() {
		return CALL_UNSUCCESSFUL;
	}

	public static void setCallUnsuccessful(boolean callUnsuccessful) {
		KeyWordCaller.CALL_UNSUCCESSFUL = callUnsuccessful;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

}
