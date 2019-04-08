package ensicaen.fr.marierave.Views.Dialogs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;

public class EditPersonnalPictureDialog extends DialogFragment implements View.OnClickListener
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.dialog_edit_personnal_picture, container);
		
		getDialog().setTitle("Sélectionner une image");
		
		Button btnTakePicture = view.findViewById(R.id.button8);
		btnTakePicture.setOnClickListener(this);
		
		Button btnSelectFile = view.findViewById(R.id.button27);
		btnSelectFile.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.button8:
				if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
				}
				
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
					
					Child child = new ChildDAO(getContext()).getChild(getArguments().getInt("childId"));
					
					String fileName = child.getFirstname() + "_" + child.getName() + "_" + child.getId() + ".jpg";
					
					
					File image = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
							.getAbsolutePath() + File.separator + "ANEC", fileName);
					
					Uri photoURI = Uri.fromFile(image);
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
					getActivity().startActivityForResult(takePictureIntent, 1);
				}

				dismiss();
				break;
			
			case R.id.button27:

				Child child = new ChildDAO(getContext()).getChild(getArguments().getInt("childId"));

				String fileName = child.getFirstname() + "_" + child.getName() + "_" + child.getId() + ".jpg";

				File image = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
						.getAbsolutePath() + File.separator + "ANEC", fileName);


				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.setType("*/*");
				Utils.tempFileUri = image.getAbsolutePath();

				getActivity().startActivityForResult(i, 50);

				dismiss();
				break;
			default:
				dismiss();
				break;
		}
	}
}
