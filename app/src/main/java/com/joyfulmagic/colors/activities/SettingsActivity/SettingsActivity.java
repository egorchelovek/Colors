package com.joyfulmagic.colors.activities.SettingsActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.BaseActivity;
import com.joyfulmagic.colors.utils.Messenger;

/**
 * Settings activity connected with settings class...
 */
public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitles(getResources().getString(R.string.activity_settings));

        setContentView(R.layout.settings);

        // init language spinner
        final Spinner spinnerLang = (Spinner) findViewById(R.id.languageSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLang.setAdapter(adapter);
        spinnerLang.setSelection(Settings.language);
        spinnerLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // set language
                Settings.language = position;
                Settings.saveSettings(getPreferences(MODE_PRIVATE));

                // short message
                Messenger.show(getApplicationContext(), getString(R.string.message_change_language) + " " + spinnerLang.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
