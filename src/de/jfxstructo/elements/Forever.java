package de.jfxstructo.elements;

import de.jfxstructo.Board;
import de.jfxstructo.graphics.Frame;

public class Forever extends AElement {

	private Subqueue q = new Subqueue();
	private Frame qFrame = new Frame();

	public Forever() {
		super();
		q.setParent(this);
	}

	public Forever(String string) {
		super(string);
		q.setParent(this);
	}

	@Override
	public AElement clone() {
		AElement ele = new Forever();
		((Forever) ele).setQueue((Subqueue) this.q.clone());
		((Forever) ele).q.parent = ele;
		return ele;
	}
	
	private void setQueue(Subqueue queue) {
		this.q = queue;
		q.setParent(this);
	}

	@Override
	public Frame prepareDraw(Board board) {
		frame.setTop(0);
		frame.setLeft(0);
		
		frame.setRight(2 * Math.round(E_PADDING/2));
		frame.setBottom((int) (2 * Math.round(E_PADDING/2)));
		
		qFrame = q.prepareDraw(board);
		
		frame.setRight(Math.max(frame.getRight(), qFrame.getRight() + E_PADDING));
		frame.setBottom(frame.getBottom() + qFrame.getBottom() +  E_PADDING);
		
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
				
		// draw children
		myFrame = top_left.copy();
		myFrame.setLeft(frame.getLeft() + E_PADDING - 1);
		myFrame.setTop((int) (top_left.getTop() + 2 * Math.round(E_PADDING / 2)));
		myFrame.setBottom(frame.getBottom() - E_PADDING);
		
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
}
