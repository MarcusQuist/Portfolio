package Draw;

import OSM.Model;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;

import java.text.DecimalFormat;
import java.util.ArrayList;

import java.util.HashMap;

import OSM.Node;

/**
 * Draws and changes the GraphicsContext as well
 * as defining when each WayType is drawn onto the map,
 * according to the zoomFactor.
 */
public class MapCanvas extends Canvas {
    GraphicsContext gc = getGraphicsContext2D();
    Affine transform = new Affine();
    Model model;
    private double zoomFactor;
    private float maxlat;
    private float maxlon;
    private float minlat;
    private float minlon;
    private float drawMaxLat;
    private float drawMaxLon;
    private float drawMinLat;
    private float drawMinLon;

    private int mapFactor;
    boolean showKd;
    private Rectangle2D primaryScreenBounds;
    private dialogBox dialogBox;

    private HashMap<WayType, Color> strokeMap;
    private HashMap<WayType, Color> fillMap;
    private Color waterColor;

    private String poiType = "";
    private double boundDistance;
    private double stageHeight;
    Image flagImage;
    Image markerImage;
    Image pinFromImage;
    Image pinToImage;
    Image busImage;
    Image shopImage;
    Image hospitalImage;

    Image taxiImage;
    Image cafeImage;

    /**
     * Initialises fields, poi images and sets camera to correct panning and zoom (so whole map is viewed upon init)
     *
     * @param model Current model
     */
    public void init(Model model) {
        strokeMap = new HashMap<>();
        fillMap = new HashMap<>();
        this.model = model;

        pan(-model.getMinlon(), -model.getMaxlat());
        primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        transform.prependScale(1, -1, 0, 0);

        zoom(800 / (model.getMaxlon() - model.getMinlon()), 0, 0);

        boundDistance = getDistance();
        mapFactor = (int) Math.log10(Math.toIntExact(Math.round(boundDistance)));
        if (boundDistance > 1000) {
            mapFactor += 1;
        }
        boundDistance = getDistance();

        zoom(mapFactor + 1, primaryScreenBounds.getHeight() / 2, primaryScreenBounds.getHeight() / 2);


        flagImage = rotateImage("/All/flag.png");
        markerImage = rotateImage("/All/Default_marker.png");
        pinFromImage = rotateImage("/All/Pin_from.png");
        pinToImage = rotateImage("/All/Pin_to.png");
        busImage = rotateImage("/All/Icons_PNG/Bus.png");
        shopImage = rotateImage("/All/Icons_PNG/Supermarket.png");
        hospitalImage = rotateImage("/All/Icons_PNG/Hospital.png");
        taxiImage = rotateImage("/All/Icons_PNG/Taxi.png");
        cafeImage = rotateImage("/All/Icons_PNG/Coffee.png");
        stageHeight = 0;
        dialogBox = new dialogBox();
        zoomFactor = 1;

    }

    /**
     * Sets current map for stroke colors
     *
     * @param strokeMap Map that will be used to stroke
     */
    public void setStrokeMap(HashMap<WayType, Color> strokeMap) {
        this.strokeMap = strokeMap;
        //repaint();
    }

    /**
     * Sets current map for fill colors
     *
     * @param fillMap Map that will be used to fill
     */
    public void setFillMap(HashMap<WayType, Color> fillMap) {
        this.fillMap = fillMap;
    }

    /**
     * Sets the color of the water
     *
     * @param waterColor Color of the water
     */
    public void setWaterColor(Color waterColor) {
        this.waterColor = waterColor;
    }

    /**
     * (re)Draws the map
     */
    public void repaint() {
        gc.setTransform(new Affine());
        if (model.getWaysOfType(WayType.COASTLINE).iterator().hasNext()) {
            gc.setFill(waterColor);
        }
        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setTransform(transform);
        gc.setFillRule(FillRule.EVEN_ODD);
        gc.setFill(fillMap.get(WayType.COASTLINE));
        for (Drawable way : model.getWaysOfType(WayType.COASTLINE)) way.fill(gc);

        if (zoomFactor >= 11 + mapFactor) {
            fill(WayType.WATER, fillMap.get((WayType.WATER)));
        }
        if (zoomFactor >= 12 + mapFactor) {
            fill(WayType.LANDUSE_RESIDENTIAL, fillMap.get(WayType.LANDUSE_RESIDENTIAL));
        }

        if (zoomFactor >= 14) {
            fill(WayType.LANDUSE_FOREST, fillMap.get(WayType.LANDUSE_FOREST));
            fill(WayType.LANDUSE_INDUSTRIAL, fillMap.get(WayType.LANDUSE_INDUSTRIAL));
            fill(WayType.LANDUSE_ALLOTMENTS, fillMap.get(WayType.LANDUSE_ALLOTMENTS));
            fill(WayType.LANDUSE_GRASS, fillMap.get(WayType.LANDUSE_GRASS));
            fill(WayType.LANDUSE_TRACK, fillMap.get(WayType.LANDUSE_TRACK));
            fill(WayType.LANDUSE_PARK, fillMap.get(WayType.LANDUSE_PARK));
            fill(WayType.LANDUSE_PLAYGROUND, fillMap.get(WayType.LANDUSE_PLAYGROUND));
            fill(WayType.LANDUSE_HOSPITAL, fillMap.get(WayType.LANDUSE_HOSPITAL));
            fill(WayType.LANDUSE_RESERVOIR, fillMap.get(WayType.LANDUSE_RESERVOIR));
        }

        if (zoomFactor >= 14 + mapFactor) {
            fill(WayType.LANDUSE_FARMYARD, fillMap.get(WayType.LANDUSE_FARMYARD));
            fill(WayType.LANDUSE_FARMLAND, fillMap.get(WayType.LANDUSE_FARMLAND));
        }

        if (zoomFactor >= 14 + mapFactor)
            fill(WayType.LANDUSE_BASIN, fillMap.get(WayType.LANDUSE_BASIN));

        if (zoomFactor >= 14 + mapFactor) {
            fill(WayType.AMENITY_SCHOOL, fillMap.get(WayType.AMENITY_SCHOOL));
            fill(WayType.AMENITY_MARKETPLACE, fillMap.get(WayType.AMENITY_MARKETPLACE));
            fill(WayType.AMENITY_PARKING, fillMap.get(WayType.AMENITY_PARKING));
            fill(WayType.AMENITY_BUS_STATION, fillMap.get(WayType.AMENITY_BUS_STATION));
            fill(WayType.AMENITY_TAXI, fillMap.get(WayType.AMENITY_TAXI));

            fill(WayType.LEISURE_PITCH, fillMap.get(WayType.LEISURE_PITCH));

        }

        if (zoomFactor >= 14 + mapFactor) fill(WayType.NATURAL_HEATH, fillMap.get(WayType.NATURAL_HEATH));

        if (zoomFactor >= 11 + mapFactor) fill(WayType.NATURAL_SCRUB, fillMap.get(WayType.NATURAL_SCRUB));

        if (zoomFactor >= 15 + mapFactor) {
            fill(WayType.AEROWAY_HELIPAD, fillMap.get(WayType.AEROWAY_HELIPAD));
            fill(WayType.AEROWAY_AERODROME, fillMap.get(WayType.AEROWAY_AERODROME));
            fill(WayType.AEROWAY_APRON, fillMap.get(WayType.AEROWAY_APRON));
            widthStroke(WayType.AEROWAY_RUNWAY, 0.0006, fillMap.get(WayType.AEROWAY_RUNWAY));
            widthStroke(WayType.AEROWAY_TAXIWAY, 0.00006, fillMap.get(WayType.AEROWAY_TAXIWAY));
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1 / Math.sqrt(Math.abs(transform.determinant())));

            fill(WayType.POWER_GENERATOR, fillMap.get(WayType.POWER_GENERATOR));

            fill(WayType.TOURISM_CAMP_SITE, fillMap.get(WayType.TOURISM_CAMP_SITE));
            fill(WayType.TOURISM_THEME_PARK, fillMap.get(WayType.TOURISM_THEME_PARK));
        }
        if (zoomFactor >= 16 + mapFactor) {
            widthStroke(WayType.HIGHWAY_UNCLASSIFIED, 0.00006, strokeMap.get(WayType.HIGHWAY_UNCLASSIFIED));
            widthStroke(WayType.HIGHWAY_PATH, 0.00002, strokeMap.get(WayType.HIGHWAY_PATH));
            widthStroke(WayType.HIGHWAY_TRACK, 0.00002, strokeMap.get(WayType.HIGHWAY_TRACK));
            widthStroke(WayType.HIGHWAY_CYCLEWAY, 0.00002, strokeMap.get(WayType.HIGHWAY_CYCLEWAY));
            widthStroke(WayType.HIGHWAY_STEPS, 0.00002, strokeMap.get(WayType.HIGHWAY_STEPS));
            widthStroke(WayType.HIGHWAY_FOOTWAY, 0.00002, strokeMap.get(WayType.HIGHWAY_FOOTWAY));
            widthStroke(WayType.HIGHWAY_SERVICE, 0.00003, strokeMap.get(WayType.HIGHWAY_SERVICE));
            widthStroke(WayType.HIGHWAY_RESIDENTIAL, 0.00006, strokeMap.get(WayType.HIGHWAY_RESIDENTIAL));
            widthStroke(WayType.HIGHWAY_MOTORWAY, 0.00006, strokeMap.get(WayType.HIGHWAY_RESIDENTIAL));
            widthStroke(WayType.HIGHWAY_MOTORWAY_LINK, 0.00006, strokeMap.get(WayType.HIGHWAY_RESIDENTIAL));
        }
        if (zoomFactor >= 7 + mapFactor) {
            widthStroke(WayType.HIGHWAY_TERTIARY, 0.0001, strokeMap.get(WayType.HIGHWAY_TERTIARY));
            widthStroke(WayType.HIGHWAY_SECONDARY, 0.0001, strokeMap.get(WayType.HIGHWAY_SERVICE));
            widthStroke(WayType.HIGHWAY_PRIMARY, 0.0001, strokeMap.get(WayType.HIGHWAY_PRIMARY));
        }
        if (zoomFactor >= 17 + mapFactor) {
            gc.setLineWidth(1 / Math.sqrt(Math.abs(transform.determinant())));
            if (zoomFactor <= 14 + mapFactor) fill(WayType.BUILDING, fillMap.get(WayType.BUILDING));
            if (zoomFactor >= 15 + mapFactor)
                fill(WayType.BUILDING, fillMap.get(WayType.BUILDING));


        }
        if (zoomFactor >= 11 + mapFactor) fill(WayType.WATER, fillMap.get(WayType.WATER));

        for (Drawable poi : model.getWaysOfType(WayType.POI_VISIT)) poi.draw(gc, zoomFactor, flagImage);
        for (Drawable poi : model.getWaysOfType(WayType.POI_MARKER)) poi.draw(gc, zoomFactor, markerImage);



        if (zoomFactor >= 16 + mapFactor) {


            model.getTrees().get(WayType.CARTOGRAPHIC_BUS_STOP).getList(minlon, minlat, maxlon, maxlat);
            for (Drawable CartographicNode : model.getTrees().get(WayType.CARTOGRAPHIC_BUS_STOP).getDrawables())
                CartographicNode.draw(gc, zoomFactor, busImage);


            model.getTrees().get(WayType.CARTOGRAPHIC_SHOP).getList(minlon, minlat, maxlon, maxlat);
            for (Drawable CartographicNode : model.getTrees().get(WayType.CARTOGRAPHIC_SHOP).getDrawables())
                CartographicNode.draw(gc, zoomFactor, shopImage);


            model.getTrees().get(WayType.CARTOGRAPHIC_HOSPITAL).getList(minlon, minlat, maxlon, maxlat);
            for (Drawable CartographicNode : model.getTrees().get(WayType.CARTOGRAPHIC_HOSPITAL).getDrawables())
                CartographicNode.draw(gc, zoomFactor, hospitalImage);

            model.getTrees().get(WayType.CARTOGRAPHIC_TAXI).getList(minlon, minlat, maxlon, maxlat);
            for (Drawable CartographicNode : model.getTrees().get(WayType.CARTOGRAPHIC_TAXI).getDrawables())
                CartographicNode.draw(gc, zoomFactor, taxiImage);


            model.getTrees().get(WayType.CARTOGRAPHIC_CAFE).getList(minlon, minlat, maxlon, maxlat);
            for (Drawable CartographicNode : model.getTrees().get(WayType.CARTOGRAPHIC_CAFE).getDrawables())
                CartographicNode.draw(gc, zoomFactor, cafeImage);
        }

        if (model.getDrawRoute()) {
            pathNodeStroke(model.getPathNodes(), 0.0001, Color.rgb(3, 128, 254, 1));

            model.getPinFrom().draw(gc, zoomFactor, pinFromImage);
            model.getPinTo().draw(gc, zoomFactor, pinToImage);
        }
        meterMeasure();
        drawKD();
    }

    /**
     * Sets the height of current stage.
     * This is done to place the meter measure a fixed place on the screen.
     *
     * @param height of the current screen.
     */
    public void setStageHeight(double height) {
        stageHeight = height;
    }

    /**
     * Draws a meter measure.
     */
    public String meterMeasure() {
        try {
            Point2D xy = transform.inverseTransform(50, stageHeight - 20);
            Point2D maxxy = transform.inverseTransform(150, stageHeight - 20);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(3 * (2 / Math.sqrt(Math.abs(transform.determinant()))));
            gc.beginPath();
            gc.moveTo(xy.getX(), xy.getY());
            gc.lineTo(maxxy.getX(), maxxy.getY());
            gc.lineTo(xy.getX(), xy.getY());
            gc.stroke();
            double distance = getDistance(xy.getX(), maxxy.getX(), xy.getY(), maxxy.getY());
            String suffix = "km";
            if (distance < 1) {
                distance *= 1000;
                suffix = "m";
            }
            return new DecimalFormat("#.00").format(distance) + suffix;
        } catch (NonInvertibleTransformException e) {
        } catch (NullPointerException e) {
        }
        return "";
    }


    /**
     * Fills a waytype
     *
     * @param wayType   Waytype to fill
     * @param fillColor Fill color
     */
    private void fill(WayType wayType, Color fillColor) {
        model.getTrees().get(wayType).getList(minlon, minlat, maxlon, maxlat);
        gc.setFill(fillColor);
        for (Drawable way : model.getTrees().get(wayType).getDrawables()) way.fill(gc);
    }

    /**
     * Strokes a waytype
     *
     * @param wayType     Waytype to stroke
     * @param width       Width of stroke
     * @param strokeColor Stroke color
     */
    private void widthStroke(WayType wayType, double width, Color strokeColor) {
        model.getTrees().get(wayType).getList(minlon, minlat, maxlon, maxlat);
        gc.setLineWidth(width);
        gc.setStroke(strokeColor);
        for (Drawable way : model.getTrees().get(wayType).getDrawables()) {
            way.stroke(gc);
        }

    }

    /**
     * Draws the path between nodes
     *
     * @param list        with nodes for the streets
     * @param width       sets the width of the streets
     * @param strokeColor sets the
     */
    private void pathNodeStroke(ArrayList<Node> list, double width, Color strokeColor) {
        gc.setLineWidth(width);
        double factor = 0.00005;
        gc.setLineWidth(factor + (2 * (3 / Math.sqrt(Math.abs(transform.determinant())))));
        gc.setStroke(strokeColor);
        gc.beginPath();
        gc.moveTo(list.get(0).getLon(), list.get(0).getLat());
        for (int i = 1; i < model.getPathNodes().size(); i++) {
            gc.lineTo(model.getPathNodes().get(i).getLon(), model.getPathNodes().get(i).getLat());
        }
        gc.stroke();
    }


    /**
     * Moves camera to coordinates
     *
     * @param x X coordinate of camera center
     * @param y Y coordinate of camera center
     */
    public void panCoords(float x, float y) {
        try {
            Point2D change = new Point2D(x, y);
            Point2D tchange = transform.transform(change.getX(), change.getY());
            transform.setTx(transform.getTx() - tchange.getX() + (primaryScreenBounds.getMaxX() / 2));
            transform.setTy(transform.getTy() - tchange.getY() + (primaryScreenBounds.getMaxY() / 2));
            changeCoords();
        } catch (Exception e) {
        }
    }

    /**
     * Appends coordinates to current camera center
     *
     * @param dx X coordinate to append
     * @param dy Y coordinate to append
     * @param dy Y coordinate to append
     */
    public void pan(double dx, double dy) {
        transform.prependTranslation(dx, dy);
        changeCoords();
    }

    /**
     * Creates PoI node
     *
     * @param dx X coordinate of node
     * @param dy Y coordinate of node
     */
    public void createPOI(double dx, double dy) {
        try {
            POINode node = new POINode((float) transform.inverseTransform(dx, dy).getX(), (float) transform.inverseTransform(dx, dy).getY());

            if (poiType.equals("marker")) {
                if (model.getWays().get(WayType.POI_MARKER).isEmpty()) {
                    model.putDrawable(WayType.POI_MARKER, node);
                }
                if (poiType.equals("marker") && !model.getWays().get(WayType.POI_MARKER).isEmpty()) {
                    model.removeDrawable(WayType.POI_MARKER);
                    model.putDrawable(WayType.POI_MARKER, node);
                }
                repaint();
            }
            if (poiType.equals("visit")) {
                model.putDrawable(WayType.POI_VISIT, node);
            }

            repaint();
        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zoom in/out
     *
     * @param factor Factor to zoom with
     * @param x      X coordinate to zoom towards
     * @param y      Y coordinate to zoom towards
     */
    public void zoom(double factor, double x, double y) {
        transform.prependScale(factor, factor, x, y);
        changeCoords();
    }

    /**
     * Zoom in/out iterativly
     *
     * @param increase How many times to zoom in/out
     * @param x        X coordinate to zoom towards
     * @param y        Y coordinate to zoom towards
     */
    public void scaleZoom(double increase, double x, double y) {
        double increases = increase - zoomFactor;
        double factor;
        if (increases > 0) {
            factor = 1.33334;
            for (int i = 0; i < increases; i++) {
                zoom(factor, x, y);
            }
        }
        if (increases < 0) {
            factor = 0.75;
            for (int i = 0; i < Math.abs(increases); i++) {
                zoom(factor, x, y);
            }
        }
        zoomFactor = increase;
    }

    /**
     * Zoom in
     *
     * @param value    Current value from zoomslider
     * @param maxValue The maxvalue the zoomslider can go to
     * @param x        X coordinate to zoom towards
     * @param y        Y coordinate to zoom towards
     * @return the new value for the zoomslider
     */
    public double plusZoom(double value, double maxValue, double x, double y) {
        if (value <= maxValue - 1) {
            scaleZoom(value + 1, x, y);
            return value + 1;
        }
        return value;
    }

    /**
     * Zoom out
     *
     * @param value    Current value from zoomslider
     * @param minValue The minvalue the zoomslider can go to
     * @param x        X coordinate to zoom away from
     * @param y        Y coordinate to zoom away from
     * @return the new value for the zoomslider
     */
    public double minusZoom(double value, double minValue, double x, double y) {
        if (value >= minValue + 1) {
            scaleZoom(value - 1, x, y);
            return value - 1;
        }
        return value;
    }

    /**
     * Sets the type of POI node
     *
     * @param poiType The type of POI
     */
    public void setPoiType(String poiType) {
        this.poiType = poiType;
    }

    /**
     * @return The x coordinate of the camera center
     */
    public double getScreenCenterX() {
        return primaryScreenBounds.getMaxX() / 2;
    }

    /**
     * @return The Y coordinate of the camera center
     */
    public double getScreenCenterY() {
        return primaryScreenBounds.getMaxY() / 2;
    }

    /**
     * Represents the zoom slider.
     */
    public void changeCoords() {
        if (showKd) {
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();


            if (zoomFactor < 14) {
                try {

                    minlat = (float) transform.inverseTransform(getScreenCenterX(), getScreenCenterY() + 300).getY();
                    drawMinLat = (float) transform.inverseTransform(getScreenCenterX(), getScreenCenterY() + 150).getY();

                    minlon = (float) transform.inverseTransform(getScreenCenterX() - 300, getScreenCenterY()).getX();
                    drawMinLon = (float) transform.inverseTransform(getScreenCenterX() - 150, getScreenCenterY()).getX();

                    maxlat = (float) transform.inverseTransform(getScreenCenterX(), getScreenCenterY() - 300).getY();
                    drawMaxLat = (float) transform.inverseTransform(getScreenCenterX(), getScreenCenterY() - 150).getY();

                    maxlon = (float) transform.inverseTransform(getScreenCenterX() + 300, getScreenCenterY()).getX();
                    drawMaxLon = (float) transform.inverseTransform(getScreenCenterX() + 150, getScreenCenterY()).getX();

                } catch (NonInvertibleTransformException e) {
                }


            } else {
                try {
                    minlat = ((float) transform.inverseTransform(primaryScreenBounds.getMaxX(), primaryScreenBounds.getMaxY()).getY());
                    drawMinLat = (float) transform.inverseTransform(getScreenCenterX(), getScreenCenterY() - 150).getY();

                    minlon = ((float) transform.inverseTransform(primaryScreenBounds.getMinX(), primaryScreenBounds.getMinY()).getX());
                    drawMinLon = (float) transform.inverseTransform(getScreenCenterX() + 150, getScreenCenterY()).getX();

                    maxlat = ((float) transform.inverseTransform(primaryScreenBounds.getMinX(), primaryScreenBounds.getMinY()).getY());
                    drawMaxLat = (float) transform.inverseTransform(getScreenCenterX(), getScreenCenterY() + 150).getY();

                    maxlon = ((float) transform.inverseTransform(primaryScreenBounds.getMaxX(), primaryScreenBounds.getMaxY()).getX());
                    drawMaxLon = (float) transform.inverseTransform(getScreenCenterX() - 150, getScreenCenterY()).getX();


                } catch (NonInvertibleTransformException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            drawMinLat = (0);
            drawMinLon = (0);
            drawMaxLat = (0);
            drawMaxLon = (0);

            try {
                minlat = ((float) transform.inverseTransform(primaryScreenBounds.getMaxX(), primaryScreenBounds.getMaxY() * 2).getY());
                minlon = ((float) transform.inverseTransform(primaryScreenBounds.getMinX() - primaryScreenBounds.getMaxX(), primaryScreenBounds.getMinY()).getX());
                maxlat = ((float) transform.inverseTransform(primaryScreenBounds.getMinX(), primaryScreenBounds.getMinY() - primaryScreenBounds.getMaxY()).getY());
                maxlon = ((float) transform.inverseTransform(primaryScreenBounds.getMaxX() * 2, primaryScreenBounds.getMaxY()).getX());


            } catch (NonInvertibleTransformException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Shows the KD
     *
     * @param showKd
     */
    public void setShowKd(boolean showKd) {
        this.showKd = showKd;
    }

    /**
     * Checks if the KD is shown.
     *
     * @return
     */
    public boolean isShowKd() {
        return showKd;
    }

    /**
     * Rotates image
     *
     * @param url Image url
     * @return The rotated image
     */
    // Inspiration to rotating and flipping the image
    // https://stackoverflow.com/questions/33613664/javafx-drawimage-rotated
    public Image rotateImage(String url) {
        ImageView iv = new ImageView(new Image(url));
        iv.setRotate(180);
        iv.setTranslateZ(iv.getBoundsInLocal().getWidth() / 2.0);
        iv.setRotationAxis(Rotate.X_AXIS);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        Image image = iv.snapshot(params, null);
        return image;
    }

    // Haversine implementation
    //https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula
    public double getDistance() {
        double p = 0.017453292519943295;
        double a = 0.5 - Math.cos((maxlat - minlat) * p) / 2 +
                Math.cos(minlat * p) * Math.cos(maxlat * p) *
                        (1 - Math.cos((maxlon - minlon) * p)) / 2;
        return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
    }

    public double getDistance(double x1, double x2, double y1, double y2) {
        double p = 0.017453292519943295;
        double a = 0.5 - Math.cos((x2 - x1) * p) / 2 +
                Math.cos(x1 * p) * Math.cos(x2 * p) *
                        (1 - Math.cos((y2 - y1) * p)) / 2;
        return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
    }


    public Affine getTransform() {
        return transform;
    }


    public void drawKD() {

        gc.setStroke(Color.TOMATO);
        drawBox(minlon, minlat, maxlon, maxlat);

        gc.setStroke(Color.CADETBLUE);
        drawBox(drawMinLon, drawMinLat, drawMaxLon, drawMaxLat);

    }

    private void drawBox(float minlon, float minlat, float maxlon, float maxlat) {
        gc.setLineWidth(2 / Math.sqrt(Math.abs(transform.determinant())));
        gc.beginPath();
        gc.moveTo(minlon, minlat);
        gc.lineTo(maxlon, minlat);
        gc.lineTo(maxlon, maxlat);
        gc.lineTo(minlon, maxlat);
        gc.lineTo(minlon, minlat);
        gc.stroke();
    }
}
