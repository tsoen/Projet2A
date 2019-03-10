package ensicaen.fr.marierave.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Controllers.SkillDAO;
import ensicaen.fr.marierave.Controllers.SkillheaderDAO;
import ensicaen.fr.marierave.Controllers.SubjectDAO;
import ensicaen.fr.marierave.Model.Skill;
import ensicaen.fr.marierave.Model.Skillheader;
import ensicaen.fr.marierave.Model.Subject;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Views.Dialogs.ImportFileDialog;
import ensicaen.fr.marierave.Views.Dialogs.NewOrUpdateSkillDialog;
import ensicaen.fr.marierave.Views.Dialogs.NewOrUpdateSkillheaderDialog;
import ensicaen.fr.marierave.Views.Dialogs.NewOrUpdateSubjectDialog;

public class AdministrationSkills extends Fragment
{
	private ListView skillsListview;
	
	private ListView topicListview;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.administration_skills, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		skillsListview = view.findViewById(R.id.listCompetences);
		topicListview = view.findViewById(R.id.listSubjects);
		
		reloadSkillListView(null);
		
		reloadSubjectListView();
		
		Button btn_newSubject = view.findViewById(R.id.btn_newSubject);
		btn_newSubject.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				NewOrUpdateSubjectDialog dialog = new NewOrUpdateSubjectDialog();
				FragmentManager fm = getActivity().getSupportFragmentManager();
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.show(fm, "newSubject");
			}
		});
		
		Button btn_newSkillheader = view.findViewById(R.id.btn_newSkillheader);
		btn_newSkillheader.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				NewOrUpdateSkillheaderDialog dialog = new NewOrUpdateSkillheaderDialog();
				FragmentManager fm = getActivity().getSupportFragmentManager();
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.show(fm, "newSkillheader");
			}
		});
		
		Button btnNewSkill = view.findViewById(R.id.button4);
		btnNewSkill.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				NewOrUpdateSkillDialog dialog = new NewOrUpdateSkillDialog();
				FragmentManager fm = getActivity().getSupportFragmentManager();
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.show(fm, "newSkill");
			}
		});
		
		ImageButton btnBack = view.findViewById(R.id.backButton);
		btnBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		Button btnImportSkills = view.findViewById(R.id.button25);
		btnImportSkills.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				test();
				
				FragmentManager fm = getActivity().getSupportFragmentManager();
				Bundle bundle = new Bundle();
				bundle.putString("mode", "importSkills");
				
				ImportFileDialog dialog = new ImportFileDialog();
				dialog.setArguments(bundle);
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.show(fm, "importFile");
			}
		});
		
		topicListview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				reloadSkillListView((Subject) parent.getAdapter().getItem(position));
			}
		});
		
	}
	
	public void reloadSkillListView(Subject filterSubject)
	{
		ListviewSkillAdapter skillsAdapter = new ListviewSkillAdapter(this);
		List<Subject> subjectList = new ArrayList<>();
		
		if (filterSubject != null) {
			subjectList.add(filterSubject);
		}
		else {
			subjectList = new SubjectDAO(getContext()).getAllSubjects();
		}
		
		for (Subject subject : subjectList) {
			skillsAdapter.addItem(subject);
			
			for (Skillheader skillheader : new SkillheaderDAO(getContext()).getSkillheadersInSubject(subject.getName())) {
				skillsAdapter.addItem(skillheader);
				
				for (Skill skill : new SkillDAO(getContext()).getSkillsInHeader(skillheader.getName())) {
					skillsAdapter.addItem(skill);
				}
			}
		}
		
		skillsListview.setAdapter(skillsAdapter);
		skillsAdapter.notifyDataSetChanged();
	}
	
	public void reloadSubjectListView()
	{
		ListviewTopicsAdapter topicsAdapter = new ListviewTopicsAdapter(getActivity(), new SubjectDAO(getContext()).getAllSubjects());
		topicListview.setAdapter(topicsAdapter);
		topicsAdapter.notifyDataSetChanged();
	}
	
	public void test()
	{
		
		//First Check if the external storage is writable
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			Log.d("myapp", "ko");
		}
		
		Bitmap screen = test5(getActivity().getWindow().findViewById(R.id.adminSkillsViewLayout));
		
		File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), "myPdfFile.pdf");
		
		if (pdfFile.exists()) {
			pdfFile.delete();
			pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), "myPdfFile.pdf");
		}
		
		
		try {
			Document document = new Document();
			
			PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
			document.open();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			screen.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			addImage(document, byteArray);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(pdfFile);
		intent.setDataAndType(uri, "application/pdf");
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
		
		/*Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, "receiver_email_address");
		email.putExtra(Intent.EXTRA_SUBJECT, "subject");
		email.putExtra(Intent.EXTRA_TEXT, "email body");
		Uri uri = Uri.fromFile(pdfFile);
		email.putExtra(Intent.EXTRA_STREAM, uri);
		email.setType("application/pdf");
		email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getActivity().startActivity(email);*/
		
	}
	
	public Bitmap test5(View view)
	{
		view.setDrawingCacheEnabled(true);
		
		ListAdapter adapter = skillsListview.getAdapter();
		int itemscount = adapter.getCount();
		int allitemsheight = 0;
		int tx = (int) skillsListview.getX();
		int ty = (int) skillsListview.getY();
		
		for (int i = 0; i < itemscount; i++) {
			
			View childView = adapter.getView(i, null, skillsListview);
			childView.measure(View.MeasureSpec.makeMeasureSpec(skillsListview.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec
					.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
			
			childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
			childView.setDrawingCacheEnabled(true);
			childView.buildDrawingCache();
			allitemsheight += childView.getMeasuredHeight();
		}
		
		skillsListview.measure(View.MeasureSpec.makeMeasureSpec(this.skillsListview.getWidth() + tx + 300, View.MeasureSpec.EXACTLY), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		
		Bitmap bm1 = view.getDrawingCache();
		Bitmap bm2 = Bitmap.createBitmap(skillsListview.getMeasuredWidth(), allitemsheight, Bitmap.Config.ARGB_8888);
		Bitmap bm = overlay(bm2, bm1);
		
		Canvas canvas = new Canvas(bm);
		
		canvas.translate(tx, ty);
		
		for (int i = 0; i < adapter.getCount(); i++) {
			View childView = adapter.getView(i, null, this.skillsListview);
			
			childView.measure(View.MeasureSpec.makeMeasureSpec(this.skillsListview.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec
					.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
			
			childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
			childView.setDrawingCacheEnabled(true);
			childView.buildDrawingCache();
			childView.getDrawingCache();
			childView.draw(canvas);
			canvas.translate(0, childView.getMeasuredHeight() + 2);
		}
		
		return bm;
	}
	
	public Bitmap overlay(Bitmap bmp1, Bitmap bmp2)
	{
		Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
		Canvas canvas = new Canvas(bmOverlay);
		canvas.drawBitmap(bmp1, new Matrix(), null);
		canvas.drawBitmap(bmp2, 0, 0, null);
		return bmOverlay;
	}
	
	private void addImage(Document document, byte[] byteArray)
	{
		Image image = null;
		try {
			image = Image.getInstance(byteArray);
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		
		image.scalePercent(25f);
		
		try {
			document.add(image);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private class ListviewSkillAdapter extends BaseAdapter
	{
		private List<Object> _skillsAndHeaders = new ArrayList<>();
		
		private AdministrationSkills fragment;
		
		ListviewSkillAdapter(AdministrationSkills fragment)
		{
			super();
			this.fragment = fragment;
		}
		
		void addItem(Object item)
		{
			_skillsAndHeaders.add(item);
			notifyDataSetChanged();
		}
		
		void removeItem(Object obj)
		{
			_skillsAndHeaders.remove(obj);
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount()
		{
			return _skillsAndHeaders.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return _skillsAndHeaders.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		private class ViewHolder
		{
			private TextView _code;
			private TextView _name;
			private ImageButton _btnEdit;
			private ImageButton _btnDelete;
		}
		
		private class HeaderHolder
		{
			private TextView _header;
			private ImageButton _btnEdit;
			private ImageButton _btnDelete;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			LayoutInflater mInflater = fragment.getLayoutInflater();
			
			final Object objectAt = _skillsAndHeaders.get(position);
			
			if (objectAt instanceof Skill) {
				convertView = mInflater.inflate(R.layout.listview_skill_admin_item, null);
				
				ViewHolder holder = new ViewHolder();
				holder._code = convertView.findViewById(R.id.txtCode);
				holder._name = convertView.findViewById(R.id.txtSkill);
				holder._btnEdit = convertView.findViewById(R.id.btnEdit);
				holder._btnDelete = convertView.findViewById(R.id.button9);
				
				final Skill skill = (Skill) objectAt;
				holder._code.setText(skill.getCode());
				holder._name.setText(skill.getName());
				
				holder._btnEdit.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Bundle bundle = new Bundle();
						bundle.putString("skillCode", skill.getCode());
						bundle.putString("skillName", skill.getName());
						bundle.putString("skillHeaderName", skill.getSkillheader());
						
						NewOrUpdateSkillDialog dialog = new NewOrUpdateSkillDialog();
						dialog.setArguments(bundle);
						dialog.setTargetFragment(fragment, 0);
						dialog.show(getActivity().getSupportFragmentManager(), "newSkill");
					}
				});
				
				holder._btnDelete.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
						builder.setMessage("Etes-vous sûr de vouloir supprimer la compétence ? Cette action est irréversible");
						builder.setCancelable(true);
						builder.setPositiveButton("Oui", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								new SkillDAO(getContext()).deleteSkill(skill.getCode());
								removeItem(skill);
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
			else if (objectAt instanceof Subject) {
				convertView = mInflater.inflate(R.layout.listview_skill_subject_admin_item, null);
				
				HeaderHolder subjectHolder = new HeaderHolder();
				subjectHolder._header = convertView.findViewById(R.id.txtBigHeader);
				subjectHolder._btnEdit = convertView.findViewById(R.id.btnEdit);
				subjectHolder._btnDelete = convertView.findViewById(R.id.button11);
				
				final Subject subject = (Subject) objectAt;
				subjectHolder._header.setText(subject.getName());
				
				subjectHolder._btnEdit.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Bundle bundle = new Bundle();
						bundle.putString("subjectName", subject.getName());
						
						NewOrUpdateSubjectDialog dialog = new NewOrUpdateSubjectDialog();
						dialog.setArguments(bundle);
						dialog.setTargetFragment(fragment, 0);
						dialog.show(getActivity().getSupportFragmentManager(), "newSubject");
					}
				});
				
				subjectHolder._btnDelete.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
						builder.setMessage("Etes-vous sûr de vouloir supprimer cette matière ? " + "Toutes les sections et compétences associées seront supprimées. " +
								"Cette action est irreversible.");
						builder.setCancelable(true);
						builder.setPositiveButton("Oui", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								new SubjectDAO(getContext()).deleteSubject(subject.getName());
								reloadSkillListView(null);
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
			else if (objectAt instanceof Skillheader) {
				convertView = mInflater.inflate(R.layout.listview_skill_skillheader_admin_item, null);
				
				HeaderHolder skillheaderHolder = new HeaderHolder();
				skillheaderHolder._header = convertView.findViewById(R.id.txtLittleHeader);
				skillheaderHolder._btnEdit = convertView.findViewById(R.id.btnEdit);
				skillheaderHolder._btnDelete = convertView.findViewById(R.id.button11);
				
				final Skillheader skillheader = (Skillheader) objectAt;
				skillheaderHolder._header.setText(skillheader.getName());
				
				skillheaderHolder._btnEdit.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Bundle bundle = new Bundle();
						bundle.putString("skillheaderName", skillheader.getName());
						bundle.putString("subjectName", skillheader.getSubject());
						
						NewOrUpdateSkillheaderDialog dialog = new NewOrUpdateSkillheaderDialog();
						dialog.setArguments(bundle);
						dialog.setTargetFragment(fragment, 0);
						dialog.show(getActivity().getSupportFragmentManager(), "newSkillheader");
					}
				});
				
				skillheaderHolder._btnDelete.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
						builder.setMessage("Etes-vous sûr de vouloir supprimer cette section ? " + "Toutes les compétences associées seront supprimées. " + "Cette " +
								"action est irreversible.");
						builder.setCancelable(true);
						builder.setPositiveButton("Oui", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								new SkillheaderDAO(getContext()).deleteSkillheader(skillheader.getName());
								reloadSkillListView(null);
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
			
			return convertView;
		}
	}
	
	private class ListviewTopicsAdapter extends BaseAdapter
	{
		private List<Subject> _topicList;
		private Activity _activity;
		
		ListviewTopicsAdapter(Activity activity, List<Subject> productList)
		{
			super();
			_activity = activity;
			_topicList = productList;
		}
		
		@Override
		public int getCount()
		{
			return _topicList.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return _topicList.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		private class ViewHolder
		{
			private TextView _txtTopic;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) {
				ViewHolder holder = new ViewHolder();
				convertView = _activity.getLayoutInflater().inflate(R.layout.listview_topic_item, null);
				holder._txtTopic = convertView.findViewById(R.id.txtTopic);
				holder._txtTopic.setText(_topicList.get(position).getName());
			}
			
			return convertView;
		}
	}
}
