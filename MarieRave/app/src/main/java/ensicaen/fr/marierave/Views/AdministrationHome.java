package ensicaen.fr.marierave.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ensicaen.fr.marierave.Controllers.ClassroomDAO;
import ensicaen.fr.marierave.Model.Classroom;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;
import ensicaen.fr.marierave.Views.Dialogs.DeleteClassroomDialog;
import ensicaen.fr.marierave.Views.Dialogs.NewClassroomDialog;

public class AdministrationHome extends Fragment
{
	private GridView gridview;
	
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
		
		Button btnChilds = view.findViewById(R.id.button5);
		btnChilds.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Utils.replaceFragments(AdministrationChilds.class, getActivity(), null, true);
			}
		});
		
		Button btnTeachers = view.findViewById(R.id.button3);
		btnTeachers.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Utils.replaceFragments(AdministrationTeachers.class, getActivity(), null, true);
			}
		});
		
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
		
		List<Classroom> classrooms = new ClassroomDAO(getContext()).getAllClassrooms();
		
		GridViewAdapter adapter = new GridViewAdapter(getActivity(), classrooms);
		gridview = view.findViewById(R.id.gridview1);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v, int i, long l)
			{
				
				Bundle bundle = new Bundle();
				TextView className = v.findViewById(R.id.button8);
				bundle.putString("classroomName", className.getText().toString());
				
				Utils.replaceFragments(AdministrationClassroom.class, getActivity(), bundle, true);
			}
		});
		
		ImageButton btnNewClassroom = view.findViewById(R.id.button6);
		btnNewClassroom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				NewClassroomDialog dialog = new NewClassroomDialog();
				
				FragmentManager fm = getActivity().getSupportFragmentManager();
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.show(fm, "newClassroom");
			}
		});
		
		ImageButton btnDeleteClassroom = view.findViewById(R.id.button7);
		btnDeleteClassroom.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DialogFragment dialog = new DeleteClassroomDialog();
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.show(fm, "deleteTeacher");
			}
		});
	}
	
	public void reloadClassroomListview()
	{
		List<Classroom> classrooms = new ClassroomDAO(getContext()).getAllClassrooms();
		GridViewAdapter adapter = new GridViewAdapter(getActivity(), classrooms);
		gridview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	private class GridViewAdapter extends BaseAdapter
	{
		private List<Classroom> _classList;
		private Activity _activity;
		
		GridViewAdapter(Activity activity, List<Classroom> classList)
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
			private TextView _classButton;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.gridview_list_classes_item, null);
				final ViewHolder holder = new ViewHolder();
				holder._classButton = convertView.findViewById(R.id.button8);
				holder._classButton.setText(_classList.get(position).getName());
			}
			
			return convertView;
		}
	}
}
