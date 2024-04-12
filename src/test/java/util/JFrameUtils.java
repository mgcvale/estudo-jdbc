package util;

import javax.swing.*;
import java.awt.*;

public abstract class JFrameUtils {
    public static Point getCenterCoordinates(JFrame frame) {
        int x = frame.getX();
        int y = frame.getY();
        int width = frame.getWidth();
        int height = frame.getHeight();
        return new Point(x + width/2, y + height/2);
    }
    public static Point getCenteredPositionForChild(JFrame frame, Container container) {
        Point frameCenter = getCenterCoordinates(frame);
        int width = container.getWidth();
        int height = container.getHeight();
        Point result = new Point(frameCenter.x - width/2, frameCenter.y - height/2);
        return new Point(frameCenter.x - width/2, frameCenter.y - height/2);
    }
}
