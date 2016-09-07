package com.spaceotechnologies.training.stopwatch.adapters;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.fragments.PictureFragment;
import com.spaceotechnologies.training.stopwatch.volley.Config;
import com.spaceotechnologies.training.stopwatch.volley.CustomVolleyRequest;
import com.spaceotechnologies.training.stopwatch.volley.Model;

import java.util.ArrayList;
import java.util.List;

import static com.spaceotechnologies.training.stopwatch.applications.MyApplication.getAppContext;

/**
 * Created by Kostez on 30.08.2016.
 */
public class BackgroundsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ImageLoader imageLoader;
    private List<Model> models;

    public static final int VIEW_ITEM = 1;
    public static final int BACKGROUNDS_FRAGMENT_POSITION = 0;
    public static final int PICTURE_FRAGMENT_POSITION = 1;
    public static final int VIEW_PROG = 0;
    public static final String PICTURES_ACTIVITY_TAG = "pictures_activity_tag";

    public BackgroundsRecyclerViewAdapter(Activity activity, List<Model> models) {
        this.activity = activity;

        if (models != null)
            this.models = (models);
        else
            this.models = new ArrayList<Model>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(getAppContext()).inflate(R.layout.row_layout, parent, false);
            vh = new BackgroundsRecyclerViewAdapter.BackgroundsViewHolder(view);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        return models.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BackgroundsViewHolder) {

            Model model = models.get(position);

            imageLoader = CustomVolleyRequest.getInstance(getAppContext()).getImageLoader();
            imageLoader.get(Config.IMAGE_DATA_URL + '/' + model.getImageUrl(), ImageLoader.getImageListener(((BackgroundsViewHolder) holder).img_android, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

            ((BackgroundsViewHolder) holder).img_android.setImageUrl(Config.IMAGE_DATA_URL + '/' + model.getImageUrl(), imageLoader);
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class BackgroundsViewHolder extends RecyclerView.ViewHolder {

        private NetworkImageView img_android;

        public BackgroundsViewHolder(final View itemView) {
            super(itemView);
            img_android = (NetworkImageView) itemView.findViewById(R.id.img_android);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    PictureFragment pictureFragment = new PictureFragment(img_android.getDrawable());
                    fragmentTransaction.replace(R.id.backgrounds_content, pictureFragment, PICTURES_ACTIVITY_TAG + ':' + PICTURE_FRAGMENT_POSITION);
                    fragmentTransaction.commit();
                }
            });
        }
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    public void addItem(Model model) {
        this.models.add(model);
    }

    public void add(List<Model> models) {
        this.models.addAll(models);
        notifyItemInserted(models.size());
    }

    public void remove(int position) {
        this.models.remove(position);
    }
}