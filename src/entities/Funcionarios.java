package entities;

public class Funcionarios {
	private int idFuncionario;
	private String matricula;
	private String departamento;
	private Pessoa pessoa;
	
	public Funcionarios() {}
	
	public Funcionarios(int idFuncionario, String matricula, String departamento, Pessoa pessoa) {
        this.idFuncionario = idFuncionario;
        this.matricula = matricula;
        this.departamento = departamento;
        this.pessoa = pessoa;
    }

	public int getId() {
		return idFuncionario;
	}

	public void setId(int id) {
		this.idFuncionario = id;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	
	
}
