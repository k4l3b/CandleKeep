package com.boondocksaints.candlekeep;

import java.util.ArrayList;
import java.util.List;

import com.boondocksaints.candlekeep.classes.Autor;
import com.boondocksaints.candlekeep.classes.Categoria;
import com.boondocksaints.candlekeep.classes.Libro;
import com.boondocksaints.candlekeep.data.CandleKeepDAO;




import android.os.Bundle;
import android.app.Activity;
import android.app.Application;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
	private List<Libro> libros;
	
            
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
		
		this.spnLibros.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int position, long id) {
				Libro l = dao.obtenerLibroPorISBN(libros.get(position).getISBN(), true);
				etxIsbn.setText(l.getISBN());
				etxTitulo.setText(l.getTitulo());
				etxCantHojas.setText(l.getCantidadPaginas().toString()); 
				etxPubDate.setText(l.getFechaPublicacion());
				//Toast.makeText(getApplicationContext(), Integer.toString(l.getAutores().size()), Toast.LENGTH_LONG).show();
			
				
				List<String> listaAutores = new ArrayList<String>();
				
				for (Autor autor : l.getAutores()) {
					listaAutores.add(autor.getNombre());
					
				}
				
				 
				
				ArrayAdapter<String> adaptador_autores = new ArrayAdapter<String>(MainActivity.this,						
						android.R.layout.simple_list_item_1,listaAutores);

				lvwAutores.setAdapter(adaptador_autores);
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
			

	}
	
	private void cargarLibros()
	{
		List<String> listaLibros = new ArrayList<String>();
		this.libros = dao.obtenerLibros(false);// obtengo solo la cabecera para el spinner
		
		for (Libro libro : this.libros) {
			listaLibros.add(libro.getTitulo());
			
		}
		ArrayAdapter<String> adaptador_libros = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listaLibros);	        
		adaptador_libros.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnLibros.setAdapter(adaptador_libros);
	
	}

	private void cargolibroficticio()
	{

	     Libro libro = new Libro();
	     libro.setISBN("987-1347-11-4");
	     libro.setTitulo("Web Services con C# v4.0");
	     libro.setFechaPublicacion("2013-05-01");
	     libro.setCantidadPaginas(453);
	     libro.getCategorias().add(new Categoria("Inf","Informática")); 
	     libro.getCategorias().add(new Categoria("Tec","Tecnología")); 
	     
	     dao.guardarLibro(libro);

	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.dao = new CandleKeepDAO(getApplicationContext(), CandleKeepDAO.DB_NAME, null, CandleKeepDAO.DB_VERSION);
		//cargolibroficticio();
		obtenerInstancia();
		cargarLibros();
		asignarListeners();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
