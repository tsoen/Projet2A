package ensicaen.fr.marierave;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
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
		
		SkillListViewAdapter a = new SkillListViewAdapter(getActivity());
		
		a.addBigSectionHeaderItem("Français");
		a.addLittleSectionHeaderItem("Lire et écrire");
		a.addBigSectionHeaderItem("Maths");
		a.addLittleSectionHeaderItem("Numération");
		a.addBigSectionHeaderItem("Histoire");
		a.addLittleSectionHeaderItem("Courir");
		
		ListView v = view.findViewById(R.id.listviewResults);
		v.setAdapter(a);
		a.notifyDataSetChanged();
		
		Button btnBack = view.findViewById(R.id.button2);
		btnBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
	}
	
	public class SkillListViewAdapter extends BaseAdapter
	{
		private static final int TYPE_BIG_SEPARATOR = 0;
		private static final int TYPE_LITTLE_SEPARATOR = 1;
		
		private ArrayList<String> productList = new ArrayList<>();
		private TreeSet<Integer> bigSectionHeader = new TreeSet<>();
		private TreeSet<Integer> littleSectionHeader = new TreeSet<>();
		
		private Activity activity;
		
		public SkillListViewAdapter(Activity activity)
		{
			super();
			this.activity = activity;
		}
		
		public void addBigSectionHeaderItem(final String item)
		{
			productList.add(item);
			bigSectionHeader.add(productList.size() - 1);
			notifyDataSetChanged();
		}
		
		public void addLittleSectionHeaderItem(final String item)
		{
			productList.add(item);
			littleSectionHeader.add(productList.size() - 1);
			notifyDataSetChanged();
		}
		
		@Override
		public int getItemViewType(int position)
		{
			return bigSectionHeader.contains(position) ? TYPE_BIG_SEPARATOR : TYPE_LITTLE_SEPARATOR;
		}
		
		@Override
		public int getViewTypeCount()
		{
			return 2;
		}
		
		@Override
		public int getCount()
		{
			return productList.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return productList.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		private class ViewHolder
		{
			TextView txtTopic;
			ProgressBar prgsbarSkill;
			TextView txtPercent;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			
			ViewHolder holder;
			LayoutInflater inflater = activity.getLayoutInflater();
			Log.d("myapp", "K");
			if (convertView == null) {
				Log.d("myapp", "KK");
				convertView = inflater.inflate(R.layout.listview_item_progressbar, null);
				holder = new ViewHolder();
				holder.txtTopic = convertView.findViewById(R.id.textView9);
				holder.prgsbarSkill = convertView.findViewById(R.id.progressBar);
				holder.txtPercent = convertView.findViewById(R.id.textView10);
				
				
				if (getItemViewType(position) == TYPE_BIG_SEPARATOR) {
					holder.txtTopic.setText(productList.get(position));
					holder.txtTopic.setTypeface(Typeface.DEFAULT_BOLD);
				}
				else {
					holder.txtTopic.setText("\t\t" + productList.get(position));
				}
				
				holder.prgsbarSkill.incrementProgressBy(50);
				holder.txtPercent.setText(holder.prgsbarSkill.getProgress() + "%");
				
				convertView.setTag(holder);
			}
			else {
				Log.d("myapp", "KKK");
				holder = (ViewHolder) convertView.getTag();
			}
			
			Log.d("myapp", "KKKK");
			return convertView;
		}
	}
}
