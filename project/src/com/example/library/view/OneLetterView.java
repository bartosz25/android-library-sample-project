package com.example.library.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.library.BookFragmentActivity;
import com.example.library.R;
import com.example.library.adapter.BookAdapter;
import com.example.library.async.BooksListGetterAsynTask;
import com.example.library.context.MainApplication;
import com.example.library.converter.UniversalConverter;
import com.example.library.model.datasource.CustomizationDataSource;
import com.example.library.model.entity.Book;
import com.example.library.model.entity.Customization;
import com.example.library.model.entity.Letter;
import com.example.library.rest.ClientREST;
import com.example.library.rest.LetterClientREST;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class OneLetterView extends LinearLayout {

    private final static String LOG_TAG = OneLetterView.class.getName();
    private List<Book> books;
    private TextView letterTextView, loaderText;
    private Context context;
    private LinearLayout listContainer;
    private int from = 0;
    private Letter letter;
    private Activity activity;
    private OneLetterView oneLetterView;
    private ProgressDialog progressDialog;
    //private ListView booksList;

    public OneLetterView(Context context) {
        super(context);
    }

    public OneLetterView(Context context, final Letter letter, final Activity activity) {
        super(context); 
        this.context = context;
        this.activity = activity;
        this.letter = letter;
        this.oneLetterView = this;
        Log.d(LOG_TAG, "After calling parent");
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        final LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        //    0x7f060043 = 2131099715, use inferior numbers
        Log.d(LOG_TAG, "Setting view " + UniversalConverter.fromStringToInt(letter.makeStringId()));
        setId(UniversalConverter.fromStringToInt(letter.makeStringId())); 
        Log.d(LOG_TAG, "Generating tag " + getId());

        final OneLetterView oneLetterView = this;

        Log.d(LOG_TAG, "Creating Letter TextView");
        listContainer = new LinearLayout(context);
        listContainer.setOrientation(LinearLayout.VERTICAL);
        listContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        letterTextView = new TextView(context);
        letterTextView.setText(letter.getCode());
        letterTextView.setTextAppearance(context, R.style.clickable_text);
        //letterTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP);
        letterTextView.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        letterTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        letterTextView.setWidth(150);
        letterTextView.setHeight(30);
        letterTextView.setOnClickListener(new BooksListener()); 

        container.addView(letterTextView);

        Log.d(LOG_TAG, "Creating counter TextView");
        TextView counterTextView = new TextView(context);
        counterTextView.setText("("+String.format(activity.getResources().getString(R.string.books_letter_quantity), ""+letter.getElementsCounter())+")");
        counterTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        counterTextView.setWidth(150);
        counterTextView.setHeight(30);
        container.addView(counterTextView);
        container.addView(listContainer);
        addView(container);
    }

    public OneLetterView(Context context, AttributeSet attrs) {
        super(context, attrs); 
    }

    public void appendBooks(Map<String, Object> result) {
        Map<String, String> state = (Map<String, String>) result.get("state");
        if(!state.get("state").equals("error")) {
            books = (List<Book>) result.get("books");
            BookAdapter adapter = new BookAdapter(context, android.R.layout.simple_list_item_1, books);    
            Log.d(LOG_TAG, "Adapter is " + adapter);
            Log.d(LOG_TAG, "Creating ListView");
            ListView booksList = new ListView(context);
            // TODO : test if (booksList == null) booksList = new ListView(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(300, 0); 
            int totalHeight = 0;
            Log.d(LOG_TAG, "Total height is " + totalHeight);
            int desiredWidth = MeasureSpec.makeMeasureSpec(booksList.getWidth(), MeasureSpec.AT_MOST);
            for(int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, booksList);
                //listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
                listItem.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                totalHeight += listItem.getMeasuredHeight();
                Log.d(LOG_TAG, "Adding height to totalHeight " + listItem.getMeasuredHeight());
                Log.d(LOG_TAG, "Adding height to totalHeight " + listItem.getHeight());
            }
            params.width = 500;
            params.height = totalHeight;
            booksList.setLayoutParams(params);

            booksList.setAdapter(adapter);
            listContainer.addView(booksList);
            Log.d(LOG_TAG, "After setting bookAdapter");

            Map<String, String> config = (Map<String, String>) result.get("config");
            from = UniversalConverter.fromStringToInt(config.get("from"))+ClientREST.PER_REQUEST;
            boolean hasNext = Boolean.valueOf(config.get("has-next"));

            if(hasNext && loaderText == null) {
                appendMoreTextView();
            } else if(!hasNext && loaderText != null) {
                listContainer.removeView(loaderText);
            } else if(hasNext && loaderText != null) {
                listContainer.removeView(loaderText);
                appendMoreTextView();
            }
        }
    }

    private void appendMoreTextView() {
        progressDialog.dismiss();
        loaderText = new TextView(context); 
        loaderText.setTextAppearance(context, R.style.clickable_text);    
        loaderText.setText(context.getResources().getString(R.string.load_more));
        loaderText.setOnClickListener(new BooksListener());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);
        loaderText.setLayoutParams(params);
        listContainer.addView(loaderText);
    }

    private class BooksListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final Resources resources = getResources();
            progressDialog = ProgressDialog.show(activity, resources.getString(R.string.loading_title), resources.getString(R.string.book_progress), true);
            progressDialog.setCancelable(true);

            BooksListGetterAsynTask booksListGetterAsynTask = new BooksListGetterAsynTask();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("activity", activity);
            params.put("oneLetterView", oneLetterView);
            Map<String, Object> reqParams = new HashMap<String, Object>();
            reqParams.put("limit", ClientREST.PER_REQUEST);
            reqParams.put("from", from);
            Map<String, List<String>> criteriaMap = new HashMap<String, List<String>>();
            List<String> elements = new ArrayList<String>();
            elements.add(letter.getCode());
            if (activity instanceof BookFragmentActivity) {
                reqParams.put("by", ""+((BookFragmentActivity) activity).lastTab.getRestTag());
                Log.d(LOG_TAG, "==========> Got = "+ ((BookFragmentActivity) activity).lastTab.getRestTag());
            } else {            
                reqParams.put("by", LetterClientREST.BY_BOOK);
            }
            criteriaMap.put("firstLetter", elements);
            reqParams.put("criteria", criteriaMap);
            params.put("params", reqParams);
            Log.d(LOG_TAG, "Doing call with request params " + reqParams);
            booksListGetterAsynTask.execute(params);
        }
    }

}