package ensicaen.fr.marierave.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Controllers.ClassroomDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.Model.Classroom;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;

public class HomeClassroom extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.classroom_home, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		final Classroom classroom = new ClassroomDAO(getContext()).getClassroom(getArguments().getString("classroomName"));
		
		TextView txtClassroomName = view.findViewById(R.id.txtClassroomName);
		txtClassroomName.setText(classroom.getName());
		
		final List<Child> childsInClassroom = new ChildDAO(getContext()).getAllChildsInClassroom(classroom.getName());
		
		GridViewAdapter adapter = new GridViewAdapter(getActivity(), childsInClassroom);
		GridView gridview = view.findViewById(R.id.gridviewProfiles);
		gridview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id)
			{
				Bundle bundle = new Bundle();
				bundle.putInt("childId", childsInClassroom.get(position).getId());
				Utils.replaceFragments(PersonnalProfile.class, getActivity(), bundle, true);
			}
		});
		
		Button btnClassAssesment = view.findViewById(R.id.btnResults);
		btnClassAssesment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Bundle bundle = new Bundle();
				bundle.putString("classroomName", classroom.getName());
				Utils.replaceFragments(ClassAssesment.class, getActivity(), bundle, true);
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
		
		Button openAssessment = view.findViewById(R.id.openAssessment3);
		openAssessment.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Bundle bundle = new Bundle();
				Utils.replaceFragments(StudentAssessment1.class, getActivity(), bundle, true);
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
				convertView = _activity.getLayoutInflater().inflate(R.layout.gridview_classroom_guy_item, null);
				ViewHolder holder = new ViewHolder();
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
