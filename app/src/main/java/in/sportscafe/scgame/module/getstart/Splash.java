package in.sportscafe.scgame.module.getstart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import in.sportscafe.scgame.R;


public class Splash extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		Handler handler =new Handler();
		handler.postDelayed(new Runnable() 
		{
			
	
		 
		public void run()
		{
			
			Intent openMainActivity =  new Intent(Splash.this, GetStartActivity.class);
            startActivity(openMainActivity);
            finish();
           
			}
		},1500);
		
		}
		
		
		
	}

