package org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia;

/**
 * Clase enumerada para los distintos tramos de la permanencia
 * 
 * @see Permanencia
 * @author Juan Antonio Manzano Plaza
 * @version 2
 */
public enum Tramo {
	MANANA("Mañana"), TARDE("Tarde");
	private String cadenaAMostrar;

	/**
	 * Constructor privado
	 * 
	 * @param cadenaAMostrar
	 *            la representación del tramo como cadena de caracteres
	 */
	private Tramo(String cadenaAMostrar) {
		this.cadenaAMostrar = cadenaAMostrar;
	}

	/**
	 * Representa un tramo como una cadena de caracteres
	 * 
	 * @return la representación del tramo
	 */
	public String toString() {
		return cadenaAMostrar;
	}

}
