package ensicaen.fr.marierave;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.InputStream;
import java.util.List;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Controllers.ClassroomDAO;
import ensicaen.fr.marierave.Controllers.DAOBase;
import ensicaen.fr.marierave.Controllers.SkillDAO;
import ensicaen.fr.marierave.Controllers.SkillheaderDAO;
import ensicaen.fr.marierave.Controllers.SubjectDAO;
import ensicaen.fr.marierave.Controllers.TeacherDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.Model.Classroom;
import ensicaen.fr.marierave.Model.Skill;
import ensicaen.fr.marierave.Model.Skillheader;
import ensicaen.fr.marierave.Model.Subject;
import ensicaen.fr.marierave.Model.Teacher;
import ensicaen.fr.marierave.Views.AdministrationSkills;
import ensicaen.fr.marierave.Views.ConnectionFragment;

public class MainActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.application_layout_fragment_container);

        new DAOBase(this).clearDatabase();

        ClassroomDAO classroomDAO = new ClassroomDAO(this);

        classroomDAO.addClassroom(new Classroom("Ecole")); //!! DO NOT REMOVE UNTIL ARCHITECTURE CHANGE !!\\

        classroomDAO.addClassroom(new Classroom("CP"));
		classroomDAO.addClassroom(new Classroom("CE1"));

        ChildDAO childDAO = new ChildDAO(this);
		childDAO.addChild(new Child("Soen", "Timothee", "CP"));
		childDAO.addChild(new Child("Rey", "Raphael", "CE1"));
		childDAO.addChild(new Child("Morretton", "Julie", "CP"));
        childDAO.addChild(new Child("Morretton", "Julie", "CP"));
        childDAO.addChild(new Child("Morretton", "Julie", "CP"));
        childDAO.addChild(new Child("Morretton", "Julie", "CP"));
        childDAO.addChild(new Child("Morretton", "Julie", "CP"));
        childDAO.addChild(new Child("Rey", "Raphael", "CP"));


		TeacherDAO teacherDAO = new TeacherDAO(this);
		teacherDAO.addTeacher(new Teacher("Lhote", "Loick", "lolo", "plolo"));
		teacherDAO.addTeacher(new Teacher("Simon", "Loick", "sisi", "psisi"));
		teacherDAO.addTeacher(new Teacher("admin", "admin", "admin", "admin"));

        /*SubjectDAO subjectDAO = new SubjectDAO(this);
		subjectDAO.addSubject(new Subject("FRANCAIS"));
		subjectDAO.addSubject(new Subject("MATHS"));
		subjectDAO.addSubject(new Subject("SPORT"));

        SkillheaderDAO skillheaderDAO = new SkillheaderDAO(this);
		skillheaderDAO.addSkillheader(new Skillheader("Lire et écrire", "FRANCAIS"));
		skillheaderDAO.addSkillheader(new Skillheader("Numération", "MATHS"));
		skillheaderDAO.addSkillheader(new Skillheader("Courir", "SPORT"));

        SkillDAO skillDAO = new SkillDAO(this);
		skillDAO.addSkill(new Skill("FRL1", "Lire des mots", "Lire et écrire"));
		skillDAO.addSkill(new Skill("FRE1", "Recopier un texte court", "Lire et écrire"));
		skillDAO.addSkill(new Skill("FRL10", "Reconnaître les pronoms personnels", "Lire et écrire"));
		skillDAO.addSkill(new Skill("MAN5", "Additionner", "Numération"));*/

        if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState == null) {
				Utils.replaceFragments(ConnectionFragment.class, this, null, false);
			}
		}
    }

	@Override
	public void onBackPressed() { }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 42) {
			try {
				Log.d("myapp", data.getData().toString());
				
				InputStream is = getContentResolver().openInputStream(data.getData());
				
				CSVFile csvFile = new CSVFile(is);
				
				List<String[]> scoreList = csvFile.read();
				
				boolean isSubject = false;
				boolean isHeader = false;
				
				String subjectName = "";
				String headerName = "";
				
				for (int i = 0; i < scoreList.size(); i++) {
					
					String[] row = scoreList.get(i);
					
					if (row[0].isEmpty()) {
						
						if (!isSubject) {
							isSubject = true;
							
							subjectName = row[1];
							
							new SubjectDAO(this).addSubject(new Subject(subjectName));
						}
						else if (!isHeader) {
							isHeader = true;
							
							headerName = row[1];
							
							new SkillheaderDAO(this).addSkillheader(new Skillheader(headerName, subjectName));
						}
					}
					else {
						new SkillDAO(this).addSkill(new Skill(row[0], row[1], headerName));
						
						isSubject = false;
						isHeader = false;
					}
				}
				
				((AdministrationSkills) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).reloadSkillListView(null);
				((AdministrationSkills) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).reloadSubjectListView();
				
			} catch (Exception e) {
				e.printStackTrace();
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Une erreur est survenue").setMessage("Aucune donnée n'a été récupérée")
						.setCancelable(false).setPositiveButton("Fermer", null);
				
				AlertDialog alert = builder.create();
				alert.show();
				return;
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
