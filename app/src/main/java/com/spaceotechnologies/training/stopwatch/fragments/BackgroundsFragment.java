package com.spaceotechnologies.training.stopwatch.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.adapters.BackgroundsRecyclerViewAdapter;
import com.spaceotechnologies.training.stopwatch.volley.Config;
import com.spaceotechnologies.training.stopwatch.volley.Model;
import com.spaceotechnologies.training.stopwatch.volley.OnLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.spaceotechnologies.training.stopwatch.applications.MyApplication.getAppContext;

/**
 * Created by Kostez on 31.08.2016.
 */
public class BackgroundsFragment extends Fragment {

    private static final int LIMIT = 20;
    protected Handler handler;
    private RecyclerView recyclerView;
    private BackgroundsRecyclerViewAdapter adapter;
    private List<Model> models = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading = false;
    private int offset = -LIMIT;
    private int page = 0;
    private int fistVisibleItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_backgrounds, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setupGridLayoutManager();
        gridLayoutManager.scrollToPosition(fistVisibleItem);

    }

    private void setupGridLayoutManager() {
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(getAppContext(), 2);
        } else {
            gridLayoutManager = new GridLayoutManager(getAppContext(), 4);
        }

        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void initViews() {

        recyclerView = (RecyclerView) getView().findViewById(R.id.backgrounds_recycler_view);
        handler = new Handler();

        setupGridLayoutManager();

        adapter = new BackgroundsRecyclerViewAdapter(getActivity(), models);
        recyclerView.setAdapter(adapter);

        onLoadMoreListener = new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                adapter.addItem(null);
                adapter.notifyItemInserted(models.size() - 1);

                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        adapter.remove(models.size() - 1);
                        adapter.notifyItemRemoved(models.size());
                        getData(getAutomaticRequest());
                    }
                }, 2000);
            }
        };

        if (models.isEmpty()) {
            onLoadMoreListener.onLoadMore();
        }

        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = gridLayoutManager.getItemCount();
                int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                fistVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (lastVisibleItem + 1) >= totalItemCount) {

                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    private String getAutomaticRequest() {
        offset = page++ * LIMIT;
        return Config.getRequest(offset, LIMIT);
    }

    private void getData(String startData) {

        if (hasConnection(getAppContext())) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL + startData,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
//                        loading.dismiss();
                            parseData(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(getAppContext());
            requestQueue.add(jsonArrayRequest);
        } else {
            Toast.makeText(getAppContext(), getAppContext().getString(R.string.please_connect_to_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.getType() == ConnectivityManager.TYPE_WIFI && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.getType() == ConnectivityManager.TYPE_MOBILE && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void parseData(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {
            Model model = new Model();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                model.setModel(json.getString(Config.TAG_MODEL));
                model.setImageUrl(json.getString(Config.TAG_IMAGE_URL));
                model.setId(json.getInt(Config.TAG_ID));
                model.setRank(json.getInt(Config.TAG_RANK));
                model.setAuthor(json.getString(Config.TAG_AUTHOR));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter.addItem(model);
            adapter.notifyItemInserted(models.size() - 1);
            setLoaded();
        }
    }

    public void setLoaded() {
        isLoading = false;
    }
}