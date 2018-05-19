package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import connect.SqliteConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import application.NewClass;

public class HomeController implements Initializable {

	@FXML
	private ListView<VBoxCell> listviewlevel1;
	@FXML
	private ListView<VBoxCell> listviewlevel2;
	@FXML
	private ListView<VBoxCell> listviewlevel3;
	@FXML
	private ImageView imgHomeheader;
	@FXML
	private TextField searchFavorite;
	@FXML
	private Button choosefile;
	@FXML
	private ImageView imagesearchfavorite;
	@FXML
	private ListView<String> listviewFavorite;
	// @FXML private ListView<String> listview;
	NewClass filename;
	SqliteConnection sqliteConnection;

	@Override
	public void initialize(URL location, ResourceBundle resours) {
		// TODO Auto-generated method stub
		// on off sound
		filename = new NewClass();
		imgHomeheader.setImage(new Image(getClass().getResourceAsStream("/icon/ic_book.png")));
		imagesearchfavorite.setImage(new Image(getClass().getResourceAsStream("/icon/ic_search.png")));
		ContenListView();
		//
		getdata();
		choosefile.setOnAction(e -> {
			FileChooser chooser = new FileChooser();
			FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("select your media(*.mp4)", "*.mp4",
					"*.avi", "*.mov");
			chooser.getExtensionFilters().add(filter);
			File seletedFile = chooser.showOpenDialog(null);

			if (seletedFile != null) {
				filename.chuoi = seletedFile.toString();
				filename.checkchoosefile = true;
				try {
					final Node source = (Node) e.getSource();
					final Stage stage = (Stage) source.getScene().getWindow();
					stage.close();
					Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
					Scene scene = new Scene(root);
					Stage primaryStage = new Stage();
					primaryStage.initStyle(StageStyle.TRANSPARENT);
					primaryStage.setOpacity(1);// 0->1
					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					primaryStage.setScene(scene);
					primaryStage.setTitle("I am Handsome");
					primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/ic_book.png")));
					primaryStage.show();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			} else {

			}
		});
	}

	private void getdata() {
		String sql = "SELECT id,time,content,linkvideo,linksub,checkchoose FROM Favorite";
		List<String> results = new ArrayList<>();
		List<String> list = new ArrayList<>();

		searchFavorite.setOnKeyReleased(e -> {
			results.clear();

			try (Connection conn = sqliteConnection.connect();
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt
							.executeQuery(sql + " where content LIKE '%" + searchFavorite.getText() + "%'")) {

				while (rs.next()) {
					results.add(rs.getInt("id") + "-" + rs.getInt("time") + "-" + rs.getString("content") + "-"
							+ rs.getString("linkvideo") + "-" + rs.getString("linksub") + "-"
							+ rs.getInt("checkchoose"));

				}
				// conn.close();
			} catch (SQLException y) {
				System.out.println(y.getMessage());
			}
			list.clear();
			for (int i = 0; i < results.size(); i++) {
				String string = results.get(i).toString();
				String[] parts = string.split("-");
				list.add(parts[2]);
			}
			ObservableList<String> listFavorite = FXCollections.observableList(list);
			listviewFavorite.setItems(listFavorite);

		});
		//
		listviewFavorite.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (listviewFavorite.getSelectionModel().getSelectedIndex() != -1) {
					try {
						String string = results.get(listviewFavorite.getSelectionModel().getSelectedIndex()).toString();
						filename.checkopenFavorite = true;
						// System.out.println(string);
						String[] parts = string.split("-");
						// System.out.println(string);
						// filename.chuoi1 = string.toString();
						filename.chuoi = parts[3];
						filename.stringopenFavorite = string;
						final Node source = (Node) event.getSource();
						final Stage stage = (Stage) source.getScene().getWindow();
						stage.close();
						Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
						Scene scene = new Scene(root);
						Stage primaryStage = new Stage();
						primaryStage.initStyle(StageStyle.TRANSPARENT);
						primaryStage.setOpacity(1);// 0->1
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setScene(scene);
						primaryStage.setTitle("I am Handsome");
						primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/ic_book.png")));
						primaryStage.show();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		//
		try (Connection conn = sqliteConnection.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			// loop through the result set
			while (rs.next()) {
				results.add(rs.getInt("id") + "-" + rs.getInt("time") + "-" + rs.getString("content") + "-"
						+ rs.getString("linkvideo") + "-" + rs.getString("linksub") + "-" + rs.getInt("checkchoose"));
			}
			// conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		for (int i = 0; i < results.size(); i++) {
			String string = results.get(i).toString();
			String[] parts = string.split("-");
			list.add(parts[2]);
		}
		ObservableList<String> listFavorite = FXCollections.observableList(list);
		listviewFavorite.setItems(listFavorite);

	}

	public void ContenListView() {
		List<VBoxCell> list = new ArrayList<>();
		List<VBoxCell> list1 = new ArrayList<>();
		List<VBoxCell> list2 = new ArrayList<>();
		List<String> results = selectAll("1");
		List<String> results2 = selectAll("2");
		List<String> results3 = selectAll("3");

		for (int i = 0; i < results.size(); i++) {
			String string = results.get(i).toString();
			String[] parts = string.split("-");
			list.add(new VBoxCell(parts[1],  parts[4]));
		}
		for (int i = 0; i < results2.size(); i++) {
			String string = results2.get(i).toString();
			String[] parts = string.split("-");
			list1.add(new VBoxCell(parts[1],  parts[4]));
		}
		for (int i = 0; i < results3.size(); i++) {
			String string = results3.get(i).toString();
			String[] parts = string.split("-");
			list2.add(new VBoxCell(parts[1], parts[4]));//t chinh cai nay
		}
		ObservableList<VBoxCell> myObservableList = FXCollections.observableList(list);
		ObservableList<VBoxCell> myObservableList1 = FXCollections.observableList(list1);
		ObservableList<VBoxCell> myObservableList2 = FXCollections.observableList(list2);
		listviewlevel1.setItems(myObservableList);
		listviewlevel1.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (listviewlevel1.getSelectionModel().getSelectedIndex() != -1) {
					try {
						String string = results.get(listviewlevel1.getSelectionModel().getSelectedIndex()).toString();
						String[] parts = string.split("-");
						filename.chuoi1 = string.toString();
						filename.chuoi = string.toString();
						final Node source = (Node) event.getSource();
						final Stage stage = (Stage) source.getScene().getWindow();
						stage.close();
						Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
						Scene scene = new Scene(root);
						Stage primaryStage = new Stage();
						primaryStage.initStyle(StageStyle.TRANSPARENT);
						primaryStage.setOpacity(1);// 0->1
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setScene(scene);
						primaryStage.setTitle("I am Handsome");
						primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/ic_book.png")));
						primaryStage.show();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		listviewlevel2.setItems(myObservableList1);
		listviewlevel2.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (listviewlevel2.getSelectionModel().getSelectedIndex() != -1) {
					try {
						String string = results2.get(listviewlevel2.getSelectionModel().getSelectedIndex()).toString();
						String[] parts = string.split("-");
						filename.chuoi1 = string.toString();
						filename.chuoi = string.toString();
						final Node source = (Node) event.getSource();
						final Stage stage = (Stage) source.getScene().getWindow();
						stage.close();
						Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
						Scene scene = new Scene(root);
						Stage primaryStage = new Stage();
						primaryStage.initStyle(StageStyle.TRANSPARENT);
						primaryStage.setOpacity(1);// 0->1
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setScene(scene);
						primaryStage.setTitle("I am Handsome");
						primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/ic_book.png")));
						primaryStage.show();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		listviewlevel3.setItems(myObservableList2);
		listviewlevel3.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (listviewlevel3.getSelectionModel().getSelectedIndex() != -1) {
					try {
						String string = results3.get(listviewlevel3.getSelectionModel().getSelectedIndex()).toString();
						String[] parts = string.split("-");
						filename.chuoi1 = string.toString();
						filename.chuoi = string.toString();
						final Node source = (Node) event.getSource();
						final Stage stage = (Stage) source.getScene().getWindow();
						stage.close();
						Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
						Scene scene = new Scene(root);
						Stage primaryStage = new Stage();
						primaryStage.initStyle(StageStyle.TRANSPARENT);
						primaryStage.setOpacity(1);// 0->1
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setScene(scene);
						primaryStage.setTitle("I am Handsome");
						primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/ic_book.png")));
						primaryStage.show();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	public List<String> selectAll(String level) {
		String sql = "SELECT id, name, level, link,imagevideo FROM ListVideo WHERE ListVideo.level=" + level + "";
		List<String> results = new ArrayList<>();
		try (Connection conn = sqliteConnection.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			// loop through the result set
			while (rs.next()) {
				results.add(rs.getInt("id") + "-" + rs.getString("name") + "-" + rs.getInt("level") + "-"
						+ rs.getString("link") + "-" + rs.getString("imagevideo"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return results;
	}

	public static class VBoxCell extends VBox {

		Label label1 = new Label();

		VBoxCell(String content, String img) {
			super();
			label1.setText(content);
			label1.setMaxWidth(Double.MAX_VALUE);
			// VBox.set
			// System.out.println(img);
			VBox.setVgrow(label1, Priority.ALWAYS);
			final Image selected = new Image(getClass().getResourceAsStream(img));
			final ImageView toggleImage = new ImageView(selected);
			toggleImage.setFitHeight(100);
			toggleImage.setFitWidth(150);
			this.getChildren().addAll(toggleImage, label1);
		}

		private ListView<VBoxCell> getListView() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	@FXML
	void close(ActionEvent event) {
		final Node source = (Node) event.getSource();
		final Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

}
