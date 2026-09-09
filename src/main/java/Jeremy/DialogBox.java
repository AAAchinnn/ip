package jeremy;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/** A chat message displayed in the conversation area. */
public class DialogBox extends HBox {

    private final Label text;

    /** Creates a right-aligned dialog box for the user. */
    private DialogBox(String message) {
        assert message != null : "Dialog message must not be null";
        text = new Label(message);
        text.setWrapText(true);
        setAlignment(Pos.TOP_RIGHT);
        getChildren().add(text);
    }

    /** Flips the dialog box to the left for Jeremy's response. */
    private void flip() {
        assert getChildren().size() == 1 : "A text-only dialog must contain one child";
        setAlignment(Pos.TOP_LEFT);
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
