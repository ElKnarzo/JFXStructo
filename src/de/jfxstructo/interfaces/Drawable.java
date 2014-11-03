package de.jfxstructo.interfaces;

import de.jfxstructo.Board;
import de.jfxstructo.graphics.Frame;

public interface Drawable {

	public Frame prepareDraw(Board b);
	public void draw(Board b, Frame top_left);
}
