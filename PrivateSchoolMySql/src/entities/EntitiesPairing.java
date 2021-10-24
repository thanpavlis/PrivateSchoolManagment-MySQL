package entities;

/*auth h klash xrhsimopoieitai gia na prosomoiwsw to join apo thn 
bash kanontas pairing dio antikeimena analoga thn kathe priptwsh*/
public class EntitiesPairing<U, V> {

    public U mainEntity;
    public V refEntity;

    public EntitiesPairing(U first, V second) {
        this.mainEntity = first;
        this.refEntity = second;
    }
}
