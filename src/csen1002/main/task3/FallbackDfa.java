package csen1002.main.task3;

import java.util.*;

/**
 * Write your info here
 * 
 * @name Ahmed Elghobashy
 * @id 46-17914
 * @labNumber 16
 */

public class FallbackDfa {

	/**
	 * Constructs a Fallback DFA
	 * 
	 * @param fdfa A formatted string representation of the Fallback DFA. The string
	 *             representation follows the one in the task description
	 */
	DFA dfa;


	public FallbackDfa(String fdfa) {
		// TODO Auto-generated constructor stub
        // TODO Auto-generated method stub
        String[] sarr = fdfa.split("#");
        String[] states = sarr[0].split(";");
        String[] alpha  = sarr[1].split(";");
        String[] trans = sarr[2].split(";");
        String[] accept = sarr[4].split(";");
        String startState = sarr[3];
        DFA dfa = new DFA(startState);
        List<Integer> l = new ArrayList<>();
        for (String s :
                accept) {
            l.add(Integer.parseInt(s));
        }
        dfa.accept.addAll(l);
        l = new ArrayList<>();
        for (String s :
                states) {
            l.add(Integer.parseInt(s));
        }
        dfa.states.addAll(l);

        createTrans(dfa,trans);
        this.dfa=dfa;
//        dfa.alphabet.addAll(Arrays.asList(alpha));

    }

	/**
	 * @param input The string to simulate by the FDFA.
	 * 
	 * @return Returns a formatted string representation of the list of tokens. The
	 *         string representation follows the one in the task description
	 */
	public String run(String input) {
	    Stack<Object[]> stack = new Stack<>();
	    int currState =Integer.parseInt(dfa.start);
	    int i=0;
        while(i<input.length())
        {
            if(dfa.accept.contains(currState))
                stack.add(new Object[]{i,currState});
            char curr = input.charAt(i);
            int nextState = transition(currState,curr);
            currState=nextState;
            i++;
        }
        if(stack.isEmpty())
            return input+","+currState;
        if(dfa.accept.contains(currState))
        {
            return input+","+currState;
        }

        Object[] last = stack.pop();
        String ret = input.substring(0,(int)last[0]);

        return ret+","+last[1]+";"+run(input.substring((int)last[0]));
	}

    private int transition(int currState, char curr) {
        for (Object[] t:
             dfa.transitions) {
            if((Integer) t[0]==currState && (char)t[1]==curr)
                return (int) t[2];
        }

        return -1;
    }

    private void createTrans(DFA dfa, String[] trans) {
        for (String t :
                trans) {
            String[] sarr =  t.split(",");
            Object[] tadd = new Object[]{Integer.parseInt(sarr[0]),sarr[1].charAt(0),Integer.parseInt(sarr[2])};
            dfa.transitions.add(tadd);
        }
    }


    class DFA
    {
        TreeSet<Character> alphabet;
        List<Integer> states;
        List<Object[]> transitions;
        String start;
        TreeSet<Integer> accept;
        public DFA(String start)
        {
            states= new ArrayList<>();
            transitions=new ArrayList<>();
            accept= new TreeSet<Integer>();
            alphabet=new TreeSet<>();
            this.start=start;
        }


    }


}
