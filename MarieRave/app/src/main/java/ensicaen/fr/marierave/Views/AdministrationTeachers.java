package ensicaen.fr.marierave.Views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import ensicaen.fr.marierave.Controllers.TeacherDAO;
import ensicaen.fr.marierave.Model.Teacher;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;
import ensicaen.fr.marierave.Views.Dialogs.EditTeacherDialog;
import ensicaen.fr.marierave.Views.Dialogs.NewTeacherDialog;

public class AdministrationTeachers extends Fragment
{
	private ListView _teacherListview;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.administration_teachers, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		ImageButton btnBack = view.findViewById(R.id.backButton);
		btnBack.setOnClickListener(new View.OnClickListener()
		{
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
				android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
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
		
		_teacherListview = view.findViewById(R.id.listview1);
		reloadTeachersListview();

		ImageButton btnNewTeacher = view.findViewById(R.id.button21);
		btnNewTeacher.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				NewTeacherDialog dialog = new NewTeacherDialog();
				FragmentManager fm = getActivity().getSupportFragmentManager();
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.show(fm, "newTeacher");
			}
		});
		
		final List<Teacher> teachers = new TeacherDAO(getContext()).getAllTeachers();
		
		EditText editText = view.findViewById(R.id.search_bar4);
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
									teachers.clear();
									for (Teacher teacher : new TeacherDAO(getContext()).getAllTeachers()) {
										if (teacher.getName().toUpperCase().startsWith(editable.toString().toUpperCase())) {
											teachers.add(teacher);
										}
									}
									
									ListViewAdapter gridViewAdapter = new ListViewAdapter(getActivity(), teachers);
									_teacherListview.setAdapter(gridViewAdapter);
									gridViewAdapter.notifyDataSetChanged();
								}
							});
						}
					}
				}, DELAY);
			}
		});
	}
	
	public void reloadTeachersListview()
	{
		ListViewAdapter adapter = new ListViewAdapter(getActivity(), new TeacherDAO(getContext()).getAllTeachers());
		_teacherListview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	private class ListViewAdapter extends BaseAdapter
	{
		private List<Teacher> _teacherList;
		private FragmentActivity _activity;
		
		ListViewAdapter(FragmentActivity activity, List<Teacher> teacherList)
		{
			super();
			_activity = activity;
			_teacherList = teacherList;
			
			Collections.sort(_teacherList, new Comparator<Teacher>()
			{
				@Override
				public int compare(Teacher o1, Teacher o2)
				{
					int value1 = o1.getName().compareToIgnoreCase(o2.getName());
					
					if (value1 == 0) {
						return o1.getFirstname().compareToIgnoreCase(o2.getFirstname());
					}
					
					return value1;
				}
			});
		}
		
		@Override
		public int getCount()
		{
			return _teacherList.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return _teacherList.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		void removeItem(Teacher teacher)
		{
			_teacherList.remove(teacher);
			notifyDataSetChanged();
		}
		
		private class ViewHolder
		{
			private TextView _name;
			private TextView _surname;
			private TextView _idConnection;
			private TextView _password;
			private ImageButton _btnEdit;
			private ImageButton _btnDelete;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder;
			
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.listview_admin_teachers_item, null);
				
				holder = new ViewHolder();
				holder._name = convertView.findViewById(R.id.textView22);
				holder._surname = convertView.findViewById(R.id.textView21);
				holder._idConnection = convertView.findViewById(R.id.textView20);
				holder._password = convertView.findViewById(R.id.textView23);
				holder._btnEdit = convertView.findViewById(R.id.button);
				holder._btnDelete = convertView.findViewById(R.id.button20);
				
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			final Teacher teacher = _teacherList.get(position);
			
			holder._btnEdit.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Bundle bundle = new Bundle();
					bundle.putInt("teacherId", teacher.getId());
					
					FragmentManager fm = getActivity().getSupportFragmentManager();
					DialogFragment dialog = new EditTeacherDialog();
					dialog.setArguments(bundle);
					dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
					dialog.show(fm, "editTeacher");
				}
			});
			
			holder._btnDelete.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
					builder.setMessage("Etes-vous sûr de vouloir supprimer cet enseignant de l'école ? Cette action est irréversible");
					builder.setCancelable(true);
					builder.setPositiveButton("Oui", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int id)
						{
							new TeacherDAO(_activity).deleteTeacher(_teacherList.get(position).getId());
							removeItem(teacher);
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
			
			holder._name.setText(_teacherList.get(position).getName());
			holder._surname.setText(_teacherList.get(position).getFirstname());
			holder._idConnection.setText(_teacherList.get(position).getIdConnection());
			holder._password.setText(_teacherList.get(position).getPassword());
			
			if (position % 2 == 0) {
				convertView.setBackgroundResource(R.color.listPaircolor);
			}
			else {
				convertView.setBackgroundColor(0x00000000);
			}
			
			return convertView;
		}
	}
}