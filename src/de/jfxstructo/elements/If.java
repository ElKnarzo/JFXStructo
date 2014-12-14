package de.jfxstructo.elements;

import de.jfxstructo.Board;
import de.jfxstructo.graphics.Frame;

public class If extends AElement {

	private Subqueue qFalse = new Subqueue();
	private Subqueue qTrue = new Subqueue();
	
	private Frame trueFrame = new Frame();
	private Frame falseFrame = new Frame();

	public If() {
		super();
		qFalse.setParent(this);
		qTrue.setParent(this);
	}

	public AElement getFalse() {
		return qFalse;
	}
	
	public If(String text) {
		super(text);
		qFalse.setParent(this);
		qTrue.setParent(this);
	}
	
	public void addElementToTrue(AElement ele) {
		qTrue.addElement(ele);
	}
	
	public void addElementToFalse(AElement ele) {
		qFalse.addElement(ele);
	}

	@Override
	public AElement clone() {
		AElement ele = new If(text.getText());
		((If) ele).qTrue = (Subqueue) this.qTrue.clone();
		((If) ele).qFalse = (Subqueue) this.qFalse.clone();
		((If) ele).qTrue.parent = ele;
		((If) ele).qFalse.parent = ele;
		return ele;
	}
	
	@Override
	public Frame prepareDraw(Board board) {
		frame.setTop(0);
		frame.setLeft(0);
				
		frame.setRight((int) Math.round((AElement.E_PADDING) + board.getTextWidth(text.getText()) * 2));
		
		// prepare the sub-queues
		falseFrame = qFalse.prepareDraw(board);
		trueFrame = qTrue.prepareDraw(board);
				
		frame.setBottom((int) (3 * Math.round(E_PADDING / 2) + board.getTextHeight(text.getText())));
		frame.setRight(Math.max(frame.getRight(), trueFrame.getRight() + falseFrame.getRight()));
		frame.setBottom(frame.getBottom() + Math.max(trueFrame.getBottom(), falseFrame.getBottom()));
		
		setDimension();
		
		return frame;
	}

	@Override
	public void draw(Board board, Frame top_left) {
		Frame myFrame = top_left.copy();
		myFrame.setBottom(myFrame.getBottom() + 1);
		board.fillRect(myFrame);
		
		frame = top_left.copy();
		frame.setBottom(frame.getTop()+height);
		myFrame.setBottom((int) (frame.getTop() + board.getTextHeight(text.getText()) + 3 * Math.round(E_PADDING / 2)));
		
		// coordinates text line 
		double x1 = frame.getLeft();
		double x2 = frame.getLeft() + frame.getWidth();
		double y1 = frame.getTop() + (board.getTextHeight(text.getText())) + Math.round(E_PADDING / 4);
		double y2 = y1;
//		board.getContext().setStroke(Color.BLUEVIOLET);
//		board.getContext().strokeLine(x1, y2, x2, y2);
		
		// coordinates left diagonal
		double x3 = myFrame.getLeft()+1;
		double x4 = myFrame.getLeft()+trueFrame.getRight();
		double y3 = myFrame.getTop()+1;
		double y4 = myFrame.getBottom();
//		board.getContext().setStroke(Color.AQUA);
//		board.getContext().strokeLine(x4, 0, x4, 2000);
		
		// coordinates right diagonal
		double x5 = x4;
		double x6 = myFrame.getRight()-1;
		double y5 = myFrame.getBottom();
		double y6 = myFrame.getTop()+1;
//		board.getContext().setStroke(Color.AQUA);
//		board.getContext().strokeLine(x3, y3, x4, y4);

		double m1 = (y2 - y1) / (x2 - x1);
		double n1 = y1 - m1 * x1;
		
		double m2 = (y4 - y3) / (x4 - x3);
		double n2 = y3 - m2 * x3;
		
		double m3 = (y6 - y5) / (x6 - x5);
		double n3 = y5 - m3 * x5;
		
		// x-value intercept point - text line & left diagonal
		double ix1 = (n1 - n2) / (m2 - m1);
		// x-value intercept point - text line & right diagonal
		double ix2 = (n1 - n3) / (m3 - m1);
		
//		System.out.println("Schnittpunkt rechts: " + ix2);
//		System.out.println("Schnittpunkt rechts - Abstand: " + Math.ceil(ix2 - x5));
//		System.out.println("Schnittpunkt links: " + ix1);
//		System.out.println("Schnittpunkt links - Abstand: " + Math.ceil(x5 - ix1));
		
		y1 -= text.getText().split("\n").length > 1 ? board.getTextHeight(text.getText()) / text.getText().split("\n").length : 0;
		// draw text
		if(Math.ceil((ix2 - x5)) < Math.ceil((x5 - ix1))) {
			board.fillTextRight(text.getText(), ix2-7, y1);
			
		} else if(Math.floor((ix2 - x5)) == Math.floor((x5 - ix1))) {
			board.fillTextCenter(text.getText(), x5, y1);
		} else {
			board.fillText(text.getText(), ix1+7, y1);
		}
		
//		board.getContext().setStroke(Color.RED);
//		board.getContext().strokeLine(x, y1, x2, y1);
		
		// draw symbols
		board.fillText("T", myFrame.getLeft() + Math.round(E_PADDING / 2), myFrame.getBottom() - Math.round(E_PADDING / 2));
		board.fillText("F", myFrame.getRight() - Math.round(E_PADDING / 2) - board.getTextWidth("F"), myFrame.getBottom() - Math.round(E_PADDING / 2));
		
		// draw triangle
		board.drawLine(myFrame.getLeft()+1, myFrame.getTop()+1, myFrame.getLeft()+trueFrame.getRight(), myFrame.getBottom());
		board.drawLine(myFrame.getLeft()+trueFrame.getRight(), myFrame.getBottom(), myFrame.getRight()-1, myFrame.getTop()+1);
		
		// draw children
		myFrame = top_left.copy();
		myFrame.setTop((int) (frame.getTop() + board.getTextHeight(text.getText()) + 3 * Math.round(E_PADDING / 2) - 1));
		myFrame.setRight(myFrame.getLeft() + trueFrame.getRight() - 1);
		
		qTrue.draw(board, myFrame);
		
		myFrame.setLeft(myFrame.getRight());
		myFrame.setRight(frame.getRight());
		
		qFalse.draw(board, myFrame);
				
		board.drawRect(top_left);

//		board.fillRect(frame, Color.AZURE);
		
//		frame.setTop(frame.getTop()-2);
//		frame.setLeft(frame.getLeft()-2);
//		frame.setBottom(frame.getBottom()+2);
//		frame.setRight(frame.getRight()+2);
//		board.drawRect(frame, Color.AQUA);
	}

	@Override
	public AElement selectElementByCoord(double x, double y) {
		AElement selMe = super.selectElementByCoord(x, y);
		AElement selT = qTrue.selectElementByCoord(x, y);
		AElement selF = qFalse.selectElementByCoord(x, y);
		if (selT != null) {
			selMe = selT;
		} else if (selF != null) {
			selF.selected = true;
			selMe = selF;
		}
	
		return selMe;
	}

}
