package OSM;

import All.*;
import Draw.WayType;
import Kd.TreeCustom;


import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

import java.util.*;

import static javax.xml.stream.XMLStreamConstants.*;
import static javax.xml.stream.XMLStreamConstants.ENTITY_DECLARATION;
import static javax.xml.stream.XMLStreamConstants.NOTATION_DECLARATION;

/**
 * Parses file
 */
public class Parser {

    iModel model;

    /**
     * Sets model to current model
     *
     * @param model Current model
     */
    public Parser(iModel model) {
        this.model = model;
    }

    private ArrayList<Address> addressList = new ArrayList<>();

    private ArrayList<Way> wayList;

    /**
     * Goes through the file creating nodes based on tags
     *
     * @param osmsource File source
     * @throws XMLStreamException
     */
    public void parse(InputStream osmsource) throws XMLStreamException {
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(osmsource);
        LongIndex<Node> idToNode = new LongIndex<>();
        LongIndex<Way> idToWay = new LongIndex<>();
        List<Way> coast = new ArrayList<>();

        ArrayList<Way> roadWayList = new ArrayList<>();
        wayList = new ArrayList<>();

        Way way = null;
        Relation rel = null;
        WayType type = null;

        String city = null;
        String housenumber = null;
        String floor = null;
        String side = null;
        String street = null;
        String postcode = null;
        String roadname = null;
        long id = 0;
        float lat = 0;
        float lon = 0;

        HashMap<Polyline, String> roadnames = new HashMap<>();

        Node currentNode = null;

        while (reader.hasNext()) {
            switch (reader.next()) {
                case START_ELEMENT:
                    switch (reader.getLocalName()) {
                        case "bounds":
                            model.setMinlat(Float.parseFloat(reader.getAttributeValue(null, "minlat")));
                            model.setMinlon(Float.parseFloat(reader.getAttributeValue(null, "minlon")));
                            model.setMaxlat(Float.parseFloat(reader.getAttributeValue(null, "maxlat")));
                            model.setMaxlon(Float.parseFloat(reader.getAttributeValue(null, "maxlon")));
                            model.setLonfactor((float) Math.cos((model.getMaxlat() + model.getMinlat()) / 2 * Math.PI / 180));
                            model.setMinlon(model.getMinlon() * model.getLonfactor());
                            model.setMaxlon(model.getMaxlon() * model.getLonfactor());
                            break;
                        case "node":
                            id = Long.parseLong(reader.getAttributeValue(null, "id"));
                            lat = Float.parseFloat(reader.getAttributeValue(null, "lat"));
                            lon = model.getLonfactor() * Float.parseFloat(reader.getAttributeValue(null, "lon"));
                            currentNode = new Node(id, lon, lat);
                            idToNode.add(currentNode);

                            break;
                        case "way":
                            id = Long.parseLong(reader.getAttributeValue(null, "id"));
                            type = WayType.UNKNOWN;
                            way = new Way(id);
                            way.setMaxSpeed(50);
                            idToWay.add(way);
                            break;
                        case "nd":
                            long ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
                            way.add(idToNode.get(ref));
                            break;
                        case "tag":
                            String k = reader.getAttributeValue(null, "k");
                            String v = reader.getAttributeValue(null, "v");

                            switch (k) {

                                case "addr:city":
                                    city = v;
                                    break;
                                case "addr:housenumber":
                                    housenumber = v;
                                    break;
                                case "addr:floor":
                                    floor = v;
                                    break;
                                case "addr:side":
                                    side = v;
                                    break;
                                case "addr:postcode":
                                    postcode = v;
                                    break;
                                case "addr:street":
                                    street = v;
                                    break;
                                case "building":
                                    type = WayType.BUILDING;
                                    break;


                                case "natural":
                                    switch (v) {
                                        case "water":
                                            type = WayType.WATER;
                                            break;

                                        case "coastline":
                                            type = WayType.COASTLINE;
                                            break;
                                        case "beach":
                                            type = WayType.NATURAL_BEACH;
                                            break;
                                        case "scrub":
                                            type = WayType.NATURAL_SCRUB;
                                            break;
                                        case "heath":
                                            type = WayType.NATURAL_HEATH;
                                            break;
                                        case "bare_rock":
                                            type = WayType.Natural_BARE_ROCK;
                                            break;
                                        case "sand":
                                            type = WayType.NATURAL_SAND;
                                            break;
                                    }
                                    break;

                                case "landuse":
                                    switch (v) {
                                        case "residential":
                                            type = WayType.LANDUSE_RESIDENTIAL;
                                            break;
                                        case "allotments":
                                            type = WayType.LANDUSE_ALLOTMENTS;
                                            break;
                                        case "grass":
                                            type = WayType.LANDUSE_GRASS;
                                            break;
                                        case "sports_centre":
                                            type = WayType.LANDUSE_SPORTS_CENTRE;
                                            break;
                                        case "track":
                                            type = WayType.LANDUSE_TRACK;
                                            break;
                                        case "park":
                                            type = WayType.LANDUSE_PARK;
                                            break;
                                        case "playground":
                                            type = WayType.LANDUSE_PLAYGROUND;
                                            break;
                                        case "industrial":
                                            type = WayType.LANDUSE_INDUSTRIAL;
                                            break;
                                        case "hospital":
                                            type = WayType.LANDUSE_HOSPITAL;
                                            break;
                                        case "reservoir":
                                            type = WayType.LANDUSE_RESERVOIR;
                                            break;
                                        case "forest":
                                            type = WayType.LANDUSE_FOREST;
                                            break;
                                        case "farmland":
                                            type = WayType.LANDUSE_FARMLAND;
                                            break;
                                        case "farmyard":
                                            type = WayType.LANDUSE_FARMYARD;
                                            break;
                                        case "basin":
                                            type = WayType.LANDUSE_BASIN;
                                            break;
                                        case "meadow":
                                            type = WayType.LANDUSE_MEADOW;
                                    }
                                    break;
                                case "waterway":
                                    switch (v) {
                                        case "stream":
                                            type = WayType.WATERWAY_STREAM;
                                            break;

                                        case "ditch":
                                            type = WayType.WATERWAY_DITCH;
                                            break;
                                    }

                                    break;

                                case "amenity":
                                    switch (v) {
                                        case "school":
                                            type = WayType.AMENITY_SCHOOL;
                                            break;
                                        case "marketplace":
                                            type = WayType.AMENITY_MARKETPLACE;
                                            break;
                                        case "bus_station":
                                            type = WayType.AMENITY_BUS_STATION;
                                            break;
                                        case "taxi":
                                            type = WayType.CARTOGRAPHIC_TAXI;
                                            model.getWays().get(WayType.CARTOGRAPHIC_TAXI).add(new CartographicNode(lon, lat));
                                            break;
                                        case "hospital":
                                            type = WayType.CARTOGRAPHIC_HOSPITAL;
                                            if(way == null)
                                            {
                                                model.getWays().get(WayType.CARTOGRAPHIC_HOSPITAL).add(new CartographicNode(lon, lat));
                                                break;
                                            }
                                            if(way != null)
                                            {
                                               model.getWays().get(WayType.CARTOGRAPHIC_HOSPITAL).add(new CartographicNode(way.getFirst().getLon(), way.getFirst().getLat()));
                                                break;
                                            }
                                            break;
                                        case "cafe":
                                            type = WayType.CARTOGRAPHIC_CAFE;
                                            if(way == null)
                                            {
                                               model.getWays().get(WayType.CARTOGRAPHIC_CAFE).add(new CartographicNode(lon, lat));
                                                break;
                                            }
                                            if(way != null)
                                            {
                                               model.getWays().get(WayType.CARTOGRAPHIC_CAFE).add(new CartographicNode(way.getFirst().getLat(), way.getFirst().getLon()));
                                                break;
                                            }
                                    }
                                    break;
                                case "leisure":
                                    switch (v) {
                                        case "pitch":
                                            type = WayType.LEISURE_PITCH;
                                            break;
                                    }

                                    break;
                                case "oneway":
                                    switch (v) {
                                        case "yes":
                                            if (way != null) {
                                                way.isOneWay();
                                                break;
                                            }
                                    }
                                    break;

                                case "junction":
                                    switch (v) {
                                        case "roundabout":
                                            if (way != null) {
                                                way.isOneWay();
                                                break;
                                            }
                                    }
                                    break;

                                case "maxspeed":
                                    switch (v) {
                                        case "DK:urban":
                                            if (way != null) {
                                                way.setMaxSpeed(50);
                                                break;
                                            }
                                        case "DK:rural":
                                            if (way != null) {
                                                way.setMaxSpeed(80);
                                                break;
                                            }
                                        case "DK:motorway":
                                            if (way != null) {
                                                way.setMaxSpeed(130);
                                                break;
                                            }
                                        case "signals":
                                            if (way != null) {
                                                way.setMaxSpeed(50);
                                                break;
                                            }
                                        case "5 knots":
                                            if (way != null) {
                                                way.setMaxSpeed(9);
                                                break;
                                            }
                                        default:
                                            if (way != null) {
                                                int maxSpeed = 50;
                                                try {
                                                    maxSpeed = Integer.parseInt(v);
                                                } catch (Exception e) {

                                                }
                                                way.setMaxSpeed(maxSpeed);
                                                break;
                                            }
                                    }
                                    break;
                                case "highway":
                                    switch (v) {
                                        case "primary":
                                            type = WayType.HIGHWAY_PRIMARY;
                                            if (way != null) {
                                                way.setVehicleAllowed();
                                                way.setBicycleAllowed();
                                                way.setWalkingAllowed();
                                                roadWayList.add(way);
                                                break;
                                            }
                                            break;
                                        case "secondary":
                                            type = WayType.HIGHWAY_SECONDARY;
                                            if (way != null) {
                                                way.setVehicleAllowed();
                                                way.setBicycleAllowed();
                                                way.setWalkingAllowed();
                                                roadWayList.add(way);
                                                break;
                                            }
                                            break;
                                        case "residential":
                                            type = WayType.HIGHWAY_RESIDENTIAL;
                                            if (way != null) {
                                                way.setVehicleAllowed();
                                                way.setBicycleAllowed();
                                                way.setWalkingAllowed();
                                                roadWayList.add(way);
                                                break;
                                            }
                                            break;
                                        case "tertiary":
                                            type = WayType.HIGHWAY_TERTIARY;
                                            if (way != null) {
                                                way.setVehicleAllowed();
                                                way.setBicycleAllowed();
                                                way.setWalkingAllowed();
                                                roadWayList.add(way);
                                            }
                                            break;
                                        case "service":
                                            type = WayType.HIGHWAY_SERVICE;
                                            if (way != null) {
                                                way.setVehicleAllowed();
                                                way.setBicycleAllowed();
                                                way.setWalkingAllowed();
                                                roadWayList.add(way);
                                                break;
                                            }
                                            break;
                                        case "cycleway":
                                            type = WayType.HIGHWAY_CYCLEWAY;
                                            if (way != null) {
                                                way.setBicycleAllowed();
                                                roadWayList.add(way);
                                                break;
                                            }
                                            break;
                                        case "footway":
                                            type = WayType.HIGHWAY_FOOTWAY;
                                            if (way != null) {
                                                way.setWalkingAllowed();
                                                roadWayList.add(way);
                                                break;
                                            }
                                            break;
                                        case "track":
                                            type = WayType.HIGHWAY_TRACK;
                                            if (way != null) {
                                                way.setBicycleAllowed();
                                                way.setWalkingAllowed();
                                                roadWayList.add(way);
                                                break;
                                            }
                                            break;
                                        case "steps":
                                            type = WayType.HIGHWAY_STEPS;
                                            if (way != null) {
                                                way.setWalkingAllowed();
                                                roadWayList.add(way);
                                                break;
                                            }
                                            break;
                                        case "unclassified":
                                            type = WayType.HIGHWAY_UNCLASSIFIED;
                                            if (way != null) {
                                                way.setVehicleAllowed();
                                                way.setBicycleAllowed();
                                                way.setWalkingAllowed();
                                                roadWayList.add(way);
                                                break;
                                            }
                                            break;

                                        case "path":
                                            type = WayType.HIGHWAY_PATH;
                                            if (way != null) {
                                                way.setBicycleAllowed();
                                                way.setWalkingAllowed();
                                                roadWayList.add(way);
                                                break;
                                            }
                                            break;
                                        case "bus_stop":
                                            type = WayType.CARTOGRAPHIC_BUS_STOP;
                                            model.getWays().get(WayType.CARTOGRAPHIC_BUS_STOP).add(new CartographicNode(lon, lat));
                                            break;
                                        case "motorway":
                                            type = WayType.HIGHWAY_MOTORWAY;
                                            if (way != null) {
                                                way.setVehicleAllowed();

                                                roadWayList.add(way);
                                                break;
                                            }
                                            break;
                                        case "motorway_link":
                                            type = WayType.HIGHWAY_MOTORWAY_LINK;
                                            if (way != null) {
                                                way.setVehicleAllowed();

                                                roadWayList.add(way);
                                                break;
                                            }
                                            break;
                                    }
                                    break;
                                case "name":
                                    roadname = v;
                                    street = v;
                                    if(way != null)
                                    {
                                        way.setStreet(street);
                                    }
                                    break;

                                case "aeroway":
                                    switch (v) {
                                        case "helipad":
                                            type = WayType.AEROWAY_HELIPAD;
                                            break;
                                        case "aerodrome":
                                            type = WayType.AEROWAY_AERODROME;
                                            break;
                                        case "runway":
                                            type = WayType.AEROWAY_RUNWAY;
                                            break;
                                        case "taxiway":
                                            type = WayType.AEROWAY_TAXIWAY;
                                            break;
                                        case "apron":
                                            type = WayType.AEROWAY_APRON;
                                            break;
                                    }

                                case "shop":
                                    switch (v) {
                                        case "supermarket":
                                            type = WayType.CARTOGRAPHIC_SHOP;
                                            if(way == null)
                                            {
                                                model.getWays().get(WayType.CARTOGRAPHIC_SHOP).add(new CartographicNode(lon, lat));
                                                break;
                                            }
                                            if(way != null)
                                            {
                                                model.getWays().get(WayType.CARTOGRAPHIC_SHOP).add(new CartographicNode(way.getFirst().getLon(), way.getFirst().getLat()));
                                                break;
                                            }
                                    }
                                    break;
                                case "power":
                                    if (v.equals("generator")) type = WayType.POWER_GENERATOR;
                                    break;
                                case "tourism":
                                    switch (v) {
                                        case "camp_site":
                                            type = WayType.TOURISM_CAMP_SITE;
                                            break;
                                        case "theme_park":
                                            type = WayType.TOURISM_THEME_PARK;
                                            break;
                                    }
                                    break;
                            }
                            break;
                        case "relation":
                            type = WayType.UNKNOWN;
                            rel = new Relation();
                            break;
                        case "member":
                            ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
                            Way member = idToWay.get(ref);
                            if (member != null) rel.add(member);
                            break;
                    }
                    break;

                case END_ELEMENT:
                    switch (reader.getLocalName()) {
                        case "way":
                            if (type == WayType.COASTLINE) {
                                coast.add(way);
                            } else {
                                Polyline p = new Polyline(way);
                                model.getWays().get(type).add(p);
                                if (type.toString().startsWith("HIGHWAY")) {
                                    roadnames.put(p, roadname);
                                }
                            }
                            way = null;
                            break;
                        case "relation":
                            if (type == WayType.WATER) {
                                model.getWays().get(type).add(new MultiPolyline(rel));
                                way = null;
                            }
                            if (type == WayType.LANDUSE_FOREST) {
                                model.getWays().get(type).add(new MultiPolyline(rel));
                                way = null;
                            }
                            if (type == WayType.BUILDING) {
                                model.getWays().get(type).add(new MultiPolyline(rel));

                                way = null;

                            }
                            if (type == WayType.LANDUSE_SPORTS_CENTRE) {
                                model.getWays().get(type).add(new MultiPolyline(rel));
                                way = null;
                            }
                            if (type == WayType.NATURAL_BEACH) {
                                model.getWays().get(type).add(new MultiPolyline(rel));
                                way = null;
                            }
                            if (type == WayType.NATURAL_SAND) {
                                model.getWays().get(type).add(new MultiPolyline(rel));
                                way = null;
                            }
                            break;
                        case "node":
                            if (housenumber != null) {
                                addressList.add(new Address(street, housenumber, floor, side, postcode, city, currentNode));
                                city = null;
                                housenumber = null;
                                floor = null;
                                side = null;
                                street = null;
                                postcode = null;
                            }
                    }
                    break;
                case PROCESSING_INSTRUCTION:
                    break;
                case CHARACTERS:
                    break;
                case COMMENT:
                    break;
                case SPACE:
                    break;
                case START_DOCUMENT:
                    break;
                case END_DOCUMENT:
                    for (Way c : merge(coast)) {
                        model.getWays().get(WayType.COASTLINE).add(new Polyline(c));
                    }
                    break;
                case ENTITY_REFERENCE:
                    break;
                case ATTRIBUTE:
                    break;
                case DTD:
                    break;
                case CDATA:
                    break;
                case NAMESPACE:
                    break;
                case NOTATION_DECLARATION:
                    break;
                case ENTITY_DECLARATION:
                    break;
            }
        }

            model.setAddressList(addressList);
            model.makeStringAddressMap();
            model.makeStringNoHouseAddressMap();
            model.setRoadnamesMap(roadnames);

        model.setAddressList(addressList);
        model.makeStringAddressMap();
        model.makeStringNoHouseAddressMap();
        model.setRoadnamesMap(roadnames);

        for(Way w : roadWayList)
        {
            for(Node n : w)
            {
                n.setStreet(w.getStreet());
            }
        }

        model.setRoadWayList(roadWayList);
        model.initEdgeWeightedDigraph();
        }

    /**
     * Makes coast lines.
     * @param coast
     * @return
     */
    private Iterable<Way> merge(List<Way> coast) {
        Map<Node, Way> pieces = new HashMap<>();
        for (Way way : coast) {
            Way res = new Way(0);
            Way before = pieces.remove(way.getFirst());
            if (before != null) {
                pieces.remove(before.getFirst());
                for (int i = 0; i < before.size() - 1; i++) {
                    res.add(before.get(i));
                }
            }
            res.addAll(way);
            Way after = pieces.remove(way.getLast());
            if (after != null) {
                pieces.remove(after.getLast());
                for (int i = 1; i < after.size(); i++) {
                    res.add(after.get(i));
                }
            }
            pieces.put(res.getFirst(), res);
            pieces.put(res.getLast(), res);
        }
        if(coast.get(0).getFirst().getLat()!=coast.get(coast.size()-1).getLast().getLat() && coast.get(0).getFirst().getLon()!=coast.get(coast.size()-1).getLast().getLon()){


            Way mergeWay=new Way(1);
            mergeWay.add(new Node(1,coast.get(0).getFirst().getLon(),coast.get(0).getFirst().getLat()));

            mergeWay.add(new Node(1,coast.get(coast.size()-1).getLast().getLon(),coast.get(coast.size()-1).getLast().getLat()));

            pieces.put(new Node(1,2,2), mergeWay);
        }


        return pieces.values();
    }

        /**
         * Creates kd-tree
         */
        public void makeTrees()
        {
            for (WayType waytype : WayType.values()) {
                if (model.getWays().get(waytype).size() != 0&&waytype!=WayType.CARTOGRAPHIC_PARKING) {
                    model.getTrees().put(waytype, new TreeCustom());
                    if(waytype.toString().startsWith("HIGHWAY")){
                    model.getTrees().get(waytype).insert(model.getWays().get(waytype));
                    }
                    else{
                        model.getTrees().get(waytype).insert(model.getWays().get(waytype));
                    }

                    if (waytype == WayType.COASTLINE || waytype == WayType.POI_MARKER || waytype == WayType.POI_VISIT) {

                    } else {
                        model.getWays().remove(waytype);
                    }
                }
            }
        }
}

