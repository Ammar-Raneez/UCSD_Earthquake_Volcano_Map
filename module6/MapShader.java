package module6;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.providers.Google.*;

import java.util.List;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;

import java.util.ArrayList;
import java.util.HashMap;


import de.fhpotsdam.unfolding.marker.Marker;

public class MapShader extends PApplet{

	UnfoldingMap map;
	HashMap<String, Float> disasterMap;
	List<Feature> countries;
	List<Marker> countryMarkers;
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	private String volcanoFile = "volcano.json";

	public void setup() {
		size(800, 600, OPENGL);
		
		List<Feature> volcanoes = GeoJSONReader.loadData(this, volcanoFile);
		List<Marker> volcanoMarkers = new ArrayList<Marker>();
		for(Feature volcanoe: volcanoes) {
			volcanoMarkers.add(new VolcanoeMarker(volcanoe));
		}
		
	    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
	    //earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
	    MapUtils.createDefaultEventDispatcher(this, map);

		// Load lifeExpectancy data
		disasterMap = ParseFeed.LoadDisastersFromCSV(this,"death-rates-natural-disasters.csv");

		// Load country polygons and adds them as markers
		countries = GeoJSONReader.loadData(this, "countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
		map.addMarkers(volcanoMarkers);
		
		// Country markers are shaded according to life expectancy (only once)
		shadeCountries();
	}

	public void draw() {
		map.draw();
	}

	private void shadeCountries() {
		for (Marker marker : countryMarkers) {
			String countryId = (String) marker.getProperty("name");
			if (disasterMap.containsKey(countryId)) {
				float deathsPerMillion = disasterMap.get(countryId);
				System.out.println(deathsPerMillion);

				int colorLevel = (int) map(deathsPerMillion, 0, 300000, 10, 255);
				marker.setColor(color(255-colorLevel, 100, colorLevel));
			}
			else {
				marker.setColor(color(150,150,150));
			}
		}
	}
}
