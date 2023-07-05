package csen1002.main.task4;

import java.util.*;

/**
 * Write your info here
 * 
 * @name Ahmed Samir Elsayed Elghobashy
 * @id 46-17914
 * @labNumber 16
 */

public class CfgEpsUnitElim {

	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG. The string
	 *            representation follows the one in the task description
	 */

	List<Character> vars = new ArrayList<>();
    List<Character> terr = new ArrayList<>();
    HashMap<Character, TreeSet<String>> transitions;
    Character start='S';
	public CfgEpsUnitElim(String cfg) {
		// TODO Auto-generated constructor stub
        String[] sarr = cfg.split("#");
        getVars(sarr[0]);
        getTerminals(sarr[1]);
        transitions= getTransitions(sarr[2]);

	}

    private HashMap<Character, TreeSet<String>> getTransitions(String s) {
	    String[] transStrArr = s.split(";");
        HashMap<Character,TreeSet<String>> ret = new HashMap<>();
        for (String t :
                transStrArr) {
            String[] temp = t.substring(2).split(",");
            ret.put(t.charAt(0),new TreeSet<>());
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

//    public static void main(String[] args) {
//        CfgEpsUnitElim c = new CfgEpsUnitElim("S;R;X;A;C#d;l;n;o#S/Xo,nRXlX,nSoS;R/A,CX,X,e,lCS,lXXXo;X/A,AdRX,dCR,e;A/X,XR,e,nCd;C/CoAo,n,nR");
//        c.eliminateEpsilonRules();
//        c.eliminateUnitRules();
//        String ans="S;R;X;A;C#d;l;n;o#S/Xo,nRXl,nRXlX,nRl,nRlX,nSoS,nXl,nXlX,nl,nlX,o;R/Ad,AdR,AdRX,AdX,CX,CoAo,Coo,XR,d,dC,dCR,dR,dRX,dX,lCS,lXXXo,lXXo,lXo,lo,n,nCd,nR;X/Ad,AdR,AdRX,AdX,CX,CoAo,Coo,XR,d,dC,dCR,dR,dRX,dX,lCS,lXXXo,lXXo,lXo,lo,n,nCd,nR;A/Ad,AdR,AdRX,AdX,CX,CoAo,Coo,XR,d,dC,dCR,dR,dRX,dX,lCS,lXXXo,lXXo,lXo,lo,n,nCd,nR;C/CoAo,Coo,n,nR";
//        System.out.println(c);
//        System.out.println(c.toString().equals(ans));
//
//
//
//        int x=0;
//    }
    /**
	 * @return Returns a formatted string representation of the CFG. The string
	 *         representation follows the one in the task description
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
//        vars.remove('S');
        String ret ="S;";
        for (char c :
                vars) {
            if(c=='S')continue;
            ret=ret+c+";";
        }
        ret=ret.substring(0,ret.length()-1)+"#";
        for (char c :
                terr) {
            ret=ret+c+";";
        }

        ret=ret.substring(0,ret.length()-1)+"#";
        ret+="S/";

        for (String str:
             transitions.get('S')) {
            ret+=str+",";
        }
        ret=ret.substring(0,ret.length()-1);
        ret+=";";
        for (char c:
             vars) {
            if(c=='S')continue;
            ret+=c+"/";
            for (String str:
                    transitions.get(c)) {
                ret+=str+",";
            }
            ret=ret.substring(0,ret.length()-1);
            ret+=";";
        }
        ret=ret.substring(0,ret.length()-1);
		return ret;
	}

	/**
	 * Eliminates Epsilon Rules from the grammar
	 */
	public void eliminateEpsilonRules() {
		// TODO Auto-generated method stub
        Set<Character> eliminated = new HashSet<>();
        while(epsilonExists())
        {
            for (Character c :
                    transitions.keySet()) {
                if(c=='S')
                    continue;
                Set<String> tran = transitions.get(c);
                boolean flag=false;
                Set<String> remove = new HashSet<>();
                for(String transition : tran) {
//                    String transition=tran.get(i);
                    if (transition.equals("e"))
                    {
                        flag=true;
                        replaceEpsilon(c,eliminated);
                        eliminated.add(c);
                        remove.add(transition);
                    }
                }
                if(flag)
                    tran.addAll(selfEps(tran,c,eliminated));
                tran.removeAll(remove);

            }
//            removeEpsilonFromEliminated(eliminated);
        }
//        removeDuplicates();

	}

    private Set<String> selfEps(Set<String> tran, Character c, Set<Character> eliminated) {
	    Set<String>  ret = new HashSet<>();
        for (String t:
             tran) {
            ret.addAll(getPerms(t,c,eliminated));
        }

        return ret;
    }

//    private void removeEpsilonFromEliminated(Set<Character> eliminated) {
//        for (char c :
//                transitions.keySet()) {
//            List<String> tr = transitions.get(c);
//            for (int i = 0; i <tr.size(); i++) {
//                if(tr.get(i).equals("e")  &&eliminated.contains(c)) {
//                    tr.remove(i);
//                    i--;
//                }
//            }
//        }
//    }

//    private void removeDuplicates() {
//        for (List<String> list : transitions.values()) {
//            Set<String> uniqueSet = new LinkedHashSet<>(list);
//            List<String> uniqueList = new ArrayList<>(uniqueSet);
//            list.clear();
//            list.addAll(uniqueList);
//        }
//    }

    private void replaceEpsilon(Character c,Set<Character> eliminated) {
        for (Character ch :
                transitions.keySet()){
            if(ch==c)
                continue;
            Set<String> newTrans = new HashSet<>();
            Set<String> old = new HashSet<>();
            for (String transiton:
                 transitions.get(ch)) {
                if(transiton.contains(c+""))
                {
                    old.add(transiton);
                    newTrans.addAll(getPerms(transiton,c,eliminated));
                }
            }
            if(newTrans.contains("e") &&  eliminated.contains(ch))
                newTrans.remove("e");
//            transitions.get(ch).removeAll(old);
            transitions.get(ch).addAll(newTrans);
        }
    }

    private List<String> getPerms(String transiton, Character c,Set<Character> eliminated) {
	    List<String> ret= new ArrayList<>();
	    perm(ret,transiton,c,0,"",eliminated);
        return ret;
    }

    private void perm(List<String> ret, String transiton, Character c,int i,String curr,Set<Character> eliminated) {
	    if(i==transiton.length())
        {
            if(curr.length()>=1)
                ret.add(curr);
            else if(!eliminated.contains(c))
                ret.add("e");
            return;
        }

	    char currCh = transiton.charAt(i);
	    if(currCh==c)
	        perm(ret,transiton,c,i+1,curr,eliminated);
        perm(ret,transiton,c,i+1,curr+currCh,eliminated);
    }

    private boolean epsilonExists() {
        for (Character c :
                transitions.keySet()) {
            if(c=='S')
                continue;
            for (String transition:
                    transitions.get(c)) {
                if(transition.equals("e"))
                    return true;
            }
        }
        return false;
    }

    /**
	 * Eliminates Unit Rules from the grammar
	 */
	public void eliminateUnitRules() {
		// TODO Auto-generated method stub
        HashMap<Character,HashSet<String>> prev = new HashMap<>();
        for (char c :
                transitions.keySet()) {
            prev.put(c,new HashSet<>(transitions.get(c)));
        }

        while(unitExists())
        {
            for (Character c :
                    transitions.keySet()) {
              Set<String> l =transitions.get(c);

                    Set<String> toRemove = new HashSet<>();
                    Set<String> toAdd = new HashSet<>();
                    for (String transition :l) {

                        if(transition.length()==1 && vars.contains(transition.charAt(0)))
                        {
                            char toReplace =transition.charAt(0);
                            toRemove.add(transition);
                            Set<String> toReplaceTransitions = transitions.get(toReplace);
                            for (String newT :
                                    toReplaceTransitions) {
                                if (!prev.get(c).contains(newT))
                                    toAdd.add(newT);
                            }
                        }
                    }
              l.removeAll(toRemove);
              l.addAll(toAdd);
              prev.get(c).addAll(toAdd);
            }
        }
	}

    private boolean unitExists() {
        for (Set<String> t :
                transitions.values()) {
            for (String transition :
                    t) {
                if(transition.length()==1 && vars.contains(transition.charAt(0)))
                    return true;
            }
        }
        return false;
    }

}
