/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public abstract class Parameters {

    private boolean DEBUG = false;

    private String propertiesFilename;

    private String propertiesDescription;

    protected Properties properties = null;

    protected Parameters(String propertiesFilename, String propertiesDescription) {
        this.propertiesFilename = propertiesFilename;
        this.propertiesDescription = propertiesDescription;
    }

    protected abstract void setDefaults(Properties defaults);

    protected abstract void updatePropertiesFromSettings();

    protected abstract void updateSettingsFromProperties();

    protected void getParameters() {
        Properties defaults = new Properties();
        FileInputStream in = null;
        setDefaults(defaults);
        properties = new Properties(defaults);
        try {
            String folder = System.getProperty("user.home");
            String filesep = System.getProperty("file.separator");
            in = new FileInputStream(folder + filesep + propertiesFilename);
            properties.load(in);
        } catch (java.io.FileNotFoundException e) {
            in = null;
            ErrorMessages.error("Can't find properties file. " + "Using defaults.");
        } catch (java.io.IOException e) {
            ErrorMessages.error("Can't read properties file. " + "Using defaults.");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (java.io.IOException e) {
                }
                in = null;
            }
        }
        updateSettingsFromProperties();
    }

    protected void saveParameters() {
        updatePropertiesFromSettings();
        if (DEBUG) {
            System.out.println("Just set properties: " + propertiesDescription);
            System.out.println(toString());
        }
        FileOutputStream out = null;
        try {
            String folder = System.getProperty("user.home");
            String filesep = System.getProperty("file.separator");
            out = new FileOutputStream(folder + filesep + propertiesFilename);
            properties.store(out, propertiesDescription);
        } catch (java.io.IOException e) {
            ErrorMessages.error("Can't save properties. " + "Oh well, it's not a big deal.");
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (java.io.IOException e) {
                }
                out = null;
            }
        }
    }
}
