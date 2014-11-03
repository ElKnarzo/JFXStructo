package de.jfxstructo.graphics;


public class Frame {

	private int right, left, top, bottom;

	public Frame() {
		right = 0;
		left = 0;
		top = 0;
		bottom = 0;
	}

	public Frame(int right, int left, int top, int bottom) {
		this.right = right;
		this.left = left;
		this.top = top;
		this.bottom = bottom;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	public int getWidth() {
		return right - left;
	}
	
	public int getHeight() {
		return bottom - top; 
	}
	
	public Frame copy() {
		Frame rect = new Frame();

		rect.left = this.left;
		rect.top = this.top;
		rect.bottom = this.bottom;
		rect.right = this.right;

		return rect;
	}
	
    public String toString() {
        return "Frame = [left:"+left+", top:"+top+", right:"+right+", bottom:"+bottom+"]";
    }

}
