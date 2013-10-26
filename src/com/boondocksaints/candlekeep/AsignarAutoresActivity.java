package com.boondocksaints.candlekeep;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;

public class AsignarAutoresActivity extends Activity {

	private Button buttonAdministrarAutores;
	
	private void obtenerInstancias()
	{
		this.buttonAdministrarAutores = (Button) findViewById(R.id.asignarAutores_buttonAdministrarAutores);
	}
	
	private void asignarListeners()
	{
		this.buttonAdministrarAutores.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick (View v) {
				Intent intento = new Intent(AsignarAutoresActivity.this, ABMAutoresActivity.class);
				startActivity(intento);
			}
		});
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asignar_autores);
		
		this.obtenerInstancias();
		this.asignarListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.asignar_autores, menu);
		return true;
	}

}
