package com.pertamina.portal.iam.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.fragments.ListWorklistFragment.OnListFragmentInteractionListener;
import com.pertamina.portal.iam.models.CalendarEvent;
import com.pertamina.portal.iam.models.Task;

import java.util.ArrayList;
import java.util.List;

import static com.pertamina.portal.core.utils.Constants.EventColor.ABSENCES;
import static com.pertamina.portal.core.utils.Constants.EventColor.LEAVE;
import static com.pertamina.portal.core.utils.Constants.EventColor.PUBLIC_HOLDY;
import static com.pertamina.portal.core.utils.Constants.EventColor.TRAINING;
import static com.pertamina.portal.core.utils.Constants.EventColor.TRAVEL;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Task} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventAdapter.ViewHolder> {

    private final List<CalendarEvent> mValues = new ArrayList<>();
    private final Activity mActivity;

    public CalendarEventAdapter(Activity activity, List<CalendarEvent> items) {
        mActivity = activity;

        if (items != null && items.size() > 0) {
            mValues.addAll(items);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_calendar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        String strDate = StringUtils.formatDateSimple(holder.mItem.dateStart);

        holder.tvEventName.setText(holder.mItem.eventName);
        holder.tvEventTime.setText(holder.mItem.time);
        holder.tvEventCategory.setText(holder.mItem.category);

        int drawable;
        if (holder.mItem.category.equalsIgnoreCase(PUBLIC_HOLDY)) {
            drawable = R.drawable.button_bg_rounded_red;
        } else if (holder.mItem.category.equalsIgnoreCase(LEAVE)) {
            drawable = R.drawable.button_bg_rounded_yellow;
        } else if (holder.mItem.category.equalsIgnoreCase(TRAINING)) {
            drawable = R.drawable.button_bg_rounded_green;
        } else if (holder.mItem.category.equalsIgnoreCase(TRAVEL)) {
            drawable = R.drawable.button_bg_rounded_corners_blue;
        } else if (holder.mItem.category.equalsIgnoreCase(ABSENCES)) {
            drawable = R.drawable.button_bg_rounded_yellow;
        } else {
            drawable = 0;
        }

        holder.llEventWraper.setBackground(mActivity.getResources().getDrawable(drawable));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void clear() {
        this.mValues.clear();
    }

    public void addList(List<CalendarEvent> calendarEvents) {
        this.mValues.addAll(calendarEvents);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView tvEventName, tvEventTime, tvEventCategory;
        public CalendarEvent mItem;
        public LinearLayout llEventWraper;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvEventName = view.findViewById(R.id.tvEventName);
            tvEventTime = view.findViewById(R.id.tvEventTime);
            tvEventCategory = view.findViewById(R.id.tvEventCategory);
            llEventWraper = view.findViewById(R.id.llEventWraper);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvEventName.getText() + "'";
        }
    }
}
