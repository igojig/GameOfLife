package life;

import java.util.*;


public class Check {

    LinkedList<Integer> list = new LinkedList<>();

    void add(int count) {
        if (list.size() >= Game.MAX_ELEMENTS)
            list.poll();
        list.add(count);
    }

    Integer get() {
        return list.getFirst();
    }

    OptionalInt test() {

        OptionalInt optionalInt=OptionalInt.empty();

        if(list.size()<Game.MAX_ELEMENTS) {
            return OptionalInt.empty();
        }

        int len = 7;

        while(len>=2){
            if(check(len)){
                optionalInt=OptionalInt.of(len);
                break;
            }
            --len;
        }

        return optionalInt;
    }

    boolean check(int len){

        int cycle=1;
        boolean flag=true;

        while (len * (cycle + 1) <= Game.MAX_ELEMENTS) {
            int from = (cycle - 1) * len;
            int to = cycle * len;
            int next_from = cycle * len;
            int next_to = (cycle + 1) * len;

            if(!compare(from, to, next_from, next_to)){
                flag=false;
                break;
            }
            cycle++;
        }
        return  flag;
    }


    boolean compare(int from, int to, int next_from, int next_to) {
        boolean flag = true;
        for (int i = 0; i < to - from; i++) {
//            if (list.get(from + i) != list.get(next_from + i)) {
            if (!Objects.equals(list.get(from + i), list.get(next_from + i))) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    @Override
    public String toString() {
        return "Check{" +
                "list=" + list +
                '}';
    }

}
