import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class Main
{
    public static void main(String[] args)
    {
        //Вікно (бібліотека swing)
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(500, 500);
        MainWindow mainWindow = new MainWindow();
        JPanel options = new JPanel();
        options.setLayout(new FlowLayout());

        //Вибір кольору
        JButton colorButton = new JButton("Колір");
        final Color[] lineColor = {Color.BLACK};
        colorButton.addActionListener(e -> {Color selectedColor = JColorChooser.showDialog(window, "Колір", lineColor[0]);
            if (selectedColor != null) {lineColor[0] = selectedColor; mainWindow.setLineColor(lineColor[0]);}});

        //Вибір товщини
        JLabel thicknessLabel = new JLabel("Товщина");
        JSpinner thicknessSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        thicknessSpinner.addChangeListener(e -> mainWindow.setLineThickness((int) thicknessSpinner.getValue()));

        //Додавання елементів
        options.add(colorButton);
        options.add(thicknessLabel);
        options.add(thicknessSpinner);
        window.add(options, BorderLayout.NORTH);
        window.add(mainWindow, BorderLayout.CENTER);
        window.setVisible(true);
    }
}

class DrawLine
{
    private int startX, startY, endX, endY;
    private Color color;
    private int thickness;

    public DrawLine(int startX, int startY, int endX, int endY, Color color, int thickness)
    {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.color = color;
        this.thickness = thickness;
    }

    //Getter (Для тестів)
    public int getStartX() { return startX; }
    public int getStartY() { return startY; }
    public int getEndX() { return endX; }
    public int getEndY() { return endY; }
    public Color getColor() { return color; }
    public int getThickness() { return thickness; }

    //Setter (Для тестів)
    public void setColor(Color color) { this.color = color; }
    public void setThickness(int thickness) { this.thickness = thickness; }

    public void draw(Graphics2D g2d)
    {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawLine(startX, startY, endX, endY);
    }
}


class MainWindow extends JPanel
{
    private Color lineColor = Color.BLACK;
    private int lineThickness = 1;
    private int startX, startY, endX, endY;
    private boolean drawing = false;
    private ArrayList<DrawLine> lines = new ArrayList<>();

    public MainWindow()
    {
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                startX = e.getX();
                startY = e.getY();
                drawing = true;
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (drawing)
                {
                    endX = e.getX();
                    endY = e.getY();
                    lines.add(new DrawLine(startX, startY, endX, endY, lineColor, lineThickness));
                    drawing = false;
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                if (drawing)
                {
                    endX = e.getX();
                    endY = e.getY();
                    repaint();
                }
            }
        });
    }

    //Getter (Для тестів)
    public ArrayList<DrawLine> getLines() {
        return lines;
    }

    public void setLineColor(Color color)
    {
        this.lineColor = color;
    }

    public void setLineThickness(int thickness)
    {
        this.lineThickness = thickness;
    }

    @Override
    protected void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D) graphics;

        for (DrawLine drawLine : lines)
        {
            drawLine.draw(g2d);
        }

        if (drawing)
        {
            g2d.setColor(lineColor);
            g2d.setStroke(new BasicStroke(lineThickness));
            g2d.drawLine(startX, startY, endX, endY);
        }
    }
}