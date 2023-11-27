package mytunes.bll.util;

import mytunes.be.Song;

import java.util.ArrayList;
import java.util.List;

public class SongSearcher {

    public List<Song> search(List<Song> searchBase, String query) {
        List<Song> searchResult = new ArrayList<>();

        for (Song song : searchBase) {
            if(compareToSongTitle(query, song) || compareToSongGenre(query, song))
            {
                searchResult.add(song);
            }
        }
        return searchResult;
    }

    private boolean compareToSongTitle(String query, Song song) {
        return song.getTitle().contains(query.toLowerCase());
    }

    private boolean compareToSongGenre(String query, Song song) {
        return song.getGenre().toLowerCase().contains(query.toLowerCase());
    }
}