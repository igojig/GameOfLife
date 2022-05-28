package life;

public class Statistics {
    // поколение ячейки
    int generationCount=-1;

    // кл-во перерождений, т.е ск. раз клетка станивиласб живой
   int reborn=0;

    Statistics(){

    }

    Statistics(Statistics stat){
        this.generationCount= stat.generationCount;
        this.reborn=stat.reborn;
    }

    void reset(){
        generationCount=-1;
        reborn=0;
    }
}
