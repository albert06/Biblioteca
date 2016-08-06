package ifrn.tads.poo.biblioteca.usuarios;

public class FalhaNoCadastroException extends Exception{
	public FalhaNoCadastroException(){
		super("O cadastro não pode ser realizado!");
	}
}
