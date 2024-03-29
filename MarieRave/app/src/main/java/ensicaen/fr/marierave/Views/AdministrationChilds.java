package ensicaen.fr.marierave.Views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;
import ensicaen.fr.marierave.Views.Dialogs.ImportFileDialog;
import ensicaen.fr.marierave.Views.Dialogs.NewOrUpdateChildDialog;

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
		
		ImageView btnLogOff = view.findViewById(R.id.imageView2);
		btnLogOff.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				builder.setMessage("Etes-vous sûr de vouloir vous déconnecter ?");
				builder.setCancelable(true);
				builder.setPositiveButton("Oui", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						Utils.replaceFragments(ConnectionFragment.class, getActivity(), null, false);
						
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
		
		listview = view.findViewById(R.id.listview1);
		reloadChildtListView();
		
		ImageButton btnNewChild = view.findViewById(R.id.button2);
		btnNewChild.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				FragmentManager fm = getActivity().getSupportFragmentManager();
				Bundle bundle = new Bundle();
				bundle.putString("mode", "new");
				
				NewOrUpdateChildDialog dialog = new NewOrUpdateChildDialog();
				dialog.setArguments(bundle);
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
		
		final List<Child> childList = new ChildDAO(getContext()).getAllChilds();
		
		EditText editText = view.findViewById(R.id.search_bar);
		editText.addTextChangedListener(new TextWatcher()
		{
			private Timer timer = new Timer();
			private final long DELAY = 500;
			
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			
			@Override
			public void afterTextChanged(final Editable editable)
			{
				timer.cancel();
				timer = new Timer();
				timer.schedule(new TimerTask()
				{
					@Override
					public void run()
					{
						if (getActivity() != null) {
							getActivity().runOnUiThread(new Runnable()
							{
								@Override
								public void run()
								{
									childList.clear();
									for (Child c : new ChildDAO(getContext()).getAllChilds()) {
										if (c.getName().toUpperCase().startsWith(editable.toString().toUpperCase())) {
											childList.add(c);
										}
									}
									
									ListViewAdapter gridViewAdapter = new ListViewAdapter(getActivity(), childList);
									listview.setAdapter(gridViewAdapter);
									gridViewAdapter.notifyDataSetChanged();
								}
							});
						}
					}
				}, DELAY);
			}
		});
	}
	
	public void reloadChildtListView()
	{
		ListViewAdapter topicsAdapter = new ListViewAdapter(getActivity(), new ChildDAO(getContext()).getAllChilds());
		listview.setAdapter(topicsAdapter);
		topicsAdapter.notifyDataSetChanged();
	}
	
	private class ListViewAdapter extends BaseAdapter
	{
		private List<Child> _childList;
		private FragmentActivity _activity;
		
		ListViewAdapter(FragmentActivity activity, List<Child> childList)
		{
			super();
			_activity = activity;
			_childList = childList;
			
			Collections.sort(_childList, new Comparator<Child>()
			{
				@Override
				public int compare(Child o1, Child o2)
				{
					int value1 = o1.getClassroom().compareToIgnoreCase(o2.getClassroom());
					
					if (value1 == 0) {
						int value2 = o1.getName().compareToIgnoreCase(o2.getName());
						
						if (value2 == 0) {
							return o1.getFirstname().compareTo(o2.getFirstname());
						}
						else {
							return value2;
						}
					}
					
					return value1;
				}
			});
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
			private ImageButton _btnPrint;
			private ImageButton _btnEdit;
			private ImageButton _btnDelete;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder;
			
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.listview_admin_childs_item, null);
				
				holder = new ViewHolder();
				holder._class = convertView.findViewById(R.id.textView);
				holder._name = convertView.findViewById(R.id.textView6);
				holder._surname = convertView.findViewById(R.id.textView8);
				holder._btnConsult = convertView.findViewById(R.id.button12);
				holder._btnEdit = convertView.findViewById(R.id.button13);
				holder._btnPrint = convertView.findViewById(R.id.button14);
				holder._btnDelete = convertView.findViewById(R.id.button17);
				
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			final Child child = _childList.get(position);
			
			holder._btnConsult.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Bundle bundle = new Bundle();
					bundle.putInt("childId", child.getId());
					Utils.replaceFragments(PersonnalProfile.class, getActivity(), bundle, true);
				}
			});
			
			holder._btnPrint.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Bundle bundle = new Bundle();
					bundle.putInt("childId", child.getId());
					
					Utils.replaceFragments(PrintPDF.class, getActivity(), bundle, true);
				}
			});
			
			holder._btnEdit.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					FragmentManager fm = getActivity().getSupportFragmentManager();
					Bundle bundle = new Bundle();
					bundle.putString("mode", "edit");
					bundle.putInt("childId", child.getId());
					
					NewOrUpdateChildDialog dialog = new NewOrUpdateChildDialog();
					dialog.setArguments(bundle);
					dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
					dialog.show(fm, "editChild");
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
			
			if (position % 2 == 0) {
				convertView.setBackgroundResource(R.color.listPaircolor);
			}
			else {
				convertView.setBackgroundColor(Color.TRANSPARENT);
			}
			
			return convertView;
		}
	}
}
