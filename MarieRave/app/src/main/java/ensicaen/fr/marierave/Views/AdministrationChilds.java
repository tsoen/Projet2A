package ensicaen.fr.marierave.Views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;
import ensicaen.fr.marierave.Views.Dialogs.ImportFileDialog;
import ensicaen.fr.marierave.Views.Dialogs.NewChildDialog;

public class AdministrationChilds extends Fragment
{
	private ListView listview;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.administration_childs, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		ImageButton btnBack = view.findViewById(R.id.backButton);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		listview = view.findViewById(R.id.listview1);
		reloadChildtListView();
		
		ImageButton btnNewChild = view.findViewById(R.id.button2);
		btnNewChild.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				FragmentManager fm = getActivity().getSupportFragmentManager();
				
				NewChildDialog dialog = new NewChildDialog();
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.show(fm, "newChild");
			}
		});
		
		Button btnImport = view.findViewById(R.id.button26);
		btnImport.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				FragmentManager fm = getActivity().getSupportFragmentManager();
				Bundle bundle = new Bundle();
				bundle.putString("mode", "importChilds");
				
				ImportFileDialog dialog = new ImportFileDialog();
				dialog.setArguments(bundle);
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.show(fm, "importChild");
			}
		});
	}
	
	public void reloadChildtListView()
	{
		ListViewAdapter topicsAdapter = new ListViewAdapter(getActivity(), new ChildDAO(getContext()).getAllChilds());
		listview.setAdapter(topicsAdapter);
		topicsAdapter.notifyDataSetChanged();
	}
	
	//@TODO make a real transparent back image
	private class ListViewAdapter extends BaseAdapter
	{
		private List<Child> _childList;
		private FragmentActivity _activity;
		
		ListViewAdapter(FragmentActivity activity, List<Child> classList)
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
		
		void removeItem(Child child)
		{
			_childList.remove(child);
			notifyDataSetChanged();
		}
		
		private class ViewHolder
		{
			private TextView _class;
			private TextView _name;
			private TextView _surname;
			private ImageButton _btnConsult;
			private ImageButton _btnResults;
			private ImageButton _btnPrint;
			private ImageButton _btnDelete;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.listview_admin_childs_item, null);
				ViewHolder holder = new ViewHolder();
				holder._class = convertView.findViewById(R.id.textView);
				holder._name = convertView.findViewById(R.id.textView6);
				holder._surname = convertView.findViewById(R.id.textView8);
				holder._btnConsult = convertView.findViewById(R.id.button12);
				holder._btnResults = convertView.findViewById(R.id.button13);
				holder._btnPrint = convertView.findViewById(R.id.button14);
				holder._btnDelete = convertView.findViewById(R.id.button17);
				
				final Child child = _childList.get(position);
				
				holder._btnConsult.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
						Bundle bundle = new Bundle();
						bundle.putInt("childId", child.getId());
						Utils.replaceFragments(PersonnalProfile.class, getActivity(), bundle, true);
					}
				});
				
				holder._btnResults.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
						Utils.replaceFragments(PersonnalProfileResults.class, _activity, null, true);
					}
				});
				
				holder._btnDelete.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
						builder.setMessage("Etes-vous sûr de vouloir supprimer cet élève de l'école ? Cette action est irréversible");
						builder.setCancelable(true);
						builder.setPositiveButton("Oui", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								new ChildDAO(_activity).deleteChild(_childList.get(position).getId());
								removeItem(child);
								dialog.cancel();
							}
						});
						builder.setNegativeButton("Non", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								dialog.cancel();
							}
						});
						
						builder.create().show();
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
