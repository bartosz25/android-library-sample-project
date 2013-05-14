package com.example.library.logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.library.OneBookActivity;
import com.example.library.rest.BookClientREST;
import com.example.library.rest.LoggerClientREST;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

/**
 * This logger send all Exceptions to Library's Web Service. It helps us to fix bugs and make some improvements.
 * 
 * The logger's init() is called on onCreate().  
 * 
 * The message sending is done when : 
 * - they are some pending messages (messages != null && messages.size() > 0)
 * - or we call implicitly Logger.sendPending() method
 * 
 * @author bartosz
 *
 */
public class Logger  {
    private final static String LOG_TAG = Logger.class.getName();
    private static Map<String, List<String>> messages;
    private static Activity activity;
    private static Handler handler;

    public static void init() {
    	if (messages != null && messages.size() > 0) {
            Logger.sendPending();
    	} else if (messages == null) {
            messages = new HashMap<String, List<String>>();
        }
    }
        
    public static void addMessage(String code, String message) {
        if (messages == null) init();
        List<String> messagesList = (List<String>) messages.get(code);
        if (!messages.containsKey(code)) {
            messagesList = new ArrayList<String>();
        }
        Log.d(LOG_TAG, " Adding " + code + " to " + message);
        messagesList.add(message);
        messages.put(code, messagesList);
    }

    public static void setActivity(Activity act) {
        activity = act;
    }
    
    public static void sendPending() {
        if (messages != null && activity != null && messages.size() > 0) {
            if (handler == null) handler = new Handler();
            Runnable runnable = new Runnable() {
                 @Override
                 public void run() {
                     handler.post(new Runnable() {
                	     @Override
                	     public void run() {
                             try {
                                 Log.d(LOG_TAG, "Sending errors to web service : "+messages);
                                 LoggerClientREST loggerClientREST = new LoggerClientREST();
                                 loggerClientREST.setActivity(activity);
                                 loggerClientREST.sendMessages(messages);
                             } catch(Exception e) {
                                 Log.e(LOG_TAG, " An error occurred on generating message to log", e);
                             } finally {
                                 messages = null;	
                            }
                         }
                     });
	             }
	        };
            new Thread(runnable).start();
        }
    }
}