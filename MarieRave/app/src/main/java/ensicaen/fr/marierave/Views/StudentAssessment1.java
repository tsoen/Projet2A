package ensicaen.fr.marierave.Views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;
import ensicaen.fr.marierave.Views.Dialogs.ChangeAppModePasswordDialog;

public class StudentAssessment1 extends Fragment
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.student_assessment_1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
	
		final EditText editText = view.findViewById(R.id.student_name);
		final GridView gridview = view.findViewById(R.id.gridviewProfiles);
	
		//final Classroom classroom = new ClassroomDAO(getContext()).getClassroom(getArguments().getString("classroomName"));
	
		final List<Child> childList = new ChildDAO(getContext()).getAllChilds();
	
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
										if (c.getFirstname().toUpperCase().startsWith(editable.toString().toUpperCase())) {
											childList.add(c);
										}
									}
									
									GridViewAdapter adapt = new GridViewAdapter(getActivity(), childList);
									gridview.setAdapter(adapt);
									adapt.notifyDataSetChanged();
								}
							});
						}
					}
				}, DELAY);
			}
		});
	
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id)
			{
				Bundle bundle = new Bundle();
				bundle.putInt("Id", childList.get(position).getId());
				bundle.putString("Prénom", childList.get(position).getFirstname());
				Utils.replaceFragments(StudentAssessment2.class, getActivity(), bundle, true);
			}
		});
	
		ImageView openAssessment = view.findViewById(R.id.openAssessment2);
		openAssessment.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ChangeAppModePasswordDialog dialog = new ChangeAppModePasswordDialog();
				dialog.setTargetFragment(getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container), 0);
				dialog.show(getActivity().getSupportFragmentManager(), "newSkill");
			}
		});
    }
	
	private class GridViewAdapter extends BaseAdapter
	{
		private List<Child> _childList;
		private Activity _activity;
		
		GridViewAdapter(Activity activity, List<Child> childList)
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
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.gridview_classroom_guy_item, null);
				ViewHolder holder = new ViewHolder();
				holder._profilePic = convertView.findViewById(R.id.imgProfilePicture);
				holder._txtName = convertView.findViewById(R.id.txtName);
				holder._txtSurname = convertView.findViewById(R.id.txtSurname);
				
				holder._profilePic.setImageResource(R.mipmap.ic_launcher_round);
				holder._txtName.setText(_childList.get(position).getName());
				holder._txtSurname.setText(_childList.get(position).getFirstname());
				
			}
			
			return convertView;
		}
	}
}
