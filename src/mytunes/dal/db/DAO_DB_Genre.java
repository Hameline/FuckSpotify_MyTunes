package mytunes.dal.db;

import mytunes.be.Genre;
import mytunes.dal.IGenreDataAccess;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DAO_DB_Genre implements IGenreDataAccess {

    private MyTunesDataBaseConnector databaseConnector;

    public DAO_DB_Genre() throws IOException {
        databaseConnector = new MyTunesDataBaseConnector();
    }
    @Override
    public List<Genre> getAllGenre() throws Exception {
        ArrayList<Genre> allGenre = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM FSpotify.dbo.Genre;";
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {

                //Map DB row to Playlist object
                int id = rs.getInt("GenreID");
                String type = rs.getString("GenreType");
                int artistID = rs.getInt("ArtistID");

                Genre genre = new Genre(type, id, artistID);
                allGenre.add(genre);
            }
            return allGenre;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get playlist from database", ex);
        }
    }
}
