/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import referees.OnePlayerReferee;
import agents.AbstractSwarm;
import agents.SwarmHashMap;
import algorithms.QLearningSelector;
import algorithms.RandomSelector;
import algorithms.WatkinsSelector;
import nashQLearning.Chessboard;
import nashQLearning.ElementaryNashEnvironment1;
import nashQLearning.ElementaryNashEnvironment2;
import nashQLearning.NashElementaryCooperativeAgent;
import nashQLearning.positionFilter;

/**
 * @author Francesco De Comit�
 *
 * 29 mars 07
 */
public class testNash {

    public static void main(String argv[]) {
        Chessboard world = new Chessboard();
        AbstractSwarm couple = new SwarmHashMap(world);
        NashElementaryCooperativeAgent Agent1 = new NashElementaryCooperativeAgent(new ElementaryNashEnvironment1(), new WatkinsSelector(0.9), new positionFilter(0), false);
        NashElementaryCooperativeAgent Agent2 = new NashElementaryCooperativeAgent(new ElementaryNashEnvironment2(), new WatkinsSelector(0.9), new positionFilter(1), true);
        couple.add(Agent1);
        couple.add(Agent2);
        Agent1.addNeighbour(Agent2);
        Agent2.addNeighbour(Agent1);
        Agent1.buildInitialComposedState(world.defaultInitialState());
        Agent2.buildInitialComposedState(world.defaultInitialState());
        OnePlayerReferee arbitre = new OnePlayerReferee(couple);
        arbitre.setMaxIter(100);
        double totalReward = 0;
        for (int i = 0; i < 50000; i++) {
            int u = arbitre.episode(world.defaultInitialState());
            totalReward += u;
            System.out.println(i + " " + u + " " + totalReward / (1.0 + i));
        }
    }
}
