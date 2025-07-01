package principal;

import java.sql.SQLException;

import conexao.DatabaseConnection;
import entities.Funcionarios;
import entities.Pessoa;
import entities.Projeto;

public class Principal {
    public static void main(String[] args) {
        DatabaseConnection dao = new DatabaseConnection();

        try {
            // Exemplo de CRUD para Pessoa
            Pessoa pessoa = new Pessoa(0, "João Silva", "joao@email.com");
            dao.createPessoa(pessoa);
            System.out.println("Pessoa cadastrada com sucesso: " + pessoa);

            // Exemplo de CRUD para Funcionario (verifica se Pessoa existe)
            Funcionarios funcionario = new Funcionarios(0, "F001", "TI", pessoa.getId());
            if (dao.pessoaExists(pessoa.getId())) {
                dao.createFuncionario(funcionario);
                System.out.println("Funcionário cadastrado com sucesso: " + funcionario);
            } else {
                System.out.println("Erro: ID da Pessoa não existe para cadastrar Funcionário.");
            }

            // Exemplo de CRUD para Projeto (vinculado a Funcionario)
            Projeto projeto = new Projeto(0, "Projeto X", "Desenvolvimento de software", funcionario.getId());
            if (dao.funcionarioExists(funcionario.getId())) {
                dao.createProjeto(projeto);
                System.out.println("Projeto cadastrado com sucesso: " + projeto);
            } else {
                System.out.println("Erro: Funcionário não existe para vincular ao Projeto.");
            }

            // Leitura de dados
            System.out.println("Pessoas cadastradas:");
            dao.readPessoas().forEach(System.out::println);
            System.out.println("Funcionários cadastrados:");
            dao.readFuncionarios().forEach(System.out::println);
            System.out.println("Projetos cadastrados:");
            dao.readProjetos().forEach(System.out::println);

            // Atualização
            pessoa.setEmail("joao.novo@email.com");
            dao.updatePessoa(pessoa);
            System.out.println("Pessoa atualizada com sucesso: " + pessoa);

            // Exclusão (verifica regra de negócio)
            if (!dao.funcionarioHasProjects(funcionario.getId())) {
                dao.deleteFuncionario(funcionario.getId());
                System.out.println("Funcionário excluído com sucesso.");
            } else {
                System.out.println("Erro: Não é possível excluir funcionário vinculado a projetos.");
            }

        } catch (SQLException e) {
            System.out.println("Erro na operação: " + e.getMessage());
        }
    }
}