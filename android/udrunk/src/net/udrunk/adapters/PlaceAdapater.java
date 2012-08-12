package net.udrunk.adapters;

import java.util.List;

import net.udrunk.R;
import net.udrunk.domain.Place;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlaceAdapater extends BaseAdapter {
	private List<Place> items;
	private Context context;

	public PlaceAdapater(Context context, int textViewResourceId,
			List<Place> items) {
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
			v = vi.inflate(R.layout.list_place_item, parent, false);
		}
		Place place = items.get(position);
		if (place != null) {
			TextView nameText = (TextView) v.findViewById(R.id.txt_name);
			TextView cityText = (TextView) v.findViewById(R.id.txt_city);

			nameText.setText(place.getName());
			if (place.getCity() != null) {
				cityText.setText(place.getCity());
			}
		}
		return v;
	}
}
