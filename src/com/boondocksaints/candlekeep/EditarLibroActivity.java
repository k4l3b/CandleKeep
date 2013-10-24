package com.boondocksaints.candlekeep;

import java.util.ArrayList;

import com.boondocksaints.candlekeep.classes.Autor;
import com.boondocksaints.candlekeep.classes.Categoria;
import com.boondocksaints.candlekeep.classes.Libro;
import com.boondocksaints.candlekeep.data.CandleKeepDAO;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditarLibroActivity extends Activity {

	private EditText etxIsbn;
	private EditText etxTitulo;
	private EditText etxPubDate;
	private EditText etxCantHojas;
	private Button buttonGuardar;
	private Button buttonCancelar;
	
	private CandleKeepDAO dao;
	
	private Libro libro;
	
	private void obtenerInstancias(){
		//Obtengo las instancias desde la interfaz.
		this.etxIsbn = (EditText) findViewById(R.id.editarLibro_etxIsbn);
		this.etxTitulo = (EditText) findViewById(R.id.editarLibro_etxTitulo);
		this.etxPubDate = (EditText) findViewById(R.id.editarLibro_etxPubDate);
		this.etxCantHojas = (EditText) findViewById(R.id.editarLibro_etxCantHojas);
		this.buttonGuardar = (Button) findViewById(R.id.editarLibro_buttonGuardar);
		this.buttonCancelar = (Button) findViewById(R.id.editarLibro_buttonCancelar);
	}
	
	private void asignarListeners()
	{
		this.buttonGuardar.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				guardarLibro();
			}
		});
		
		this.buttonCancelar.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void inicializarDAO()
	{
		this.dao = new CandleKeepDAO(this, CandleKeepDAO.DB_NAME, null, CandleKeepDAO.DB_VERSION);
	}
	
	private void obtenerLibroAEditar()
	{
		// levanto el parametro con el que invocaron este activity
		// y leo el libro a editar
		Intent intent = getIntent();
		
		// si no existe el parametro -> isbn = ""
		String isbn = (intent.getStringExtra("isbn") == null) ? "" : intent.getStringExtra("isbn");

		this.libro = dao.obtenerLibroPorISBN(isbn, true); // obtengo el libro con todo el detalle

		// si el libro no existe, creo uno en blanco
		// ademas, seteo el isbn del libro como read-only solo para el update
		if (this.libro == null)
		{
			this.libro = new Libro("", "", "", 0, new ArrayList<Autor>(), new ArrayList<Categoria>());
			this.etxIsbn.setEnabled(true);
		}
		else
			this.etxIsbn.setEnabled(false);
		
		// Asigno los valores del libro obtenido a los campos de edicion
		this.etxIsbn.setText(this.libro.getISBN());
		this.etxTitulo.setText(this.libro.getTitulo());
		this.etxPubDate.setText(this.libro.getFechaPublicacion());
		this.etxCantHojas.setText(this.libro.getCantidadPaginas().toString());
	}
	
	private Boolean validarLibro()
	{
		if (this.etxIsbn.getText().toString().trim().length() == 0)
		{
			Toast.makeText(getApplicationContext(), getString(R.string.strErrorISBN), Toast.LENGTH_LONG).show();
			return false;
		}
			
		if (this.etxTitulo.getText().toString().trim().length() == 0)
		{
			Toast.makeText(getApplicationContext(), getString(R.string.strErrorTitulo), Toast.LENGTH_LONG).show();
			return false;
		}

		if (this.etxPubDate.getText().toString().trim().length() == 0)
		{
			Toast.makeText(getApplicationContext(), getString(R.string.strErrorFechaPublicacion), Toast.LENGTH_LONG).show();
			return false;
		}

		if (this.etxCantHojas.getText().toString().trim().length() == 0)
		{
			Toast.makeText(getApplicationContext(), getString(R.string.strErrorCantPaginas), Toast.LENGTH_LONG).show();
			return false;
		}

		return true;
	}
	
	private void guardarLibro() {
		// si el libro esta bien, lo guardo y cierro el activity
		if (this.validarLibro())
		{
			this.dao.guardarLibro(this.libro);
			finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editar_libro);
		
		this.inicializarDAO();
		this.obtenerInstancias();
		this.asignarListeners();
		this.obtenerLibroAEditar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.editar_libro, menu);
		return true;
	}

}
