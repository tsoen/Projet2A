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
		return inflater.inflate(R.layout.home_classroom, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		ArrayList<Enfant> subjectList = new ArrayList<>();
		subjectList.add(new Enfant("", "Rey", "Raphael"));
		subjectList.add(new Enfant("", "Soen", "Timothee"));
		subjectList.add(new Enfant("", "Rey", "Raphael"));
		subjectList.add(new Enfant("", "Soen", "Timothee"));
		subjectList.add(new Enfant("", "Rey", "Raphael"));
		subjectList.add(new Enfant("", "Soen", "Timothee"));
		subjectList.add(new Enfant("", "Rey", "Raphael"));
		subjectList.add(new Enfant("", "Soen", "Timothee"));
		
		GridViewAdapter adapter = new GridViewAdapter(getActivity(), subjectList);
		GridView gridview = view.findViewById(R.id.gridviewProfiles);
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
		private ArrayList<Enfant> _productList;
		private Activity _activity;
		
		GridViewAdapter(Activity activity, ArrayList<Enfant> productList)
		{
			super();
			_activity = activity;
			_productList = productList;
		}
		
		@Override
		public int getCount()
		{
			return _productList.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return _productList.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		private class ViewHolder
		{
			private ImageView _profilePic;
			private TextView _txtName;
			private TextView _txtSurname;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.gridview_classroom_item, null);
				ViewHolder holder = new ViewHolder();
				holder._profilePic = convertView.findViewById(R.id.imgProfilePicture);
				holder._txtName = convertView.findViewById(R.id.txtName);
				holder._txtSurname = convertView.findViewById(R.id.txtSurname);
				
				holder._profilePic.setImageResource(R.mipmap.ic_launcher_round);
				holder._txtName.setText(_productList.get(position).getName());
				holder._txtSurname.setText(_productList.get(position).getSurname());
			}
			
			return convertView;
		}
	}
}
