package ensicaen.fr.marierave;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Model.Enfant;

public class AdministrationResults extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.administration_results, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		Button btnBack = view.findViewById(R.id.button1);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		List<Enfant> childList = new ArrayList<>();
		childList.add(new Enfant("", "Rey", "Raphael", "CE1"));
		childList.add(new Enfant("", "Soen", "Timothee", "CM2"));
		childList.add(new Enfant("", "Rey", "Raphael", "CE1"));
		childList.add(new Enfant("", "Soen", "Timothee", "CM2"));
		childList.add(new Enfant("", "Rey", "Raphael", "CE1"));
		childList.add(new Enfant("", "Soen", "Timothee", "CM2"));
		childList.add(new Enfant("", "Rey", "Raphael", "CE1"));
		childList.add(new Enfant("", "Soen", "Timothee", "CM2"));
		childList.add(new Enfant("", "Rey", "Raphael", "CE1"));
		childList.add(new Enfant("", "Soen", "Timothee", "CM2"));
		
		
		
		ListViewAdapter adapter = new ListViewAdapter(getActivity(), childList);
		ListView gridview = view.findViewById(R.id.listview1);
		gridview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	private class ListViewAdapter extends BaseAdapter
	{
		private List<Enfant> _childList;
		private Activity _activity;
		
		ListViewAdapter(Activity activity, List<Enfant> classList)
		{
			super();
			_activity = activity;
			_childList = classList;
		}
		
		@Override
		public int getCount()
		{
			return _childList.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return _childList.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		private class ViewHolder
		{
			private TextView _class;
			private TextView _name;
			private TextView _surname;
			private Button _btnConsult;
			private Button _btnResults;
			private Button _btnPrint;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.listview_class_results, null);
				ViewHolder holder = new ViewHolder();
				
				holder._class = convertView.findViewById(R.id.textView);
				holder._name = convertView.findViewById(R.id.textView6);
				holder._surname = convertView.findViewById(R.id.textView8);
				holder._btnConsult = convertView.findViewById(R.id.button12);
				holder._btnResults = convertView.findViewById(R.id.button13);
				holder._btnPrint = convertView.findViewById(R.id.button14);
				
				holder._btnConsult.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
						Utils.replaceFragments(PersonnalProfile.class, getActivity(), null, true);
					}
				});
				
				holder._btnResults.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
						Utils.replaceFragments(PersonnalProfileResults.class, getActivity(), null, true);
					}
				});
				
				holder._class.setText(_childList.get(position).getClassR());
				holder._name.setText(_childList.get(position).getName());
				holder._surname.setText(_childList.get(position).getSurname());
			}
			
			return convertView;
		}
	}
}
