package ifrn.tads.poo.biblioteca.sistema;
import java.util.*;

import ifrn.tads.poo.biblioteca.usuarios.Administrador;
import ifrn.tads.poo.biblioteca.usuarios.FalhaNoCadastroException;
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
	// construtor da biblioteca, instancia as listagens;
	public Biblioteca(String nome){
		this.nomeBiblioteca = nome;
		this.livros = new HashMap<>();
		this.apostilas = new HashMap<>();   
		this.textos = new HashMap<>();
		this.admins = new ArrayList<Administrador>();
		this.clientes = new ArrayList<Usuario>();
	}
	//Verifica o tamanho do cpf
	public void verificarCPF(String cpf){
		if(cpf.length()	!=	11)	
			throw new IllegalArgumentException("CPF	deve	conter	11	dígitos");	
		for(int i	=	0;	i	<	cpf.length();	i++){	
			char	c	=	cpf.charAt(i);	
			if(!Character.isDigit(c))	
					throw new IllegalArgumentException("CPF	deve	conter	apenas dígitos");	
		}	
	}
	public int qtdUsu(){
		return this.clientes.size();
	}
	//devolve um array de usuarios
	public Usuario[] listUsu(){
		Usuario listUsu[] = new Usuario[this.clientes.size()];
		return this.clientes.toArray(listUsu);        
	}
	// verifica se o cpf e o nome do cadastro do adm ja existe
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
	// retorna um usuario baseado no código
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
	// verifica se o ISBN do livro é valido ou ja existe um igual cadastrado
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
	// quantidade de uma apostila 
	public int qtdUmaApo(Apostila apo){
		if(this.apostilas.containsKey(apo))
			return this.apostilas.get(apo);
		return -1;
	}
	// quantidade de um texto
	public int qtdUmTxt(Texto txt){
		if(this.textos.containsKey(txt))
			return this.textos.get(txt);
		return -1;
	}
	// quantidade de apostilas da biblioteca
	public int qtdApo(){
		return this.apostilas.size();
	}
	// quantidade de textos da biblioteca
	public int qtdTxt(){
		return this.textos.size();
	}
	// retorna um array de textos
	public Texto[] listTxt(){
		Texto[] listTxt = new Texto[qtdApo()];
		return this.textos.keySet().toArray(listTxt);
	}
	// retorna um array de livros
	public Livro[] listLivros(){
		Livro listLi[] = new Livro[tamLivros()];
		return this.livros.keySet().toArray(listLi);
	}
	// retorna um array de apostilas
	public Apostila[] listApostila(){
		Apostila listApo[] = new Apostila[qtdApo()];
		return this.apostilas.keySet().toArray(listApo);
	}
	// retorna a quantidade de livros cadastrados na biblioteca
	public int tamLivros(){
		return this.livros.size();
	}
	// quantidade individual de cada livro
	public int qtdDoLivro(Livro livro){
		if(this.livros.containsKey(livro)){
			return this.livros.get(livro);
		}
		return -1;
	}
	// adiciona um livro na biblioteca
	public void adicionarLivro(Livro novo, Integer qtd){
		if(this.livros.containsKey(novo)){
			this.livros.put(novo, new Integer(this.livros.get(qtd).intValue() + 1));
		}
		this.livros.put(novo, qtd);
	}
	// metodo para alugar livros, apostilas e textos
	
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
		if(veriReserva(alugado,vaiAlugar)){
			vaiAlugar.retirarReservado(alugado);
		}			
		
	}	
	// verifica se a senha apresentada pertence a algum adm
	public boolean loguinAdm(String senha){
		boolean cadastrado = false;
		Administrador admL[] = new Administrador[this.admins.size()];
		admL = this.admins.toArray(admL);
		for(int i = 0; i < admL.length ; i++){
			if(admL[i].getSenha().compareTo(senha) == 0)
				cadastrado = true;
		}
		return cadastrado;
	}
	//verifica se o item foi reservado
	public boolean veriReserva(ItemAcervo item, Usuario usu){
		boolean reservado = false;
		ItemAcervo lR [] = new ItemAcervo[usu.qtdReservados()];
		lR = usu.listaReservados();		
		int i;
		for(i = 0; i < lR.length ; i++){
			if(lR[i] == item)
				reservado = true;
		}
		return reservado;
		
	}
	// retira um item da lista de reservados do usuario 
	public void removerReservado(Usuario usu , ItemAcervo item){
		usu.retirarReservado(item);
	}
	// reserva livro, apostila ou texto
	public void reservar(Usuario usu, ItemAcervo reserva){
		if(reserva instanceof Livro){
			usu.reservar(reserva);
			this.livros.put((Livro)reserva, new Integer(this.livros.get((Livro)reserva).intValue() -1));
		}else if(reserva instanceof Apostila){
			usu.reservar(reserva);
			this.apostilas.put((Apostila)reserva, new Integer(this.apostilas.get((Apostila)reserva).intValue() -1));
		}else if(reserva instanceof Texto){
			usu.reservar(reserva);
			this.textos.put((Texto)reserva, new Integer (this.textos.get((Texto)reserva).intValue() -1));
		}
	}
	// calcula a quantidade de dias atrasados na entrega
	public double CalcDias(Calendar dataEnt, Calendar dataLim){
		int dias = 1;

		while(dataLim.compareTo(dataEnt)== -1 ){
			
			dataLim.set(Calendar.DAY_OF_MONTH,dataLim.get(Calendar.DAY_OF_MONTH)+1);
			dias++;
		}		
		return dias;
	}
	// verifica seu o nome ou cpf ja existe em algum cadastro
	public int veriUsu(Usuario usu){
		int valor = 0,i;		
		Usuario[] liUsu = new Usuario[this.clientes.size()]; 
		liUsu = listUsu();
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
	// adiciona uma apostila na biblioteca
	public void adicionarApostila(Apostila nova, Integer qtd){
		if(this.apostilas.containsKey(nova)){
			this.apostilas.put(nova, new Integer(this.apostilas.get(qtd).intValue() + 1));
		}
		this.apostilas.put(nova, qtd);
	}
	// adiciona um texto na biblioteca
	public void adicionarTexto(Texto novo, Integer qtd){
		if(this.textos.containsKey(novo)){
			this.textos.put(novo, new Integer(this.textos.get(qtd).intValue() + 1));
		}
		this.textos.put(novo, qtd);
	}
	// adiciona um usuario na listagem da biblioteca
	public void addUsuario(Usuario novo)throws FalhaNoCadastroException{
		verificarCPF(novo.getCPF());
		clientes.add(novo);
	}
	// adiciona um adm na listagem da biblioteca
	public void addAdm(Administrador novo) throws FalhaNoCadastroException{
		
		verificarCPF(novo.getCPF());
		admins.add(novo);
		
	}
}
