package de.KnollFrank.lib.settingssearch.common.graph;

public interface GraphTransformer<VSrc, ESrc, VDst, EDst> {

    record ContextOfInnerNode<ESrc, VDst>(ESrc edgeFromParentNodeToInnerNode,
                                          VDst transformedParentNode) {
    }

    VDst transformRootNode(VSrc rootNode);

    VDst transformInnerNode(VSrc innerNode, ContextOfInnerNode<ESrc, VDst> contextOfInnerNode);

    EDst transformEdge(ESrc edge, VDst transformedParentNode);
}
