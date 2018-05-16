package goalp.exputil;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.Point;
import com.panayotis.gnuplot.dataset.PointDataSet;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.terminal.PostscriptTerminal;

public class PlotBuilder {
 		
	private JavaPlot p;
	
	private PointDataSet<Number> dataSet;
	private String labelFont = "Arial";
	private int fontSize = 20;
	 
	private PlotBuilder(){
		
		this.p = new JavaPlot(true);
	}
	
	public static PlotBuilder create(){
		return new PlotBuilder();
	}
	
	public PlotBuilder asEps(String fileDest){
        PostscriptTerminal epsf = new PostscriptTerminal(fileDest);
        epsf.setColor(true);
        p.setTerminal(epsf);
        return this;
	}
	
	public PlotBuilder xLabel(String label){
        p.getAxis("x").setLabel(label, labelFont, fontSize);
        p.getAxis("x").set("xrange [] reverse");
        return this;
	}
	
	public PlotBuilder xRange(Double begin, Double end, boolean reverse){
		p.getAxis("x").set( p.getAxis("x").getName() + rangePattern(begin, end, reverse));
		return this;
	}
	
	public PlotBuilder yRange(Double begin, Double end, boolean reverse){
		p.getAxis("y").set( p.getAxis("y").getName() + rangePattern(begin, end, reverse));
		return this;
	}
	
	public PlotBuilder zRange(Double begin, Double end, boolean reverse){
		p.getAxis("z").set( p.getAxis("z").getName() + rangePattern(begin, end, reverse));
		return this;
	}
	
	public String rangePattern(Double begin, Double end, boolean reverse){
		String beginPattern, endPattern, reversePattern;
		if(begin.isNaN()){
			beginPattern = "";
		}else{
			beginPattern = begin.toString();
		}
		
		if(end.isNaN()){
			endPattern = "";
		}else{
			endPattern = end.toString();
		}

		if(reverse){
			reversePattern = "reverse";
		}else{
			reversePattern = "";
		}
		
        return "range ["+
        beginPattern + ":" +
        endPattern+ "] "+ reversePattern;
	}
	
	public PlotBuilder yLabel(String label){
        p.getAxis("y").setLabel(label, labelFont, fontSize);
        return this;
	}
	
	public PlotBuilder yReverse(){
        p.getAxis("y").set( p.getAxis("y").getName() + "range [] reverse");
        return this;
	}
	
	public PlotBuilder zLabel(String label){
        p.getAxis("z").setLabel(label, labelFont, fontSize);
        return this;
	}
	
	public PlotBuilder zReverse(){
        p.getAxis("z").set( p.getAxis("z").getName() + "range [] reverse");        
        return this;
	}
	
	public PlotBuilder zTicslevel(int level){
        p.getAxis("z").set( "ticslevel " + level);       
        return this;
	}
	
	public PlotBuilder xyplane(int level){
        p.set("grid ytics lc rgb \"#bbbbbb\" lw 1 lt 0","" );
        p.set("grid xtics lc rgb \"#bbbbbb\" lw 1 lt 0","" );
        p.set("grid ztics lc rgb \"#bbbbbb\" lw 1 lt 0","" );
       // p.set("yzplane", level+ "" );
        p.set("grid", "");
        //p.set("tics axis", "");

        return this;
	}
	
	public PlotBuilder addDataSet(PointDataSet<Number> dataSet) {
		p.addPlot(dataSet);
		return this;
	}
	
	public PlotBuilder addDataSet(DataSetPlot dataSet) {
		p.addPlot(dataSet);
		return this;
	}
	
	public PlotBuilder addDataSet(double[][] plot) {
		p.addPlot(new DataSetPlot(plot));
		return this;
	}

	public PlotBuilder addIntegerDataSet(PointDataSet<Integer> dataSet) {
		p.addPlot(dataSet);
		return this;		
	}
	
	public PlotBuilder addDoubleDataSet(PointDataSet<Double> dataSet) {
		p.addPlot(dataSet);
		return this;
	}
	
	public PlotBuilder addPoint(Number x, Number y){
		if(dataSet == null){
			dataSet = new PointDataSet<Number>();
		}
		dataSet.add(new Point<Number>(x,y));
		return this;
	}

	public void plot(){
		System.out.println(p.getCommands());
		if(dataSet != null){
			p.addPlot(dataSet);
		}
		p.plot();
	}

}
