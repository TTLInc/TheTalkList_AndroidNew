package com.ttl.project.thetalklist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;


public class Support extends Fragment {

    ExpandableLinearLayout faqs_layout,video_layout;
    ImageView faqs_btn,video_btn;
    int faqs_bits,video_bits;
    Button supportEmailButton;
    LinearLayout faqs_btn_layout,videos_btn_layout,support_faq_layout,supports_howitworks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_support, container, false);

        video_layout= (ExpandableLinearLayout) view.findViewById(R.id.video1111_layout);
        faqs_layout= (ExpandableLinearLayout) view.findViewById(R.id.faqs_layout);


        video_btn= (ImageView) view.findViewById(R.id.videos_btn);
        faqs_btn= (ImageView) view.findViewById(R.id.faqs_btn);

        supportEmailButton= (Button) view.findViewById(R.id.supportEmailButton);


        faqs_btn_layout= (LinearLayout) view.findViewById(R.id.faqs_btn_layout);
        videos_btn_layout= (LinearLayout) view.findViewById(R.id.videos_btn_layout);
        support_faq_layout= (LinearLayout) view.findViewById(R.id.support_faq_layout);
        supports_howitworks= (LinearLayout) view.findViewById(R.id.supports_howitworks);

        supports_howitworks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentStack.getInstance().push(new Support());
                getFragmentManager().beginTransaction().replace(R.id.viewpager,new Supports_howitworks()).commit();
            }
        });


        support_faq_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentStack.getInstance().push(new Support());
                getFragmentManager().beginTransaction().replace(R.id.viewpager,new Fragment_FAQS()).commit();
            }
        });


        supportEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={"support@thetalklist.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                startActivity(Intent.createChooser(intent, "Send Mail"));
            }
        });

        video_layout.collapse();
        faqs_layout.collapse();

        videos_btn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (faqs_layout.isExpanded()) {
                    faqs_layout.collapse();
                    faqs_btn.setImageResource(R.drawable.side_aerrow);
                    faqs_bits=0;
                }
                FragmentStack.getInstance().push(new Support());
                getFragmentManager().beginTransaction().replace(R.id.viewpager,new Supports_howitworks()).commit();
                video_layout.toggle();
                if (video_bits== 0) {
                   video_btn.setImageResource(R.drawable.down_aerrow);
                    video_bits= 1;
                } else {
                    video_btn.setImageResource(R.drawable.side_aerrow);
                    video_bits= 0;
                }
            }
        });
        faqs_btn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (video_layout.isExpanded()) {
                    video_layout.collapse();
                    video_btn.setImageResource(R.drawable.side_aerrow);
                    video_bits=0;
                }

                faqs_layout.toggle();
                support_faq_layout.performClick();
                if (faqs_bits == 0) {
                    faqs_btn.setImageResource(R.drawable.down_aerrow);
                    faqs_bits = 1;
                } else {
                    faqs_btn.setImageResource(R.drawable.side_aerrow);
                    faqs_bits = 0;
                }
            }
        });



        return view;
    }


}
