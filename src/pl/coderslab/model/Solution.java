package pl.coderslab.model;

import pl.coderslab.service.DbService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Solution {
    private int id;
    private String created;
    private String updated;
    private String description;

    public Solution() {
    }

    public Solution(String created, String updated, String description) {
        this.created = created;
        this.updated = updated;
        this.description = description;
        this.id=0;
    }

    public Solution setCreated(String created) {
        this.created = created;
        return this;
    }

    public Solution setUpdated(String updated) {
        this.updated = updated;
        return this;
    }

    public Solution setDescription(String description) {
        this.description = description;
        return this;
    }

    public void save() {
        if (this.id == 0) {
            String query = "INSERT INTO Solution(created, updated, description) VALUES(?,?,?)";
            List<String> params = new ArrayList<>();
            params.add(this.created);
            params.add(this.updated);
            params.add(this.description);
            try {
                Integer id = DbService.insertIntoDatabase(query, params);
                if (id != null) {
                    this.id = id;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            String query = "UPDATE Solution SET created = ?, updated = ?, description = ? WHERE id = ?";
            List<String> params = new ArrayList<>();
            params.add(this.created);
            params.add(this.updated);
            params.add(this.description);
            params.add(String.valueOf(this.id));
            try {
                DbService.executeUpdate(query, params);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static ArrayList<Solution> loadAll() {
        ArrayList<Solution> solutions = new ArrayList<>();
        String query = "SELECT id, created, updated, description FROM Solution";
        try {

            List<String[]> rows = DbService.getData(query, null);
            for (String[] row : rows) {
                Solution solution = new Solution();
                solution.id = Integer.parseInt(row[0]);
                solution.created = row[1];
                solution.updated = row[2];
                solution.description = row[3];
                solutions.add(solution);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solutions;
    }

    public static Solution loadById(int id) {
        String query = "SELECT id, created, updated, description FROM Solution WHERE id = ?";
        try {
            ArrayList<String> params = new ArrayList<>();
            params.add(String.valueOf(id));
            List<String[]> rows = DbService.getData(query, params);
            for (String[] row : rows) {
                Solution solution = new Solution();
                solution.id = Integer.parseInt(row[0]);
                solution.created = row[1];
                solution.updated = row[2];
                solution.description = row[3];
                return solution;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void delete(int id){
        String query = "DELETE FROM Solution WHERE id =?";
        List<String> params = new ArrayList<>();
        params.add(String.valueOf(id));
        try {
            int affectedRows = DbService.executeUpdate(query, params);
            if(affectedRows>0){
                System.out.println("Usunalem rozwiazanie o id: "+id);
            }else{
                System.out.println("Operacja usuniecia rozwiazanie zakonczyla sie niepowodzeniem");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
