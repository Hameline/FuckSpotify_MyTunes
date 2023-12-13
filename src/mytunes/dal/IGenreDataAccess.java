package mytunes.dal;

import mytunes.be.Genre;
import java.util.List;

public interface IGenreDataAccess {

    List<Genre> getAllGenre() throws Exception;
}
