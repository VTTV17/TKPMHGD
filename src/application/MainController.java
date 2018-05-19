package application;

import javafx.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import connect.SqliteConnection;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController implements Initializable{
	@FXML private MediaView mediaView;
	@FXML private ImageView imghead;
	@FXML
    private ImageView imgSearch;

	@FXML
    private ImageView imgSound;
	@FXML private ListView<HBoxCell> listview;
//	@FXML private ListView<String> listview;
	 @FXML private Slider volumeSlider;
	 @FXML private Slider mediaSlider;
	 
	// @FXML private Label timeplayer;
	 @FXML private Button playButton,resetButton,addsub;

	private MediaPlayer mediaPlayer;
	private Media media;
	Duration duration;
	SqliteConnection sqliteConnection;
	@FXML
	private Label playTime,labelhead;
	private boolean check=true;
	private boolean checkSound=true;
	private Double giaTri=0.;

	@Override
	public void initialize(URL location, ResourceBundle resours) {
		// TODO Auto-generated method stub
		
		ClickMediaPlayer();
        OnOffSound();
               
        if(NewClass.checkchoosefile)
        {
        	AnhXa(NewClass.chuoi);
        	Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Notification");
    		alert.setHeaderText(null);
    		alert.setContentText("Do you want to add subtitle files?"+"\n"+"Please select the 'AddSub' button to add");

    		Optional<ButtonType> result = alert.showAndWait();
    		String filenamevideo=NewClass.chuoi;
//    		InsertListVideo(NewClass.chuoi);
        	NewClass.checkchoosefile=false;
        	
        	playButton.setStyle("-fx-background-image: url('/icon/ic-pause.png')");
			check=false;
			
        }else {
            String []array=NewClass.chuoi.toString().split("-");
        	AnhXa(array[3]);
        	ContenListView(array[0]);
        	addsub.setDisable(!NewClass.checkchoosefile);
        }
        
        SliderVolume();
        interactWithMedia();
        AddSub();
	}
	
//	private void InsertListVideo(String chuoi) {
//		// TODO Auto-generated method stub
//		String sql = "INSERT INTO ListVideo(name,level,link,imagevideo) VALUES(?,?,?,?)";
//		String[] parts = chuoi.split("@'\'");
////		String[] name=parts[parts.length-1].toString().split(".");
////		String[] name=parts[parts.length-1].split(".");
////        try (Connection conn = sqliteConnection.connect();
////                PreparedStatement pstmt = conn.prepareStatement(sql)) {
////            pstmt.setString(1, name[0]);
////            pstmt.setInt(2, 3);
////            pstmt.setString(3, chuoi);
////            pstmt.setString(4, "src/img/anhmacdinh.png");
////            pstmt.executeUpdate();
////        } catch (SQLException e) {
////            System.out.println(e.getMessage());
////        }
//		System.out.println("name[0]"+chuoi+"src/img/anhmacdinh.png");
//	}

	private void AddSub() {
		// TODO Auto-generated method stub
		
		
		addsub.setOnAction(e->{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Please select the correct text file for the video of your choice"+"\r\n"+"Sample form:"+"\r\n" +"-00:05-Hello! What your name?"+"\r\n" +"-00:10-Hi! My name marry"+"\r\n" +"....");

			alert.showAndWait();
			FileChooser fileChooser = new FileChooser();
            
            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
             
            //Show save file dialog
            File file = fileChooser.showOpenDialog(null);
            if(file != null){
                System.out.println(readFile(file));
                alert.setTitle("Information Dialog");
    			alert.setHeaderText(null);
    			alert.setContentText(readFile(file));
    			alert.showAndWait();
    			SaveFile(readFile(file));
            }
		});
	}
	public void insertListSub(String content, int secound,int idvideo) {
        String sql = "INSERT INTO ListSub(content,secound,idvideo) VALUES(?,?,?)";
 
        try (Connection conn = sqliteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, content);
            pstmt.setInt(2, secound);
            pstmt.setInt(3, idvideo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	private void SaveFile(String fileText) {
		// TODO Auto-generated method stub
		try {
			String[] parts = fileText.split("-");
			System.out.println(parts.length);
			List<HBoxCell> list = new ArrayList<>();
			for(int i=1;i<parts.length;i+=2) {
//				insertListSub(parts[i],110,1);
				list.add(new HBoxCell(parts[i]+"   "+parts[i+1]));
			}		
			
	        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
	        listview.setItems(myObservableList);
	        listview.setOnMouseClicked(new EventHandler<MouseEvent>() {

	            @Override
	            public void handle(MouseEvent event) {
	            	if(listview.getSelectionModel().getSelectedIndex()!=-1)
	            	{
	            		String string = parts[listview.getSelectionModel().getSelectedIndex()*2+1].toString();
	                    mediaPlayer.seek(Duration.seconds(Double.parseDouble(String.valueOf( ConvertToNumber(string)))));
	            	}
	            }
	        });
		}catch(Exception e) {
 			e.printStackTrace();
 		}
	}
	private int ConvertToNumber(String hours) {
		String[] parts = hours.split(":");
		int number=0;
		if(parts.length>2) {
			number+=Integer.parseInt(parts[parts.length-2].toString().trim())*60+Integer.parseInt(parts[parts.length-3].toString().trim())*3600
					+Integer.parseInt(parts[parts.length-1].toString().trim());
		}else 
			number+=Integer.parseInt(parts[parts.length-2].toString().trim())*60
			+Integer.parseInt(parts[parts.length-1].toString().trim());
		return number;
	}
	
	private String readFile(File file){
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;
         
        try {
 
            bufferedReader = new BufferedReader(new FileReader(file));
             
            String text;
            while ((text = bufferedReader.readLine()) != null) {
                stringBuffer.append(text);
            }
 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
         
        return stringBuffer.toString();
    }
	
	private void AnhXa(String link) {
//		link="src/media/Meetingafriend.mp4";
		// TODO Auto-generated method stub
		String path=new File(link).getAbsolutePath();
		media=new Media(new File(path).toURI().toString());
		mediaPlayer=new MediaPlayer(media);
		mediaPlayer.pause();
		mediaView.setMediaPlayer(mediaPlayer);
//		mediaPlayer.setAutoPlay(true);
		imghead.setImage(new Image(getClass().getResourceAsStream("/icon/ic_book.png")));
		playButton.setStyle("-fx-background-image: url('/icon/ic-play.png')");
		resetButton.setStyle("-fx-background-image: url('/icon/ic-resart.png')");	
		mediaView.setPreserveRatio(true);
		imgSearch.setImage(new Image(getClass().getResourceAsStream("/icon/ic_search.png")));
		imgSound.setImage(new Image(getClass().getResourceAsStream("/icon/ic.png")));
		
	}

	private void ClickMediaPlayer() {
		// TODO Auto-generated method stub
		mediaView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                // TODO Auto-generated method stub
            	if(check)
        		{
        			mediaPlayer.pause();
        			playButton.setStyle("-fx-background-image: url('/icon/ic-pause.png')");
        			check=false;
        			
        		}else {
        			mediaPlayer.play();
        			playButton.setStyle("-fx-background-image: url('/icon/ic-play.png')");
        			check=true;
        		}
            }
        });
	}

	public String convert(String seconds) {
		Double se=Double.valueOf(seconds);
		int s=(int) Math.round(se);
		int m=0,h=0;
		
		m=s/60;
		h=m/60;
		m=m%60;
		s=s%60;
		String min,second,hours;
		if(m<10)
		{
			min="0"+String.valueOf(m);
		}else {
			min=String.valueOf(m);
		}
		if(h<10)
		{
			hours="0"+String.valueOf(h);
		}else {
			hours=String.valueOf(h);
		}
		if(s<10)
		{
			second="0"+String.valueOf(s);
		}else {
			second=String.valueOf(s);
		}
		if (h>0)
		{
			seconds=hours+":"+min+":"+second;
		}else {
			seconds=min+":"+second;
		}
		return seconds;
		
	}
	
	public void interactWithMedia() {

        mediaPlayer.setOnReady(() -> {
         
          mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>(){
          	@Override
          	public void changed(ObservableValue<? extends Duration> observable,Duration oldValue,Duration newValue) {
          		mediaSlider.setValue(newValue.toSeconds());
          	}
          });
          playTime.textProperty().bind(
                  Bindings.createStringBinding(() -> {
                          Duration time = mediaPlayer.getCurrentTime();
                          return String.format("%4d:%02d:%04.1f",
                              (int) time.toHours(),
                              (int) time.toMinutes() % 60,
                              time.toSeconds() % 3600);
                      },
                      mediaPlayer.currentTimeProperty()));
          mediaSlider.setMinWidth(0);
          mediaSlider.setMaxWidth(Double.MAX_VALUE);
          mediaSlider.setMin(0);
          mediaSlider.maxProperty().bind(Bindings.createDoubleBinding(
                      () -> mediaPlayer.getTotalDuration().toSeconds(),mediaPlayer.totalDurationProperty()));
          mediaSlider.setStyle("-fx-control-inner-background: palegreen;");
          mediaSlider.setOnMouseClicked(new EventHandler< MouseEvent>(){
          	public void handle(MouseEvent event) {
          		mediaPlayer.seek(Duration.seconds(mediaSlider.getValue()));
          	}
          });
        });
	}
	
	public void SliderVolume() {
		 volumeSlider.setValue(mediaPlayer.getVolume() * 100); // 1.0 = max 0.0 = min
	        volumeSlider.valueProperty().addListener(new InvalidationListener() {
	            
	            @Override
	            public void invalidated(Observable observable) {
	                mediaPlayer.setVolume(volumeSlider.getValue() / 100);
	                if(volumeSlider.getValue() / 100==0)
	                {
	                	imgSound.setImage(new Image(getClass().getResourceAsStream("/icon/ic-sound-off.png")));
	                }else {
	                	imgSound.setImage(new Image(getClass().getResourceAsStream("/icon/ic.png")));
	                }
	            }
	        });
	}
	
	public void ContenListView(String idvieo) {
		List<HBoxCell> list = new ArrayList<>();
		List<String> results =selectAll(idvieo);
        
        for(int i=0;i<results.size();i++)
        {
        	String string = results.get(i).toString();
        	String[] parts = string.split("-");
        	String part1 = parts[0]; 
        	String part2 = parts[1]; 
        	list.add(new HBoxCell(convert(parts[0])+"   "+part2));
        }
        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
//		ObservableList<String> myObservableList = FXCollections.observableList(results);
        listview.setItems(myObservableList);
        listview.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
//                System.out.println("clicked on " + results.get(listview.getSelectionModel().getSelectedIndex()));
                //mediaSlider.setValue(30);
            	if(listview.getSelectionModel().getSelectedIndex()!=-1)
            	{
            		String string = results.get(listview.getSelectionModel().getSelectedIndex()).toString();
                 	String[] parts = string.split("-");
                 	String part1 = parts[0]; 
                 	String part2 = parts[1]; 
                    mediaPlayer.seek(Duration.seconds(Double.parseDouble(part1)));
            	}
            }
        });
		
	}
	
	public List<String> selectAll(String idvideo){
        String sql = "SELECT id, content, secound, idvideo FROM ListSub WHERE ListSub.idvideo="+idvideo+"";
        List<String> results = new ArrayList<>();
        try (Connection conn = sqliteConnection.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
            	results.add(rs.getDouble("secound") +  "-" + 
                        rs.getString("content"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return results;
    }

	public static class HBoxCell extends HBox {
        Label label = new Label();
        Label label1 = new Label();
        ToggleButton toggle = new ToggleButton();
        ToggleButton toggle1 = new ToggleButton();
        
        HBoxCell(String content) {
             super();

             label1.setText(content);
             label1.setMaxWidth(Double.MAX_VALUE);
             HBox.setHgrow(label1, Priority.ALWAYS);

                   final Image        selected    = new Image(getClass().getResourceAsStream("/icon/star.png"));
                   final Image        selected1    = new Image(getClass().getResourceAsStream("/icon/speaker.png"));
                   final ImageView    toggleImage = new ImageView(selected);
                   final ImageView    toggleImage1 = new ImageView(selected1);
                   toggleImage.setFitHeight(25);	toggleImage.setFitWidth(25);
                   toggleImage1.setFitHeight(25);	toggleImage1.setFitWidth(25);
             toggle.setMinSize(25, 25); toggle.setMaxSize(25, 25);
             toggle.setGraphic(toggleImage1);
             toggle.setStyle("-fx-background: transparent");
             
             toggle1.setMinSize(25, 25);	toggle1.setMaxSize(25, 25);
             toggle1.setText("123123123");
             toggle1.setGraphic(toggleImage);
             Button button=new Button();
//             button.setText("12");
             button.setMinSize(25, 25); toggle.setMaxSize(25, 25);
             button.setGraphic(toggleImage1);
             button.setStyle("-fx-background: transparent");
             button.setGraphic(toggleImage);
//             button.setOnMouseClicked(new EventHandler<MouseEvent>() {
//
//                 @Override
//                 public void handle(MouseEvent event) {
////                	 <Syste>
//                	 System.out.println("click ");
////                     mediaPlayer.seek(Duration.seconds(Double.parseDouble(part1)));
//                 }
//             });
             button.setOnAction(e->getListView().getItems().remove(getId()));
             
//             this.getChildren().addAll(label,label1, toggle,toggle1);
             this.getChildren().addAll(label1, button);
        }

		private ListView<HBoxCell> getListView() {
			// TODO Auto-generated method stub
			return null;
		}
   }
	
	public void OnOffSound() {
		imgSound.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

		     @Override
		     public void handle(MouseEvent event) {
		    	 giaTri=(volumeSlider.getValue() / 100);
		 		if(checkSound)
		 		{
		 			mediaPlayer.setVolume(0);
		 			imgSound.setImage(new Image(getClass().getResourceAsStream("/icon/ic-sound-off.png")));
		 			checkSound=false;
		             
		 		}else {
		 			imgSound.setImage(new Image(getClass().getResourceAsStream("/icon/ic.png")));
		 			mediaPlayer.setVolume(giaTri);
		 			checkSound=true;
		 		}
		     }
		});
		
		
	}
	
	public void PlayPauses(ActionEvent event) {
		if(check)
		{
			mediaPlayer.pause();
			playButton.setStyle("-fx-background-image: url('/icon/ic-pause.png')");
			check=false;
			
		}else {
			mediaPlayer.play();
			playButton.setStyle("-fx-background-image: url('/icon/ic-play.png')");
			check=true;
		}
	}
	
	public void Reset(ActionEvent event) {
		mediaPlayer.seek(mediaPlayer.getStartTime());
		mediaPlayer.play();
	}
	
	@FXML
	void close(ActionEvent event)
	{
		try {
			final Node source = (Node) event.getSource();
		    final Stage stage = (Stage) source.getScene().getWindow();
		    mediaPlayer.stop();
		    stage.close();
			Parent root = FXMLLoader.load(getClass().getResource("/application/Home.fxml"));
	        Scene scene = new Scene(root);
	        Stage primaryStage=new Stage();
	        primaryStage.initStyle(StageStyle.TRANSPARENT);
	        primaryStage.setOpacity(1);// 0->1
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/ic_book.png")));
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
