package com.example.library.view;

import java.util.List;

import com.example.library.OneBookActivity;
import com.example.library.R;
import com.example.library.logger.Logger;
import com.example.library.model.datasource.CustomizationDataSource;
import com.example.library.model.entity.Book;
import com.example.library.model.entity.Customization;
import com.example.library.model.datasource.WaitingListDataSource;

import android.widget.LinearLayout.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout; 
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OneBorrowingView extends RelativeLayout {

    private final static String LOG_TAG = OneBorrowingView.class.getName();

    public OneBorrowingView(final Context context, final Book book, List<Customization> customizations) {
        super(context); 
        try {
            LinearLayout container = new LinearLayout(context);

            TextView header = new TextView(context);
            TextView delete = new TextView(context);
            header.setId(1);
            delete.setId(2);

            header.setText(book.getTitle());
            RelativeLayout.LayoutParams headerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT); 
            headerParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            headerParams.addRule(RelativeLayout.LEFT_OF, delete.getId());
            // setMargins : left, top, right, bottom
            headerParams.setMargins(10, 0, 0, 0);
            header.setLayoutParams(headerParams);

            RelativeLayout.LayoutParams deleteParams = new RelativeLayout.LayoutParams(       RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);            deleteParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT); 
            deleteParams.setMargins(0, 0, 10, 0);
            delete.setLayoutParams(deleteParams);

            final Resources resources = context.getResources();
            delete.setText(resources.getString(R.string.delete));
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(LOG_TAG, "CLicked on view " + view);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    // set title
                    alertDialogBuilder.setTitle(resources.getString(R.string.delete_title));
                    // set dialog message
                    alertDialogBuilder
                    .setMessage(resources.getString(R.string.delete_confirm))
                    .setCancelable(true)
                    .setPositiveButton(resources.getString(R.string.delete_do), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d(LOG_TAG, "Clicked on YES");
                            WaitingListDataSource waitingListDataSource = new WaitingListDataSource(context);
                            if(!waitingListDataSource.deleteItem((int)book.getId())) {
                                Toast.makeText(context, resources.getString(R.string.error_deleting_waiting_list), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton(resources.getString(R.string.cancel),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            Log.d(LOG_TAG, "Clicked on NO");
                            dialog.cancel();
                        }
                    });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
            });
            addView(header);
            addView(delete);
        } catch(Exception e) {
            Logger.addMessage(LOG_TAG, "An error occured on generating borrowing view : " + e.getMessage());
            Log.d(LOG_TAG, " ANn", e);
        }
    }

    public OneBorrowingView(Context context) {
        super(context);
    }

}