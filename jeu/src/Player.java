public class Player {
    private String current;
    private String oppenent;

    public Player(char i){
        if(i=='1'){
            this.current="O"; //la premiere valeur de la string venu du serveur est le choix de notre champ ( soit rouge, soit noir)
            this.oppenent="X";
        }else{
            this.current="X"; //la premiere valeur de la string venu du serveur est le choix de notre champ ( soit rouge, soit noir)
            this.oppenent="O";
        }

    }

    public String getCurrent() {
        return current;
    }

    public String getOppenent() {
        return oppenent;
    }

    public String getNonValue(String c){
        if(c.equals("X")){
            return "O";
        }else{
            return "X";
        }

    }
}