/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import objects.IMapGenerator;
import objects.Planet;
import objects.Race;
import objects.geom.MapGeometry;
import objects.production.AbstractProduction;
import objects.production.IProduction;
import util.SuperProperties;
import util.Utils;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: serhiy
 * Created on Oct 31, 2006, 4:06:21 AM
 */
public class MapGen {

    private static final Logger logger = Logger.getLogger("ogs.maps");

    public static void main(String[] args) {
        Properties props = new SuperProperties();
        try {
            props.load(System.in);
        } catch (IOException err) {
            logger.log(Level.SEVERE, "Map generator", err);
            System.exit(1);
            return;
        }
        MapGeometry geometry = null;
        try {
            URLClassLoader urlLoader = new URLClassLoader(new URL[0]);
            geometry = (MapGeometry) urlLoader.loadClass(props.getProperty("Galaxy.MapGeometry")).newInstance();
            geometry.init(props);
            geometry.setSize(Double.parseDouble(props.getProperty("Galaxy.Size", "1")));
        } catch (Exception err) {
            logger.log(Level.SEVERE, "Galaxy constructor, map geometry", err);
            System.exit(1);
        }
        HashMap<String, AbstractProduction> productions = new HashMap<String, AbstractProduction>();
        try {
            URLClassLoader urlLoader = new URLClassLoader(new URL[0]);
            String[] availableProduction = Utils.split(props.getProperty("Galaxy.ProductionTypes"));
            for (int i = 0; i < availableProduction.length; ++i) {
                String key = availableProduction[i];
                AbstractProduction prod = (AbstractProduction) urlLoader.loadClass(props.getProperty("Galaxy.ProductionType." + key)).newInstance();
                prod.setCode(i);
                productions.put(key, prod);
            }
        } catch (Exception err) {
            logger.log(Level.SEVERE, "Galaxy constructor, production types", err);
            System.exit(1);
        }
        IMapGenerator mapGenerator;
        try {
            URLClassLoader urlLoader = new URLClassLoader(new URL[0]);
            mapGenerator = (IMapGenerator) urlLoader.loadClass(props.getProperty("Galaxy.MapGenerator")).newInstance();
            mapGenerator.extractData(props, geometry);
        } catch (Exception err) {
            logger.log(Level.SEVERE, "Map generator", err);
            System.exit(1);
            return;
        }
        int racesCount = Integer.parseInt(props.getProperty("Galaxy.Races"));
        Race[] races = new Race[racesCount];
        Map<Race, Integer> raceCodes = new HashMap<Race, Integer>();
        for (int i = 0; i < races.length; ++i) {
            Race race = new Race(null, "Player_" + (i + 1), null);
            races[i] = race;
            raceCodes.put(race, i);
        }
        Planet[] planets = mapGenerator.generate(null, races);
        save(System.out, planets, raceCodes);
    }

    private static void save(OutputStream out, Planet[] planets, Map<Race, Integer> raceCodes) {
        PrintWriter writer = new PrintWriter(out);
        for (Planet planet : planets) {
            IProduction production = planet.getProduction();
            String productionName = production == null ? "-" : production.getName();
            println(writer, planet.getPosition().getX(), planet.getPosition().getY(), planet.getResources(), planet.getSize(), planet.getName(), planet.getPopulation(), planet.getIndustry(), planet.getFullEffort() - planet.getEffort(), productionName, productionName, planet.getNumber(), planet.getOwner() == null ? -1 : raceCodes.get(planet.getOwner()));
        }
        writer.flush();
    }

    static void println(PrintWriter writer, Object... args) {
        for (Object arg : args) {
            writer.print(arg == null ? "null" : arg.toString());
            writer.print(' ');
        }
        writer.println();
    }
}
