package pictest.objects;

public class ServerPay {
	public static final String TAG_ID = "photo_id";
	public static final String PAYER_ID = "payer_id";
	public static final String PAY_PRICE = "pay_price";
	private long photo_id;
	private long payer_id;
	private double pay_price;

	public long getPhotoId() {
		return photo_id;
	}

	public void setId(long id) {
		this.photo_id = id;
	}

	public long getPayer_id() {
		return payer_id;
	}

	public void setPayer_id(long payer_id) {
		this.payer_id = payer_id;
	}

	public double getPay_price() {
		return pay_price;
	}

	public void setPay_price(double pay_price) {
		this.pay_price = pay_price;
	}

	public static String getTagId() {
		return TAG_ID;
	}

	public static String getPayerId() {
		return PAYER_ID;
	}

	public static String getPayPrice() {
		return PAY_PRICE;
	}

}