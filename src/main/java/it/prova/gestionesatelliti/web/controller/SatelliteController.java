package it.prova.gestionesatelliti.web.controller;

import java.text.ParseException;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;
import it.prova.gestionesatelliti.service.SatelliteService;

@Controller
@RequestMapping(value = "/satellite")
public class SatelliteController {

	@Autowired
	private SatelliteService satelliteService;
	
	@GetMapping
	public ModelAndView listAll() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.listAllElements();
		Date date=new Date();
		mv.addObject("now_attr", date);
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}
	
	@GetMapping("/search")
	public String search() {
		return "satellite/search";
	}
	
	@PostMapping("/list")
	public String listByExample(Satellite satellite, ModelMap model) {
		List<Satellite> result= satelliteService.findByExample(satellite);
		Date date=new Date();
		model.addAttribute("now_attr", date);
		model.addAttribute("satellite_list_attribute", result);
		return "satellite/list";
	}
	
	@GetMapping("/insert")
	public String create(Model model) {
		model.addAttribute("insert_satellite_attr", new Satellite());
		return "satellite/insert";
	}
	
	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("insert_satellite_attr") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttrs) throws ParseException {

		Date date=new Date();
		/*if (result.hasErrors()
				||(satellite.getDataRientro()!=null&&satellite.getDataLancio()!=null&&satellite.getDataLancio().after(satellite.getDataRientro())
				||(satellite.getDataLancio()!=null&&date.after(satellite.getDataLancio())&&satellite.getStato()==null)))
			return "satellite/insert";
		*/
		satelliteService.inserisciNuovo(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}

	@GetMapping("/show/{idSatellite}")
	public String show(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("show_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/show";
	}
	
	@GetMapping("/delete/{idSatellite}")
	public String delete(@PathVariable(required = true) Long idSatellite,Model model) {
		model.addAttribute("delete_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/delete";
	}
	
	@PostMapping("/executeDelete")
	public String executeDelete(@RequestParam(required = true) Long idSatellite,RedirectAttributes redirectAttrs) {
		
		Satellite satellite=satelliteService.caricaSingoloElemento(idSatellite);
		Date date=new Date();
		if ((satellite.getDataLancio()!=null&&date.after(satellite.getDataLancio()))||(satellite.getDataRientro()!=null&&satellite.getDataRientro().after(date))) {
			redirectAttrs.addFlashAttribute("successMessage", "Operazione Fallita");
			return "redirect:/satellite";
		}
		
		satelliteService.rimuovi(idSatellite);
		
		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}
	
	@GetMapping("/preUpdate/{idSatellite}")
	public String preUpdate(@PathVariable(required = true) Long idSatellite,Model model) {
		model.addAttribute("update_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/update";
	}
	
	@PostMapping("/update")
	public String update(@Valid @ModelAttribute("update_satellite_attr") Satellite satellite, BindingResult result,RedirectAttributes redirectAttrs) {
		
		Date date=new Date();
		if (result.hasErrors())
			return "satellite/update";
		/*if (result.hasErrors()&&satellite.getDataLancio()!=null&&satellite.getDataLancio().before(date)) {
			
		}*/
		
		satelliteService.aggiorna(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}
	
	@PostMapping("/lancio")
	public String lancio(@PathVariable(required = true) Long lancio_attr,RedirectAttributes redirectAttrs) {
		
		Satellite satellite=satelliteService.caricaSingoloElemento(lancio_attr);
		satellite.setDataRientro(new Date());
		satellite.setStato(StatoSatellite.IN_MOVIMENTO);
		
		satelliteService.aggiorna(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}
	
	@PostMapping("/rientro")
	public String rientro(@PathVariable(required = true) Long rientro_attr,RedirectAttributes redirectAttrs) {
		
		Satellite satellite=satelliteService.caricaSingoloElemento(rientro_attr);
		satellite.setDataRientro(new Date());
		satellite.setStato(StatoSatellite.DISATTIVATO);
		
		satelliteService.aggiorna(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}
	
	@GetMapping("/cercaPiu2")
	public ModelAndView trovaLanciatiDaPiuDi2AnniAttivi() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.trovaSatellitiConStatoDisattivatoELanciatiDaPiuDi2Anni();
		mv.addObject("satellite_list_attribute", results);
		Date date=new Date();
		mv.addObject("now_attr", date);
		mv.setViewName("satellite/list");
		return mv;
	}
	
	@GetMapping("/cercaDisattivatiNonRientrati")
	public ModelAndView trovaDisattivatiMaNonRientrati() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.trovaIDisattivatiMaNonRientrati();
		mv.addObject("satellite_list_attribute", results);
		Date date=new Date();
		mv.addObject("now_attr", date);
		mv.setViewName("satellite/list");
		return mv;
	}
	
	@GetMapping("/cercaFissiPiu10")
	public ModelAndView trovaSatellitiLanciatiEFissiDaPiuDi10Anni() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.trovaIDisattivatiMaNonRientrati();
		mv.addObject("satellite_list_attribute", results);
		Date date=new Date();
		mv.addObject("now_attr", date);
		mv.setViewName("satellite/list");
		return mv;
	}
}
