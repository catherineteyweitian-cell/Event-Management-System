import java.awt.CardLayout;
import java.awt.Container;

//baseRegisterPanel is the parent class for all registration panels.
//it extends BaseScreen and provides shared layout helpers for forms.

public class BaseRegisterPanel extends BaseScreen {
    protected BaseRegisterPanel(CardLayout card, Container c) {
        super(card, c);
        }

    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
