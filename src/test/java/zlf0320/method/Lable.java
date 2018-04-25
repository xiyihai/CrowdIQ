package zlf0320.method;

//import java.util.ArrayList;
public class Lable {
	int i;
	int j;
	String s;

	public int setI(int a) {
		i = a;
		return a;
	}

	public int setJ(int b) {
		j = b;
		return b;
	}

	public String setS(String sl) {
		s = sl;
		return sl;
	}

	public int getJ() {
		int b = j;
		return b;
	}

	public int getI() {
		int a = i;
		return a;
	}

	public String getS() {
		String sl = s;
		return sl;
	}

	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Lable> lablelist = new ArrayList<Lable>();
		Lable l = new Lable();
		for (int i = 0; i < 2; i++) {

			l.setI(5);
			l.setJ(9);
			l.setS("wewrewr");
			lablelist.add(l);
			System.out.println(lablelist.get(i).getI());

		}
	}*/

}
