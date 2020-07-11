package module6;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import processing.core.PConstants;
import processing.core.PGraphics;

public class VolcanoeMarker extends CommonMarker {
	
	public static int TRI_SIZE = 5;  
	
	public VolcanoeMarker(Location location) {
		super(location);
	}
	
	
	public VolcanoeMarker(Feature volcanoe) {
		super(((PointFeature)volcanoe).getLocation(), volcanoe.getProperties());
	}

	public void drawMarker(PGraphics pg, float x, float y) {
		pg.pushStyle();
		
		pg.fill(255,0,0);
		pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		
		pg.popStyle();
	}
	
	public double threatCircle() {	
		double miles = 20.0f * Math.pow(1.8, 2*getPei()-5);
		double km = (miles * 1.6f);
		return km;
	}
	
	public void showTitle(PGraphics pg, float x, float y)
	{
		String name = getVolcanoe() + " " + getCountry() + " ";
		String region = "Region: " + getRegion();
		
		pg.pushStyle();
		
		pg.fill(255, 255, 255);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x, y-TRI_SIZE-39, Math.max(pg.textWidth(name), pg.textWidth(region)) + 6, 39);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(name, x+3, y-TRI_SIZE-33);
		pg.text(region, x+3, y - TRI_SIZE -18);
		
		pg.popStyle();
	}
	
	private float getPei() {
		try {
			return Float.parseFloat((getStringProperty("PEI")));
		} catch(NullPointerException e) {
			return 5;
		}
	}
	
	
	private String getVolcanoe()
	{
		return getStringProperty("V_Name");
	}
	
	private String getCountry()
	{
		return getStringProperty("Country");
	}
	
	private String getRegion()
	{
		return getStringProperty("Region");
	}
}
