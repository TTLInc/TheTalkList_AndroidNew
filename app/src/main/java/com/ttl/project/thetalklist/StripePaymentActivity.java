package com.ttl.project.thetalklist;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.StripePaymentGatewayIntegrationFolder.ProgressDialogFragment;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONException;
import org.json.JSONObject;

import rx.subscriptions.CompositeSubscription;

//Stripe payment activity

public class StripePaymentActivity extends AppCompatActivity {

    //        public static final String  PUBLISHABLE_KEY = "pk_test_m2095bSj8vVA0n55nBjcBRDH";
    public static final String PUBLISHABLE_KEY = "pk_live_ERuUHGMtI96h5FOLEy0NrU4C";
    public static final String APPLICATION_ID = "RKNck9SdN6sqcznBvy5lqnN2ln1FrrSabNcq8YEK";
    public static final String CLIENT_KEY = "zWtkaYFS0Ia91jKkgmIHJql30cARcrDmKUGAXLTY";
    public static final String BACK4PAPP_API = "https://parseapi.back4app.com/";
    private CardInputWidget mCardInputWidget;
    private ProgressDialogFragment mProgressDialogFragment;
    private long mPrice;
    private Stripe mStripe;
    private CompositeSubscription mCompositeSubscription;
    Card card;
    private static final String TAG = "StripePaymentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_payment);

        mPrice = 100;

        progress = new ProgressDialog(this);


        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
        mCompositeSubscription = new CompositeSubscription();
        mStripe = new Stripe(this, PUBLISHABLE_KEY);


        mProgressDialogFragment =
                ProgressDialogFragment.newInstance(R.string.completing_purchase);

        Button purchase = (Button) findViewById(R.id.purchase);


        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    card = mCardInputWidget.getCard();
                    if (card == null) {
                        displayError("Card Input Error");
                        return;
                    } else {
                        ImageView cardView = (ImageView)findViewById( R.id.cardView);
                        if ( card.getBrand().equals( Card.AMERICAN_EXPRESS ))
                            cardView.setImageDrawable( getDrawable( R.drawable.amex_payment));
                        else if( card.getBrand().equals( Card.DISCOVER) )
                            cardView.setImageDrawable( getDrawable( R.drawable.discover_payment));
                        else if( card.getBrand().equals( Card.VISA) )
                            cardView.setImageDrawable( getDrawable( R.drawable.visa_payment));
                        else if( card.getBrand().equals( Card.MASTERCARD) )
                            cardView.setImageDrawable( getDrawable( R.drawable.master_card_payment));
                        buy();
                    }
                } catch (IllegalStateException e) {
                    Toast.makeText(StripePaymentActivity.this, "Reopen application", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    //Method run code of buying the credits
    private void buy() {
        boolean validation = card.validateCard();
        if (validation) {
            startProgress("Validating Credit Card");
            new Stripe(this, PUBLISHABLE_KEY).createToken(
                    card,
                    PUBLISHABLE_KEY,
                    new TokenCallback() {
                        @Override
                        public void onError(Exception error) {
                            Log.d("Stripe", error.toString());
                        }

                        @Override
                        public void onSuccess(Token token) {
                            finishProgress();
                            SharedPreferences paymentPref = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                            final int money = paymentPref.getInt("ammount", 0);


                            String URL = "https://www.thetalklist.com/api/stripe_payment?access_token=" + token.getId() + "&id=" + getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0) + "&amount=" + money;
                            Log.e("stripe  url", URL);

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {


                                    try {
                                        JSONObject resObj = new JSONObject(response);
                                        if (resObj.getInt("status") == 0) {
                                            Toast.makeText(StripePaymentActivity.this, "Payment Successful!", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(StripePaymentActivity.this, money + " Credits Added!", Toast.LENGTH_SHORT).show();
                                            Log.e("payment response", response);
                                            Dialog d = new Dialog(StripePaymentActivity.this);
                                            d.setContentView(R.layout.stripe_payments_successful_popup);

                                            TextView stripe_sucess_popup_txt = d.findViewById(R.id.stripe_sucess_popup_txt);
                                            stripe_sucess_popup_txt.setText("Your balance has increased by " + money + " credits.");
                                            d.show();
                                            Button stripe_ok = d.findViewById(R.id.stripe_ok);
                                            stripe_ok.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
//                                                    startActivity(new Intent(getApplicationContext(),SettingFlyout.class));

                                                    Intent i=new Intent(getApplicationContext(),SettingFlyout.class);
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);


                                                   /* try {
                                                        final FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                                                        fragmentTransaction1.replace(R.id.viewpager, new Earn_Buy_tabLayout()).commit();
                                                        finish();
                                                    } catch (Exception e) {
                                                        Log.e(TAG, "Reload Error " + e);
                                                    }*/

                                                }
                                            });


                                        } else {
                                            Toast.makeText(StripePaymentActivity.this, "Error in credit card entry.", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(StripePaymentActivity.this, "Stripe Payment error. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 15, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            queue.add(stringRequest);
                        }
                    });
        } else if (!card.validateNumber()) {
            Toast.makeText(this, "Card details invalid", Toast.LENGTH_SHORT).show();
            Log.d("Stripe", "The card number that you entered is invalid");
        } else if (!card.validateExpiryDate()) {
            Toast.makeText(this, "Card details invalid", Toast.LENGTH_SHORT).show();
            Log.d("Stripe", "The expiration date that you entered is invalid");
        } else if (!card.validateCVC()) {
            Toast.makeText(this, "Card details invalid", Toast.LENGTH_SHORT).show();
            Log.d("Stripe", "The CVC code that you entered is invalid");
        } else {
            Toast.makeText(this, "Card details invalid", Toast.LENGTH_SHORT).show();
            Log.d("Stripe", "The card details that you entered are invalid");
        }
    }


    private ProgressDialog progress;

    private void startProgress(String title) {
        progress.setTitle(title);
        progress.setMessage("Please Wait");
        progress.show();
    }

    private void finishProgress() {
        progress.dismiss();
    }


    private void displayError(String errorMessage) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(errorMessage);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent I = new Intent(getApplicationContext(), StripePaymentActivity.class);
                        startActivity(I);
                    }
                });
        alertDialog.show();
    }

}
