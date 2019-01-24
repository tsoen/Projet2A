package ensicaen.fr.marierave.Views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import ensicaen.fr.marierave.Controllers.ClassroomDAO;
import ensicaen.fr.marierave.Model.Classroom;
import ensicaen.fr.marierave.R;

public class ClassAssesment extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.class_assesment, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		final Classroom classroom = new ClassroomDAO(getContext()).getClassroom(getArguments().getString("classroomName"));
		
		TextView txtClassroomName = view.findViewById(R.id.classname);
		txtClassroomName.setText(classroom.getName());
		
		ImageButton btnBack = view.findViewById(R.id.backButton);
		btnBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
	}
}
