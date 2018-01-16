package com.ttl.project.thetalklist;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.Services.LoginService;
import com.pascalwelsch.holocircularprogressbar.HoloCircularProgressBar;

import org.json.JSONException;
import org.json.JSONObject;


public class TTL_Score extends Fragment {

    //public TabLayout tabLayout;
//    public ViewPager viewPager;
//    ProgressBar progressBar6;


    TextView main_credit;
    TextView main_cred;
    Button ttlscore_more, redeem_free_session, redeem_travel;
    LinearLayout ttlscore_point_system;
    TextView ttl_score_count, ttl_score_needed;
    private HoloCircularProgressBar mHoloCircularProgressBar;

    private ObjectAnimator mProgressBarAnimator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void animate(final HoloCircularProgressBar progressBar,
                         final Animator.AnimatorListener listener) {
        final float progress = (float) (Math.random() * 2);
        int duration = 3000;
        animate(progressBar, listener, progress, duration);
    }

    private void animate(final HoloCircularProgressBar progressBar, final Animator.AnimatorListener listener,
                         final float progress, final int duration) {

        mProgressBarAnimator = ObjectAnimator.ofFloat(progressBar, "progress", progress);
        mProgressBarAnimator.setDuration(duration);

        mProgressBarAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationCancel(final Animator animation) {
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                progressBar.setProgress(progress);
            }

            @Override
            public void onAnimationRepeat(final Animator animation) {
            }

            @Override
            public void onAnimationStart(final Animator animation) {
            }
        });
        if (listener != null) {
            mProgressBarAnimator.addListener(listener);
        }
        mProgressBarAnimator.reverse();
        mProgressBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                progressBar.setProgress((Float) animation.getAnimatedValue());
            }
        });
        progressBar.setMarkerProgress(progress);
        mProgressBarAnimator.start();
    }


    //    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ttl__score, container, false);

        //tabLayout= (TabLayout) view.findViewById(R.id.tabz);

      /*  progressBar6= (ProgressBar) view.findViewById(R.id.progressBar6);
        progressBar6.setProgress(0);   // Main Progress
        progressBar6.setSecondaryProgress(75); // Secondary Progress
        progressBar6.setMax(100);
        progressBar6.setProgressDrawable(getResources().getDrawable(R.drawable.circular_progressbar));*/

//        final CircleProgressBar circleProgressBar = (CircleProgressBar) view.findViewById(R.id.custom_progressBar);

//        viewPager = (ViewPager) view.findViewById(R.id.ttl_score_viewpager);

        mHoloCircularProgressBar = (HoloCircularProgressBar) view.findViewById(
                R.id.holoCircularProgressBar);

//        mHoloCircularProgressBar.setProgress(0.2f);
        ttlscore_more = (Button) view.findViewById(R.id.ttlscore_more);
        redeem_travel = (Button) view.findViewById(R.id.redeem_travel);
        redeem_free_session = (Button) view.findViewById(R.id.redeem_free_session);
        ttlscore_point_system = (LinearLayout) view.findViewById(R.id.ttlscore_point_system);


        ttl_score_count = (TextView) view.findViewById(R.id.ttl_score_count);
        ttl_score_needed = (TextView) view.findViewById(R.id.ttl_score_needed);


        setFragment((int)getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getFloat("ttl_points", 0.0f));
      /*  if (getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getFloat("ttl_points", 0.0f) < 200) {
            ttl_score_count.setText(String.valueOf(200.0f - getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getFloat("ttl_points", 0.0f)));
            mHoloCircularProgressBar.setProgress((float) 1-(200.0f - getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getFloat("ttl_points", 0.0f))/200);



        } else {
            ttl_score_count.setText(String.valueOf(getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getFloat("ttl_points", 0.0f)));

            ttl_score_needed.setVisibility(View.GONE);
        }
        ttlscore_point_system.setVisibility(View.GONE);

        ttlscore_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ttlscore_more.getText().toString().equals("More...")) {
                    ttlscore_point_system.setVisibility(View.VISIBLE);
                    ttlscore_more.setText("Less...");
                } else {
                    ttlscore_point_system.setVisibility(View.GONE);
                    ttlscore_more.setText("More...");
                }
            }
        });

        if (getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getFloat("ttl_points", 0.0f) < 200.0f) {
            redeem_travel.setClickable(false);
            redeem_free_session.setClickable(false);

            redeem_travel.setBackgroundColor(Color.parseColor("#9E9E9E"));
            redeem_free_session.setBackgroundColor(Color.parseColor("#9E9E9E"));
        }else {
            redeem_travel.setClickable(true);
            redeem_free_session.setClickable(true);
        }
*/
         main_credit= (TextView) getActivity().findViewById(R.id.num_ttlScore);
         main_cred= (TextView) getActivity().findViewById(R.id.num_credits);

        final SharedPreferences pref = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        redeem_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Float.parseFloat(main_credit.getText().toString())>=200.0f) {

                    String Url = "https://www.thetalklist.com/api/ttl_points_rewards_travel?uid=" + pref.getInt("id", 0)
                            + "&name=" + pref.getString("usernm", "").replace(" ", "%20") + "&redemption=Prize&ttl_points=" + pref.getFloat("money", 0.0f);
                    StringRequest sr = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                            Log.e("res ttl travel", response);
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getInt("status") != 0) {
                                    Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Your name is now in our drawing. Good luck!", Toast.LENGTH_SHORT).show();
                                    LoginService loginService = new LoginService();
                                    loginService.login(pref.getString("email", ""), pref.getString("pass", ""), getContext());

                                    if (obj.getDouble("total_points") == 0.0) {
                                        ttl_score_count.setText("200.0");
                                        ttl_score_needed.setVisibility(View.VISIBLE);
                                    } else {
                                        main_credit.setText(String.valueOf(Integer.parseInt(String.valueOf(obj.getInt("total_points")))));
                                        ttl_score_count.setText(String.valueOf(Float.parseFloat(String.valueOf(obj.getDouble("total_points")))));
                                        if (obj.getDouble("total_points") < 200) {

                                            redeem_travel.setClickable(false);
                                            redeem_free_session.setClickable(false);

                                            redeem_travel.setFocusable(false);
                                            redeem_free_session.setFocusable(false);

                                            redeem_travel.setBackgroundColor(Color.parseColor("#9E9E9E"));
                                            redeem_free_session.setBackgroundColor(Color.parseColor("#9E9E9E"));
                                        }
                                    }
                                    setFragment((int) obj.getDouble("total_points"));
//                                main_credit.setText("text");
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    Volley.newRequestQueue(getContext()).add(sr);

                }

            }
        });

        redeem_free_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Float.parseFloat(main_credit.getText().toString())>=200.00f) {
                    String Url = "https://www.thetalklist.com/api/ttl_points_rewards_credit?uid=" + pref.getInt("id", 0)
                            + "&name=" + pref.getString("usernm", "").replace(" ", "%20") + "&redemption=Prize&ttl_points=" + pref.getFloat("money", 0.0f);
                    StringRequest sr = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                            Log.e("res ttl credit", response);
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getInt("status") != 0) {
                                    Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "10 credits added to your account for a free session!", Toast.LENGTH_SHORT).show();
                                    LoginService loginService = new LoginService();
                                    loginService.login(pref.getString("email", ""), pref.getString("pass", ""), getContext());

                                    main_cred.setText(String.valueOf(Float.parseFloat(String.valueOf(obj.getDouble("total_credits")))));
                                    if (obj.getDouble("total_points") == 0.0) {
                                        ttl_score_count.setText("200.0");
                                        ttl_score_needed.setVisibility(View.VISIBLE);
                                    } else {
                                        ttl_score_count.setText(String.valueOf(Float.parseFloat(String.valueOf(obj.getInt("total_points")))));
                                        main_credit.setText(String.valueOf(Integer.parseInt(String.valueOf(obj.getInt("total_points")))));
                                        if (obj.getDouble("total_points") < 200) {
                                            redeem_travel.setClickable(false);
                                            redeem_free_session.setClickable(false);

                                            redeem_travel.setFocusable(false);
                                            redeem_free_session.setFocusable(false);


                                            redeem_travel.setBackgroundColor(Color.parseColor("#9E9E9E"));
                                            redeem_free_session.setBackgroundColor(Color.parseColor("#9E9E9E"));


//                                        if (obj.getDouble("total_points")==0.0)


                                        }
//                                main_credit.setText("text");
                                    }
                                    setFragment((int) obj.getDouble("total_points"));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    Volley.newRequestQueue(getContext()).add(sr);
                }
            }
        });

//        setupViewPager(viewPager);

        //tabLayout.setupWithViewPager(viewPager);

//<<<<<<< HEAD
//=======
        //tabLayout.addTab(tab1);*/
//>>>>>>> origin/master

        //TextView ttl_score= (TextView) view.findViewById(R.id.ttl_score_ttl);
//        SharedPreferences pref = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);

//        progressBar6 = (ProgressBar) view.findViewById(R.id.progressBar6);

//        progressBar6.set

        //ttl_score.setText(String.valueOf(pref.getFloat("ttl_points",0.0f)));

        //tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#4DB806"));

        //tabLayout.setSelected(true);
        return view;
    }

  /*  private void setupViewPager(ViewPager viewPager) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        TTL_Score.ViewPagerAdapter adapter = new TTL_Score.ViewPagerAdapter(fragmentManager);

        adapter.addFragment(new TTL_Score_Drawer_viewpager(), "Tutor");
        adapter.addFragment(new TTL_Score_Drawer_viewpager(), "Student");
        viewPager.setAdapter(adapter);

    }*/

   /* private static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(android.support.v4.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }*/



   public void setFragment(int points){
       if (points < 200 && points>0 ) {
           ttl_score_count.setText(String.valueOf(200 - points));
           mHoloCircularProgressBar.setProgress((float) 1-(200 - (int)getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getFloat("ttl_points", 0.0f))/200);
           redeem_travel.setClickable(false);
           redeem_free_session.setClickable(false);

           redeem_travel.setFocusable(false);
           redeem_free_session.setFocusable(false);
           ttl_score_needed.setVisibility(View.VISIBLE);

       }else if(points<0){
           ttl_score_count.setText(String.valueOf(200));
           ttl_score_needed.setVisibility(View.VISIBLE);
       } else if (points==0){
           ttl_score_count.setText(String.valueOf(200));
           ttl_score_needed.setVisibility(View.VISIBLE);
           mHoloCircularProgressBar.setProgress(0f);
       }else {
//           ttl_score_count.setText(String.valueOf(getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getFloat("ttl_points", 0.0f)));
           ttl_score_count.setText(String.valueOf(points));

           ttl_score_needed.setVisibility(View.GONE);
           mHoloCircularProgressBar.setProgress(1f);


       }
       ttlscore_point_system.setVisibility(View.GONE);

       ttlscore_more.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (ttlscore_more.getText().toString().equals("More...")) {
                   ttlscore_point_system.setVisibility(View.VISIBLE);
                   ttlscore_more.setText("Less...");
               } else {
                   ttlscore_point_system.setVisibility(View.GONE);
                   ttlscore_more.setText("More...");
               }
           }
       });

       if (points < 200.0f) {
           redeem_travel.setClickable(false);
           redeem_free_session.setClickable(false);

           redeem_travel.setFocusable(false);
           redeem_free_session.setFocusable(false);
           ttl_score_needed.setVisibility(View.VISIBLE);

           redeem_travel.setBackgroundColor(Color.parseColor("#9E9E9E"));
           redeem_free_session.setBackgroundColor(Color.parseColor("#9E9E9E"));
           mHoloCircularProgressBar.setProgress((float) 1-(200.0f - points)/200);

           if (points<0){
               mHoloCircularProgressBar.setProgress(0f);
//               ttl_score_count.setText("0.00");
//               main_credit.setText("0.00");
           }
       }else {
           redeem_travel.setClickable(true);
           redeem_free_session.setClickable(true);
       }

   }
}
