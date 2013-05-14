package com.example.library;
  
import java.util.HashMap; 
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Typeface;
import android.os.Bundle; 
import android.os.Environment;
import android.util.Log; 
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView; 
import android.widget.Toast;

import com.example.library.logger.Logger;
import com.example.library.model.datasource.CustomizationDataSource;
import com.example.library.model.entity.Customization;
import com.example.library.view.PlacementTextView;
// TODO : (plus tard) remplacer le texte Actions par les icônes correspondantes
// TODO : (plus tard) pour éviter de rajouter trop d'OnTouchListeners, on va implémenter le déplacement des PlacementTextView actuels avec les coordonnées X et Y
public class CustomizeActivity extends BaseActivity {

    private final static String LOG_TAG = CustomizeActivity.class.getName();
    public final static String PLACEMENT_HEADER = "HEADER";
    public final static String PLACEMENT_LEAD = "LEAD";
    public final static String PLACEMENT_CONTENT = "CONTENT";
    public final static String PLACEMENT_FOOTER = "FOOTER";
    private PlacementTextView layoutHeader, layoutLead, layoutDesc, layoutFooter;
    private Map<String, String> layoutPlaces = new HashMap<String, String>();
    private CustomizationDataSource customizationDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            customizationDataSource = new CustomizationDataSource(getBaseContext());
            CustomizationDataSource.getDatabaseInstance();// it opens a connexion if null
            Log.d(LOG_TAG, "Creating CustomizeActivity");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.customize_book_list);

            Log.d(LOG_TAG, "Home dir is "+ getApplicationInfo().dataDir);

            List<Customization> customizations = customizationDataSource.getAllCustomizations();
            Log.d(LOG_TAG, "Found customizations list " + customizations);
            if (customizations != null && customizations.size() > 0) {
                for (Customization customization : customizations) {
                    // get string label
                    int stringId = getResources().getIdentifier(customization.getText(), "string", "com.example.library");
                    String text = getResources().getString(stringId);
                    // get view
                    int resId = getResources().getIdentifier(""+customization.getId(), "id", "com.example.library");
                    switch(resId) {
                        case R.id.customizeHeaderContainer:
                            layoutHeader = (PlacementTextView) findViewById(resId);
                            layoutHeader.setPlacement(customization.getPlace());
                            layoutHeader.setText(text);
                            layoutHeader.setMethod(customization.getMethod());
                            layoutHeader.setTextId(customization.getText());
                            layoutHeader.setResString(customization.getId());
                        break;
                        case R.id.customizeWriterContainer:
                            layoutLead = (PlacementTextView) findViewById(resId);
                            layoutLead.setPlacement(customization.getPlace());
                            layoutLead.setText(text);
                            layoutLead.setMethod(customization.getMethod());
                            layoutLead.setTextId(customization.getText());
                            layoutLead.setResString(customization.getId());
                        break;
                        case R.id.customizeDescContainer:
	                        layoutDesc = (PlacementTextView) findViewById(resId);
                            layoutDesc.setPlacement(customization.getPlace());
                            layoutDesc.setText(text);
                            layoutDesc.setMethod(customization.getMethod());
                            layoutDesc.setTextId(customization.getText());
	                       layoutDesc.setResString(customization.getId());
                        break;
                        case R.id.customizeActionsContainer:
                            layoutFooter = (PlacementTextView) findViewById(resId);
                            layoutFooter.setPlacement(customization.getPlace());
                            layoutFooter.setText(text);
                            layoutFooter.setMethod(customization.getMethod());
                            layoutFooter.setTextId(customization.getText());
                            layoutFooter.setResString(customization.getId());
                        break;
                    }
                }
                layoutHeader.setOnDragListener(new DragListener());
                layoutHeader.setOnTouchListener(new TouchListener());

                layoutLead.setOnDragListener(new DragListener());
                layoutLead.setOnTouchListener(new TouchListener());

                layoutDesc.setOnDragListener(new DragListener());
                layoutDesc.setOnTouchListener(new TouchListener());

                layoutFooter.setOnDragListener(new DragListener());
                layoutFooter.setOnTouchListener(new TouchListener());

                layoutPlaces.put(layoutHeader.getPlacement(), layoutHeader.getResString());
                layoutPlaces.put(layoutLead.getPlacement(), layoutLead.getResString());
                layoutPlaces.put(layoutDesc.getPlacement(), layoutDesc.getResString());
                layoutPlaces.put(layoutFooter.getPlacement(), layoutFooter.getResString());
            } else {
                Logger.addMessage(LOG_TAG, "An error ocurred on initializing CustomizeActivity : customizations can't be read from the database");
                Log.e(LOG_TAG, "An error ocurred on initializing CustomizeActivity : customizations can't be read from the database");
                Toast.makeText(this, getResources().getString(R.string.error_loading_customizations), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Logger.addMessage(LOG_TAG, "An error occured on customizing : " + e.getMessage());
            Log.e(LOG_TAG, " An error occured", e);
        }
    }

    private final class DragListener implements OnDragListener {
        // View => élément qui reçoit le drag
        // DragEvent => élément qui est draggé
        @Override
        public boolean onDrag(View view, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d(LOG_TAG, "ACTION_DRAG_STARTED");
                    // quand on commence le drag, la zone se met en vert
                    //view.setBackgroundColor(getResources().getColor(R.color.green));
                    // Do nothing
                break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d(LOG_TAG, "ACTION_DRAG_ENTERED");
                    // quand on place l'élément draggable dans la zone
                    view.setBackgroundColor(getResources().getColor(R.color.pomegranate));
                break;
                case DragEvent.ACTION_DRAG_EXITED: 
                    // quand on quitte la zone de drag
                    Log.d(LOG_TAG, "ACTION_DRAG_EXITED");
                    Log.d(LOG_TAG, "View is" + view);
                    Log.d(LOG_TAG, "Drawable is " + getResources().getDrawable(R.drawable.black_bottom_border));
                    //view.setBackgroundColor(getResources().getColor(R.color.red));
                    // Use setBackgroundDrawable() instead of setBackground which is not supporter at every devices and APIs
                    ((PlacementTextView) view).setBackgroundDrawable(getResources().getDrawable(R.drawable.black_bottom_border));
                break;
                case DragEvent.ACTION_DROP:
                    Log.d(LOG_TAG, "ACTION_DROP");
                    Log.d(LOG_TAG, "FOUND DRAG EVENT " + event.toString());

                    // quand on lache l'élément dragable dans la zone
                    //view.setBackgroundColor(getResources().getColor(R.color.peter_river));
                    //handle the dragged view being dropped over a target view
                    View draggedView = (View) event.getLocalState();
                    //stop displaying the view where it was before it was dragged
                    //draggedView.setVisibility(View.INVISIBLE);

                    //view dragged item is being dropped on
                    PlacementTextView dropTarget = (PlacementTextView) view;

                    //view being dragged and dropped
                    PlacementTextView dropped = (PlacementTextView) draggedView;
                    //dropped.setX(event.getX());
                    //dropped.setY(event.getY());

                    //update the text in the target view to reflect the data being dropped
                    CharSequence dropTargetText = dropTarget.getText();
                    dropTarget.setText(dropped.getText());
                    dropped.setText(dropTargetText);

                    //update BookHelper method
                    CharSequence dropTargetMethod = dropTarget.getMethod();
                    dropTarget.setMethod(dropped.getMethod());
                    dropped.setMethod(""+dropTargetMethod);

                    //update textId value
                    String dropTextId = dropTarget.getTextId();
                    dropTarget.setTextId(dropped.getTextId());
                    dropped.setTextId(dropTextId);

                    //make it bold to highlight the fact that an item has been dropped
                    //dropTarget.setTypeface(Typeface.DEFAULT_BOLD); 
                    dropTarget.setOnTouchListener(new TouchListener());
                    dropped.setOnTouchListener(new TouchListener());

                    String dropTargetPlacement = dropTarget.getPlacement();
                    dropTarget.setPlacement(dropped.getPlacement());
                    dropped.setPlacement(dropTargetPlacement);


                    String dropTargetResString = dropTarget.getResString();
                    dropTarget.setResString(dropped.getResString());
                    dropped.setResString(dropTargetResString);

                    layoutPlaces.put(dropped.getPlacement(), dropped.getResString());
                    layoutPlaces.put(dropTarget.getPlacement(), dropTarget.getResString());

                    Log.d(LOG_TAG, "event Y " + event.getY());
                    Log.d(LOG_TAG, "event X " + event.getX());
                break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d(LOG_TAG, "ACTION_DRAG_ENDED");
                    //view.setBackground(getResources().getDrawable(R.drawable.black_bottom_border));
                    // view.setBackgroundColor(getResources().getColor(R.color.carrot));
                    ((PlacementTextView) view).setBackgroundDrawable(getResources().getDrawable(R.drawable.black_bottom_border));
                break;
                default:
                break;
            }
            return true;
        } 
    } 

    // This defines your touch listener
    private final class TouchListener implements OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(LOG_TAG, "Motion event => down");
                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                //view.startDrag(data, shadowBuilder, view, 0);
                view.startDrag(data, shadowBuilder, view, 0);
                //view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    public void submitCustomize(View view) {
        Log.d(LOG_TAG, "Submit customize action");
        Log.d(LOG_TAG, "Saving places after changements " + layoutPlaces);
        boolean result = customizationDataSource.updatePlaces(layoutPlaces, this);
        String message = getResources().getString(R.string.error_customization_save);
        if (result) message = getResources().getString(R.string.success_customization_save);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        //customizationDataSource.close();
    }

}