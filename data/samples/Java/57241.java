/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import junit.framework.*;
import net.llando.*;
import net.llando.util.*;
import org.json.simple.*;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

public class TestJSONStringBuffer extends TestCase {

    static String[] singleObject = { "{ \"str\": \n", "\"{}[]:\\\"blah\"}" };

    static String[] twoObjects = { "{ \"str\": \n", "\"{}[]:\"}[\"{}[]\",\n", "2,3,4 ]" };

    static String[] objectInObject = { "{ \"obj1\" : { \n", "\"obj2\" : {\n", " \"obj3\" : 42 }}}" };

    public void testObjectInObject() {
        try {
            JSONStringBuffer buf = new JSONStringBuffer();
            buf.append(objectInObject[0]);
            assertEquals(false, buf.stringsAvailable());
            buf.append(objectInObject[1]);
            assertEquals(false, buf.stringsAvailable());
            buf.append(objectInObject[2]);
            assertEquals(true, buf.stringsAvailable());
            ArrayList<String> strings = buf.retrieveAvailableStrings();
            assertEquals(1, strings.size());
            Object o = new Decoder().decode(strings.get(0));
            assertNotNull(o);
            assertEquals(JSONObject.class, o.getClass());
            JSONObject obj1 = (JSONObject) ((JSONObject) o).get("obj1");
            assertEquals(JSONObject.class, obj1.getClass());
            JSONObject obj2 = (JSONObject) obj1.get("obj2");
            assertEquals(JSONObject.class, obj2.getClass());
            Long obj3 = (Long) obj2.get("obj3");
            assertEquals(Long.class, obj3.getClass());
            assertEquals(new Long(42), obj3);
        } catch (Exception e) {
            System.err.println(e);
            fail();
        }
    }

    public void testTwoObjects() {
        JSONStringBuffer buf = new JSONStringBuffer();
        try {
            buf.append(twoObjects[0]);
            assertEquals(false, buf.stringsAvailable());
            buf.append(twoObjects[1]);
            assertEquals(true, buf.stringsAvailable());
            ArrayList<String> strings = buf.retrieveAvailableStrings();
            assertEquals(1, strings.size());
            Object o = new Decoder().decode(strings.get(0));
            assertEquals(JSONObject.class, o.getClass());
            assertEquals("{}[]:", ((JSONObject) o).get("str"));
            assertEquals(false, buf.stringsAvailable());
            buf.append(twoObjects[2]);
            assertEquals(true, buf.stringsAvailable());
            strings = buf.retrieveAvailableStrings();
            assertEquals(1, strings.size());
            o = new Decoder().decode(strings.get(0));
            assertEquals(JSONArray.class, o.getClass());
            assertEquals("{}[]", ((JSONArray) o).get(0));
            assertEquals(new Long(2), ((JSONArray) o).get(1));
            assertEquals(new Long(3), ((JSONArray) o).get(2));
        } catch (Exception e) {
            System.err.println(e);
            fail();
        }
    }

    public void testSingleObject() {
        JSONStringBuffer buf = new JSONStringBuffer();
        try {
            buf.append(singleObject[0]);
            assertEquals(false, buf.stringsAvailable());
            buf.append(singleObject[1]);
            assertEquals(true, buf.stringsAvailable());
            ArrayList<String> strings = buf.retrieveAvailableStrings();
            assertEquals(1, strings.size());
            Object o = new Decoder().decode(strings.get(0));
            assertNotNull(o);
            assertEquals(JSONObject.class, o.getClass());
            assertEquals("{}[]:\"blah", ((JSONObject) o).get("str"));
        } catch (Exception e) {
            System.err.println(e);
            fail();
        }
    }
}
