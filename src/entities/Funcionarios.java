package entities;

public class Funcionarios extends Pessoa {
    private int id;
    private String matricula;
    private String departamento;
    private int pessoaId; // Chave estrangeira

    public Funcionarios() {
    	
    }
    
    public Funcionarios(int id, String matricula, String departamento, int pessoaId) {
        this.id = id;
        this.matricula = matricula;
        this.departamento = departamento;
        this.pessoaId = pessoaId;
    }

    // Getters e Setters
    public int getId(){ 
    	return id; 
    }
    
    public void setId(int id){ 
    	this.id = id; 
    }
    
    public String getMatricula(){ 
    	return matricula; 
    }
    
    public void setMatricula(String matricula){ 
    	this.matricula = matricula; 
    }
    
    public String getDepartamento(){ 
    	return departamento; 
    }
    
    public void setDepartamento(String departamento){ 
    	this.departamento = departamento; 
    }
    
    public int getPessoaId(){ 
    	return pessoaId; 
    }
    
    public void setPessoaId(int pessoaId){ 
    	this.pessoaId = pessoaId; 
    }

    @Override
    public String toString() {
        return "Funcionario id=" + id + ", matricula='" + matricula + "', departamento='" + departamento + "', pessoaId=" + pessoaId;
    }
}