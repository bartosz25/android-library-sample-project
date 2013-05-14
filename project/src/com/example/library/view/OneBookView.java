package com.example.library.view;

import java.util.List;

import com.example.library.OneBookActivity;
import com.example.library.R;
import com.example.library.model.datasource.CustomizationDataSource;
import com.example.library.model.entity.Book;
import com.example.library.model.entity.Customization;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout; 
import android.widget.TextView;

public class OneBookView extends LinearLayout {

    private final static String LOG_TAG = OneBookView.class.getName();

    public OneBookView(final Context context, final Book book) {
        super(context);
        Log.d(LOG_TAG, "Context is " + context);
        Log.d(LOG_TAG, "Book is " + book.getTitle());

        LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LinearLayout container = new LinearLayout(context);
        container.setLayoutParams(params);
        // CES DEUX PARAMETRES POSENT PROBLEME : à remplacer par un LinearLayout contenu dans OneBookView
        //setOrientation(LinearLayout.VERTICAL);
        //setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        TextView header = new TextView(context);
        header.setTextAppearance(context, R.style.clickable_text);
        header.setText(book.getTitle());
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OneBookActivity.class);
                intent.putExtra("bookId", book.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        container.addView(header);
        addView(container);
    }

    public OneBookView(Context context) {
        super(context);
        Log.d(LOG_TAG, "calling normal constructor");
    }

}