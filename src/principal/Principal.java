package principal;

import conexao.DatabaseConnection;

public class Principal {

	public static void main(String[] args) throws Exception {
		DatabaseConnection dao = new DatabaseConnection();
		dao.readDataBase();
		

	}

}
