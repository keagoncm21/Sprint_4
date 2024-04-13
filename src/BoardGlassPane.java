import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//class for the glass pane that draws lines representing SOS sequences
class BoardGlassPane extends JPanel {
    private final List<LineInfo> lines = new ArrayList<>();

    //method to update the bounds of the glass pane based on the parent panel size
    public void updateBounds(JPanel parentPanel) {
        int width = parentPanel.getWidth();
        int height = parentPanel.getHeight();
        setBounds(0, 0, width, height);
    }

    //constructor to initialize the glass pane and make it transparent
    public BoardGlassPane() {
        setOpaque(false); //make the panel transparent
    }

    //override paintComponent to draw the lines on the glass pane
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (LineInfo line : lines) {
            g.setColor(line.color);
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
    }

    //method to add a line to the glass pane and repaint it
    public void addLine(int x1, int y1, int x2, int y2, Color color) {
        lines.add(new LineInfo(x1, y1, x2, y2, color));
        repaint(); // Tell the panel to repaint itself
    }

    //method to clear all lines from the glass pane and repaint it
    public void clearLines() {
        lines.clear();
        repaint();
    }

    //inner class to represent line information (start and end points, color)
    private static class LineInfo {
        final int x1, y1, x2, y2;
        final Color color;


        LineInfo(int x1, int y1, int x2, int y2, Color color) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
        }
    }

    //used for testing
    public int getLinesCount() {
        return lines.size();
    }

}