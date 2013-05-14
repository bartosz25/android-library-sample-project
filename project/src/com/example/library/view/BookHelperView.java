package com.example.library.view;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.logger.Logger;
import com.example.library.model.entity.Book;
import com.example.library.model.entity.Writer;
import com.example.library.OneBookActivity;
import com.example.library.R;
import com.example.library.model.datasource.WaitingListDataSource;
import com.example.library.SendSmsActivity;
import com.example.library.async.BorrowingMakerAsynTask;
import com.example.library.converter.UniversalConverter;

public class BookHelperView {

    private static final String LOG_TAG = BookHelperView.class.getName();
    private Book book;
    private Activity activity;
    private LinearLayout.LayoutParams paramsMargin;

    public BookHelperView() {
        getParamsMargin();
    }
    public Book getBook() {
        return book;
    }
    public Activity getActivity() {
        return activity;
    } 
    public LinearLayout.LayoutParams getParamsMargin() {
        paramsMargin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsMargin.setMargins(5, 0, 5, 0);
        return paramsMargin;
    }

    public void setBook(Book book) {
        this.book = book;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void constructFromId(int viewId, String method) {
        try {
            PlacementTextView placementView = (PlacementTextView) activity.findViewById(viewId);
            Method toInvoke = getClass().getDeclaredMethod(method, new Class[]{PlacementTextView.class});
            toInvoke.invoke(this, placementView);
        } catch(Exception e) {
            Logger.addMessage(LOG_TAG, "An error occured on calling method dynamically ("+method+") : " + e.getMessage());
            Log.e(LOG_TAG, "An error occured on calling method "+method+" dynamically", e);
        }
    }

    /**
     * Utilitary methods, used to fill up PlacementContextView with Book data.
     */
    public void constructTitle(PlacementTextView placementView) {
        Log.d(LOG_TAG, "Got " + book.getTitle());
        placementView.setText(book.getTitle());
        placementView.setLayoutParams(getParamsMargin());
    }

    public void constructLead(PlacementTextView placementView) {
        Log.d(LOG_TAG, "Got " + book.getWritersString());
        placementView.setText(book.getWritersString());
        placementView.setLayoutParams(getParamsMargin());
    }

    public void constructContent(PlacementTextView placementView) {
        Log.d(LOG_TAG, "Got " + book.getDescription());
        placementView.setText(book.getDescription());
        placementView.setLayoutParams(getParamsMargin());
    }

    public void constructFooter(PlacementTextView placementView) {
        placementView.setVisibility(View.INVISIBLE);
        Log.d(LOG_TAG, "Got footer");
        LinearLayout footerLayout = (LinearLayout) activity.findViewById(R.id.footerActions); 

        final Resources resources = activity.getResources();
        TextView textBorrow = new TextView(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View borrowingView = inflater.inflate(R.layout.borrowing_form, (ViewGroup) null, true);
        final DatePicker datePicker = (DatePicker) borrowingView.findViewById(R.id.borrowingDatePicker);
        //datePicker.setMinDate(UniversalConverter.fromDateTimeToLong(new Date()));
        textBorrow.setText(resources.getString(R.string.action_borrow));
        textBorrow.setTextAppearance(activity, R.style.clickable_text);
        textBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "CLicked on view " + view);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setTitle(resources.getString(R.string.borrow_title));  
                alertDialogBuilder.setView(borrowingView);
                alertDialogBuilder.setCancelable(true)
                .setPositiveButton(resources.getString(R.string.borrow_do), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { 
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, datePicker.getYear());
                        calendar.set(Calendar.MONTH, datePicker.getMonth());
                        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("activity", activity);
                        params.put("id", book.getId());
                        params.put("date", calendar.getTime());
                        BorrowingMakerAsynTask borrowingMakerAsynTask = new BorrowingMakerAsynTask();
                        borrowingMakerAsynTask.execute(params);
                        Log.d(LOG_TAG, "Choosed date " + datePicker.getDayOfMonth() + "-" + datePicker.getMonth() + "-"+ datePicker.getYear());
                    }
                })
                .setNegativeButton(resources.getString(R.string.cancel),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });

        TextView textAdd = new TextView(activity);
        textAdd.setTextAppearance(activity, R.style.clickable_text);
        textAdd.setText(resources.getString(R.string.action_add));
        textAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    WaitingListDataSource waitingListDataSource = new WaitingListDataSource(activity.getBaseContext());
                    Map<String, Object> result = waitingListDataSource.insertBook(book);
                    int messageId = resources.getIdentifier((String)result.get("message"), "string", "com.example.library");
                    String message = resources.getString(messageId);
                    String complement = (String) result.get("messageComplement");
                    // TODO : valider ce fragment et voir si le message qui s'affiche est correct
                    if(!(Boolean) result.get("result")) { 
                        complement = resources.getString(resources.getIdentifier(complement, "string", "com.example.library"));
                    }
                    message = String.format(message, complement);
                    // book already added
                    String alreadyAdded = (String) result.get("alreadyAdded");
                    if(alreadyAdded.length() > 0) {
                        String messageAlready = resources.getString(R.string.error_already_saved_books);
                        Log.d(LOG_TAG, "Some books wasn't added");
                        message = message + String.format(messageAlready, alreadyAdded);
                    }

                    Toast toast = Toast.makeText(activity, message, Toast.LENGTH_LONG);
                    toast.show();
                } catch(Exception e) {
                    Logger.addMessage(LOG_TAG, "An error occured on generating book view : " + e.getMessage());
                    Log.e(LOG_TAG, "AAA",e);
                }
            }			
        });
        //textAdd.setPadding(0, 0, 5, 0);

        LinearLayout.LayoutParams paramsSeparator = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT);
        paramsSeparator.setMargins(5, 0, 5, 0);
        TextView separatorAdd = new TextView(activity);
        separatorAdd.setLayoutParams(paramsSeparator);
        separatorAdd.setBackgroundResource(R.drawable.black_right_border);
        // TODO : voir si l'on ne peut pas cloner separatorAdd;
        TextView separatorBorrow = new TextView(activity);
        separatorBorrow.setLayoutParams(paramsSeparator);
        separatorBorrow.setBackgroundResource(R.drawable.black_right_border);

        TextView textSms = new TextView(activity);
        textSms.setText(resources.getString(R.string.action_sms));
        textSms.setTextAppearance(activity, R.style.clickable_text);
        textSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getBaseContext(), SendSmsActivity.class);
                intent.putExtra("bookId", book.getId());
                intent.putExtra("bookTitle", book.getTitle());
                intent.putExtra("bookUrl", book.getUrl());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.getBaseContext().startActivity(intent);
            }
        });

        footerLayout.addView(textBorrow);
        footerLayout.addView(separatorBorrow);
        footerLayout.addView(textAdd);
        footerLayout.addView(separatorAdd);
        footerLayout.addView(textSms);
        footerLayout.setVisibility(View.VISIBLE);
        footerLayout.setLayoutParams(getParamsMargin());
    }

}