package goalp.exputil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.panayotis.gnuplot.dataset.Point;
import com.panayotis.gnuplot.dataset.PointDataSet;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.FillStyle;
import com.panayotis.gnuplot.style.FillStyle.Fill;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;

public class DataSetBuilder<N extends Number> {
	
	private PointDataSet<N> dataset;
	private PlotStyle ps;
	
	private String yUnit;
	
	private DataSetBuilder(){
		this.dataset = new PointDataSet<N>();
		this.ps = new PlotStyle();
	}
	
	public static DataSetBuilder<Number> create(){
		return new DataSetBuilder<Number>();
	}
	
	public DataSetBuilder<N> setStyle(Style style, Fill fill){
		ps.setStyle(style);
		ps.setFill(new FillStyle(fill));
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public DataSetBuilder<N> addPoint(N... coords){
		return addPoint(new Point<>(coords));
	}
	
	public DataSetBuilder<N> addPoint(Point<N> point){
		dataset.add(point);
		return this;
	}
	
	/**
	 * 
	 * Limitation: after conversion, the dataset will composed of longs with possible lost of precision
	 * 
	 * @param dimension
	 * @param origin
	 * @return
	 */
	public DataSetBuilder<N> convertTimeDimensionToHumanReadable(int dimension, TimeUnit origin){
		long min = Long.MAX_VALUE;
		long max = Long.MIN_VALUE;
		
		TimeUnit actualUnit = origin;
		
		for(Point<N> point:this.dataset){
			if(point.get(dimension).longValue() > max){
				max = point.get(1).longValue();
			}
			
			if(point.get(dimension).longValue() < min){
				min = point.get(1).longValue();
			}
		}
		
		TimeUnit choosenNewUnit = actualUnit;
		
		for(TimeUnit unit:TimeUnit.values()){
			long newMin = unit.convert(min, actualUnit);
			if(newMin>=1 && newMin < 1000){
				choosenNewUnit = unit;
			}
		}
		convertTimeDimension(dimension, actualUnit, choosenNewUnit);
		
		return this;
	}
	
	@SuppressWarnings("unchecked")
	private void convertTimeDimension(int dimension, TimeUnit from, TimeUnit to){
		PointDataSet<Number> newDataSet = new PointDataSet<Number>();
		
		for(Point<N> point:this.dataset){
			//always long
			Long newValue = to.convert(point.get(dimension).longValue(), from);
			ArrayList<Long> values = new ArrayList<Long>();
			for(int i = 0; i < point.getDimensions(); i++){
				if(i == dimension){
					values.add(newValue);
				}else{
					values.add(point.get(i).longValue());					
				}
			}
			Long[] arr = new Long[point.getDimensions()];
			values.toArray(arr);
			Point<Number> newPoint = new Point<Number>(arr);
			newDataSet.add(newPoint);
		}
		this.dataset = (PointDataSet<N>) newDataSet;
	}

	/**
	 * Build dataset plot with style
	 * @return
	 */
	public DataSetPlot  buildDataSetPlot(){
		PointDataSet<N> built = this.dataset;
		DataSetPlot dsplot = new DataSetPlot(built);
		
		dsplot.setPlotStyle(ps);
		
		this.dataset = null;
		return dsplot;
	}
	
	/**
	 *  Build dataset of point (no style)
	 * @return
	 */
	public PointDataSet<N>  buildPointDataSet(){
		PointDataSet<N> built = this.dataset;
		this.dataset = null;
		return built;
	}
}
