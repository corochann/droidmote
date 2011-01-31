package cz.mpelant.droidmotetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button b = (Button) findViewById(R.id.settings);
        Button b2 = (Button) findViewById(R.id.send);
        b.setOnClickListener(new OnClickListener() {
			//Send an intent to launch config dialog
			@Override
			public void onClick(View v) {
				Intent i = new Intent("cz.mpelant.droidmote.CONFIG");
				startActivity(i);
				
			}
		});
        
        b2.setOnClickListener(new OnClickListener() {
        	//Send an intent to perform backspace click
			@Override
			public void onClick(View v) {
				Intent i = new Intent("cz.mpelant.droidmote.SEND_TO_PC");
				i.putExtra("type", "droidmoteKeyID");
				i.putExtra("value", "VK_BACK_SPACE");
				sendBroadcast(i);
				
				
			}
		});
    }
}