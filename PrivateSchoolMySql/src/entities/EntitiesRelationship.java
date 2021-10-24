package entities;

import java.util.ArrayList;
import java.util.Collections;

/*auth h klash anaparista tis sxeseis metaxy twn diaforwn ontothtwn se epipedo id ths morfhs entitiesId  -> polla ids*/
public class EntitiesRelationship {

    private int rowId;// to rowId einai o auxwn arithmos ths listas
    private int entitieId;//einai to id ths mias ontothtas thn opoia thelw na enwsw me polles p.x to id tou Course deixnei se pollous Students
    private ArrayList<Integer> ids;//einai ta ids (polla) pou sindeontai me thn mia ontothta id gia paradeigma ta ids twn Student pou anhkoun sto mathima me to sygkekrimeno entitie id

    public EntitiesRelationship(int entitieId, int id) {
        this.entitieId = entitieId;
        this.ids = new ArrayList<>();
        addListIds(id);
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getEntitieId() {
        return entitieId;
    }

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public void addListIds(int id) {
        ids.add(id);
        Collections.sort(ids);//sortarisma ths lista twn ids meta apo prosthiki neou id
    }

    @Override
    public String toString() {
        return "EntitiesRelationship{" + "rowId=" + rowId + ", entitieId=" + entitieId + ", ids=" + ids + '}';
    }
}
