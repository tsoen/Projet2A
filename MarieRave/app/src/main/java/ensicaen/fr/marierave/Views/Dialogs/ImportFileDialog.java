package ensicaen.fr.marierave.Views.Dialogs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import ensicaen.fr.marierave.R;

public class ImportFileDialog extends DialogFragment implements android.view.View.OnClickListener
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.dialog_import_file, container);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setCancelable(false);
		
		ImageButton btnDownload = view.findViewById(R.id.imageButton);
		btnDownload.setOnClickListener(this);
		
		ImageButton btnImport = view.findViewById(R.id.imageButton3);
		btnImport.setOnClickListener(this);
		
		Button btnCancel = view.findViewById(R.id.button29);
		btnCancel.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.imageButton:
				storeFile();
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setMessage("Le fichier a été téléchargé dans le dossier Downloads/")
						.setCancelable(false).setPositiveButton("Fermer", null);
				
				AlertDialog alert = builder.create();
				alert.show();
				
				dismiss();
				break;
			
			case R.id.imageButton3:
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.setType("*/*");
				getActivity().startActivityForResult(i, 42);
				dismiss();
				break;
			case R.id.button29:
				dismiss();
			default:
				dismiss();
				break;
		}
	}
	
	public void storeFile()
	{
		if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
			return;
		}
		
		try {
			InputStream in = getActivity().getAssets().open("tableur.csv");
			
			OutputStream out = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/tableur.csv");
			
			byte[] buffer = new byte[512];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			
			in.close();
			out.flush();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
