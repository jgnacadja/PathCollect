package org.odk.collect.android.formentry;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.button.MaterialButton;

import org.odk.collect.android.R;
import org.odk.collect.android.activities.FormHierarchyActivity;
import org.odk.collect.android.activities.MainMenuActivity;
import org.odk.collect.android.listeners.SwipeHandler;
import org.odk.collect.android.utilities.FormNameUtils;

public class FormEndView extends SwipeHandler.View {

    private final Listener listener;
    private final String formTitle;
    private final String defaultInstanceName;

    public FormEndView(Context context, String formTitle, String defaultInstanceName, boolean instanceComplete, Listener listener) {
        super(context);
        this.formTitle = formTitle;
        this.defaultInstanceName = defaultInstanceName;
        this.listener = listener;
        init(context, instanceComplete);
    }

    private void init(Context context, boolean instanceComplete) {
        inflate(context, R.layout.form_entry_end, this);

        ((TextView) findViewById(R.id.description)).setText(context.getString(R.string.save_enter_data_description, formTitle));

        EditText saveAs = findViewById(R.id.save_name);

        // disallow carriage returns in the name
        InputFilter returnFilter = (source, start, end, dest, dstart, dend) -> FormNameUtils.normalizeFormName(source.toString().substring(start, end), true);
        saveAs.setFilters(new InputFilter[]{returnFilter});

        saveAs.setText(defaultInstanceName);
        saveAs.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                listener.onSaveAsChanged(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        final CheckBox markAsFinalized = findViewById(R.id.mark_finished);
        markAsFinalized.setChecked(instanceComplete);

        findViewById(R.id.save_exit_button).setOnClickListener(v -> {
            listener.onSaveClicked(markAsFinalized.isChecked());
        });

        MaterialButton viewButton = findViewById(R.id.view_button);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FormHierarchyActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public boolean shouldSuppressFlingGesture(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Nullable
    @Override
    public NestedScrollView getVerticalScrollView() {
        return findViewById(R.id.scroll_view);
    }

    public interface Listener {
        void onSaveAsChanged(String string);

        void onSaveClicked(boolean markAsFinalized);
    }
}
