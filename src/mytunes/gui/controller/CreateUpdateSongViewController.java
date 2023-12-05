package mytunes.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mytunes.be.Artist;
import mytunes.be.Genre;
import mytunes.be.Song;
import mytunes.gui.model.SongPlaylistModel;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CreateUpdateSongViewController extends BaseController implements Initializable {
    @FXML
    private ComboBox<String> menuGenre;
    private SongPlaylistModel songPlaylistModel;
    private MainViewController mainViewController;
    public ToggleButton tgglBtnIfAlbum;
    public Button btnUpdate, btnCreate, btnCancel;
    @FXML
    private TextField txtSongName, txtArtist, txtGenre, txtAlbumName, txtTime, txtFile;
    private Song selectedSong;


    public CreateUpdateSongViewController() throws Exception {
        try {
            songPlaylistModel = new SongPlaylistModel();
            mainViewController = new MainViewController();
        }
        catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addToComboBox();
    }
    public void setup() {
        songPlaylistModel = getModel();
        if (selectedSong != null) {
            txtSongName.setText(selectedSong.getTitle());
            txtArtist.setText(selectedSong.getArtist().getName());
            menuGenre.setValue(selectedSong.getGenre().getType());
            txtTime.setText(String.valueOf(selectedSong.getTime()));
        }
    }

    @FXML
    private void handleIfAlbum(ActionEvent actionEvent) {
    }
    @FXML
    private void handleUpdate(ActionEvent actionEvent) {
        if (selectedSong == null) {
            // Handle no selection
            displayError(new Exception("No song selected"));
            return;
        }
        try {
            String title = txtSongName.getText();
            String artistName = txtArtist.getText();
            String artistNameLowercase = artistName.toLowerCase();
            String genreType = menuGenre.getValue();
            int time = Integer.parseInt(txtTime.getText());

            // Update artist details
            Artist artist = songPlaylistModel.findArtistName(artistNameLowercase);
            if (artist == null) {
                artist = new Artist(artistName, -1);
                int artistID = songPlaylistModel.createArtist(artist).getId();
                artistName = artist.getName();
            } else {
                artistName = artist.getName();
            }

            // Update genre
            List<Genre> allGenres = songPlaylistModel.getAllGenres();
            Genre selectedGenre = null;
            for (Genre genre : allGenres) {
                if (genre.getType().equals(genreType)) {
                    selectedGenre = genre;
                    break;
                }
            }

            // Update the selected song details
            selectedSong.setTitle(title);
            selectedSong.setTime(time);
            selectedSong.setArtist(artist);
            selectedSong.setGenre(selectedGenre);
            // ... Update other details as necessary

            // Save the updated song
            songPlaylistModel.updateSong(selectedSong);

            // Refresh TableView

        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        } finally {
            // Close the window or perform other cleanup
            btnUpdate.getScene().getWindow().hide();
        }

    }
    @FXML
    private void handleCreate(ActionEvent actionEvent) {
        String title = txtSongName.getText();
        String artistName = txtArtist.getText();
        String artistNameLowercase = artistName.toLowerCase();
        String genreType = menuGenre.getValue();
        int time = Integer.parseInt(txtTime.getText());
        String fPath = txtFile.getText();

        try {
            Artist artist = songPlaylistModel.findArtistName(artistNameLowercase);
            if (artist == null){
                artist = new Artist(artistName, -1);
                artistName = String.valueOf(songPlaylistModel.createArtist(artist));
            }

            List<Genre> allGenres = songPlaylistModel.getAllGenres();
            Genre selecedGenre = null;
            for (Genre genre : allGenres){
                if (genre.getType().equals(genreType));
                selecedGenre = genre;
                break;
            }
            Song newSong = new Song(-1, title, time, artist, selecedGenre, "", fPath);
            songPlaylistModel.createSong(newSong);
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        } finally {
            btnCreate.getScene().getWindow().hide();
        }
    }

    private void displayError(Throwable t)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    private void addToComboBox(){
        try {
            List<Genre> genres = songPlaylistModel.getAllGenres();
            for (Genre type : genres){
                menuGenre.getItems().add(type.getType());
            }
            menuGenre.getSelectionModel().selectFirst();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setSelectedSong(Song song) {
        this.selectedSong = song;
    }

    public void handleCancelSong(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void handleChooseFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        File f = fc.showOpenDialog(stage);
        txtFile.setText(f.getPath());
    }
}