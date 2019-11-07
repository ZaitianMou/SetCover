import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class sortBySubsetsLength implements Comparator<Subset>{
    @Override
    public int compare(Subset x,Subset y){

        if (x.subset.length>y.subset.length)
            return -1;
        else if (x.subset.length==y.subset.length)
            return 0;
        else return 1;
    }
}

public class SetCover {
    static ArrayList results=new ArrayList();
    static int num=0;

    public static void main(String[]args) {
        long timeStart=System.nanoTime();

        ArrayList subsetsSelected=new ArrayList();
        String fileName = "s-rg-40-20";
        File file = new File(fileName);
        try {
            Scanner fileRead = new Scanner(file);
            int sizeOfUniverse=Integer.parseInt(fileRead.nextLine());
            int sizeOfSubsets=Integer.parseInt(fileRead.nextLine());

            ArrayList<Subset> subsets=new ArrayList<>();
            int[] elementsOfUniverse=new int[sizeOfUniverse];

            //readInputLine
            for (int i=0;i<sizeOfSubsets;i++){
                String s=fileRead.nextLine();
                String[] s1=s.split(" ");
                if (s.equals("")) {
                    String[] s2={};
                    s1= s2;
                }
                int[] x=new int[s1.length];
                for (int j=0;j<s1.length;j++){
                    if (s.length()>0) {
                        x[j] = Integer.parseInt(s1[j]);
                        elementsOfUniverse[x[j]-1]++;
                    }

                }
                subsets.add(new Subset(x));
            }


            //find unique element
            //it seems doesn't work
            int numOfUniqueElement=0;
            for (int i=0;i<sizeOfUniverse;i++){
                if (elementsOfUniverse[i]==1){
                    numOfUniqueElement++;
                }
            }
            System.out.println(">>>>>>UNIQUE Element number: "+numOfUniqueElement);


            //find those subsets that contains only one or two elements
            //and check if elements in those subsets are already cover by other subsets

            for (int i=0;i<subsets.size();i++){
                switch (subsets.get(i).subset.length){
                    case 0:
                        subsets.remove(i);
                        i--;
                        break;
                    case 1:
                        if (elementsOfUniverse[subsets.get(i).subset[0]-1]>=2){
                            elementsOfUniverse[(subsets.get(i).subset[0])-1]--;
                            subsets.remove(i);
                            sizeOfSubsets--;
                            i--;
                        }
                        break;
                }
            }
            System.out.println("Size of Subsets after removing subsets that have only one elements " +
                    "and contained by other subsets: "+subsets.size());


            //obtain the minimum number of subsets needed
            ArrayList<Subset> tempSubsets = new ArrayList<>();
            for (int j = 0; j < subsets.size(); j++) {
                tempSubsets.add(subsets.get(j));
            }
            int tempSize = sizeOfUniverse;
            int count = 0;
            while (tempSize > 0) {
                int maxLength = 0;
                int indexOfMaxSize=0;
                for (int i=0; i < tempSubsets.size(); i++) {
                    if (tempSubsets.get(i).subset.length > maxLength) {
                        maxLength = tempSubsets.get(i).subset.length;
                        indexOfMaxSize=i;
                    }
                }
                tempSubsets.remove(indexOfMaxSize);
                tempSize -= maxLength;
                count++;
            }
            //Then count is the minimum size;


            //prune again, sort the subsets based on the length of each subset
            Collections.sort(subsets,new sortBySubsetsLength());

            //print all the subsets
            for (int i=0;i<subsets.size();i++) {
                System.out.println(subsets.get(i).returnTheSubset());
            }

            int[] elementsOfSubsetsSelected=new int[sizeOfUniverse];

            backtrack(subsets,subsetsSelected,elementsOfSubsetsSelected,sizeOfUniverse,0,count);

            //ending
            long timeEnd=System.nanoTime();
            System.out.println();
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(">>>>>>>Congratulation!>>>>>>>>>>>");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("Execution time period in seconds: "+(timeEnd-timeStart)/1000000000);
            System.out.println("Size of results are: "+results.size());
            System.out.println("Number of calling backtrack: "+num);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(">>>>>>Here are results: ");
            for(int i=0;i<results.size();i++){
                Subset temp=(Subset)results.get(i);
                System.out.println(">>Subset "+(i)+" is: "+temp.returnTheSubset());
            }

        }
        catch(FileNotFoundException e){
            System.out.println("！！Warning: FileNotFoundException");
        }
    }

    static void backtrack(ArrayList subsets,ArrayList subsetsSelected, int[] elementsOfSubsetsSelected, int sizeOfUniverse,int n,int minSize) {
        num++;
        //print subsetSelected
//        System.out.print("--"+results.size()+"-"+subsetsSelected.size());
//        for (int i = 0; i < subsetsSelected.size(); i++) {
//            System.out.print(((Subset) (subsetsSelected.get(i))).returnTheSubset());
//            System.out.print("  ");
//        }
//        System.out.println();

        if (isValid(subsetsSelected, elementsOfSubsetsSelected, sizeOfUniverse, minSize)) {

            //process solution
            if (results.size()==0) {
                //implement a deep copy
                for(int j=0;j<subsetsSelected.size();j++) {
                    results.add(subsetsSelected.get(j));
                }
            }
            else {
                //update the result
                System.out.println("Get a possible result of size "+results.size()+". Current subsetsSelected length is "+subsetsSelected.size());
                if (subsetsSelected.size() < results.size()) {
                    System.out.println("-Update the result");
                    //again, do a deep copy
                    results.clear();
                    for(int j=0;j<subsetsSelected.size();j++) {
                        results.add(subsetsSelected.get(j));
                    }
                }
            }

        }
        else{
            ArrayList<Subset> candidates= new ArrayList<>();
            candidates=constructCandidates(subsets,n,subsetsSelected,elementsOfSubsetsSelected);

            for(int i=0;i<candidates.size();i++){
                n++;
                subsetsSelected.add(candidates.get(i));
                for (int j=0;j<(candidates.get(i)).subset.length;j++) {
                    elementsOfSubsetsSelected[((Subset)(subsetsSelected.get(subsetsSelected.size()-1))).subset[j]-1]++;
                }

                if ((subsetsSelected.size()<results.size()) || (results.size()==0)) {
                    backtrack(subsets,subsetsSelected,elementsOfSubsetsSelected,sizeOfUniverse,n,minSize);
                }

                if (subsetsSelected.size()!=0) {
                    for (int j = 0; j < (((Subset) subsetsSelected.get(subsetsSelected.size() - 1))).subset.length; j++) {
                        elementsOfSubsetsSelected[((Subset) (subsetsSelected.get(subsetsSelected.size() - 1))).subset[j] - 1]--;
                    }
                    subsetsSelected.remove(subsetsSelected.size() - 1);
                }
            }
            n--;
        }
    }

    static ArrayList<Subset> constructCandidates(ArrayList subsets, int n, ArrayList subsetSelected,int[] elementsOfSubsetsSelected){

        ArrayList candidates= new ArrayList();
        if (subsetSelected.size()==0)
            return subsets;

        int lastIndex=subsets.indexOf(subsetSelected.get(subsetSelected.size()-1));

        for (int i=lastIndex+1;i<subsets.size();i++){

            //prune here, to check whether elements are ready covered by subsetsSelected
            boolean couldBeDiscard=true;
            for (int j=0;j<((Subset)subsets.get(i)).subset.length;j++){
                //System.out.println(((Subset)subsets.get(i)).subset[j]);
                if (elementsOfSubsetsSelected[((Subset)subsets.get(i)).subset[j]-1]<1){
                    couldBeDiscard=false;
                }
            }
            if (!couldBeDiscard)
                candidates.add(subsets.get(i));
        }
        return candidates;
    }

    //check whether the subsets of subset cover all the elements in the universe
    static boolean isValid(ArrayList subsetsSelected,int[]elementOfSubsetsSelected,int sizeOfUniverse,int minSize){
        //if the size of subsetsSelected is less than minSize, then it means obviously it's not sufficient.
        //Then i just return false, without doing the execution below.

        if (subsetsSelected.size()<minSize){
            return false;
        }

        //Then start
        boolean t=true;
        for (int i=0;i<elementOfSubsetsSelected.length;i++){

            if (elementOfSubsetsSelected[i]<1) t=false;
        }
        return t;
    }
}
class Subset{
    int [] subset;
    public Subset(int[] x){this.subset=x; }
    public String returnTheSubset(){
        String s="[";
        for (int i=0;i<this.subset.length;i++){
            s+=this.subset[i];
            s+=", ";
        }
        if (s.length()>2) s=s.substring(0,s.length()-2);
        s+="]";
        return s;
    }
}