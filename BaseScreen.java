import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JPanel;

//BaseScreen is the parent class for all pages in the app.
//it stores the CardLayout and container so child pages can switch screens easily.
//it also provides screen width/height for simple beginner UI layouts.
public class BaseScreen extends JPanel {
    protected final CardLayout card;
    protected final Container c;
    protected final int screenWidth;
    protected final int screenHeight;

    protected BaseScreen() {
        this(null, null);
    }

    protected BaseScreen(CardLayout card, Container c) {
        this.card = card;
        this.c = c;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenWidth = screen.width;
        this.screenHeight = screen.height;
        setSize(screenWidth, screenHeight);
        setLayout(null);
    }

    //switch to a different page by name (example: "login")
    protected void go(String name) {
        try {
            if (card != null && c != null) {
                card.show(c, name);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //true when the string is null/empty/only spaces
    protected static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

