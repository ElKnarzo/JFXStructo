package de.jfxstructo.elements;

import java.util.Vector;

import de.jfxstructo.Board;
import de.jfxstructo.graphics.Frame;

public class Subqueue extends AElement {

	private static String placeholder = "\u2205";
	private Vector<AElement> queue = new Vector<>();

	public Subqueue() {
		super(placeholder);
	}

	public Vector<AElement> getQueue() {
		return queue;
	}

	public int getIndexOf(AElement ele) {
		return queue.indexOf(ele);
	}

	public AElement getElement(int index) {
		return queue.get(index);
	}

	public void addElement(AElement ele) {
		ele.setParent(this);
		queue.add(ele);
	}

	public void removeElement(AElement ele) {
		queue.remove(ele);
	}

	public void removeElement(int index) {
		queue.removeElement(queue.get(index));
	}

	@Override
	public AElement clone() {
		AElement ele = new Subqueue();
		for (int i = 0; i < queue.size(); i++) {
			((Subqueue) ele).addElement(queue.get(i).clone());
		}
		return ele;
	}

	@Override
	public Frame prepareDraw(Board board) {
		Frame subrect = new Frame();

		frame.setTop(0);
		frame.setLeft(0);
		frame.setRight(0);
		frame.setBottom(0);

		if (queue.size() > 0) {
			for(int i = 0; i < queue.size(); i++) {
				subrect = queue.get(i).prepareDraw(board);
				frame.setRight(Math.max(frame.getRight(), subrect.getRight()));
				frame.setBottom(frame.getBottom() + subrect.getBottom());
			}
		} else {
			frame.setRight(2 * E_PADDING);
			frame.setBottom((int) (2 * Math.round(E_PADDING/2) + Math.round(text.getLayoutBounds().getHeight())));
		}

		setDimension();

		return frame;
	}

	@Override
	public void draw(Board board, Frame top_left) {
		frame = top_left.copy();

		Frame myframe;
		Frame subframe;

		myframe = top_left.copy();
		myframe.setBottom(myframe.getTop());

		if(queue.size() > 0) {

			for (int i = 0; i < queue.size(); i++) {
				subframe = queue.get(i).prepareDraw(board);
				myframe.setBottom(top_left.getBottom());

				if(i == queue.size()-1) {
					myframe.setBottom(top_left.getBottom());
				}

				queue.get(i).draw(board, myframe);
				myframe.setTop(myframe.getTop()+subframe.getBottom());
			}

		} else {
			// draw nothing
			frame = top_left.copy();

			board.fillRect(frame);
			board.drawRect(frame);

			double x_text = top_left.getLeft() + (top_left.getWidth() / 2) - Math.round(text.getLayoutBounds().getWidth()) / 2;
			double y_text = top_left.getTop() + Math.round(E_PADDING / 2) + Math.round(text.getLayoutBounds().getHeight());
			board.fillText(text.getText(), x_text, y_text);
		}
	}

	@Override
	public AElement selectElementByCoord(double x, double y) {
		AElement res = super.selectElementByCoord(x, y);
		AElement sel = null;
		for (int i = 0; i < queue.size(); i++) {
			sel = queue.get(i).selectElementByCoord(x, y);
			if (sel != null) {
				res = sel;
			}
		}
		return res;
	}


}
