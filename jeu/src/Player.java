public class Player {
    private String current;
    private String opponent;

    public Player(char i){
        if(i=='1'){
            this.current="X"; //la premiere valeur de la string venu du serveur est le choix de notre champ ( soit rouge, soit noir)
            this.opponent="O";
        }else{
            this.current="O"; //la premiere valeur de la string venu du serveur est le choix de notre champ ( soit rouge, soit noir)
            this.opponent="X";
        }

    }

    public String getCurrent() {
        return current;
    }

    public String getOpponent() {
        return opponent;
    }

    public String getNonValue(String c){
        if(c.equals("X")){
            return "O";
        }else{
            return "X";
        }

    }
}