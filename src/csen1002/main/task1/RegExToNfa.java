package csen1002.main.task1;

import java.util.*;

/**
 * Write your info here
 * 
 * @name ahmed samir elsayed elghobashy
 * @id 46-17914
 * @labNumber 16
 */

public class RegExToNfa {

	/**
	 * Constructs an csen1002.main.task1.RegExToNfa.NFA corresponding to a regular expression based on Thompson's
	 * construction
	 * 
	 * @param input The alphabet and the regular expression in postfix notation for
	 *              which the csen1002.main.task1.RegExToNfa.NFA is to be constructed
	 */
	NFA ret;
    TreeSet<Character> alphabet;
	public RegExToNfa(String input) {
		// TODO Auto-generated constructor stub
        String[] arr = input.split("#");
        alphabet = createAlphabet(arr[0]);
        String regex = arr[1];
        NFA nfa = convertToNFA(regex,alphabet);
        ret=nfa;
	}

    private NFA convertToNFA(String regex, Set<Character> alphabet) {
        Stack<NFA> stack = new Stack<>();
        int n=0;
        for (int i = 0; i <regex.length(); i++) {
            char curr = regex.charAt(i);
            if(alphabet.contains(curr) || curr=='e')
            {
                NFA temp = new NFA(curr,n);
                stack.push(temp);
                n= temp.largest+1;
            }
            else{
                if(curr=='*')
                {
                    NFA nfa1 = stack.pop();
                    NFA  nfaPush = applyStar(nfa1,n);
                    stack.push(nfaPush);
                    n=nfaPush.largest+1;
                }
                else if(curr=='|')
                {
                    NFA nfa1 = stack.pop();
                    NFA nfa2 = stack.pop();
                    NFA nfaPush =applyUnion(nfa1,nfa2,n);
                    stack.push(nfaPush);
                    n=nfaPush.largest+1;
                }
                else if(curr=='.')
                {
                    NFA nfa1 = stack.pop();
                    NFA nfa2 = stack.pop();
                    NFA nfaPush =applyConcat(nfa1,nfa2,n);
                    stack.push(nfaPush);
                }
            }
        }
        return stack.pop();
    }

    private NFA applyStar(NFA nfa1, int n) {
	    int newStart = n++;
	    int newAccept = n;
        nfa1.transitions.add(new Object[]{newStart, 'e', nfa1.start});
        nfa1.transitions.add(new Object[]{nfa1.accept, 'e', newAccept});
        nfa1.transitions.add(new Object[]{newStart, 'e', newAccept});
        nfa1.transitions.add(new Object[]{nfa1.accept, 'e', nfa1.start});
        nfa1.states.add(newStart);
        nfa1.states.add(newAccept);
        nfa1.accept=newAccept;
        nfa1.start=newStart;
        nfa1.largest=n;

        return nfa1;
    }

    private NFA applyConcat(NFA nfa2, NFA nfa1, int n) {
	    NFA nfaconcat = new NFA();
	    nfaconcat.states.addAll(nfa1.states);
        nfaconcat.states.addAll(nfa2.states);
        nfaconcat.states.remove(nfa2.start);
        nfaconcat.transitions.addAll(nfa1.transitions);
        for (Object[] transition :
                nfa2.transitions) {
            if( ((int)transition[0])== nfa2.start)
            {
                nfaconcat.transitions.add(new Object[]{nfa1.accept,transition[1],transition[2]});
            }
           else if( ((int) transition[2])== nfa2.start)
            {
                nfaconcat.transitions.add(new Object[]{transition[0],transition[1],nfa1.accept});
            }
           else
            nfaconcat.transitions.add(transition);
        }
        nfaconcat.accept= nfa2.accept;
        nfaconcat.start=nfa1.start;
        nfaconcat.largest= Math.max(nfa2.largest, nfa1.largest);
        return nfaconcat;
    }

    private NFA applyUnion(NFA nfa1, NFA nfa2,int n) {
	    NFA ret = new NFA();
	    ret.states.addAll(nfa1.states);
        ret.states.addAll(nfa2.states);
        int start =n++;
        int accept=n;
        ret.start=start;
        ret.accept=accept;
        ret.largest=n;
        ret.transitions.add(new Object[]{start,'e',nfa1.start});
        ret.transitions.add(new Object[]{start,'e',nfa2.start});
        ret.transitions.add(new Object[]{nfa1.accept,'e',accept});
        ret.transitions.add(new Object[]{nfa2.accept,'e',accept});
        ret.transitions.addAll(nfa1.transitions);
        ret.transitions.addAll(nfa2.transitions);
        ret.states.addAll(Arrays.asList(start,accept));
        return ret;
    }


    private TreeSet<Character> createAlphabet(String s) {
	    String[] arr = s.split(";");
        TreeSet<Character> ret = new TreeSet();
        for (int i = 0; i < arr.length; i++)
            ret.add(arr[i].charAt(0));
	    return ret;
    }

    /**
	 * @return Returns a formatted string representation of the csen1002.main.task1.RegExToNfa.NFA. The string
	 *         representation follows the one in the task description
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
        List<Object[]> transations = ret.transitions;
        TreeSet<Integer> states = ret.states;

        Comparator<Object[]> comparator = new Comparator<Object[]>() {
            @Override
            public int compare(Object[] o1, Object[] o2) {
                Integer i1 = (Integer) o1[0];
                Integer i2 = (Integer) o2[0];
                Character c1 = (Character) o1[1];
                Character c2 = (Character) o2[1];
                Integer e1 = (Integer) o1[2];
                Integer e2 = (Integer) o2[2];

                if(i1.compareTo(i2)!=0) return i1.compareTo(i2);
                else if(c1.compareTo(c2)!=0) return c1.compareTo(c2);
                else return e1.compareTo(e2);
            }
        };
        String retstr = "";
        Collections.sort(transations,comparator);
        //states
        for (Integer i : states) {
            retstr=retstr+i+";";
        }
        retstr = retstr.substring(0, retstr.length() - 1)+"#";
        //alpha
        for (char i : alphabet) {
            retstr=retstr+i+";";
        }
        retstr = retstr.substring(0, retstr.length() - 1)+"#";
        //trans
        for (Object[] t : transations) {
            String s =t[0]+","+t[1]+","+t[2];
            retstr = retstr+s+";";
        }
        retstr = retstr.substring(0, retstr.length() - 1)+"#";
        retstr=retstr+ret.start+"#"+ret.accept;


        return retstr;
	}

    public static void main(String[] args) {
        RegExToNfa r = new RegExToNfa("a;o;z#za|*o.");
        System.out.println(r.toString().equals("0;1;2;3;4;5;6;7;9#a;o;z#0,z,1;1,e,5;2,a,3;3,e,5;4,e,0;4,e,2;5,e,4;5,e,7;6,e,4;6,e,7;7,o,9#6#9"));

    }
}

class NFA{
    TreeSet<Integer> states;
    List<Object[]> transitions;
    int start;
    int accept;
    int largest;
    public NFA(char c,int n)
    {
        states  = new TreeSet();
        transitions=new ArrayList<>();
        int first = n;
        n++;
        int second=n;
        start=first;
        accept=second;
        states.add(first);states.add(second);
        transitions.add(new Object[]{first,c,second});
        largest=n;
    }

    public NFA()
    {
        states  = new TreeSet();
        transitions=new ArrayList<>();

    }
}
