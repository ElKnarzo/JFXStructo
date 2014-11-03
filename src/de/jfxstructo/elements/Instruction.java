package de.jfxstructo.elements;

import de.jfxstructo.Board;
import de.jfxstructo.graphics.Frame;

public class Instruction extends Element {

	
	
	public Instruction() {
		super();
	}

	public Instruction(String text) {
		super(text);
	}

	@Override
	public Frame prepareDraw(Board board) {
		frame.setTop(0);
		frame.setLeft(0);
		frame.setRight(0);
		frame.setBottom(0);
				
		frame.setRight(Math.round(2*(Element.E_PADDING/2)));
		if(frame.getRight() < board.getTextWidth(text.getText()) + E_PADDING) {
			frame.setRight((int) (board.getTextWidth(text.getText()) + E_PADDING));
		}
		frame.setBottom((int) (2 * Math.round(E_PADDING/2) + board.getTextHeight(text.getText())));
	
		setDimension();
		
		return frame;
	}

	@Override
	public void draw(Board board, Frame top_left) {
		frame = top_left.copy();
		frame.setBottom(frame.getTop()+height);
		
		board.fillRect(frame);
		board.drawRect(top_left);
		
		double x_text = frame.getLeft() + Math.round(E_PADDING / 2);
		double y_text = frame.getTop() + Math.round(E_PADDING / 2) + board.getTextHeight(text.getText());
		board.fillText(text.getText(), x_text, y_text);
		
//		board.fillRect(frame, Color.AZURE);
	}

	@Override
	public Element clone() {
		Instruction ele = new Instruction(text.getText());
		return ele;
	}
}
