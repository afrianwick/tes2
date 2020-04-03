package com.pertamina.portal.iam.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.ImageUtils.ImageUtils;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.worklist.ClvActivity;
import com.pertamina.portal.iam.fragments.ListWorklistFragment.OnListFragmentInteractionListener;
import com.pertamina.portal.iam.models.IamComment;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IamComment} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ListCommentAdapter extends RecyclerView.Adapter<ListCommentAdapter.ViewHolder> {

    private static final int VIEW_KANAN = 1;
    private static final int VIEW_KIRI = 2;
    private static final String TAG = "ListCommentAdapter";
    private final List<IamComment> list;
    private final Activity mActivity;

    public ListCommentAdapter(Activity activity, List<IamComment> items) {
        mActivity = activity;
        list = items;
    }

    @Override
    public int getItemViewType(int position) {
        String username = PrefUtils.Build(mActivity).getPref().getString(Constants.KEY_USERNAME, "");
        IamComment comment = (IamComment) list.get(position);

//        Log.d(TAG, "comment.username = " + comment.createdBy);
//        Log.d(TAG, "username = " + username);

        if (comment.createdBy.equalsIgnoreCase(username)) {
            return VIEW_KANAN;
        } else {
            return VIEW_KIRI;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment_kanan, parent, false);

        if (viewType == VIEW_KIRI) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_comment_kiri, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = list.get(position);

        if (holder.getItemViewType() == VIEW_KIRI) {
            loadProfilePic(holder.mItem.createdBy, holder.ivProfile);
            holder.tvName.setText(holder.mItem.username);
            holder.tvAction.setText(holder.mItem.status);
        }

        loadProfilePic(holder.mItem.createdBy, holder.ivProfile);

        Date date = StringUtils.toDate(holder.mItem.strDate);
        String strDate = StringUtils.formatDateFull(date);

        holder.tvDate.setText(strDate);
        holder.tvMessage.setText(holder.mItem.message);

//        Log.d(TAG, "holder.mItem.message = " + holder.mItem.message);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvName;
        private final TextView tvMessage;
        private final TextView tvDate;
        private final CircularImageView ivProfile;
        private final TextView tvAction;
        public IamComment mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvMessage = (TextView) view.findViewById(R.id.tvMessage);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            ivProfile = (CircularImageView) view.findViewById(R.id.ivAva);
            tvAction = (TextView) view.findViewById(R.id.tvAction);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvName.getText() + "'";
        }
    }

    protected void loadProfilePic(String username, final CircularImageView civ) {
        username = username.replace("\\", "%5C");
        String url = "ROOT/Public/Images/ProfilePictures/" + username + ".jpg";

        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(mActivity, 2000);
        restApi.loadImage(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        civ.setImageBitmap(ImageUtils.scaleDown(bmp, 100, false));

//                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
//                        Glide.with(mActivity)
//                                .load(bmp)
//                                .transition(withCrossFade())
////                                .thumbnail(thumbnailRequest)
//                                .into(civ);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.d(TAG, "load image not success" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "load image onFailure");
                t.printStackTrace();
            }
        });
    }
}
