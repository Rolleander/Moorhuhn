
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageObserver;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

public class Main extends Frame implements MouseListener, MouseMotionListener, KeyListener {

	// ----------------------------Variables--------------------------------------------------------
	public static double xf, yf;
	public static int resolution[] = new int[2];

	public static Image huhn[] = new Image[21];
	public static Image fathuhn[] = new Image[25];
	private Image score[] = new Image[4];
	private Image digits[] = new Image[10];
	private Image killlabels[] = new Image[12];
	private Image veg[] = new Image[3];
	private static int vegeta, vegetaload, lang, langp, langz;
	private Image back, cloud, ammo, cursor, spitz, langbild, platte;
	private static int x = 1000;
	private int mx;
	private int my;
	private static int muni = 10;
	private boolean klick = false, start = true;
	private static Huhn[] huhner = new Huhn[1000];
	private static int punkte = 0;
	public static Sound sound = new Sound();
	public static Image title, button;
	private static int kills = 0, killst = 0, spielart;
	public static int shotradius = 100;
	public static boolean special, crazy;
	private static int rand, zeit;
	private Menu menu = new Menu();
	private static int plattehit;
	public static String ordnerpfad = System.getProperty("user.home") + "/BRollGames/Moorhuhn";

	public static Score highscore;

	public static void main(String[] args) {
		Main main = new Main();

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		main.setSize(dimension);

		main.setUndecorated(true);
		main.setVisible(true);
		main.setEnabled(true);
		main.setResizable(false);
		main.setTitle("Java Moorhuhn");
		main.setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/icon.png")));
		main.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		resolution[0] = 1000;
		resolution[1] = 600;
		xf = (double) dimension.getWidth() / (double) resolution[0];
		yf = (double) dimension.getHeight() / (double) resolution[1];
		main.init();
	}

	Robot robby;

	public void init() {
		File file = new File(ordnerpfad);
		if (!file.exists()) {
			file.mkdirs();
		}

		readHighscores();
		try {
			robby = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setFocusable(true);
		requestFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseListener(menu);
		addKeyListener(this);
		addMouseMotionListener(menu);
		initImages();

		Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(16, 16), "Custom Cursor");
		setCursor(c);
		for (int i = 0; i < huhner.length; i++) {
			huhner[i] = new Huhn();

		}

		start();
		run();
	}

	private void readHighscores() {
		InputStream file;
		try {
			file = new FileInputStream(ordnerpfad + "/Highscores.dat");
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			highscore = (Score) input.readObject();
			input.close();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			highscore = new Score();
			saveHighscores();
		}

	}

	private void saveHighscores() {
		ObjectOutputStream o = null;
		try {
			FileOutputStream fos = new FileOutputStream(ordnerpfad + "/Highscores.dat");
			o = new ObjectOutputStream(fos);
			o.writeObject(highscore);
		} catch (IOException e) {
			System.err.println("Error while Saving: " + e);
		} finally {
			try {
				o.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setHighscore(int spielart, int punkte) {
		boolean save = false;
		for (int i = 0; i < 10; i++) {
			if (punkte > highscore.score[spielart][i]) {
				for (int h = 9; h > i; h--) {
					highscore.score[spielart][h] = highscore.score[spielart][h - 1];
				}
				save = true;
				highscore.score[spielart][i] = punkte;
				break;
			}
		}
		if (save) {
			saveHighscores();
		}
	}

	private static int munimax;

	public static void neuesSpiel(int nr) {
		vegeta = 0;
		vegetaload = 0;
		plattehit = 0;
		shotradius = 0;
		huhner = new Huhn[10000];
		for (int i = 0; i < 10000; i++) {
			huhner[i] = new Huhn();

		}

		punkte = 0;
		rand = 80;
		special = false;
		crazy = false;

		muni = 10;
		zeit = 101;

		if (nr == 1)// Special
		{
			kills = 0;
			killst = 0;
			rand = 70;
			special = true;
		}
		if (nr == 2)// Crazy
		{
			kills = 0;
			killst = 0;
			rand = 30;
			crazy = true;
			muni = 33;
			special = true;
		}
		if (nr == 3) {// 1000
			kills = 0;
			killst = 0;
			rand = 2;
			crazy = true;
			muni = 99;
			special = true;
		}

		if (nr == 4) {
			kills = 0;
			killst = 0;
			huhner = new Huhn[10000];
			for (int i = 0; i < huhner.length; i++) {
				huhner[i] = new Huhn();
				huhner[i].init(x, resolution[0], 1);
			}
			rand = 1;
			muni = 3;
			special = true;
			shotradius = 100;

		}

		spielart = nr;
		munimax = muni;
		sound.loopSound(6);
		x = 1000;
	}

	private void newHuhn() {
		if ((int) (Math.random() * rand + 1) == 1) {
			int fat = (int) (Math.random() * 25 + 1);
			if (spielart == 3) {
				fat = 0;
			}
			if (fat == 1) {
				for (int i = 0; i < huhner.length; i++) {

					if (huhner[i].isDead()) {
						huhner[i] = new Huhn();

						huhner[i].init(x, resolution[0], 5);
						break;
					}
				}
			} else {
				for (int i = 0; i < huhner.length; i++) {

					if (huhner[i].isDead()) {
						huhner[i] = new Huhn();
						if (spielart == 3) {
							huhner[i].init(x, resolution[0], 1);
						} else {
							huhner[i].init(x, resolution[0], (int) (Math.random() * 4 + 1));
						}

						break;
					}
				}
			}
		}
	}

	private static int[] killp = { 0, 3, 5, 7, 10, 15, 30, 50, 80, 125, 175, 300 };
	private static int[] killb = { 0, 2, 3, 5, 7, 10, 12, 15, 17, 20, 25, 30 };

	public static int killer() {
		int art = -1;

		if (special) {
			kills++;
			killst += 35;
			for (int i = 1; i < killp.length; i++) {

				if (kills == killb[i]) {
					sound.playKillSound(i);
					punkte += killp[i];
					art = i;
					if (i == 11) {
						killst = 0;
						kills = 0;
					}
					break;
				}
			}
		} else {
			art = -1;
		}

		return art;
	}

	int hu = 0;
	private int vegp = 0;

	public void paint(Graphics g) {

		if (start) {

			start = false;
			for (int i = 0; i < huhn.length; i++) {
				g.drawImage(huhn[i], 0, 0, null);
			}
			for (int i = 0; i < fathuhn.length; i++) {
				g.drawImage(fathuhn[i], 0, 0, null);
			}

		}

		if (menu.isOpen()) {
			menu.paint(g, xf, yf);

		} else {

			killst--;
			if (killst < 1) {
				killst = 0;
				kills = 0;
			}

			newHuhn();
			if (mx < (50 * xf)) {
				if (x > 0) {
					x -= 10;
				}
			}
			if (mx > (950 * xf)) {
				if (x < 2000) {
					x += 10;
				}
			}
			g.drawImage(cloud, 0, 0, (int) (1000 * xf), (int) (600 * yf), null);

			for (int r = 1; r < 6; r++) {
				if (r == 3) {
					int scx = (int) ((mx / xf));
					int scy = (int) (my / yf);
					if (special) {
						int vx = (int) (2680 - x), vy = 280, vb = 100, vh = 100;
						g.drawImage(veg[vegeta / 10], (int) (vx * xf), (int) (vy * yf), (int) (vb * xf),
								(int) (vh * yf), null);

						if (vegeta > 0) {
							vegeta--;
							if (vegetaload > 9001) {
								if (vegeta < 10) {
									vegeta = 20;
								}
							}
						}
						if (klick && vegetaload < 9001) {
							if (scx > vx && scx < vx + vb && scy > vy && scy < vy + vh) {
								vegetaload += (int) (Math.random() * 50 + 1) + 9;
								vegeta = 20;
								sound.playSound(10, false);
								if (vegetaload > 9000) {
									sound.playSound(9, true);
									g.setColor(Color.WHITE);
									g.fillRect(0, 0, (int) (1000 * xf), (int) (600 * yf));
									try {
										// Pausenzeit
										Thread.sleep(500);
									} catch (InterruptedException ex) {
									}
									for (int i = 0; i < huhner.length; i++) {
										if (huhner[i] != null) {
											if (!huhner[i].isDead()) {
												huhner[i].killHuhn();
											}
										}
									}

									// punkte+=9000;

								}
								vegp = 20;
							}
						}
					}
					g.drawImage(back, (int) (-x * xf), 0, (int) (3000 * xf), (int) (600 * yf), null);
					// spitzkopf
					if (special) {
						int sx = (2670 - x), sy = 455, sb = 50, sh = 60;
						g.drawImage(spitz, (int) (sx * xf), (int) (sy * yf), (int) (sb * xf), (int) (sh * yf), null);

						if (klick) {

							if (scx > sx && scx < sx + sb && scy > sy && scy < sy + sh) {
								sound.playSound(8, true);

							}
						}

					}
					// Lang
					if (plattehit > 4) {
						if ((int) (Math.random() * 300 + 1) == 1) {
							lang = 70;
						}

						if (special && lang > 0) {
							lang--;
							int sx = (2638 - x), sy = 210, sb = 25, sh = 36;
							g.drawImage(langbild, (int) ((sx) * xf), (int) ((sy - lang / 10) * yf), (int) (sb * xf),
									(int) (sh * yf), null);

							if (klick) {

								if (scx > sx && scx < sx + sb && scy > sy && scy < sy + sh) {
									if ((int) (Math.random() * 3 + 1) == 1) {

										int p = (int) (Math.random() * 1000 + 1);
										punkte += p;
										langp = p;
									} else {
										punkte = 0;
										langp = 0;
									}

									langz = 20;

								}
							}
						}
					} else {
						// platte
						int sx = (2643 - x), sy = 214, sb = 26, sh = 32;
						g.drawImage(platte, (int) ((sx) * xf), (int) ((sy - lang / 10) * yf), (int) (sb * xf),
								(int) (sh * yf), null);
						if (klick) {
							if (scx > sx && scx < sx + sb && scy > sy && scy < sy + sh) {
								plattehit++;
							}
						}
					}

				}
				for (int i = 0; i < huhner.length; i++) {
					if (huhner[i] != null) {

						if (!huhner[i].isDead()) {

							if (huhner[i].getStufe() == r) {
								int hx = huhner[i].getPos()[0];
								int hy = huhner[i].getPos()[1];
								// System.out.println("lebt: "+i+" xpos"+hx+" yps"+hy);
								int bild = huhner[i].getBild();
								Dimension dim = huhner[i].getDimension();
								if (huhner[i].isRight()) {

									g.drawImage(huhn[bild], (int) ((hx - x + dim.width) * xf), (int) (hy * yf),
											(int) (-dim.width * xf), (int) (dim.width * yf), null);

								} else {
									if (r == 5) {
										g.drawImage(fathuhn[bild], (int) ((hx - x) * xf), (int) (hy * yf),
												(int) (dim.width * xf), (int) (dim.width * yf), null);
									} else {
										g.drawImage(huhn[bild], (int) ((hx - x) * xf), (int) (hy * yf),
												(int) (dim.width * xf), (int) (dim.width * yf), null);
									}
								}

								int py = huhner[i].getPunktY();
								if (py > -20) {
									int bonus = huhner[i].getBonus();
									if (bonus > 0) {
										paintDigits(g, bonus, (int) ((hx + dim.width / 2 - 20 - x)),
												(int) ((py + dim.height / 3)));

									} else {
										if (r < 5) {
											g.drawImage(score[r - 1], (int) ((hx + dim.width / 2 - 20 - x) * xf),
													(int) ((py + dim.height / 3) * yf), (int) (40 * xf),
													(int) (20 * yf), null);

										} else {
											g.drawImage(score[0], (int) ((hx + dim.width / 2 - 20 - x) * xf),
													(int) ((py + dim.height / 3) * yf), (int) (40 * xf),
													(int) (20 * yf), null);

										}

									}
								}

								int k = huhner[i].getKill();
								if (k > -1) {
									g.drawImage(killlabels[k], (int) ((hx + dim.width / 2 - 60 - x) * xf),
											(int) ((py + dim.height / 3 - 35) * yf), (int) (150 * xf), (int) (32 * yf),
											null);

									paintDigits(g, killp[k], hx + dim.width / 2 - 60 - x + 160,
											py + dim.height / 3 - 30);

								}

								if (klick) {
									int treffer = huhner[i].treffen((int) (x + (mx / xf)), (int) (my / yf));
									if (treffer == 1) {
										sound.playSound((int) (Math.random() * 3 + 1), true);
										switch (r) {
										case 1:
											punkte += 25;
											break;
										case 2:
											punkte += 15;
											break;
										case 3:
											punkte += 10;
											break;
										case 4:
											punkte += 5;
											break;
										case 5:
											punkte += 25;
											break;
										}
										if (special == false) {
											klick = false;
										}
									}
									if (treffer == 2) {
										sound.playSound((int) (Math.random() * 3 + 1), true);
										punkte += huhner[i].getBonus();
									}

								}
								huhner[i].move();
							}

						}

					}
				}
			}
			klick = false;
			/*
			 * if(shotradius>0){ int kx=(int)((mx/xf)-shotradius); int
			 * ky=(int)((my/yf)-shotradius); g.setColor(new Color(0,0,0));
			 * g.drawOval((int)(kx*xf),(int)(ky*yf),(int)(shotradius*2*xf),(int)(shotradius*
			 * 2*yf)); }
			 */

			for (int i = 0; i < muni; i++) {
				if (spielart == 3) {
					g.drawImage(ammo, (int) ((985 - 10 * i) * xf), (int) ((570) * yf), (int) (14 * xf), (int) (26 * yf),
							null);
				} else {
					g.drawImage(ammo, (int) ((955 - 30 * i) * xf), (int) ((510) * yf), (int) (44 * xf), (int) (79 * yf),
							null);
				}

			}

			if (vegp > 0) {
				paintDigits(g, vegetaload, 2730 - x - (String.valueOf(vegetaload).length() * 14) / 2, 280);
				vegp--;
			}
			if (langz > 0) {
				paintDigits(g, langp, 2650 - x - (String.valueOf(langp).length() * 14) / 2, 210);
				langz--;
			}
			paintDigits(g, punkte, 990 - (String.valueOf(punkte).length() * 14), 10);
			// Zeit
			if (System.currentTimeMillis() / 1000 != tz0) {
				zeit--;
				if (zeit < 1) {// ENde
					for (int i = 0; i < huhner.length; i++) {
						huhner[i] = new Huhn();

					}
					setHighscore(spielart, punkte);
					menu.ende(spielart, punkte);

				}
			}
			paintDigits(g, zeit, 10, 10);
			tz0 = (int) (System.currentTimeMillis() / 1000);
		}

	}

	int tz0 = 0;

	public void paintDigits(Graphics g, int zahl, int x, int y) {
		String s = String.valueOf(zahl);
		for (int i = 0; i < s.length(); i++) {
			int z = Integer.parseInt(s.substring(i, i + 1));
			g.drawImage(digits[z], (int) ((x + i * 14) * xf), (int) (y * yf), (int) (16 * xf), (int) (20 * yf), null);
		}

	}

	public void initImages() {

		URL filename = getClass().getResource("moorhuhn.gif");
		Image image = Toolkit.getDefaultToolkit().getImage(filename);
		filename = getClass().getResource("parallaxbackground.GIF");
		back = Toolkit.getDefaultToolkit().getImage(filename);
		filename = getClass().getResource("cloud.gif");
		cloud = Toolkit.getDefaultToolkit().getImage(filename);
		filename = getClass().getResource("cursor.gif");
		cursor = Toolkit.getDefaultToolkit().getImage(filename);
		filename = getClass().getResource("ammo.gif");
		ammo = Toolkit.getDefaultToolkit().getImage(filename);
		filename = getClass().getResource("title.gif");
		title = Toolkit.getDefaultToolkit().getImage(filename);
		filename = getClass().getResource("button.gif");
		button = Toolkit.getDefaultToolkit().getImage(filename);
		filename = getClass().getResource("spitzkopf.gif");
		spitz = Toolkit.getDefaultToolkit().getImage(filename);
		filename = getClass().getResource("lang.gif");
		langbild = Toolkit.getDefaultToolkit().getImage(filename);
		filename = getClass().getResource("platte.gif");
		platte = Toolkit.getDefaultToolkit().getImage(filename);

		for (int i = 0; i < 2; i++) {
			for (int h = 0; h < 13; h++) {
				if (i == 1 && h == 8) {
					break;
				}
				huhn[i * 13 + h] = createImage(
						new FilteredImageSource(image.getSource(), new CropImageFilter(i * 142, h * 152, 142, 152)));
			}
		}
		filename = getClass().getResource("scores.gif");
		image = Toolkit.getDefaultToolkit().getImage(filename);
		for (int i = 0; i < 4; i++) {
			score[i] = createImage(new FilteredImageSource(image.getSource(), new CropImageFilter(0, i * 20, 40, 20)));
		}
		filename = getClass().getResource("veg.gif");
		image = Toolkit.getDefaultToolkit().getImage(filename);
		for (int i = 0; i < 3; i++) {
			veg[i] = createImage(new FilteredImageSource(image.getSource(), new CropImageFilter(i * 100, 0, 100, 100)));
		}
		filename = getClass().getResource("scroe.gif");
		image = Toolkit.getDefaultToolkit().getImage(filename);
		for (int i = 0; i < 10; i++) {
			digits[i] = createImage(new FilteredImageSource(image.getSource(), new CropImageFilter(i * 16, 0, 16, 20)));
		}
		filename = getClass().getResource("foregroundhuhn.gif");
		image = Toolkit.getDefaultToolkit().getImage(filename);
		for (int i = 0; i < 25; i++) {
			fathuhn[i] = createImage(
					new FilteredImageSource(image.getSource(), new CropImageFilter(0, i * 380, 380, 380)));
		}
		filename = getClass().getResource("kills.gif");
		image = Toolkit.getDefaultToolkit().getImage(filename);
		for (int i = 0; i < 12; i++) {
			killlabels[i] = createImage(
					new FilteredImageSource(image.getSource(), new CropImageFilter(0, i * 32, 150, 32)));
		}

	}

	// Bilder für DoubleBuffer
	private Image dbImage;
	private Graphics dbg;

	public void update(Graphics g) {
		// DOUBLE BUFFER gegen Bildschirmflackern
		if (dbImage == null) {
			dbImage = createImage(this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics();
		}
		dbg.setColor(getBackground());
		dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);
		dbg.setColor(getForeground());
		paint(dbg);
		g.drawImage(dbImage, 0, 0, this);
	}

	public void start() {
		// Thread starten
		Thread th = new Thread();
		th.start();
	}

	public void stop() {
		// Wird ausgeführt wenn Thread gestoppt wird
	}

	public void destroy() {
		// Wird ausgeführt wenn Thread geschlossen wird (Programmende)
		// Hier müsst ihr alle Lieder stoppen, falls ihr welche verwendet
		// damit sie im Browser nicht weiterlaufen
	}

	public int GAME_TICK = 1000 / 75; // 25FPS

	public void run() {
		// Run-Methode
		while (true) {
			long startTime = System.nanoTime();
			long diff = System.nanoTime() - startTime;
			if (diff < GAME_TICK) {
				try {
					Thread.sleep(GAME_TICK - diff);
				} catch (InterruptedException e) {
					break;
				}
			} else {
				repaint();
			}

		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	public void shot(int e) {
		if (menu.isOpen() == false) {
			if (e == 1) {
				if (muni > 0) {
					klick = true;
					muni--;
					sound.playSound(0, true);
				} else {
					sound.playSound(4, true);
				}

			}
			if (e == 3) {
				if (muni == 0) {
					sound.playSound(5, true);
					muni = munimax;
				}

			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		shot(e.getButton());

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			for (int i = 0; i < huhner.length; i++) {
				huhner[i] = new Huhn();

			}

			menu.ende(0, 0);

		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
