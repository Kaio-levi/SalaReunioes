package conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entities.Funcionarios;
import entities.Pessoa;
import entities.Projeto;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost/empresa?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Método para obter conexão
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // CRUD para Pessoa
    public void createPessoa(Pessoa pessoa) throws SQLException {
        String sql = "INSERT INTO Pessoa (nome, email) VALUES (?, ?)";
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

    public List<Pessoa> readPessoas() throws SQLException {
        List<Pessoa> pessoas = new ArrayList<>();
        String sql = "SELECT * FROM Pessoa";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pessoa p = new Pessoa(rs.getInt("id"), rs.getString("nome"), rs.getString("email"));
                pessoas.add(p);
            }
        }
        return pessoas;
    }

    public void updatePessoa(Pessoa pessoa) throws SQLException {
        String sql = "UPDATE Pessoa SET nome = ?, email = ? WHERE id = ?";
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

    public void deletePessoa(int id) throws SQLException {
        String sql = "DELETE FROM Pessoa WHERE id = ?";
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

    // CRUD para Funcionario (com regras de negócio)
    public void createFuncionario(Funcionarios funcionarios) throws SQLException {
        if (pessoaExists(funcionarios.getPessoaId())) {
            String sql = "INSERT INTO Funcionario (matricula, departamento, Pessoa_id) VALUES (?, ?, ?)";
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

    public List<Funcionarios> readFuncionarios() throws SQLException {
        List<Funcionarios> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM Funcionario";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Funcionarios f = new Funcionarios(rs.getInt("id"), rs.getString("matricula"), rs.getString("departamento"), rs.getInt("Pessoa_id"));
                funcionarios.add(f);
            }
        }
        return funcionarios;
    }

    public void updateFuncionario(Funcionarios funcionario) throws SQLException {
        String sql = "UPDATE Funcionario SET matricula = ?, departamento = ?, Pessoa_id = ? WHERE id = ?";
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

    public void deleteFuncionario(int id) throws SQLException {
        if (!funcionarioHasProjects(id)) {
            String sql = "DELETE FROM Funcionario WHERE id = ?";
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

    // CRUD para Projeto (com regras de negócio)
    public void createProjeto(Projeto projeto) throws SQLException {
        if (funcionarioExists(projeto.getFuncionarioId())) {
            String sql = "INSERT INTO Projeto (nome, descricao, Funcionario_id) VALUES (?, ?, ?)";
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

    public List<Projeto> readProjetos() throws SQLException {
        List<Projeto> projetos = new ArrayList<>();
        String sql = "SELECT * FROM Projeto";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Projeto p = new Projeto(rs.getInt("id"), rs.getString("nome"), rs.getString("descricao"), rs.getInt("Funcionario_id"));
                projetos.add(p);
            }
        }
        return projetos;
    }

    public void updateProjeto(Projeto projeto) throws SQLException {
        String sql = "UPDATE Projeto SET nome = ?, descricao = ?, Funcionario_id = ? WHERE id = ?";
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

    public void deleteProjeto(int id) throws SQLException {
        String sql = "DELETE FROM Projeto WHERE id = ?";
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

    // Métodos auxiliares para regras de negócio
    public boolean pessoaExists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Pessoa WHERE id = ?";
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

    public boolean funcionarioExists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Funcionario WHERE id = ?";
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

    public boolean funcionarioHasProjects(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Projeto WHERE Funcionario_id = ?";
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