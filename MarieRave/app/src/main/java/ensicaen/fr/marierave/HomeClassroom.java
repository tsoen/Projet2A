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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeClassroom extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.welcome_classe, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		
		ArrayList<Model2> subjectList = new ArrayList<>();
		subjectList.add(new Model2("", "Rey", "Raphael"));
		subjectList.add(new Model2("", "Soen", "Timothee"));
		subjectList.add(new Model2("", "Rey", "Raphael"));
		subjectList.add(new Model2("", "Soen", "Timothee"));
		subjectList.add(new Model2("", "Rey", "Raphael"));
		subjectList.add(new Model2("", "Soen", "Timothee"));
		subjectList.add(new Model2("", "Rey", "Raphael"));
		subjectList.add(new Model2("", "Soen", "Timothee"));
		
		GridView v = view.findViewById(R.id.gridviewProfiles);
		GridViewAdapter a = new GridViewAdapter(getActivity(), subjectList);
		v.setAdapter(a);
		
		a.notifyDataSetChanged();
		
		v.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id)
			{
				Utils.replaceFragments(PersonnalProfile.class, getActivity(), null, true);
			}
		});
		
	}
	
	public class GridViewAdapter extends BaseAdapter
	{
		private ArrayList<Model2> productList;
		private Activity activity;
		
		public GridViewAdapter(Activity activity, ArrayList<Model2> productList)
		{
			super();
			this.activity = activity;
			this.productList = productList;
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
			ImageView profilePic;
			TextView txtName;
			TextView txtSurname;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			
			ViewHolder holder;
			LayoutInflater inflater = activity.getLayoutInflater();
			
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.gridview_item, null);
				holder = new ViewHolder();
				holder.profilePic = convertView.findViewById(R.id.imageView);
				holder.txtName = convertView.findViewById(R.id.textView7);
				holder.txtSurname = convertView.findViewById(R.id.textView8);
				
				holder.profilePic.setImageResource(R.mipmap.ic_launcher_round);
				holder.txtName.setText(productList.get(position).getName());
				holder.txtSurname.setText(productList.get(position).getSurname());
				
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			return convertView;
		}
	}
}
