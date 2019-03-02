package org.iesalandalus.programacion.reservasaulas.modelo.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Permanencia;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorTramo;

/**
 * Clase que guarda y define las operaciones que se pueden realizar sobre un
 * conjunto de reservas.
 * 
 * @see Reserva
 * @author Juan Antonio Manzano Plaza
 * @version 2
 *
 */
public class Reservas {

	private static final float MAX_PUNTOS_PROFESOR_MES = 200f;
	private List<Reserva> coleccionReservas;

	/**
	 * Constructor por defecto. Inicializa la colecci�n de profesores
	 */
	public Reservas() {
		coleccionReservas = new ArrayList<Reserva>();
	}

	/**
	 * Constructor copia. Realiza copia profunda para evitar aliasing
	 * 
	 * @param reservas
	 *            el objeto del que obtener los datos para inicializar
	 * @throws IllegalArgumentException
	 *             si recibe un conjunto de reservas nulas
	 */
	public Reservas(Reservas reservas) throws IllegalArgumentException {
		setReservas(reservas);
	}

	/**
	 * Guarda en la colecci�n actual de reservas los que hay en la colecci�n
	 * recibida como par�metro
	 * 
	 * @param reservas
	 *            la colecci�n a copiar
	 * @throws IllegalArgumentException
	 *             si se intenta copiar un conjunto de reservas nulo
	 */
	private void setReservas(Reservas reservas) throws IllegalArgumentException {
		if (reservas == null)
			throw new IllegalArgumentException("No se pueden copiar reservas nulas.");
		this.coleccionReservas = copiaProfundaReservas(reservas.getReservas());
	}

	/**
	 * Realiza la copia en profundidad de cada reserva para evitar aliasing
	 * 
	 * @param reservas
	 *            la colecci�n de reservas a copiar
	 * @return una copia de la colecci�n
	 */
	private List<Reserva> copiaProfundaReservas(List<Reserva> reservas) {
		List<Reserva> copia = new ArrayList<Reserva>();
		for (Reserva r : reservas)
			copia.add(new Reserva(r));
		return copia;
	}

	/**
	 * Obtiene todas las reservas de la colecci�n actual. Realiza una copia para
	 * evitar aliasing
	 * 
	 * @return una copia de la colecci�n
	 */
	public List<Reserva> getReservas() {
		return copiaProfundaReservas(this.coleccionReservas);
	}

	/**
	 * Obtiene el n�mero de reservas que existen en la colecci�n actual
	 * 
	 * @return el n�mero de reservas
	 */
	public int getNumReservas() {
		return this.coleccionReservas.size();
	}

	/**
	 * Guarda una reserva en la colecci�n
	 * 
	 * @param reserva
	 *            la reserva a guardar
	 * @throws IllegalArgumentException
	 *             si la reserva es nula
	 * @throws OperationNotSupportedException
	 *             si la reserva ya existe
	 */
	public void insertar(Reserva reserva) throws OperationNotSupportedException, IllegalArgumentException {
		// Comprobamos que es una reserva v�lida
		if (reserva == null)
			throw new IllegalArgumentException("No se puede realizar una reserva nula.");
		if (this.coleccionReservas.contains(reserva))
			throw new OperationNotSupportedException("La reserva ya existe.");
		if (!esMesSiguienteOPosterior(reserva))
			throw new OperationNotSupportedException(
					"S�lo se pueden hacer reservas para el mes que viene o posteriores.");
		if (getPuntosGastadosReserva(reserva) > MAX_PUNTOS_PROFESOR_MES)
			throw new OperationNotSupportedException(
					"Esta reserva excede los puntos m�ximos por mes para dicho profesor.");

		Permanencia permanenciaReserva = reserva.getPermanencia();
		Aula aulaReserva = reserva.getAula();
		List<Reserva> reservasAula = getReservasAula(aulaReserva);

		// Comprobamos que las permanencias son del tipo que corresponde para ese aula y
		// d�a
		// Comprobamos que ese aula no est� reservada para ese aula y permanencia
		for (Reserva r : reservasAula) {
			if (r.getPermanencia() instanceof PermanenciaPorTramo && permanenciaReserva instanceof PermanenciaPorHora
					&& r.getPermanencia().getDia().equals(permanenciaReserva.getDia()))
				throw new OperationNotSupportedException(
						"Ya se ha realizado una reserva por tramo para este d�a y aula.");
			if (r.getPermanencia() instanceof PermanenciaPorHora && permanenciaReserva instanceof PermanenciaPorTramo
					&& r.getPermanencia().getDia().equals(permanenciaReserva.getDia()))
				throw new OperationNotSupportedException(
						"Ya se ha realizado una reserva por hora para este d�a y aula.");
		}
		// Si ha pasado todas las comprobaciones a�adimos la reserva
		coleccionReservas.add(reserva);
	}

	/**
	 * Comprueba si el mes de la permanencia de la reserva es del mes siguiente al
	 * actual
	 * 
	 * @param aInsertar
	 *            la reserva de la que queremos comprobar la fecha
	 * @return true si la fecha es del mes siguiente o posterior, false si no
	 */
	private boolean esMesSiguienteOPosterior(Reserva aInsertar) {
		LocalDate mesSiguiente = LocalDate.now().plusMonths(1);
		if (aInsertar.getPermanencia().getDia()
				.isBefore(LocalDate.of(mesSiguiente.getYear(), mesSiguiente.getMonth(), 1)))
			return false;
		return true;
	}

	/**
	 * Obtiene los puntos gastados en las reservas de el mes de ese profesor
	 * 
	 * @param aInsertar
	 *            la reserva de la que queremos obtener los puntos gastados en ese
	 *            mes por ese profesor
	 * @return el n�mero de puntos gastadods en el mes de la reserva por el profesor
	 *         de la reserva
	 */
	private float getPuntosGastadosReserva(Reserva aInsertar) {
		List<Reserva> reservasProfesor = getReservasProfesorMes(aInsertar.getProfesor(),
				aInsertar.getPermanencia().getDia());
		float puntosProfesor = 0f;
		for (Reserva r : reservasProfesor) {
			puntosProfesor += r.getPuntos();
		}
		return puntosProfesor + aInsertar.getPuntos();
	}

	/**
	 * Obtiene las reservas correspondientes al profesor y d�a obtenidos por
	 * par�metro
	 * 
	 * @param reservador
	 *            el profesor a cuyo nombre est�n las reservas
	 * @param dia
	 *            la fecha de la que obtener el mes
	 * @return una lista con las reservas correspondientes a ese profesor durante el
	 *         mes
	 */
	private List<Reserva> getReservasProfesorMes(Profesor reservador, LocalDate dia) {
		List<Reserva> devolver = new ArrayList<Reserva>();
		for (Reserva reserva : coleccionReservas) {
			if (reserva.getProfesor().equals(reservador)
					&& reserva.getPermanencia().getDia().getMonthValue() == dia.getMonthValue()
					&& reserva.getPermanencia().getDia().getYear() == dia.getYear())
				devolver.add(reserva);
		}
		return devolver;
	}

	/*
	 * //ESTE M�TODO NO SE EN QUE UTILIZARLO private Reserva getReservaDia(LocalDate
	 * dia) { for(Reserva reserva : coleccionReservas) {
	 * if(reserva.getPermanencia().getDia().equals(dia)) return new
	 * Reserva(reserva); } return null; }
	 */

	/**
	 * Busca una reserva en la colecci�n
	 * 
	 * @param reserva
	 *            la reserva a buscar
	 * @return la reserva buscada o null si no la encuentra
	 */
	public Reserva buscar(Reserva reserva) {
		if (reserva == null)
			return null;
		if (this.coleccionReservas.indexOf(reserva) == -1)
			return null;
		return this.coleccionReservas.get(this.coleccionReservas.indexOf(reserva));
	}

	/**
	 * Borra una reserva de la colecci�n
	 * 
	 * @param reserva
	 *            la reserva a borrar
	 * @throws IllegalArgumentException
	 *             si la reserva es nula
	 * @throws OperationNotSupportedException
	 *             si la reserva no existe
	 */
	public void borrar(Reserva reserva) throws OperationNotSupportedException, IllegalArgumentException {
		if (reserva == null)
			throw new IllegalArgumentException("No se puede anular una reserva nula.");
		// Si no consigue eliminar la reserva lanza la excepci�n
		if (!this.coleccionReservas.remove(reserva))
			throw new OperationNotSupportedException("La reserva a anular no existe.");
	}

	/**
	 * Obtiene las salidas de todas las reservas de la colecci�n
	 * 
	 * @return la salida de las reservas
	 */
	public List<String> representar() {
		List<String> representar = new ArrayList<String>();
		for (Reserva r : this.coleccionReservas)
			representar.add(r.toString());
		return representar;
	}

	/**
	 * Obtiene las reservas a nombre de un profesor indicado
	 * 
	 * @param profesor
	 *            el profesor que ha reservado
	 * @return las reservas del profesor
	 * @throws IllegalArgumentException
	 *             si el profesor es nulo
	 */
	public List<Reserva> getReservasProfesor(Profesor profesor) throws IllegalArgumentException {
		if (profesor == null)
			throw new IllegalArgumentException("No se pueden comprobar las reservas de un profesor nulo.");
		List<Reserva> devolver = new ArrayList<Reserva>();
		for (Reserva r : this.coleccionReservas) {
			if (r.getProfesor().equals(profesor))
				devolver.add(new Reserva(r));
		}
		return devolver;
	}

	/**
	 * Obtiene las reservas realizadas a un aula indicada
	 * 
	 * @param aula
	 *            el aula reservada
	 * @return las reservas del aula
	 * @throws IllegalArgumentException
	 *             si el aula es nula
	 */
	public List<Reserva> getReservasAula(Aula aula) throws IllegalArgumentException {
		if (aula == null)
			throw new IllegalArgumentException("No se pueden comprobar las reservas realizadas sobre un aula nula.");
		List<Reserva> devolver = new ArrayList<Reserva>();
		for (Reserva r : this.coleccionReservas) {
			if (r.getAula().equals(aula))
				devolver.add(new Reserva(r));
		}
		return devolver;
	}

	/**
	 * Obtiene las reservas realizadas en una fecha y tramo concretos
	 * 
	 * @param permanencia
	 *            la fecha y el tramo de las reservas
	 * @return las reservas de esa fecha y tramo
	 * @throws IllegalArgumentException
	 *             si la permanencia es nula
	 */
	public List<Reserva> getReservasPermanencia(Permanencia permanencia) throws IllegalArgumentException {
		if (permanencia == null)
			throw new IllegalArgumentException("No se pueden consultar las reservas de una permanencia nula.");
		List<Reserva> devolver = new ArrayList<Reserva>();
		for (Reserva r : this.coleccionReservas) {
			if (r.getPermanencia().equals(permanencia))
				devolver.add(new Reserva(r));
		}
		return devolver;
	}

	/**
	 * Comprueba si un aula est� disponible en una fecha y tramos indicados
	 * 
	 * @param aula
	 *            el aula a comprobar
	 * @param permanencia
	 *            la fecha y tramo en las que comprobar el aula
	 * @return True si est� disponible, False si est� reservada
	 * @throws IllegalArgumentException
	 *             si el aula o la permanencia son nulas, o si el tipo de
	 *             permanencia consultado no coincide con el de las reservas
	 *             realizadas sobre ese aula ese d�a
	 */
	public boolean consultarDisponibilidad(Aula aula, Permanencia permanencia) throws IllegalArgumentException {
		if (aula == null)
			throw new IllegalArgumentException("No se puede consultar la disponibilidad de un aula nula.");
		if (permanencia == null)
			throw new IllegalArgumentException("No se puede consultar la disponibilidad de una permanencia nula.");

		// Diferenciamos el tipo de permanencia
		for (Reserva r : this.coleccionReservas) {
			if (r.getAula().equals(aula) && r.getPermanencia().getDia().equals(permanencia.getDia())) {
				if (r.getPermanencia() instanceof PermanenciaPorTramo && permanencia instanceof PermanenciaPorHora)
					throw new IllegalArgumentException("Las reservas realizadas para ese aula y d�a son por tramo.");
				if (r.getPermanencia() instanceof PermanenciaPorHora && permanencia instanceof PermanenciaPorTramo)
					throw new IllegalArgumentException("Las reservas realizadas para ese aula y d�a son por hora.");
				if (r.getPermanencia().equals(permanencia))
					return false;
			}
		}
		return true;
	}

}
