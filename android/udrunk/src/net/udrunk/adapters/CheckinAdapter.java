package net.udrunk.adapters;

import java.util.List;

import net.udrunk.R;
import net.udrunk.UdrunkApplication;
import net.udrunk.domain.Checkin;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckinAdapter extends BaseAdapter {

	private List<Checkin> items;
	private Context context;

	public CheckinAdapter(Context context, int textViewResourceId,
			List<Checkin> items) {
		super();
		this.context = context;
		this.items = items;
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return items.get(position);
	}
	
	/** Use the array index as a unique id. */
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_feed_item, parent, false);
		}
		Checkin feed = items.get(position);
		if (feed != null) {
			ImageView avatarImg = (ImageView) v.findViewById(R.id.img_avatar);
			TextView nameText = (TextView) v.findViewById(R.id.txt_name);
			TextView statusText = (TextView) v.findViewById(R.id.txt_status);
			TextView percentText = (TextView) v.findViewById(R.id.txt_level);
			TextView placeText = (TextView) v.findViewById(R.id.txt_place);
			TextView dateText = (TextView) v.findViewById(R.id.txt_date);

			nameText.setText(feed.getUser().getUsername());
			UdrunkApplication.imageLoader.bind(avatarImg, feed.getUser()
					.getAvatar(), null);
			dateText.setText(feed.getAdded());

			if (feed.getStatus() != null && feed.getStatus() != "") {
				statusText.setText(feed.getStatus());
			} 
			if (feed.getPlace() != null && feed.getPlace().getName() != null) {
				placeText.setText("@ " + feed.getPlace().getName());
			}
			if (feed.getLevel() != null) {
				percentText.setText(feed.getLevel() + "%");
			}
		}
		return v;
	}
}
