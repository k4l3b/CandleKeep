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
	public static final int DB_VERSION = 3;

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
		db.execSQL("create table LIBROS (ISBN text primary key, TITULO text not null, FECHA_PUBLICACION text not null, CANT_PAGINAS integer not null)");

		// Tabla AUTORES
		db.execSQL("create table AUTORES (ID_AUTOR integer primary key autoincrement, NOMBRE text not null)");

		// Tabla AUTORES_POR_LIBRO
		db.execSQL("create table AUTORES_POR_LIBRO (ISBN text, ID_AUTOR integer, primary key(ISBN, ID_AUTOR))");

		// Tabla CATEGORIAS
		db.execSQL("create table CATEGORIAS (ID_CATEG text primary key, DESCRIPCION text not null)");

		// Tabla CATEGORIAS_POR_LIBRO
		db.execSQL("create table CATEGORIAS_POR_LIBRO (ISBN text, ID_CATEG text, primary key(ISBN, ID_CATEG))");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
		
		Integer version = versionAnterior; 
		
		// Paso de version 1 -> 2
		if (version < 2)
		{
			// Se cambio el nombre de un campo: LIBROS.CANT_HOJAS a LIBROS.CANT_PAGINAS
			
			db.execSQL("create table LIBROS_TMP (ISBN text primary key, TITULO text not null, FECHA_PUBLICACION text not null, CANT_HOJAS integer not null)");
			db.execSQL("insert into LIBROS_TMP select * from LIBROS");
			db.execSQL("drop table LIBROS");
			db.execSQL("create table LIBROS (ISBN text primary key, TITULO text not null, FECHA_PUBLICACION text not null, CANT_PAGINAS integer not null)");
			db.execSQL("insert into LIBROS select * from LIBROS_TMP");
			db.execSQL("drop table LIBROS_TMP");
			
			// aumento la version para el proximo if
			version++; 
		}

		// Paso de version 2 -> 3
		if (version < 3)
		{
			// Se cambio el campo AUTORES.ID_AUTOR a AUTOINCREMENT
			
			db.execSQL("create table AUTORES_TMP (ID_AUTOR integer primary key, NOMBRE text not null)");
			db.execSQL("insert into AUTORES_TMP select * from AUTORES");
			db.execSQL("drop table AUTORES");
			db.execSQL("create table AUTORES (ID_AUTOR integer primary key autoincrement, NOMBRE text not null)");
			db.execSQL("insert into AUTORES select * from AUTORES_TMP");
			db.execSQL("drop table AUTORES_TMP");
			
			// aumento la version para el proximo if
			version++; 
		}
	}

// Metodos de Capa de Datos ***************************************************
	public Categoria obtenerCategoriaPorId(String idCategoria)
	{
		SQLiteDatabase db = getWritableDatabase();
		Categoria unaCateg = null;

		Cursor rs = db.rawQuery(String.format("select ID_CATEG, DESCRIPCION from CATEGORIAS where ID_CATEG='%s'", 
				idCategoria), null);
		
		if (rs.moveToNext())
		{
			unaCateg = new Categoria(rs.getString(0), rs.getString(1)); 
		}
		
		rs.close();
		db.close();
		
		return unaCateg;
	}
	
	public List<Categoria> obtenerCategorias()
	{
		List<Categoria> categorias = new ArrayList<Categoria>();
		
		SQLiteDatabase db = getWritableDatabase();
		Cursor rs = db.rawQuery("select ID_CATEG, DESCRIPCION from CATEGORIAS", null);
		
		while (rs.moveToNext())
			categorias.add(new Categoria(rs.getString(0), rs.getString(1))); 
		
		rs.close();
		db.close();
		
		return categorias;
	}
	
	public List<Categoria> obtenerCategoriasPorLibro(String isbn)
	{
		List<Categoria> categorias = new ArrayList<Categoria>();
		
		SQLiteDatabase db = getWritableDatabase();
		Cursor rs = db.rawQuery(
				String.format("select C.ID_CATEG, C.DESCRIPCION " +
							  " from CATEGORIAS C, CATEGORIAS_POR_LIBRO CpL" + 
							  " where CpL.ISBN='%s' and CpL.ID_CATEG = C.ID_CATEG", 
							  isbn), null);
		
		while (rs.moveToNext())
		{
			categorias.add(new Categoria(rs.getString(0), rs.getString(1))); 
		}
		
		rs.close();
		db.close();
		
		return categorias;
	}
	
	public Categoria guardarCategoria(Categoria unaCategoria)
	{
		if (unaCategoria != null)
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
			
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(query);
			db.close();
		}
		return unaCategoria;
	}
	
	public Categoria eliminarCategoria(Categoria unaCategoria)
	{
		if (unaCategoria != null)
		{
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(String.format("delete from CATEGORIAS where ID_CATEG='%s'", 
					unaCategoria.getIdCategoria()));
			db.close();
		}
		return unaCategoria;
		
	}
	
	public void guardarCategoriasPorLibro(String isbn, List<Categoria> categorias)
	{
		// primero borro todas las categorias asociadas al libro
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(String.format("delete from CATEGORIAS_POR_LIBRO where ISBN='%s'", isbn));
		db.close();
		
		// ahora agrego a la tabla de categorias las que me falten (tambien actualizo las existentes)
		for (Categoria categoria : categorias) {
			this.guardarCategoria(categoria); // la guardo si no existe, o la actualizo si ya existe
			
			// agrego la referencia de la categoria al libro
			db = getWritableDatabase();
			db.execSQL(String.format("insert into CATEGORIAS_POR_LIBRO (ISBN, ID_CATEG) values ('%s','%s')",  
					isbn, categoria.getIdCategoria()));
			db.close();
		}
		
	}
	
	public Autor obtenerAutorPorId(Integer idAutor)
	{
		SQLiteDatabase db = getWritableDatabase();
		Autor unAutor = null;

		Cursor rs = db.rawQuery(String.format("select ID_AUTOR, NOMBRE from AUTORES where ID_AUTOR=%d", 
				idAutor), null);
		
		if (rs.moveToNext())
		{
			unAutor = new Autor(rs.getInt(0), rs.getString(1)); 
		}
		
		rs.close();
		db.close();
		
		return unAutor;
	}
	
	public List<Autor> obtenerAutores()
	{
		List<Autor> autores = new ArrayList<Autor>();
		
		SQLiteDatabase db = getWritableDatabase();
		Cursor rs = db.rawQuery("select ID_AUTOR, NOMBRE from AUTORES", null);
		
		while (rs.moveToNext())
			autores.add(new Autor(rs.getInt(0), rs.getString(1))); 
		
		rs.close();
		db.close();
		
		return autores;
	}
	
	public List<Autor> obtenerAutoresPorLibro(String isbn)
	{
		List<Autor> autores = new ArrayList<Autor>();
		
		SQLiteDatabase db = getWritableDatabase();
		Cursor rs = db.rawQuery(
				String.format("select A.ID_AUTOR, A.NOMBRE " +
							  " from AUTORES A, AUTORES_POR_LIBRO ApL" + 
							  " where ApL.ISBN='%s' and ApL.ID_AUTOR = A.ID_AUTOR", 
							  isbn), null);
		
		while (rs.moveToNext())
			autores.add(new Autor(rs.getInt(0), rs.getString(1))); 
		
		rs.close();
		db.close();
		
		return autores;
	}
	
	public Integer obtenerAutorUltimoIdAutorInsertado()
	{
		SQLiteDatabase db = getWritableDatabase();
		Cursor rs = db.rawQuery("SELECT SEQ FROM SQLITE_SEQUENCE WHERE NAME='AUTORES'", null);
		int id = 0;     
		
		if (rs.moveToFirst())
			id = rs.getInt(0);
		
		rs.close();
		db.close();
		
		return id;		
	}
	
	public Autor guardarAutor(Autor unAutor)
	{
		if (unAutor != null)
		{
			Autor aux = this.obtenerAutorPorId(unAutor.getIdAutor());
			String query = "";
			
			if (aux == null)
			{
				// Hago un insert
				query = String.format("insert into AUTORES (NOMBRE) values ('%s')", 
						unAutor.getNombre());
				// como el id es automatico, lo tengo que volver a setear en el objeto autor
				unAutor.setIdAutor(this.obtenerAutorUltimoIdAutorInsertado()+1);
			}
			else
			{
				// Hago un update 
				query = String.format("update AUTORES set NOMBRE='%s' where ID_AUTOR=%d", 
						unAutor.getNombre(), unAutor.getIdAutor());
			}
			
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(query);
			db.close();
		}
		return unAutor;
	}
	
	public Autor eliminarAutor(Autor unAutor)
	{
		if (unAutor != null)
		{
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(String.format("delete from AUTORES where ID_AUTOR=%d", 
					unAutor.getIdAutor()));
			db.close();
		}
		return unAutor;
		
	}
	
	public void guardarAutoresPorLibro(String isbn, List<Autor> autores)
	{
		// primero borro todos los autores asociados al libro
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(String.format("delete from AUTORES_POR_LIBRO where ISBN='%s'", isbn));
		db.close();
		
		// ahora agrego a la tabla de autores los que me falten (tambien actualizo los existentes)
		for (Autor autor : autores) {
			this.guardarAutor(autor); // lo guardo si no existe, o lo actualizo si ya existe
			
			// agrego la referencia del autor al libro
			db = getWritableDatabase();
			db.execSQL(String.format("insert into AUTORES_POR_LIBRO (ISBN, ID_AUTOR) values ('%s',%d)",  
					isbn, autor.getIdAutor()));
			db.close();
		}
	}

	
	public List<Libro> obtenerLibros()
	{
		List<Libro> libros = new ArrayList<Libro>();
		
		// Levanto todos los libros
		SQLiteDatabase db = getWritableDatabase();
		Cursor rs = db.rawQuery("select ISBN, TITULO, FECHA_PUBLICACION, CANT_PAGINAS from LIBROS order by TITULO", null);
		
		while (rs.moveToNext())
			libros.add(new Libro(rs.getString(0), rs.getString(1), rs.getString(2), rs.getInt(3), null, null)); 
		
		rs.close();
		db.close();
		
		// Por cada libro, leo sus autores y categorias
		for (Libro libro : libros) {
			libro.setCategorias(this.obtenerCategoriasPorLibro(libro.getISBN()));
			libro.setAutores(this.obtenerAutoresPorLibro(libro.getISBN())); 
		}

		return libros;
	}

	public Libro obtenerLibroPorISBN(String isbn)
	{
		Libro unLibro = null;
		
		SQLiteDatabase db = getWritableDatabase();
		Cursor rs = db.rawQuery(String.format("select ISBN, TITULO, FECHA_PUBLICACION, CANT_PAGINAS from LIBROS where ISBN='%s'", 
				isbn), null);
		
		if (rs.moveToNext())
			unLibro = new Libro(rs.getString(0), rs.getString(1), rs.getString(2), rs.getInt(3), null, null); 
		
		rs.close();
		db.close();
		
		// completo categorias y autores
		if (unLibro != null)
		{
			unLibro.setCategorias(this.obtenerCategoriasPorLibro(isbn));
			unLibro.setAutores(this.obtenerAutoresPorLibro(isbn));
		}
		
		return unLibro;
	}

	public Libro guardarLibro(Libro unLibro)
	{
		if (unLibro != null)
		{
			Libro aux = this.obtenerLibroPorISBN(unLibro.getISBN());
			String query = "";
			
			if (aux == null)
			{
				// Hago un insert
				query = String.format("insert into LIBROS (ISBN, TITULO, FECHA_PUBLICACION, CANT_PAGINAS) values ('%s','%s', '%s', %d)", 
						unLibro.getISBN(), unLibro.getTitulo(), unLibro.getFechaPublicacion(), unLibro.getCantidadPaginas());
			}
			else
			{
				// Hago un update 
				query = String.format("update LIBROS set TITULO='%s', FECHA_PUBLICACION='%s', CANT_PAGINAS=%d where ISBN='%s'", 
						unLibro.getTitulo(), unLibro.getFechaPublicacion(), unLibro.getCantidadPaginas(), unLibro.getISBN());
			}
			
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(query);
			db.close();
			
			this.guardarCategoriasPorLibro(unLibro.getISBN(), unLibro.getCategorias());
			this.guardarAutoresPorLibro(unLibro.getISBN(), unLibro.getAutores());
		} 
		return unLibro;
	}
	
}
