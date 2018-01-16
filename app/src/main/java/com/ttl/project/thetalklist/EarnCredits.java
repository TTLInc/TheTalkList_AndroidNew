package com.ttl.project.thetalklist;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.Services.LoginService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


public class EarnCredits extends Fragment {

    EditText earn_credit_paypalEmail,earn_credit_paypalammount;
    TextView earn_credit_currentBalance;
    Button earn_credit_submitBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_earn_credits, container, false);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GothamBookRegular.ttf");

        final LoginService loginService=new LoginService();
        loginService.login(getContext().getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getString("email",""),getContext().getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getString("pass",""),getContext());

        earn_credit_paypalEmail= (EditText) view.findViewById(R.id.earn_credit_paypalEmail);
        earn_credit_paypalammount= (EditText) view.findViewById(R.id.earn_credit_paypalaamount);
        //earn_credit_AIESEC_ammount= (EditText) view.findViewById(R.id.earn_credit_AIESEC_ammount);
        earn_credit_currentBalance= (TextView) view.findViewById(R.id.earn_credit_currentBalance);

        //earn_credit_ratePerMinute= (TextView) view.findViewById(R.id.earn_credit_ratePerMinute);
        earn_credit_submitBtn= (Button) view.findViewById(R.id.earn_credit_submitBtn);
        //educationDonation_more= (Button) view.findViewById(R.id.educationDonation_more);
        earn_credit_paypalEmail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        earn_credit_paypalEmail.setTypeface(typeface);
        earn_credit_paypalammount.setTypeface(typeface);
        //earn_credit_AIESEC_ammount.setTypeface(typeface);
        earn_credit_currentBalance.setTypeface(typeface);
        //earn_credit_ratePerMinute.setTypeface(typeface);
        earn_credit_submitBtn.setTypeface(typeface);

        TextView tv= (TextView) getActivity().findViewById(R.id.num_credits);
        earn_credit_currentBalance.setText(tv.getText().toString());


        SharedPreferences preferences = getActivity().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);

            String res="USD $"+String.format("%.02f",preferences.getFloat("hRate", 0.0f) / 25.0f *60)+"/hr ($"+String.format("%.02f",preferences.getFloat("hRate", 0.0f) / 25.0f)+"/min)";
            //earn_credit_ratePerMinute.setText(res);




        earn_credit_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String earn_credit_paypalEmail_str = earn_credit_paypalEmail.getText().toString();
                final String earn_credit_paypalaamount_float = earn_credit_paypalammount.getText().toString();
                final String id = String.valueOf(getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0));
//                if (earn_credit_paypalEmail_str.equals("")) {

                    if (getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getFloat("money", 0.0f) >= Float.parseFloat(earn_credit_paypalaamount_float)) {

                        if (getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("coupon_credits", 0) > 10) {


                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                            final TextView main_credit = (TextView) getActivity().findViewById(R.id.num_credits);
//                https://www.thetalklist.com/api/payment_cashout?uid=17431&payment_account=saurabhit29@gmail.com&trnAmount=1

                            if (!earn_credit_paypalEmail_str.equals("") || !earn_credit_paypalaamount_float.equals("")) {
                                if (Float.parseFloat(earn_credit_currentBalance.getText().toString()) >= Float.parseFloat(earn_credit_paypalammount.getText().toString())) {
                                    final Dialog dialog = new Dialog(getContext());
                                    dialog.setContentView(R.layout.threedotprogressbar);
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.show();
                                    String URL = "https://www.thetalklist.com/api/paypal_cashout?" +
                                            "payment_account=" + earn_credit_paypalEmail_str.replace(" ", "") +
                                            "&trnAmount=" + earn_credit_paypalaamount_float +
                                            "&uid=" + id;
                                    RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
                                    Log.e("url", URL);
                                    StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {


                                            try {
                                                JSONObject obj = new JSONObject(response);

                                                if (obj.getInt("status") == 0) {
                                                    dialog.dismiss();
                                                    Log.e("cashout response", response);
                                                    main_credit.setText(String.valueOf(Float.parseFloat(String.valueOf(obj.getDouble("money")))));
                                                    earn_credit_currentBalance.setText(String.valueOf(Float.parseFloat(String.valueOf(obj.getDouble("money")))));
                                                    Toast.makeText(getContext(), "Funds will be deposited within 24 hours.", Toast.LENGTH_LONG).show();
                                                    earn_credit_paypalEmail.setText("");
                                                    earn_credit_paypalammount.setText("");
                                                    loginService.login(getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getString("email", ""), getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getString("pass", ""), getContext());


                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();


                                            }
                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {


                                        }
                                    }
                                    ) {
                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            HashMap<String, String> headers = new HashMap<String, String>();
                                            headers.put("payment_account", earn_credit_paypalEmail_str);
                                            headers.put("trnAmount", earn_credit_paypalaamount_float);
                                            headers.put("uid", id);
                                            return headers;
                                        }
                                    };


                                    sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    queue1.add(sr);

                                } else
                                    Toast.makeText(getContext(), "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "You can’t cashout promotional credits.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "You don't have enough credits.", Toast.LENGTH_SHORT).show();
                    }
//                }

            }
        });






        return view;
    }


}
