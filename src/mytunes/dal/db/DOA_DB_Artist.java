package mytunes.dal.db;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import mytunes.be.Artist;
import mytunes.dal.IArtistDataAccess;

import java.io.IOException;
import java.sql.*;
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

    public Artist findArtistByName(String name) throws SQLException {
        Connection connection = databaseConnector.getConnection();
        String stmt = "SELECT * from FSpotify.dbo.Artist Where Lower(ArtistName) = Lower(?);";

        try (PreparedStatement pstmt = connection.prepareStatement(stmt)){
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()){
                    return createArtistFromResultSet(rs);
                }
            }
        } catch (SQLException e){
            throw e;
        } finally {
            connection.close();
        }
        return null;
    }

    @Override
    public Artist createArtist(Artist artist) throws Exception {
        String sql = "INSERT INTO FSpotify.dbo.Artist (ArtistName) VALUES (?);";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            pstmt.setString(1, artist.getName());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0){
                throw new SQLException("Creating artist failed, now name affected");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()){
                if (generatedKeys.next()){
                    artist.setId(generatedKeys.getInt(1));
                    return artist;
                } else {
                    throw new SQLException("No id generated");
                }

            }catch (SQLException se){
                se.printStackTrace();
                throw new Exception("Error creating a artist");
            }
        }
    }

    private Artist createArtistFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("ArtistID");
        String name = rs.getString("ArtistName");

        return new Artist(name, id);
    }

    @Override
    public Artist updateArtist(Artist artist) throws Exception {
        return null;
    }
}
