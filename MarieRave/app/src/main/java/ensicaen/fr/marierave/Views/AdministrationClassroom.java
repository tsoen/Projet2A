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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Controllers.ClassroomDAO;
import ensicaen.fr.marierave.Controllers.TeacherClassroomDAO;
import ensicaen.fr.marierave.Controllers.TeacherDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.Model.Classroom;
import ensicaen.fr.marierave.Model.Teacher;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Views.Dialogs.AddChildToClassroomDialog;
import ensicaen.fr.marierave.Views.Dialogs.AddTeacherToClassroomDialog;

public class AdministrationClassroom extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.administration_classroom, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		final Classroom classroom = new ClassroomDAO(getContext()).getClassroom(getArguments().getString("classroomName"));
		
		TextView txtClassroomName = view.findViewById(R.id.className);
		txtClassroomName.setText(classroom.getName());
		
		Button btnBack = view.findViewById(R.id.backButton);
		btnBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		List<Child> subjectList = new ChildDAO(getContext()).getAllChildsInClassroom(classroom.getName());
		List<Teacher> teacherList = new ArrayList<>();
		
		List<Integer> teacherIdList = new TeacherClassroomDAO(getActivity()).getTeachersIdInClassroom(classroom.getName());
		for (Integer id : teacherIdList) {
			teacherList.add(new TeacherDAO(getActivity()).getTeacher(id));
		}
		
		GridViewChildsAdapter adapter = new GridViewChildsAdapter(getActivity(), subjectList);
		final GridView gridviewChilds = view.findViewById(R.id.gridviewChilds);
		gridviewChilds.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		GridViewTeachersAdapter adapterTeacherView = new GridViewTeachersAdapter(getActivity(), teacherList);
		final GridView gridviewTeachers = view.findViewById(R.id.teachersList);
		gridviewTeachers.setAdapter(adapterTeacherView);
		adapterTeacherView.notifyDataSetChanged();
		
		Button btnAddTeacher = view.findViewById(R.id.addTeacher);
		btnAddTeacher.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AddTeacherToClassroomDialog dialog = new AddTeacherToClassroomDialog(getActivity(), classroom.getName(), "Add");
				dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
				{
					@Override
					public void onDismiss(final DialogInterface arg0)
					{
						List<Teacher> teacherList = new ArrayList<>();
						List<Integer> teacherIdList = new TeacherClassroomDAO(getActivity()).getTeachersIdInClassroom(classroom.getName());
						for (Integer id : teacherIdList) {
							teacherList.add(new TeacherDAO(getActivity()).getTeacher(id));
						}
						GridViewTeachersAdapter adapterTeacherView = new GridViewTeachersAdapter(getActivity(), teacherList);
						gridviewTeachers.setAdapter(adapterTeacherView);
					}
				});
				
				dialog.show();
			}
		});
		
		Button btnDeleteTeacher = view.findViewById(R.id.removeTeacher);
		btnDeleteTeacher.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AddTeacherToClassroomDialog dialog = new AddTeacherToClassroomDialog(getActivity(), classroom.getName(), "Delete");
				dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
				{
					@Override
					public void onDismiss(final DialogInterface arg0)
					{
						List<Teacher> teacherList = new ArrayList<>();
						List<Integer> teacherIdList = new TeacherClassroomDAO(getActivity()).getTeachersIdInClassroom(classroom.getName());
						for (Integer id : teacherIdList) {
							teacherList.add(new TeacherDAO(getActivity()).getTeacher(id));
						}
						GridViewTeachersAdapter adapterTeacherView = new GridViewTeachersAdapter(getActivity(), teacherList);
						gridviewTeachers.setAdapter(adapterTeacherView);
					}
				});
				
				dialog.show();
			}
		});
		
		
		Button btnAddStudent = view.findViewById(R.id.addStudent);
		btnAddStudent.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AddChildToClassroomDialog dialog = new AddChildToClassroomDialog(getActivity(), classroom.getName(), "Add");
				dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
				{
					@Override
					public void onDismiss(final DialogInterface arg0)
					{
						List<Child> subjectList = new ChildDAO(getContext()).getAllChildsInClassroom(classroom.getName());
						GridViewChildsAdapter adapter = new GridViewChildsAdapter(getActivity(), subjectList);
						gridviewChilds.setAdapter(adapter);
					}
				});
				
				dialog.show();
			}
		});
		
		Button removeStudent = view.findViewById(R.id.removeStudent);
		removeStudent.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AddChildToClassroomDialog dialog = new AddChildToClassroomDialog(getActivity(), classroom.getName(), "Delete");
				dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
				{
					@Override
					public void onDismiss(final DialogInterface arg0)
					{
						List<Child> subjectList = new ChildDAO(getContext()).getAllChildsInClassroom(classroom.getName());
						GridViewChildsAdapter adapter = new GridViewChildsAdapter(getActivity(), subjectList);
						gridviewChilds.setAdapter(adapter);
					}
				});
				
				dialog.show();
			}
		});
	}
	
	private class GridViewChildsAdapter extends BaseAdapter
	{
		private List<Child> _productList;
		private Activity _activity;
		
		GridViewChildsAdapter(Activity activity, List<Child> productList)
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
				GridViewChildsAdapter.ViewHolder holder = new GridViewChildsAdapter.ViewHolder();
				holder._profilePic = convertView.findViewById(R.id.imgProfilePicture);
				holder._txtName = convertView.findViewById(R.id.txtName);
				holder._txtSurname = convertView.findViewById(R.id.txtSurname);
				
				holder._profilePic.setImageResource(R.mipmap.ic_launcher_round);
				holder._txtName.setText(_productList.get(position).getName());
				holder._txtSurname.setText(_productList.get(position).getFirstname());
			}
			
			return convertView;
		}
	}
	
	private class GridViewTeachersAdapter extends BaseAdapter
	{
		private List<Teacher> _teacherList;
		private Activity _activity;
		
		GridViewTeachersAdapter(Activity activity, List<Teacher> teacherList)
		{
			super();
			_activity = activity;
			_teacherList = teacherList;
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
		
		private class ViewHolder
		{
			private ImageView _profilePic;
			private TextView _txtName;
			private TextView _txtFirstname;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.gridview_classroom_item, null);
				ViewHolder holder = new ViewHolder();
				holder._profilePic = convertView.findViewById(R.id.imgProfilePicture);
				holder._txtName = convertView.findViewById(R.id.txtName);
				holder._txtFirstname = convertView.findViewById(R.id.txtSurname);
				
				holder._profilePic.setImageResource(R.mipmap.ic_launcher_round);
				holder._txtName.setText(_teacherList.get(position).getName());
				holder._txtFirstname.setText(_teacherList.get(position).getFirstname());
			}
			
			return convertView;
		}
	}
}
