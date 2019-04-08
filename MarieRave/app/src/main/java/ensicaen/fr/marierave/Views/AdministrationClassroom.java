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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Controllers.TeacherClassroomDAO;
import ensicaen.fr.marierave.Controllers.TeacherDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.Model.Teacher;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;
import ensicaen.fr.marierave.Views.Dialogs.AddChildToClassroomDialog;
import ensicaen.fr.marierave.Views.Dialogs.AddTeacherToClassroomDialog;

public class AdministrationClassroom extends Fragment implements android.view.View.OnClickListener
{
	private GridView _teachersGridview;
	
	private GridView _childsGridview;
	
	private String _classroomName;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.administration_classroom, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		_classroomName = getArguments().getString("classroomName");
		
		TextView txtClassroomName = view.findViewById(R.id.className);
		txtClassroomName.setText(_classroomName);
		
		ImageButton btnBack = view.findViewById(R.id.backButton);
		btnBack.setOnClickListener(this);
		
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
		
		_childsGridview = view.findViewById(R.id.gridviewChilds);
		reloadChildsGridview();
		
		_teachersGridview = view.findViewById(R.id.teachersList);
		reloadTeachersGridview();
		
		ImageButton btnAddTeacher = view.findViewById(R.id.addTeacher);
		btnAddTeacher.setOnClickListener(this);
		
		ImageButton btnDeleteTeacher = view.findViewById(R.id.removeTeacher);
		btnDeleteTeacher.setOnClickListener(this);
		
		ImageButton btnAddChild = view.findViewById(R.id.addStudent);
		btnAddChild.setOnClickListener(this);
		
		ImageButton btnRemoveChild = view.findViewById(R.id.removeStudent);
		btnRemoveChild.setOnClickListener(this);
	}
	
	public void reloadTeachersGridview()
	{
		List<Teacher> teacherList = new ArrayList<>();
		List<Integer> teacherIdList = new TeacherClassroomDAO(getActivity()).getTeachersIdInClassroom(_classroomName);
		TeacherDAO teacherDAO = new TeacherDAO(getActivity());
		for (Integer id : teacherIdList) {
			teacherList.add(teacherDAO.getTeacher(id));
		}
		
		GridViewTeachersAdapter adapterTeacherView = new GridViewTeachersAdapter(getActivity(), teacherList);
		_teachersGridview.setAdapter(adapterTeacherView);
		adapterTeacherView.notifyDataSetChanged();
	}
	
	public void reloadChildsGridview()
	{
		List<Child> subjectList = new ChildDAO(getContext()).getAllChildsInClassroom(_classroomName);
		GridViewChildsAdapter adapter = new GridViewChildsAdapter(getActivity(), subjectList);
		_childsGridview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onClick(View v)
	{
		Bundle bundle = new Bundle();
		DialogFragment dialog;
		FragmentManager fm = getActivity().getSupportFragmentManager();
		
		switch (v.getId()) {
			case R.id.backButton:
				getActivity().getSupportFragmentManager().popBackStack();
				break;
			
			case R.id.addTeacher:
				bundle.putString("classroomName", _classroomName);
				bundle.putString("mode", "Add");
				
				dialog = new AddTeacherToClassroomDialog();
				dialog.setArguments(bundle);
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.show(fm, "addTeacher");
				break;
			
			case R.id.removeTeacher:
				bundle.putString("classroomName", _classroomName);
				bundle.putString("mode", "Delete");
				
				dialog = new AddTeacherToClassroomDialog();
				dialog.setArguments(bundle);
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.show(fm, "deleteTeacher");
				break;
			
			case R.id.addStudent:
				bundle.putString("classroomName", _classroomName);
				bundle.putString("mode", "Add");
				
				dialog = new AddChildToClassroomDialog();
				dialog.setArguments(bundle);
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.show(fm, "addChild");
				break;
			
			case R.id.removeStudent:
				bundle.putString("classroomName", _classroomName);
				bundle.putString("mode", "Delete");
				
				dialog = new AddChildToClassroomDialog();
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.setArguments(bundle);
				dialog.show(fm, "deleteChild");
				break;
			
			default:
				break;
		}
	}
	
	private class GridViewChildsAdapter extends BaseAdapter
	{
        private List<Child> _childList;
		private Activity _activity;

		GridViewChildsAdapter(Activity activity, List<Child> childList)
		{
			super();
			_activity = activity;
			_childList = childList;
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
			private ImageView _profilePic;
			private TextView _txtName;
			private TextView _txtSurname;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder;
			
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.gridview_classroom_guy_item, null);
				
				holder = new ViewHolder();
				holder._profilePic = convertView.findViewById(R.id.imgProfilePicture);
				holder._txtName = convertView.findViewById(R.id.txtName);
				holder._txtSurname = convertView.findViewById(R.id.txtSurname);
				
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if (Utils.getChildPersonnalPicture(getContext(), _childList.get(position).getId()) != null) {
				Picasso.get().load(new File(Utils.getChildPersonnalPicturePath(getContext(), _childList.get(position).getId()))).into(holder._profilePic);
			}
			else {
				Picasso.get().load(R.drawable.garcon_icon).into(holder._profilePic);
			}
			
			holder._txtName.setText(_childList.get(position).getName());
			holder._txtSurname.setText(_childList.get(position).getFirstname());
			
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
				convertView = _activity.getLayoutInflater().inflate(R.layout.gridview_classroom_guy_item, null);
				ViewHolder holder = new ViewHolder();
				holder._profilePic = convertView.findViewById(R.id.imgProfilePicture);
				holder._txtName = convertView.findViewById(R.id.txtName);
				holder._txtFirstname = convertView.findViewById(R.id.txtSurname);

				holder._profilePic.setImageResource(R.drawable.garcon_icon);
				holder._txtName.setText(_teacherList.get(position).getName());
				holder._txtFirstname.setText(_teacherList.get(position).getFirstname());
			}
			
			return convertView;
		}
	}
}
