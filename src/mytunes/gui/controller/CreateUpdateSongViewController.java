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
    @FXML
    private Button btnUpdate, btnCreate, btnCancel;
    @FXML
    private TextField txtSongName, txtArtist, txtTime, txtFile;
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

    // Setup comes from our BaseController and helps us set up how out program should look once it runs
    public void setup() {
        songPlaylistModel = getModel();
        if (selectedSong != null) {
            txtSongName.setText(selectedSong.getTitle());
            txtArtist.setText(selectedSong.getArtist().getName());
            menuGenre.setValue(selectedSong.getGenre().getType());
            txtTime.setText(String.valueOf(selectedSong.getTime()));
        }
    }

    // Here we insert new information for the selected song and updates it
    @FXML
    private void handleUpdate(ActionEvent actionEvent) {
        if (selectedSong == null) {
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
            // Update genre, from a drop down menu, no
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
    /**
     * In this method we handle the creation of a song.
     */
    private void handleCreate(ActionEvent actionEvent) {
        String title = txtSongName.getText();
        String artistName = txtArtist.getText();
        //we set the artist name to lower case in order for os the check if it's already existing.
        String artistNameLowercase = artistName.toLowerCase();
        String genreType = menuGenre.getValue();
        int time = Integer.parseInt(txtTime.getText());
        String fPath = txtFile.getText();

        try {
            //checks if artist exist.
            Artist artist = songPlaylistModel.findArtistName(artistNameLowercase);
            if (artist == null){ //if it doesn't exist, a new artist is created.
                artist = new Artist(artistName, -1);
                artistName = String.valueOf(songPlaylistModel.createArtist(artist));
            }
            /**
             * we have chosen to have all genre i DB - the user selects from drop down.
             * So the user can't create a genre.
             */
            List<Genre> allGenres = songPlaylistModel.getAllGenres();
            Genre selecedGenre = null;
            for (Genre genre : allGenres){
                if (genre.getType().equals(genreType));
                selecedGenre = genre;
                break;
            }
            // creates the song title here, no need to check for duplicate.
            Song newSong = new Song(-1, title, time, artist, selecedGenre, "", fPath);
            songPlaylistModel.createSong(newSong);
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        } finally {
            btnCreate.getScene().getWindow().hide();
        }
    }

    // An error that pops up when something goes wrong with our create or update
    private void displayError(Throwable t)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    // Adds new genres to the combo box
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

    // A button that closes the window
    public void handleCancelSong(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    // A file chooser that opens your pathfinder so you can find and insert a mp3 or wav song file
    public void handleChooseFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        File f = fc.showOpenDialog(stage);
        txtFile.setText(f.getPath());
    }
}