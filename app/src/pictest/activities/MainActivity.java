package pictest.activities;

import java.util.regex.Pattern;

import org.json.JSONException;

import pictest.connection.FbConnManager;
import pictest.connection.PayPalManager;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.pictest.R;

public class MainActivity extends Activity implements OnClickListener {
	private static final String typeFacebook = "com.facebook.auth.login";
	private static final String typeGoogle = "com.google";
	private boolean hasFacebookAccount;
	private boolean hasGoogleAccount;
	private PayPalManager paypal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getAccounts();
		
		((Button) findViewById(R.id.BTNGalery)).setOnClickListener(this);
		((Button) findViewById(R.id.BTNPayPal)).setOnClickListener(this);
		
		FbConnManager.image = getResources().getDrawable(R.drawable.ic_launcher);
		
	}

	private void getAccounts() {
		hasGoogleAccount = hasFacebookAccount = false;
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(getApplicationContext())
				.getAccounts();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				String possibleEmail = account.name;
				Log.d("email:", possibleEmail + " - type: " + account.type);
				
				// to know if exists any of the accounts
				if(account.type.compareToIgnoreCase(typeFacebook) == 0)
					hasFacebookAccount = true;
				if(account.type.compareToIgnoreCase(typeGoogle) == 0)
					hasGoogleAccount = true;
			} else
				Log.d("not email:", account.name + " - type: " + account.type);
		}
		if(hasFacebookAccount)
			Log.d("accounts", "has facebook account");
		if(hasGoogleAccount)
			Log.d("accounts", "has google account");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.BTNGalery:
			startActivity(new Intent(this, FbSignInActivity.class));
			break;
		case R.id.BTNPayPal:
			paypal = new PayPalManager(this);
		//ESTO ES DE EJEMPLO NADA MAS
			paypal.buyPhoto(34, 666, 47757575747880L,
					12.34);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			PaymentConfirmation confirm = data
					.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
			if (confirm != null) {
				try {
					Log.i("paymentExample", confirm.toJSONObject().toString(4));
					paypal.savePayTransaction();
					// TODO: send 'confirm' to your server for verification.
					// see
					// https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
					// for more details.

				} catch (JSONException e) {
					Log.e("paymentExample",
							"an extremely unlikely failure occurred: ", e);
				}
			}
		} else if (resultCode == Activity.RESULT_CANCELED) {
			Log.i("paymentExample", "The user canceled.");
		} else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
			Log.i("paymentExample",
					"An invalid payment was submitted. Please see the docs.");
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (paypal != null)
			paypal.finishService();
		super.onDestroy();
	}

}
