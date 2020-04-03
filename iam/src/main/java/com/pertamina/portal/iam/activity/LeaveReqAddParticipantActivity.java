package com.pertamina.portal.iam.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.utils.KeyboardUtils;

import java.util.Calendar;

public class LeaveReqAddParticipantActivity extends BackableNoActionbarActivity {

    private Button saveButton;
    private TextInputEditText nameTIET, noteTIET;
    private AlertDialog alertDialog;
    private Calendar c;
    public static String NAME = "name", NOTE = "note";
    public static int ADDITIONAL_PARTICIPANT_RESULT = 2020;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participant);
        super.onCreateBackable(this, R.id.ivBack);

        buildAlert();

        nameTIET = findViewById(R.id.addParticipantNameTIET);
        noteTIET = findViewById(R.id.addParticipantNoteTIET);
        saveButton = findViewById(R.id.addParticipantSaveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameTIET.getText().toString().equals("")) {
                    alertDialog.setMessage("Lengkapi data!");
                    alertDialog.show();
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra(NAME, nameTIET.getText().toString());
                intent.putExtra(NOTE, noteTIET.getText().toString());
                setResult(ADDITIONAL_PARTICIPANT_RESULT, intent);
                finish();
            }
        });

        KeyboardUtils.setupUI(this, findViewById(R.id.addParticipantParentLL));
    }

    public void buildAlert() {
        alertDialog = new AlertDialog.Builder(this)
                .setNeutralButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
    }
}
