package entities;

public class Projeto {
	//atributos
	private int id;
	private String nome;
	private String descricao;
	private int funcionarioId;
	
	//construtor padrao
	public Projeto() {}
	//construtor com as variaveis referenciadas
	public Projeto(int idProjeto, String nome, String descricao, int funcionarioId) {
		this.id = idProjeto;
		this.nome = nome;
		this.descricao = descricao;
		this.funcionarioId = funcionarioId;
	}
	//getters and setters
	public int getIdProjeto() {
		return id;
	}

	public void setIdProjeto(int idProjeto) {
		this.id = idProjeto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getFuncionarioId(){ 
		return funcionarioId; 
		}
	
    public void setFuncionarioId(int funcionarioId) { 
    	this.funcionarioId = funcionarioId;
    }
    
}    
