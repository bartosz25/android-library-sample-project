package com.example.library;

import java.util.HashMap;
import android.widget.LinearLayout;
import java.util.List;

import com.example.library.context.MainApplication;
import com.example.library.fragment.BookFragment;
import com.example.library.model.datasource.CustomizationDataSource;
import com.example.library.model.entity.Letter;
import com.example.library.model.entity.TabInfo;
import com.example.library.rest.LetterClientREST;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView; 
import android.view.MotionEvent;

public class BookFragmentActivity extends BaseFragmentActivity /*implements TabHost.OnTabChangeListener */ {

    private static final String LOG_TAG = BookFragmentActivity.class.getName();
    private static final String TAB_BOOKS = "Books";
    private static final String TAB_CATEGORIES = "Categories";
    private static final String TAB_WRITERS = "Writers";
    private TabHost mTabHost;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
    private TabInfo mLastTab = null;
    private CustomizationDataSource customizationDataSource;
    private String tabTitles, tabCategories, tabWriters;
    public TextView headerQuantity;
    public String[] letters;
    private boolean wasTouched;
    private MainApplication app;
    private Spinner spinnerChoices;
    private BookFragmentActivity parentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customizationDataSource = new  CustomizationDataSource(this);
        CustomizationDataSource.getDatabaseInstance();// it opens a connexion if null);
        setContentView(R.layout.activity_books_list);
        Resources resources = getResources();
        // it will be available for fragment.BookFragment to put the quantity of loaded books and of all available books
        headerQuantity = (TextView) findViewById(R.id.headerNb);

        app = (MainApplication) getApplication();
        parentActivity = this;

        String showBooks = resources.getString(R.string.do_sort_text);
        //final String[] letters = resources.getStringArray(R.array.book_choices);
        spinnerChoices = (Spinner) findViewById(R.id.headerSortOptions);
        spinnerChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // un autre problème : quand on scrolle dans la liste qui contient les livres affichés, souvent les livres
                // se cachent (malgré le fait qu'ils sont déjà chargés); voir si ce n'est pas lié à l'utilisation du cache dans
                // OneLetterView
                // après le choix, scroller à la lettre sélectionnée avec ListView.setSelection(3)
                // régler un problème où :
                // je vois les lettres C, D, E
                // je coche l'affichage de la lettre H (que je ne vois pas)
                // les lettres C, D, E restent affichées
                // je scrolle jusqu'à la lettre H, je ne vois plus les lettres C, D, E
                // je remonte - les lettres C, D, E sont cachées tandis qu'elles devrait l'être dès le début : après la sélection de la lettre
                app.setDisplayedLetter(letters[position]);
                if (wasTouched) { 
                    if (position > 0) {
                        changeVisibility(View.INVISIBLE);
                        Log.d(LOG_TAG, "=========> Showing id 100"+id);
                        int idView = parentActivity.getResources().getIdentifier(Letter.makeStaticStringId((int)id), "layout", "com.example.library");
                        Log.d(LOG_TAG, "Selected id" + letters[position]);
                        LinearLayout displayedLetter = (LinearLayout) parentActivity.findViewById(idView);
                        Log.d(LOG_TAG, "Found " + displayedLetter);
                        if (displayedLetter != null) {
                            displayedLetter.setVisibility(View.VISIBLE);
                            Log.d(LOG_TAG, "====> " + displayedLetter.getParent());
                            ((ListView) displayedLetter.getParent()).smoothScrollBy(0, 0); // Stops the listview from overshooting.
                            //listView.setSelection(0);
                        }
                    } else {
                        changeVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinnerChoices.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                wasTouched = true;
                return false;
            }
        }); 

        tabTitles = resources.getString(R.string.tab_books);
        tabCategories = resources.getString(R.string.tab_categories);
        tabWriters = resources.getString(R.string.tab_writers);
        // Step 2: Setup TabHost
        initialiseTabHost(savedInstanceState);
 
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
    } 

    // sauvegarde l'état actuel du Fragment. Grâce à cette méthode on peut reconstruire cet état après le redémarrage de l'activité
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }

    // initialisation des TabHost
    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        addTab(this, this.mTabHost, this.mTabHost.newTabSpec(TAB_BOOKS).setIndicator(tabTitles), new TabInfo(TAB_BOOKS, BookFragment.class, args, this, LetterClientREST.BY_BOOK));
        addTab(this, this.mTabHost, this.mTabHost.newTabSpec(TAB_CATEGORIES).setIndicator(tabCategories), new TabInfo(TAB_CATEGORIES, BookFragment.class, args, this, LetterClientREST.BY_CATEGORY));
        addTab(this, this.mTabHost, this.mTabHost.newTabSpec(TAB_WRITERS).setIndicator(tabWriters), new TabInfo(TAB_WRITERS, BookFragment.class, args, this, LetterClientREST.BY_WRITER));
        // Default to first tab
        this.onTabChanged(TAB_BOOKS);
        mTabHost.setOnTabChangedListener(this);
    }

    private void changeVisibility(int visibility) {
        for (int i = 0; i < letters.length; i++) {
            int idView = parentActivity.getResources().getIdentifier(Letter.makeStaticStringId(i), "layout", "com.example.library");
            LinearLayout layout = ((LinearLayout) parentActivity.findViewById(idView));
            if (layout != null) {
                Log.d(LOG_TAG, "Found id for idView " + idView);
                layout.setVisibility(visibility);
            }
        }
    }

    public void setSpinnerAdapter(String[] letters) {
        this.letters = letters;
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.letters);
        spinnerChoices.setAdapter(spinnerArrayAdapter);
    }

}