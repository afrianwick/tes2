package com.pertamina.portal.iam.adapters.worklist;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.DokumenPreviewActivity;
import com.pertamina.portal.iam.fragments.ListWorklistFragment.OnListFragmentInteractionListener;
import com.pertamina.portal.iam.interfaces.MyDocumentView;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.MyDocumentData;
import com.pertamina.portal.iam.models.worklist.RmjData;
import com.pertamina.portal.iam.utils.PicassoTrustAll;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IamComment} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDocumentAdapter extends RecyclerView.Adapter<MyDocumentAdapter.ViewHolder> {

    private static final String TAG = "MyDocumentAdapter";
    private final List<MyDocumentData> list;
    private final MyDocumentView myDocumentView;
    private final Activity mActivity;
    private List<Integer> visibilities;

    public MyDocumentAdapter(Activity activity, List<MyDocumentData> items, List<Integer> visibilites, MyDocumentView myDocumentView) {
        mActivity = activity;
        list = items;
        this.myDocumentView = myDocumentView;
        this.visibilities = visibilites;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_document, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MyDocumentData data = list.get(position);

        holder.cvPersonal.setVisibility(visibilities.get(position));

        if (position == 0) {
            holder.clContainerDocumentType.setVisibility(View.VISIBLE);
        } else if (data.documentType.equals(list.get(position - 1).documentType)) {
            holder.clContainerDocumentType.setVisibility(View.GONE);
        } else {
            holder.clContainerDocumentType.setVisibility(View.VISIBLE);
        }

        if (holder.clContainerDocumentType.getVisibility() == View.VISIBLE && visibilities.get(position) == View.VISIBLE) {
            holder.signIV.setBackground(mActivity.getResources().getDrawable(R.drawable.ic_collapse));
        } else if (holder.clContainerDocumentType.getVisibility() == View.VISIBLE && visibilities.get(position) == View.GONE){
            holder.signIV.setBackground(mActivity.getResources().getDrawable(R.drawable.ic_expand));
        }

        int intDateOfIssue = data.dateOfIssue;
        String strRawDate = String.valueOf(intDateOfIssue);
        String strDateOfIssue = strRawDate != null ? StringUtils.reformatDateYyyyMmDd(strRawDate) : "null";

        holder.tvName.setText(data.name);
        holder.tvDocType.setText(data.documentType);
        holder.tvFilename.setText(data.filename);
        holder.tvDateUpload.setText(data.uploadDate);
        holder.tvDateOfIssue.setText(strDateOfIssue);
        holder.tvDocType2.setText(data.documentType);
        holder.tvDocumentType.setText(data.documentType);
        holder.tvDocumentType.setText(data.name);

        Log.d(TAG, "data.name = " + data.name);

        holder.cvPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, DokumenPreviewActivity.class);
                intent.putExtra(DokumenPreviewActivity.URL, data.action);
                intent.putExtra(DokumenPreviewActivity.FILENAME, data.filename);
                intent.putExtra(DokumenPreviewActivity.TGL_UPLOAD, data.uploadDate);
                intent.putExtra(DokumenPreviewActivity.NAME, data.name);
                mActivity.startActivity(intent);
            }
        });

        holder.clContainerDocumentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDocumentView.onShowHideDocument(position, data.documentType);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvName, tvDocType, tvFilename, tvDateUpload,
                tvDateOfIssue, tvDocType2, tvDocumentType;
        public final CardView cvPersonal;
        public final ImageView signIV;
        public final ConstraintLayout clContainerDocumentType;
        public MyDocumentData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvDocType = (TextView) view.findViewById(R.id.tvDocType);
            tvFilename = (TextView) view.findViewById(R.id.tvFilename);
            tvDateUpload = (TextView) view.findViewById(R.id.tvDateUpload);
            tvDateOfIssue = (TextView) view.findViewById(R.id.tvDateOfIssue);
            tvDocType2 = (TextView) view.findViewById(R.id.tvDocType2);
            tvDocumentType = (TextView) view.findViewById(R.id.tvDocumentType);
            clContainerDocumentType = view.findViewById(R.id.clContainerDocumentType);
            cvPersonal = view.findViewById(R.id.cvPersonal);
            signIV = view.findViewById(R.id.listItemMyDocumentExpandCollapseSignIV);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvName.getText() + "'";
        }
    }
}
