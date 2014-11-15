package org.masters.breakout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import org.masters.breakout.shapes.Block;

public class BreakOutPanel extends JComponent implements Animator {

	private boolean isResizing;
	private BufferedImage buf = null;
	
	private final int ncols = 8;
	private final int nrows = 6;
	
	private int[][] blocks;
	private Dimension blockDimension;
	
	private Ellipse2D.Float ball;
	private Dimension ballDimension;

	private final int block_padding = 1;
	
	private float base_velocity_x = 0.1333f;
	private float base_velocity_y = 0.1333f;
	private float velocity_x;
	private float velocity_y;

	private float x_direction = 1;
	private float y_direction = 1;
	private boolean _intersecting;
	
	public BreakOutPanel() {
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				isResizing = true;
			}
		});
		
		initBlocks();
		// triggers calculation of dimension and velocity on first paintComponents()
		isResizing = true;
	}
	
	@Override
	public boolean isOpaque() {
		return true;
	}
	
	private void initBlocks(){
		blocks = new int[nrows][ncols];
		for (int i=0; i<nrows; i++){
			for (int j=0; j<ncols; j++){
				blocks[i][j] = 1;
			}
		}
	}

	
	@Override
	protected void paintComponent(Graphics g) {
		
		if (isResizing) {
			buf = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
			calculateDimensions();
			calculateVelocity();
			isResizing = false;
		}

		Graphics2D g2 = (Graphics2D)buf.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);

		// clear screen
		if (isOpaque()){
			g2.setColor(getBackground());
			g2.fillRect(0, 0, getWidth(), getHeight());
		}
		
		// paint blocks to screen buffer
		for (int i=0; i<nrows; i++){
			for (int j=0; j<ncols; j++){
				if (blocks[i][j]==1){
					Block b = new Block(
							(j * (blockDimension.width+block_padding) + block_padding),
							(i * (blockDimension.height+block_padding) + block_padding),
							blockDimension.width,
							blockDimension.height);
					b.setColor(Color.blue);
					b.update(g2);
				}
			}
		}
		
		g2.setColor(getForeground());
		g2.fill(ball);
		
		// paint screen buffer to screen
		g.drawImage(buf, 0, 0, null);
	}

	
	private void calculateDimensions()
	{
		blockDimension = calculateBlockSize();
		ballDimension = calculateBallSize();
		ball = createBall(ballDimension);
	}

	private void calculateVelocity() {
		// distance ball will travel per update elapsedTime
		velocity_x = ((float)getWidth() / 300.0f) * base_velocity_x;
		velocity_y = ((float)getHeight() / 300.0f) * base_velocity_y;
	}
	
	private Dimension calculateBlockSize() {
		// Blocks stretch horizontal 100%, vertical 50% 
		// screen width divided by columns, less the padding minus one column on far right
		// half screen height divided by rows, less the passing minus one row on bottom	
		return new Dimension(getWidth() / ncols, (getHeight() / 2) / nrows);
	}
	
	private Dimension calculateBallSize() {
		int tmpWidth = getWidth() / (ncols *2); // ball is half width of a block
		return new Dimension(tmpWidth, tmpWidth);
	}
	
	private Ellipse2D.Float createBall(Dimension ballSize) {
		return new Ellipse2D.Float(getWidth()/2 - ballSize.width, (getHeight()/4) * 3, ballSize.width, ballSize.height);
	}

	@Override
	public void update(long elapsedTime) {
		// buf only exists after our first paintComponent has occurred
		if (buf == null) return;
		// we are being actively resized, don't update until finished 
		if (isResizing) return;
		
		ball.x += x_direction * velocity_x * elapsedTime;
		ball.y += y_direction * velocity_y * elapsedTime; 
		
		if (ball.x >= getWidth()
					- ball.getBounds().width){
			x_direction = -1;
			ball.x = getWidth()
					- ball.getBounds().width;
		} else if (ball.x < 0){
			x_direction = 1;
			ball.x = 0;
		}
	
		if (ball.y >= getHeight()
					- ball.getBounds().height){
			y_direction = -1;
			ball.y = getHeight() - ball.getBounds().height;
		} else if (ball.y < 0){
			y_direction = 1;
			ball.y = 0;
		}
		
		// check interception with blocks

		int row = (int) Math.floor(ball.y/blockDimension.height);
		int col = (int) Math.floor(ball.x/blockDimension.width);
		System.err.println("row: " + row + ", col: " + col);
		
		if (!_intersecting && row < nrows && col < ncols && 
				ball.y < nrows * blockDimension.width && 
				row >= 0 && col >= 0 && 
				blocks[row][col]==1){
			y_direction = -y_direction;
			blocks[row][col] = 0;
			_intersecting = true;
		} else {
			_intersecting = false;
		}

		System.err.println("ball:" + ball.x + "," + ball.y);

		repaint();
	}
}
