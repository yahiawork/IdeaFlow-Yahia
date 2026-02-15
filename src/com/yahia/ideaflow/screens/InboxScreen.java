package com.yahia.ideaflow.screens;

import com.yahia.ideaflow.model.Idea;
import com.yahia.ideaflow.model.IdeaStore;
import com.yahia.ideaflow.theme.Colors;
import com.yahia.ideaflow.ui.IdeaCard;
import com.yahia.ideaflow.ui.RoundedPanel;
import com.yahia.ideaflow.ui.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Inbox list of ideas (cards).
 */
public final class InboxScreen extends RoundedPanel {

    private final IdeaStore store;
    private final Consumer<Idea> onOpen;

    private String search = "";

    private final JPanel list = new JPanel();
    private final JComboBox<String> sortBox = new JComboBox<>(new String[]{"Top score", "Newest"});
    private final RoundedButton sessionBtn = new RoundedButton("Start sorting (5 min)", RoundedButton.Kind.GHOST, 14);

    public InboxScreen(IdeaStore store, Consumer<Idea> onOpen) {
        super(22, Colors.BG);
        this.store = store;
        this.onOpen = onOpen;
        setLayout(new BorderLayout(12, 12));

        RoundedPanel header = new RoundedPanel(18, Colors.SURFACE);
        header.setShadow(true);
        header.setOutline(true);
        header.setLayout(new BorderLayout(12, 12));
        header.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel title = new JLabel("Inbox");
        title.setForeground(Colors.TEXT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        header.add(title, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);

        sortBox.setPreferredSize(new Dimension(140, 34));
        actions.add(sortBox);

        sessionBtn.addActionListener(e -> showSessionDialog());
        actions.add(sessionBtn);

        header.add(actions, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        list.setOpaque(false);
        list.setLayout(new GridBagLayout());

        JScrollPane sp = new JScrollPane(list);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setOpaque(false);
        sp.setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.getVerticalScrollBar().setUI(new com.yahia.ideaflow.ui.ModernScrollBarUI(12));

        add(sp, BorderLayout.CENTER);

        refresh();
    }

    public void setSearch(String query) {
        this.search = query == null ? "" : query;
    }

    public void refresh() {
        list.removeAll();

        java.util.List<Idea> ideas = new java.util.ArrayList<>(store.search(search));

        // Sort
        String sort = (String) sortBox.getSelectedItem();
        if ("Top score".equals(sort)) {
            ideas.sort(Comparator.comparingInt(Idea::score).reversed());
        } else {
            ideas.sort((a,b) -> b.createdAt.compareTo(a.createdAt));
        }

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(10, 10, 10, 10);

        for (Idea idea : ideas) {
            IdeaCard card = new IdeaCard(idea, onOpen);
            list.add(card, gc);
            gc.gridy++;
        }

        gc.weighty = 1;
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        list.add(spacer, gc);

        revalidate();
        repaint();
    }

    private void showSessionDialog() {
        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this), "Sorting session", Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        RoundedPanel root = new RoundedPanel(22, Colors.SURFACE);
        root.setShadow(true);
        root.setOutline(true);
        root.setLayout(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel head = new JLabel("Quick decisions (demo)");
        head.setForeground(Colors.TEXT);
        head.setFont(head.getFont().deriveFont(Font.BOLD, 16f));

        JTextArea info = new JTextArea(
                "This is a placeholder for the full 5-minute sorting flow.\n" +
                "In the full version: Keep/Delete, choose category, write next action."
        );
        info.setEditable(false);
        info.setOpaque(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setForeground(Colors.MUTED_TEXT);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        RoundedButton close = new RoundedButton("Close", RoundedButton.Kind.PRIMARY, 14);
        close.addActionListener(e -> dlg.dispose());
        buttons.add(close);

        root.add(head, BorderLayout.NORTH);
        root.add(info, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);

        dlg.setContentPane(root);
        dlg.pack();
        dlg.setSize(420, 240);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }
}
