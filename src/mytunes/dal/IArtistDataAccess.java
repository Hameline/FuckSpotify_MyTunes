package mytunes.dal;

import mytunes.be.Artist;
import java.util.List;

public interface IArtistDataAccess {

    List<Artist> getAllArtist() throws Exception;

    public Artist createArtist(Artist artist) throws Exception;

    public Artist updateArtist(Artist artist) throws Exception;


}
