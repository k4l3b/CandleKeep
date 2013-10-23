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
	
	public List<Libro> obtenerLibros()
	{
		List<Libro> libros = new ArrayList<Libro>();
		
		Cursor rs = this.ejecutarQuery("select ISBN, TITULO, FECHA_PUBLICACION, CANT_HOJAS from LIBROS");
		
		while (rs.moveToNext())
		{
			libros.add(new Libro(rs.getString(0), rs.getString(1), rs.getString(2), rs.getInt(3), new ArrayList<Autor>(), new ArrayList<Categoria>())); 
		}
		
		rs.close();
		
		return libros;
	}
	
	
}
