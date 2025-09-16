package io.github.client.util.java.interfaces;

import imgui.ImGuiIO;

/**
 * @author <a href="https://github.com/FlorianMichael/fabric-imgui-example-mod">fabric imgui example mod</a>
 */
@FunctionalInterface
public interface IRender {
    void render(final ImGuiIO io);
}