package ch.primeo.fridgely.factory;

import javax.swing.JFrame;

public interface FrameFactory {
    JFrame create(String title);
}
