import java.awt.Dimension;
import java.awt.Point;

public class Huhn {

	private double[] pos = new double[2];
	private double[] speed = new double[2];
	Dimension dim = new Dimension();
	private boolean dead = false;
	private int bild, zaehler, freq;
	private int row;
	private boolean die = false;
	private int bonus, timer = 0, kill = -1, punktet = 0;
	private int punkty = -100;
	float winkel = 0;

	public Huhn() {
		bild = 0;
		zaehler = 0;
		dead = true;
		bonus = 0;
		die = false;
	}

	public void init(int x, int breite, int stufe) {
		double sped = 1;
		bonus = 0;
		timer = 0;
		kill = -1;
		punktet = 0;

		switch (stufe) {
		case 4:
			dim.setSize(142, 152);
			sped = 2;
			break;
		case 3:
			dim.setSize(100, 112);
			sped = 1.5;
			break;
		case 2:
			dim.setSize(62, 74);
			sped = 1;
			break;
		case 1:
			dim.setSize(32, 44);
			sped = 0.5;
			break;
		case 5:
			dim.setSize(380, 380);
			sped = 0;
			break;
		}
		if (Main.crazy) {
			sped *= 2;
		}
		die = false;
		punkty = -100;
		boolean create = false;

		do {
			pos[0] = (int) (Math.random() * 3000) + 1;
			// pos[0]=2000;

			if (pos[0] + dim.width >= x && pos[0] <= x + breite) {

			} else {
				create = true;
			}
			if (stufe == 5) {
				create = true;
			}

		} while (create == false);

		row = stufe;
		if (row < 5) {
			pos[1] = (int) (Math.random() * (100 + stufe * 100) + 1);
			// pos[1]=100;
		} else {
			pos[1] = 220;
			Main.sound.playSound(7, true);
			speed[0] = 0;
		}
		// pos[1]=100;
		// pos[0]=2000;

		bild = (int) (Math.random() * 13 + 1) - 1;
		// bild=0;
		if (pos[0] < x + breite / 2)
			if ((int) (Math.random() * 2 + 1) == 1) {
				speed[0] = sped;

			} else {
				speed[0] = -sped;

			}
		speed[0] = sped;
		// speed[0]=0;
		freq = 5;
		dead = false;

	}

	public int getPunktY() {
		return punkty;
	}

	public Dimension getDimension() {
		return dim;
	}

	public boolean isDead() {
		return dead;
	}

	public int getStufe() {
		return row;
	}

	public boolean isRight() {
		boolean r = false;
		if (speed[0] > 0) {
			r = true;
		}
		return r;
	}

	public void killHuhn() {

		treffen((int) (pos[0] + dim.width / 2), (int) (pos[1] + dim.height / 2));
	}

	public int treffen(int mx, int my) {
		int treffer = 0;
		// int xp= (int) (pos[0]+Math.cos(Math.toRadians(winkel))*radius);
		// int yp= (int) (pos[1]+Math.sin(Math.toRadians(winkel))*radius);

		if (inRadius(mx, (int) (pos[0] + dim.width / 2), my, (int) (pos[1] + dim.height / 2),
				(int) (dim.width / 1.9) + Main.shotradius))
		// if(inRadius(mx,(int)(pos[0]+dim.width/2),my,(int)(pos[1]+dim.height/2),2000))
		{
			if (die == false) {
				die = true;
				speed[1] = speed[0] * 3;
				if (speed[1] < 0) {
					speed[1] *= -1;
				}
				speed[0] = 0;

				bild = 13;
				punkty = (int) pos[1];
				treffer = 1;
				if (row == 5) {
					bild = 18;
				}

				kill = Main.killer();

			} else {
				if (Main.special) {
					if (row < 5) {
						punkty = (int) pos[1] - dim.height / 2;

						speed[1] = -5;

						bild = 13;
						speed[0] = ((Math.random() * 6 + 1) - 4) / 3;
						bonus++;
						treffer = 2;
					}
				}
			}
			punktet = 50;
		}

		return treffer;
	}

	public int getKill() {
		return kill;
	}

	public int getBonus() {
		return bonus;
	}

	private boolean inRadius(int x1, int x2, int y1, int y2, int r)// Punkt im Radius des anderen Punkts?
	{
		boolean b = false;
		int abstand = (int) Point.distance(x1, y1, x2, y2);
		if (abstand <= r)
		// if(abstand<=50)
		{
			b = true;
		}
		return b;
	}

	float radius = 100;

	public void move() {

		punktet--;
		if (punktet < 1) {
			punkty = -100;
			kill = -1;

		}

		// radius+=0.1;
		/*
		 * winkel+=30; pos[0]+=Math.cos(Math.toRadians(winkel))*radius;
		 * pos[1]+=Math.sin(Math.toRadians(winkel))*radius;
		 */
		pos[0] += speed[0];
		pos[1] += speed[1];
		if (row < 5) {
			if (die) {

				if (Main.crazy) {
					if (speed[1] < 9) {
						speed[1] += 0.2;
					}
				} else {
					if (speed[1] < 5) {
						speed[1] += 0.13;
					}
				}

			}
			zaehler++;
			if (zaehler > freq) {
				zaehler = 0;
				bild++;
				if (die == false) {

					if (bild > 12) {
						bild = 0;
					}
				} else {

					if (bild > 20) {
						bild = 20;

					}
				}
			}
			if (pos[0] < -dim.width || pos[0] > 3000 || pos[1] > 600) {
				dead = true;
			}
		} else {// FATHUHN
			zaehler++;
			if (die == true) {
				if (zaehler > 10) {
					zaehler = 0;
					bild++;

					if (bild > 24) {
						bild = 24;

						dead = true;
					}
				}
			} else {

				if (zaehler > 10) {
					zaehler = 0;
					bild++;
					if (bild > 18) {
						bild = 6;
					}
				}
				timer++;
				if (timer > 500) {
					bild = 18;
					die = true;
				}
			}

		}

	}

	public int[] getPos() {
		int[] p = new int[2];
		//

		p[0] = (int) pos[0];
		p[1] = (int) pos[1];
		// p[0]+=Math.cos(Math.toRadians(winkel))*radius;
		// p[1]+=Math.sin(Math.toRadians(winkel))*radius;
		return p;
	}

	public int getBild() {
		return bild;
	}

}
