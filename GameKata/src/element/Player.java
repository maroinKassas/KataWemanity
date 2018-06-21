package element;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import game.Game;

public class Player implements KeyListener {
	
	private int x;
	private int y;
	private int width;
	private int height;
	private int jumpMax;
	
	private Game game;
	
	private boolean jump;
	private boolean collisionFloor;
	private boolean collisionBlock;
	
	public Player(int x, int y, int width, int height, Game game) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.jumpMax = game.height - game.floor.getHeight() - 300;
		
		this.game = game;

		this.jump = false;
		this.collisionFloor = false;
		this.collisionBlock = false;
	}
	
	public void collision(){
		if (this.y + this.height >= this.game.floor.getY()){
			this.collisionFloor = true;
		} else {
			this.collisionFloor = false;
		}
		
		for(Block block : this.game.blocks){
			if (this.x + this.width - 10 >= block.getX() && this.x <= block.getX() + block.getWidth() - 10
					&& this.y + this.height >= block.getY() + 10){
				this.collisionBlock = true;
				break;
			}
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getJumpMax() {
		return jumpMax;
	}

	public void setJumpMax(int jumpMax) {
		this.jumpMax = jumpMax;
	}

	public boolean isJump() {
		return jump;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}
	
	public boolean isCollisionFloor() {
		return collisionFloor;
	}

	public void setCollisionFloor(boolean collisionFloor) {
		this.collisionFloor = collisionFloor;
	}

	public boolean isCollisionBlock() {
		return collisionBlock;
	}

	public void setCollisionBlock(boolean collisionBlock) {
		this.collisionBlock = collisionBlock;
	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		if(keyEvent.getKeyCode() == KeyEvent.VK_SPACE){
			if(this.collisionFloor){
				this.jump = true;
			}
			this.collisionFloor = false;
	    }
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}
}
