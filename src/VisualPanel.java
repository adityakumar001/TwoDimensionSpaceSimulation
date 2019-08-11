import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

public class VisualPanel extends JPanel {
    private ArrayList<ArrayList<SpaceObject>> spaceObject;

    private KeyBoard keys;
    private double rotation = 0;
    private double radian = Math.PI / 180;
    private int ySpace = Main.HEIGHT + 600;
    private int xSpace = Main.WIDTH + 600;

    VisualPanel() {
        setFocusable(true);
        Random random = new Random();

        int[] layers = {random.nextInt(10) + 15, random.nextInt(15) + 25, random.nextInt(15) + 15,
                random.nextInt(10) + 10};

        spaceObject = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ArrayList<SpaceObject> layer = new ArrayList<>();
            for (int k = 0; k < layers[i]; k++) {
                layer.add(new SpaceObject(i));
            }
            spaceObject.add(layer);
        }

        keys = new KeyBoard();
        addKeyListener(keys);

        AnimationThread animationThread = new AnimationThread();
        animationThread.start();

    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHints(renderingHints);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.translate(Main.WIDTH / 2, Main.HEIGHT / 2);
        if (rotation != 0) {
            layComponents(g2d, rotation);
        } else {
            layComponents(g2d);
        }
    }

    private void layComponents(Graphics2D g2d) {
        for (int i = 0; i < 3; i++) {
            ArrayList<SpaceObject> layer = spaceObject.get(i);
            for (int k = 0; k < layer.size(); k++) {
                g2d.setColor(layer.get(k).fillColor);
                g2d.fill(layer.get(k).shape);
            }
        }

        for (int i = 0; i < spaceObject.get(3).size(); i++) {
            g2d.setColor(spaceObject.get(3).get(i).fillColor);
            g2d.fill(spaceObject.get(3).get(i).shape);
        }

    }

    private void layComponents(Graphics2D g2d, double rotation) {

        for (int i = 0; i < 3; i++) {
            ArrayList<SpaceObject> layer = spaceObject.get(i);
            g2d.rotate(rotation - 5 * i + 5);
            for (int k = 0; k < layer.size(); k++) {
                g2d.setColor(layer.get(k).fillColor);
                g2d.fill(layer.get(k).shape);
            }
        }
        g2d.rotate(rotation);
        for (int i = 0; i < spaceObject.get(3).size(); i++) {
            g2d.setColor(spaceObject.get(3).get(i).fillColor);
            g2d.fill(spaceObject.get(3).get(i).shape);
        }
    }

    private void updateEnv() {
        if (keys.rotateAntiClock && rotation != -180 * radian) {
            rotation -= 1 * radian;
        } else if (keys.rotateClock && rotation != +180 * radian) {
            rotation += 1 * radian;
        }
        for (int i = 0; i < spaceObject.size(); i++) {
            for (SpaceObject spaceObject : spaceObject.get(i)) {
                if (keys.up)
                    spaceObject.shape.y += spaceObject.yVel - 1;
                else if (keys.down)
                    spaceObject.shape.y += spaceObject.yVel + 1;
                else
                    spaceObject.shape.y += spaceObject.yVel;

                if (keys.left)
                    spaceObject.shape.x += spaceObject.xVel - 1;
                else if (keys.right)
                    spaceObject.shape.x += spaceObject.xVel + 1;
                else
                    spaceObject.shape.x += spaceObject.xVel;

                if (spaceObject.shape.x > (double) xSpace / 2) {
                    spaceObject.shape.x = (double) -xSpace / 2;
                } else if (spaceObject.shape.x < (double) -xSpace / 2) {
                    spaceObject.shape.x = (double) xSpace / 2;
                } else if (spaceObject.shape.y > (double) ySpace / 2) {
                    spaceObject.shape.y = (double) -ySpace / 2;
                } else if (spaceObject.shape.y < (double) -ySpace / 2) {
                    spaceObject.shape.y = (double) ySpace / 2;
                }

            }
        }
    }

    class SpaceObject {
        Ellipse2D.Double shape;
        Color fillColor;
        Random random = new Random();
        double xVel, yVel;
        int radius;
        private int layer;

        SpaceObject(int layer) {
            shape = new Ellipse2D.Double();
            this.layer = layer;
            assignWidth();
            assignVelocity();
            assignColor();
            shape.y = random.nextInt(ySpace) - (ySpace) / 2;
            shape.x = random.nextInt(xSpace) - (xSpace) / 2;
        }

        private void assignColor() {
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);
            int alpha;
            switch (layer) {
                case 0:
                    alpha = random.nextInt(50) + 50;
                    fillColor = new Color(red, green, blue, alpha);
                    break;
                case 1:
                    alpha = random.nextInt(50) + 100;
                    fillColor = new Color(red, green, blue, alpha);
                    break;
                case 2:
                    alpha = random.nextInt(100) + 150;
                    fillColor = new Color(red, green, blue, alpha);
                    break;
                case 3:
                    fillColor = new Color(red, green, blue);
                    break;
            }
        }

        private void assignVelocity() {
            switch (layer) {
                case 0:
                    xVel = random.nextDouble() - 0.5;
                    yVel = random.nextDouble() - 0.5;
                    break;
                case 1:
                    xVel = Math.random() * 2 - 0.5;
                    yVel = Math.random() * 2 - 0.5;
                    break;
                case 2:
                    xVel = Math.random() * 4 - 1;
                    yVel = Math.random() * 4 - 1;
                    break;
                case 3:
                    xVel = Math.random() * 4 - 0.5;
                    yVel = Math.random() * 4 - 0.5;
                    break;
            }
        }

        private void assignWidth() {
            switch (layer) {
                case 0:
                    radius = random.nextInt(15) + 10;
                    shape.height = radius;
                    shape.width = radius;
                    break;
                case 1:
                    radius = random.nextInt(25) + 20;
                    shape.height = radius;
                    shape.width = radius;
                    break;
                case 2:
                    radius = random.nextInt(30) + 40;
                    shape.height = radius;
                    shape.width = radius;
                    break;
                case 3:
                    radius = random.nextInt(40) + 60;
                    shape.height = radius;
                    shape.width = radius;
                    break;
            }
        }
    }

    class AnimationThread extends Thread {
        public void run() {
            long prev = System.nanoTime();
            while (true)
                if ((System.nanoTime() - prev) * 60 / Math.pow(10, 9) > 1) {
                    prev = System.nanoTime();
                    keys.update();
                    updateEnv();
                    repaint();
                }
        }
    }
}
