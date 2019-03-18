package ensicaen.fr.marierave.Views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Controllers.ClassroomDAO;
import ensicaen.fr.marierave.Controllers.TeacherClassroomDAO;
import ensicaen.fr.marierave.Model.Classroom;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;
import ensicaen.fr.marierave.Views.Dialogs.TeacherTakesOrQuitsClassroomDialog;

public class TeacherHome extends Fragment
{
	private GridView gridview;

    @Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.teacher_home, container, false);
    }

    @Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
        super.onViewCreated(view, savedInstanceState);

		Button btn_admin = view.findViewById(R.id.btn_Admin);
		if (!Utils.teacherLoggedInLogin.equals("admin")) {
			btn_admin.setVisibility(View.INVISIBLE);
		}
		else {
			btn_admin.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Utils.replaceFragments(AdministrationHome.class, getActivity(), null, true);
				}
			});
		}
		
		ImageView openAssessment = view.findViewById(R.id.openAssessment);
		openAssessment.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Bundle bundle = new Bundle();
				Utils.replaceFragments(StudentAssessment1.class, getActivity(), bundle, true);
			}
		});

		ImageButton btnAddClass = view.findViewById(R.id.btnAdd_Class);
		btnAddClass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				Bundle bundle = new Bundle();
				bundle.putString("mode", "add");
				
				DialogFragment dialog = new TeacherTakesOrQuitsClassroomDialog();
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.setArguments(bundle);
				dialog.show(fm, "takeClassroom");
			}
		});
		
		ImageButton btnDeleteClass = view.findViewById(R.id.btnDelete_Class);
		btnDeleteClass.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				FragmentManager fm = getActivity().getSupportFragmentManager();
				Bundle bundle = new Bundle();
				bundle.putString("mode", "delete");
				
				DialogFragment dialog = new TeacherTakesOrQuitsClassroomDialog();
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.setArguments(bundle);
				dialog.show(fm, "takeClassroom");
			}
		});

		gridview = view.findViewById(R.id.gridview_classes);
		reloadClassroomListview();

		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				Bundle bundle = new Bundle();
				TextView className = view.findViewById(R.id.button8);
				bundle.putString("classroomName", className.getText().toString());
				Utils.replaceFragments(HomeClassroom.class, getActivity(), bundle, true);
			}
		});
	}

	public void reloadClassroomListview() {
		List<String> classroomNames = new TeacherClassroomDAO(getContext()).getClassroomsWithThisTeacher(Utils.teacherLoggedInId);
		List<Classroom> classrooms = new ArrayList<>();
		ClassroomDAO classroomDAO = new ClassroomDAO(getContext());
		for (String s : classroomNames) {
			classrooms.add(classroomDAO.getClassroom(s));
		}

		GridViewAdapter adapter = new GridViewAdapter(getActivity(), classrooms);
		gridview.setAdapter(adapter);
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
			private TextView _className;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.gridview_list_classes_item, null);
				final ViewHolder holder = new ViewHolder();
				holder._className = convertView.findViewById(R.id.button8);
				holder._className.setText(_classList.get(position).getName());
			}
			
			return convertView;
		}
	}
}
