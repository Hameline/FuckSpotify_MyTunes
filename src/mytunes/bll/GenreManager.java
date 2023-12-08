package mytunes.bll;

import mytunes.be.Artist;
import mytunes.be.Genre;
import mytunes.bll.util.GenreSearcher;
import mytunes.dal.IGenreDataAccess;
import mytunes.dal.db.DAO_DB_Genre;

import java.io.IOException;
import java.util.List;

public class GenreManager {
    private GenreSearcher genreSearcher = new GenreSearcher();
    private IGenreDataAccess DAO_DB;

    public GenreManager() throws IOException {
        DAO_DB = new DAO_DB_Genre();
    }

    public List<Genre> getAllGenre() throws Exception {
        return DAO_DB.getAllGenre();
    }




}