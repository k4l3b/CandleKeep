package com.boondocksaints.candlekeep;

import java.util.ArrayList;
import java.util.List;

import com.boondocksaints.candlekeep.classes.Autor;
import com.boondocksaints.candlekeep.classes.Categoria;
import com.boondocksaints.candlekeep.classes.Libro;
import com.boondocksaints.candlekeep.data.CandleKeepDAO;





import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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
			
			@Override
			public void onClick(View v) {
				// abro el activity para alta de nuevo libro
				Intent intento = new Intent(MainActivity.this, EditarLibroActivity.class);
				startActivity(intento);
			}
		} );
		
		this.btnEdtLibro.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// valdo que haya algo seleccionado
				if (spnLibros.getSelectedItemPosition() != Spinner.INVALID_POSITION)
				{
					// abro el activity para edicion
					Intent intento = new Intent(MainActivity.this, EditarLibroActivity.class);
					intento.putExtra("isbn", libros.get(spnLibros.getSelectedItemPosition()).getISBN());
					startActivity(intento);
				}
				else
					Toast.makeText(MainActivity.this, getString(R.string.strErrorSelLibro), Toast.LENGTH_SHORT).show();
			}
		} );

		this.btnBrrLibro.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Falta confirmacion
				// valido que haya algo seleccionado
				if (spnLibros.getSelectedItemPosition() != Spinner.INVALID_POSITION)
				{
					dao.eliminarLibro(libros.get(spnLibros.getSelectedItemPosition()));
					cargarLibros();
				}
				else
					Toast.makeText(MainActivity.this, getString(R.string.strErrorSelLibro), Toast.LENGTH_SHORT).show();
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
		LayoutParams param = new LayoutParams(lvwAutores.getLayoutParams().width, 
				150 * libros.size());
		lvwAutores.setLayoutParams(param);
		
		 
		ArrayAdapter<String> adaptador_libros = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listaLibros);	        
		adaptador_libros.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnLibros.setAdapter(adaptador_libros);
	
	}

	private void cargolibroficticio()
	{

	     Libro libro = new Libro();
	     libro.setISBN("987-1347-11-124");
	     libro.setTitulo("La vida de Peron");
	     libro.setFechaPublicacion("2013-05-11");
	     libro.setCantidadPaginas(15);
	     libro.getCategorias().add(new Categoria("His","Historia")); 
	     libro.getCategorias().add(new Categoria("Bif","Biografia"));
	     libro.getAutores().add(new Autor(-1, "Juan Domingo"));
	     libro.getAutores().add(new Autor(-1, "John Sunday"));
	     libro.getAutores().add(new Autor(-1, "Juancito Feriado"));
	     libro.getAutores().add(new Autor(-1, "Big Chin"));
	     
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
	protected void onResume() {
		super.onResume();
		cargarLibros();
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
