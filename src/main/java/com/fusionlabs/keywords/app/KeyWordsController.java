package com.fusionlabs.keywords.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * This controller takes keywords list from the main param
 * Then it takes first 10 keywords from the list and creates 10 parallel threads to call another webservice
 * If all calls are accepted, add 5 more keywords on the amount until the maximum requests number is reached
 * If during the process, any thread reports back failure, it should stop creating more threads;
 * and reduce the amount of calling to the actual successful calls number
 * 
 * The maximum requests to be made at the same time is set to 60
 * Also if the words left to search is less than the requests planed to make, reduce the number of calls as well
 * 
 * @author ruchun_jin
 *
 */
public class KeyWordsController {
	
	private static final Logger log = LoggerFactory.getLogger(KeyWordsController.class);
	
	private static int STARING_REQUESTS = 10;
	private static int MAX_REQUESTS = 60;
	
    public static void main(String[] keywords) {
    	
    	final List<String> keywordsList = Arrays.asList(keywords);
    	
    	int startIndex = 0, planCalled = STARING_REQUESTS, kSize = keywordsList.size(), keywordsLeft = kSize;
    	List<String> subKeywordsList = keywordsList.subList(startIndex, startIndex + planCalled);
    	
        for (;keywordsLeft > 0;) {
        	int actualCalled = acceptedCallsMade(subKeywordsList);
        	keywordsLeft -= actualCalled;
        	startIndex += actualCalled;
        	
        	planCalled = (int) (actualCalled < planCalled ? actualCalled : (planCalled + STARING_REQUESTS/2));      	
        	
        	if (MAX_REQUESTS < planCalled) planCalled = MAX_REQUESTS;       	
        	if (keywordsLeft < planCalled) planCalled = keywordsLeft;
        	
        	subKeywordsList = keywordsList.subList(startIndex, startIndex + planCalled);
        }
    }
    
    public static int acceptedCallsMade (List<String> keywordsList) {
    	
    	List<KeyWordCaller> keyCallers = new ArrayList<KeyWordCaller>();
    	KeyWordCaller.setCallUnsuccessful(false); 	
    	try {
    		
    		KeyWordCaller caller = null;
    		
    		for (String keyword : keywordsList) {
        		
        		if (KeyWordCaller.isCallUnsuccessful()) break;
        		
        		caller = new KeyWordCaller(keyword);
        		keyCallers.add(caller);
        		caller.call();
        	}
    	
			Thread.sleep(2000);
			
		} catch (InterruptedException e) {
			System.out.println("Thread got interrupted : " + e.getLocalizedMessage());
			log.error(e.getLocalizedMessage());
		}
    	
    	for (KeyWordCaller caller : keyCallers) {
    		
    		if (caller.getResults() == null || caller.getResults().isEmpty()) {
    			keyCallers.remove(caller);
    		}
    	}
    	
    	return keyCallers.size();
    }
}
