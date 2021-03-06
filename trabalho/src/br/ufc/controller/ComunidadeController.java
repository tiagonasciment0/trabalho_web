package br.ufc.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.ufc.dao.IComunidadeDAO;
import br.ufc.dao.IForumDAO;
import br.ufc.dao.IMensagemDAO;
import br.ufc.dao.IUsuarioDAO;
import br.ufc.model.CategoriaEnum;
import br.ufc.model.Comunidade;
import br.ufc.model.Forum;
import br.ufc.model.Mensagem;
import br.ufc.model.Usuario;
import br.ufc.util.AulaFileUtil;

@Controller
@Transactional
public class ComunidadeController {

	@Autowired
	private IComunidadeDAO comunidadeDAO;

	@Autowired
	private IUsuarioDAO usuarioDAO;
	
	@Autowired
	private IForumDAO forumDAO;
	
	@Autowired
	private IMensagemDAO mensagemDAO;
	
	@Autowired
	private ServletContext context;
		
	@RequestMapping(value="/inserirComunidadeFormulario", method={RequestMethod.GET})//Quando só recarrega
	public String inserirComunidadeFormulario(Model model){//link para o formulário
		model.addAttribute("categorias", Arrays.asList(CategoriaEnum.values()));
		return "comunidades/inserir_comunidade_formulario";
	}
	
	@RequestMapping(value="/inserirComunidade", method={RequestMethod.POST})//inserir dados
	public String inserirComunidade(Comunidade comunidade, 
			@RequestParam(value="image", required=false)MultipartFile image){

		if(image!=null){
			String path = context.getRealPath("/");
			String relativePath = "resources/images/"+comunidade.getNome()+".png";
 			path+= relativePath;
			AulaFileUtil.saveFile(path, image);
			comunidade.setImagem(relativePath);
		}
		comunidadeDAO.inserir(comunidade);
		return "redirect:/listarComunidade";
	}
	
	//LISTAR
	@RequestMapping("/listarComunidade")
	public String listarComunidade(Model model){
		List<Comunidade> comunidades = comunidadeDAO.listar();
		model.addAttribute("comunidades", comunidades);
		return "comunidades/listar_comunidade";
	}
	
	@RequestMapping("/verComunidade/{idComunidade}")
	public String verComunidade(Model model, @PathVariable(value="idComunidade") Long idComunidade, HttpSession session){
		Usuario usuario =   usuarioDAO.recuperar(((Usuario)session.getAttribute("usuario_logado")).getId());
		Comunidade comunidade = comunidadeDAO.recuperar(idComunidade);
		Boolean participando = comunidade.getUsuarios().contains(usuario);
		model.addAttribute("comunidade", comunidade);
		model.addAttribute("usuario", usuario);
		model.addAttribute("participando", participando);
		return "comunidades/ver_comunidade";
	}
	
	@RequestMapping("/participarComunidade/{idComunidade}")
	public String participarComunidade(Model model, @PathVariable(value="idComunidade") Long idComunidade, HttpSession session){
		Comunidade comunidade = comunidadeDAO.recuperar(idComunidade);
		Usuario usuario = (Usuario)session.getAttribute("usuario_logado");
		comunidade.addUsuario(usuario);
		comunidadeDAO.alterar(comunidade);//altera a lista
		model.addAttribute("comunidade", comunidade);
		return "redirect:/verComunidade/" + comunidade.getId();
	}
	
	@RequestMapping("/adicionarForum/{idComunidade}")
	public String adicionarForum(Model model, @PathVariable(value="idComunidade") Long idComunidade, Forum forum, HttpSession session){
		Comunidade comunidade = comunidadeDAO.recuperar(idComunidade);
		forum.setComunidade(comunidade);
		forumDAO.inserir(forum);
		comunidade.addForum(forum);
		comunidadeDAO.alterar(comunidade);
		model.addAttribute("comunidade", comunidade);
		return "redirect:/verComunidade/" + comunidade.getId();
	}
	
	@RequestMapping("/verForum/{idComunidade}/{idForum}")
	public String verForum(Model model, @PathVariable(value="idComunidade") Long idComunidade, @PathVariable(value="idForum") Long idForum, HttpSession session){
		Comunidade comunidade = comunidadeDAO.recuperar(idComunidade);
		Forum forum = forumDAO.recuperar(idForum);

		model.addAttribute("comunidade", comunidade);
		model.addAttribute("forum", forum);
		return "comunidades/ver_forum";
	}
	
	@RequestMapping("/adicionarMensagem/{idComunidade}/{idForum}")
	public String adicionarForum(Model model, @PathVariable(value="idComunidade") Long idComunidade, @PathVariable(value="idForum") Long idForum, Mensagem mensagem ,HttpSession session){
		Usuario usuario =   usuarioDAO.recuperar(((Usuario)session.getAttribute("usuario_logado")).getId());
		Comunidade comunidade = comunidadeDAO.recuperar(idComunidade);
		Forum forum = forumDAO.recuperar(idForum);
		mensagem.setUsuario(usuario);
		mensagem.setForum(forum);		
		mensagemDAO.inserir(mensagem);
		forum.addMensagen(mensagem);
		forumDAO.alterar(forum);
		model.addAttribute("comunidade", comunidade);
		model.addAttribute("forum", forum);
		return "redirect:/verForum/" + comunidade.getId() + "/" + forum.getForumId();
	}
}
