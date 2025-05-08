package ch.primeo.fridgely.factory;

import org.springframework.stereotype.Component;

import javax.swing.JFrame;
import java.awt.GraphicsEnvironment;

@Component
public class DefaultFrameFactory implements FrameFactory {
    public JFrame create(String title) {
        if (GraphicsEnvironment.isHeadless()) {
            throw new IllegalStateException("Cannot create JFrame in headless environment");
        }
        return new JFrame(title);
    }
}
