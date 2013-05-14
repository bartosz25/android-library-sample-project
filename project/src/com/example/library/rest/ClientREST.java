package com.example.library.rest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.example.library.converter.FromXmlToBookCopyConverter;
import com.example.library.logger.Logger;
import com.example.library.model.datasource.SubscriberDataSource;
import com.example.library.model.entity.Book;
import com.example.library.model.entity.Subscriber;

import android.app.Activity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public abstract class ClientREST {

    protected final static String LOG_TAG = ClientREST.class.getName(); 
    protected final static String METHOD_GET = "GET";
    protected final static String METHOD_POST = "POST";
    private final static String ENCODING = "UTF-8";
    private final static String POST_LANG_NAME = "lang";
    private final static String POST_DATEFORMAT_NAME = "date-format";
    private final static String POST_VERSION = "version";
    private final static String DEFAULT_DATEFORMAT = "yyyy-MM-dd";
    private final static String WEB_SERVICE_VERSION = "1.0";
    private final static String HASH_STRING = "0Q9FD8S11£SDQXCW%DSQD";
    protected final static String XPATH_CONFIG = "/response/configuration";
    protected final static String XPATH_STATE = "/response/header";
    public final static String KEY_CONFIG = "config";
    public final static String KEY_STATE = "state";
    private final static String BASE_URL = "http://bartosz-works.net/"; //"http://10.0.2.2/";
    protected Activity activity;
    public final static int PER_REQUEST = 50;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    public Activity getActivity() {
        return activity;
    }
    public String call(String url, String requestMethod, Document body) {
        // TODO : (plus tard) implémenter la gestion du https:// HttpsURLConnection ? 
        Log.d(LOG_TAG, "Generated XML " + body.asXML());
        StringBuffer xml = new StringBuffer();
        try {
            URL urlInst = new URL(BASE_URL+	url);
            HttpURLConnection connection = (HttpURLConnection) urlInst.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(60000); // 1 minute of timeout 
            // http://www.w3.org/TR/html401/interact/forms.html#h-17.13.4.1
            //connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("accessKey", constructAccessKey());
            connection.setRequestMethod(requestMethod);
            if(body != null) {
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(body.asXML());
                writer.flush();
                writer.close();
            }
            InputStream inputStream;
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();  
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                xml.append(line);
            }
            bufferedReader.close();
        } catch(Exception e) {
            Logger.addMessage(LOG_TAG, "An error occured on connecting to url : " + url + ". Error is : " + e.getMessage());
            Log.e(LOG_TAG, "An error occured on connecting to " + url, e);
            return null;
        }
        return xml.toString();
    }

    /**
     * XML getters
     */
    protected Map<String, String> getResponseState(Node stateNode) {
        Map<String, String> state = new HashMap<String, String>();
        List<Node> nodes = stateNode.selectNodes("./*");
        for (Node node : nodes) {
            state.put(node.getName(), node.getText());
        }
        Log.d(LOG_TAG, "Found state " + state);
        return state;
    }

    protected Map<String, String> getConfiguration(Node configNode) {
        Map<String, String> configuration = new HashMap<String, String>();
        List<Node> nodes = configNode.selectNodes("./*");
        for(Node node : nodes) {    
            configuration.put(node.getName(), node.getText());
        }
        Log.d(LOG_TAG, "Found configuration " + configuration);
        return configuration;
    }

    /**
     * XML constructors 
     */
    // params are extracted from base params passed to initial constructPostXml
    protected void constructConfig(Element root, Map<String, String> config) {
        if(config == null) config = new HashMap<String, String>();
        Element configElement = root.addElement("configuration");
        if (!config.containsKey(POST_LANG_NAME)) config.put(POST_LANG_NAME, ""+Locale.getDefault());
        if (!config.containsKey(POST_DATEFORMAT_NAME)) config.put(POST_DATEFORMAT_NAME, DEFAULT_DATEFORMAT);
        if (!config.containsKey(POST_VERSION)) config.put(POST_VERSION, WEB_SERVICE_VERSION);
        for (Map.Entry<String, String> entry : config.entrySet()) {
            Element oneEntry = configElement.addElement(entry.getKey()).addText(entry.getValue());
        } 
    }

    private String constructAccessKey() {
        String accessKey = "";
        SubscriberDataSource subscriberDataSource = new SubscriberDataSource(activity);
        Subscriber subscriber = subscriberDataSource.getSubscriber();
        if(subscriber != null) {
            try { 
                SecretKeySpec key = new SecretKeySpec((subscriber.getAccessKey()).getBytes("UTF-8"), "HmacSHA1");
                Mac mac = Mac.getInstance("Hmacf1");    
mac.init(key);
                byte[] bytes = mac.doFinal(HASH_STRING.getBytes("UTF-8"));
                accessKey = new String(Base64.encode(bytes, Base64.DEFAULT)).replace("\r\n", "");
            } catch (Exception e) {
                Logger.addMessage(LOG_TAG, "An error occured on constructing access key : " + e.getMessage());
                Log.e(LOG_TAG, "An exception occured on preparing accessKey for subscriber : " + subscriber  ,e);
            }
        }
        return accessKey;
    }

}