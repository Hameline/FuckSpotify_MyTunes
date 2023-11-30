package mytunes.bll.util;

import mytunes.be.Artist;
import mytunes.be.Genre;
import mytunes.be.Song;

import java.util.ArrayList;
import java.util.List;

public class SongSearcher {

    public List<Song> search(List<Song> searchBase, String query) {
        List<Song> searchResult = new ArrayList<>();

        for (Song song : searchBase) {
            if(compareToSongTitle(query, song) || compareToSongGenre(query, song) || compareToSongArtist(query, song))
            {
                searchResult.add(song);
            }
        }
        return searchResult;
    }

    private boolean compareToSongTitle(String query, Song song) {
        return song.getTitle().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToSongGenre(String query, Song song) {
        return song.getGenre().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToSongArtist(String query, Song song){
        return song.getArtist().toLowerCase().contains(query.toLowerCase());
    }
}