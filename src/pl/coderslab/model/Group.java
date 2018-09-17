package pl.coderslab.model;

import pl.coderslab.service.DbService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Group {
    private int id;
    private String name;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
        this.id = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void save() {
        if (this.id == 0) {
            String query = "INSERT INTO Group(name) VALUES(?)";
            List<String> params = new ArrayList<>();
            params.add(this.name);
            try {
                Integer id = DbService.insertIntoDatabase(query, params);
                if (id != null) {
                    this.id = id;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            String query = "UPDATE Group SET name = ? WHERE id = ?";
            List<String> params = new ArrayList<>();
            params.add(this.name);
            params.add(String.valueOf(this.id));
            try {
                DbService.executeUpdate(query, params);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static ArrayList<Group> loadAll() {
        ArrayList<Group> groups = new ArrayList<>();
        String query = "SELECT id, name FROM Group";
        try {
            List<String[]> rows = DbService.getData(query, null);
            for (String[] row : rows) {
                Group group = new Group();
                group.id = Integer.parseInt(row[0]);
                group.name = row[1];
                groups.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    public static Group loadById(int id) {
        String query = "SELECT id, name FROM Group WHERE id = ?";
        try {
            ArrayList<String> params = new ArrayList<>();
            params.add(String.valueOf(id));
            List<String[]> rows = DbService.getData(query, params);
            for (String[] row : rows) {
                Group group = new Group();
                group.id = Integer.parseInt(row[0]);
                group.name = row[1];
                return group;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void delete(int id){
        String query = "DELETE FROM Group WHERE id =?";
        List<String> params = new ArrayList<>();
        params.add(String.valueOf(id));
        try {
            int affectedRows = DbService.executeUpdate(query, params);
            if(affectedRows>0){
                System.out.println("Usunalem grupe o id: "+id);
            }else{
                System.out.println("Operacja usuniecia grupy zakonczyla sie niepowodzeniem");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
