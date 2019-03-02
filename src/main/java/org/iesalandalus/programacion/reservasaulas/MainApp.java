package org.iesalandalus.programacion.reservasaulas;

import org.iesalandalus.programacion.reservasaulas.controlador.ControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.IModeloReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.ModeloReservasAulas;
import org.iesalandalus.programacion.reservasaulas.vista.IVistaReservasAulas;
import org.iesalandalus.programacion.reservasaulas.vista.VistaReservasAulas;

/**
 *
 * Clase principal del programa. Se encarga de hacer la primera llamada y
 * comenzar la ejecuci�n.
 * 
 * @see VistaReservasAulas
 * @see ControladorReservasAulas
 * @see ModeloReservasAulas
 * @author Juan Antonio Manzano Plaza
 * @version 2
 *
 */
public class MainApp {

	public static void main(String[] args) {
		System.out.println("Programa para la gesti�n de reservas de espacios del IES Al-�ndalus.");
		IVistaReservasAulas vista = new VistaReservasAulas();
		IModeloReservasAulas modelo = new ModeloReservasAulas();
		IControladorReservasAulas controlador = new ControladorReservasAulas(modelo, vista);
		controlador.comenzar();
	}

}
