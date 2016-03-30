/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lang.*;

public class Parser {

    /**
	 * datastream for global access
	 */
    private BufferedReader log = null;

    private Language lang = null;

    private String playerName;

    private Hashtable<String, Actor> actors;

    /**
	 * constructor
	 * 
	 * @param name
	 */
    public Parser(String name) {
        this.playerName = name;
        this.actors = new Hashtable<String, Actor>();
    }

    /**
	 * reads the specified file or combatlog
	 * 
	 * @param fileName
	 * @throws IOException
	 */
    private void read(String fileName) throws IOException {
        debug("reading start");
        this.log = new BufferedReader(new FileReader(fileName));
        debug("reading end");
    }

    private void processAutoHit(Matcher m) {
        System.out.print("autoHit\t\t");
        System.out.print(m.group(2));
        System.out.print(m.group(3));
        System.out.print(m.group(4));
        System.out.print(m.group(5));
        if (m.group(15) != null) System.out.print(" glancing!");
        if ((m.group(6) != null)) {
            System.out.print(" " + m.group(7));
            System.out.print(" " + m.group(8));
        }
        if ((m.group(12) != null)) {
            System.out.print(" " + m.group(13));
            System.out.print(" " + m.group(14));
        }
        System.out.print("\n");
        Actor current, target;
        int blocked = 0, absorbed = 0, resisted = 0;
        boolean glancing = false;
        String school = "Physical";
        if (!actors.containsKey(m.group(3))) actors.put(m.group(3), new Actor(m.group(3)));
        if (!actors.containsKey(m.group(4))) actors.put(m.group(4), new Actor(m.group(4)));
        if ((m.group(6) != null)) {
            if (m.group(8).equals("blocked")) blocked = Integer.parseInt(m.group(7)); else if (m.group(8).equals("absorbed")) absorbed = Integer.parseInt(m.group(7)); else if (m.group(8).equals("resisted")) resisted = Integer.parseInt(m.group(7));
        }
        if ((m.group(12) != null)) {
            if (m.group(14).equals("blocked")) blocked = Integer.parseInt(m.group(13)); else if (m.group(14).equals("absorbed")) absorbed = Integer.parseInt(m.group(13)); else if (m.group(14).equals("resisted")) resisted = Integer.parseInt(m.group(13));
        }
        if (m.group(15) != null) glancing = true;
        current = actors.get(m.group(3));
        target = actors.get(m.group(4));
        Spell s = new Spell(m.group(2), current, target, "autohit", school, Integer.parseInt(m.group(5)), false, glancing, blocked, absorbed, resisted);
        current.addSpell(s);
    }

    private void processAutoCrit(Matcher m) {
        System.out.print("autoCrit\t");
        System.out.print(m.group(2));
        System.out.print(m.group(3));
        System.out.print(m.group(4));
        System.out.print(m.group(5));
        if ((m.group(6) != null)) {
            System.out.print(" " + m.group(7));
            System.out.print(" " + m.group(8));
        }
        if ((m.group(12) != null)) {
            System.out.print(" " + m.group(13));
            System.out.print(" " + m.group(14));
        }
        System.out.print("\n");
        Actor current, target;
        int blocked = 0, absorbed = 0, resisted = 0;
        boolean glancing = false;
        String school = "Physical";
        if (!actors.containsKey(m.group(3))) actors.put(m.group(3), new Actor(m.group(3)));
        if (!actors.containsKey(m.group(4))) actors.put(m.group(4), new Actor(m.group(4)));
        if ((m.group(6) != null)) {
            if (m.group(8).equals("blocked")) blocked = Integer.parseInt(m.group(7)); else if (m.group(8).equals("absorbed")) absorbed = Integer.parseInt(m.group(7)); else if (m.group(8).equals("resisted")) resisted = Integer.parseInt(m.group(7));
        }
        if ((m.group(12) != null)) {
            if (m.group(14).equals("blocked")) blocked = Integer.parseInt(m.group(13)); else if (m.group(14).equals("absorbed")) absorbed = Integer.parseInt(m.group(13)); else if (m.group(14).equals("resisted")) resisted = Integer.parseInt(m.group(13));
        }
        current = actors.get(m.group(3));
        target = actors.get(m.group(4));
        Spell s = new Spell(m.group(2), current, target, "autohit", school, Integer.parseInt(m.group(5)), true, glancing, blocked, absorbed, resisted);
        current.addSpell(s);
    }

    private void processNormalHit(Matcher m) {
        System.out.print("normalHit\t");
        System.out.print(m.group(2));
        System.out.print(m.group(3));
        System.out.print(m.group(4));
        System.out.print(m.group(5));
        System.out.print(m.group(6));
        if (m.group(7) != null) System.out.print(m.group(8));
        if (m.group(12) != null) {
            System.out.print(m.group(13));
            System.out.print(m.group(14));
        }
        System.out.print("\n");
        Actor current, target;
        int blocked = 0, absorbed = 0, resisted = 0;
        boolean glancing = false;
        String school = "Physical";
        if (!actors.containsKey(m.group(3))) actors.put(m.group(3), new Actor(m.group(3)));
        if (!actors.containsKey(m.group(5))) actors.put(m.group(5), new Actor(m.group(5)));
        if (m.group(7) != null) school = m.group(8);
        if ((m.group(12) != null)) {
            if (m.group(14).equals("blocked")) blocked = Integer.parseInt(m.group(13)); else if (m.group(14).equals("absorbed")) absorbed = Integer.parseInt(m.group(13)); else if (m.group(14).equals("resisted")) resisted = Integer.parseInt(m.group(13));
        }
        current = actors.get(m.group(3));
        target = actors.get(m.group(5));
        Spell s = new Spell(m.group(2), current, target, m.group(4), school, Integer.parseInt(m.group(6)), false, glancing, blocked, absorbed, resisted);
        current.addSpell(s);
    }

    private void processCriticalHit(Matcher m) {
        System.out.print("criticalHit\t");
        System.out.print(m.group(2));
        System.out.print(m.group(3));
        System.out.print(m.group(4));
        System.out.print(m.group(5));
        System.out.print(m.group(6));
        if (m.group(7) != null) System.out.print(m.group(8));
        if (m.group(12) != null) {
            System.out.print(m.group(13));
            System.out.print(m.group(14));
        }
        System.out.print("\n");
        Actor current, target;
        int blocked = 0, absorbed = 0, resisted = 0;
        boolean glancing = false;
        String school = "Physical";
        if (!actors.containsKey(m.group(3))) actors.put(m.group(3), new Actor(m.group(3)));
        if (!actors.containsKey(m.group(5))) actors.put(m.group(5), new Actor(m.group(5)));
        if (m.group(7) != null) school = m.group(8);
        if ((m.group(12) != null)) {
            if (m.group(14).equals("blocked")) blocked = Integer.parseInt(m.group(13)); else if (m.group(14).equals("absorbed")) absorbed = Integer.parseInt(m.group(13)); else if (m.group(14).equals("resisted")) resisted = Integer.parseInt(m.group(13));
        }
        current = actors.get(m.group(3));
        target = actors.get(m.group(5));
        Spell s = new Spell(m.group(2), current, target, m.group(4), school, Integer.parseInt(m.group(6)), true, glancing, blocked, absorbed, resisted);
        current.addSpell(s);
    }

    private void processDot(Matcher m) {
        System.out.print("Dot\t\t");
        System.out.print(m.group(2));
        System.out.print(m.group(6));
        System.out.print(m.group(7));
        System.out.print(m.group(3));
        System.out.print(m.group(4));
        if (m.group(5) != null) System.out.print(m.group(5));
        if (m.group(11) != null) {
            System.out.print(m.group(12));
            System.out.print(m.group(13));
        }
        System.out.print("\n");
        Actor current, target;
        int blocked = 0, absorbed = 0, resisted = 0;
        boolean glancing = false;
        String school = "Physical";
        if (!actors.containsKey(m.group(6))) actors.put(m.group(6), new Actor(m.group(6)));
        if (!actors.containsKey(m.group(3))) actors.put(m.group(3), new Actor(m.group(3)));
        if (m.group(5) != null) school = m.group(5);
        if ((m.group(11) != null)) {
            if (m.group(13).equals("blocked")) blocked = Integer.parseInt(m.group(12)); else if (m.group(13).equals("absorbed")) absorbed = Integer.parseInt(m.group(12)); else if (m.group(13).equals("resisted")) resisted = Integer.parseInt(m.group(12));
        }
        current = actors.get(m.group(6));
        target = actors.get(m.group(3));
        Spell s = new Spell(m.group(2), current, target, m.group(7), school, Integer.parseInt(m.group(4)), false, glancing, blocked, absorbed, resisted);
        current.addSpell(s);
    }

    private void processHeal(Matcher m) {
        System.out.print("Heal\t\t");
        System.out.print(m.group(2));
        System.out.print(m.group(3));
        System.out.print(m.group(4));
        System.out.print(m.group(5));
        System.out.print(m.group(6));
        System.out.print("\n");
        Actor current, target;
        int blocked = 0, absorbed = 0, resisted = 0;
        boolean glancing = false;
        String school = "Heal";
        String spell = m.group(4);
        if (spell.contains("critically")) spell = spell.substring(0, spell.length() - 11);
        if (!actors.containsKey(m.group(3))) actors.put(m.group(3), new Actor(m.group(3)));
        if (!actors.containsKey(m.group(5))) actors.put(m.group(5), new Actor(m.group(5)));
        current = actors.get(m.group(3));
        target = actors.get(m.group(5));
        Spell s;
        if (spell.equals(m.group(4))) s = new Spell(m.group(2), current, target, spell, school, Integer.parseInt(m.group(6)), false, glancing, blocked, absorbed, resisted); else s = new Spell(m.group(2), current, target, spell, school, Integer.parseInt(m.group(6)), true, glancing, blocked, absorbed, resisted);
        current.addSpell(s);
    }

    private void processDeath(Matcher m) {
        System.out.print("Death\t\t");
        System.out.print(m.group(2));
        System.out.print(m.group(3));
        System.out.print("\n");
        Actor current;
        if (!actors.containsKey(m.group(3))) actors.put(m.group(3), new Actor(m.group(3)));
        current = actors.get(m.group(3));
        current.addDeath(m.group(2));
    }

    private void processCast(Matcher m) {
        System.out.print("Cast\t\t");
        System.out.print(m.group(2));
        System.out.print(m.group(3));
        System.out.print(m.group(4));
        System.out.print("\n");
        Actor current, target = null;
        int blocked = 0, absorbed = 0, resisted = 0;
        boolean glancing = false;
        String school = "Physical", strTarget = null;
        String spell = m.group(4);
        Pattern pa = Pattern.compile("^(.+) on (.+)");
        Matcher ma = pa.matcher(m.group(4));
        if (ma.matches()) {
            strTarget = ma.group(2);
            spell = ma.group(1);
        }
        if (!actors.containsKey(m.group(3))) actors.put(m.group(3), new Actor(m.group(3)));
        if (strTarget != null && !actors.containsKey(strTarget)) actors.put(strTarget, new Actor(strTarget));
        current = actors.get(m.group(3));
        if (strTarget != null) target = actors.get(strTarget);
        Spell s = new Spell(m.group(2), current, target, spell, school, 0, false, glancing, blocked, absorbed, resisted);
        current.addSpell(s);
    }

    private void processAfflicted(Matcher m) {
        System.out.print("Debuff\t\t");
        System.out.print(m.group(2));
        System.out.print(m.group(3));
        System.out.print(m.group(4));
        System.out.print("\n");
        Actor current;
        if (!actors.containsKey(m.group(3))) actors.put(m.group(3), new Actor(m.group(3)));
        current = actors.get(m.group(3));
        if (!current.getBuffs().containsKey(m.group(4))) {
            Buff b = new Buff(m.group(2), m.group(4), true);
            current.addBuff(m.group(4), b);
        }
        Buff b = current.getBuffs().get(m.group(4));
        b.gain();
    }

    /** TODO */
    private void processGain(Matcher m) {
        for (int i = 0; i <= m.groupCount(); i++) System.out.println(i + " " + m.group(i));
    }

    private void processRemove(Matcher m) {
        System.out.print("Remove\t\t");
        System.out.print(m.group(2));
        System.out.print(m.group(3));
        System.out.print(m.group(4));
        System.out.print(m.group(5));
        System.out.print(m.group(6));
        System.out.print("\n");
        Actor current;
        if (!actors.containsKey(m.group(3))) actors.put(m.group(3), new Actor(m.group(3)));
        current = actors.get(m.group(3));
        if (!current.getBuffs().containsKey(m.group(4))) {
            Buff b = new Buff(m.group(2), m.group(4), true);
            current.addBuff(m.group(4), b);
        }
        Buff b = current.getBuffs().get(m.group(4));
        b.dispell();
    }

    private void parse() throws IOException {
        debug("start parsing");
        Pattern autoHit = Pattern.compile(lang.getAutoHit());
        Pattern autoCrit = Pattern.compile(lang.getAutoCrit());
        Pattern normalHit = Pattern.compile(lang.getNormalHit());
        Pattern criticalHit = Pattern.compile(lang.getCriticalHit());
        Pattern dot = Pattern.compile(lang.getDot());
        Pattern heal = Pattern.compile(lang.getHeal());
        Pattern dies = Pattern.compile(lang.getDies());
        Pattern beginCast = Pattern.compile(lang.getBeginCast());
        Pattern cast = Pattern.compile(lang.getCast());
        Pattern remove = Pattern.compile(lang.getRemove());
        Pattern beginPerform = Pattern.compile(lang.getBeginPerform());
        Pattern perform = Pattern.compile(lang.getPerform());
        Pattern extraHit = Pattern.compile(lang.getExtraHit());
        Pattern gain = Pattern.compile(lang.getGain());
        Pattern afflicted = Pattern.compile(lang.getAfflicted());
        Pattern fades = Pattern.compile(lang.getFades());
        Pattern reflect = Pattern.compile(lang.getReflect());
        Pattern miss = Pattern.compile(lang.getMiss());
        Pattern spellMiss = Pattern.compile(lang.getSpellMiss());
        Pattern dodgeParry = Pattern.compile(lang.getDodgeParry());
        Pattern spellDodgeParry = Pattern.compile(lang.getSpellDodgeParry());
        Pattern resist = Pattern.compile(lang.getResist());
        for (int i = 0; i < 2000; i++) {
            String line = log.readLine();
            line = substituteUser(line);
            Matcher mAutoHit = autoHit.matcher(line);
            Matcher mAutoCrit = autoCrit.matcher(line);
            Matcher mNormalHit = normalHit.matcher(line);
            Matcher mCriticalHit = criticalHit.matcher(line);
            Matcher mDot = dot.matcher(line);
            Matcher mHeal = heal.matcher(line);
            Matcher mDies = dies.matcher(line);
            Matcher mBeginCast = beginCast.matcher(line);
            Matcher mCast = cast.matcher(line);
            Matcher mAfflicted = afflicted.matcher(line);
            Matcher mGain = gain.matcher(line);
            Matcher mRemove = remove.matcher(line);
            Matcher mBeginPerform = beginPerform.matcher(line);
            Matcher mPerform = perform.matcher(line);
            Matcher mExtraHit = extraHit.matcher(line);
            Matcher mFades = fades.matcher(line);
            Matcher mReflect = reflect.matcher(line);
            Matcher mMiss = miss.matcher(line);
            Matcher mSpellMiss = spellMiss.matcher(line);
            Matcher mDodgeParry = dodgeParry.matcher(line);
            Matcher mSpellDodgeParry = spellDodgeParry.matcher(line);
            Matcher mResist = resist.matcher(line);
            if (mAutoHit.matches()) processAutoHit(mAutoHit); else if (mAutoCrit.matches()) processAutoCrit(mAutoCrit); else if (mNormalHit.matches()) processNormalHit(mNormalHit); else if (mCriticalHit.matches()) processCriticalHit(mCriticalHit); else if (mDot.matches()) processDot(mDot); else if (mHeal.matches()) processHeal(mHeal); else if (mDies.matches()) processDeath(mDies); else if (mCast.matches()) processCast(mCast); else if (mAfflicted.matches()) processAfflicted(mAfflicted); else if (mGain.matches()) processGain(mGain); else if (mRemove.matches()) processRemove(mRemove);
        }
        debug("end parsing");
    }

    /**
	 * detects whether german or english logs are being parsed
	 * 
	 * @throws IOException
	 */
    private void detectLanguage() throws IOException {
        debug("language detection start");
        if (this.log.markSupported()) this.log.mark(1);
        while (true) {
            String line = this.log.readLine();
            if (line.toLowerCase().contains("you") || line.contains("gain") || line.toLowerCase().contains("your")) {
                debug("english");
                this.lang = new Lang_enGB();
                break;
            } else if (line.toLowerCase().contains("ihr") || line.contains("erhaltet") || line.toLowerCase().contains("euer")) {
                debug("german");
                this.lang = new Lang_deDE();
                break;
            }
        }
        this.log.reset();
        debug("language detection end");
    }

    private String substituteUser(String line) {
        line = line.replace("Your", playerName + " 's");
        line = line.replace("your", playerName + " 's");
        line = line.replace("you are", playerName + " is");
        line = line.replace("you hit", playerName + " hits");
        line = line.replace("you crit", playerName + " crits");
        line = line.replace("you gain", playerName + " gains");
        line = line.replace("you gain", playerName + " gains");
        line = line.replace("you", playerName);
        return line;
    }

    /**
	 * output for simple debugging
	 * 
	 * @param s
	 */
    private void debug(String s) {
        System.out.println("[DEBUG] " + s);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length != 1) System.exit(1);
        Parser p = new Parser(args[0]);
        try {
            p.read("./CombatLog01.txt");
            p.detectLanguage();
            p.debug("" + p.lang.getAfflicted());
            p.parse();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
