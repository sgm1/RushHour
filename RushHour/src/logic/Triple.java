package logic;


public class Triple<W, E, R>{
	public final W from;
	public final E dir;
	public final R spaces;

	public Triple(W f, E d, R s){
		from = f;
		dir = d;
		spaces = s;
	}
}