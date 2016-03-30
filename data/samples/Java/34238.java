/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;

/**
 * @author gunter.zeilinger@tiani.com
 * @version $Revision: 3997 $ $Date: 2006-05-22 07:41:19 -0400 (Mon, 22 May 2006) $
 * @since May 22, 2006
 */
public class ListIIORegistry {

    private static final IIORegistry theRegistry = IIORegistry.getDefaultInstance();

    /**
     * @param args
     */
    public static void main(String[] args) {
        printImageReader();
        printImageWriter();
        printServiceProviders();
    }

    private static void printImageReader() {
        String[] formats = ImageIO.getReaderFormatNames();
        for (int i = 0; i < formats.length; i++) {
            System.out.println("\nIMAGE READERS FOR FORMAT " + formats[i]);
            for (Iterator it = ImageIO.getImageReadersByFormatName(formats[i]); it.hasNext(); ) {
                System.out.println(it.next().getClass().getName());
            }
        }
    }

    private static void printImageWriter() {
        String[] formats = ImageIO.getWriterFormatNames();
        for (int i = 0; i < formats.length; i++) {
            System.out.println("\nIMAGE WRITERS FOR FORMAT " + formats[i]);
            for (Iterator it = ImageIO.getImageWritersByFormatName(formats[i]); it.hasNext(); ) {
                System.out.println(it.next().getClass().getName());
            }
        }
    }

    private static void printServiceProviders() {
        for (Iterator iter = theRegistry.getCategories(); iter.hasNext(); ) {
            Class category = (Class) iter.next();
            System.out.println("\nCATEGORY " + category.getName());
            for (Iterator it = theRegistry.getServiceProviders(category, true); it.hasNext(); ) {
                System.out.println(it.next().getClass().getName());
            }
        }
    }
}
