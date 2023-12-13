package mytunes.bll.util;

import mytunes.be.Genre;
import mytunes.be.Song;

import java.util.ArrayList;
import java.util.List;

public class GenreSearcher {

    public List<Genre> search(List<Genre> searchBase, String query) {
        List<Genre> searchResult = new ArrayList<>();

        for (Genre genre : searchBase) {
            if(compareToSongGenre(query, genre))
            {
                searchResult.add(genre);
            }
        }
        return searchResult;
    }

    private boolean compareToSongGenre(String query, Genre genre) {
        return genre.getType().toLowerCase().contains(query.toLowerCase());
    }

}