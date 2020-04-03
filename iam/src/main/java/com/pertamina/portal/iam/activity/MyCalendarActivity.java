package com.pertamina.portal.iam.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.iam.R;

public class MyCalendarActivity extends BackableNoActionbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar);
        super.onCreateBackable(this, R.id.ivBack);

        View llLeave = findViewById(R.id.llLeaveReq);
        View llEvent = findViewById(R.id.llEvent);

        llLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCalendarActivity.this, LeaveRequestActivity.class);
                startActivity(intent);
            }
        });

        llEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCalendarActivity.this, EventCalendarActivity.class);
                startActivity(intent);
            }
        });
    }
}
