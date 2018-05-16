package goalp.exputil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.panayotis.gnuplot.dataset.Point;
import com.panayotis.gnuplot.dataset.PointDataSet;

public class GnuPlotDataBuilder {
 			
	private List<PointDataSet<Number>> dataSet;
	
	private String fileDest;
	
	private GnuPlotDataBuilder(){
		this.dataSet = new ArrayList<>();
	}
	
	public static GnuPlotDataBuilder create(){
		return new GnuPlotDataBuilder();
	}
	
	public GnuPlotDataBuilder toFile(String fileDest){
		this.fileDest = fileDest;
		return this;
	}
	
	public GnuPlotDataBuilder addDataSet(PointDataSet<Number> dataSet) {
		this.dataSet.add(dataSet);
		return this;
	}
	
	public void done(){
		Path path = Paths.get(fileDest);
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			write(dataSet, writer);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private void write(List<PointDataSet<Number>> dataSets, BufferedWriter writer) throws IOException {
		for(PointDataSet<Number> dataSet: dataSets){
			for(Point<Number> point: dataSet){
				for(int i = 0; i<point.getDimensions(); i++){
					writer.write(point.get(i)+"\t");
				}
				writer.write("\n");
			}
			writer.write("\n");
		}
	}
}
