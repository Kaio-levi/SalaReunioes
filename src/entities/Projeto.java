package entities;

public class Projeto {
	private int idProjeto;
	private String nome;
	private String descricao;
	private Funcionarios funcionarios;
	
	public Projeto() {}

	public Projeto(int idProjeto, String nome, String descricao, Funcionarios funcionarios) {
		this.idProjeto = idProjeto;
		this.nome = nome;
		this.descricao = descricao;
		this.funcionarios = funcionarios;
	}

	public int getIdProjeto() {
		return idProjeto;
	}

	public void setIdProjeto(int idProjeto) {
		this.idProjeto = idProjeto;
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

	public Funcionarios getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(Funcionarios funcionarios) {
		this.funcionarios = funcionarios;
	}
}
