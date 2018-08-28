package app.pictograma.com.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;


import app.pictograma.com.R;
import app.pictograma.com.adapter.TourFragmentPagerAdapter;
import app.pictograma.com.config.AppPreferences;


public class TourActivity extends FragmentActivity {
    private AppPreferences app;

    /**
     * The pager widget, which handles animation and allows swiping horizontally
     * to access previous and next pages.
     */
    ViewPager pager = null;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    TourFragmentPagerAdapter pagerAdapter;



    @Override
    protected void onCreate(Bundle arg0) {

        super.onCreate(arg0);
        this.setContentView(R.layout.activity_tour);

        // Instantiate a ViewPager
        this.pager = (ViewPager) this.findViewById(R.id.pager);

        app = new AppPreferences(getApplicationContext());

        // Create an adapter with the fragments we show on the ViewPager
        TourFragmentPagerAdapter adapter = new TourFragmentPagerAdapter(
                getSupportFragmentManager());
        adapter.addFragment(ScreenSlidePageFragment.newInstance(R.drawable.ic_tour_logo,R.drawable.ic_tour1,getString(R.string.tour1),1));
        adapter.addFragment(ScreenSlidePageFragment.newInstance(R.drawable.ic_tour_logo,R.drawable.ic_tour2,getString(R.string.tour2),2));
        adapter.addFragment(ScreenSlidePageFragment.newInstance(R.drawable.ic_tour_logo,R.drawable.ic_tour3,getString(R.string.tour3),3));


        this.pager.setAdapter(adapter);

        PageIndicatorView pageIndicatorView = (PageIndicatorView) this.findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setViewPager(pager);

        pageIndicatorView.setInteractiveAnimation(true);
        pageIndicatorView.setAnimationType(AnimationType.THIN_WORM);


    }

    @Override
    public void onBackPressed() {

        // Return to previous page when we press back button
        if (this.pager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);

    }

    public void close(View v)
    {

        app.setTour("1");
        Intent mainIntent = new Intent().setClass(
                TourActivity.this, LoginActivity.class);
        startActivity(mainIntent);

        // Close the activity so the user won't able to go back this
        // activity pressing Back button
        finish();
    }



}
