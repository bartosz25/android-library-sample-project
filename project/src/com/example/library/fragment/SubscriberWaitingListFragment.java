package com.example.library.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.example.library.BookFragmentActivity;
import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.adapter.BorrowingAdapter;
import com.example.library.adapter.LetterAdapter;
import com.example.library.async.BookGetterAsynTask;
import com.example.library.async.BooksWaitingListGetterAsynTask;
import com.example.library.converter.UniversalConverter;
import com.example.library.model.datasource.WaitingListDataSource;
import com.example.library.model.entity.Book;
import com.example.library.model.entity.Letter;

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
import com.example.library.rest.BookClientREST;
import com.example.library.rest.ClientREST;

public class SubscriberWaitingListFragment extends Fragment {

    private static final String LOG_TAG = SubscriberWaitingListFragment.class.getName(); 
    private LinearLayout layout;
    private ViewGroup container;
    private ProgressDialog progressDialog;
    private List<Book> books;
    private int from = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        this.container = container;
        layout = (LinearLayout) inflater.inflate(R.layout.my_account_waiting_list, container, false);

        WaitingListDataSource waitingListDataSource = new WaitingListDataSource(container.getContext());
        List<Integer> ids = waitingListDataSource.getWaitingIds();
        if(ids == null || ids.size() == 0) {
            ids = new ArrayList<Integer>();
            ids.add(33);
            ids.add(45);
        }
        Log.d(LOG_TAG, "Found ids " + ids);
        if(ids != null && ids.size() > 0) {
            // chargement a lieu uniquement quand on clique sur l'onglet
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("activity", getActivity());
            params.put("subscriberWaitingListFragment", this);
            params.put("ids", ids);
            Map<String, Object> reqParams = new HashMap<String, Object>();
            reqParams.put("limit", ClientREST.PER_REQUEST);
            reqParams.put("from", from);
            Map<String, List<String>> criteriaMap = new HashMap<String, List<String>>();
            List<String> elements = new ArrayList<String>();
            for (Integer id : ids) {
                elements.add(""+id);
            }
            criteriaMap.put("id", elements);
            reqParams.put("criteria", criteriaMap);
            params.put("params", reqParams);
            BooksWaitingListGetterAsynTask booksWaitingListGetterAsynTask = new BooksWaitingListGetterAsynTask();
            booksWaitingListGetterAsynTask.execute(params);
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
        }
        return layout;
    }

    public void constructBooksList(Map<String, Object> result) {
        progressDialog.dismiss();
        Map<String, String> state = (Map<String, String>) result.get("state");
        if(!state.get("state").equals("error")) {
            books = (List<Book>) result.get("books");
            BorrowingAdapter adapter = new BorrowingAdapter(container.getContext(), android.R.layout.simple_list_item_1, books);
            ((ListView) layout.findViewById(R.id.waitingBooksList)).setAdapter(adapter);

            Map<String, String> config = (Map<String, String>) result.get("config");
            from = (UniversalConverter.fromStringToInt(config.get("from")+ClientREST.PER_REQUEST));
        }
    }

}