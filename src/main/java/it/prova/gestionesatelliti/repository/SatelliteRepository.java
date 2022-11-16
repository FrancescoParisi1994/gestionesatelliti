package it.prova.gestionesatelliti.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.gestionesatelliti.model.Satellite;

public interface SatelliteRepository extends CrudRepository<Satellite, Long>,JpaSpecificationExecutor<Satellite> {

	@Query("from Satellite where stato != 'DISATTIVATO' and dataLancio < ?1")
	public List<Satellite> findByStato_IsNotDISATTIVATOAndDataLancioLessThan(Date date);
	
	@Query("from Satellite where stato = 'DISATTIVATO' and dataRientro = null")
	public List<Satellite> findByStato_DISATTIVATOAndDataRientroIsNull();
	
	@Query("from Satellite where stato = 'FISSO' and dataLancio < ?1")
	public List<Satellite> findByStato_FISSOAndDataLancioLessThan(Date date);
}
