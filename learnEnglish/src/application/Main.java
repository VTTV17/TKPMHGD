package application;

import java.util.concurrent.Callable;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/Home.fxml"));
			Scene scene = new Scene(root);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.setOpacity(1);// 0->1
			// primaryStage.setFullScreen(true);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("I am Handsome");
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/ic_book.png")));
			// primaryStage.initStyle(arg0);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);

	}
	/////////////
	// private int count = 0 ;
	// @Override
	// public void start(Stage primaryStage) {
	// ListView<String> listView = new ListView<>();
	//
	// Button addButton = new Button("Add");
	// addButton.setOnAction(e -> listView.getItems().add("Item " + (++count)));
	//
	// Button deleteButton = new Button("Delete");
	// deleteButton.setOnAction(e ->
	// listView.getItems().remove(listView.getSelectionModel().getSelectedIndex()));
	// deleteButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
	//
	// listView.setCellFactory(lv -> {
	// ListCell<String> cell = new ListCell<String>() {
	// @Override
	// protected void updateItem(String item, boolean empty) {
	// super.updateItem(item, empty);
	// setText(item);
	// }
	// };
	// cell.setOnMouseClicked(e -> {
	// if (!cell.isEmpty()) {
	// System.out.println("You clicked on " + cell.getItem());
	// e.consume();
	// }
	// });
	// return cell;
	// });
	//
	// listView.setOnMouseClicked(e -> {
	// System.out.println("You clicked on an empty cell");
	// });
	//
	// BorderPane root = new BorderPane(listView);
	// ButtonBar buttons = new ButtonBar();
	// buttons.getButtons().addAll(addButton, deleteButton);
	// root.setBottom(buttons);
	//
	//
	// Scene scene = new Scene(root, 600, 600);
	// primaryStage.setScene(scene);
	// primaryStage.show();
	// }
	//
	// public static void main(String[] args) {
	// launch(args);
	// }

	// @Override
	// public void start(Stage primaryStage) {
	// final ListView<MyDataType> listView = new ListView<>();
	// for (int i=0; i<=20; i++) {
	// listView.getItems().add(new MyDataType("Item "+i, false));
	// }
	// final HBox controls = new HBox(5);
	// controls.setPadding(new Insets(5));
	// final Button button = new Button("Set selection");
	// final TextField indexField = new TextField();
	//
	// button.setOnAction(new EventHandler<ActionEvent>() {
	//
	// @Override
	// public void handle(ActionEvent event) {
	// try {
	// int index = Integer.parseInt(indexField.getText());
	// if (index >= 0 && index < listView.getItems().size()) {
	// final MyDataType item = listView.getItems().get(index);
	// item.setSelected( ! item.isSelected() );
	// }
	// } catch (NumberFormatException nfe) {
	// indexField.setText("");
	// }
	// }
	//
	// });
	//
	// controls.getChildren().addAll(new Label("Enter selection index:"),
	// indexField, button);
	// final BorderPane root = new BorderPane();
	// root.setCenter(listView);
	// root.setBottom(controls);
	//
	// listView.setCellFactory(new Callback<ListView<MyDataType>,
	// ListCell<MyDataType>>() {
	//
	// public ListCell<MyDataType> call(ListView<MyDataType> lv) {
	// final ListCell<MyDataType> cell = new ListCell<>();
	// final IntegerBinding previousIndex = cell.indexProperty().subtract(1);
	// final ObjectBinding<MyDataType> previousItem =
	// Bindings.valueAt(listView.getItems(), previousIndex);
	// final BooleanBinding previousItemSelected =
	// Bindings.selectBoolean(previousItem, "selected");
	// final StringBinding thisItemName = Bindings.selectString(cell.itemProperty(),
	// "name");
	// final BooleanBinding thisItemSelected =
	// Bindings.selectBoolean(cell.itemProperty(), "selected");
	// cell.textProperty().bind(Bindings.createStringBinding(new Callable<String>()
	// {
	//
	// public String call() throws Exception {
	// if (cell.getItem() == null) {
	// return null ;
	// } else {
	// String value = cell.getItem().getName();
	// if (thisItemSelected.get()) {
	// value = value + " (selected) " ;
	// } else if (previousItemSelected.get()) {
	// value = value + " (selected item is above)";
	// }
	// return value ;
	// }
	// }
	//
	// }, thisItemName, thisItemSelected, previousItemSelected));
	// return cell;
	// }
	//
	// });
	//
	// final Scene scene = new Scene(root, 600, 400);
	// primaryStage.setScene(scene);
	// primaryStage.show();
	// }
	//
	// public static class MyDataType {
	// private final BooleanProperty selected ;
	// private final StringProperty name ;
	// public MyDataType(String name, boolean selected) {
	// this.name = new SimpleStringProperty(this, "name", name);
	//
	// this.selected = new SimpleBooleanProperty(this, "selected", selected);
	// }
	// public final String getName() {
	// return name.get();
	// }
	// public final void setName(String name) {
	// this.name.set(name);
	// }
	// public final StringProperty nameProperty() {
	// return name ;
	// }
	// public final boolean isSelected() {
	// return selected.get();
	// }
	// public final void setSelected(boolean selected) {
	// this.selected.set(selected);
	// }
	// public final BooleanProperty selectedProperty() {
	// return selected;
	// }
	// }
	//
	// public static void main(String[] args) {
	// launch(args);
	// }

}
