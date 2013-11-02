/**
 * 
 */
package com.boondocksaints.candlekeep.classes;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Kaleb
 *
 */
public class Libro implements Parcelable {

// Propiedades y Atributos ****************************************************
	private String isbn;
	private String titulo;
	private String fechaPublicacion;
	private Integer cantidadPaginas;
	private List<Autor> autores;
	private List<Categoria> categorias;
	
	public String getISBN() {
		return isbn;
	}

	public void setISBN(String isbn) {
		this.isbn = isbn;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(String fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public Integer getCantidadPaginas() {
		return cantidadPaginas;
	}

	public void setCantidadPaginas(Integer cantidadPaginas) {
		this.cantidadPaginas = cantidadPaginas;
	}

	public List<Autor> getAutores() {
		return autores;
	}

	public void setAutores(List<Autor> autores) {
		this.autores = autores;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

// Constructores **************************************************************
	public Libro() 
	{
		super();
		this.isbn = "";
		this.titulo = "";
		this.fechaPublicacion = "";
		this.cantidadPaginas = 0;
		this.autores = new ArrayList<Autor>();
		this.categorias = new ArrayList<Categoria>();
	}

	public Libro(String isbn, String titulo, String fechaPublicacion,
		Integer cantidadDeHojas, List<Autor> autores, List<Categoria> categorias) 
	{
		super();
		this.isbn = isbn;
		this.titulo = titulo;
		this.fechaPublicacion = fechaPublicacion;
		this.cantidadPaginas = cantidadDeHojas;
		this.autores = autores;
		this.categorias = categorias;
	}
	
	public Libro(Parcel in)
	{
		// Constructor desde el Parcel
		this.isbn = in.readString();
		this.titulo = in.readString();
		this.fechaPublicacion = in.readString();
		this.cantidadPaginas = in.readInt();
		
		this.autores = new ArrayList<Autor>();
		in.readList(this.autores, null);
		
		this.categorias = new ArrayList<Categoria>();
		in.readList(this.categorias, null);
		
	}

// Implementacion de Parcelable ***********************************************
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.isbn);
		dest.writeString(this.titulo);
		dest.writeString(this.fechaPublicacion);
		dest.writeInt(this.cantidadPaginas);
		dest.writeList(this.autores);
		dest.writeList(this.categorias);
	}
	
	public static final Parcelable.Creator<Libro> CREATOR = new Parcelable.Creator<Libro>() {
        public Libro createFromParcel(Parcel in) {
            return new Libro(in);
        }

        public Libro[] newArray(int size) {
            return new Libro[size];
        }
    };


}
