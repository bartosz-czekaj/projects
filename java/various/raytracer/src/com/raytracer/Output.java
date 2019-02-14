package com.raytracer;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.BiFunction;

public class Output extends JPanel {

    private BufferedImage canvas;
    private static Output instance = null;

    private Output(int width, int height) {

        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < canvas.getHeight(); i++){
            for(int j = 0; j < canvas.getWidth(); j++){
                canvas.setRGB(j, i, Color.BLUE.getRGB());
            }
        }
        this.setPreferredSize(new Dimension(width, height));

    }

    public int getCanvasWidth(){
        return canvas.getWidth();
    }

    public int getCanvasHeight(){
        return canvas.getHeight();
    }

    public static Output getOutput() {
        if(Output.instance == null) {
            final int WIDTH = 640;
            final int HEIGHT = 480;
            Output.instance = new Output(WIDTH, HEIGHT);
        }

        return Output.instance;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.drawImage(canvas, null, null);
    }

    public static void setPixel(int x, int y, int rgb) {
        Output panel = getOutput();
        BiFunction<Integer, Integer, Boolean> isOutsideTheBox =(Integer pixel, Integer dimension) -> {
            return pixel < 0 || pixel >= dimension;
        };

        if(isOutsideTheBox.apply(y, panel.canvas.getHeight()) || isOutsideTheBox.apply(x, panel.canvas.getWidth())) {
            return;
        }

        panel.canvas.setRGB(x, y, rgb);
        panel.repaint();
    }


    public static void main(String[] args) {

	    Output panel = getOutput();
        new RayTracer().trace();

        JFrame frame = new JFrame("Direct raytracing demo");
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
