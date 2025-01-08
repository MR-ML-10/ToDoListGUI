
/**
 * This class implements a simple To-Do list application using JavaFX. It allows users to add,
 * remove, reorder, and save tasks in a persistent manner.
 *
 * <p>The application provides a graphical user interface with options to manipulate the list of
 * tasks. Users can add new tasks, remove tasks, reorder tasks, and save the list to disk for
 * later use. The application also offers alerts to confirm actions, such as starting with an
 * empty list or saving changes when closing the application.
 *
 * @authors Rick Mercer and Abdulrahman Al Rajhi
 * @FILE: ToDoListGUI.java
 * @ASSIGNMENT: Programming Assignment ToDoListGUI
 * @COURSE: CSC 335; Fall 2023
 * @DATE: 09/29/2023
 *
 * @version 1.0 
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ToDoListGUI extends Application implements Serializable {

	private static final long serialVersionUID = 1748554082308169736L;

	BorderPane pane = new BorderPane();

	private TextField newToDoInput;
	private Label newToDoLabel;
	private Button saveButton;
	private VBox addToDos = new VBox(); // Initialize addToDos

	private ObservableList<String> observableList = FXCollections.observableArrayList();
	private ListView<String> listView;

	Button top = new Button("Top");
	Button bottom = new Button("Bottom");
	Button raise = new Button("Raise");
	Button lower = new Button("Lower");
	Button remove = new Button("Remove");
	HBox buttons = new HBox();

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Initializes and displays the JavaFX application window.
	 *
	 * @param stage The primary stage for this application.
	 * @throws Exception If an exception occurs while starting the application.
	 */
	public void start(Stage stage) throws Exception {
		startAppAlert();
		layoutGUI(); // Initialize the GUI components and observableList
		enterKeyHandler();
		removeElement();
		serializationRead(); // Load the saved data into the observableList
		topElement();
		raiseElement();
		lowerElement();
		bottomElement();
		saveButtonHandler();
		Scene scene = new Scene(pane, 600, 400); // Increased scene size
		stage.setScene(scene);
		stage.show();
		stage.setOnCloseRequest((e) -> {
			try {
				closeAppAlert();
			} catch (Exception e1) {
				System.err.println("Error while closing the application: " + e1.getMessage());
			}
			Platform.exit();
			System.exit(0);
		});
	}

	/**
	 * Initializes the graphical user interface components.
	 */
	private void layoutGUI() {
		// Initialize newToDoInput and other components
		newToDoInput = new TextField();
		newToDoLabel = new Label("Enter a new ToDo:");
		saveButton = new Button("Save current list");

		newToDoInput.setMinWidth(400);
		addToDos.setSpacing(4);
		addToDos.getChildren().addAll(newToDoLabel, newToDoInput, saveButton);
		addToDos.setPadding(new Insets(10, 10, 10, 10));

		top.setMinWidth(100);
		bottom.setMinWidth(100);
		raise.setMinWidth(100);
		lower.setMinWidth(100);
		remove.setMinWidth(100);

		buttons.getChildren().addAll(top, bottom, raise, lower, remove);
		buttons.setSpacing(10);
		buttons.setPadding(new Insets(10, 10, 10, 10));

		listView = new ListView<>(observableList);

		pane.setTop(addToDos);
		pane.setBottom(buttons);
		pane.setCenter(listView);
		pane.setPadding(new Insets(10, 10, 10, 10));
	}

	/**
	 * Handles the Enter key press event in the newToDoInput field to add a new
	 * task.
	 */
	private void enterKeyHandler() {
		newToDoInput.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				String trimmedText = newToDoInput.getText().trim();
				if (!trimmedText.isEmpty()) {
					observableList.add(trimmedText); // Add the text to the list
					newToDoInput.clear(); // Clear the input field
				}
			}
		});
	}

	/**
	 * to remove the selected element inside of the observable list
	 */
	private void removeElement() {
		remove.setOnAction(e -> {
			int selectedIndex = listView.getSelectionModel().getSelectedIndex();
			if (selectedIndex >= 0) {
				observableList.remove(selectedIndex);
			} else {
				return;
			}
		});
	}

	/**
	 * to move the selected element into the top of the observable list
	 */
	private void topElement() {
		top.setOnAction(e -> {
			int selectedIndex = listView.getSelectionModel().getSelectedIndex();
			if (selectedIndex >= 0) {
				String selectedValue = observableList.get(selectedIndex);
				observableList.remove(selectedIndex);
				observableList.add(0, selectedValue);
				listView.getSelectionModel().select(0); // Select the moved item
			} else {
				return;
			}
		});
	}

	/**
	 * to raise the selected element inside of the observable list to element after
	 * it
	 */
	private void raiseElement() {
		raise.setOnAction(e -> {
			int selectedIndex = listView.getSelectionModel().getSelectedIndex();
			if (selectedIndex > 0) {
				String selectedValue = observableList.get(selectedIndex);
				observableList.remove(selectedIndex);
				observableList.add(selectedIndex - 1, selectedValue);
				listView.getSelectionModel().select(selectedIndex - 1); // Select the moved item

			} else {
				return;
			}
		});
	}

	/**
	 * to lower the selected element inside of the observable list to the element
	 * before it
	 */
	private void lowerElement() {
		lower.setOnAction(e -> {
			int selectedIndex = listView.getSelectionModel().getSelectedIndex();
			if (selectedIndex >= 0 && selectedIndex + 1 < observableList.size()) {
				String selectedValue = observableList.get(selectedIndex);
				observableList.remove(selectedIndex);
				observableList.add(selectedIndex + 1, selectedValue);
				listView.getSelectionModel().select(selectedIndex + 1); // Select the moved item

			} else {
				return;
			}
		});
	}

	/**
	 * to move the selected element inside of the observable list to the bottom of
	 * list
	 */
	private void bottomElement() {
		bottom.setOnAction(e -> {
			int selectedIndex = listView.getSelectionModel().getSelectedIndex();
			if (selectedIndex >= 0 && selectedIndex + 1 < observableList.size()) {
				String selectedValue = observableList.get(selectedIndex);
				observableList.remove(selectedIndex);
				observableList.add(selectedValue);
				listView.getSelectionModel().select(observableList.size() - 1); // Select the moved item

			} else {
				return;
			}
		});
	}

	/**
	 * Handles the action of clicking the "Save current list" button.
	 */
	private void saveButtonHandler() {
		saveButton.setOnAction(e -> {
			String fileName = "objects.ser";
			try {
				FileOutputStream bytesToDisk = new FileOutputStream(fileName);
				ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);
				// Write the current state of the observableList to the file
				outFile.writeObject(new ArrayList<>(observableList));
				outFile.close();
				bytesToDisk.close();
			} catch (IOException ex) {
				System.err.println("Error while saving the list: " + ex.getMessage());
			}
		});
	}

	/**
	 * Handles the action of clicking the "Save current list" button.
	 */
	private ArrayList<String> serializationRead() {
		String fileName = "objects.ser";
		ArrayList<String> list = new ArrayList<>();
		try {
			FileInputStream bytesToDisk = new FileInputStream(fileName);
			ObjectInputStream inFile = new ObjectInputStream(bytesToDisk);
			list = (ArrayList<String>) inFile.readObject();
			inFile.close();
			bytesToDisk.close();
			return list;
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error while reading data from the file: " + e.getMessage());
		}
		return list;
	}

	/**
	 * Handles the action of clicking the "Save current list" button.
	 */
	public void serializationWrite() {
		String fileName = "objects.ser";
		try {
			FileOutputStream bytesToDisk = new FileOutputStream(fileName);
			ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);
			// Write the current state of the observableList to the file
			outFile.writeObject(new ArrayList<>(observableList));
			// Should close input files also
			outFile.close();
			bytesToDisk.close();
		} catch (IOException ex) {
			System.err.println("Error while saving the list: " + ex.getMessage());
		}
	}

	/**
	 * Displays an alert to confirm whether to start the app with the save changes
	 * or start with an empty list.
	 *
	 */
	public void startAppAlert() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Click cancel to start with zero todos");
		alert.setContentText("Click OK to start with persistant ToDo List");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.isPresent() && result.get() == ButtonType.OK) {
			ArrayList<String> deserializedList = serializationRead();
			observableList.addAll(deserializedList); // Load the saved data
		} else {
			observableList.clear(); // Initialize an empty list
		}
	}

	/**
	 * Displays an alert to confirm whether to save changes when closing the
	 * application.
	 *
	 * @throws Exception If an exception occurs during the alert display.
	 */
	public void closeAppAlert() throws Exception {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Click cancel to not save any changes");
		alert.setContentText("To Save the current ToDo list, click OK");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.isPresent() && result.get() == ButtonType.OK) {
			serializationWrite();
		}
	}
}
