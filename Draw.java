import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public class Draw extends JComponent{

	private BufferedImage image;
	private BufferedImage avi;
	private BufferedImage backgroundImage;
	private URL resource = getClass().getResource("idle.png");
	private URL resource2 = getClass().getResource("avi.png");
	private Font font;

	

	public int x = 30;
	public int y = 305;
	public int health = 200;
	public int height = 0;
	public int width = 0;
	public boolean alive= true;

	public int state= 0;

	public int enemyCount;

	Monster[] monsters = new Monster[20];

	public Draw(){
		spawnEnemy();
		try {

                 AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("slime.wav").getAbsoluteFile());
                  Clip clip = AudioSystem.getClip();
                 clip.open(audioInputStream);
                clip.start();

             }catch(Exception x) { x.printStackTrace(); }
		
		try{
			image = ImageIO.read(resource);
			avi = ImageIO.read(resource2);
			backgroundImage = ImageIO.read(getClass().getResource("bg.jpg"));
			font = new Font("Comic Sans MS", Font.PLAIN, 12);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		height = image.getHeight();
		width = image.getWidth();

		startGame();
	}

		public void startGame(){
		Thread gameThread = new Thread(new Runnable(){
			public void run(){
				while(true){
					try{
						for(int c = 0; c < monsters.length; c++){
							if(monsters[c]!=null){
								monsters[c].moveTo(x,y);
								repaint();
							}
						}
						Thread.sleep(100);
					} catch (InterruptedException e) {
							e.printStackTrace();
					}
					
				}
				
			}
		});
		gameThread.start();
	}

	public void spawnEnemy(){
		if(enemyCount < 20){
			monsters[enemyCount] = new Monster(700,380, this);
			enemyCount++;
		}
	}
	public void reloadImage(){
		state++;
		if( state == 0){
			resource = getClass().getResource("takbo0.png");
		}
		else if (state == 1){
			resource = getClass().getResource("takbo0.png");
		}
		else if (state == 2){
			resource = getClass().getResource("takbo1.png");
		}
		else if (state == 3){
			resource = getClass().getResource("takbo2.png");
		}
		else if (state == 4){
			resource = getClass().getResource("takbo3.png");
		}
		else if (state == 5){
			resource = getClass().getResource("takbo4.png");
		}
		else if (state == 6){
			resource = getClass().getResource("takbo5.png");
		}
		else if (state == 7){
			resource = getClass().getResource("takbo6.png");
		}
		else if (state == 8){
			resource = getClass().getResource("takbo7.png");
			state = 0;
		}
		try{
			image = ImageIO.read(resource);
		}
		catch(IOException e){
			e.printStackTrace();
		}

		if(health<=0){
			dieAnimation();		
		}


	}

	public void attackAnimation(){
		Thread thread1 = new Thread(new Runnable(){
			public void run(){
				for(int ctr = 0; ctr < 6; ctr++){
					try {
						if(ctr==5){
							resource = getClass().getResource("idle.png");
						}
						else{
							resource = getClass().getResource("slash"+ctr+".png");
						}
						
						try{
							image = ImageIO.read(resource);
						}
						catch(IOException e){
							e.printStackTrace();
						}
				        repaint();
				        Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				for(int x=0; x<monsters.length; x++){
					if(monsters[x]!=null){
						if(monsters[x].contact){
							monsters[x].life = monsters[x].life - 5;
						}
					}
				}
			}
		});
		thread1.start();
	}

	public void dieAnimation(){
		if(alive){
			Thread thread2 = new Thread(new Runnable(){
				public void run(){
					for(int ctr = 0; ctr < 8; ctr++){
						try {					
							resource = getClass().getResource("hide"+ctr+".png");
							
							try{
								image = ImageIO.read(resource);
							}
							catch(IOException e){
								e.printStackTrace();
							}
					       	repaint();
					        Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			thread2.start();
		}
		alive = false;
	}

	public void jumpAnimation(){
		Thread thread3 = new Thread(new Runnable(){
			public void run(){
				for(int ctr = 0; ctr < 7; ctr++){
					try {
						if(ctr==6){
							resource = getClass().getResource("idle.png");
						}
						else{
							resource = getClass().getResource("j"+ctr+".png");
						}
						
						try{
							image = ImageIO.read(resource);
						}
						catch(IOException e){
							e.printStackTrace();
						}
				        repaint();
				        Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread3.start();
	}

	

public void checkCollision(){
		int xChecker = x + width;
		int yChecker = y;

		for(int x=0; x<monsters.length; x++){
			boolean collideX = false;
			boolean collideY = false;

			if(monsters[x]!=null){
				monsters[x].contact = false;

				if(yChecker > monsters[x].yPos){
					if(yChecker-monsters[x].yPos < monsters[x].height){
						collideY = true;
					}
				}
				else{
					if(monsters[x].yPos - (yChecker+height) < monsters[x].height){
						collideY = true;
					}
				}

				if(xChecker > monsters[x].xPos){
					if((xChecker-width)-monsters[x].xPos < monsters[x].width){
						collideX = true;
					}
				}
				else{
					if(monsters[x].xPos-xChecker < monsters[x].width){
						collideX = true;
					}
				}
			}

			if(collideX && collideY){
				monsters[x].contact = true;
				health = health - 2;
						
			}

		}
	}
	



	public void paintComponent(Graphics g){
	super.paintComponent(g);

	g.setColor(Color.BLACK);
	g.drawImage(backgroundImage, -x + getWidth() - getWidth(), 0, this);
	g.drawImage(image, x,y, this);

	g.setFont(font);
	g.drawImage(avi, 10,20, this);
	g.drawString("Status",80,30);
	g.setColor(Color.RED);
	g.fillRect(80,35, health, 20);

	for(int c = 0; c < monsters.length; c++){
			if(monsters[c]!=null){
				g.drawImage(monsters[c].image, monsters[c].xPos, monsters[c].yPos, this);
				g.setColor(Color.GREEN);
				g.fillRect(monsters[c].xPos+7, monsters[c].yPos, monsters[c].life, 2);
			}	
	}
}

	public void moveLeft(){
			x = x - 5;
			reloadImage();
			repaint();
			checkCollision();

		}

	public void moveRight(){
			x = x + 5;
			reloadImage();
			repaint();
			checkCollision();
		}

	public void moveUp(){
			y = y - 5;
			reloadImage();
			repaint();
			checkCollision();
		}

	public void die(){
		dieAnimation();
		}

	public void attack(){
		attackAnimation();
	}

	public void jump(){
		jumpAnimation();
	}


}

	