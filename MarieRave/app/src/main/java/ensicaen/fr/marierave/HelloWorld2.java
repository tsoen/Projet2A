package ensicaen.fr.marierave;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelloWorld2 extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.hello_world_2, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		TextView txtFromAgrs = view.findViewById(R.id.txtHelloWorld2);
		
		Bundle args = getArguments();
		txtFromAgrs.setText(args.getString("myParameterText"));
	}
}
