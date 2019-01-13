package ensicaen.fr.marierave.Views;

import android.app.Activity;
import android.content.DialogInterface;
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

import java.util.List;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;
import ensicaen.fr.marierave.Views.Dialogs.NewChildDialog;

public class AdministrationChilds extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.administration_childs, container, false);
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
		
		List<Child> childList = new ChildDAO(getContext()).getAllChilds();
		
		ListViewAdapter adapter = new ListViewAdapter(getActivity(), childList);
		final ListView listview = view.findViewById(R.id.listview1);
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		Button btnNewChild = view.findViewById(R.id.button2);
		btnNewChild.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				NewChildDialog dialog = new NewChildDialog(getActivity());
				dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(final DialogInterface arg0) {
						List<Child> childs = new ChildDAO(getContext()).getAllChilds();
						ListViewAdapter adapter = new ListViewAdapter(getActivity(), childs);
						listview.setAdapter(adapter);
					}
				});
				
				dialog.show();
			}
		});
	}
	
	private class ListViewAdapter extends BaseAdapter
	{
		private List<Child> _childList;
		private Activity _activity;
		
		ListViewAdapter(Activity activity, List<Child> classList)
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
				
				holder._class.setText(_childList.get(position).getClassroom());
				holder._name.setText(_childList.get(position).getName());
				holder._surname.setText(_childList.get(position).getFirstname());
			}
			
			return convertView;
		}
	}
}
