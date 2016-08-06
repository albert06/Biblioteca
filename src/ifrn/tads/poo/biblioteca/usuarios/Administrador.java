package ifrn.tads.poo.biblioteca.usuarios;



public class Administrador extends Usuario{
	String senha;
	public Administrador(int codUsuario, String nome, String endereco, String cpf, String senha) {
		super(codUsuario, nome, endereco, cpf);
		this.senha = senha;
	}
	public String getSenha(){
		return this.senha;
	}
}
