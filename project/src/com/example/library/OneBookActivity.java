package com.example.library;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.library.async.BookGetterAsynTask;
import com.example.library.async.BorrowingReturnReminderAsynTask;
import com.example.library.model.datasource.CustomizationDataSource;
import com.example.library.model.entity.Book;
import com.example.library.model.entity.Customization;
import com.example.library.rest.ClientREST;
import com.example.library.view.BookHelperView;
import com.example.library.view.PlacementTextView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewManager;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class OneBookActivity extends BaseActivity  {

    private final static String LOG_TAG = OneBookActivity.class.getName(); 
    private ProgressDialog progressDialog;
    private CustomizationDataSource customizationDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        customizationDataSource = new CustomizationDataSource(getBaseContext());
        CustomizationDataSource.getDatabaseInstance();// it opens a connexion if null

        Log.d(LOG_TAG, "Creating OneBokActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customize_book_list); 
        View submitChangesBtn = findViewById(R.id.customizeConfirm);
        ((ViewManager) submitChangesBtn.getParent()).removeView(submitChangesBtn);

        Intent intent = getIntent();
        long bookId = 1l; //intent.getLongExtra("bookId", 0l);
        Log.d(LOG_TAG, "Book id is " + bookId); 

        final Resources resources = getResources();
        progressDialog = ProgressDialog.show(this, resources.getString(R.string.loading_title), resources.getString(R.string.book_progress), true);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface listener) {
                progressDialog.setMessage(resources.getString(R.string.book_back));
                progressDialog.setCancelable(false);
                progressDialog.show();
                Log.d(LOG_TAG, "Loading canceled");
                Intent intentBooksList = new Intent(getApplicationContext(), BookFragmentActivity.class);
                startActivity(intentBooksList);
            }
        });

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("activity", this);
        params.put("id", bookId);
        BookGetterAsynTask bookGetterAsynTask = new BookGetterAsynTask();
        bookGetterAsynTask.execute(params);
    } 

    public void fillUpBookData(Map<String, Object> result) {
        progressDialog.dismiss();
        if (!handleResponseType((Map<String, String>) result.get(ClientREST.KEY_STATE))) {
            Log.d(LOG_TAG, "Found book details");
            Book book = (Book) result.get("book");
            BookHelperView bookHelperView = new BookHelperView();    
            bookHelperView.setBook(book);
            bookHelperView.setActivity(this);
            List<Customization> customizations = customizationDataSource.getAllCustomizations();
            Log.d(LOG_TAG, "Found customizations list " + customizations);
            if (customizations != null && customizations.size() > 0) {
                for (Customization customization : customizations) {
                    Log.d(LOG_TAG, "Customizing for " +customization);
                    bookHelperView.constructFromId(getResources().getIdentifier(""+customization.getId(), "id", "com.example.library"), customization.getMethod());
                }
            }
        }
    }

}