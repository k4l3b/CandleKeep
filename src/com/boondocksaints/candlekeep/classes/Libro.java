/**
 * 
 */
package com.boondocksaints.candlekeep.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kaleb
 *
 */
public class Libro {

// Propiedades y Atributos ****************************************************
	private String isbn;
	private String titulo;
	private String fechaPublicacion;
	private Integer cantidadDeHojas;
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

	public Integer getCantidadDeHojas() {
		return cantidadDeHojas;
	}

	public void setCantidadDeHojas(Integer cantidadDeHojas) {
		this.cantidadDeHojas = cantidadDeHojas;
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
		this.cantidadDeHojas = 0;
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
		this.cantidadDeHojas = cantidadDeHojas;
		this.autores = autores;
		this.categorias = categorias;
	}

}
