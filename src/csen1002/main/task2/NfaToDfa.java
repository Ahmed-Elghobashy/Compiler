package csen1002.main.task2;

import java.util.*;

/**
 * Write your info here
 * 
 * @name ahmed samir elsayed elghobashy
 * @id 46-17914
 * @labNumber 16
 */

public class NfaToDfa {

	/**
	 * Constructs a csen1002.main.task2.DFA corresponding to an csen1002.main.task2.NFA
	 * 
	 * @param input A formatted string representation of the csen1002.main.task2.NFA for which an
	 *              equivalent csen1002.main.task2.DFA is to be constructed. The string representation
	 *              follows the one in the task description
	 */
	DFA dfa;

//    public static void main(String[] args) {
//        NfaToDfa n = new NfaToDfa("0;1;2;3#a;b#0,a,0;0,b,0;0,b,1;1,a,2;1,e,2;2,b,3;3,a,3;3,b,3#0#3");
//        System.out.println(n.toString());
//
//    }
	public NfaToDfa(String input) {
		// TODO Auto-generated constructor stub
        NFA nfa = constructNFA(input);
        dfa = convetToDFA(nfa);
	}

    private DFA convetToDFA(NFA nfa) {
        Hashtable<Integer,TreeSet<Integer>> epsilonClosure = new Hashtable<Integer, TreeSet<Integer>>();
        DFA ret = new DFA(nfa.start);
        ret.alphabet.addAll(nfa.alpha);
        createEps(nfa,epsilonClosure);
        createDfa(ret,nfa,epsilonClosure);
        createAcceptStates(ret,nfa);
        return ret;
    }

    private void createAcceptStates(DFA ret, NFA nfa) {
        List<Integer> nAcceptS = nfa.accept;
        for (String state :
                ret.states) {
            String[] s = state.split("/");
            for (String str :
                s) {
                int temp = Integer.parseInt(str);
                if(nAcceptS.contains(temp))
                    ret.accept.add(state);
            }
        }
    }

    private void createDfa(DFA ret, NFA nfa, Hashtable<Integer, TreeSet<Integer>> epsilonClosure) {
	    ret.start=convertString(epsilonClosure.get(nfa.start));
	    Queue<String> notFinishedStates = new LinkedList<>();
//        for (Integer i:
//             nfa.states) {
//            String str = convertString(epsilonClosure.get(i));
            notFinishedStates.add(ret.start);
            ret.states.add(ret.start);
        //}
	    while(!notFinishedStates.isEmpty())
        {
            String currState = notFinishedStates.poll();

            for (char c :
                    ret.alphabet) {
                if (currState.equals("-1"))
                {
                    ret.transitions.add(new Object[]{currState,c,currState});
                    continue;
                }
                String stateToTransition = findTransition(nfa,currState,c,epsilonClosure);
                if(!ret.states.contains(stateToTransition))
                {
                    ret.states.add(stateToTransition);
                    notFinishedStates.add(stateToTransition);
                }
                ret.transitions.add(new Object[]{currState,c,stateToTransition});
            }
        }
    }

    private String findTransition(NFA nfa, String currState, char c, Hashtable<Integer, TreeSet<Integer>> epsilonClosure) {
	    TreeSet<Integer> ret=new TreeSet<>();
	    String[] states = currState.split("/");
        List<Object[]> transitions = nfa.transitions;
        for (String state :
                states) {
            int sNum = Integer.parseInt(state);
            for (Object[] transiton :
                    transitions) {
                if ((int) transiton[0] == sNum && (char) transiton[1] == c)
                    ret.addAll(epsilonClosure.get((int)transiton[2]));
            }
        }

        return ret.isEmpty()? "-1":convertString(ret);
    }

    private String convertString(TreeSet<Integer> integers) {
	    String ret="";
        for (int s :
                integers) {
            ret=ret+s+"/";
        }
        return ret.substring(0,ret.length()-1);
    }

    private void createEps(NFA nfa, Hashtable<Integer, TreeSet<Integer>> epsilonClosure) {
	    boolean flag=true;
        TreeSet<Integer> states = nfa.states;
        List<Object[]> transitions = nfa.transitions;
        for (int state :
                states) {
            epsilonClosure.putIfAbsent(state,new TreeSet<>());
            epsilonClosure.get(state).add(state);
            for (Object[] t :
                    transitions) {
                if((int)t[0]==state && (char)t[1]=='e') epsilonClosure.get(state).add((int)t[2]);
            }
        }

	    while(flag)
        {
            flag=false;
            for (int state :
                    states) {
                TreeSet<Integer> currStateEpsColosure = epsilonClosure.get(state);
                int size =  currStateEpsColosure.size();
                List<Integer> tmp = new ArrayList<>(currStateEpsColosure);
                for (int s :
                        tmp) {
                    currStateEpsColosure.addAll(epsilonClosure.get(s));
                }
                if(currStateEpsColosure.size()!=size)
                    flag=true;
            }
        }
    }

    private NFA constructNFA(String input) {
	    String[] sarr = input.split("#");
	    String[] states = sarr[0].split(";");
	    String[] alpha  = sarr[1].split(";");
	    String[] trans = sarr[2].split(";");
	    String[] accept = sarr[4].split(";");
	    Integer startState = Integer.parseInt(sarr[3]);
	    NFA nfa = new NFA();
	    createStates(nfa,states);
        createAlpha(nfa,alpha);
        createTrans(nfa,trans);
        createAccept(nfa,accept);
        nfa.start=startState;
	    return nfa;
    }

    private void createAccept(NFA nfa, String[] accept) {
        for (String acc :
                accept) {
            nfa.accept.add(Integer.parseInt(acc));
        }
    }

    private void createTrans(NFA nfa, String[] trans) {
        for (String t :
                trans) {
            String[] sarr =  t.split(",");
            Object[] tadd = new Object[]{Integer.parseInt(sarr[0]),sarr[1].charAt(0),Integer.parseInt(sarr[2])};
            nfa.transitions.add(tadd);
        }
    }

    private void createAlpha(NFA nfa, String[] alpha) {
        for (String ch :
                alpha) {
            nfa.alpha.add(ch.charAt(0));
        }
    }

    private void createStates(NFA nfa, String[] states) {
        for (String state :
                states) {
            nfa.states.add(Integer.parseInt(state));
        }
    }

    /**
	 * @return Returns a formatted string representation of the csen1002.main.task2.DFA. The string
	 *         representation follows the one in the task description
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub

		return dfa.toString();
	}



}
class NFA {
    TreeSet<Integer> states;
    List<Object[]> transitions;
    TreeSet<Character> alpha;
    int start;
    List<Integer> accept;

    public NFA(String input) {

    }

    public NFA() {
        states = new TreeSet();
        transitions = new ArrayList<>();
        accept=new ArrayList<>();
        alpha= new TreeSet<>();

    }
}

class DFA
{
    TreeSet<Character> alphabet;
    List<String> states;
    List<Object[]> transitions;
    String start;
    TreeSet<String> accept;
    public DFA(int start)
    {
        states= new ArrayList<>();
        transitions=new ArrayList<>();
        accept= new TreeSet<String>();
        alphabet=new TreeSet<>();
    }

    public String toString()
    {

        Comparator c = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                String s1 =(String) o1;
                String s2=(String) o2;
                String[] sarr1 = s1.split("/");
                String[] sarr2 = s2.split("/");
                int i=0;
                while(i<sarr1.length && i<sarr2.length)
                {
                    int i1 = Integer.parseInt(sarr1[i]);
                    int i2 = Integer.parseInt(sarr2[i]);
                    if(i1==i2)
                    {
                        i++;
                        continue;
                    }
                    return Integer.compare(i1,i2);
                }
              return Integer.compare(sarr1.length,sarr2.length);
            }
        };


        Comparator c2 = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Object[] t1 = (Object[]) o1;
                Object[] t2 = (Object[]) o2;
                if(c.compare(t1[0],t2[0])!=0)
                    return c.compare(t1[0],t2[0]);
                if((char)t1[1] !=(char) t2[1])
                    return Character.compare((char)t1[1],(char) t2[1]);
                return c.compare(t1[2],t2[2]);
            }
        };
        String ret = "";
        Collections.sort(states,c);
        for (String state :
                states) {
            ret=ret+state+";";
        }
        ret = ret.substring(0, ret.length()-1)+"#";
        for (char ch:
             alphabet) {
            ret=ret+ch+";";
        }
        ret = ret.substring(0, ret.length()-1)+"#";
        Collections.sort(transitions,c2);
        for (Object[] t :
                transitions) {
            ret+=t[0]+","+t[1]+","+t[2]+";";
        }
        ret = ret.substring(0, ret.length()-1)+"#";
        ret+=start+"#";
        ArrayList<String> temp = new ArrayList<>(accept);
        Collections.sort(temp,c);
        for (String state :
                temp) {
            ret=ret+state+";";
        }
        ret = ret.substring(0, ret.length()-1);
        return ret;
    }

}


