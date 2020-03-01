package Draw;

import javafx.scene.paint.Color;

import java.util.HashMap;

/**
 * The {@code LoadColorMap} code maps between waytypes and colors.
 */
public class LoadColorMap
{

    private HashMap<WayType, Color> fillMap;
    private HashMap<WayType, Color> strokeMap;
    private boolean normalStroke = false;

    private MapCanvas mapCanvas;

    private Color watercolor = Color.rgb(171, 211, 221);
    private Color forest = Color.rgb(200,223,190);
    private Color grass = Color.rgb(233,246,231);
    private Color ground = Color.rgb(242,238, 233);
    private Color building = Color.rgb(217,208, 201);
    private Color cycleway = Color.rgb(138,173, 242);
    private Color primaryroad = Color.rgb(247,178, 158);
    private Color secondaryroad = Color.rgb(251,213, 167);
    private Color buildingarea = Color.rgb(224,223,223);
    private Color industrialarea = Color.rgb(235,219,232);
    private Color park = Color.rgb(154,200,200);
    private Color farmyard = Color.rgb(226,222, 217);

    /**
     * Initialises fillmap and strokemap and sets current MapCanvas
     * @param mapCanvas current mapcanvas
     */
    public LoadColorMap(MapCanvas mapCanvas)
    {
        fillMap = new HashMap<>();
        strokeMap = new HashMap<>();
        this.mapCanvas = mapCanvas;
        loadFillMap();
    }


    /**
     * Default map for stroking
     */
    public void loadStrokeMap()
    {
        strokeMap.clear();
        strokeMap.put(WayType.HIGHWAY_UNCLASSIFIED, Color.WHITE);
        strokeMap.put(WayType.HIGHWAY_PATH, Color.RED);
        strokeMap.put(WayType.HIGHWAY_TRACK, Color.SIENNA);
        strokeMap.put(WayType.HIGHWAY_CYCLEWAY, cycleway);
        strokeMap.put(WayType.HIGHWAY_STEPS, Color.ORANGE);
        strokeMap.put(WayType.HIGHWAY_FOOTWAY, Color.RED);
        strokeMap.put(WayType.HIGHWAY_SERVICE, Color.WHITE);
        strokeMap.put(WayType.HIGHWAY_RESIDENTIAL, Color.WHITE);
        strokeMap.put(WayType.HIGHWAY_TERTIARY, Color.WHITE);;
        strokeMap.put(WayType.HIGHWAY_MOTORWAY, Color.RED);
        strokeMap.put(WayType.HIGHWAY_MOTORWAY_LINK, Color.RED);
        strokeMap.put(WayType.HIGHWAY_SECONDARY, secondaryroad);
        strokeMap.put(WayType.HIGHWAY_PRIMARY, primaryroad);
        normalStroke = true;

        mapCanvas.setStrokeMap(strokeMap);
    }

    /**
     * Default map for filling
     */
    public void loadFillMap()
    {
        fillMap.clear();
        mapCanvas.setWaterColor(watercolor);
        fillMap.put(WayType.COASTLINE, ground);
        fillMap.put(WayType.WATER, watercolor);
        fillMap.put(WayType.LANDUSE_RESIDENTIAL, buildingarea);
        fillMap.put(WayType.LANDUSE_FOREST, forest);
        fillMap.put(WayType.LANDUSE_INDUSTRIAL, industrialarea);
        fillMap.put(WayType.LANDUSE_ALLOTMENTS, park);
        fillMap.put(WayType.LANDUSE_GRASS, grass);
        fillMap.put(WayType.LANDUSE_TRACK, Color.BURLYWOOD);
        fillMap.put(WayType.LANDUSE_PARK, park);
        fillMap.put(WayType.LANDUSE_PLAYGROUND, Color.LIGHTCYAN);
        fillMap.put(WayType.LANDUSE_HOSPITAL, Color.INDIANRED);
        fillMap.put(WayType.LANDUSE_RESERVOIR, watercolor);
        fillMap.put(WayType.LANDUSE_FARMYARD, farmyard);
        fillMap.put(WayType.LANDUSE_FARMLAND, farmyard);
        fillMap.put(WayType.LANDUSE_BASIN, watercolor);
        fillMap.put(WayType.AMENITY_SCHOOL, Color.BEIGE);
        fillMap.put(WayType.AMENITY_MARKETPLACE,  Color.BEIGE);
        fillMap.put(WayType.AMENITY_PARKING, Color.LIGHTGRAY);
        fillMap.put(WayType.AMENITY_BUS_STATION, Color.DARKGRAY);
        fillMap.put(WayType.AMENITY_TAXI, Color.DARKGRAY);
        fillMap.put(WayType.LEISURE_PITCH, Color.SEAGREEN);
        fillMap.put(WayType.MAN_MADE_PIER, Color.LEMONCHIFFON);
        fillMap.put(WayType.MAN_MADE_PITCH, Color.SEAGREEN);
        fillMap.put(WayType.NATURAL_HEATH, Color.DARKKHAKI);
        fillMap.put(WayType.NATURAL_SCRUB, Color.OLIVEDRAB);
        fillMap.put(WayType.AEROWAY_HELIPAD, Color.DARKGREY);
        fillMap.put(WayType.AEROWAY_AERODROME, Color.WHEAT);
        fillMap.put(WayType.AEROWAY_APRON, Color.LIGHTGREY);
        fillMap.put(WayType.AEROWAY_RUNWAY, Color.GREY);
        fillMap.put(WayType.AEROWAY_TAXIWAY, Color.GREY);
        fillMap.put(WayType.POWER_GENERATOR, Color.ROSYBROWN);
        fillMap.put(WayType.TOURISM_CAMP_SITE, Color.SADDLEBROWN);
        fillMap.put(WayType.TOURISM_THEME_PARK, Color.SADDLEBROWN);
        fillMap.put(WayType.BUILDING, building);
        fillMap.put(WayType.WATER, watercolor);
        if(!normalStroke) loadStrokeMap();
        mapCanvas.setFillMap(fillMap);
    }
    // https://www.templatemonster.com/blog/designing-colorblind-friendly-website/ "link til color blind"

    /**
     * Colorblind map for filling
     */
    public void loadCBFillMap()
    {
        fillMap.clear();
        // Color.rgb(233, 41, 0)
        mapCanvas.setWaterColor(Color.STEELBLUE);
        fillMap.put(WayType.COASTLINE, Color.DARKGREEN);
        fillMap.put(WayType.WATER, Color.SKYBLUE);
        fillMap.put(WayType.LANDUSE_RESIDENTIAL, Color.DARKSLATEGRAY);
        fillMap.put(WayType.LANDUSE_FOREST, Color.DARKOLIVEGREEN);
        fillMap.put(WayType.LANDUSE_INDUSTRIAL, Color.INDIGO);
        fillMap.put(WayType.LANDUSE_ALLOTMENTS, Color.DARKOLIVEGREEN);
        fillMap.put(WayType.LANDUSE_GRASS, Color.LIGHTGREEN);
        fillMap.put(WayType.LANDUSE_TRACK, Color.BURLYWOOD);
        fillMap.put(WayType.LANDUSE_PARK, Color.DARKOLIVEGREEN);
        fillMap.put(WayType.LANDUSE_PLAYGROUND, Color.LIGHTCYAN);
        fillMap.put(WayType.LANDUSE_HOSPITAL, Color.INDIANRED);
        fillMap.put(WayType.LANDUSE_RESERVOIR, Color.SKYBLUE);
        fillMap.put(WayType.LANDUSE_FARMYARD, Color.DARKGOLDENROD);
        fillMap.put(WayType.LANDUSE_FARMLAND, Color.PERU);
        fillMap.put(WayType.LANDUSE_BASIN, Color.CYAN);
        fillMap.put(WayType.AMENITY_SCHOOL, Color.DARKGRAY);
        fillMap.put(WayType.AMENITY_MARKETPLACE,  Color.DARKGRAY);
        fillMap.put(WayType.AMENITY_PARKING, Color.DARKGRAY);
        fillMap.put(WayType.AMENITY_BUS_STATION, Color.DARKGRAY);
        fillMap.put(WayType.AMENITY_TAXI, Color.DARKGRAY);
        fillMap.put(WayType.LEISURE_PITCH, Color.SEAGREEN);
        fillMap.put(WayType.MAN_MADE_PIER, Color.LEMONCHIFFON);
        fillMap.put(WayType.MAN_MADE_PITCH, Color.SEAGREEN);
        fillMap.put(WayType.NATURAL_HEATH, Color.DARKKHAKI);
        fillMap.put(WayType.NATURAL_SCRUB, Color.OLIVEDRAB);
        fillMap.put(WayType.AEROWAY_HELIPAD, Color.DARKGREY);
        fillMap.put(WayType.AEROWAY_AERODROME, Color.WHEAT);
        fillMap.put(WayType.AEROWAY_APRON, Color.LIGHTGREY);
        fillMap.put(WayType.AEROWAY_RUNWAY, Color.GREY);
        fillMap.put(WayType.AEROWAY_TAXIWAY, Color.GREY);
        fillMap.put(WayType.POWER_GENERATOR, Color.ROSYBROWN);
        fillMap.put(WayType.TOURISM_CAMP_SITE, Color.SADDLEBROWN);
        fillMap.put(WayType.TOURISM_THEME_PARK, Color.SADDLEBROWN);
        fillMap.put(WayType.BUILDING, Color.BURLYWOOD);
        fillMap.put(WayType.WATER, Color.SKYBLUE);
        mapCanvas.setFillMap(fillMap);
        loadStrokeMap();
    }

    /**
     * Black and white map for filling
     */
    public void loadBwFillMap()
    {
        fillMap.clear();
        mapCanvas.setWaterColor(Color.WHITE);
        fillMap.put(WayType.COASTLINE, Color.DARKSLATEGRAY);
        fillMap.put(WayType.WATER, Color.LIGHTGREY);
        fillMap.put(WayType.LANDUSE_RESIDENTIAL, Color.DIMGRAY);
        fillMap.put(WayType.LANDUSE_FOREST, Color.DIMGRAY);
        fillMap.put(WayType.LANDUSE_INDUSTRIAL, Color.DARKGRAY);
        fillMap.put(WayType.LANDUSE_ALLOTMENTS, Color.DARKGRAY);
        fillMap.put(WayType.LANDUSE_GRASS, Color.LIGHTGREY);
        fillMap.put(WayType.LANDUSE_TRACK, Color.WHITE);
        fillMap.put(WayType.LANDUSE_PARK, Color.DARKGRAY);
        fillMap.put(WayType.LANDUSE_PLAYGROUND, Color.DARKGRAY);
        fillMap.put(WayType.LANDUSE_HOSPITAL, Color.DARKGRAY);
        fillMap.put(WayType.LANDUSE_RESERVOIR, Color.DARKGRAY);
        fillMap.put(WayType.LANDUSE_FARMYARD, Color.LIGHTGREY);
        fillMap.put(WayType.LANDUSE_FARMLAND, Color.GRAY);
        fillMap.put(WayType.LANDUSE_BASIN, Color.LIGHTGREY);
        fillMap.put(WayType.AMENITY_SCHOOL, Color.DARKGRAY);
        fillMap.put(WayType.AMENITY_MARKETPLACE,  Color.DARKGRAY);
        fillMap.put(WayType.AMENITY_PARKING, Color.LIGHTGRAY);
        fillMap.put(WayType.AMENITY_BUS_STATION, Color.DARKGRAY);
        fillMap.put(WayType.AMENITY_TAXI, Color.DARKGRAY);
        fillMap.put(WayType.LEISURE_PITCH, Color.DARKGRAY);
        fillMap.put(WayType.MAN_MADE_PIER, Color.LIGHTGREY);
        fillMap.put(WayType.MAN_MADE_PITCH, Color.LIGHTGREY);
        fillMap.put(WayType.NATURAL_HEATH, Color.DARKGRAY);
        fillMap.put(WayType.NATURAL_SCRUB, Color.GRAY);
        fillMap.put(WayType.AEROWAY_HELIPAD, Color.DARKGREY);
        fillMap.put(WayType.AEROWAY_AERODROME, Color.LIGHTGREY);
        fillMap.put(WayType.AEROWAY_APRON, Color.LIGHTGREY);
        fillMap.put(WayType.AEROWAY_RUNWAY, Color.GREY);
        fillMap.put(WayType.AEROWAY_TAXIWAY, Color.GREY);
        fillMap.put(WayType.POWER_GENERATOR, Color.DARKGRAY);
        fillMap.put(WayType.TOURISM_CAMP_SITE, Color.DARKGRAY);
        fillMap.put(WayType.TOURISM_THEME_PARK, Color.DARKGRAY);
        fillMap.put(WayType.BUILDING, Color.WHITE);
        fillMap.put(WayType.WATER, Color.LIGHTGREY);
        mapCanvas.setFillMap(fillMap);
        loadBwStrokeMap();
    }

    /**
     * Black and white map for stroking
     */
    public void loadBwStrokeMap()
    {
        strokeMap.clear();
        strokeMap.put(WayType.HIGHWAY_UNCLASSIFIED, Color.BLACK);
        strokeMap.put(WayType.HIGHWAY_PATH, Color.BLACK);
        strokeMap.put(WayType.HIGHWAY_TRACK, Color.BLACK);
        strokeMap.put(WayType.HIGHWAY_CYCLEWAY, Color.BLACK);
        strokeMap.put(WayType.HIGHWAY_STEPS, Color.BLACK);
        strokeMap.put(WayType.HIGHWAY_FOOTWAY, Color.BLACK);
        strokeMap.put(WayType.HIGHWAY_SERVICE, Color.BLACK);
        strokeMap.put(WayType.HIGHWAY_RESIDENTIAL, Color.BLACK);
        strokeMap.put(WayType.HIGHWAY_TERTIARY, Color.BLACK);
        strokeMap.put(WayType.HIGHWAY_SECONDARY, Color.BLACK);
        strokeMap.put(WayType.HIGHWAY_PRIMARY, Color.BLACK);
        strokeMap.put(WayType.HIGHWAY_MOTORWAY, Color.BLACK);
        strokeMap.put(WayType.HIGHWAY_MOTORWAY_LINK, Color.BLACK);
        strokeMap.put(WayType.BUILDING, Color.BLACK);
        strokeMap.put(WayType.WATER, Color.BLACK);
        normalStroke = false;
        mapCanvas.setStrokeMap(strokeMap);
    }
}
