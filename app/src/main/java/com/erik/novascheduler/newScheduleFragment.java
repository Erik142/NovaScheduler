package com.erik.novascheduler;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final String PREFS_KEY = "prefs_url";
    private final String URL_KEY = "url_key";

    private Button acceptButton, cancelButton;
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

    public newScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/

        SharedPreferences appPreferences = getActivity().getSharedPreferences(PREFS_KEY,0);
        defaultURLs = appPreferences.getString(URL_KEY,null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getDialog().setTitle("Lägg till nytt schema");

        View rootView = inflater.inflate(R.layout.fragment_new_schedule, container, false);

        acceptButton = (Button)rootView.findViewById(R.id.acceptButton);
        cancelButton = (Button)rootView.findViewById(R.id.cancelButton);
        freeTextBoxEdit = (EditText)rootView.findViewById(R.id.freeTextBoxEdit);
        defaultSchedule = (CheckBox)rootView.findViewById(R.id.defaultCheckBox);
        schoolSpinner = (Spinner)rootView.findViewById(R.id.schoolSpinner);

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


    }

    public void onCancelButtonClick()
    {
        this.dismiss();
    }

    public void onAcceptButtonClick()
    {
        if (!freeTextBoxEdit.getText().toString().equals("") || freeTextBoxEdit.getText().toString() != null) {
            SharedPreferences appPreferences = getActivity().getSharedPreferences(PREFS_KEY, 0);
            SharedPreferences.Editor prefsEditor = appPreferences.edit();

            Log.i(APP_NAME, "freeTextBoxEdit.getText().toString() = " + freeTextBoxEdit.getText().toString());

            String finalURLs = "";

            try {
                if (defaultURLs != null) {
                    //Kommer inte fungera eftersom defaultURLS har formen url;default=boolean
                    ArrayList<String> URLs = schemaDrawerItem.getDefiniteURLs(defaultURLs.split("\\|"));
                    String tmpUrl = schemaDrawerItem.buildURL(getActivity(), freeTextBoxEdit.getText().toString(), schoolSpinner.getSelectedItem().toString());

                    for (int i = 0; i < URLs.size(); i++) {
                        if (tmpUrl.equals(URLs.get(i))) {
                            Toast.makeText(getActivity().getApplicationContext(), "Schemat har redan lagts till!", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            finalURLs = defaultURLs + "|" + schemaDrawerItem.buildURL(getActivity(),freeTextBoxEdit.getText().toString(),schoolSpinner.getSelectedItem().toString())
                                    + ";default=" + String.valueOf(defaultSchedule.isChecked());
                        }
                    }


                } else {
                    finalURLs = schemaDrawerItem.buildURL(getActivity(),freeTextBoxEdit.getText().toString(),schoolSpinner.getSelectedItem().toString())
                            + ";default=" + String.valueOf(defaultSchedule.isChecked());
                }

                if (finalURLs != null || !finalURLs.equals("")) {
                    prefsEditor.putString(URL_KEY, finalURLs);
                    prefsEditor.commit();
                    String returnURL = schemaDrawerItem.buildURL(getActivity(), freeTextBoxEdit.getText().toString(), schoolSpinner.getSelectedItem().toString());
                    mInterface.onDialogCompleted(returnURL);
                }

                this.dismiss();
            }
            catch (Exception e)
            {

            }
        }
        else
        {
            Toast.makeText(getActivity().getApplicationContext(), "Det finns fortfarande tomma fält", Toast.LENGTH_SHORT);
        }

    }

    public interface newScheduleInterface
    {
        public void onDialogCompleted(String url);
    }


}
