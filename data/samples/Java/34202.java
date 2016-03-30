/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import pspdash.AbstractLanguageFilter;
import pspdash.StringUtils;
import java.util.StringTokenizer;

public class CobolFilter extends AbstractLanguageFilter {

    private static final String[] FILENAME_ENDINGS = { ".cob", ".cbl" };

    protected String[] getDefaultFilenameEndings() {
        return FILENAME_ENDINGS;
    }

    protected int doubleCheckFileContents(String contents, int match) {
        if (Character.isDigit(contents.charAt(0))) return match + 30; else return match;
    }

    /** Insert flags in a file to highlight the syntax of the language.
     */
    public void highlightSyntax(StringBuffer file) {
        int lineBeg = 0, lineEnd;
        while (lineBeg < file.length()) {
            lineEnd = StringUtils.indexOf(file, "\n", lineBeg);
            if (lineEnd == -1) lineEnd = file.length();
            while (lineBeg < lineEnd && Character.isDigit(file.charAt(lineBeg))) {
                file.deleteCharAt(lineBeg);
                lineEnd--;
            }
            if (lineBeg < lineEnd && "*/".indexOf(file.charAt(lineBeg)) != -1) {
                file.insert(lineEnd, COMMENT_END);
                file.insert(lineBeg, COMMENT_START);
                lineEnd += 2;
            }
            lineBeg = lineEnd + 1;
        }
    }

    private boolean countEnd = false, countExit = false, countDot = false;

    protected void setOptions(String options) {
        options = options.toUpperCase();
        countEnd = (options.indexOf("+END") != -1);
        countExit = (options.indexOf("+EXIT") != -1);
        countDot = (options.indexOf("+.") != -1);
    }

    public boolean isSignificant(String line) {
        line = line.trim();
        if (line.length() == 0) return false;
        if (line.equals(".")) return countDot;
        if (startsWithIgnoreCase(line, "END")) return countEnd;
        if (startsWithIgnoreCase(line, "EXIT")) return countExit;
        return true;
    }

    public String[][] getOptions() {
        return OPTIONS;
    }

    protected String[][] OPTIONS = { { "+END", "Count </I><TT>END-</TT> <I>clauses as lines of code." }, { "-END", ("Do not count </I><TT>END-</TT> <I>clauses as lines of " + "code <B>(default)</B>.") }, { "+EXIT", "Count </I><TT>EXIT</TT> <I>statements as lines of code." }, { "-EXIT", ("Do not count </I><TT>EXIT</TT> <I>statements as lines " + "of code <B>(default)</B>.") }, { "+.", "Count a period on a line by itself as a line of code." }, { "-.", ("Do not count a period on a line by itself as a line of " + "code <B>(default)</B>.") } };
}
