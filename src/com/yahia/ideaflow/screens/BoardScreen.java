package com.yahia.ideaflow.screens;

import com.yahia.ideaflow.model.Idea;
import com.yahia.ideaflow.model.IdeaStore;
import com.yahia.ideaflow.theme.Colors;
import com.yahia.ideaflow.ui.IdeaCard;
import com.yahia.ideaflow.ui.RoundedPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Simple Kanban layout (no drag in this demo, but columns are real).
 */
public final class BoardScreen extends RoundedPanel {

    private final IdeaStore store;
    private final Consumer<Idea> onOpen;

    private final Column inboxCol;
    private final Column planningCol;
    private final Column doingCol;
    private final Column doneCol;

    public BoardScreen(IdeaStore store, Consumer<Idea> onOpen) {
        super(22, Colors.BG);
        this.store = store;
        this.onOpen = onOpen;

        setLayout(new BorderLayout(12, 12));

        RoundedPanel header = new RoundedPanel(18, Colors.SURFACE);
        header.setShadow(true);
        header.setOutline(true);
        header.setLayout(new BorderLayout(12, 12));
        header.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel title = new JLabel("Board");
        title.setForeground(Colors.TEXT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        header.add(title, BorderLayout.WEST);

        JLabel hint = new JLabel("Tip: open an idea and change its status.");
        hint.setForeground(Colors.MUTED_TEXT);
        header.add(hint, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(1, 4, 12, 12));
        grid.setOpaque(false);

        inboxCol = new Column("Inbox");
        planningCol = new Column("Planning");
        doingCol = new Column("Doing");
        doneCol = new Column("Done");

        grid.add(inboxCol);
        grid.add(planningCol);
        grid.add(doingCol);
        grid.add(doneCol);

        add(grid, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        inboxCol.setIdeas(store.byStatus(Idea.Status.INBOX));
        planningCol.setIdeas(store.byStatus(Idea.Status.PLANNING));
        doingCol.setIdeas(store.byStatus(Idea.Status.DOING));
        doneCol.setIdeas(store.byStatus(Idea.Status.DONE));
    }

    private final class Column extends RoundedPanel {
        private final JPanel list = new JPanel(new GridBagLayout());

        Column(String name) {
            super(18, Colors.SURFACE);
            setShadow(true);
            setOutline(true);
            setLayout(new BorderLayout(8, 8));
            setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

            JLabel title = new JLabel(name);
            title.setForeground(Colors.TEXT);
            title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
            add(title, BorderLayout.NORTH);

            list.setOpaque(false);

            JScrollPane sp = new JScrollPane(list);
            sp.setBorder(BorderFactory.createEmptyBorder());
            sp.getViewport().setOpaque(false);
            sp.setOpaque(false);
            sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.getVerticalScrollBar().setUI(new com.yahia.ideaflow.ui.ModernScrollBarUI(12));

            add(sp, BorderLayout.CENTER);
        }

        void setIdeas(List<Idea> ideas) {
            list.removeAll();

            GridBagConstraints gc = new GridBagConstraints();
            gc.gridx = 0;
            gc.gridy = 0;
            gc.weightx = 1;
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.insets = new Insets(8, 0, 8, 0);

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
    }
}
