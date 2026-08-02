package ui.gui;

import algorithms.MultiStopOptimizer;
import core.CampusMapData;
import core.RouteFormatter;
import core.RoutePlanner;
import core.RouteResult;
import graph.Graph;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Desktop entry point. Layout: a dark control sidebar (west), the blueprint
 * map as the main stage (center), and a cream "route ticket" printout along
 * the bottom — evoking the printed slip you'd get from a campus help desk.
 */
public class MainWindow extends JFrame {

    private final Graph graph = CampusMapData.buildSampleCampus();

    private JComboBox<String> modeBox;
    private JComboBox<String> startBox;
    private JComboBox<String> endBox;
    private JList<String> stopsList;
    private JCheckBox congestionCheck;
    private CampusMapPanel mapPanel;
    private JTextArea ticketArea;

    public MainWindow() {
        super("Campus Wayfinder — Graph & Dijkstra Route Engine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.NAVY_BG);

        add(buildSidebar(), BorderLayout.WEST);
        mapPanel = new CampusMapPanel(graph);
        add(mapPanel, BorderLayout.CENTER);
        add(buildTicketPanel(), BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(1150, 760));
        setLocationRelativeTo(null);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(Theme.NAVY_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 18, 20, 18));

        sidebar.add(label("CAMPUS", Theme.FONT_TITLE, Theme.TEXT_LIGHT));
        sidebar.add(label("WAYFINDER", Theme.FONT_TITLE, Theme.AMBER));
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(label("Graph + Dijkstra route engine", Theme.FONT_MONO_LABEL, Theme.TEXT_DIM));
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(divider());
        sidebar.add(Box.createVerticalStrut(16));

        sidebar.add(label("MODE", Theme.FONT_CHROME_BOLD, Theme.TEXT_DIM));
        sidebar.add(Box.createVerticalStrut(6));
        modeBox = new JComboBox<>(new String[]{"Shortest Route", "Multi-Stop Route", "Accessible Route"});
        styleCombo(modeBox);
        modeBox.addActionListener(e -> updateModeVisibility());
        sidebar.add(modeBox);
        sidebar.add(Box.createVerticalStrut(16));

        sidebar.add(label("FROM", Theme.FONT_CHROME_BOLD, Theme.TEXT_DIM));
        sidebar.add(Box.createVerticalStrut(6));
        startBox = new JComboBox<>(locationNames());
        styleCombo(startBox);
        sidebar.add(startBox);
        sidebar.add(Box.createVerticalStrut(16));

        sidebar.add(label("TO", Theme.FONT_CHROME_BOLD, Theme.TEXT_DIM));
        sidebar.add(Box.createVerticalStrut(6));
        endBox = new JComboBox<>(locationNames());
        if (endBox.getItemCount() > 1) endBox.setSelectedIndex(1);
        styleCombo(endBox);
        sidebar.add(endBox);
        sidebar.add(Box.createVerticalStrut(16));

        sidebar.add(label("STOPS (multi-stop mode)", Theme.FONT_CHROME_BOLD, Theme.TEXT_DIM));
        sidebar.add(Box.createVerticalStrut(6));
        stopsList = new JList<>(locationNames());
        stopsList.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        stopsList.setBackground(Theme.NAVY_BG);
        stopsList.setForeground(Theme.TEXT_LIGHT);
        stopsList.setSelectionBackground(Theme.AMBER_DIM);
        stopsList.setFont(Theme.FONT_MONO);
        JScrollPane stopsScroll = new JScrollPane(stopsList);
        stopsScroll.setPreferredSize(new Dimension(220, 110));
        stopsScroll.setMaximumSize(new Dimension(220, 110));
        stopsScroll.setBorder(BorderFactory.createLineBorder(Theme.GRID_LINE));
        sidebar.add(stopsScroll);
        sidebar.add(Box.createVerticalStrut(16));

        congestionCheck = new JCheckBox("Apply time-based congestion");
        congestionCheck.setSelected(true);
        congestionCheck.setOpaque(false);
        congestionCheck.setForeground(Theme.TEXT_LIGHT);
        congestionCheck.setFont(Theme.FONT_MONO_LABEL);
        congestionCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(congestionCheck);
        sidebar.add(Box.createVerticalStrut(20));

        RoundedButton findButton = new RoundedButton("FIND ROUTE", Theme.AMBER,
                Theme.AMBER.brighter(), Theme.NAVY_BG);
        findButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        findButton.setMaximumSize(new Dimension(224, 42));
        findButton.addActionListener(e -> onFindRoute());
        sidebar.add(findButton);
        sidebar.add(Box.createVerticalStrut(10));

        RoundedButton resetButton = new RoundedButton("RESET MAP", Theme.NAVY_BG,
                Theme.GRID_LINE, Theme.TEXT_LIGHT);
        resetButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.CYAN_DIM), new EmptyBorder(9, 15, 9, 15)));
        resetButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        resetButton.setMaximumSize(new Dimension(224, 42));
        resetButton.addActionListener(e -> onReset());
        sidebar.add(resetButton);

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private JPanel buildTicketPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Theme.NAVY_BG);
        wrapper.setBorder(new EmptyBorder(0, 20, 20, 20));

        ticketArea = new JTextArea(6, 40);
        ticketArea.setEditable(false);
        ticketArea.setFont(Theme.FONT_MONO);
        ticketArea.setBackground(Theme.PAPER);
        ticketArea.setForeground(Theme.PAPER_INK);
        ticketArea.setBorder(new EmptyBorder(14, 18, 14, 18));
        ticketArea.setText("Select a mode and locations, then press FIND ROUTE.\nYour route printout will appear here.");
        ticketArea.setLineWrap(true);
        ticketArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(ticketArea);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.GRID_LINE, 2));
        scroll.setPreferredSize(new Dimension(0, 150));
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    private void updateModeVisibility() {
        boolean multiStop = modeBox.getSelectedItem().equals("Multi-Stop Route");
        endBox.setEnabled(!multiStop);
        stopsList.setEnabled(multiStop);
    }

    private void onFindRoute() {
        String mode = (String) modeBox.getSelectedItem();
        String start = idFromName((String) startBox.getSelectedItem());
        boolean useCongestion = congestionCheck.isSelected();

        try {
            if ("Multi-Stop Route".equals(mode)) {
                List<String> stops = new ArrayList<>();
                for (String name : stopsList.getSelectedValuesList()) stops.add(idFromName(name));

                MultiStopOptimizer.MultiStopResult result =
                        RoutePlanner.planMultiStopRoute(graph, start, stops, useCongestion, false);

                if (result.path() == null) {
                    ticketArea.setText("No route: " + result.message());
                    mapPanel.clearRoute();
                    return;
                }
                mapPanel.showRoute(result.path(), start, result.path().get(result.path().size() - 1));
                ticketArea.setText(RouteFormatter.formatSummary(graph, result.path(), result.cost(), result.message())
                        + "\n" + RouteFormatter.formatDirections(graph, result.path()));

            } else {
                String end = idFromName((String) endBox.getSelectedItem());
                boolean accessible = "Accessible Route".equals(mode);

                RouteResult result = RoutePlanner.planSingleRoute(graph, start, end, useCongestion, accessible);

                if (!result.isSuccess()) {
                    ticketArea.setText("No route: " + result.getMessage());
                    mapPanel.clearRoute();
                    return;
                }
                mapPanel.showRoute(result.getPath(), start, end);
                ticketArea.setText(RouteFormatter.formatSummary(graph, result.getPath(), result.getCost(), result.getMessage())
                        + "\n" + RouteFormatter.formatDirections(graph, result.getPath()));
            }
        } catch (IllegalArgumentException ex) {
            ticketArea.setText("Error: " + ex.getMessage());
        }
    }

    private void onReset() {
        mapPanel.clearRoute();
        ticketArea.setText("Select a mode and locations, then press FIND ROUTE.\nYour route printout will appear here.");
    }

    private String[] locationNames() {
        List<String> ids = new ArrayList<>(graph.allNodeIds());
        ids.sort(String::compareTo);
        String[] names = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) names[i] = graph.getNode(ids.get(i)).getName() + "  [" + ids.get(i) + "]";
        return names;
    }

    private String idFromName(String display) {
        int start = display.lastIndexOf('[');
        int end = display.lastIndexOf(']');
        return display.substring(start + 1, end);
    }

    private JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JPanel divider() {
        JPanel p = new JPanel();
        p.setMaximumSize(new Dimension(224, 1));
        p.setPreferredSize(new Dimension(224, 1));
        p.setBackground(Theme.GRID_LINE);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    private void styleCombo(JComboBox<String> box) {
        box.setFont(Theme.FONT_MONO);
        box.setBackground(Theme.NAVY_BG);
        box.setForeground(Theme.TEXT_LIGHT);
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.setMaximumSize(new Dimension(224, 32));
        box.setBorder(BorderFactory.createLineBorder(Theme.GRID_LINE));
    }
}
