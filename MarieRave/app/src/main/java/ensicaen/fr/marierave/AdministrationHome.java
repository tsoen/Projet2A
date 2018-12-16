package ensicaen.fr.marierave;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class AdministrationHome extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.administration_home, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		Button btnSkills = view.findViewById(R.id.button4);
		btnSkills.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Utils.replaceFragments(AdministrationSkills.class, getActivity(), null, true);
			}
		});
		
		Button btnResults = view.findViewById(R.id.button5);
		btnResults.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Utils.replaceFragments(AdministrationResults.class, getActivity(), null, true);
			}
		});
		
		Button btnBack = view.findViewById(R.id.button1);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		List<String> classNames = new ArrayList<>();
		classNames.add("CP");
		classNames.add("CE1 - A");
		classNames.add("CE1 - B");
		classNames.add("CE2");
		classNames.add("CP");
		classNames.add("CE1 - A");
		classNames.add("CE1 - B");
		classNames.add("CE2");
		classNames.add("CP");
		classNames.add("CE1 - A");
		classNames.add("CE1 - B");
		classNames.add("CE2");
		classNames.add("CP");
		classNames.add("CE1 - A");
		classNames.add("CE1 - B");
		classNames.add("CE2");
		classNames.add("CP");
		classNames.add("CE1 - A");
		classNames.add("CE1 - B");
		classNames.add("CE2");
		GridViewAdapter adapter = new GridViewAdapter(getActivity(), classNames);
		GridView gridview = view.findViewById(R.id.gridview1);
		gridview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id)
			{
				Utils.replaceFragments(PersonnalProfile.class, getActivity(), null, true);
			}
		});
	}
	
	private class GridViewAdapter extends BaseAdapter
	{
		private List<String> _classList;
		private Activity _activity;
		
		GridViewAdapter(Activity activity, List<String> classList)
		{
			super();
			_activity = activity;
			_classList = classList;
		}
		
		@Override
		public int getCount()
		{
			return _classList.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return _classList.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		private class ViewHolder
		{
			private Button _classButton;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.gridview_list_classes_item, null);
				ViewHolder holder = new ViewHolder();
				holder._classButton = convertView.findViewById(R.id.button8);
				
				holder._classButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
						Utils.replaceFragments(ClassAdministration.class, getActivity(), null, true);
					}
				});
				
				holder._classButton.setText(_classList.get(position));
			}
			
			return convertView;
		}
	}
}
