package ensicaen.fr.marierave;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Controllers.ClassroomDAO;
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
import ensicaen.fr.marierave.Views.AdministrationChilds;
import ensicaen.fr.marierave.Views.AdministrationSkills;
import ensicaen.fr.marierave.Views.ConnectionFragment;

public class MainActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.application_layout_fragment_container);

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		File wallpaperDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "ANEC/");
		wallpaperDirectory.mkdirs();
		
		//new DAOBase(this).clearDatabase();

        ClassroomDAO classroomDAO = new ClassroomDAO(this);
        classroomDAO.addClassroom(new Classroom("Ecole")); //!! DO NOT REMOVE UNTIL ARCHITECTURE CHANGE !!\\
		TeacherDAO teacherDAO = new TeacherDAO(this);
		teacherDAO.addTeacher(new Teacher("admin", "admin", "admin", "admin"));

		/*
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
		
		SubjectDAO subjectDAO = new SubjectDAO(this);
		subjectDAO.addSubject(new Subject("FRANCAIS"));
		subjectDAO.addSubject(new Subject("MATHS"));
		subjectDAO.addSubject(new Subject("SPORT"));

        SkillheaderDAO skillheaderDAO = new SkillheaderDAO(this);
		skillheaderDAO.addSkillheader(new Skillheader("Lire et écrire", "FRANCAIS"));
		skillheaderDAO.addSkillheader(new Skillheader("Numération", "MATHS"));
		skillheaderDAO.addSkillheader(new Skillheader("Courir", "SPORT"));
		skillheaderDAO.addSkillheader(new Skillheader("Courir1", "SPORT"));
		skillheaderDAO.addSkillheader(new Skillheader("Courir2", "SPORT"));
		skillheaderDAO.addSkillheader(new Skillheader("Courir3", "SPORT"));
		skillheaderDAO.addSkillheader(new Skillheader("Courir4", "SPORT"));
		skillheaderDAO.addSkillheader(new Skillheader("Courir5", "SPORT"));
		skillheaderDAO.addSkillheader(new Skillheader("Courir6", "SPORT"));
		skillheaderDAO.addSkillheader(new Skillheader("Courir7", "SPORT"));
		skillheaderDAO.addSkillheader(new Skillheader("Courir8", "SPORT"));
		skillheaderDAO.addSkillheader(new Skillheader("Courir9", "SPORT"));
		skillheaderDAO.addSkillheader(new Skillheader("Courir10", "SPORT"));
		skillheaderDAO.addSkillheader(new Skillheader("Courir11", "SPORT"));
		
		
		SkillDAO skillDAO = new SkillDAO(this);
		skillDAO.addSkill(new Skill("FRL1", "Lire des mots", "Lire et écrire"));
		skillDAO.addSkill(new Skill("FRE1", "Recopier un texte court", "Lire et écrire"));
		skillDAO.addSkill(new Skill("FRL10", "Reconnaître les pronoms personnels", "Lire et écrire"));
		skillDAO.addSkill(new Skill("MAN5", "Additionner", "Numération"));
		skillDAO.addSkill(new Skill("SPO1", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO2", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO3", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO4", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO5", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO6", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO7", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO8", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO9", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO10", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO11", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO12", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO13", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO14", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO15", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO16", "SPORT", "Courir"));
		
		skillDAO.addSkill(new Skill("SPO17", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO18", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO19", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO20", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO21", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO22", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO23", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO24", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO25", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO26", "SPORT", "Courir"));
		
		skillDAO.addSkill(new Skill("SPO27", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO28", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO29", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO30", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO31", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO32", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO33", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO34", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO35", "SPORT", "Courir"));
		skillDAO.addSkill(new Skill("SPO36", "SPORT", "Courir"));
*/

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
				
				InputStream is = getContentResolver().openInputStream(data.getData());
				
				CSVFile csvFile = new CSVFile(is);
				
				List<String[]> scoreList = csvFile.read();
				
				boolean isSubject = false;
				boolean isHeader = false;
				
				String subjectName = "";
				String headerName = "";
				
				SubjectDAO subjectDAO = new SubjectDAO(this);
				SkillheaderDAO skillheaderDAO = new SkillheaderDAO(this);
				SkillDAO skillDAO = new SkillDAO(this);
				
				for (int i = 0; i < scoreList.size(); i++) {
					
					String[] row = scoreList.get(i);
					
					if (row[0].isEmpty()) {
						
						if (!isSubject) {
							isSubject = true;
							
							subjectName = row[1];
							
							if (!subjectDAO.subjectExists(subjectName)) {
								subjectDAO.addSubject(new Subject(subjectName));
							}
						}
						else if (!isHeader) {
							isHeader = true;
							
							headerName = row[1];
							
							if (!skillheaderDAO.skillheaderExists(headerName)) {
								skillheaderDAO.addSkillheader(new Skillheader(headerName, subjectName));
							}
						}
					}
					else {
						if (!skillDAO.skillExists(row[0])) {
							skillDAO.addSkill(new Skill(row[0], row[1], headerName));
						}
						
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
		else if (requestCode == 43) {
			try {
				InputStream is = getContentResolver().openInputStream(data.getData());
				
				CSVFile csvFile = new CSVFile(is);
				
				List<String[]> childList = csvFile.read();
				
				ClassroomDAO classroomDAO = new ClassroomDAO(this);
				ChildDAO childDAO = new ChildDAO(this);
				
				for (int i = 1; i < childList.size(); i++) {
					
					String[] row = childList.get(i);
					
					if (!classroomDAO.classroomExists(row[2])) {
						classroomDAO.addClassroom(new Classroom(row[2]));
					}
					
					childDAO.addChild(new Child(row[0], row[1], row[2]));
				}
				
				((AdministrationChilds) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).reloadChildtListView();
				
			} catch (Exception e) {
				e.printStackTrace();
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Une erreur est survenue").setMessage("Aucune donnée n'a été récupérée")
						.setCancelable(false).setPositiveButton("Fermer", null);
				
				AlertDialog alert = builder.create();
				alert.show();
				return;
			}
		}
		else if (requestCode == 1 && resultCode == RESULT_OK) {
			
			
		
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
