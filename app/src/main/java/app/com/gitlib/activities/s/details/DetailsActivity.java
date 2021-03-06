package app.com.gitlib.activities.s.details;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import app.com.gitlib.R;
import app.com.gitlib.adapters.DetailsFragmentPagerAdapter;
import app.com.gitlib.models.alltopic.Item;

public class DetailsActivity extends AppCompatActivity {
    private TextView UserName , RepoLink ,Description , NumberOfForks , NumberOfStars , NumberOfWatch , NumberOfIssues , Language , CreatedAt , UpdatedAt;
    private ImageView UserIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        init();
        bindUIWithComponents();
    }

    private void init() {
        UserName = findViewById(R.id.UserName);
        RepoLink = findViewById(R.id.RepoLink);
        UserIcon = findViewById(R.id.UserIcon);
        Description = findViewById(R.id.Description);
        NumberOfForks = findViewById(R.id.NumberOfForks);
        NumberOfStars = findViewById(R.id.NumberOfStars);
        NumberOfWatch = findViewById(R.id.NumberOfWatch);
        NumberOfIssues = findViewById(R.id.NumberOfIssues);
        Language = findViewById(R.id.Language);
        CreatedAt = findViewById(R.id.Created_At);
        UpdatedAt = findViewById(R.id.Updated_At);
    }

    private void bindUIWithComponents() {
        setViewPager();
        getData();
        setData();
    }

    private Item getData(){
        Item item = (Item) getIntent().getSerializableExtra("item");
        return item;
    }

    private void setData(){
        UserName.setText(getData().getFullName());
        RepoLink.setText(getData().getHtmlUrl());

        if (getData().getDescription() == null) Description.setText("No description for this repository.");
        else Description.setText(getData().getDescription());

        NumberOfForks.setText(""+getData().getForksCount());
        NumberOfStars.setText(""+getData().getStargazersCount());
        NumberOfWatch.setText(""+getData().getWatchersCount());

        if (getData().getOpenIssuesCount() == null || getData().getOpenIssuesCount() == 0) NumberOfIssues.setText("0");
        else NumberOfIssues.setText(""+getData().getOpenIssuesCount());

        if (getData().getLanguage() == null) Language.setText("No language");
        else Language.setText(getData().getLanguage());

        if (getData().getCreatedAt() == null) CreatedAt.setText("No data found");
        else CreatedAt.setText(getData().getCreatedAt());

        if (getData().getUpdatedAt() == null) UpdatedAt.setText("No data found");
        else UpdatedAt.setText(getData().getUpdatedAt());


        //Picasso.get().load(getData().getAvatar_url()).into(UserIcon);
    }

    private void setViewPager(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        DetailsFragmentPagerAdapter adapter = new DetailsFragmentPagerAdapter(getSupportFragmentManager(), this, getData().getFullName());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
