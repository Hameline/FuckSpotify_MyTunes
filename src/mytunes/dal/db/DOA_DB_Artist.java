package mytunes.dal.db;

import mytunes.be.Artist;
import mytunes.dal.IArtistDataAccess;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DOA_DB_Artist implements IArtistDataAccess {

    private MyTunesDataBaseConnector databaseConnector;

    public DOA_DB_Artist() throws IOException {
        databaseConnector = new MyTunesDataBaseConnector();
    }
    @Override
    public List<Artist> getAllArtist() throws Exception {
        ArrayList<Artist> allArtist = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM FSpotify.dbo.Artist;";
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {

                //Map DB row to Song object
                int id = rs.getInt("ArtistID");
                String nameA = rs.getString("ArtistName");

                //Creates a new artist and add it to the list of artist
                Artist artist = new Artist(nameA, id);
                allArtist.add(artist);
            }
            return allArtist;

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get artist from database", ex);
        }
    }

    @Override
    public Artist createArtist(Artist artist) throws Exception {
        return null;
    }

    @Override
    public Artist updateArtist(Artist artist) throws Exception {
        return null;
    }
}
