package goalp.exputil;

public class TimeUtil {

	public enum Unit {
	    NANOSECONDS {
	        public Double toNanos(Double d)   { return d; }
	        public Double toMicros(Double d)  { return d/(C1/C0); }
	        public Double toMillis(Double d)  { return d/(C2/C0); }
	        public Double toSeconds(Double d) { return d/(C3/C0); }
	    };
	    

	    // Handy constants for conversion methods
	    static final double C0 = 1.0d;
	    static final double C1 = C0 * 1000.0d;
	    static final double C2 = C1 * 1000.0d;
	    static final double C3 = C2 * 1000.0d;
	    
	    public Double toNanos(Double duration) {
	        throw new AbstractMethodError();
	    }
	    public Double toMicros(Double duration) {
	        throw new AbstractMethodError();
	    }
	    public Double toMillis(Double duration) {
	        throw new AbstractMethodError();
	    }
	    public Double toSeconds(Double duration) {
	        throw new AbstractMethodError();
	    }
	}
}
