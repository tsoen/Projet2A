package ensicaen.fr.marierave.Views.Dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import ensicaen.fr.marierave.Controllers.TeacherDAO;
import ensicaen.fr.marierave.Model.Teacher;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Views.AdministrationTeachers;

public class EditTeacherDialog extends DialogFragment implements android.view.View.OnClickListener
{
	private Teacher _teacher;
	private EditText edit_teacherName;
	private EditText edit_teacherFirstName;
	private EditText edit_teacherId;
	private EditText edit_teacherPassword;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.dialog_edit_teacher, container);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		
		Integer teacherId = getArguments().getInt("teacherId");
		
		_teacher = new TeacherDAO(getContext()).getTeacher(teacherId);
		
		edit_teacherName = view.findViewById(R.id.editText2);
		edit_teacherName.setText(_teacher.getName());
		
		edit_teacherFirstName = view.findViewById(R.id.editText3);
		edit_teacherFirstName.setText(_teacher.getFirstname());
		
		edit_teacherId = view.findViewById(R.id.editText4);
		edit_teacherId.setText(_teacher.getIdConnection());
		
		edit_teacherPassword = view.findViewById(R.id.editText5);
		edit_teacherPassword.setText(_teacher.getPassword());
		
		Button btnValidate = view.findViewById(R.id.button19);
		btnValidate.setOnClickListener(this);
		
		Button btnCancel = view.findViewById(R.id.button22);
		btnCancel.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.button19:
				
				Teacher teacher = new Teacher(_teacher.getId(), edit_teacherName.getText().toString(), edit_teacherFirstName.getText().toString(), edit_teacherId
						.getText().toString(), edit_teacherPassword.getText().toString());
				
				new TeacherDAO(getContext()).updateTeacher(teacher);
				
				((AdministrationTeachers) getTargetFragment()).reloadTeachersListview();
				
				dismiss();
				break;
			
			case R.id.button22:
				dismiss();
				break;
			default:
				dismiss();
				break;
		}
	}
}

