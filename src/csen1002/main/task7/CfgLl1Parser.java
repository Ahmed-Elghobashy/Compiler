package csen1002.main.task7;

import java.util.*;

/**
 * Write your info here
 * 
 * @name Ahmed Elghobashy
 * @id 46-17914
 * @labNumber 16
 */

public class CfgLl1Parser {

    List<Character> vars = new ArrayList<>();
    List<Character> terr = new ArrayList<>();
    LinkedHashMap<Character, List<String>> transitions;
    LinkedHashMap<Character,TreeSet<String>> first =new LinkedHashMap<>();
    LinkedHashMap<Character,TreeSet<String>> follow =new LinkedHashMap<>();


    /**
	 *Constructs a Context Free Grammar
	 * 
	 *formatted string representation of the CFG, the First sets of
	 *            each right-hand side, and the Follow sets of each variable. The
	 *            string representation follows the one in the task description
	 */

	public CfgLl1Parser(String input) {
		// TODO Auto-generated constructor stub
        String[] sarr = input.split("#");
        getVars(sarr[0]);
        getTerminals(sarr[1]);
        transitions= getTransitions(sarr[2]);
        String[] firstArr = sarr[3].split(";");
        String[] followArr = sarr[4].split(";");
        for (String s:
             firstArr) {
            getFirst(s);
        }
        for (String s:
                followArr) {
            getFollow(s);
        }
    }

    private void getFollow(String s) {
	    String[] sarr = s.split("/");
	    Character var = sarr[0].charAt(0);
	    String[] follows = sarr[1].split("");
        for (String str :
                follows) {
            follow.putIfAbsent(var,new TreeSet<>());
            if(str.length()>1)
            {
                for (Character c :
                        str.toCharArray()) {
                    follow.get(var).add(c+"");
                }
            }
            follow.get(var).add(str);
        }
    }

    private void getFirst(String s) {
        String[] sarr = s.split("/");
        Character var = sarr[0].charAt(0);
        String[] firsts = sarr[1].split(",");
        for (String str :
                firsts) {
            first.putIfAbsent(var,new TreeSet<>());
            if(str.length()>1)
            {
                for (Character c :
                        str.toCharArray()) {
                    first.get(var).add(c+"");
                }
            }
            else
                first.get(var).add(str);
        }
    }

    /**
	 * @param input The string to be parsed by the LL(1) CFG.
	 * 
	 * @return A string encoding a left-most derivation.
	 */
	public String parse(String input) {
		// TODO Auto-generated method stub
        // construct parse table
        HashMap<Character,HashMap<Character,String>> parseTable = new HashMap<>();
        for (int i = 0; i <vars.size(); i++) {
            Character var = vars.get(i);
            List<String> varTrans = transitions.get(var);

            for (int j = 0; j <terr.size(); j++) {
                Character terminal = terr.get(j);
               fillParseTableCell(parseTable,varTrans,var,terminal,i,j);
            }
            Character terminal = '$';
            fillParseTableCell(parseTable,varTrans,var,terminal,i,terr.size()+1);

        }

        Stack<Character> stack = new Stack<>();
        stack.push('S');
        int pointer  = 0;
        input=input+"$";
        List<String> retlist = new ArrayList<>();
        String pre="";
        String post="";
        retlist.add("S");
        while(pointer<input.length())
        {

            char currChar = input.charAt(pointer);
            if(stack.isEmpty()) {
                if(currChar=='$')
                    break;
                else
                {
                    retlist.add("ERROR");
                    break;
                }
            }
            char topStack = stack.peek();


            if(Character.isLowerCase(topStack))
            {
                if(currChar==topStack)
                {
                    pre+=stack.pop();
                    pointer++;
                    continue;
                }
                else {
                    retlist.add("ERROR");
                    break;
                }
            }

            HashMap<Character, String> t = parseTable.get(topStack);
            String next = t.get(currChar);

            if (next==null)
            {
                retlist.add("ERROR");
                break;
            }
            if(next.equals("e")) {
                stack.pop();
                String toAdd = getToAdd(stack);
                retlist.add(pre+toAdd);
                continue;
            }
            stack.pop();
            for (int i =next.length()-1; i>=0 ; i--) {
                stack.add(next.charAt(i));
            }
            String toAdd = getToAdd(stack);
            retlist.add(pre+toAdd);
        }

		return String.join(";", retlist);

	}

    private String getToAdd(Stack<Character> stack) {
        String ret = "";
        Stack<Character> tempStack = new Stack<>();

        // Transfer elements from original stack to temp stack
        while (!stack.isEmpty()) {
            tempStack.push(stack.pop());
        }

        // Build resulting string and restore original stack
        while (!tempStack.isEmpty()) {
            char c = tempStack.peek();
            ret = c+ret;
            stack.push(tempStack.pop());
        }

        return ret;
    }


    public void fillParseTableCell(HashMap<Character, HashMap<Character, String>> parseTable, List<String> varTrans, Character var, Character terminal, int i, int j)
    {
        String entry= null;

        for (String tran :
                varTrans) {
            TreeSet<String> tt = follow.getOrDefault(var, new TreeSet<>());
            if (tran.charAt(0) == terminal||expressionHasFirst(tran,terminal) || allHasFirstEpsilon(tran) && tt.contains(terminal+""))
                entry = tran;

        }
        if(entry!=null) {
            parseTable.putIfAbsent(var,new HashMap());
            parseTable.get(var).put(terminal, entry);
        }

    }

    private boolean expressionHasFirst(String tran, Character terminal) {
        for (int j = 0; j < tran.length(); j++) {
            char curr =tran.charAt(j);
            if(curr==terminal)return true;
            if(Character.isLowerCase(curr)) return false;
            if(first.get(curr).contains(terminal+"")) return true;
            if (!first.get(curr).contains("e")) return false;
        }
        return false;
    }


    private boolean allHasFirstEpsilon(String tran) {
        if (tran.equals("e")) return true;
        for (int i = 0; i < tran.length(); i++) {
            char c=tran.charAt(i);
            if (first.containsKey(c) && first.get(c).contains("e"))
                continue;
            else
                return false;
        }
        return true;

    }

    /**
     * Calculates the Follow Set of each variable in the CFG.
     *
     * @return A string representation of the Follow of each variable in the CFG,
     *         formatted as specified in the task description.
     */


    private LinkedHashMap<Character, List<String>> getTransitions(String s) {
        String[] transStrArr = s.split(";");
        LinkedHashMap<Character,List<String>> ret = new LinkedHashMap<>();
        for (String t :
                transStrArr) {
            String[] temp = t.substring(2).split(",");
            ret.put(t.charAt(0),new ArrayList<>());
            ret.get(t.charAt(0)).addAll(Arrays.asList(temp));
        }
        return ret;
    }


    private void getTerminals(String s) {
        String[] terArr = s.split(";");
        for (String v :
                terArr) {
            terr.add(v.charAt(0));
        }
    }

    private void getVars(String s) {
        String[] varArr = s.split(";");
        for (String v :
                varArr) {
            vars.add(v.charAt(0));
        }
    }

}
