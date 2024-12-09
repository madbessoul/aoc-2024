package mad.day05;


import java.util.*;

public record RuleSet(List<Rule> rules) {

    private RuleSet ruleSubSet(List<Integer> update) {
        return new RuleSet(
                this.rules().stream().filter(rule -> update.contains(rule.x()) && update.contains(rule.y())).toList()
                );
    }

    //TODO : Turn the ruleset into a sequence of integers that satisfy all rules
    // Topological sort
    // Can't run it once on the full ruleset because the graph contains cycles (because why make things easy right?)
    private List<Integer> sequence(List<Integer> update) {

        var ruleSubset = ruleSubSet(update);
        // Graph
        // key : node
        // value : list of connected nodes
        Map<Integer, List<Integer>> graph = new HashMap<>();

        Set<Integer> nodes = new HashSet<>();
        Map<Integer, Integer> incomingEdges = new HashMap<>();

        // Graph initialization
        for (Rule rule: ruleSubset.rules()) {
            // Add nodes
            graph.putIfAbsent(rule.x(), new ArrayList<>());
            graph.putIfAbsent(rule.y(), new ArrayList<>());

            // Connect nodes based on individual rules
            graph.get(rule.x()).add(rule.y());

            // Add nodes to the set of nodes
            nodes.add(rule.x());
            nodes.add(rule.y());

            // Update incoming edges
            incomingEdges.merge(rule.y(), 1, Integer::sum);
            incomingEdges.putIfAbsent(rule.x(), 0);
        }

        // Queue to store nodes that are done
        Queue<Integer> queue = new LinkedList<>();

        // Init with easy nodes that have no incoming edges
        for (Integer node: nodes) {
            if (incomingEdges.get(node) == 0) {
                queue.offer(node);
            }
        }


        List<Integer> result = new ArrayList<>();
        while(!queue.isEmpty()) {
            int current = queue.poll();
            result.add(current);

            for (Integer adjNode: graph.getOrDefault(current, List.of())) {
                incomingEdges.merge(adjNode, -1, Integer::sum);
                if (incomingEdges.get(adjNode) == 0) {
                    queue.offer(adjNode);
                }
            }
        }

        if (result.size() != nodes.size()) {
            throw new IllegalStateException("Graph is not a DAG");
        }
        return result;
    }

    // Subset of the full sequence with only required values to validate the update
    public List<Integer> subsequence(List<Integer> update) {
        return sequence(update);
    }

    // If the update matches a subsequence from the ruleset, it should be valid
    public boolean validateUpdate(List<Integer> update) {
        return update.equals(subsequence(update));
    }
}
