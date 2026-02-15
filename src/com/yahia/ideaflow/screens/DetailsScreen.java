package com.yahia.ideaflow.screens;

import com.yahia.ideaflow.model.Idea;
import com.yahia.ideaflow.model.IdeaStore;
import com.yahia.ideaflow.theme.Colors;
import com.yahia.ideaflow.ui.RoundedButton;
import com.yahia.ideaflow.ui.RoundedPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Idea details editor: title/body, sliders, status selector.
 */
public final class DetailsScreen extends RoundedPanel {

    private final IdeaStore store;
    private final Runnable onBack;

    private Idea idea;

    private final JTextField titleField = new JTextField();
    private final JTextArea bodyArea = new JTextArea();

    private final JSlider urgency = new JSlider(0, 3, 1);
    private final JSlider impact = new JSlider(0, 3, 1);
    private final JSlider effort = new JSlider(0, 3, 1);
    private final JComboBox<Idea.Energy> energy = new JComboBox<>(Idea.Energy.values());
    private final JComboBox<Idea.Status> status = new JComboBox<>(Idea.Status.values());

    private final JLabel scoreLabel = new JLabel("Score: 0");

    public DetailsScreen(IdeaStore store, Runnable onBack) {
        super(22, Colors.BG);
        this.store = store;
        this.onBack = onBack;

        setLayout(new BorderLayout(12, 12));

        RoundedPanel header = new RoundedPanel(18, Colors.SURFACE);
        header.setShadow(true);
        header.setOutline(true);
        header.setLayout(new BorderLayout(12, 12));
        header.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        RoundedButton back = new RoundedButton("← Back", RoundedButton.Kind.GHOST, 14);
        back.addActionListener(e -> {
            save();
            onBack.run();
        });

        JLabel title = new JLabel("Idea Details");
        title.setForeground(Colors.TEXT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));

        header.add(back, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0;
        gc.weightx = 1;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(0, 0, 12, 0);

        body.add(cardEditor(), gc);
        gc.gridy++;
        body.add(cardControls(), gc);

        gc.gridy++;
        gc.weighty = 1;
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        body.add(spacer, gc);

        JScrollPane sp = new JScrollPane(body);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setOpaque(false);
        sp.setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.getVerticalScrollBar().setUI(new com.yahia.ideaflow.ui.ModernScrollBarUI(12));

        add(sp, BorderLayout.CENTER);

        styleSlider(urgency);
        styleSlider(impact);
        styleSlider(effort);
        hookRecalc();
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
        titleField.setText(idea.title);
        bodyArea.setText(idea.body == null ? "" : idea.body);

        urgency.setValue(idea.urgency);
        impact.setValue(idea.impact);
        effort.setValue(idea.effort);
        energy.setSelectedItem(idea.energy);
        status.setSelectedItem(idea.status);

        recalcScore();
    }

    private RoundedPanel cardEditor() {
        RoundedPanel card = new RoundedPanel(18, Colors.SURFACE);
        card.setShadow(true);
        card.setOutline(true);
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel t = new JLabel("Title");
        t.setForeground(Colors.MUTED_TEXT);
        card.add(t, BorderLayout.NORTH);

        titleField.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        RoundedPanel wrap = new RoundedPanel(16, Colors.SURFACE_2);
        wrap.setOutline(true);
        wrap.setLayout(new BorderLayout());
        wrap.add(titleField, BorderLayout.CENTER);

        card.add(wrap, BorderLayout.CENTER);

        // body
        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setOpaque(false);
        JLabel b = new JLabel("Notes");
        b.setForeground(Colors.MUTED_TEXT);
        bottom.add(b, BorderLayout.NORTH);

        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        bodyArea.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        RoundedPanel bodyWrap = new RoundedPanel(16, Colors.SURFACE_2);
        bodyWrap.setOutline(true);
        bodyWrap.setLayout(new BorderLayout());
        bodyWrap.add(bodyArea, BorderLayout.CENTER);

        bottom.add(bodyWrap, BorderLayout.CENTER);

        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    private RoundedPanel cardControls() {
        RoundedPanel card = new RoundedPanel(18, Colors.SURFACE);
        card.setShadow(true);
        card.setOutline(true);
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0;
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 0, 6, 0);

        scoreLabel.setForeground(Colors.TEXT);
        scoreLabel.setFont(scoreLabel.getFont().deriveFont(Font.BOLD, 14f));

        card.add(scoreLabel, gc);

        gc.gridy++;
        card.add(row("Urgency", urgency), gc);
        gc.gridy++;
        card.add(row("Impact", impact), gc);
        gc.gridy++;
        card.add(row("Effort", effort), gc);

        gc.gridy++;
        card.add(row("Energy", energy), gc);
        gc.gridy++;
        card.add(row("Status", status), gc);

        gc.gridy++;
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);

        RoundedButton save = new RoundedButton("Save", RoundedButton.Kind.PRIMARY, 14);
        save.addActionListener(e -> save());

        RoundedButton delete = new RoundedButton("Delete", RoundedButton.Kind.DANGER, 14);
        delete.addActionListener(e -> {
            if (idea == null) return;
            int ok = JOptionPane.showConfirmDialog(this, "Delete this idea?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                store.removeIdea(idea.id);
                onBack.run();
            }
        });

        actions.add(delete);
        actions.add(save);
        card.add(actions, gc);

        return card;
    }

    private JPanel row(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setOpaque(false);

        JLabel l = new JLabel(label);
        l.setForeground(Colors.MUTED_TEXT);
        l.setPreferredSize(new Dimension(90, 24));

        comp.setPreferredSize(new Dimension(200, 36));
        p.add(l, BorderLayout.WEST);
        p.add(comp, BorderLayout.CENTER);

        return p;
    }

    private void hookRecalc() {
        urgency.addChangeListener(e -> recalcScore());
        impact.addChangeListener(e -> recalcScore());
        effort.addChangeListener(e -> recalcScore());
    }

    private void recalcScore() {
        Idea temp = (idea != null) ? idea : new Idea("", "");
        temp.urgency = urgency.getValue();
        temp.impact = impact.getValue();
        temp.effort = effort.getValue();
        scoreLabel.setText("Score: " + temp.score());
    }

    private void styleSlider(JSlider s) {
        s.setOpaque(false);
        s.setPaintTicks(false);
        s.setPaintLabels(false);
        s.setFocusable(false);
    }

    private void save() {
        if (idea == null) return;
        idea.title = titleField.getText().trim();
        idea.body = bodyArea.getText();

        idea.urgency = urgency.getValue();
        idea.impact = impact.getValue();
        idea.effort = effort.getValue();
        idea.energy = (Idea.Energy) energy.getSelectedItem();
        idea.status = (Idea.Status) status.getSelectedItem();

        recalcScore();
    }
}
