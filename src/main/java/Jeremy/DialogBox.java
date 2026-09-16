package jeremy;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/** A chat message displayed in the conversation area. */
public class DialogBox extends HBox {

    private static final String USER_MESSAGE_STYLE = "-fx-background-color: #7a7d52;"
            + " -fx-text-fill: #f7efe3; -fx-font-family: 'Monospaced';"
            + " -fx-font-size: 13px; -fx-padding: 9px;";
    private static final String JEREMY_MESSAGE_STYLE = "-fx-background-color: #4f3439;"
            + " -fx-text-fill: #f7efe3; -fx-font-family: 'Monospaced';"
            + " -fx-font-size: 13px; -fx-padding: 9px;";

    private final Label text;

    /** Creates a right-aligned dialog box for the user. */
    private DialogBox(String message) {
        assert message != null : "Dialog message must not be null";
        text = new Label(message);
        text.setWrapText(true);
        text.setStyle(USER_MESSAGE_STYLE);
        setAlignment(Pos.TOP_RIGHT);
        setPadding(new Insets(4.0, 8.0, 4.0, 8.0));
        getChildren().add(text);
    }

    /** Flips the dialog box to the left for Jeremy's response. */
    private void flip() {
        setAlignment(Pos.TOP_LEFT);
        text.setStyle(JEREMY_MESSAGE_STYLE);
    }

    /** Creates a dialog box representing the user. */
    public static DialogBox getUserDialog(String message) {
        return new DialogBox(message);
    }

    /** Creates a dialog box representing Jeremy. */
    public static DialogBox getJeremyDialog(String message) {
        DialogBox dialogBox = new DialogBox(message);
        dialogBox.flip();
        return dialogBox;
    }
}
