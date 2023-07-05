package csen1002.main.task6;

import java.util.*;

/**
 * Write your info here
 * 
 * @name Ahmed Elghobashy
 * @id 46-17914
 * @labNumber 16
 */

public class CfgFirstFollow {

	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG. The string
	 *            representation follows the one in the task description
	 */
    List<Character> vars = new ArrayList<>();
    List<Character> terr = new ArrayList<>();
    LinkedHashMap<Character, List<String>> transitions;
    LinkedHashMap<Character,TreeSet<String>> first =new LinkedHashMap<>();
    LinkedHashMap<Character,TreeSet<String>> follow =new LinkedHashMap<>();

    public CfgFirstFollow(String cfg) {
		// TODO Auto-generated constructor stub
        String[] sarr = cfg.split("#");
        getVars(sarr[0]);
        getTerminals(sarr[1]);
        transitions= getTransitions(sarr[2]);

	}

	/**
	 * Calculates the First Set of each variable in the CFG.
	 * 
	 * @return A string representation of the First of each variable in the CFG,
	 *         formatted as specified in the task description.
	 */
	public String first() {
		// TODO Auto-generated method stub
        for (char c :
                transitions.keySet()) {
            first.put(c,new TreeSet());
        }
        boolean change =true;
        while (change)
        {
            change=false;
            for (char c :
                    transitions.keySet())
            {
                List<String> tran = transitions.get(c);

                for (String transStr :
                        tran) {
                    //the epsilon rule
                    if(allHasFirstEpsilon(transStr))
                        change = change || first.get(c).add("e");
                    //the second rule
                        change = change || addSecondRule(c,transStr);

                        int i=0;
                        while(i<transStr.length() && first.getOrDefault(transStr.charAt(i),new TreeSet<>()).contains("e"))
                        {
                            change = change || addSecondRule(c,transStr.substring(i+1));
                            i++;
                        }

                }
            }
        }
        String ret = "";
        for (char c :
                first.keySet()) {
            ret += c + "/";
            TreeSet<String> l =first.get(c);
            for (String t :
                    l) {
                ret+=t;
            }
            ret+=";";
        }

		return ret.substring(0,ret.length()-1);
	}

    private boolean addSecondRule(char c, String tran) {
	    boolean tillEps = true;
        boolean change = false;
        for (int i = 0; i <tran.length() && tillEps; i++) {
            char ch = tran.charAt(i);
            if (Character.isUpperCase(ch)) {
                TreeSet<String> tmp = first.get(ch);

                for (String ct :
                        tmp) {
                    if(ct.equals("e"))
                        continue;
                    change = false || first.get(c).add(ct);
                }
//                change = false || first.get(c).addAll(tmp);
                tillEps=first.get(ch).contains("e");
            }
            else {
                change = false || first.get(c).add(ch + "");
                break;
            }
        }

        return change;
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
	public String follow() {
		// TODO Auto-generated method stub
        first();
        for (char c :
                vars) {
            follow.put(c,new TreeSet());
        }
        follow.put('S',new TreeSet<>());
        follow.get('S').add("$");
        boolean change =true;
        while (change)
        {
            change=false;
            for (char c :
                    transitions.keySet())
            {
                List<String> tran = transitions.get(c);

                for (String transStr :
                        tran) {
                    change = change || computeFollows(c,transStr);

                }
            }

        }
        String ret = "";
        for (char c :
                follow.keySet()) {
            ret += c + "/";
            TreeSet<String> l =follow.get(c);
            for (String t :
                    l) {
                ret+=t;
            }
            ret+=";";
        }

        return ret.substring(0,ret.length()-1);
	}

    private boolean computeFollows(char c, String transStr) {
	    int i=0;
	    boolean change = false;
	    while (i<transStr.length())
        {
            char b = transStr.charAt(i);
            if(!Character.isUpperCase(b))
            {
                i++;
                continue;
            }
            String betas = transStr.substring(i+1);
            //first rule
            if(betas.length()>0) {
                char firstBeta = betas.charAt(0);
                if(!first.containsKey(firstBeta))
                {
                    change = change || follow.get(b).add(firstBeta+"");
                }else {
                    for (String str :
                            first.get(firstBeta)) {
                        if (str.equals("e"))
                            continue;
                        change = change || follow.get(b).add(str);
                    }
                }
            }
            //second rule
            if(allHasFirstEpsilon(betas))
            {
                change = change || follow.get(b).addAll(follow.get(c));
            }
            i++;
        }
	    return change;
    }


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

    public static void main(String[] args) {
        CfgFirstFollow cfgFirstFollow= new CfgFirstFollow("S;V;U;Z;Y;B#g;i;j;k;r;u;z#S/VkZY,Y,Yi,Z,iBuSY;V/VV,jB;U/BuV,UYY,V,YV,e,j;Z/S,kVz,kYBB;Y/e,gB,gBVVi;B/UVr,Y,ZSVi");
        System.out.println(cfgFirstFollow.first());


    }
}
