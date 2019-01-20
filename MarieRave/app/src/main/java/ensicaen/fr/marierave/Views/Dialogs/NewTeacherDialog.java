package ensicaen.fr.marierave.Views.Dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import ensicaen.fr.marierave.Controllers.TeacherDAO;
import ensicaen.fr.marierave.Model.Teacher;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Views.AdministrationTeachers;

public class NewTeacherDialog extends DialogFragment implements android.view.View.OnClickListener
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.dialog_new_teacher, container);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setCancelable(false);
		
		Button btnValidate = view.findViewById(R.id.btn_validate);
		btnValidate.setOnClickListener(this);
		
		Button btnCancel = view.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.btn_validate:
				EditText editTeacherName = getDialog().findViewById(R.id.edit_teacherName);
				EditText editTeacherFirstname = getDialog().findViewById(R.id.edit_teacherFirstname);
				
				TeacherDAO teacherDAO = new TeacherDAO(getContext());
				teacherDAO.addTeacher(new Teacher(editTeacherName.getText().toString(), editTeacherFirstname.getText().toString()));
				
				Teacher teacher = teacherDAO.getTeacher(teacherDAO.getLastId());
				
				((AdministrationTeachers) getTargetFragment()).reloadTeachersListview();
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				builder.setMessage("L'enseignant a bien été créé. N'oubliez pas de lui communiquer ses identifiants : \n" + "Identifiant : " + teacher
						.getIdConnection() + "\n" + "Mot de passe : " + teacher.getPassword()).setCancelable(false)
						.setPositiveButton("Ok", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								dismiss();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
				
				dismiss();
				break;
			
			case R.id.btn_cancel:
				dismiss();
				break;
			default:
				dismiss();
				break;
		}
	}
}