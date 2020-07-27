import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {

    private BasicDataSource dataSource;

    public DataBase(String databaseName) { // konstruktorius
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        dataSource.setUrl("jdbc:mysql://localhost:3306/" + databaseName + "?useUnicode=yes&characterEncoding=UTF-8");
        dataSource.setValidationQuery("SELECT 1");                                                                     // tik prisijungus pabando ivykdyti programa ar viskas prisijungia taisyklingai.

    }

    public int registration(String email, String password) { //Registruja vartotoja ir prijungia
        int status = 0;
        String query = "INSERT INTO users (email, password)"
                + " VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            status = statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    public User login(String email, String password) { // leidimas prisijungti vartotojui
        String query = "SELECT * FROM users WHERE email=? AND password=?";                                                                     // ima uzklausa is lenteles (*) reiskia kad auto grazina visus stulpelius vartotojai lenteles pavadinimas

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User(email, password);
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    } // leidimas prisijungti vartotojui

    public Wallet addCard(int id, String cardbarcode) { // leidimas prisijungti vartotojui
        String query = "SELECT * FROM wallet WHERE id=? AND cardbarcode=?";                                                                     // ima uzklausa is lenteles (*) reiskia kad auto grazina visus stulpelius vartotojai lenteles pavadinimas

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.setString(2, cardbarcode);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Wallet wallet = new Wallet(id, cardbarcode);
                wallet.setId(resultSet.getInt("id"));
                wallet.setCardbarcode(resultSet.getString("cardbarcode"));
                return wallet;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    } // leidimas prisijungti vartotojui
}