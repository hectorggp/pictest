/**
 * 
 */
package pictest.connection;

import java.math.BigDecimal;

import org.holoeverywhere.widget.Toast;

import pictest.objects.ServerPay;
import android.app.Activity;
import android.content.Intent;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

/**
 * @author julia_000
 * 
 */
public class PayPalManager {

	// esta en modo de debug online
	private static final String CONFIG_ENVIRONMENT = PaymentActivity.ENVIRONMENT_SANDBOX;

	// este client id es para probar la app online y te deje hacer pagos OJO!
	private static final String CONFIG_CLIENT_ID = "Ac0CIhBiG8rPg7M0XYQS7KwSLf46E6Yd4YVtzo2yWwo3PgrOCmNXxd-kWtIQ";

	private ServerPay transaction;

	private Activity context;

	/**
	 * Constructor del manejador de PayPal
	 * 
	 * @param context
	 */
	public PayPalManager(Activity context) {
		this.context = context;
		this.startService();
		transaction = new ServerPay();
	}

	private void startService() {
		Intent intent = new Intent(context, PayPalService.class);

		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT,
				CONFIG_ENVIRONMENT);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
		intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, "wait-for-it!!");

		context.startService(intent);
	}

	/**
	 * Metodo para comprar una foto.
	 * 
	 * @param payer_id
	 *            El id del comprador
	 * @param owner_id
	 *            El id del dueño de la foto.
	 * @param id
	 *            El id de la foto a comprar.
	 * @param pay_price
	 *            El precio que se ofrece.
	 */
	public void buyPhoto(long payer_id, long owner_id, long id, double pay_price) {
		if (payer_id == -1) {
			Toast.makeText(context, "No has iniciado sesion.",
					Toast.LENGTH_LONG).show();
			return;
		}

		PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(
				String.valueOf(pay_price)), "USD", "Photo_" + id);

		Intent intent = new Intent(context, PaymentActivity.class);

		String receiver = getReceiverPayPalAccount(owner_id);
		String payer = getPayerPayPalAccount();

		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT,
				CONFIG_ENVIRONMENT);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
		intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, receiver);

		// It's important to repeat the clientId here so that the SDK has it if
		// Android restarts your
		// app midway through the payment UI flow.
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID,
				"APP-80W284485P519543T");
		intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, payer);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

		transaction.setId(id);
		transaction.setPay_price(pay_price);
		transaction.setPayer_id(payer_id);

		context.startActivityForResult(intent, 0);
	}

	/**
	 * Es necesario terminar el servicio!!
	 */
	public void finishService() {
		context.stopService(new Intent(context, PayPalService.class));
	}

	// ------------------------------------------------------------------------
	// Esto es lo que tenes que implementar
	// ------------------------------------------------------------------------

	/**
	 * Metodo para obtener la cuenta PayPal del comprador
	 * 
	 * @return
	 */
	public String getPayerPayPalAccount() {
		// Si no existe podes agregarla aca en el servidor :3
		return "wait-for-it";
	}

	/**
	 * Metodo para objtener la cuenta PayPal del vendedor
	 * 
	 * @param owner_id
	 *            La id del vendedor, si no lo necesitas obvialo
	 * @return
	 */
	public String getReceiverPayPalAccount(long owner_id) {
		return "jchamale.usac-faciitator@gmail.com";
	}

	/**
	 * Guarda una transaccion exitosa en el servidor.
	 */
	public void savePayTransaction() {
		downloadImage(transaction.getPhotoId());
		// guardar transaccion en el servidor, LOS DATOS ENSTAN EN TRANSACTION
		// OBJECT
	}

	public void downloadImage(long photo_id) {

	}
}
