package org.masters.breakout.shapes;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;

public class Block {

	private Rectangle2D rect;
	private Color color;

	public Block(float x, float y, float width, float height) {
		rect = new Rectangle2D.Float(x, y, width, height);
		this.color = Color.white;
	}



	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return this.color;
	}

	public void update(Graphics2D g) {
		g.setColor(getColor());
		g.fill(rect);
	}

	public boolean intersects(Shape shape) {
		return shape.intersects(rect);
	}

	public Rectangle2D getBounds() {
		return rect.getBounds();
	}
}
