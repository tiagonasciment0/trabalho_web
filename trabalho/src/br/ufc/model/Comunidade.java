package br.ufc.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.swing.event.ListSelectionEvent;

@Entity(name="COMUNIDADE")
public class Comunidade {
	
	
	@Id
	@Column(name="COMUNIDADE_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long comunidadeId;
	@Column(name="NOME")
	private String nome;
	
	@Column(name="IMAGEM")
	private String imagem;
	
	/*MANY_TO_MANY*/
	@ManyToMany
	@JoinTable(name="USUARIO_COMUNIDADE",
			joinColumns=@JoinColumn(name="COMUNIDADE_ID",referencedColumnName="COMUNIDADE_ID"),
			inverseJoinColumns=@JoinColumn(name="USUARIO_ID", referencedColumnName="USUARIO_ID"))
	private List<Usuario> usuarios;
	/*MANY_TO_MANY*/
	
	
	/*ONE_TO_MANY*/
	@OneToMany(mappedBy="comunidade", targetEntity=Forum.class, fetch=FetchType.EAGER)//tipo de busca
	private List<Forum> foruns;
	/*ONE_TO_MANY*/
	
	private String categoria;
	/*MANY_TO_ONE*/
	
	public Long getId() {
		return comunidadeId;
	}
	public void setId(Long id) {
		this.comunidadeId = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getImagem() {
		return imagem;
	}
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	public Long getComunidadeId() {
		return comunidadeId;
	}
	public void setComunidadeId(Long comunidadeId) {
		this.comunidadeId = comunidadeId;
	}
	
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	public void addUsuario(Usuario usuario) {
		this.usuarios.add(usuario);
	}
	
	public List<Forum> getForuns() {
		ArrayList<Forum> foruns = new ArrayList<Forum>();
		foruns.addAll(this.foruns);
		Collections.reverse(foruns);
		return foruns;
	}
	public void setForuns(List<Forum> foruns) {
		this.foruns = foruns;
	}
	
	public void addForum(Forum forum) {
		this.foruns.add(forum);
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	
}
