package de.jfxstructo.elements;

import java.util.Vector;

import de.jfxstructo.Board;
import de.jfxstructo.graphics.Frame;

public class Case extends Element {

	private Vector<String> cases = new Vector<>();
	private Vector<Subqueue> qs = new Vector<>();
	
	private boolean hasElse = true;
	
	public Case() {
		super();
	}

	public Case(String text) {
		super(text);
	}

	public Case(String text, boolean hasElse) {
		super(text);
		this.hasElse = hasElse;
	}
	
	public Case(String text, String[] cases,boolean hasElse) {
		super(text);
		this.hasElse = hasElse;
		
		for (String string : cases) {
			this.cases.add(string);
			Subqueue q = new Subqueue();
			q.setParent(this);
			qs.add(q);
		}
		
//		qs.get(3).addElement(new For());
	}
	
	public void add(String caseStr, Subqueue q) {
		q.setParent(this);
		qs.add(q);
		cases.add(caseStr);
	}

	@Override
	public Element clone()  { // Problem here???
		Element ele = new Case(text.getText(), hasElse);
		for (int i = 0; i < qs.size(); i++) {
			Subqueue q = (Subqueue) qs.get(i).clone();
			((Case) ele).add(cases.get(i), q);
		}
		return ele;
	}
	
	@Override
	public Frame prepareDraw(Board b) {
		frame.setTop(0);
		frame.setLeft(0);
		
		frame.setRight(Math.round(2 * (E_PADDING / 2)));
		for (int i = 0; i < cases.size(); i++) {
			if(frame.getRight() < b.getTextWidth(cases.get(i)) + 2 * E_PADDING / 2) {
				frame.setRight((int) (b.getTextWidth(cases.get(i)) + 2 * E_PADDING / 2));
			}
		}
		frame.setBottom((int) (4 * Math.round(E_PADDING / 2) + 2 * b.getTextHeight(text.getText())));
		
		Frame rtt;
		
		int fullWidth = 0, maxHeight = 0;
		if(qs.size() != 0) {
			for (int i = 0; i < cases.size(); i++) {
				rtt = ((Subqueue) qs.get(i)).prepareDraw(b);
				fullWidth = (int) (fullWidth + Math.max(rtt.getRight(), b.getTextWidth(cases.get(i)) + Math.round(E_PADDING / 2)));
				if (maxHeight < rtt.getBottom()) {
					maxHeight = rtt.getBottom();
				}
			}
		}
		
		frame.setRight((int) (Math.max(frame.getRight(), fullWidth)+b.getTextWidth(text.getText()+Math.round(E_PADDING / 2))));
		frame.setBottom(frame.getBottom() + maxHeight);
		
		setDimension();
		
		return frame;
	}

	@Override
	public void draw(Board board, Frame top_left) {
		Frame myFrame = top_left.copy();
				
		frame = top_left.copy();
		frame.setBottom(frame.getTop()+height);
		
		// fill shape
		myFrame.setLeft(myFrame.getLeft() + 1);
		myFrame.setTop(myFrame.getTop() + 1);
		myFrame.setBottom((int) (frame.getTop() + 2 * board.getTextHeight(text.getText()) + 4 * Math.round(E_PADDING / 2)));
		board.fillRect(myFrame);
		
		myFrame = frame.copy();
		myFrame.setBottom((int) (frame.getTop() + 2 * board.getTextHeight(text.getText()) + 4 *  Math.round(E_PADDING / 2)));
		
		// draw lines
		int lineLength = 0;
		int count = hasElse ? cases.size() - 1 : cases.size();
		Frame rtt;
		
		for(int i = 0; i < count; i++) {
			rtt = qs.get(i).prepareDraw(board);
			lineLength = (int) (lineLength + Math.max(rtt.getRight(), board.getTextWidth(cases.get(i)) + Math.round(E_PADDING / 2)));
		}
		
		if(!hasElse) {
			lineLength = frame.getRight();
		}
		
		int ax = myFrame.getLeft() + 1;
		int ay = myFrame.getTop() + 1;
		int bx = myFrame.getLeft() + lineLength;
		int by = (int) (myFrame.getBottom() - 1 - board.getTextHeight(text.getText()) - Math.round(E_PADDING / 2));

		if (!hasElse) {
			bx = myFrame.getRight();
		}
		
		board.drawLine(ax, ay, bx+1, by);

		if (hasElse) {
			board.drawLine(bx+1, myFrame.getBottom() - 1, bx+1, by);
			board.drawLine(bx+1, by, myFrame.getRight()+1, myFrame.getTop() + 1);
		}
		
		int y = myFrame.getTop() + E_PADDING;
		int a = myFrame.getLeft() + Math.round((myFrame.getWidth()) / 2);
		int b = myFrame.getTop();
		int c = myFrame.getLeft() + width - 1;
		int d = myFrame.getBottom() - 1;
		int x = Math.round(((y - b) * (c - a) + a * (d - b)) / (d - b));
		
		double x_text = 0;
		double y_text = 0;
		if(hasElse) {
			
			// coordinates text line 
			double x1 = frame.getLeft();
			double x2 = frame.getLeft() + top_left.getWidth();
			double y1 = frame.getTop() + (board.getTextHeight(text.getText())) + Math.round(E_PADDING / 4);
			double y2 = y1;
//			b.getContext().setStroke(Color.BLUEVIOLET);
//			b.getContext().strokeLine(x1, y2, x2, y2);
			
			// coordinates right diagonal
			double x5 = bx+1;
			double x6 = myFrame.getRight()+1;
			double y5 = by;
			double y6 = myFrame.getTop()+1;
//			b.getContext().setStroke(Color.AQUA);
//			b.getContext().strokeLine(x5, y5, x6, y6);
			
			double m1 = (y2 - y1) / (x2 - x1);
			double n1 = y1 - m1 * x1;
			
			double m3 = (y6 - y5) / (x6 - x5);
			double n3 = y5 - m3 * x5;
			
			y1 -= text.getText().split("\n").length > 1 ? board.getTextHeight(text.getText()) / text.getText().split("\n").length : 0;
			// x-value intercept point - text line & right diagonal
			double ix2 = (n1 - n3) / (m3 - m1);
			board.fillTextRight(text.getText(), ix2-7, y1);
			
		} else {
			x_text = x - Math.round(board.getTextWidth(text.getText()) / cases.size());
			y_text = myFrame.getTop() + Math.round(E_PADDING / 3) + board.getTextHeight(text.getText());
		}		
		board.fillText(text.getText(), x_text, y_text);
		
		// draw children
		myFrame = frame.copy();
		myFrame.setTop((int) (frame.getTop() + board.getTextHeight(text.getText()) * 2 + 4	* Math.round(E_PADDING / 2) - 1));
		
		if(qs.size() != 0) {
			
			for (int i = 0; i < cases.size(); i++) {
				rtt = qs.get(i).prepareDraw(board);
				
				if (i == cases.size()-1) {
					myFrame.setRight(top_left.getRight());
				} else {
					myFrame.setRight((int) (myFrame.getLeft() + Math.max(rtt.getRight(), board.getTextWidth(cases.get(i)) + Math.round(E_PADDING / 2))));
				}
				
				// draw child
				qs.get(i).draw(board, myFrame);
				
				// draw cases text
				x_text = myFrame.getRight() + Math.round((myFrame.getLeft() - myFrame.getRight()) / 2) - board.getTextWidth(cases.get(i)) / 2;
				y_text = myFrame.getTop() - Math.round(E_PADDING / 4);
				board.fillText(cases.get(i), x_text, y_text);
				
				// draw bottom up line
				int mx = myFrame.getRight()+1;
//				int my = (int) (myrect.getTop() - b.getTextHeight(cases.get(i)));
				int sx = mx;
				int sy = Math.round((sx * (by - ay) - ax * by + ay * bx) / (bx - ax));
				
				if(i != cases.size()-1) {
					board.drawLine(mx, myFrame.getTop(), sx, sy+1);
				}

				myFrame.setLeft(myFrame.getRight());
			}
		}
		
		board.drawRect(top_left);
		
//		b.fillRect(frame, Color.AZURE);
	}
	
	@Override
	public Element selectElementByCoord(double x, double y) {
		Element selMe = super.selectElementByCoord(x, y);
		Element selCh = null;

		for (int i = 0; i < qs.size(); i++) {
			Element pre = ((Subqueue) qs.get(i)).selectElementByCoord(x, y);
			if (pre != null) {
				selCh = pre;
			}
		}

		if (selCh != null) {
			selected = false;
			selMe = selCh;
		}

		return selMe;
	}
	
}
