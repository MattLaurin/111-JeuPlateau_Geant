import java.util.*;

public class plateau {
    private Map<String,String> mapPlateau = new LinkedHashMap<>();
    private String[] lettres={"A","B","C","D","E","F","G","H","I"};

    public plateau(){

        for(int j=9;j>=1;j--){

            for(int i=0;i< lettres.length;i++){
                mapPlateau.put(lettres[i]+j,lettres[i]+j);
            }
        }
    }
    public void play(){

    }
    public void undo(){

    }

    public void printPlateau(){
        List<String> keys = new ArrayList<String>(mapPlateau.keySet());
        int i = 1;
        for(String key : keys){
            String value = mapPlateau.get(key);
            System.out.print(value + "   ");
            if(i % 9 == 0){
                System.out.println();
            }
            i++;
        }
        System.out.println("-------------------------");
    }
}
