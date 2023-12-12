package mytunes.gui.controller;

import mytunes.gui.model.SongPlaylistModel;

public abstract class BaseController {
    private SongPlaylistModel model;

    public SongPlaylistModel getModel() {
        return model;
    }

    public void setModel(SongPlaylistModel model) {
        this.model = model;
    }

    public abstract void setup() throws Exception;
}