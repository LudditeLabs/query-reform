/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageReader;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.dict.Tags;
import org.dcm4che.image.ColorModelFactory;
import org.dcm4che.image.ColorModelParam;
import org.dcm4che.imageio.plugins.DcmMetadata;

/**
 *
 * @author  gunter.zeilinger@tiani.com
 * @version 1.0.0
 */
class ImageBox extends JPanel {

    /** Creates a new instance of ImagePanel */
    static final ColorModelFactory cmFactory = ColorModelFactory.getInstance();

    static final Color myGray = new Color(204, 204, 204);

    private final ImageReader reader;

    private final Dataset dataset;

    private final int origWidth;

    private final int origHeight;

    private BufferedImage origImage;

    private BufferedImage image = null;

    ColorModelParam cmParam = null;

    JSlider zoomSlider = null;

    JSlider widthSlider = null;

    JSlider centerSlider = null;

    JSlider frameSlider = null;

    View view = new View();

    public ImageBox(ImageReader reader) throws IOException {
        super(new BorderLayout());
        this.reader = reader;
        this.dataset = ((DcmMetadata) reader.getStreamMetadata()).getDataset();
        this.image = this.origImage = reader.read(0);
        origWidth = origImage.getWidth();
        origHeight = origImage.getHeight();
        view.setPreferredSize(new Dimension(origWidth, origHeight));
        zoomSlider = new JSlider(SwingConstants.VERTICAL, 0, 40, 10);
        zoomSlider.setMinorTickSpacing(5);
        zoomSlider.setMajorTickSpacing(10);
        zoomSlider.setPaintTicks(true);
        zoomSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                zoomChanged();
            }
        });
        add(zoomSlider, BorderLayout.EAST);
        String pmi = dataset.getString(Tags.PhotometricInterpretation, null);
        if ("MONOCHROME1".equals(pmi) || "MONOCHROME2".equals(pmi)) {
            cmParam = cmFactory.makeParam(dataset);
            int bits = dataset.getInt(Tags.BitsStored, 8);
            int size = 1 << bits;
            int signed = dataset.getInt(Tags.PixelRepresentation, 0);
            int min = dataset.getInt(Tags.SmallestImagePixelValue, signed == 0 ? 0 : -(size >> 1));
            int max = dataset.getInt(Tags.LargestImagePixelValue, signed == 0 ? size - 1 : (size >> 1) - 1);
            int c = (int) cmParam.toMeasureValue((min + max) >> 1);
            int cMin = (int) cmParam.toMeasureValue(min);
            int cMax = (int) cmParam.toMeasureValue(max - 1);
            int wMax = cMax - cMin;
            int w = wMax;
            int nWindow = cmParam.getNumberOfWindows();
            if (nWindow > 0) {
                c = (int) cmParam.getWindowCenter(0);
                w = (int) cmParam.getWindowWidth(0);
            }
            widthSlider = new JSlider(SwingConstants.HORIZONTAL, 0, Math.max(w, wMax), w);
            widthSlider.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    windowChanged();
                }
            });
            add(widthSlider, BorderLayout.NORTH);
            centerSlider = new JSlider(SwingConstants.VERTICAL, Math.min(c << 1, cMin), Math.max(c << 1, cMax), c);
            centerSlider.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    windowChanged();
                }
            });
            add(centerSlider, BorderLayout.WEST);
        }
        int nframes = reader.getNumImages(true);
        if (nframes > 1) {
            frameSlider = new JSlider(SwingConstants.HORIZONTAL, 0, nframes - 1, 0);
            frameSlider.setMajorTickSpacing(1);
            frameSlider.setPaintTicks(true);
            frameSlider.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    frameChanged();
                }
            });
            add(frameSlider, BorderLayout.SOUTH);
        }
        add(new JScrollPane(view), BorderLayout.CENTER);
    }

    public ImageBox(File f) {
        super(new BorderLayout());
        reader = null;
        BufferedInputStream fis = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(f));
        } catch (Exception e) {
            e.printStackTrace();
        }
        DcmObjectFactory dcmof = DcmObjectFactory.getInstance();
        this.dataset = dcmof.newDataset();
        try {
            dataset.readFile(fis, null, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.image = this.origImage = this.dataset.toBufferedImage();
        int width = dataset.getInt(Tags.Columns, -1);
        int height = dataset.getInt(Tags.Rows, -1);
        int bitsAllocd = dataset.getInt(Tags.BitsAllocated, -1);
        int bitsStored = dataset.getInt(Tags.BitsStored, -1);
        int highBit = dataset.getInt(Tags.HighBit, -1);
        int pixelRep = dataset.getInt(Tags.PixelRepresentation, -1);
        String pmi = dataset.getString(Tags.PhotometricInterpretation, null);
        int spp = dataset.getInt(Tags.SamplesPerPixel, -1);
        int planarConf = dataset.getInt(Tags.PlanarConfiguration, -1);
        System.out.println("width, height: " + width + "," + height);
        System.out.println("ba=" + bitsAllocd + ", bs=" + bitsStored + ", hb=" + highBit);
        System.out.println("signed: " + (pixelRep == 1));
        System.out.println("spp: " + spp);
        System.out.println("pmi: " + pmi);
        System.out.println("planar conf: " + planarConf);
        origWidth = origImage.getWidth();
        origHeight = origImage.getHeight();
        view.setPreferredSize(new Dimension(origWidth, origHeight));
        zoomSlider = new JSlider(SwingConstants.VERTICAL, 0, 40, 10);
        zoomSlider.setMinorTickSpacing(5);
        zoomSlider.setMajorTickSpacing(10);
        zoomSlider.setPaintTicks(true);
        zoomSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                zoomChanged();
            }
        });
        add(zoomSlider, BorderLayout.EAST);
        if ("MONOCHROME1".equals(pmi) || "MONOCHROME2".equals(pmi)) {
            cmParam = cmFactory.makeParam(dataset);
            int bits = dataset.getInt(Tags.BitsStored, 8);
            int size = 1 << bits;
            int signed = dataset.getInt(Tags.PixelRepresentation, 0);
            int min = dataset.getInt(Tags.SmallestImagePixelValue, signed == 0 ? 0 : -(size >> 1));
            int max = dataset.getInt(Tags.LargestImagePixelValue, signed == 0 ? size - 1 : (size >> 1) - 1);
            int c = (int) cmParam.toMeasureValue((min + max) >> 1);
            int cMin = (int) cmParam.toMeasureValue(min);
            int cMax = (int) cmParam.toMeasureValue(max - 1);
            int wMax = cMax - cMin;
            int w = wMax;
            int nWindow = cmParam.getNumberOfWindows();
            if (nWindow > 0) {
                c = (int) cmParam.getWindowCenter(0);
                w = (int) cmParam.getWindowWidth(0);
            }
            widthSlider = new JSlider(SwingConstants.HORIZONTAL, 0, Math.max(w, wMax), w);
            widthSlider.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    windowChanged();
                }
            });
            add(widthSlider, BorderLayout.NORTH);
            centerSlider = new JSlider(SwingConstants.VERTICAL, Math.min(c << 1, cMin), Math.max(c << 1, cMax), c);
            centerSlider.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    windowChanged();
                }
            });
            add(centerSlider, BorderLayout.WEST);
        }
        int nframes = 0;
        if (nframes > 1) {
            frameSlider = new JSlider(SwingConstants.HORIZONTAL, 0, nframes - 1, 0);
            frameSlider.setMajorTickSpacing(1);
            frameSlider.setPaintTicks(true);
            frameSlider.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    frameChanged();
                }
            });
            add(frameSlider, BorderLayout.SOUTH);
        }
        add(new JScrollPane(view), BorderLayout.CENTER);
    }

    public void zoomChanged() {
        double scale = Math.max(1, zoomSlider.getValue()) / 10.;
        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(scale, scale), AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        this.image = op.filter(origImage, op.createCompatibleDestImage(origImage, image.getColorModel()));
        view.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        view.revalidate();
        view.repaint();
    }

    public void windowChanged() {
        this.cmParam = cmParam.update(centerSlider.getValue(), widthSlider.getValue(), cmParam.isInverse());
        ColorModel cm = cmFactory.getColorModel(cmParam);
        this.image = new BufferedImage(cm, image.getRaster(), false, null);
        view.repaint();
    }

    public void frameChanged() {
        try {
            this.origImage = reader.read(frameSlider.getValue());
            zoomChanged();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    class View extends JPanel {

        public void paint(Graphics g) {
            if (getWidth() > image.getWidth() || getHeight() > image.getHeight()) {
                g.setColor(myGray);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            g.drawImage(image, 0, 0, null);
        }
    }
}
