package de.jfxstructo.elements;


import de.jfxstructo.Board;
import de.jfxstructo.graphics.Frame;


public class Root extends Element {

	private Subqueue queue = new Subqueue();
		
	public Root(String text) {
		this.text.setText(text);
		queue.setParent(this);
	}
	
	public Root() {
		text.setText("???");
		queue.setParent(this);
		
		queue.addElement(new If(""));
		((If) queue.getElement(0)).addElementToTrue(new For());
		queue.addElement(new Instruction("Ins1"));
		queue.addElement(new Instruction("Ins2"));
		queue.addElement(new For("For"));
		queue.addElement(new Case("B", new String[]{"1","2","3","4"}, true));
		queue.addElement(new Instruction("Ins3"));
		queue.addElement(new While("While"));
		queue.addElement(new Repeat("a < 10"));
		queue.addElement(new Repeat("b < 24"));
		queue.addElement(new Forever("Forever"));
	}

	private void setQueue(Subqueue queue) {
		this.queue = queue;
		queue.setParent(this);
	}
	
	public void removeElement(Element ele) {
		if (ele != null) {
			ele.selected = false;
			if (!ele.getClass().getSimpleName().equals("Subqueue")
					&& !ele.getClass().getSimpleName().equals("Root")) {
				((Subqueue) ele.parent).removeElement(ele);
			}
		}
	}

	@Override
	public Frame prepareDraw(Board board) {
		Frame subFrame = new Frame();
		
		frame.setTop(0);
		frame.setLeft(0);
		
		frame.setRight(2 * E_PADDING);
		if (frame.getRight() < board.getTextWidth(text.getText()) + 2 * E_PADDING) {
			frame.setRight((int) (board.getTextWidth(text.getText()) + 2 * E_PADDING));
		}
		frame.setBottom((int) (3 * E_PADDING + board.getTextHeight(text.getText())));
		
		subFrame = queue.prepareDraw(board);
		
//		if (isNice == true) {
			frame.setRight(Math.max(frame.getRight(), subFrame.getRight() + 2 * Element.E_PADDING));
//		} else {
//			rect.setRight(Math.max(rect.getRight(), subrect.getRight()));
//		}
		
		frame.setBottom(frame.getBottom() + subFrame.getBottom());
		
		setDimension();
		
		return frame;
	}

	@Override
	public void draw(Board board, Frame top_left) {
		Frame subFrame = top_left.copy();
						
//		if(isNice) {
			subFrame.setTop((int) (subFrame.getTop() + board.getTextHeight(text.getText()) + 2 * E_PADDING));
			subFrame.setBottom(subFrame.getBottom() - E_PADDING);
			subFrame.setLeft(subFrame.getLeft() + E_PADDING);
			subFrame.setRight(subFrame.getRight() - E_PADDING);
//		} else {	
//			rect.setTop((int) (top_left.getTop() + text.getLayoutBounds().getHeight() * text.getText().toCharArray().length + 2
//					* Math.round(E_PADDING / 2)));
//			rect.setLeft(top_left.getLeft());
//		}
		
		board.fillRect(frame);
		queue.draw(board, subFrame);
		board.drawRect(frame);
		
		double x_text = getFrame().getLeft() + E_PADDING;
		double y_text = getFrame().getTop() + 2 * E_PADDING;
		board.fillText(text.getText(), x_text, y_text);
	}

	@Override
	public Element clone() {
		Root ele = new Root(text.getText());
		ele.setQueue((Subqueue) this.queue.clone());
		return ele;
	}
	
	@Override
	public Element selectElementByCoord(double x, double y) {
		Element selMe = super.selectElementByCoord(x, y);
		Element selCh = queue.selectElementByCoord(x, y);
		
		if (selCh != null) {
			return selCh;
		} else {
			return selMe;
		}
	}
}

