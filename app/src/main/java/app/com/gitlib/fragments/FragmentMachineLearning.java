package app.com.gitlib.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import app.com.gitlib.R;
import app.com.gitlib.activities.s.details.DetailsActivity;
import app.com.gitlib.adapters.AllTopicAdapter;
import app.com.gitlib.apiutils.AllApiService;
import app.com.gitlib.apiutils.AllUrlClass;
import app.com.gitlib.models.alltopic.Item;
import app.com.gitlib.models.alltopic.TopicBase;
import app.com.gitlib.utils.UX;
import app.com.gitlib.utils.UtilsManager;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMachineLearning extends Fragment {
    private static final FragmentMachineLearning FRAGMENT_MACHINE_LEARNING = null;
    private RecyclerView androidTopicRecyclerView;
    private UX ux;
    private UtilsManager utilsManager;
    private ArrayList<Item> mlItemList;
    private Spinner mlFilterSpinner;
    private AllUrlClass allUrlClass;
    private AllApiService apiService;
    private CircleImageView refreshListButton;
    private TextView NoData;
    private ImageView NoDataIV;
    private String[] mlFilterList = new String[]{"Select Query","Big Data","Data Science",
            "Natural Language Processing","Neural Network","Deep Learning"};

    public FragmentMachineLearning() {
        // Required empty public constructor
    }

    public static FragmentMachineLearning getInstance() {
        if (FRAGMENT_MACHINE_LEARNING == null) return new FragmentMachineLearning();
        else return FRAGMENT_MACHINE_LEARNING;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_machine_learning, container, false);
        init(view);
        bindUIWithComponents(view);
        return view;
    }

    private void init(View view) {
        androidTopicRecyclerView = view.findViewById(R.id.mRecyclerView);
        refreshListButton = view.findViewById(R.id.RefreshList);
        NoData = view.findViewById(R.id.NoDataMessage);
        NoDataIV = view.findViewById(R.id.NoDataIV);
        mlFilterSpinner = view.findViewById(R.id.FilterSpinner);
        allUrlClass = new AllUrlClass();
        mlItemList = new ArrayList<>();
        ux = new UX(getContext());
        utilsManager = new UtilsManager(getContext());
    }

    private void bindUIWithComponents(View view) {
        ux.setSpinnerAdapter(mlFilterSpinner,mlFilterList);

        new BackgroundDataLoad(view , allUrlClass.ALL_TOPICS_BASE_URL , "ml").execute();

        refreshListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundDataLoad(view , allUrlClass.ALL_TOPICS_BASE_URL , "ml").execute();
            }
        });

        mlFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String queryString = adapterView.getItemAtPosition(position).toString();
                new BackgroundDataLoad(view, allUrlClass.ALL_TOPICS_BASE_URL, "" + queryString).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadListView(){
        ux.loadListView(mlItemList, androidTopicRecyclerView, R.layout.adapter_layout_trending_ml_repos).setOnItemClickListener(new AllTopicAdapter.onItemClickListener() {
            @Override
            public void respond(Item androidItem) {
                Intent intent = new Intent(getContext() , DetailsActivity.class);
                intent.putExtra("item", androidItem);
                getContext().startActivity(intent);
            }
        });
    }

    private class BackgroundDataLoad extends AsyncTask<String, Void, String> {

        View view;
        String url , querySting;

        public BackgroundDataLoad(View view, String url, String querySting) {
            this.view = view;
            this.url = url;
            this.querySting = querySting;
        }

        @Override
        protected void onPreExecute() {
            ux.getLoadingView();
        }

        @Override
        protected String doInBackground(String... strings) {
            loadRecord(url , querySting);
            return "done";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("done")){
                Log.v("result async task :: ",result);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (mlItemList.size()>0){
                            loadListView();
                            NoData.setVisibility(View.GONE);
                            NoDataIV.setVisibility(View.GONE);
                        }
                        else {
                            NoData.setVisibility(View.VISIBLE);
                            NoDataIV.setVisibility(View.VISIBLE);
                            Toasty.error(getContext(),R.string.no_data_message).show();
                        }
                        ux.removeLoadingView();
                    }
                }, 6000);
            }
        }

    }

    private void loadRecord(String url , String queryString) {
        mlItemList.clear();
        //Creating the instance for api service from AllApiService interface
        apiService=utilsManager.getClient(url).create(AllApiService.class);
        final Call<TopicBase> androidTopicCall=apiService.getAllTopics(url+"repositories",queryString);
        //handling user requests and their interactions with the application.
        androidTopicCall.enqueue(new Callback<TopicBase>() {
            @Override
            public void onResponse(Call<TopicBase> call, Response<TopicBase> response) {
                try{
                    for (int start=0;start<response.body().getItems().size();start++) {
                        Item item=response.body().getItems().get(start);
                        //License license = item.getLicense();
                        mlItemList.add(new Item(item.getFullName(),item.getAvatar_url(),item.getHtmlUrl(),item.getLanguage(),item.getStargazersCount(),item.getWatchersCount(),
                                item.getForksCount(),item.getForks(),item.getWatchers()));
                    }
                }
                catch (Exception e){
                    Log.v("EXCEPTION : ",""+e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<TopicBase> call, Throwable t) {
                Log.v("ML fragment",""+t.getMessage());
            }
        });

    }
}
