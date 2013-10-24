/**
 * 
 */
package com.boondocksaints.candlekeep.data;

import java.util.ArrayList;
import java.util.List;

import com.boondocksaints.candlekeep.classes.*;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Kaleb
 *
 */
public class CandleKeepDAO extends SQLiteOpenHelper {
// Constantes *****************************************************************
	public static final String DB_NAME = "CandleKeepDataBase";
	public static final int DB_VERSION = 1;

// Constructores **************************************************************
	public CandleKeepDAO(Context context, String name, CursorFactory factory,
			int version) 
	{
		super(context, name, factory, version);
	}

// Metodos de Definicion de DB ************************************************
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		// Inicializo la estructura de datos ----------------------------------
		
		// Tabla LIBROS
		db.execSQL("create table LIBROS (ISBN text primary key, TITULO text not null, FECHA_PUBLICACION text not null, CANT_HOJAS integer not null)");

		// Tabla AUTORES
		db.execSQL("create table AUTORES (ID_AUTOR integer primary key, NOMBRE text not null)");

		// Tabla AUTORES_POR_LIBRO
		db.execSQL("create table AUTORES_POR_LIBRO (ISBN text, ID_AUTOR integer, primary key(ISBN, ID_AUTOR))");

		// Tabla CATEGORIAS
		db.execSQL("create table CATEGORIAS (ID_CATEG text primary key, DESCRIPCION text not null)");

		// Tabla CATEGORIAS_POR_LIBRO
		db.execSQL("create table CATEGORIAS_POR_LIBRO (ISBN text, ID_CATEG text, primary key(ISBN, ID_CATEG))");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
		
		// Paso de version 1 -> 2
		// -- no necesario aun --

	}

// Metodos Auxiliares *********************************************************
	private Cursor ejecutarQuery(String query)
	{
		SQLiteDatabase db = getWritableDatabase();
		
		Cursor rs = db.rawQuery(query, null);
		
		db.close();
		
		return rs;
	}

	private void ejecutarNoQuery(String query)
	{
		SQLiteDatabase db = getWritableDatabase();
		
		db.execSQL(query);
		
		db.close();
	}
	
// Metodos de Capa de Datos ***************************************************
	public Categoria obtenerCategoriaPorId(String idCategoria)
	{
		Categoria unaCateg = null;
		
		Cursor rs = this.ejecutarQuery(String.format("select ID_CATEG, DESCRIPCION from CATEGORIAS where ID_CATEG='%s'", 
				idCategoria));
		
		if (rs.moveToNext())
		{
			unaCateg = new Categoria(rs.getString(0), rs.getString(1)); 
		}
		
		rs.close();
		
		return unaCateg;
	}
	
	public List<Categoria> obtenerCategoriasPorLibro(String isbn)
	{
		List<Categoria> categorias = new ArrayList<Categoria>();
		
		Cursor rs = this.ejecutarQuery(
				String.format("select C.ID_CATEG, C.DESCRIPCION " +
							  " from CATEGORIAS C, CATEGORIAS_POR_LIBRO CpL" + 
							  " where CpL.ISBN='%s' and CpL.ID_CATEG = C.ID_CATEG", 
							  isbn));
		
		while (rs.moveToNext())
		{
			categorias.add(new Categoria(rs.getString(0), rs.getString(1))); 
		}
		
		rs.close();
		
		return categorias;
	}
	
	public Categoria guardarCategoria(Categoria unaCategoria)
	{
		Categoria aux = this.obtenerCategoriaPorId(unaCategoria.getIdCategoria());
		String query = "";
		
		if (aux == null)
		{
			// Hago un insert
			query = String.format("insert into CATEGORIAS (ID_CATEG, DESCRIPCION) values ('%s','%s')", 
					unaCategoria.getIdCategoria(), unaCategoria.getDescripcion());
		}
		else
		{
			// Hago un update 
			query = String.format("update CATEGORIAS set DESCRIPCION='%s' where ID_CATEG='%s'", 
					unaCategoria.getDescripcion(), unaCategoria.getIdCategoria());
		}
		
		this.ejecutarNoQuery(query);
		
		return unaCategoria;
	}
	
	public Categoria eliminarCategoria(Categoria unaCategoria)
	{
		this.ejecutarNoQuery(String.format("delete from CATEGORIAS where ID_CATEG='%s'", 
				unaCategoria.getIdCategoria()));
		
		return unaCategoria;
		
	}
	
	public void guardarCategoriasPorLibro(String isbn, List<Categoria> categorias)
	{
		// primero borro todas las categorias asociadas al libro
		this.ejecutarNoQuery(String.format("delete from CATEGORIAS_POR_LIBRO where ISBN='%s'", isbn));
		
		// ahora agrego a la tabla de categorias las que me falten (tambien actualizo las existentes)
		for (Categoria categoria : categorias) {
			this.guardarCategoria(categoria); // la guardo si no existe, o la actualizo si ya existe
			
			// agrego la referencia de la categoria al libro
			this.ejecutarNoQuery(String.format("insert into CATEGORIAS_POR_LIBRO (ISBN, ID_CATEG) values ('%s','%s')",  
					isbn, categoria.getIdCategoria()));
		}
		
	}
	
	public List<Libro> obtenerLibros()
	{
		List<Libro> libros = new ArrayList<Libro>();
		
		Cursor rs = this.ejecutarQuery("select ISBN, TITULO, FECHA_PUBLICACION, CANT_HOJAS from LIBROS order by TITULO");
		
		while (rs.moveToNext())
		{
			// TODO: hay que llenar el array de autores por libro
			List<Categoria> categorias = this.obtenerCategoriasPorLibro(rs.getString(0));
			libros.add(new Libro(rs.getString(0), rs.getString(1), rs.getString(2), rs.getInt(3), new ArrayList<Autor>(), categorias)); 
		}
		
		rs.close();
		
		return libros;
	}

	public Libro obtenerLibroPorISBN(String isbn)
	{
		Libro unLibro = null;
		
		Cursor rs = this.ejecutarQuery(String.format("select ISBN, TITULO, FECHA_PUBLICACION, CANT_HOJAS from LIBROS where ISBN='%s'", 
				isbn));
		
		if (rs.moveToNext())
		{
			List<Categoria> categorias = this.obtenerCategoriasPorLibro(isbn);
			List<Autor> autores = new ArrayList<Autor>(); // TODO: falta obtener los autores
			
			unLibro = new Libro(rs.getString(0), rs.getString(1), rs.getString(2), rs.getInt(3), autores, categorias); 
		}
		
		rs.close();
		
		return unLibro;
	}

	public Libro guardarLibro(Libro unLibro)
	{
		Libro aux = this.obtenerLibroPorISBN(unLibro.getISBN());
		String query = "";
		
		if (aux == null)
		{
			// Hago un insert
			query = String.format("insert into LIBROS (ISBN, TITULO, FECHA_PUBLICACION, CANT_HOJAS) values ('%s','%s', '%s', %d)", 
					unLibro.getISBN(), unLibro.getTitulo(), unLibro.getFechaPublicacion(), unLibro.getCantidadDeHojas());
		}
		else
		{
			// Hago un update 
			query = String.format("update LIBROS set TITULO='%s', FECHA_PUBLICACION='%s', CANT_HOJAS=%d where ISBN='%s'", 
					unLibro.getTitulo(), unLibro.getFechaPublicacion(), unLibro.getCantidadDeHojas(), unLibro.getISBN());
		}
		
		this.ejecutarNoQuery(query);
		
		// TODO: falta guardar los autores
		this.guardarCategoriasPorLibro(unLibro.getISBN(), unLibro.getCategorias());
		
		return unLibro;
	}
	
}
