package mytunes.bll;

import mytunes.be.Artist;
import mytunes.bll.util.ArtistSearcher;
import mytunes.dal.IArtistDataAccess;
import mytunes.dal.db.DOA_DB_Artist;
import java.io.IOException;
import java.util.List;

public class ArtistManager {
    private ArtistSearcher artistSearcher = new ArtistSearcher();
    private IArtistDataAccess DAO_DB;

    public ArtistManager() throws IOException {
        DAO_DB = new DOA_DB_Artist();
    }

    public List<Artist> getAllArtist() throws Exception {
        return DAO_DB.getAllArtist();
    }

    public List<Artist> searchArtist(String query) throws Exception {
        List<Artist> allArtist = getAllArtist();
        List<Artist> searchResult = artistSearcher.search(allArtist, query);
        return searchResult;
    }

    public Artist createArtist(Artist newArtist) throws Exception {
        return DAO_DB.createArtist(newArtist);
    }



    public Artist updateArtist(Artist selectedSong) throws Exception {
        return DAO_DB.updateArtist(selectedSong);
    }

    public Artist findArtistByName(String name) throws Exception {
        DAO_DB.findArtistByName(name);
        return null;
    }
}