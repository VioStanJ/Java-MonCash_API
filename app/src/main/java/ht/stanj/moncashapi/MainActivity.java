package ht.stanj.moncashapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.digicelgroup.moncash.APIContext;
import com.digicelgroup.moncash.exception.MonCashRestException;
import com.digicelgroup.moncash.http.Constants;
import com.digicelgroup.moncash.payments.Payment;
import com.digicelgroup.moncash.payments.PaymentCapture;
import com.digicelgroup.moncash.payments.PaymentCreator;
import com.digicelgroup.moncash.payments.TransactionId;

import org.apache.http.HttpStatus;

public class MainActivity extends AppCompatActivity {

    Button achte1 ;
    Button achte2 ;

    private APIContext apiContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        achte1 = findViewById(R.id.achte1);
        achte2 = findViewById(R.id.achte2);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        apiContext = new APIContext(getString(R.string.client_id),
                    getString(R.string.client_secret),Constants.SANDBOX);

        achte1.setOnClickListener(v -> {
            paid(445);
        });

        achte2.setOnClickListener(v -> {
            paid(1040);
        });
    }

    private void paid(float price) {
        PaymentCreator paymentCreator = new PaymentCreator();
        Payment payment = new Payment();
        payment.setOrderId(System.currentTimeMillis()+"");
        payment.setAmount(price);
        PaymentCreator creator = null;
        try {
            creator = paymentCreator.execute(apiContext, PaymentCreator.class, payment);
            if(creator.getStatus() !=null &&
                    creator.getStatus().compareTo(HttpStatus.SC_ACCEPTED+"")==0){
                Log.e("MONCASH","redirect to the link below");
                //creator.redirectUri() method return the payment gateway url
                Log.e("MONCASH",creator.redirectUri());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(creator.redirectUri()));
                startActivity(browserIntent);
            }else if(creator.getStatus()==null){
                Log.e("MONCASH","Error");
                Log.e("MONCASH",creator.getError());
                Log.e("MONCASH",creator.getError_description());
            }else{
                Log.e("MONCASH","Error");
                Log.e("MONCASH",creator.getStatus());
                Log.e("MONCASH",creator.getError());
                Log.e("MONCASH",creator.getMessage());
                Log.e("MONCASH",creator.getPath());
            }
        } catch (MonCashRestException e) {
            e.printStackTrace();
        }
    }

    //    http://tutorialspots.com/android-how-to-fix-error-android-os-networkonmainthreadexception-at-android-os-strictmodeandroidblockguardpolicy-onnetwork-6586.html

}