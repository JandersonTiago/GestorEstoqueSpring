package br.com.springboot.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.springboot.bo.ClienteBO;
import br.com.springboot.model.Cliente;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	private ClienteBO clienteBO;
	
	// Método para exibir o formulário de cadastro de cliente
	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public ModelAndView novo(ModelMap model) {
		model.addAttribute("cliente", new Cliente());
		return new ModelAndView("/cliente/formulario", model);
	}
	
	// Método para salvar um novo cliente ou atualizar um existente
	@RequestMapping(value = "", method=RequestMethod.POST)
	public String salva(@Valid @ModelAttribute Cliente cliente, BindingResult result, RedirectAttributes attr) {
		if (result.hasErrors())
			return "cliente/formulario";
		
		if (cliente.getId() == null) {
			clienteBO.insere(cliente);
			attr.addFlashAttribute("feedback", "O cliente foi cadastrado com sucesso");
		}
		else { 
			clienteBO.atualiza(cliente);
			attr.addFlashAttribute("feedback", "O cliente foi atualizado com sucesso");
		}
		return "redirect:/clientes";
	}
	
	// Método para listar todos os clientes
	@RequestMapping(value = "", method=RequestMethod.GET)
	public ModelAndView lista(ModelMap model) {
		model.addAttribute("clientes", clienteBO.listaTodos());
		return new ModelAndView("/cliente/lista", model);		
	}

	// Método para exibir o formulário de edição de um cliente
	@RequestMapping(value = "/edita/{id}", method = RequestMethod.GET)
	public ModelAndView edita(@PathVariable("id") Long id, ModelMap model) {
		try {
			model.addAttribute("cliente", clienteBO.pesquisaPeloId(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("/cliente/formulario", model);
	}
	
	// Método para inativar um cliente
	@RequestMapping(value = "/inativa/{id}", method = RequestMethod.GET)
	public String inativa(@PathVariable("id") Long id, RedirectAttributes attr) {
		System.out.println(id);
		try {
			Cliente cliente = clienteBO.pesquisaPeloId(id); 
			clienteBO.inativa(cliente);
			attr.addFlashAttribute("feedback", "O cliente foi inativado com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/clientes";
	}
	
	// Método para ativar um cliente
	@RequestMapping(value = "/ativa/{id}", method = RequestMethod.GET)
	public String ativa(@PathVariable("id") Long id, RedirectAttributes attr) {
		System.out.println(id);
		try {
			Cliente cliente = clienteBO.pesquisaPeloId(id); 
			clienteBO.ativa(cliente);
			attr.addFlashAttribute("feedback", "O cliente foi ativado com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/clientes";
	}
}
