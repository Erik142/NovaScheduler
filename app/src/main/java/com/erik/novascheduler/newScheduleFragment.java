package com.erik.novascheduler;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newScheduleFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FREETEXTBOX = "freetextbox";
    private static final String ARG_SCHOOL = "school";
    private static final String ARG_DEFAULT = "default";

    // TODO: Rename and change types of parameters
    private String mFreeTextBox;
    private String mSchool;
    private boolean mDefault;
    private boolean editMode = false;

    private final String PREFS_KEY = "prefs_url";
    private final String URL_KEY = "url_key";

    private Button acceptButton, cancelButton, removeButton;
    private EditText freeTextBoxEdit;
    private CheckBox defaultSchedule;
    private Spinner schoolSpinner;

    private String defaultURLs;
    private newScheduleInterface mInterface;

    private final String APP_NAME = "NovaScheduler";




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment newScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static newScheduleFragment newInstance() {
        newScheduleFragment fragment = new newScheduleFragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    public static newScheduleFragment newInstance(schemaDrawerItem item, Activity parentActivity)
    {
        Log.i("NovaScheduler", "newInstance: " + parentActivity.getSharedPreferences("prefs_url",0).getString("url_key",null));
        newScheduleFragment fragment = new newScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FREETEXTBOX, item.getFreeTextBox());
        args.putString(ARG_SCHOOL, item.getSchool(parentActivity));
        args.putBoolean(ARG_DEFAULT,item.isDefault());

        fragment.setArguments(args);

        return fragment;
    }

    public newScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            editMode = true;
            mSchool = getArguments().getString(ARG_SCHOOL);
            mFreeTextBox = getArguments().getString(ARG_FREETEXTBOX);
            mDefault = getArguments().getBoolean(ARG_DEFAULT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getDialog().setTitle("Lägg till nytt schema");

        View rootView = inflater.inflate(R.layout.fragment_new_schedule, container, false);

        acceptButton = (Button)rootView.findViewById(R.id.acceptButton);
        cancelButton = (Button)rootView.findViewById(R.id.cancelButton);
        removeButton = (Button)rootView.findViewById(R.id.removeButton);
        freeTextBoxEdit = (EditText)rootView.findViewById(R.id.freeTextBoxEdit);
        defaultSchedule = (CheckBox)rootView.findViewById(R.id.defaultCheckBox);
        schoolSpinner = (Spinner)rootView.findViewById(R.id.schoolSpinner);

        SharedPreferences appPreferences = getActivity().getSharedPreferences(PREFS_KEY,0);
        defaultURLs = "";
        defaultURLs += appPreferences.getString(URL_KEY,null);
        Log.i(APP_NAME,"newSchedule defaultURLs: " + defaultURLs);

        if (defaultURLs.matches("") || defaultURLs.equals("null"))
        {
            defaultURLs = "";
            defaultSchedule.setChecked(true);
            defaultSchedule.setClickable(false);
        }

        freeTextBoxEdit.setText(freeTextBoxEdit.getText().toString().toUpperCase());

        if (editMode)
        {
            freeTextBoxEdit.setText(mFreeTextBox);
            defaultSchedule.setChecked(mDefault);

            Resources res = getActivity().getResources();
            String[] schools = res.getStringArray(R.array.schoolArray);

            for (int i = 0; i < schools.length; i++)
            {
                if (schools[i].equals(mSchool))
                {
                    schoolSpinner.setSelection(i);
                    break;
                }
            }
            Log.i("NovaScheduler", "newSchedule edit: " + getActivity().getSharedPreferences("prefs_url", 0).getString("url_key",null));
        }

        initListeners();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mInterface = (newScheduleInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement newScheduleInterface");
        }
    }

    public void initListeners()
    {
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAcceptButtonClick();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelButtonClick();
            }
        });

        if (editMode)
        {
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRemoveButtonClick();
                }
            });

            removeButton.setVisibility(View.VISIBLE);
        }

    }

    public void onRemoveButtonClick()
    {
        mInterface.onDialogCompleted("", schemaDrawerItem.buildURL(getActivity(), freeTextBoxEdit.getText().toString(), schoolSpinner.getSelectedItem().toString()));

        if (!defaultURLs.equals("null") || !defaultURLs.matches("")) {
            //Kommer inte fungera eftersom defaultURLS har formen url;default=boolean
            ArrayList<String> URLs = schemaDrawerItem.getDefiniteURLs(defaultURLs);
            String tmpUrl = schemaDrawerItem.buildURL(getActivity(), freeTextBoxEdit.getText().toString(), schoolSpinner.getSelectedItem().toString());

            for (String url:URLs) {
                if (url.equals(tmpUrl))
                {
                    URLs.remove(url);
                    break;
                }
            }


        }

        this.dismiss();
    }

    public void onCancelButtonClick()
    {
        this.dismiss();
    }

    public void onAcceptButtonClick()
    {
        if (!freeTextBoxEdit.getText().toString().matches("")) {
            SharedPreferences appPreferences = getActivity().getSharedPreferences(PREFS_KEY, 0);
            SharedPreferences.Editor prefsEditor = appPreferences.edit();

            Log.i(APP_NAME, "newScheduleFragment, freeTextBoxEdit.getText().toString() = " + freeTextBoxEdit.getText().toString());
            Log.i(APP_NAME, "newScheduleFragment, mFreeTextBox = " + mFreeTextBox);

            String finalURLs = "";
            boolean isAdded = false;

            try {
                if (!defaultURLs.equals("null") && !defaultURLs.matches("") && !defaultURLs.isEmpty()) {
                    //Kommer inte fungera eftersom defaultURLS har formen url;default=boolean
                    ArrayList<String> URLs = schemaDrawerItem.getDefiniteURLs(defaultURLs);
                    ArrayList<Boolean> defaultBools = schemaDrawerItem.getDefaultBoolean(defaultURLs);
                    String tmpUrl = schemaDrawerItem.buildURL(getActivity(), freeTextBoxEdit.getText().toString(), schoolSpinner.getSelectedItem().toString());

                    if (editMode)
                    {
                        String oldURL = schemaDrawerItem.buildURL(getActivity(), mFreeTextBox, mSchool);
                        for (int i = 0; i < URLs.size(); i++)
                        {
                            if (URLs.get(i).equals(oldURL))
                            {
                                Log.i(APP_NAME, "URLs " + i + ": " + URLs.get(i));
                                URLs.remove(URLs.get(i));
                                defaultBools.remove(defaultBools.get(i));
                                break;
                            }
                        }
                    }

                    for (int i = 0; i < URLs.size(); i++) {
                        if (tmpUrl.equals(URLs.get(i))) {
                            Toast.makeText(getActivity().getApplicationContext(), "Schemat har redan lagts till!", Toast.LENGTH_SHORT).show();
                            isAdded = true;
                            break;
                        }
                    } if (!isAdded) {
                            if (defaultSchedule.isChecked())
                            {
                                finalURLs = "";

                                for (int j = 0; j < URLs.size(); j++)
                                {
                                    if (!URLs.get(j).trim().matches("")) {
                                        Log.i(APP_NAME, "newScheduleFragment, URLs.get(" + j + ") = " + URLs.get(j));
                                        finalURLs += URLs.get(j) + ";default=false|";
                                    }
                                }
                            }
                            else {
                                boolean hasDefault = false;
                                for (int j = 0; j < defaultBools.size(); j++)
                                {
                                    if (defaultBools.get(j))
                                    {
                                        hasDefault = true;
                                        break;
                                    }
                                }

                                int j = 0;
                                if (!hasDefault && !URLs.get(0).trim().matches(""))
                                {
                                    finalURLs = URLs.get(0) + ";default=true|";
                                    j = 1;
                                }

                                for (; j < URLs.size(); j++)
                                {
                                    if (!URLs.get(j).trim().matches(""))
                                    finalURLs += URLs.get(j) + "default=" + String.valueOf(defaultBools.get(j)) + "|";
                                }

                            }
                        if (!finalURLs.trim().matches(""))
                        {
                            finalURLs += schemaDrawerItem.buildURL(getActivity(), freeTextBoxEdit.getText().toString(), schoolSpinner.getSelectedItem().toString())
                                    + ";default=" + String.valueOf(defaultSchedule.isChecked());
                        }
                        else
                        {
                            finalURLs = schemaDrawerItem.buildURL(getActivity(),freeTextBoxEdit.getText().toString(),schoolSpinner.getSelectedItem().toString())
                                    + ";default=true";
                            defaultSchedule.setChecked(true);
                        }

                        }



                } else {
                    finalURLs = schemaDrawerItem.buildURL(getActivity(),freeTextBoxEdit.getText().toString(),schoolSpinner.getSelectedItem().toString())
                            + ";default=true";
                }

                Log.i(APP_NAME, "newScheduleFragment, finalURLs = " + finalURLs);

                if (!isAdded) {
                    if (!finalURLs.trim().matches("null") || !finalURLs.trim().matches("")) {
                        prefsEditor.putString(URL_KEY, finalURLs);
                        prefsEditor.commit();
                        String returnURL = schemaDrawerItem.buildURL(getActivity(), freeTextBoxEdit.getText().toString(), schoolSpinner.getSelectedItem().toString()) + ";default=" +
                                String.valueOf(defaultSchedule.isChecked());
                        if (editMode)
                        {
                            mInterface.onDialogCompleted(returnURL, (schemaDrawerItem.buildURL(getActivity(), mFreeTextBox, mSchool) + ";default=" + String.valueOf(defaultSchedule.isChecked())));
                        }
                        else
                        {
                            mInterface.onDialogCompleted(returnURL, "");
                        }

                    }

                    this.dismiss();
                }
            }
            catch (Exception e)
            {
                Log.i(APP_NAME, "newScheduleFragment, exception: " + e.toString());
                this.dismiss();
            }
        }
        else
        {
            Toast.makeText(getActivity().getApplicationContext(), "Det finns fortfarande tomma fält", Toast.LENGTH_SHORT).show();
        }

    }

    public interface newScheduleInterface
    {
        void onDialogCompleted(String URL, String oldURL);
    }


}
