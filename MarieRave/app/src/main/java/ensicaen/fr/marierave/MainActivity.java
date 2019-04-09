package ensicaen.fr.marierave;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
import ensicaen.fr.marierave.Views.PersonnalProfile;

import static ensicaen.fr.marierave.Utils.ANEC_PATH;

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
		
		File directoryANEC = new File(ANEC_PATH);
		directoryANEC.mkdirs();
		
		try {
			//!! DO NOT REMOVE UNTIL ARCHITECTURE CHANGE !!\\
			ClassroomDAO classroomDAO = new ClassroomDAO(this);
			classroomDAO.addClassroom(new Classroom("Ecole"));
			
			TeacherDAO teacherDAO = new TeacherDAO(this);
			teacherDAO.addTeacher(new Teacher("admin", "admin", "admin", "admin"));
		} catch (Exception e) {
			e.printStackTrace();
		}

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
		else if (requestCode == 1) {
			((PersonnalProfile) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).reloadPicture();
		}
		else if (requestCode == 50) {
			
			if (data == null || data.getData() == null) {
				return;
			}
			
			File dst = new File(Utils.tempFileUri);
			
			try (InputStream in = getContentResolver().openInputStream(data.getData())) {
				try (OutputStream out = new FileOutputStream(dst)) {
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			((PersonnalProfile) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).reloadPicture();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
