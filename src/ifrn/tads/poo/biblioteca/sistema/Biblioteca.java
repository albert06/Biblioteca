package ifrn.tads.poo.biblioteca.sistema;
import java.util.*;

import ifrn.tads.poo.biblioteca.usuarios.Administrador;
import ifrn.tads.poo.biblioteca.usuarios.Usuario;
import irfn.tads.poo.biblioteca.materialdeleitura.Apostila;
import irfn.tads.poo.biblioteca.materialdeleitura.ItemAcervo;
import irfn.tads.poo.biblioteca.materialdeleitura.Livro;
import irfn.tads.poo.biblioteca.materialdeleitura.Texto;


public class Biblioteca {
String nomeBiblioteca;
	
	Map<Livro, Integer> livros;
	Map<Apostila, Integer> apostilas;
	Map<Texto, Integer> textos;
	ArrayList<Administrador> admins;
	ArrayList<Usuario> clientes;	
	
	public Biblioteca(String nome){
		this.nomeBiblioteca = nome;
		this.livros = new HashMap<>();
		this.apostilas = new HashMap<>();   // construtor da biblioteca, instancia as listagens;
		this.textos = new HashMap<>();
		this.admins = new ArrayList<Administrador>();
		this.clientes = new ArrayList<Usuario>();
	}
	
	public int verificarCPF(String cpf){
		int valido = 0;		
		if(cpf.length() == 11){
			valido = 0;          //Verifica o tamanho do cpf
		}else
			valido = 1;		
		return valido;
	}
	public Usuario[] listUsu(){
		Usuario listUsu[] = new Usuario[this.clientes.size()];
		return this.livros.keySet().toArray(listUsu);
	}
	public int veriAdm(Administrador adm){
		int retorno = 0,i;
		Administrador[] listAdm = new Administrador[this.admins.size()];
		this.admins.toArray(listAdm);
		for(i = 0; i < (listAdm.length / 2); i++){
			if((adm.getNome() == listAdm[i].getNome()) & (adm.getCPF() == listAdm[i].getCPF())){
				retorno = 1;
				break;
			}else if(adm.getCPF() == listAdm[i].getCPF()){
				retorno = 2;
				break;
			}else if((adm.getNome() == listAdm[(listAdm.length - 1) - i].getNome()) & (adm.getCPF() == listAdm[(listAdm.length - 1) - i].getCPF())){
				retorno = 1;
				break;
			}else if(adm.getCPF() == listAdm[(listAdm.length - 1) - i].getCPF()){
				retorno = 2;
				break;
			}
		}
		return retorno;
	}
	public Usuario buscaUsuario(int cod){
		int indice = -1,i = 0;		
		Usuario[] listUsu = new Usuario[this.clientes.size()];
		listUsu = listUsu();
		while(i < listUsu.length){
			if(listUsu[i].getCod() == cod){
				indice = i;				
				break;
			}
			i++;
		}
		return listUsu[indice];		
	}
	public boolean verificarISBN(String num){
		boolean verdade  = false, repetido = false;
		int i = 0;
		Livro[] lista = new Livro[this.livros.size()];
		lista = listLivros();		
		while(i++<lista.length){
			if(lista[i].getISBN() == num){
				repetido = true;		
			}			
		}		
		if(num.length() == 13){			
			if((num.charAt(0) == '9') & (num.charAt(1) == '7') & (num.charAt(2) == '8') & (repetido == false)){				
				verdade  = true;
			}			
		}
		return verdade;
	}
	public int qtdUmaApo(Apostila apo){
		if(this.apostilas.containsKey(apo))
			return this.apostilas.get(apo);
		return -1;
	}
	public int qtdUmTxt(Texto txt){
		if(this.textos.containsKey(txt))
			return this.textos.get(txt);
		return -1;
	}
	public int qtdApo(){
		return this.apostilas.size();
	}
	public int qtdTxt(){
		return this.textos.size();
	}
	public Texto[] listTxt(){
		Texto[] listTxt = new Texto[qtdApo()];
		return this.textos.keySet().toArray(listTxt);
	}
	public Livro[] listLivros(){
		Livro listLi[] = new Livro[tamLivros()];
		return this.livros.keySet().toArray(listLi);
	}
	public Apostila[] listApostila(){
		Apostila listApo[] = new Apostila[qtdApo()];
		return this.apostilas.keySet().toArray(listApo);
	}
	public int tamLivros(){
		return this.livros.size();
	}
	public int qtdDoLivro(Livro livro){
		if(this.livros.containsKey(livro)){
			return this.livros.get(livro);
		}
		return -1;
	}
	public void adicionarLivro(Livro novo, Integer qtd){
		if(this.livros.containsKey(novo)){
			this.livros.put(novo, new Integer(this.livros.get(qtd).intValue() + 1));
		}
		this.livros.put(novo, qtd);
	}

	public void alugar(ItemAcervo alugado,Date dataAluguel, int prazo, int cod, Scanner sc){
		
		Usuario vaiAlugar = null;
		String pago = null;
		System.out.println("Você quer pagar? S/N");
		if(sc.hasNext())
			pago = sc.next();
		if((pago == "s") || (pago == "S"))
			alugado.setPago(true);		
	}
	public void alugar(ItemAcervo alugado,Date dataAluguel, int prazo, int cod){
		Usuario vaiAlugar = null;

		alugado.setDataAluguel(dataAluguel);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataAluguel);
		cal.add(Calendar.DAY_OF_MONTH, prazo);
		alugado.setDataDevolucao(cal.getTime());		
		vaiAlugar = buscaUsuario(cod);
		vaiAlugar.addAlugado(alugado);
		if(alugado instanceof Livro)
			this.livros.put((Livro)alugado, new Integer(this.livros.get((Livro)alugado).intValue() - 1));
		else if(alugado instanceof Texto)
			this.textos.put((Texto)alugado, new Integer(this.textos.get((Texto)alugado).intValue() - 1));

		else if(alugado instanceof Apostila)
			this.apostilas.put((Apostila)alugado, new Integer(this.apostilas.get((Apostila)alugado).intValue() - 1));
		
		
	}
	public double CalcDias(Calendar dataEnt, Calendar dataLim){
		int dias = 1;

		while(dataLim.compareTo(dataEnt)== -1 ){
			
			dataLim.set(Calendar.DAY_OF_MONTH,dataLim.get(Calendar.DAY_OF_MONTH)+1);
			dias++;
		}
		
		return dias;
		

		
		

	}
	public int veriUsu(Usuario usu){
		int valor = 0,i;		
		Usuario[] liUsu = listUsu();
		this.clientes.toArray(liUsu);
		for(i = 0; i < (liUsu.length / 2); i++){
			if((usu.getNome() == liUsu[i].getNome()) & (usu.getCPF() == liUsu[i].getCPF())){
				valor = 1;
				break;
			}else if(usu.getCPF() == liUsu[i].getCPF()){
				valor = 2;
				break;
			}else if((usu.getNome() == liUsu[(liUsu.length - 1) - i].getNome()) & (usu.getCPF() == liUsu[(liUsu.length - 1) - i].getCPF())){
				valor = 1;
				break;
			}else if(usu.getCPF() == liUsu[(liUsu.length - 1) - i].getCPF()){
				valor = 2;

				break;
			}
		}
		return valor;
	}
	public void adicionarApostila(Apostila nova, Integer qtd){
		if(this.apostilas.containsKey(nova)){
			this.apostilas.put(nova, new Integer(this.apostilas.get(qtd).intValue() + 1));
		}
		this.apostilas.put(nova, qtd);
	}
	public void adicionarTexto(Texto novo, Integer qtd){
		if(this.textos.containsKey(novo)){
			this.textos.put(novo, new Integer(this.textos.get(qtd).intValue() + 1));
		}
		this.textos.put(novo, qtd);
	}
	public void addUsuario(Usuario novo){
		clientes.add(novo);
	}
	public void addAdm(Administrador novo){
		admins.add(novo);
	}
}
