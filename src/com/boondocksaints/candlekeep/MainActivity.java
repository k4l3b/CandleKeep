package com.boondocksaints.candlekeep;

import java.util.ArrayList;
import java.util.List;

import com.boondocksaints.candlekeep.classes.Libro;
import com.boondocksaints.candlekeep.data.CandleKeepDAO;



import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends Activity {

	private Spinner spnLibros;
	private EditText etxIsbn;
	private EditText etxTitulo;
	private EditText etxPubDate;
	private EditText etxCantHojas;
	private ListView lvwAutores;
	private ListView lvwCategorias;
	private Button btnNuevoLibro;
	private Button btnEdtLibro;
	private Button btnBrrLibro;
	private CandleKeepDAO dao;
	
            
	private void obtenerInstancia(){
		//Obtengo las instancias desde la interfaz.
		this.spnLibros = (Spinner) findViewById(R.id.spnLibros);
		this.etxIsbn = (EditText) findViewById(R.id.etxIsbn);
		this.etxTitulo = (EditText) findViewById(R.id.etxTitulo);
		this.etxPubDate = (EditText) findViewById(R.id.etxPubDate);
		this.etxCantHojas = (EditText) findViewById(R.id.etxCantHojas);
		this.lvwAutores = (ListView) findViewById(R.id.lvwAutores);
		this.lvwCategorias = (ListView) findViewById(R.id.lvwCategorias);
		this.btnNuevoLibro = (Button) findViewById(R.id.btnNvoLibro);
		this.btnEdtLibro = (Button) findViewById(R.id.btnEdtLibro);
		this.btnBrrLibro = (Button) findViewById(R.id.btnBrrLibro);
	}
	
	private void asignarListeners(){
		this.btnNuevoLibro.setOnClickListener(new View.OnClickListener() {
			//TODO: Deberia llamar a una nueva actividad en blanco para cargar un nuevo libro
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Nuevo libro", Toast.LENGTH_SHORT).show();		
			}
		} );
		
		this.btnEdtLibro.setOnClickListener(new View.OnClickListener() {
			//TODO: Deberia de traer un nuevo activity con los datos de un libro
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Editar Libro", Toast.LENGTH_SHORT).show();		
			}
		} );

		this.btnBrrLibro.setOnClickListener(new View.OnClickListener() {
			//TODO: Deberia eliminar un libro
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Eliminar Libro", Toast.LENGTH_SHORT).show();		
			}
		} );
		

		
			

	}
	
	private void cargarLibros()
	{
		final List<String> listaLibros = new ArrayList<String>();
		
		List<Libro> libros = dao.obtenerLibros();
		for (Libro libro : libros) {
			listaLibros.add(libro.getTitulo());
			
		}
		ArrayAdapter<String> adaptador_libros = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listaLibros);	        
		adaptador_libros.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnLibros.setAdapter(adaptador_libros);
		 

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		obtenerInstancia();
		cargarLibros();
		asignarListeners();
		this.dao = new CandleKeepDAO(getApplicationContext(), CandleKeepDAO.DB_NAME, null, CandleKeepDAO.DB_VERSION);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
