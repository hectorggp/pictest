package pictest.objects;

public class FbFriend {

	public static final String TAG_FRIEND_NAME = "name";
	public static final String TAG_FRIEND_ID = "id";

	private String name;
	private long id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
