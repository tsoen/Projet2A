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

import java.util.List;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Controllers.ClassroomDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.Model.Classroom;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Views.Dialogs.AddChildToClassroomDIalog;

public class ClassAdministration extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.class_administration, container, false);
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
		
		GridViewAdapter adapter = new GridViewAdapter(getActivity(), subjectList);
		final GridView gridview = view.findViewById(R.id.gridviewChilds);
		gridview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		Button btnAddStudent = view.findViewById(R.id.addStudent);
		btnAddStudent.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AddChildToClassroomDIalog dialog = new AddChildToClassroomDIalog(getActivity(), classroom.getName());
				dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
				{
					@Override
					public void onDismiss(final DialogInterface arg0)
					{
						List<Child> subjectList = new ChildDAO(getContext()).getAllChildsInClassroom(classroom.getName());
						GridViewAdapter adapter = new GridViewAdapter(getActivity(), subjectList);
						gridview.setAdapter(adapter);
					}
				});
				
				dialog.show();
			}
		});
	}
	
	private class GridViewAdapter extends BaseAdapter
	{
		private List<Child> _productList;
		private Activity _activity;
		
		GridViewAdapter(Activity activity, List<Child> productList)
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
				GridViewAdapter.ViewHolder holder = new GridViewAdapter.ViewHolder();
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
}
