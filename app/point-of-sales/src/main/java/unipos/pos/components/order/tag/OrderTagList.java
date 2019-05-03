package unipos.pos.components.order.tag;


import java.util.ArrayList;

/**
 * Created by ggradnig on 18.01.2017.
 */
public class OrderTagList extends ArrayList<OrderTag> {

    //Todo: those two methods are only used mutually, reduce to one!!

    public boolean containsKey(String key){
        for(OrderTag tag : this){
            if(tag.getKey().equals(key))
                return true;
        }
        return false;
    }

    public OrderTag getByKey(String key){
        return this.stream().filter(x -> x.getKey().equals(key)).findFirst().get();
    }
}
