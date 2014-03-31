package logic;

/**
 * Used to represent tuples of size 3
 * 
 * @author Shanon Mathai
 *
 * @param <W> Type 1
 * @param <E> Type 2
 * @param <R> Type 3
 */
public class Triple<W, E, R>{
	public final W from;
	public final E dir;
	public final R spaces;

	/**
	 * Create a Triple object
	 * 
	 * @param f Stored into from (must be Type 1)
	 * @param d Stored into dir (must be Type 2)
	 * @param s Stored into spaces (must be Type 3)
	 */
	public Triple(W f, E d, R s){
		from = f;
		dir = d;
		spaces = s;
	}
}