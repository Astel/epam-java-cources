package com.epam.university.java.core.task012;

/**
 * Created by Александр on 14.09.2017.
 */
public class GraphFactoryImpl implements GraphFactory {
    /**
     * Create new Graph instance with designated amount of vertexes.
     *
     * @param vertexesCount vertexes count
     * @return new graph instance
     */
    @Override
    public Graph newInstance(int vertexesCount) {
        return new SimpleGraph();
    }
}
