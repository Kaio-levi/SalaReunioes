package conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entities.Funcionarios;
import entities.Pessoa;
import entities.Projeto;

public class DatabaseConnection {
	private static final String URL = "jdbc:mysql://localhost/mydb?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Método para obter conexão
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // criar pessoa
    public void createPessoa(Pessoa pessoa) throws SQLException {
        String sql = "INSERT INTO Pessoa (Nome, Email) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getEmail());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pessoa.setId(rs.getInt(1));
                }
            }
            System.out.println("Pessoa cadastrada com sucesso.");
        }
    }

    //listar pessoa
    public List<Pessoa> readPessoas() throws SQLException {
        List<Pessoa> pessoas = new ArrayList<>();
        String sql = "SELECT * FROM Pessoa";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pessoa p = new Pessoa(rs.getInt("idPessoa"), rs.getString("Nome"), rs.getString("Email"));
                pessoas.add(p);
            }
        }
        return pessoas;
    }
    
    //atualizar pessoa 
    public void updatePessoa(Pessoa pessoa) throws SQLException {
        String sql = "UPDATE Pessoa SET Nome = ?, Email = ? WHERE idPessoa = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getEmail());
            stmt.setInt(3, pessoa.getId());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Pessoa atualizada com sucesso.");
            } else {
                System.out.println("Erro: Pessoa não encontrada para atualização.");
            }
        }
    }
    
    //deletar pessoa
    public void deletePessoa(int id) throws SQLException {
        String sql = "DELETE FROM Pessoa WHERE idPessoa = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Pessoa excluída com sucesso.");
            } else {
                System.out.println("Erro: Pessoa não encontrada para exclusão.");
            }
        }
    }

    // criar funcionario
    public void createFuncionario(Funcionarios funcionarios) throws SQLException {
        if (pessoaExists(funcionarios.getPessoaId())) {
            String sql = "INSERT INTO Funcionario (Matricula, Departamento, Pessoa_idPessoa) VALUES (?, ?, ?)";
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, funcionarios.getMatricula());
                stmt.setString(2, funcionarios.getDepartamento());
                stmt.setInt(3, funcionarios.getPessoaId());
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        funcionarios.setId(rs.getInt(1));
                    }
                }
                System.out.println("Funcionário cadastrado com sucesso.");
            }
        } else {
            System.out.println("Erro: ID da Pessoa não existe para cadastrar Funcionário.");
        }
    }
    
    //listar funcionario
    public List<Funcionarios> readFuncionarios() throws SQLException {
        List<Funcionarios> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM Funcionario";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Funcionarios f = new Funcionarios(rs.getInt("idFuncionarios"), rs.getString("Matricula"), rs.getString("Departamento"), rs.getInt("Pessoa_idPessoa"));
                funcionarios.add(f);
            }
        }
        return funcionarios;
    }
    //atualizar Funcionario
    public void updateFuncionario(Funcionarios funcionario) throws SQLException {
        String sql = "UPDATE Funcionario SET Matricula = ?, Departamento = ?, Pessoa_idPessoa = ? WHERE idPessoa = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getMatricula());
            stmt.setString(2, funcionario.getDepartamento());
            stmt.setInt(3, funcionario.getPessoaId());
            stmt.setInt(4, funcionario.getId());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Funcionário atualizado com sucesso.");
            } else {
                System.out.println("Erro: Funcionário não encontrado para atualização.");
            }
        }
    }
    //deletar funcionario
    public void deleteFuncionario(int id) throws SQLException {
        if (!funcionarioHasProjects(id)) {
            String sql = "DELETE FROM Funcionario WHERE idFuncionario = ?";
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("Funcionário excluído com sucesso.");
                } else {
                    System.out.println("Erro: Funcionário não encontrado para exclusão.");
                }
            }
        } else {
            System.out.println("Erro: Não é possível excluir funcionário vinculado a projetos.");
        }
    }

    // criar projeto intragado ao funcionario
    public void createProjeto(Projeto projeto) throws SQLException {
        if (funcionarioExists(projeto.getFuncionarioId())) {
            String sql = "INSERT INTO Projeto (Nome, Descricao, Funcionario_idFuncionario) VALUES (?, ?, ?)";
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, projeto.getNome());
                stmt.setString(2, projeto.getDescricao());
                stmt.setInt(3, projeto.getFuncionarioId());
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        projeto.setIdProjeto(rs.getInt(1));
                    }
                }
                System.out.println("Projeto cadastrado com sucesso.");
            }
        } else {
            System.out.println("Erro: Funcionário não existe para vincular ao Projeto.");
        }
    }
    //listar projeto
    public List<Projeto> readProjetos() throws SQLException {
        List<Projeto> projetos = new ArrayList<>();
        String sql = "SELECT * FROM Projeto";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Projeto p = new Projeto(rs.getInt("idProjeto"), rs.getString("Nome"), rs.getString("Descricao"), rs.getInt("Funcionario_idFuncionario"));
                projetos.add(p);
            }
        }
        return projetos;
    }
    //atualiza projeto
    public void updateProjeto(Projeto projeto) throws SQLException {
        String sql = "UPDATE Projeto SET Nome = ?, Descricao = ?, Funcionario_idFuncionario = ? WHERE idProjeto = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setInt(3, projeto.getFuncionarioId());
            stmt.setInt(4, projeto.getIdProjeto());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Projeto atualizado com sucesso.");
            } else {
                System.out.println("Erro: Projeto não encontrado para atualização.");
            }
        }
    }
    //deleta projeto
    public void deleteProjeto(int id) throws SQLException {
        String sql = "DELETE FROM Projeto WHERE idProjeto = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Projeto excluído com sucesso.");
            } else {
                System.out.println("Erro: Projeto não encontrado para exclusão.");
            }
        }
    }

    // regras de negócio
    
    //verifica se a pessoa existe
    public boolean pessoaExists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Pessoa WHERE idPessoa = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    //verifica se funcionario existe
    public boolean funcionarioExists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Funcionario WHERE idFuncionario = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    //verifica se o funcionario esta vinculado a um projeto
    public boolean funcionarioHasProjects(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Projeto WHERE Funcionario_idFuncionairo = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}