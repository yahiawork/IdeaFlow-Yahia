package com.yahia.ideaflow.app;

import com.yahia.ideaflow.model.Idea;
import com.yahia.ideaflow.model.IdeaStore;
import com.yahia.ideaflow.screens.BoardScreen;
import com.yahia.ideaflow.screens.DetailsScreen;
import com.yahia.ideaflow.screens.InboxScreen;
import com.yahia.ideaflow.theme.Colors;
import com.yahia.ideaflow.ui.Sidebar;
import com.yahia.ideaflow.ui.TopBar;
import com.yahia.ideaflow.ui.RoundedPanel;
import com.yahia.ideaflow.ui.SoftBackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * App window layout:
 * - TopBar (search / add / theme toggle)
 * - Sidebar (navigation)
 * - Content (CardLayout screens)
 */
public final class MainFrame extends JFrame {

    private final IdeaStore store = new IdeaStore();

    private final com.yahia.ideaflow.ui.AnimatedNavigator content = new com.yahia.ideaflow.ui.AnimatedNavigator();
private final InboxScreen inboxScreen;
    private final BoardScreen boardScreen;
    private final DetailsScreen detailsScreen;

    public MainFrame() {
        super("IdeaFlow");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1080, 680));
        setLocationRelativeTo(null);

        // Root (subtle gradient background)
        SoftBackgroundPanel root = new SoftBackgroundPanel();
        root.setLayout(new BorderLayout(0, 0));
        setContentPane(root);

        // Top bar
        TopBar topBar = new TopBar(
                this::onQuickAdd,
                this::onSearchChanged
        );
        root.add(topBar, BorderLayout.NORTH);

        // Center area: sidebar + content
        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        root.add(center, BorderLayout.CENTER);

        Consumer<String> onNavigate = this::navigateTo;
        Sidebar sidebar = new Sidebar(onNavigate);
        center.add(sidebar, BorderLayout.WEST);

        // Screens
        inboxScreen = new InboxScreen(store, this::openDetails);
        boardScreen = new BoardScreen(store, this::openDetails);
        detailsScreen = new DetailsScreen(store, this::backToInbox);

        content.addScreen("inbox", inboxScreen);
        content.addScreen("board", boardScreen);
        content.addScreen("details", detailsScreen);

        center.add(content, BorderLayout.CENTER);

        // Default screen
        navigateTo("inbox");
    }

    private void navigateTo(String key) {
    com.yahia.ideaflow.ui.AnimatedNavigator.Direction dir =
            "inbox".equals(key) ? com.yahia.ideaflow.ui.AnimatedNavigator.Direction.RIGHT
            : com.yahia.ideaflow.ui.AnimatedNavigator.Direction.LEFT;

    // Special cases: details should feel like drilling in
    if ("details".equals(key)) dir = com.yahia.ideaflow.ui.AnimatedNavigator.Direction.LEFT;
    if ("inbox".equals(key) && "details".equals(content.getCurrentKey()))
        dir = com.yahia.ideaflow.ui.AnimatedNavigator.Direction.RIGHT;

    content.show(key, dir);

    if ("inbox".equals(key)) inboxScreen.refresh();
    if ("board".equals(key)) boardScreen.refresh();
}


    private void openDetails(Idea idea) {
        detailsScreen.setIdea(idea);
        navigateTo("details");
    }

    private void backToInbox() {
        navigateTo("inbox");
    }

    private void onQuickAdd() {
        String title = JOptionPane.showInputDialog(
                this,
                "Idea title:",
                "Quick Add",
                JOptionPane.PLAIN_MESSAGE
        );

        if (title == null) return;
        title = title.trim();
        if (title.isEmpty()) return;

        store.addIdea(new Idea(title, ""));
        inboxScreen.refresh();
        boardScreen.refresh();
    }

    private void onSearchChanged(String query) {
        inboxScreen.setSearch(query);
        inboxScreen.refresh();
    }
}
