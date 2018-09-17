package pl.coderslab.model;

import org.mindrot.BCrypt;
import pl.coderslab.service.DbService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Exercise {

    private int id;
    private String title;
    private String description;

    public Exercise() {
    }

    public Exercise(String title, String description) {
        this.title = title;
        this.description = description;
        this.id = 0;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void save() {
        if (this.id == 0) {
            String query = "INSERT INTO Exercise(title, description) VALUES(?,?)";
            List<String> params = new ArrayList<>();
            params.add(this.title);
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
            String query = "UPDATE Exercise SET title = ?, description = ? WHERE id = ?";
            List<String> params = new ArrayList<>();
            params.add(this.title);
            params.add(this.description);
            params.add(String.valueOf(this.id));
            try {
                DbService.executeUpdate(query, params);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static ArrayList<Exercise> loadAll() {
        ArrayList<Exercise> exercises = new ArrayList<>();
        String query = "SELECT title, description FROM Exercise";
        try {
            List<String[]> rows = DbService.getData(query, null);
            for (String[] row : rows) {
                Exercise exercise = new Exercise();
                exercise.id = Integer.parseInt(row[0]);
                exercise.title = row[1];
                exercise.description = row[2];
                exercises.add(exercise);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exercises;
    }

    public static Exercise loadById(int id) {
        String query = "SELECT title, description FROM Exercise WHERE id = ?";
        try {
            ArrayList<String> params = new ArrayList<>();
            params.add(String.valueOf(id));
            List<String[]> rows = DbService.getData(query, params);
            for (String[] row : rows) {
                Exercise exercise = new Exercise();
                exercise.id = Integer.parseInt(row[0]);
                exercise.title = row[1];
                exercise.description = row[2];
                return exercise;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void delete(int id){
        String query = "DELETE FROM Exercise WHERE id =?";
        List<String> params = new ArrayList<>();
        params.add(String.valueOf(id));
        try {
            int affectedRows = DbService.executeUpdate(query, params);
            if(affectedRows>0){
                System.out.println("Usunalem zadanie o id: "+id);
            }else{
                System.out.println("Operacja usuniecia zadania zakonczyla sie niepowodzeniem");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
