package com.boondocksaints.candlekeep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.boondocksaints.candlekeep.classes.Libro;

public class AsignarAutoresActivity extends Activity {

	private Button buttonAdministrarAutores;
	
	private Libro libro;
	
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
	
	private void obtenerLibro()
	{
		
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
