package OSM;

import All.Address;
import Draw.Drawable;
import Draw.WayType;
import Kd.TreeCustom;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * Reads a file
 */
public class Reader {
    /**
     * Loads obj file or parses file to parser
     * @param filename File to read
     * @param parser Parser to pass file to
     * @param model Current model
     * @throws IOException
     * @throws XMLStreamException
     * @throws ClassNotFoundException
     */
    public Reader(String filename, InputStream inputStream, Parser parser, iModel model) throws IOException, XMLStreamException, ClassNotFoundException {
        InputStream osmsource;
        if (filename.endsWith(".obj")) {
            try (ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(inputStream))) {
                model.setWays((Map<WayType, List<Drawable>>) input.readObject());

                model.setTrees((Map<WayType, TreeCustom>) input.readObject());

                model.setAddressList((ArrayList<Address>) input.readObject());
                model.makeStringAddressMap();
                model.makeStringNoHouseAddressMap();

                model.setRoadWayList((ArrayList<Way>) input.readObject());
                model.initEdgeWeightedDigraph();

                float lonFactor = input.readFloat();
                float minLat = input.readFloat();
                float minLon = input.readFloat();
                float maxLat = input.readFloat();
                float maxLon = input.readFloat();

                model.setLonfactor(lonFactor);
                model.setMinlat(minLat);
                model.setMinlon(minLon);
                model.setMaxlat(maxLat);
                model.setMaxlon(maxLon);
            }
        } else {
            if (filename.endsWith(".zip")) {
                ZipInputStream zip = new ZipInputStream(new BufferedInputStream(inputStream));
                zip.getNextEntry();
                osmsource = zip;
            } else {
                osmsource = new BufferedInputStream(inputStream);
            }
            parser.parse(osmsource);
            String[] filenameparts = filename.split("/");
            try (ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream( filenameparts[filenameparts.length-1] + ".obj" )))) {
                output.writeObject(model.getWays());

                output.writeObject(model.getTrees());
                output.writeObject(model.getAddressList());

                output.writeObject(model.getRoadWayList());

                output.writeFloat(model.getLonfactor());
                output.writeFloat(model.getMinlat());
                output.writeFloat(model.getMinlon());
                output.writeFloat(model.getMaxlat());
                output.writeFloat(model.getMaxlon());

            }
        }
    }
}
