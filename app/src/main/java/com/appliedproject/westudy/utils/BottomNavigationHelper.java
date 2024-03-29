package com.appliedproject.westudy.utils;

import android.util.Log;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class BottomNavigationHelper {
    private static final String TAG = "BottomNavigationView";
    public static void setupBottomNavigationView(AnimatedBottomBar bottomBar){
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        bottomBar.setSelectedTabType(AnimatedBottomBar.TabType.TEXT);
    }

//    public static void enableNavigation(final Context context, AnimatedBottomBar bottomBar){
//        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
//            @Override
//            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NotNull AnimatedBottomBar.Tab tab1) {
//                    switch (i){
//                        case R.id.action_study:
//                            Intent studyIntent = new Intent(context, StudyActivity.class);
//                            context.startActivity(studyIntent);
//                            break;
//                        case R.id.action_explore:
//                            Intent exploreIntent = new Intent(context, ExploreActivity.class);
//                            context.startActivity(exploreIntent);
//                            break;
//                        case R.id.action_profile:
//                            Intent profileIntent = new Intent(context, ProfileActivity.class);
//                            context.startActivity(profileIntent);
//                            break;
//                    }
//            }
//
//            @Override
//            public void onTabReselected(int i, @NotNull AnimatedBottomBar.Tab tab) {
//
//            }
//        });
//    }
}
