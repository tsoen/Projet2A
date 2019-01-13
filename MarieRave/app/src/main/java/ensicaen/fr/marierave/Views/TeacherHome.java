package ensicaen.fr.marierave.Views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class TeacherHome extends Fragment
{
    @Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.teacher_home, container, false);
    }

    @Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
        super.onViewCreated(view, savedInstanceState);

        ImageButton btn_exit = view.findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });

        Button btn_admin = view.findViewById(R.id.btn_Admin);
        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Utils.replaceFragments(AdministrationHome.class, getActivity(), bundle, true);
            }
        });
	
		List<Classroom> classrooms = new ClassroomDAO(getContext()).getAllClassrooms();
	
		GridViewAdapter adapter = new GridViewAdapter(getActivity(), classrooms);
		final GridView gridview = view.findViewById(R.id.gridview_classes);
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
			private Button _classButton;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.gridview_list_classes_item, null);
				final ViewHolder holder = new ViewHolder();
				holder._classButton = convertView.findViewById(R.id.button8);
				
				holder._classButton.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Bundle bundle = new Bundle();
						bundle.putString("classroomName", holder._classButton.getText().toString());
						
						Utils.replaceFragments(HomeClassroom.class, getActivity(), bundle, true);
					}
				});
				
				holder._classButton.setText(_classList.get(position).getName());
			}
			
			return convertView;
		}
	}
}