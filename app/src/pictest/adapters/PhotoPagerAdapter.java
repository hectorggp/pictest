package pictest.adapters;

import java.util.List;

import pictest.objects.FbPhoto;
import uk.co.senab.photoview.PhotoView;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.pictest.R;

public class PhotoPagerAdapter extends PagerAdapter {

	private List<FbPhoto> list;

	public PhotoPagerAdapter(List<FbPhoto> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public View instantiateItem(ViewGroup container, final int position) {
		PhotoView photoView = new PhotoView(container.getContext());
		if (list.get(position).getImage() != null)
			photoView.setImageDrawable(list.get(position).getImage());
		else
			photoView.setImageResource(R.drawable.ic_launcher);

		// Now just add PhotoView to ViewPager and return it
		container.addView(photoView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		return photoView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

}
