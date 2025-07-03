package principal;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import conexao.DatabaseConnection;
import entities.Funcionarios;
import entities.Pessoa;
import entities.Projeto;


public class Principal {
    // Instância estática de DatabaseConnection para acesso ao banco de dados
    final static DatabaseConnection dao = new DatabaseConnection();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean power = true; //mantem o programa ligado

        // while para manter o programa em funcionando até o usuário sair
        while (power) {
            System.out.println("Escolha o objeto que deseja CRUDAR: \n1-Pessoa\n2-Funcionario\n3-Projeto\n0-Sair");
            int op = sc.nextInt();
            sc.nextLine(); // Limpa o buffer

            switch (op) {
                case 1: // CRUD para Pessoa
                    System.out.println("***Menu Pessoa***");
                    System.out.println("Escolha a operação que deseja fazer: \n1-Cadastrar Pessoa\n2-Listar Pessoas"
                            + "\n3-Atualizar Pessoa\n4-Excluir Pessoa");
                    int opCrudPessoa = sc.nextInt();
                    sc.nextLine(); // Limpa o buffer

                    switch (opCrudPessoa) {
                        case 1:
                            adicionarPessoa(sc);
                            break;
                        case 2:
                            listarPessoa(sc);
                            break;
                        case 3:
                            atualizarPessoa(sc); 
                            break;
                        case 4:
                            excluirPessoa(sc);
                            break;
                        default:
                            System.out.println("Operação inválida");
                            break;
                    }
                    break;

                case 2: // Operações CRUD para Funcionário
                    System.out.println("***Menu Funcionário***");
                    System.out.println("Escolha a operação que deseja fazer: \n1-Cadastrar Funcionário\n2-Listar Funcionários"
                            + "\n3-Atualizar Funcionário\n4-Excluir Funcionário");
                    int opCrudFuncionario = sc.nextInt();
                    sc.nextLine(); // Limpa o buffer

                    switch (opCrudFuncionario) {
                        case 1:
                            adicionarFuncionario(sc);
                            break;
                        case 2:
                            listarFuncionario(sc);
                            break;
                        case 3:
                            atualizarFuncionario(sc);
                            break;
                        case 4:
                            excluirFuncionario(sc); 
                            break;
                        default:
                            System.out.println("Operação inválida");
                            break;
                    }
                    break;

                case 3: // CRUD para Projeto
                    System.out.println("***Menu Projeto***");
                    System.out.println("Escolha a operação que deseja fazer: \n1-Cadastrar Projeto\n2-Listar Projetos"
                            + "\n3-Atualizar Projeto\n4-Excluir Projeto");
                    int opCrudProjeto = sc.nextInt();
                    sc.nextLine(); // Limpa o buffer

                    switch (opCrudProjeto) {
                        case 1:
                            adicionarProjeto(sc);
                            break;
                        case 2:
                            listarProjetos(sc);
                            break;
                        case 3:
                            atualizarProjeto(sc);
                            break;
                        case 4:
                            excluirProjeto(sc);
                            break;
                        default:
                            System.out.println("Operação inválida");
                            break;
                    }
                    break;

                case 0: // Sair do programa
                    power = false;
                    break;

                default:
                    System.out.println("Operação inválida");
                    break;
            }
        }

        sc.close(); // Fecha o Scanner 
    }

    
    //Adiciona uma nova pessoa ao banco de dados, pedindo nome e email do usuário.
    public static void adicionarPessoa(Scanner sc) {
        System.out.println("Digite o nome da pessoa: ");
        String nome = sc.nextLine();
        System.out.println("Digite o email da pessoa: ");
        String email = sc.nextLine();

        Pessoa pessoa = new Pessoa(0, nome, email);

        try {
            dao.createPessoa(pessoa);
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar pessoa: " + e.getMessage());
        }
    }

    
     // Lista as pessoas cadastradas no banco de dados.
    
    public static void listarPessoa(Scanner sc) {
        try {
            List<Pessoa> pessoas = dao.readPessoas();
            if (pessoas.isEmpty()) {
                System.out.println("Nenhuma pessoa cadastrada.");
            } else {
                System.out.println("Pessoas cadastradas:");
                for (Pessoa p : pessoas) {
                    System.out.println("ID: " + p.getId() + ", Nome: " + p.getNome() + ", Email: " + p.getEmail());
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar pessoas: " + e.getMessage());
        }
    }

    
     /* Atualiza os dados de uma pessoa existente, solicitando o ID, novo nome e novo email 
      * e Verifica se a pessoa existe antes de prosseguir com a atualização.
      */
    public static void atualizarPessoa(Scanner sc) {
        System.out.println("Digite o ID da pessoa a ser atualizada:");
        int idAtualizar = sc.nextInt();
        sc.nextLine(); // Limpa o buffer

        try {
            if (dao.pessoaExists(idAtualizar)) {
                System.out.println("Digite o novo nome da pessoa:");
                String novoNome = sc.nextLine();
                System.out.println("Digite o novo email da pessoa:");
                String novoEmail = sc.nextLine();

                Pessoa pessoaAtualizada = new Pessoa(idAtualizar, novoNome, novoEmail);
                dao.updatePessoa(pessoaAtualizada);
            } else {
                System.out.println("Erro: Pessoa com ID " + idAtualizar + " não existe.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar pessoa: " + e.getMessage());
        }
    }

    //Exclui uma pessoa do banco de dados, verificando se ela existe e se não está vinculada a um funcionário.
    public static void excluirPessoa(Scanner sc) {
        System.out.println("Digite o ID da pessoa a ser excluída:");
        int idExcluir = sc.nextInt();
        try {
            if (dao.pessoaExists(idExcluir)) {
                // Verifica se a pessoa está vinculada a um funcionário
                boolean hasFuncionario = false;
                List<Funcionarios> funcionarios = dao.readFuncionarios();
                for (Funcionarios f : funcionarios) {
                    if (f.getPessoaId() == idExcluir) {
                        hasFuncionario = true;
                        break;
                    }
                }
                if (!hasFuncionario) {
                    dao.deletePessoa(idExcluir);
                } else {
                    System.out.println("Erro: Não é possível excluir pessoa vinculada a um funcionário.");
                }
            } else {
                System.out.println("Erro: Pessoa com ID " + idExcluir + " não existe.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao excluir pessoa: " + e.getMessage());
        }
    }

    
     // Adiciona um novo funcionário ao banco de dados, solicitando matrícula, departamento e ID da pessoa associada.
     // Verifica se a pessoa associada existe antes de prosseguir.
    public static void adicionarFuncionario(Scanner sc) {
        System.out.println("Digite a matrícula do funcionário:");
        String matricula = sc.nextLine();
        System.out.println("Digite o departamento do funcionário:");
        String departamento = sc.nextLine();
        System.out.println("Digite o ID da pessoa associada:");
        int pessoaId = sc.nextInt();
        sc.nextLine(); // Limpa o buffer

        try {
            if (dao.pessoaExists(pessoaId)) {
                Funcionarios funcionario = new Funcionarios(0, matricula, departamento, pessoaId);
                dao.createFuncionario(funcionario);
            } else {
                System.out.println("Erro: Pessoa com ID " + pessoaId + " não existe.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar funcionário: " + e.getMessage());
        }
    }

    /**
     * Lista todos os funcionários cadastrados no banco de dados.
     * 
     * @param sc Scanner (não utilizado diretamente, mantido para consistência).
     */
    public static void listarFuncionario(Scanner sc) {
        try {
            List<Funcionarios> funcionarios = dao.readFuncionarios();
            if (funcionarios.isEmpty()) {
                System.out.println("Nenhum funcionário cadastrado.");
            } else {
                System.out.println("Funcionários cadastrados:");
                for (Funcionarios f : funcionarios) {
                    System.out.println("ID: " + f.getId() + ", Matrícula: " + f.getMatricula()
                            + ", Departamento: " + f.getDepartamento() + ", Pessoa ID: " + f.getPessoaId());
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar funcionários: " + e.getMessage());
        }
    }

    /**
     * Atualiza os dados de um funcionário existente, solicitando o ID, nova matrícula, novo departamento
     * e novo ID da pessoa associada. Verifica se o funcionário e a nova pessoa existem.
     * 
     * @param sc Scanner para entrada de dados do usuário.
     */
    public static void atualizarFuncionario(Scanner sc) {
        System.out.println("Digite o ID do funcionário a ser atualizado:");
        int idFuncAtualizar = sc.nextInt();
        sc.nextLine(); // Limpa o buffer

        try {
            if (dao.funcionarioExists(idFuncAtualizar)) {
                System.out.println("Digite a nova matrícula do funcionário:");
                String novaMatricula = sc.nextLine();
                System.out.println("Digite o novo departamento do funcionário:");
                String novoDepartamento = sc.nextLine();
                System.out.println("Digite o novo ID da pessoa associada:");
                int novoPessoaId = sc.nextInt();
                sc.nextLine(); // Limpa o buffer

                if (dao.pessoaExists(novoPessoaId)) {
                    Funcionarios funcionarioAtualizado = new Funcionarios(idFuncAtualizar, novaMatricula, novoDepartamento, novoPessoaId);
                    dao.updateFuncionario(funcionarioAtualizado);
                } else {
                    System.out.println("Erro: Pessoa com ID " + novoPessoaId + " não existe.");
                }
            } else {
                System.out.println("Erro: Funcionário com ID " + idFuncAtualizar + " não existe.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar funcionário: " + e.getMessage());
        }
    }

    /**
     * Exclui um funcionário do banco de dados, verificando se ele existe e se não está vinculado a projetos.
     * 
     * @param sc Scanner para entrada de dados do usuário.
     */
    public static void excluirFuncionario(Scanner sc) {
        System.out.println("Digite o ID do funcionário a ser excluído:");
        int idFuncExcluir = sc.nextInt();

        try {
            if (dao.funcionarioExists(idFuncExcluir)) {
                if (!dao.funcionarioHasProjects(idFuncExcluir)) {
                    dao.deleteFuncionario(idFuncExcluir);
                } else {
                    System.out.println("Erro: Não é possível excluir funcionário vinculado a projetos.");
                }
            } else {
                System.out.println("Erro: Funcionário com ID " + idFuncExcluir + " não existe.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao excluir funcionário: " + e.getMessage());
        }
    }

    /*
     * Adiciona um novo projeto ao banco de dados, solicitando nome, descrição e ID do funcionário associado.
     * Verifica se o funcionário associado existe antes de prosseguir.
     */
    public static void adicionarProjeto(Scanner sc) {
        System.out.println("Digite o nome do projeto:");
        String nomeProjeto = sc.nextLine();
        System.out.println("Digite a descrição do projeto:");
        String descricao = sc.nextLine();
        System.out.println("Digite o ID do funcionário associado:");
        int funcionarioId = sc.nextInt();
        sc.nextLine(); // Limpa o buffer

        try {
            if (dao.funcionarioExists(funcionarioId)) {
                Projeto projeto = new Projeto(0, nomeProjeto, descricao, funcionarioId);
                dao.createProjeto(projeto);
            } else {
                System.out.println("Erro: Funcionário com ID " + funcionarioId + " não existe.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar projeto: " + e.getMessage());
        }
    }

    /*
     * Lista todos os projetos cadastrados no banco de dados.
     */
    public static void listarProjetos(Scanner sc) {
        try {
            List<Projeto> projetos = dao.readProjetos();
            if (projetos.isEmpty()) {
                System.out.println("Nenhum projeto cadastrado.");
            } else {
                System.out.println("Projetos cadastrados:");
                for (Projeto p : projetos) {
                    System.out.println("ID: " + p.getIdProjeto() + ", Nome: " + p.getNome()
                            + ", Descrição: " + p.getDescricao() + ", Funcionário ID: " + p.getFuncionarioId());
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar projetos: " + e.getMessage());
        }
    }

    /*
     * Atualiza os dados de um projeto existente, solicitando o ID, novo nome, nova descrição
     * e novo ID do funcionário associado. Verifica se o projeto e o funcionário existem. 
     */
    public static void atualizarProjeto(Scanner sc) {
        System.out.println("Digite o ID do projeto a ser atualizado:");
        int idProjetoAtualizar = sc.nextInt();
        sc.nextLine(); // Limpa o buffer

        try {
            // Verifica se o projeto existe
            boolean projetoExists = false;
            List<Projeto> projetos = dao.readProjetos();
            for (Projeto p : projetos) {
                if (p.getIdProjeto() == idProjetoAtualizar) {
                    projetoExists = true;
                    break;
                }
            }
            if (projetoExists) {
                System.out.println("Digite o novo nome do projeto:");
                String novoNomeProjeto = sc.nextLine();
                System.out.println("Digite a nova descrição do projeto:");
                String novaDescricao = sc.nextLine();
                System.out.println("Digite o novo ID do funcionário associado:");
                int novoFuncionarioId = sc.nextInt();
                sc.nextLine(); // Limpa o buffer

                if (dao.funcionarioExists(novoFuncionarioId)) {
                    Projeto projetoAtualizado = new Projeto(idProjetoAtualizar, novoNomeProjeto, novaDescricao, novoFuncionarioId);
                    dao.updateProjeto(projetoAtualizado);
                } else {
                    System.out.println("Erro: Funcionário com ID " + novoFuncionarioId + " não existe.");
                }
            } else {
                System.out.println("Erro: Projeto com ID " + idProjetoAtualizar + " não existe.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar projeto: " + e.getMessage());
        }
    }

    /*
     * Exclui um projeto do banco de dados, verificando se ele existe.
     */
    public static void excluirProjeto(Scanner sc) {
        System.out.println("Digite o ID do projeto a ser excluído:");
        int idProjetoExcluir = sc.nextInt();

        try {
            // Verifica se o projeto existe
            boolean projetoExists = false;
            List<Projeto> projetosExcluir = dao.readProjetos();
            for (Projeto p : projetosExcluir) {
                if (p.getIdProjeto() == idProjetoExcluir) {
                    projetoExists = true;
                    break;
                }
            }
            if (projetoExists) {
                dao.deleteProjeto(idProjetoExcluir);
            } else {
                System.out.println("Erro: Projeto com ID " + idProjetoExcluir + " não existe.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao excluir projeto: " + e.getMessage());
        }
    }
}