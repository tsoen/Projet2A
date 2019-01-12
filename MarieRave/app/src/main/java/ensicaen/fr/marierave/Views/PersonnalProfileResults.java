package ensicaen.fr.marierave.Views;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;

public class PersonnalProfileResults extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.personnal_profile_results, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		SkillListViewAdapter adapter = new SkillListViewAdapter(getActivity());
		adapter.addBigSectionHeaderItem("Français");
		adapter.addLittleSectionHeaderItem("Lire et écrire");
		adapter.addBigSectionHeaderItem("Maths");
		adapter.addLittleSectionHeaderItem("Numération");
		adapter.addBigSectionHeaderItem("Sport");
		adapter.addLittleSectionHeaderItem("Courir");
		
		ListView listView = view.findViewById(R.id.listviewResults);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		Button btnBack = view.findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		Button btnResults = view.findViewById(R.id.btnResults);
		btnResults.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Utils.replaceFragments(PersonnalProfile.class, getActivity(), null, true);
			}
		});
	}
	
	private class SkillListViewAdapter extends BaseAdapter
	{
		private static final int TYPE_BIG_SEPARATOR = 0;
		private static final int TYPE_LITTLE_SEPARATOR = 1;
		
		private ArrayList<String> _headersList = new ArrayList<>();
		private TreeSet<Integer> _bigHeaders = new TreeSet<>();
		private TreeSet<Integer> _littleHeaders = new TreeSet<>();
		
		private Activity _activity;
		
		SkillListViewAdapter(Activity activity)
		{
			super();
			_activity = activity;
		}
		
		void addBigSectionHeaderItem(final String item)
		{
			_headersList.add(item);
			_bigHeaders.add(_headersList.size() - 1);
			notifyDataSetChanged();
		}
		
		void addLittleSectionHeaderItem(final String item)
		{
			_headersList.add(item);
			_littleHeaders.add(_headersList.size() - 1);
			notifyDataSetChanged();
		}
		
		@Override
		public int getItemViewType(int position)
		{
			return _bigHeaders.contains(position) ? TYPE_BIG_SEPARATOR : TYPE_LITTLE_SEPARATOR;
		}
		
		@Override
		public int getViewTypeCount()
		{
			return 2;
		}
		
		@Override
		public int getCount()
		{
			return _headersList.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return _headersList.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		private class ViewHolder
		{
			private TextView _txtTopic;
			private ProgressBar _prgsbarSkill;
			private TextView _txtPercent;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.listview_progressbar_item, null);
				
				ViewHolder holder = new ViewHolder();
				holder._txtTopic = convertView.findViewById(R.id.txtTopic);
				holder._prgsbarSkill = convertView.findViewById(R.id.progressBar);
				holder._txtPercent = convertView.findViewById(R.id.txtPercent);
				
				if (getItemViewType(position) == TYPE_BIG_SEPARATOR) {
					holder._txtTopic.setText(_headersList.get(position));
					holder._txtTopic.setTypeface(Typeface.DEFAULT_BOLD);
				}
				else {
					holder._txtTopic.setText("\t\t" + _headersList.get(position));
				}
				
				holder._prgsbarSkill.incrementProgressBy(50);
				holder._txtPercent.setText(holder._prgsbarSkill.getProgress() + "%");
			}
			
			return convertView;
		}
	}
}
