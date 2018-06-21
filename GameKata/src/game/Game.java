package game;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import display.Display;
import element.Block;
import element.Player;

public class Game implements Runnable{
	
	private Display display;
	
	private Thread thread;
	private boolean running = false;
	
	private BufferStrategy bufferStrategy;
	private Graphics graphics;
	
	private BufferedImage background;
	private static final String PATH_BACKGROUND = "/textures/background.jpg";
	private static final String PATH_BACKGROUND_GAME_OVER = "/textures/backgroundGameOver.jpg";
	
	public String title;
	public int width;
	public int height;
	
	public Player player;
	public Block floor;
	
	public List<Block> blocks;
	private int lastBlockX = 0;
	
	public int gravity;
	public int movementSpeed;

	public Game(String title, int width, int height){
		this.title = title;
		this.width = width;
		this.height = height;
		
		this.gravity = 4;
		this.movementSpeed = 3;
		
		this.floor = new Block(0, this.height - 150, this.width, 150);
		this.blocks = new ArrayList<>();
		this.player = new Player(250, this.floor.getY() - 70, 70, 70, this);
		
		
		this.display = new Display(title, width, height);
	}
	
	private void init() {	
		int randomRange;
		int randomHeight;
		
		for(int i = 0; i < 10; i++) {
			
			randomRange = (int)(Math.random() * 250) + 250;
			randomHeight = (int)(Math.random() * 50);
			
			if( i != 0 ){
				this.blocks.add(new Block(this.blocks.get(i - 1).getX() + 500 + randomRange, this.floor.getY() - (50 + randomHeight), 
						50, 50 + randomHeight));
			} else{
				this.blocks.add(new Block(this.width + 100, this.floor.getY() - (50 + randomHeight), 
						50, 50 + randomHeight));
			}	
		}
		
		this.display.getFrame().addKeyListener(player);
		this.background = ImageLoader.loadImage(PATH_BACKGROUND);
	}
	
	private void tick() {
		int randomRange;
		
		this.player.collision();
		
		for (int i = 0; i < this.blocks.size(); i++) {
			
			this.blocks.get(i).move(movementSpeed);
			randomRange = (int)(Math.random() * 750) + 250;
			
			if(this.blocks.get(i).getX() + this.blocks.get(i).getWidth() < 0){
				if( i != 0){
					this.blocks.get(i).setX(this.blocks.get(i - 1).getX() + 250 + randomRange);
				} else{
					this.blocks.get(i).setX(this.width + this.lastBlockX + randomRange);
				}
			}
			
			if ( i == this.blocks.size() -1 ) {
				this.lastBlockX = this.blocks.get(i).getX();
			}
		}
		
		if(!this.player.isCollisionFloor() && !this.player.isJump()){
			this.player.setY(this.player.getY() + this.gravity);
		} else if (this.player.isJump()){
			this.player.setY(this.player.getY() - this.gravity);
			if (this.player.getY() < this.player.getJumpMax()) {
				this.player.setJump(false);
			}
		}
		
		if(this.player.isCollisionBlock()){
			this.background = ImageLoader.loadImage(PATH_BACKGROUND_GAME_OVER);
		}
	}
	
	private void render() {		
 		this.bufferStrategy = display.getCanvas().getBufferStrategy();
		if(this.bufferStrategy == null){
			this.display.getCanvas().createBufferStrategy(3);
			return;
		}
		this.graphics = bufferStrategy.getDrawGraphics();
		this.graphics.clearRect(0, 0, width, height);

		this.graphics.drawImage(this.background, 0, 0, this.width, this.height, null);

		//
		this.graphics.fillRect(this.floor.getX(), this.floor.getY(), 
				this.floor.getWidth(), this.floor.getHeight());
		for (Block block : this.blocks) {
			this.graphics.fillRect(block.getX(), block.getY(), 
					block.getWidth(), block.getHeight());
		}
		this.graphics.fillRect(this.player.getX(), this.player.getY(), 
				this.player.getWidth(), this.player.getHeight());
		//
		
		this.bufferStrategy.show();
		this.graphics.dispose();
	}

	@Override
	public void run() {
		this.init();

		double delta = 0;
		long lastTime = System.nanoTime();
		long now;
		
		while(this.running){
			now = System.nanoTime();
			delta += now - lastTime;
			lastTime = now;
			
			if ( delta % 10000 == 0 && !this.player.isCollisionBlock()) {
				this.movementSpeed++;
				this.gravity++;
			}
			
			this.tick();
			this.render();
			
			if(this.player.isCollisionBlock() && delta % 20 == 0){
				this.stop();
			}
		}
	}

	public synchronized void start(){
		if(this.running){
			return;
		}
		this.running = true;
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	public synchronized void stop(){
		if(!this.running){
			return;
		}
		this.running = false;
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
