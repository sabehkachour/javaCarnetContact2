import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class main{
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@localhost:1521:FREE";
        String user = "system";
        String password = "oracle_4U";

        try {
            // Charger le pilote Oracle (nécessaire pour les anciennes versions de Java)
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Établir la connexion
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connexion réussie !");
            connection.close();
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Pilote Oracle non trouvé !");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à Oracle !");
            e.printStackTrace();
        }
    }
}