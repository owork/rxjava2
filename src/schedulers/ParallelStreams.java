package schedulers;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

public class ParallelStreams {
    public static void main(String[] args) {

        List<Beer> beers = loadCellar();  // populate the beer collection

        Observable<Beer> observableBeers = Observable.fromIterable(beers);

        observableBeers
                .flatMap(beer -> Observable.just(beer)
                           .subscribeOn(Schedulers.computation())  // replace computation() with newThread()
                           .map(beeer -> matureBeer(beeer))
                 )
                .subscribe(beer -> System.out.println("Subscriber got " +
                               beer.name + " on  " +
                               Thread.currentThread().getName())
                );


        // Just to keep the program running
        try {
            Thread.sleep(5000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    // Populate beer collection
    static List<Beer> loadCellar() {
        List<Beer> beerStock = new ArrayList<>();

        beerStock.add(new Beer("Stella", "Belgium", 7.75f));
        beerStock.add(new Beer("Sam Adams", "USA", 7.00f));
        beerStock.add(new Beer("Obolon", "Ukraine", 4.00f));
        beerStock.add(new Beer("Bud Light", "USA", 5.00f));
        beerStock.add(new Beer("Yuengling", "USA", 5.50f));
        beerStock.add(new Beer("Leffe Blonde", "Belgium", 8.75f));
        beerStock.add(new Beer("Chimay Blue", "Belgium", 10.00f));
        beerStock.add(new Beer("Brooklyn Lager", "USA", 8.25f));

        return beerStock;
    }

    private static Beer matureBeer(Beer beer){
        try {
            System.out.println("** Maturing " + beer.name +
                    " on " + Thread.currentThread().getName());

            Thread.sleep((int)(Math.random()*500));
            return beer;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
