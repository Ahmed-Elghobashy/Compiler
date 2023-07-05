package csen1002.main.task5;

import java.util.*;

/**
 * Write your info here
 * 
 * @name Ahmed Elghobashy
 * @id 46-17914
 * @labNumber 16
 */

public class CfgLeftRecElim {

	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG. The string
	 *            representation follows the one in the task description
	 */
    List<Character> vars = new ArrayList<>();
    List<Character> varDash = new ArrayList<>();
    List<Character> terr = new ArrayList<>();
    LinkedHashMap<Character, List<String>> transitions;
    LinkedHashMap<Character, List<String>> transitionDash;
	public CfgLeftRecElim(String cfg) {
		// TODO Auto-generated constructor stub
        String[] sarr = cfg.split("#");
        getVars(sarr[0]);
        getTerminals(sarr[1]);
        transitions= getTransitions(sarr[2]);
        transitionDash=new LinkedHashMap<>();
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

	/**
	 * @return Returns a formatted string representation of the CFG. The string
	 *         representation follows the one in the task description
	 */
	@Override
    public String toString() {
        // TODO Auto-generated method stub
        String ret ="S;";
        for (char c :
                vars) {
            if(c=='S')continue;
            ret=ret+c+";";
        }
        for (char c :
                varDash) {
            ret=ret+c+"'"+";";
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
        for (char c:
                varDash) {
            ret+=c+"'"+"/";
            for (String str:
                    transitionDash.get(c)) {
                ret+=str+",";
            }
            ret=ret.substring(0,ret.length()-1);
            ret+=";";
        }
        ret=ret.substring(0,ret.length()-1);
        return ret;
    }

	/**
	 * Eliminates Left Recursion from the grammar
	 */
	public void eliminateLeftRecursion() {
		// TODO Auto-generated method stub
        Set<Character> elem = new HashSet<>();
        for (Character c:
             transitions.keySet()) {

            while(hasLeftOld(c,elem))
                subWithLeft(c,elem);
            if(hasLeftRecursion(c))
            {
                eliminateLeftRecursionFromVar(c,elem);
            }
            elem.add(c);

        }


	}

    private boolean hasLeftOld(Character c, Set<Character> elem) {
        List<String> trans = transitions.get(c);

        for (String t :transitions.get(c)) {
            if(elem.contains(t.charAt(0)))
                return true;
        }

        return false;
	}

    private void subWithLeft(Character c, Set<Character> elem) {
	    List<String> trans = transitions.get(c);

        for (int i=0;i<trans.size();i++) {
            String t = trans.get(i);
            if(elem.contains(t.charAt(0)))
            {
                List<String> elemTrans = transitions.get(t.charAt(0));
                List<String> toAdd = new ArrayList<>();
                for(String curr:elemTrans)
                {
                    toAdd.add(curr+t.substring(1));
                }
                trans.remove(i);
                trans.addAll(i,toAdd);
            }

        }

    }

    private void eliminateLeftRecursionFromVar(Character c, Set<Character> elem) {

	    List<String> trans = transitions.get(c);
        List<String> betas = identifyBetas(c,trans);
        List<String> alphas = identifyAlphas(c,trans);
        varDash.add(c);
        newTransWithoutLeftRec(trans,betas,alphas,c);
        createDashTransitions(c,betas,alphas);


    }

    private void createDashTransitions(Character c, List<String> betas, List<String> alphas) {
        List<String> trans = new ArrayList<>();
        for (String alpha :
                alphas) {
            trans.add(alpha + c + "'");
        }
        trans.add("e");
        transitionDash.put(c,trans);
    }

    private void newTransWithoutLeftRec(List<String> trans, List<String> betas, List<String> alphas, Character c) {
	    trans.clear();
        for (String beta :
                betas) {
            trans.add(beta+c+"'");
        }
    }

    private List<String> identifyAlphas(Character c, List<String> trans) {
        List<String> alphas = new ArrayList<>();
        for (String tr :
                trans) {
            if(tr.charAt(0)==c)
                alphas.add(tr.substring(1));
        }
        return alphas;
    }

    private List<String> identifyBetas(Character c, List<String> trans) {
        List<String> betas = new ArrayList<>();
        for (String tr :
                trans) {
            if(tr.charAt(0)!=c)
                betas.add(tr);
        }
        return betas;
    }

    private boolean hasLeftRecursion(Character c) {
        List<String> trans = transitions.get(c);
        for (String t :
                trans) {
            if(t.charAt(0)==c)
                return true;
        }

        return false;
    }

    public static void main(String[] args) {
        CfgLeftRecElim cfgLeftRecElim = new CfgLeftRecElim("S;Z;G;P;X;H;R#l;m;p;x#S/mG,pHSP;Z/PpRlP,ZH,Zl,lP,mRRH;G/HX,PXZ,PmZmX,Rm,xP;P/XHpZx,pRH;X/RxZG,ZlGx,ZxH,mX,pSRHS;H/HRXP,HZ,mHP,mPPHR,x;R/GZRX,HGP,lSZS,lZSm,pGxG,pS");
        cfgLeftRecElim.eliminateLeftRecursion();
        System.out.println(cfgLeftRecElim);

    }

}
