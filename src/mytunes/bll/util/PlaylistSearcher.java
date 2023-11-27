package mytunes.bll.util;

import mytunes.be.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistSearcher {
    public List<Playlist> search(List<Playlist> searchBase, String query) {
        List<Playlist> searchResult = new ArrayList<>();

        for (Playlist playlist : searchBase) {
            if(compareToPlaylistName(query, playlist) || compareToPlaylistName(query, playlist))
            {
                searchResult.add(playlist);
            }
        }
        return searchResult;
    }

    private boolean compareToPlaylistName(String query, Playlist playlist) {
        return playlist.getName().contains(query.toLowerCase());
    }
}
