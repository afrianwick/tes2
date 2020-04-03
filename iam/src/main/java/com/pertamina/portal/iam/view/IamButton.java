package com.pertamina.portal.iam.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pertamina.portal.iam.R;

public class IamButton extends LinearLayout {

    private final TextView title;
    private final LinearLayout llBtnWrapper;
    private View mValue;
    private ImageView image;
    private String btnText;
    private Drawable btnDrawable, btnBackground;

    public IamButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.IamButton, 0, 0);
        btnText = a.getString(R.styleable.IamButton_titleText);
        btnDrawable = a.getDrawable(R.styleable.IamButton_drawableResource);
        btnBackground = a.getDrawable(R.styleable.IamButton_background);

        a.recycle();

//        setOrientation(LinearLayout.HORIZONTAL);
//        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_iam_button, this, true);

        llBtnWrapper = (LinearLayout) view.findViewById(R.id.llWraperBtnIcon);
        title = (TextView) findViewById(R.id.tvBtn);
        image = (ImageView) findViewById(R.id.imgIcon);

        title.setText(btnText);
        image.setImageDrawable(btnDrawable);
        llBtnWrapper.setBackground(btnBackground);
    }

    public IamButton(Context context) {
        this(context, null);
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }

    public void setBtnDrawable(Drawable btnDrawable) {
        this.btnDrawable = btnDrawable;
        llBtnWrapper.setBackground(btnDrawable);
    }

}
