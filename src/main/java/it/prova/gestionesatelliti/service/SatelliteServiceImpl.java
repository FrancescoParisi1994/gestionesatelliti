package it.prova.gestionesatelliti.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.repository.SatelliteRepository;

@Service
public class SatelliteServiceImpl implements SatelliteService {
	
	@Autowired
	private SatelliteRepository satelliteRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> findByExample(Satellite satellite) {
		Specification<Satellite> specificationCriteria = (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (StringUtils.isNotEmpty(satellite.getDenominazione()))
				predicates.add(cb.like(cb.upper(root.get("denominazione")), "%" + satellite.getDenominazione().toUpperCase() + "%"));

			if (StringUtils.isNotEmpty(satellite.getCodice()))
				predicates.add(cb.like(cb.upper(root.get("codice")), "%" + satellite.getCodice().toUpperCase() + "%"));

			if (satellite.getDataLancio() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("datalancio"), satellite.getDataLancio()));

			if (satellite.getDataRientro() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("datarientro"), satellite.getDataRientro()));

			if (satellite.getStato() != null)
				predicates.add(cb.equal(root.get("stato"), satellite.getStato()));


			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};

		return satelliteRepository.findAll(specificationCriteria);

	}

	@Override
	@Transactional
	public void inserisciNuovo(Satellite satellite) {
		satelliteRepository.save(satellite);
	}
	
	@Transactional(readOnly = true)
	public List<Satellite> listAllElements() {
		return (List<Satellite>) satelliteRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Satellite caricaSingoloElemento(Long idLong) {
		return satelliteRepository.findById(idLong).orElse(null);
	}

	@Override
	@Transactional
	public void rimuovi(Long idLong) {
		satelliteRepository.deleteById(idLong);
	}

	@Override
	@Transactional
	public void aggiorna(Satellite satellite) {
		satelliteRepository.save(satellite);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> trovaSatellitiConStatoDisattivatoELanciatiDaPiuDi2Anni() {
		Date date=new Date();
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, -2);
		Date adessoDate=calendar.getTime();
		
		return satelliteRepository.findByStato_IsNotDISATTIVATOAndDataLancioLessThan(adessoDate);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> trovaIDisattivatiMaNonRientrati() {
		return satelliteRepository.findByStato_DISATTIVATOAndDataRientroIsNull();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> trovaSatellitiConStatoFissoELanciatiDaPiuDi10Anni() {
		Date date=new Date();
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, -10);
		Date adessoDate=calendar.getTime();
		return satelliteRepository.findByStato_FISSOAndDataLancioLessThan(adessoDate);
	}

	
}
