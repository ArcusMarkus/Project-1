package search.bestfirst;

import core.Duple;
import search.SearchNode;
import search.SearchQueue;

import java.util.HashMap;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.ToIntFunction;

public class BestFirstQueue<T> implements SearchQueue<T> {
    // Each object in the priority queue is an estimate paired with a SearchNode.
    private PriorityQueue<Duple<Integer,SearchNode<T>>> queue;

    // For each object encountered, this is the lowest total length estimate
    // encountered so far.
    private HashMap<T,Integer> lowestEstimateFor;

    // Use this heuristic to get the estimated distance to the goal node.
    private ToIntFunction<T> heuristic;

    public BestFirstQueue(ToIntFunction<T> heuristic) {
        // TODO: Initialize the three objects listed above.
        queue = new PriorityQueue<>((d1, d2) -> Integer.compare(d1.getFirst(), d2.getFirst()));
        lowestEstimateFor = new HashMap<>();
        this.heuristic = heuristic;

    }

    @Override
    public void enqueue(SearchNode<T> node) {

        int estimate = heuristic.applyAsInt(node.getValue());

        if (!lowestEstimateFor.containsKey(node.getValue()) || estimate < lowestEstimateFor.get(node.getValue())) {
            lowestEstimateFor.put(node.getValue(), estimate);
            queue.add(new Duple<>(estimate, node));
        }
        // TODO: Calculate the total distance estimate. Then
        //  add the node to the queue if there is no current estimate for the node
        //  or if the new estimate is lower than the lowest seen so far.
    }

    @Override
    public Optional<SearchNode<T>> dequeue() {
        // TODO: If the queue is empty, return Optional.empty(), otherwise, return
        //  whichever object is next from the priority queue.
        if (queue.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(queue.poll().getSecond());
        }
    }
}
