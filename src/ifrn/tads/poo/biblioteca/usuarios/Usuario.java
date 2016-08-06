package ifrn.tads.poo.biblioteca.usuarios;

import java.util.ArrayList;

import irfn.tads.poo.biblioteca.materialdeleitura.ItemAcervo;



public class Usuario {
	int codUsuario;
	String nome, endereco, cpf;
	ArrayList<ItemAcervo> alugados;
	ArrayList<ItemAcervo> reservados;
	public Usuario(int codUsuario, String nome, String endereco, String cpf) {
		this.codUsuario = codUsuario;
		this.nome = nome;
		this.endereco = endereco;
		this.cpf = cpf;
		this.alugados = new ArrayList<ItemAcervo>();
		this.reservados = new ArrayList<ItemAcervo>();
	}
	public void reservar(ItemAcervo item){
		reservados.add(item);
	}
	public void retirarReservado(ItemAcervo item){
		reservados.remove(item);
	}
	public String getCPF(){
		return this.cpf;
	}
	public String getNome(){
		return this.nome;
	}
	public int getCod(){
		return this.codUsuario;
	}
	public void addAlugado(ItemAcervo item){
		this.alugados.add(item);
	}
	public void devolver(ItemAcervo item){
		this.alugados.remove(item);
	}
	public ItemAcervo[] listaAlugados(){
		ItemAcervo[] listAlu = new ItemAcervo[this.alugados.size()];
		listAlu = this.alugados.toArray(listAlu);
		return listAlu;
	}
	public int qtdAlugados(){
		return this.alugados.size();
	}
}

