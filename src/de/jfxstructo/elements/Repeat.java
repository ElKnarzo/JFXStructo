package de.jfxstructo.elements;

import de.jfxstructo.Board;
import de.jfxstructo.graphics.Frame;

public class Repeat extends AElement {

	private Subqueue q = new Subqueue();
	private Frame qFrame = new Frame();

	public Repeat() {
		super();
		q.setParent(this);
	}

	public Repeat(String text) {
		super(text);
		q.setParent(this);
	}

	private void setQueue(Subqueue q) {
		q.setParent(this);
		this.q = q;
	}

	@Override
	public AElement clone() {
		AElement ele = new Repeat(text.getText());
		Subqueue s = (Subqueue) q.clone();
		((Repeat) ele).setQueue(s);
		return ele;
	}
	
	@Override
	public Frame prepareDraw(Board board) {
		frame.setTop(0);
		frame.setLeft(0);
		
		frame.setRight(2 * Math.round(E_PADDING/2));
		if (frame.getRight() < board.getTextWidth(text.getText()) + 2 * Math.round(E_PADDING / 2)) {
			frame.setRight((int) (board.getTextWidth(text.getText()) + 2 * Math.round(E_PADDING / 2)));
		}
		
		frame.setBottom((int) (2 * Math.round(E_PADDING/2) + board.getTextHeight(text.getText())));
		
		qFrame = q.prepareDraw(board);
		
		frame.setRight(Math.max(frame.getRight(), qFrame.getRight() + E_PADDING));
		frame.setBottom(frame.getBottom() + qFrame.getBottom()  +  E_PADDING);	
		
		setDimension();
		
		return frame;
	}

	@Override
	public void draw(Board board, Frame top_left) {
		Frame myFrame = top_left.copy();
		board.fillRect(myFrame);
		
		frame = top_left.copy();
		
		// draw shape
		frame.setBottom(frame.getTop()+height);
		board.drawRect(top_left);

		// draw children
		myFrame = top_left.copy();
		myFrame.setLeft(myFrame.getLeft() + E_PADDING - 1);
		myFrame.setTop(top_left.getTop());
		myFrame.setBottom((int) (frame.getBottom() - board.getTextHeight(text.getText()) - 2 * Math.round(E_PADDING / 2) - 1));
		
		q.draw(board, myFrame);
		
		double x_text = frame.getLeft() + Math.round(E_PADDING / 2);
		double y_text = frame.getBottom() - Math.round(E_PADDING / 2);
		board.fillText(text.getText(), x_text, y_text);
		
//		board.drawRect(myFrame, Color.AQUA);
	}

	@Override
	public AElement selectElementByCoord(double x, double y) {
		AElement selMe = super.selectElementByCoord(x,y);
		AElement sel = q.selectElementByCoord(x,y);
		if(sel!=null) {
			selected=false;
			selMe = sel;
		}
		
		return selMe;
	}
	
}
