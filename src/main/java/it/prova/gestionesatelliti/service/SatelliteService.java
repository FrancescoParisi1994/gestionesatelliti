package it.prova.gestionesatelliti.service;

import java.util.Date;
import java.util.List;

import it.prova.gestionesatelliti.model.Satellite;

public interface SatelliteService {

	public List<Satellite> findByExample(Satellite satellite);
	
	public void inserisciNuovo(Satellite satellite);
	
	public List<Satellite> listAllElements();
	
	public Satellite caricaSingoloElemento(Long idLong);
	
	public void rimuovi(Long idLong);
	
	public void aggiorna(Satellite satellite);
	
	public List<Satellite> trovaSatellitiConStatoDisattivatoELanciatiDaPiuDi2Anni();
	
	public List<Satellite> trovaIDisattivatiMaNonRientrati();
	
	public List<Satellite> trovaSatellitiConStatoFissoELanciatiDaPiuDi10Anni();
}
