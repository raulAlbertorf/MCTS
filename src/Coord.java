
@SuppressWarnings("EqualsAndHashcode")
public class Coord {
	public int x;
	public int y;
	

	Coord(int x, int y) {
		this.x=x;
		this.y=y;
	}

	Coord(char x, int y) {
		//this.x=x;
		this.y=y;
	}
	

	Coord(String s) {
		switch (s.charAt(0)) {
			case 'A':
			case 'a':
				y=0;
				break;
			case 'B':
			case 'b':
				y=1;
				break;
			case 'C':
			case 'c':
				y=2;
				break;
			case 'D':
			case 'd':
				y=3;
				break;
			case 'E':
			case 'e':
				y=4;
				break;
			case 'F':
			case 'f':
				y=5;
				break;
			case 'G':
			case 'g':
				y=6;
				break;
			case 'H':
			case 'h':
				y=7;
				break;
		}
		switch (s.charAt(1)) {
			case '1':
				x=7;
				break;
			case '2':
				x=6;
				break;
			case '3':
				x=5;
				break;
			case '4':
				x=4;
				break;
			case '5':
				x=3;
				break;
			case '6':
				x=2;
				break;
			case '7':
				x=1;
				break;
			case '8':
				x=0;
				break;
		}
	}

	public static char toChar (int incoord) {
		char c=' ';
		switch (incoord) {
			case 0: 
				c='A';
				break;
			case 1: 
				c='B';
				break;
			case 2: 
				c='C';
				break;
			case 3: 
				c='D';
				break;
			case 4: 
				c='E';
				break;
			case 5: 
				c='F';
				break;
			case 6: 
				c='G';
				break;
			case 7: 
				c='H';
				break;
			
		}
		return c;
	}

        @Override
	public String toString() {
		return ""+toChar(y)+(Board.BOARDSIZE-x);
	}

        @Override
	public boolean equals(Object o) {
		Coord c;
		if (o instanceof Coord) {
			c=(Coord)o;
			if (c.x == this.x && c.y == this.y)
				return true;
			else return false;
		} else return false;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
        @Override
        @SuppressWarnings("CloneDeclaresCloneNotSupported")
	public Coord clone() {
		return new Coord(x, y);
	}
}