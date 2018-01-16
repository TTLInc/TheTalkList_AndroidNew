package com.ttl.project.thetalklist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class BuyCredits extends Fragment {
    Button buybtn_cr10,buybtn_cr55,buybtn_cr110;
TextView buyCredit_currentBalance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_buy_credits, container, false);
        buybtn_cr10= (Button) view.findViewById(R.id.buybtn_cr10);
        buybtn_cr55= (Button) view.findViewById(R.id.buybtn_cr55);
        buybtn_cr110= (Button) view.findViewById(R.id.buybtn_cr110);
        buyCredit_currentBalance= (TextView) view.findViewById(R.id.buyCredit_currentBalance);

        TextView tv= (TextView) getActivity().findViewById(R.id.num_credits);
        buyCredit_currentBalance.setText(tv.getText().toString());


        SharedPreferences paymentPref=getContext().getSharedPreferences("loginStatus",Context.MODE_PRIVATE);
        final SharedPreferences.Editor rdit=paymentPref.edit();
        buybtn_cr10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getContext(),StripePaymentActivity.class);
                i.putExtra("ammount",10);
                rdit.putInt("ammount",10).apply();
                startActivity(i);
            }
        });

        buybtn_cr55.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),StripePaymentActivity.class);
                i.putExtra("ammount",50);
                rdit.putInt("ammount",50).apply();
                startActivity(i);
            }
        });
        buybtn_cr110.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),StripePaymentActivity.class);
                i.putExtra("ammount",100);
                rdit.putInt("ammount",100).apply();
                startActivity(i);
            }
        });

        return view;
    }

}
