package net.udrunk.adapters;

import java.util.ArrayList;
import java.util.List;

import net.udrunk.R;
import net.udrunk.domain.Place;
import net.udrunk.model.Model;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;

@EBean
public class PlaceAdapater extends BaseAdapter {
	private List<Place> items = new ArrayList<Place>();
	
	@RootContext
	protected Context context;

	@Bean
	protected Model model;

	public void updateItems(List<Place> items)
	{
		if(items == null)
			items = new ArrayList<Place>();
		this.items = items;
		notifyDataSetChanged();
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
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_place_item, parent, false);
		}
		Place place = items.get(position);
		if (place != null) {
			TextView nameText = (TextView) v.findViewById(R.id.txt_name);
			TextView cityText = (TextView) v.findViewById(R.id.txt_city);
			TextView distanceText = (TextView) v.findViewById(R.id.txt_distance);

			nameText.setText(place.getName());
			if (place.getCity() != null) {
				cityText.setText(place.getCity());
			}
			
			Location placeLoc = new Location("Place");
			placeLoc.setLongitude(place.getGeometry().x);
			placeLoc.setLatitude(place.getGeometry().y);
			float dist = placeLoc.distanceTo(model.currentLocation);
			distanceText.setText(((int)dist) + "m");
		}
		return v;
	}
}
