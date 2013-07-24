package pictest.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class FbSignInActivity extends SherlockFragmentActivity {

	// private FragmentFbSignInActivity mainFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setHomeButtonEnabled(true);
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// if (savedInstanceState == null) {
		// // Add the fragment on initial activity setup
		// mainFragment = new FragmentFbSignInActivity();
		// getSupportFragmentManager().beginTransaction()
		// .add(android.R.id.content, mainFragment).commit();
		// } else {
		// // Or set the fragment from restored state info
		// mainFragment = (FragmentFbSignInActivity) getSupportFragmentManager()
		// .findFragmentById(android.R.id.content);
		// }

		ActionBar.Tab tab1 = bar.newTab();
		ActionBar.Tab tab2 = bar.newTab();
		tab1.setText("Facebook");
		tab2.setText("Google");
		tab1.setTabListener(new MyTabListener());
		tab2.setTabListener(new MyTabListener());
		bar.addTab(tab1);
		bar.addTab(tab2);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class MyTabListener implements ActionBar.TabListener {

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			switch (tab.getPosition()) {
			case 0:
				FragmentFbSignInActivity frag1 = new FragmentFbSignInActivity();
				ft.replace(android.R.id.content, frag1);
				break;
			case 1:
				FragmentGgSignInActivity frag2 = new FragmentGgSignInActivity();
				ft.replace(android.R.id.content, frag2);
				break;
			}
		}

		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
			// TODO Auto-generated method stub

		}

	}
}
