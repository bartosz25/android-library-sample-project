package com.example.library;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.util.Log;

import com.example.library.rest.BookClientREST;
import com.example.library.rest.BorrowingClientREST;
import com.example.library.rest.SubscriberPreferencyClientREST;
import com.example.library.rest.LetterClientREST;

public class TestActivity extends BaseActivity {
    
	private final static String LOG_TAG = "TestActivity";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account_form);
        /*try {
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("from", "0");
        params.put("limit", "50");
        List<String> ids = new ArrayList<String>();
        ids.add("3");
        ids.add("6");
        ids.add("95");
        ids.add("104");
        Map<String, List<String>> criteria = new HashMap<String, List<String>>();
        criteria.put("id", ids);
        params.put("criteria", criteria);
        
        BookClientREST bookClientREST = new BookClientREST();
        bookClientREST.setActivity(this);

        Log.d(LOG_TAG, "Response from oneBook" + bookClientREST.getBook(3l));
        Log.d(LOG_TAG, "Response from listBooks" + bookClientREST.getBooks(params));
        

        BorrowingClientREST borrowingClientREST = new BorrowingClientREST();
        borrowingClientREST.setActivity(this);
        Log.d(LOG_TAG, "Borrowing response " + borrowingClientREST.getBorrowings(BorrowingClientREST.TYPE_TO_RETURN));
        
        
        SubscriberPreferencyClientREST subscriberPreferencyClientREST = new SubscriberPreferencyClientREST();
        subscriberPreferencyClientREST.setActivity(this);
        Log.d(LOG_TAG, "Preferency response " + subscriberPreferencyClientREST.getPreferencies(SubscriberPreferencyClientREST.TYPE_WEB_SERVICE));

        Map<String, String> preferencies = new HashMap<String, String>();
        preferencies.put("BORA", "TEST");
        preferencies.put("SYNC", "Do synchronization ?");
        preferencies.put("MANU", "Manual synchronization yes/no ?");
        preferencies.put("ABOU", "Test description about me");
        preferencies.put("REFE", "http://www.google.com");
        Log.d(LOG_TAG, "Preferency response " + subscriberPreferencyClientREST.postPreferencies(preferencies));
        
        
        LetterClientREST letterClientREST = new LetterClientREST();
        letterClientREST.setActivity(this);
        Log.d(LOG_TAG, "Received letters " + letterClientREST.getLetters(LetterClientREST.BY_BOOK));
        
        } catch(Exception e) {
        	Log.e(LOG_TAG, " An error ", e);
        	
        }*/
    }

    
}

/*checkBorrowingReturns();
BookClientREST bookClientREST = new BookClientREST();
bookClientREST.getBook(null, null);
bookClientREST.getBooks(BookClientREST.URL_BOOKS_PER_CATEGORY, null, null);
BorrowingClientREST borrowingClientREST = new BorrowingClientREST();
borrowingClientREST.getBorrowings(null, null);
SubscriberPreferencyClientREST subscriberPreferencyClientREST = new SubscriberPreferencyClientREST();
subscriberPreferencyClientREST.getPreferencies(null, null);
    	//Intent intentWaitingList = new Intent(getApplicationContext(), WaitingListActivity.class);
    	//startActivity(intentWaitingList);
    	 
    	//Intent intentSendSms = new Intent(getApplicationContext(), SendSMSActivity.class);
    	//startActivity(intentSendSms);
    	Intent intentCustomize = new Intent(getApplicationContext(), CustomizeActivity.class);
        startActivity(intentCustomize);
    	//Intent intentDisplayWebsite = new Intent(getApplicationContext(), DisplayWebsiteActivity.class);
        //startActivity(intentDisplayWebsite);
     
    
    public void showToast(String message) {
    	Log.d(LOG_TAG, "An tag messag"+message);
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        if(toast == null) Log.d(LOG_TAG, "Toast is null");
        else toast.show();
    }    
    	 * */