package com.example.library.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.example.library.BookFragmentActivity;
import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.adapter.LetterAdapter;
import com.example.library.async.LettersGetterAsynTask;
import com.example.library.model.entity.Book;
import com.example.library.model.entity.Letter;
import com.example.library.rest.LetterClientREST;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//        faire le chargement de la manière suivante : On charge les 50 premiers livres, lettre par lettre. A chaque chargement, on rajoute un 
//        loader dans la partie concernée (par exemple : je charge les livres "B", dans la liste, à côté de la lettre B, on affiche un loader.
//        En outre, au début du chargement on va scroller jusqu'à la lettre B et après le chargement, on va scroller au premier titre nouvellement chargé
public class BookFragment extends Fragment {

    private static final String LOG_TAG = BookFragment.class.getName();
    private ListView listView;
    private List<Letter> lettersList;
    private Activity activity;
    private ProgressDialog progressDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);         
        this.activity = activity;
    }

    @Override
    // Crée la vue associée au fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }

        LinearLayout fragmentLayout = (LinearLayout)inflater.inflate(R.layout.book_tab_content, container, false);
        listView = (ListView) fragmentLayout.findViewById(R.id.list1);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("activity", activity);
        params.put("bookFragment", this);
        if (activity instanceof BookFragmentActivity) {
            params.put("by", ((BookFragmentActivity) activity).lastTab.getRestTag());
            Log.d(LOG_TAG, "==========> Got = "+ ((BookFragmentActivity) activity).lastTab.getRestTag());
        } else {            
            params.put("by", LetterClientREST.BY_BOOK);
        }
        LettersGetterAsynTask lettersGetterAsynTask = new LettersGetterAsynTask();
        lettersGetterAsynTask.execute(params);
        progressDialog = ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.loading_title), getActivity().getResources().getString(R.string.book_progress), true);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { 
            @Override
            public void onCancel(DialogInterface listener) {
                progressDialog.setMessage(getActivity().getResources().getString(R.string.main_back));
                progressDialog.setCancelable(false);
                progressDialog.show();
                Log.d(LOG_TAG, "Loading canceled");
                Intent intentMain = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(intentMain);
            } 
        });
        return fragmentLayout;
    }

    public void appendLetters(Map<String, Object> result) {
        progressDialog.dismiss();
        Map<String, String> state = (Map<String, String>) result.get("state");
        if(!state.get("state").equals("error")) {
            lettersList = (List<Letter>) result.get("letters");

            listView.setAdapter(new LetterAdapter(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, lettersList, getActivity()));
            listView.setSelection(4);
            String booksQuantity = activity.getResources().getString(R.string.books_quantity);
            String booksQuantityFormatted = String.format(booksQuantity, "4", "500");
            ((BookFragmentActivity) activity).headerQuantity.setText(booksQuantityFormatted);

            String[] lettersArray = new String[lettersList.size()+1];
            lettersArray[0] = activity.getResources().getString(R.string.books_all);
            int key = 1;
            for (Letter letter : lettersList) {
                lettersArray[key] = letter.getCode();
                key++;
            }
            ((BookFragmentActivity) activity).setSpinnerAdapter(lettersArray);
        } else {
            Log.e(LOG_TAG, "An error occured on getting letters : " + state);
        }
    }

}