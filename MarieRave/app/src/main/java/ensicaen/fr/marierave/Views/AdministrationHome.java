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
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.List;

import ensicaen.fr.marierave.Controllers.ClassroomDAO;
import ensicaen.fr.marierave.Model.Classroom;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;
import ensicaen.fr.marierave.Views.Dialogs.NewClassroomDialog;

public class AdministrationHome extends Fragment
{
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
		
		List<Classroom> classrooms = new ClassroomDAO(getContext()).getAllClassrooms();
		
		GridViewAdapter adapter = new GridViewAdapter(getActivity(), classrooms);
		final GridView gridview = view.findViewById(R.id.gridview1);
		gridview.setAdapter(adapter);
		
		ImageButton btnNewClassroom = view.findViewById(R.id.button6);
		btnNewClassroom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				NewClassroomDialog dialog = new NewClassroomDialog(getActivity());
				dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(final DialogInterface arg0) {
						List<Classroom> classrooms = new ClassroomDAO(getContext()).getAllClassrooms();
						GridViewAdapter adapter = new GridViewAdapter(getActivity(), classrooms);
						gridview.setAdapter(adapter);
					}
				});
				
				dialog.show();
			}
		});
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
			private Button _classButton;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.gridview_list_classes_item, null);
				final ViewHolder holder = new ViewHolder();
				holder._classButton = convertView.findViewById(R.id.button8);
				
				holder._classButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
						Bundle bundle = new Bundle();
						bundle.putString("classroomName", holder._classButton.getText().toString());
						
						Utils.replaceFragments(AdministrationClassroom.class, getActivity(), bundle, true);
					}
				});
				
				holder._classButton.setText(_classList.get(position).getName());
			}
			
			return convertView;
		}
	}
}
