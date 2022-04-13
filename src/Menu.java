import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Menu implements MouseListener, MouseMotionListener {

	private boolean open;

	Font f = new Font("Arial", 0, 50);
	Font f2 = new Font("Arial", 0, 30);
	String[] text = { "Classic", "Special", "Crazy", "Insane", "Terror", "Exit" };
	int mx, my;
	boolean klick = false;

	public Menu() {
		open = true;
	}

	public boolean isOpen() {
		return open;
	}

	int p;
	int h = 0;
	int scw = -1;

	public void paint(Graphics g, double xf, double yf) {
		g.drawImage(Main.title, 0, 0, (int) (1000 * xf), (int) (600 * yf), null);

		if (scw > -1) {
			g.setFont(f2);
			g.drawImage(Main.button, (int) (700 * xf), (int) (200 * yf), (int) (250 * xf), (int) (300 * yf), null);
			g.setColor(new Color(0, 0, 100));
			g.drawString(text[scw] + " Highscore", (int) (710 * xf), (int) (240 * yf));

			for (int i = 0; i < 10; i++) {
				g.setColor(new Color(255, 255, 255));
				g.drawString((i + 1) + ":", (int) (710 * xf), (int) ((270 + i * 20) * yf));
				g.setColor(new Color(0, 0, 0));
				g.drawString("" + Main.highscore.score[scw][i], (int) (750 * xf), (int) ((270 + i * 20) * yf));
			}
			scw = -1;
		}
		g.setFont(f);
		p++;
		if (p > 2) {
			h++;
			if (h > 12) {
				h = 0;
			}
			p = 0;
		}

		int x = 150;
		int xp = 20;
		for (int i = 0; i < text.length; i++) {
			g.drawImage(Main.button, (int) ((x + i * xp) * xf), (int) ((200 + i * 55) * yf), (int) (400 * xf),
					(int) (50 * yf), null);

			if (mx > (int) ((x + i * xp) * xf) && mx < (int) ((x + i * xp) * xf) + (int) (400 * xf)) {
				if (my > (int) ((200 + i * 55) * yf) && my < (int) ((200 + i * 55) * yf) + (int) (50 * yf)) {
					if (i < 5) {
						scw = i;
					}
					g.setColor(new Color(0, 0, 0, 200));
					g.fillRect((int) ((x + 3 + i * xp) * xf), (int) ((203 + i * 55) * yf), (int) (394 * xf),
							(int) (44 * yf));
					if (klick) {
						Main.sound.playSound(4, true);
						if (i < 5) {

							Main.neuesSpiel(i);

						}
						if (i == 5) {
							try {
								// Pausenzeit
								Thread.sleep(500);
							} catch (InterruptedException ex) {
							}
							System.exit(0);
						}
						open = false;
					}
				}
			}

			g.drawImage(Main.huhn[h], (int) ((x + 60 + i * xp) * xf), (int) ((205 + i * 55) * yf), (int) (-50 * xf),
					(int) (50 * yf), null);
			g.setColor(new Color(255, 255, 255));

			g.drawString(text[i], (int) ((x + 80 + i * xp) * xf), (int) ((235 + i * 55) * yf));

		}

		klick = false;
	}

	public void ende(int spiel, int punkte) {
		Main.sound.stopSound(6);
		open = true;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mx = e.getX();
		my = e.getY();
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
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (open) {
			// Main.sound.playSound(4, true);
			klick = true;
		}
	}

}
