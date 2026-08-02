package ui.gui;

import graph.Edge;
import graph.Graph;
import graph.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Draws the campus as a blueprint-style line drawing: faint grid backdrop,
 * cyan paths, white-ringed location markers, and — when a route has been
 * calculated — an amber dashed "trail" tracing the computed path, with a
 * green ring on the start marker and a coral pin on the destination.
 */
public class CampusMapPanel extends JPanel {

    private final Graph graph;
    private List<String> highlightedPath = null;
    private String startId = null;
    private String endId = null;

    public CampusMapPanel(Graph graph) {
        this.graph = graph;
        setBackground(Theme.NAVY_BG);
        setPreferredSize(new Dimension(920, 560));
    }

    public void showRoute(List<String> path, String start, String end) {
        this.highlightedPath = path;
        this.startId = start;
        this.endId = end;
        repaint();
    }

    public void clearRoute() {
        this.highlightedPath = null;
        this.startId = null;
        this.endId = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2);
        drawEdges(g2);
        if (highlightedPath != null && highlightedPath.size() > 1) {
            drawHighlightedPath(g2);
        }
        drawNodes(g2);
        drawCompassRose(g2);
    }

    private void drawGrid(Graphics2D g2) {
        g2.setColor(Theme.GRID_LINE);
        g2.setStroke(new BasicStroke(1f));
        int step = 40;
        for (int x = 0; x < getWidth(); x += step) {
            g2.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += step) {
            g2.drawLine(0, y, getWidth(), y);
        }
    }

    private boolean isOnHighlightedPath(String a, String b) {
        if (highlightedPath == null) return false;
        for (int i = 0; i < highlightedPath.size() - 1; i++) {
            String x = highlightedPath.get(i);
            String y = highlightedPath.get(i + 1);
            if ((x.equals(a) && y.equals(b)) || (x.equals(b) && y.equals(a))) return true;
        }
        return false;
    }

    private void drawEdges(Graphics2D g2) {
        Set<String> drawn = new HashSet<>();
        for (String nodeId : graph.allNodeIds()) {
            Point p1 = GraphLayout.get(nodeId);
            for (Edge e : graph.getNeighbors(nodeId)) {
                String key1 = nodeId + "-" + e.getToNode();
                String key2 = e.getToNode() + "-" + nodeId;
                if (drawn.contains(key1) || drawn.contains(key2)) continue;
                drawn.add(key1);

                if (isOnHighlightedPath(nodeId, e.getToNode())) continue; // drawn later, on top

                Point p2 = GraphLayout.get(e.getToNode());
                g2.setColor(e.hasStairs() ? Theme.CYAN_DIM : Theme.CYAN_LINE);
                float[] dash = e.hasStairs() ? new float[]{2f, 5f} : null;
                g2.setStroke(dash != null
                        ? new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, dash, 0)
                        : new BasicStroke(1.8f));
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    private void drawHighlightedPath(Graphics2D g2) {
        g2.setColor(Theme.AMBER);
        float[] dash = {10f, 6f};
        g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, dash, 0));
        for (int i = 0; i < highlightedPath.size() - 1; i++) {
            Point p1 = GraphLayout.get(highlightedPath.get(i));
            Point p2 = GraphLayout.get(highlightedPath.get(i + 1));
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    private void drawNodes(Graphics2D g2) {
        for (String nodeId : graph.allNodeIds()) {
            Node node = graph.getNode(nodeId);
            Point p = GraphLayout.get(nodeId);

            boolean isStart = nodeId.equals(startId);
            boolean isEnd = nodeId.equals(endId);
            boolean onPath = highlightedPath != null && highlightedPath.contains(nodeId);

            int r = isStart || isEnd ? 11 : 7;
            Ellipse2D circle = new Ellipse2D.Double(p.x - r, p.y - r, r * 2, r * 2);

            if (isStart) {
                g2.setColor(Theme.GREEN_START);
            } else if (isEnd) {
                g2.setColor(Theme.RED_END);
            } else if (onPath) {
                g2.setColor(Theme.AMBER);
            } else {
                g2.setColor(Theme.NAVY_BG);
            }
            g2.fill(circle);

            g2.setColor(onPath || isStart || isEnd ? Color.WHITE : Theme.CYAN_LINE);
            g2.setStroke(new BasicStroke(1.6f));
            g2.draw(circle);

            g2.setFont(Theme.FONT_MONO_LABEL);
            g2.setColor(Theme.TEXT_LIGHT);
            FontMetrics fm = g2.getFontMetrics();
            String label = node.getName() + (node.hasRamp() ? "" : " *");
            int labelWidth = fm.stringWidth(label);
            g2.drawString(label, p.x - labelWidth / 2, p.y + r + 16);
        }
    }

    private void drawCompassRose(Graphics2D g2) {
        int cx = getWidth() - 55;
        int cy = 50;
        g2.setColor(Theme.CYAN_DIM);
        g2.setStroke(new BasicStroke(1.4f));
        g2.drawOval(cx - 22, cy - 22, 44, 44);
        g2.drawLine(cx, cy - 22, cx, cy + 22);
        g2.drawLine(cx - 22, cy, cx + 22, cy);
        g2.setFont(Theme.FONT_MONO_LABEL);
        g2.setColor(Theme.TEXT_DIM);
        g2.drawString("N", cx - 4, cy - 26);
        g2.drawString("* = stairs-only access", getWidth() - 200, getHeight() - 12);
    }
}
