package de.jfxstructo.elements;

import de.jfxstructo.Board;
import de.jfxstructo.graphics.Frame;

public class For extends AElement {
	
	private Subqueue q = new Subqueue();
	private Frame qFrame = new Frame();

	public For() {
		super();
		q.setParent(this);
		
		q.addElement(new If());
	}

	public For(String text) {
		super(text);
		q.setParent(this);
	}
	
	@Override
	public AElement clone() {
		AElement ele = new For(text.getText());
		((For) ele).setQueue((Subqueue) this.q.clone());
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
		
		// draw shape
		frame = top_left.copy();
		frame.setBottom(frame.getTop()+height);
		board.drawRect(top_left);
		
		double x_text = frame.getLeft() + Math.round(E_PADDING / 2);
		double y_text = frame.getTop() + Math.round(E_PADDING / 2) + board.getTextHeight(text.getText());
		board.fillText(text.getText(), x_text, y_text);

		// draw children
		myFrame = top_left.copy();
		myFrame.setLeft(myFrame.getLeft() + E_PADDING - 1);
		myFrame.setTop((int) (top_left.getTop() + board.getTextHeight(text.getText()) + 2 * Math.round(E_PADDING / 2) - 1));
		myFrame.setBottom(myFrame.getBottom());
		
		q.draw(board, myFrame);
		
//		board.drawRect(frame, Color.AQUA);
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

	private void setQueue(Subqueue queue) {
		q = queue;
		q.setParent(this);
	}
	
}
