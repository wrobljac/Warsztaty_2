package pl.coderslab.model;

import org.mindrot.BCrypt;
import pl.coderslab.service.DbService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User {

    private int id;
    private String name;
    private String username;
    private String password;
    private String email;

    public User() {
    }

    public User(String name, String username, String password, String email) {
        this.name = name;
        this.username = username;
        setPassword(password);
        this.email = email;
        this.id = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void save() {
        if (this.id == 0) {
            String query = "INSERT INTO Users(name, username, password, email) VALUES(?,?,?,?)";
            List<String> params = new ArrayList<>();
            params.add(this.name);
            params.add(this.username);
            params.add(this.password);
            params.add(this.email);
            try {
                Integer id = DbService.insertIntoDatabase(query, params);
                if (id != null) {
                    this.id = id;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            String query = "UPDATE Users SET name = ?, username = ?, password = ?, email = ? WHERE id = ?";
            List<String> params = new ArrayList<>();
            params.add(this.name);
            params.add(this.username);
            params.add(this.password);
            params.add(this.email);
            params.add(String.valueOf(this.id));
            try {
                DbService.executeUpdate(query, params);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static ArrayList<User> loadAll() {
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT id, name, username, password, email FROM Users";
        try {

            List<String[]> rows = DbService.getData(query, null);
            for (String[] row : rows) {
                User user = new User();
                user.id = Integer.parseInt(row[0]);
                user.name = row[1];
                user.username = row[2];
                user.password = row[3];
                user.email = row[4];
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static User loadById(int id) {
        String query = "SELECT id, name, username, password, email FROM Users WHERE id = ?";
        try {
            ArrayList<String> params = new ArrayList<>();
            params.add(String.valueOf(id));
            List<String[]> rows = DbService.getData(query, params);
            for (String[] row : rows) {
                User user = new User();
                user.id = Integer.parseInt(row[0]);
                user.name = row[1];
                user.username = row[2];
                user.password = row[3];
                user.email = row[4];
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void delete(int id){
        String query = "DELETE FROM Users WHERE id =?";
        List<String> params = new ArrayList<>();
        params.add(String.valueOf(id));
        try {
            int affectedRows = DbService.executeUpdate(query, params);
            if(affectedRows>0){
                System.out.println("Usunalem usera o id: "+id);
            }else{
                System.out.println("Operacja usuniecia usera zakonczyla sie niepowodzeniem");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
