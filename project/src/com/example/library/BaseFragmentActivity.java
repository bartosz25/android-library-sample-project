package com.example.library;
 
import java.util.HashMap;

import com.example.library.model.entity.TabInfo;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

public class BaseFragmentActivity extends BaseActivity implements TabHost.OnTabChangeListener {

    private final static String LOG_TAG = BaseFragmentActivity.class.getName();
    protected TabHost tabHost;
    protected HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
    protected TabInfo lastTabOpened = null;
    public TabInfo lastTab = null;

    // rajoute un onglet
    protected void addTab(BaseFragmentActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        Log.d(LOG_TAG, "Adding tab : " + tabInfo.getTag());
    	// Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        String tag = tabSpec.getTag();
        // Check to see if we already have a fragment for this tab, probably
        // from a previously saved state.  If so, deactivate it, because our
        // initial state is that a tab isn't shown.
        tabInfo.setFragment(activity.getSupportFragmentManager().findFragmentByTag(tag));
        if (tabInfo.getFragment() != null && !tabInfo.getFragment().isDetached()) {
            Log.d(LOG_TAG, "Fragment isn't detached; Detach it to TabInfo : " + tabInfo.getTag());
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.detach(tabInfo.getFragment());
            ft.commit();
            activity.getSupportFragmentManager().executePendingTransactions();
        }
        tabHost.addTab(tabSpec);
        mapTabInfo.put(tabInfo.getTag(), tabInfo);
    }

    class TabFactory implements TabContentFactory {
        private final Context mContext;

        public TabFactory(Context context) {
            mContext = context;
        }
        public View createTabContent(String tag) {
            Log.d(LOG_TAG, "Creating tab content for tag" + tag);
            View view = new View(mContext);
            view.setMinimumWidth(0);
            view.setMinimumHeight(0);
            return view;
        }
    }

    @Override
    public void onTabChanged(String tag) {
        Log.d(LOG_TAG, "Tab was changed : " + tag);
        TabInfo newTab = (TabInfo) this.mapTabInfo.get(tag);
        if (lastTab != newTab) {
            Log.d(LOG_TAG, "Tab was changed (lastTab isn't the same as newTab) : " + tag);
            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            if (lastTab != null) {
                if (lastTab.getFragment() != null) {
                    ft.detach(lastTab.getFragment());
                    Log.d(LOG_TAG, "FragmentTransaction detaching lastTab : " + lastTab.getTag());
                }
            }
            if (newTab != null) {
                if (newTab.getFragment() == null) {
                    newTab.setFragment(Fragment.instantiate(this,
                    newTab.getClss().getName(), newTab.getArgs()));
                    // add = ajout d'un Fragment dans l'état d'une activité
                    ft.add(R.id.realtabcontent, newTab.getFragment(), newTab.getTag());
                    Log.d(LOG_TAG, "FragmentTransaction adding new Fragment : " + newTab.getTag());
                } else {
                    // attach = attachement d'un Fragment dans l'état d'une activité 
                    //          Un Fragment peut être attaché uniquement quand il a été préalablément détaché, ce qui est forcément
                    //          le cas avec ft.detach() de l'onglet récemment ouvert.
                    ft.attach(newTab.getFragment());
                    Log.d(LOG_TAG, "Attaching newTab fragment : " + tag);
                }
            }
            lastTab = newTab;
            ft.commit(); // envoi des opérations sur FragmentTransactions
            // la méthode ci-dessous doit être appélée après ft.commit(). 
            // elle classe la transaction en étant celle qui peut être exécutée d'une manière asynchrone par le thread principal
            this.getSupportFragmentManager().executePendingTransactions();
        }
    }

}
