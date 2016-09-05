package com.spaceotechnologies.training.stopwatch.fragments;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.spaceotechnologies.training.stopwatch.volley.OnLoadMoreListener;
import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.adapters.BackgroundsRecyclerViewAdapter;
import com.spaceotechnologies.training.stopwatch.volley.Config;
import com.spaceotechnologies.training.stopwatch.volley.Model;

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

    private static final int NUMBER = 20;
    protected Handler handler;
    private RecyclerView recyclerView;
    private BackgroundsRecyclerViewAdapter adapter;
    private List<Model> models;
    private GridLayoutManager gridLayoutManager;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading = false;
    private int from = -NUMBER;
    private int page = 0;

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

    private void initViews() {

        recyclerView = (RecyclerView) getView().findViewById(R.id.backgrounds_recycler_view);
        handler = new Handler();

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(getAppContext(), 2);
        } else {
            gridLayoutManager = new GridLayoutManager(getAppContext(), 4);
        }

        recyclerView.setLayoutManager(gridLayoutManager);
        models = new ArrayList<>();
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

        onLoadMoreListener.onLoadMore();

        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = gridLayoutManager.getItemCount();
                int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

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
        from = page++ * NUMBER;
        return getRequest(from, NUMBER);
    }

    private String getRequest(int from, int number) {
        return "/boobs/" + from + '/' + number + "/date/";
    }

    private void getData(String startData) {

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