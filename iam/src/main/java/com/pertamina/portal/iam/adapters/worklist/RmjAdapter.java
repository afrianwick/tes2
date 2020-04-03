package com.pertamina.portal.iam.adapters.worklist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.RmjDetailActivity;
import com.pertamina.portal.iam.fragments.ListWorklistFragment.OnListFragmentInteractionListener;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.worklist.RmjData;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IamComment} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RmjAdapter extends RecyclerView.Adapter<RmjAdapter.ViewHolder> {

    private static final String TAG = "RmjAdapter";
    private final List<RmjData> list;
    private final Activity mActivity;

    public RmjAdapter(Activity activity, List<RmjData> items) {
        mActivity = activity;
        list = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rmj_detail, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final RmjData data = list.get(position);

        holder.tvPanNumber.setText(data.panNumber);
        holder.tvNamaPekerja.setText(data.namaPekerja);
        holder.tvKodeJabatan.setText(data.kodeJabatan);
        holder.tvNamaJabatan.setText(data.namaJabatan);
        holder.tvJenisMutasi.setText(data.jenisMutasi);
        holder.tvJabatanLama.setText(data.jabatanLama);

        if (data.prlJabatanBaru - data.prlBsBaru >= 3) {
            holder.cvPersonal1.setCardBackgroundColor(mActivity.getResources().getColor(R.color.iamYellowSmooth));
            holder.tvPanNumber.setTextColor(mActivity.getResources().getColor(R.color.white));
            holder.tvNamaPekerja.setTextColor(mActivity.getResources().getColor(R.color.white));
            holder.tvKodeJabatan.setTextColor(mActivity.getResources().getColor(R.color.white));
            holder.tvNamaJabatan.setTextColor(mActivity.getResources().getColor(R.color.white));
            holder.tvJenisMutasi.setTextColor(mActivity.getResources().getColor(R.color.white));
            holder.tvJabatanLama.setTextColor(mActivity.getResources().getColor(R.color.white));
        }

        holder.cvPersonal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, RmjDetailActivity.class);
                intent.putExtra("rmjData", data);
                mActivity.startActivity(intent);
            }
        });

        Log.d(TAG, "data.namaPekerja = " + data.namaPekerja);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvPanNumber, tvNamaPekerja, tvKodeJabatan, tvNamaJabatan,
                tvJenisMutasi, tvJabatanLama;
        public RmjData mItem;
        public CardView cvPersonal1;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvPanNumber = (TextView) view.findViewById(R.id.tvPanNumber);
            tvNamaPekerja = (TextView) view.findViewById(R.id.tvNamaPekerja);
            tvKodeJabatan = (TextView) view.findViewById(R.id.tvKodeJabatan);
            tvNamaJabatan = (TextView) view.findViewById(R.id.tvNamaJabatan);
            tvJenisMutasi = (TextView) view.findViewById(R.id.tvJenisMutasi);
            tvJabatanLama = (TextView) view.findViewById(R.id.tvJabatanLama);
            cvPersonal1 = view.findViewById(R.id.cvPersonal1);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvNamaPekerja.getText() + "'";
        }
    }
}
